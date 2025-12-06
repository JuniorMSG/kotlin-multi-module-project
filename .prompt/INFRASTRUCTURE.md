# 📋 프로젝트 인프라 문서 업데이트

## 🏗️ 전체 아키텍처

```
┌─────────────────────────────────────────────────────────────┐
│  로컬 개발 환경                                                │
│                                                               │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Docker Compose (Host)                               │   │
│  │                                                       │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │   │
│  │  │  MySQL   │  │  Redis   │  │  Kafka+Zookeeper │  │   │
│  │  │  :13306  │  │  :16379  │  │  :19092/:12181   │  │   │
│  │  └──────────┘  └──────────┘  └──────────────────┘  │   │
│  └──────────────────────────────────────────────────────┘   │
│                            ▲                                 │
│                            │ localhost 접근                   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Kubernetes (Rancher Desktop - Lima VM)             │   │
│  │                                                       │   │
│  │  ┌─────────────────────────────────────────────┐    │   │
│  │  │  hexagonal-payment Pod                      │    │   │
│  │  │  - hostNetwork: true                        │    │   │
│  │  │  - Port: 10001 (HTTP), 20002 (Debug)        │    │   │
│  │  └─────────────────────────────────────────────┘    │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

---

## 📁 업데이트된 프로젝트 구조

```
kotlin-multi-module-project/
├── .github/
├── .gradle/
├── .idea/
├── .kotlin/
├── build/
│
├── default-api/
├── default-batch/
├── default-consumer/
├── default-core/
├── default-producer/
├── exercise/
│
├── hexagonal-payment/                    ⭐ 주요 모듈
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/
│   │   │   │   └── com/ms/multi/
│   │   │   │       ├── adapter/
│   │   │   │       │   ├── in/web/      ← REST Controllers
│   │   │   │       │   └── out/         ← JPA Repositories, Redis
│   │   │   │       ├── application/     ← Use Cases
│   │   │   │       ├── domain/          ← Entities, Value Objects
│   │   │   │       └── config/          ← Spring Configuration
│   │   │   └── resources/
│   │   │       ├── application.yml       ← 기본 설정
│   │   │       ├── application-local.yml ← 로컬 개발
│   │   │       └── application-k8s.yml   ← K8s 환경
│   │   └── test/
│   ├── build/
│   │   └── libs/
│   │       └── hexagonal-payment-0.0.1-SNAPSHOT.jar
│   ├── build.gradle.kts
│   ├── Dockerfile                        ← 멀티스테이지 빌드
│   └── deploy.sh                         ← 배포 자동화
│
├── k8s/                                  ⭐ Kubernetes 매니페스트
│   └── hexagonal-payment/
│       ├── namespace.yaml                ← payment 네임스페이스
│       ├── configmap.yaml                ← 환경 설정
│       ├── secret.yaml                   ← 민감 정보
│       ├── deployment.yaml               ← Pod 배포 (hostNetwork: true)
│       ├── service.yaml                  ← ClusterIP Service
│       └── deploy.sh                     ← 전체 배포 스크립트
│
├── init-scripts/                         ⭐ DB 초기화
│   └── 01-init-hexagonal-payment.sql     ← MySQL 스키마/데이터
│
├── docker-compose.yml                    ⭐ 인프라 서비스
├── gradle/
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── build.gradle.kts
```

---

## 🐳 Docker Compose 구성

### 서비스 목록

| 서비스           | 이미지                             | 포트    | 용도          |
|---------------|---------------------------------|-------|-------------|
| **mysql**     | mysql:8.4                       | 13306 | 메인 데이터베이스   |
| **redis**     | redis:7.2-alpine                | 16379 | 캐시 & 세션     |
| **zookeeper** | confluentinc/cp-zookeeper:7.6.0 | 12181 | Kafka 코디네이터 |
| **kafka**     | confluentinc/cp-kafka:7.6.0     | 19092 | 메시지 브로커     |

### MySQL 설정

```yaml
mysql:
  environment:
    MYSQL_ROOT_PASSWORD: root
    MYSQL_DATABASE: module
    MYSQL_USER: admin
    MYSQL_PASSWORD: admin
  volumes:
    - mysql-data:/var/lib/mysql
    - ./init-scripts/01-init-hexagonal-payment.sql:/docker-entrypoint-initdb.d/
```

**초기화 스크립트:**

- `hexagonal_payment` 데이터베이스 생성
- `payment_user` 사용자 생성 (password: payment123)
- 필요한 테이블 자동 생성

### Redis 설정

```yaml
redis:
  command: redis-server --requirepass admin123
  ports:
    - "16379:6379"
```

---

## ☸️ Kubernetes 배포 구성

### Namespace

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: payment
```

### ConfigMap (환경 설정)

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: hexagonal-payment-config
  namespace: payment
data:
  SPRING_PROFILES_ACTIVE: "k8s"
  SPRING_DATASOURCE_URL: "jdbc:mysql://localhost:13306/hexagonal_payment?..."
  SPRING_DATASOURCE_USERNAME: "payment_user"
  SPRING_DATA_REDIS_HOST: "localhost"
  SPRING_DATA_REDIS_PORT: "16379"
  SERVER_PORT: "10001"
  JAVA_OPTS: "-Xms512m -Xmx1024m -XX:+UseG1GC"
```

### Secret (민감 정보)

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: hexagonal-payment-secret
  namespace: payment
type: Opaque
data:
  SPRING_DATASOURCE_PASSWORD: cGF5bWVudDEyMw==  # payment123
  SPRING_DATA_REDIS_PASSWORD: YWRtaW4xMjM=      # admin123
```

### Deployment (핵심 설정)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hexagonal-payment
  namespace: payment
spec:
  replicas: 1
  template:
    spec:
      hostNetwork: true                    # ⭐ Host 네트워크 사용
      dnsPolicy: ClusterFirstWithHostNet
      containers:
        - name: hexagonal-payment
          image: hexagonal-payment:1.0.0
          imagePullPolicy: Never           # 로컬 이미지 사용
          ports:
            - containerPort: 10001         # HTTP
            - containerPort: 20002          # Debug
          resources:
            requests:
              memory: "512Mi"
              cpu: "250m"
            limits:
              memory: "1Gi"
              cpu: "500m"
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: 10001
            initialDelaySeconds: 60
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: 10001
            initialDelaySeconds: 30
```

**주요 특징:**

- `hostNetwork: true` → localhost로 Docker 서비스 접근
- `imagePullPolicy: Never` → 로컬 빌드 이미지 사용
- Health Check → Spring Actuator 활용

### Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: hexagonal-payment
  namespace: payment
spec:
  type: ClusterIP
  selector:
    app: hexagonal-payment
  ports:
    - name: http
      port: 10001
      targetPort: 10001
    - name: debug
      port: 20002
      targetPort: 20002
```

---

## 🚀 배포 프로세스

### 1. 인프라 시작

```bash
# Docker Compose 서비스 시작
docker-compose up -d

# 상태 확인
docker-compose ps
docker-compose logs -f mysql redis
```

### 2. 애플리케이션 빌드

```bash
cd hexagonal-payment

# Gradle 빌드
./gradlew clean bootJar

# Docker 이미지 빌드
docker build -t hexagonal-payment:1.0.0 .

# 이미지 확인
docker images | grep hexagonal-payment
```

### 3. Kubernetes 배포

```bash
cd ../k8s/hexagonal-payment

# 전체 배포 (자동화 스크립트)
./deploy.sh

# 또는 수동 배포
kubectl apply -f namespace.yaml
kubectl apply -f secret.yaml
kubectl apply -f configmap.yaml
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

### 4. 배포 확인

```bash
# Pod 상태
kubectl get pods -n payment -w

# 로그 확인
kubectl logs -f -n payment -l app=hexagonal-payment

# 서비스 확인
kubectl get svc -n payment

# Health Check
curl http://localhost:10001/actuator/health
```

---

## 🔧 환경별 설정

### Local (IDE 실행)

```yaml
# application-local.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:13306/hexagonal_payment
  data:
    redis:
      host: localhost
      port: 16379
```

### K8s (컨테이너 실행)

```yaml
# application-k8s.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:13306/hexagonal_payment  # hostNetwork로 접근
  data:
    redis:
      host: localhost
      port: 16379
```

**동일한 localhost 사용 가능 (hostNetwork 덕분)**

---

## 🐛 디버깅

### IntelliJ Remote Debug 설정

1. **Run → Edit Configurations**
2. **Add New Configuration → Remote JVM Debug**
3. **설정:**
   ```
   Host: localhost
   Port: 20002
   ```
4. **Debug 모드로 실행**

### Pod 내부 접근

```bash
# Shell 접속
kubectl exec -it -n payment <pod-name> -- /bin/sh

# 환경 변수 확인
kubectl exec -n payment <pod-name> -- env | grep SPRING

# 로그 실시간 확인
kubectl logs -f -n payment <pod-name>
```

---

## 📊 리소스 사용량

### 권장 사양

| 항목         | 최소     | 권장     |
|------------|--------|--------|
| **CPU**    | 2 Core | 4 Core |
| **Memory** | 4 GB   | 8 GB   |
| **Disk**   | 20 GB  | 50 GB  |

### Rancher Desktop 설정

```
Memory: 6 GB
CPUs: 4
Disk: 40 GB
```

---

## 🔐 보안 고려사항

### Secret 관리

```bash
# Base64 인코딩
echo -n "payment123" | base64

# Secret 생성
kubectl create secret generic hexagonal-payment-secret \
  --from-literal=SPRING_DATASOURCE_PASSWORD=payment123 \
  --from-literal=SPRING_DATA_REDIS_PASSWORD=admin123 \
  -n payment
```

### 프로덕션 권장사항

- ❌ hostNetwork 사용 금지
- ✅ Managed Database 사용 (RDS, ElastiCache)
- ✅ Secret Manager 사용 (AWS Secrets Manager, Vault)
- ✅ Network Policy 적용
- ✅ RBAC 설정

---

## 📝 주요 명령어 모음

```bash
# === Docker ===
docker-compose up -d                    # 인프라 시작
docker-compose down -v                  # 인프라 정지 + 볼륨 삭제
docker-compose logs -f mysql            # MySQL 로그

# === Gradle ===
./gradlew :hexagonal-payment:clean bootJar    # 빌드
./gradlew :hexagonal-payment:bootRun          # 로컬 실행

# === Docker Build ===
docker build -t hexagonal-payment:1.0.0 .     # 이미지 빌드
docker run --rm -p 10001:10001 hexagonal-payment:1.0.0  # 테스트 실행

# === Kubernetes ===
kubectl apply -f k8s/hexagonal-payment/       # 전체 배포
kubectl delete -f k8s/hexagonal-payment/      # 전체 삭제
kubectl rollout restart deployment hexagonal-payment -n payment  # 재시작
kubectl logs -f -n payment -l app=hexagonal-payment  # 로그

# === 상태 확인 ===
kubectl get all -n payment                    # 전체 리소스
kubectl describe pod <pod-name> -n payment    # Pod 상세
kubectl top pod -n payment                    # 리소스 사용량
```

---

## 🎯 다음 단계

### 개선 계획

1. **CI/CD 파이프라인**
    - GitHub Actions
    - 자동 빌드/배포

2. **모니터링**
    - Prometheus + Grafana
    - Spring Boot Admin

3. **로깅**
    - ELK Stack
    - Fluentd

4. **테스트**
    - Testcontainers
    - Integration Tests

---

## 📚 참고 자료

- [Spring Boot on Kubernetes](https://spring.io/guides/gs/spring-boot-kubernetes/)
- [Rancher Desktop Documentation](https://docs.rancherdesktop.io/)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
