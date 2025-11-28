# Kotlin Multi Module Project Structure

## 📁 전체 프로젝트 구조

```
kotlin-multi-module-project/              ← 루트 프로젝트
├── .github/                              ← GitHub 설정
├── .gradle/                              ← Gradle 캐시
├── .idea/                                ← IntelliJ IDEA 설정
├── .kotlin/                              ← Kotlin 컴파일 캐시
├── build/                                ← 루트 빌드 출력
│
├── default-api/                          ← API 모듈
│   ├── src/
│   ├── build/
│   └── build.gradle.kts
│
├── default-batch/                        ← Batch 모듈
│   ├── src/
│   ├── build/
│   └── build.gradle.kts
│
├── default-consumer/                     ← Consumer 모듈
│   ├── src/
│   ├── build/
│   └── build.gradle.kts
│
├── default-core/                         ← Core 모듈 (공통)
│   ├── src/
│   ├── build/
│   └── build.gradle.kts
│
├── default-producer/                     ← Producer 모듈
│   ├── src/
│   ├── build/
│   └── build.gradle.kts
│
├── exercise/                             ← 연습용 모듈
│   ├── src/
│   ├── build/
│   └── build.gradle.kts
│
├── hexagonal-payment/                    ← Payment 모듈 (Hexagonal Architecture)
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-local.yml
│   │   │       └── application-k8s.yml
│   │   └── test/
│   ├── build/
│   │   └── libs/
│   │       └── hexagonal-payment-*.jar   ← 빌드된 JAR
│   ├── build.gradle.kts
│   ├── Dockerfile                        ← Docker 이미지 빌드용
│   └── deploy.sh                         ← 배포 스크립트
│
├── init/                                 ← 초기화 스크립트
├── init-scripts/                         ← Gradle init 스크립트
│
├── k8s/                                  ← Kubernetes 매니페스트
│   ├── hexagonal-payment/

│
├── gradle/                               ← Gradle Wrapper
│   └── wrapper/
├── gradlew                               ← Gradle 실행 스크립트 (Unix)
├── gradlew.bat                           ← Gradle 실행 스크립트 (Windows)
├── settings.gradle.kts                   ← Gradle 설정
└── build.gradle.kts                      ← 루트 빌드 설정
```

---

## 🎯 모듈별 역할

| 모듈                    | 역할                 | 의존성          |
|-----------------------|--------------------|--------------|
| **default-core**      | 공통 도메인, 유틸리티       | -            |
| **default-api**       | REST API           | default-core |
| **default-batch**     | 배치 작업              | default-core |
| **default-consumer**  | 메시지 소비자            | default-core |
| **default-producer**  | 메시지 생산자            | default-core |
| **hexagonal-payment** | 결제 시스템 (Hexagonal) | 독립 모듈        |
| **exercise**          | 학습/테스트용            | -            |

---

## 🔧 기술 스택

### Language & Framework

- **Kotlin** 1.9.21+
- **Java** 21 (Amazon Corretto)
- **Spring Boot** 3.x
- **Gradle** 8.5+

### Infrastructure

- **MySQL** 8.0
- **Redis** 7.x
- **Kubernetes**
- **Docker**

---

## 🚀 hexagonal-payment 모듈 상세

### 구조

```
hexagonal-payment/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/example/payment/
│   │   │       ├── adapter/          ← Adapter Layer
│   │   │       │   ├── in/           ← Inbound (Controller, Event Listener)
│   │   │       │   └── out/          ← Outbound (Repository, External API)
│   │   │       ├── application/      ← Application Layer (Use Cases)
│   │   │       ├── domain/           ← Domain Layer (Entities, Value Objects)
│   │   │       └── config/           ← Configuration
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-local.yml
│   │       └── application-k8s.yml
│   └── test/
├── build.gradle.kts
├── Dockerfile
└── deploy.sh
```

### Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Data Redis
- Spring Boot Starter Cache (Caffeine)
- Spring Boot Starter Validation
- Spring Boot Starter Actuator
- MySQL Connector
- Jackson Kotlin Module
- Kotlin Reflect & Stdlib

### 빌드 산출물

- **JAR 위치**: `hexagonal-payment/build/libs/hexagonal-payment-*.jar`
- **Docker 이미지**: `hexagonal-payment:latest`

---

## 📦 빌드 명령어

### 전체 프로젝트 빌드

```bash
./gradlew clean build -x test
```

### 특정 모듈 빌드

```bash
# hexagonal-payment 모듈만
./gradlew :hexagonal-payment:clean :hexagonal-payment:build -x test

# default-api 모듈만
./gradlew :default-api:clean :default-api:build -x test
```

### JAR 확인

```bash
# hexagonal-payment
ls -lh hexagonal-payment/build/libs/

# default-api
ls -lh default-api/build/libs/
```

---

## 🐳 Docker 빌드

### hexagonal-payment

```bash
# 1. JAR 빌드
./gradlew :hexagonal-payment:build -x test

# 2. Docker 이미지 빌드
cd hexagonal-payment
docker build -t hexagonal-payment:latest .

# 3. 이미지 확인
docker images | grep hexagonal-payment

# 4. Java 버전 확인
docker run --rm hexagonal-payment:latest java -version
```

---

## ☸️ Kubernetes 배포

### Namespace 생성

```bash
kubectl apply -f k8s/hexagonal-payment/namespace.yaml
```

### 전체 배포

```bash
kubectl apply -f k8s/hexagonal-payment/
```

### 배포 확인

```bash
# Pod 상태
kubectl get pods -n payment

# 로그 확인
kubectl logs -f -n payment -l app=hexagonal-payment

# 서비스 확인
kubectl get svc -n payment
```

### 접근

```bash
# NodePort 접근
curl http://localhost:30001/actuator/health

# Port Forward
kubectl port-forward -n payment service/hexagonal-payment 10001:10001
curl http://localhost:10001/actuator/health
```

---

## 🔍 디버깅

### Remote Debug 설정

```bash
# Debug 포트 포워딩
kubectl port-forward -n payment service/hexagonal-payment 5005:5005
```

**IntelliJ IDEA**:

```
Run → Edit Configurations → Remote JVM Debug
- Host: localhost
- Port: 5005
```

---

## 📝 환경 변수

### hexagonal-payment 필수 환경 변수

| 변수명                          | 설명           | 예시                          |
|------------------------------|--------------|-----------------------------|
| `SPRING_PROFILES_ACTIVE`     | 활성 프로파일      | `k8s`, `local`              |
| `SPRING_DATASOURCE_URL`      | MySQL 연결 URL | `jdbc:mysql://host:3306/db` |
| `SPRING_DATASOURCE_USERNAME` | MySQL 사용자명   | `payment_user`              |
| `SPRING_DATASOURCE_PASSWORD` | MySQL 비밀번호   | `payment123`                |
| `SPRING_DATA_REDIS_HOST`     | Redis 호스트    | `localhost`                 |
| `SPRING_DATA_REDIS_PORT`     | Redis 포트     | `6379`                      |
| `SPRING_DATA_REDIS_PASSWORD` | Redis 비밀번호   | `admin123`                  |

---

## 🛠️ 유용한 명령어

### Gradle

```bash
# 의존성 확인
./gradlew :hexagonal-payment:dependencies

# 태스크 목록
./gradlew :hexagonal-payment:tasks

# 빌드 캐시 삭제
./gradlew clean
```

### Docker

```bash
# 이미지 삭제
docker rmi hexagonal-payment:latest

# 컨테이너 로그
docker logs <container-id>

# 컨테이너 접속
docker exec -it <container-id> sh
```

### Kubernetes

```bash
# 전체 리소스 확인
kubectl get all -n payment

# Pod 재시작
kubectl rollout restart deployment/hexagonal-payment -n payment

# ConfigMap 확인
kubectl get configmap -n payment

# Secret 확인
kubectl get secret -n payment
```

---

## 📚 참고 사항

### Port 정보

- **10001**: hexagonal-payment HTTP
- **5005**: hexagonal-payment Debug
- **13306**: MySQL (로컬)
- **16379**: Redis (로컬)
- **30001**: hexagonal-payment NodePort (K8s)
- **30005**: hexagonal-payment Debug NodePort (K8s)

### Profile별 설정

- **local**: 로컬 개발 환경 (application-local.yml)
- **k8s**: Kubernetes 환경 (application-k8s.yml)
- **default**: 기본 설정 (application.yml)

---

## 🎓 AI Assistant를 위한 정보

### 중요한 경로

- **프로젝트 루트**: `kotlin-multi-module-project/`
- **hexagonal-payment 모듈**: `kotlin-multi-module-project/hexagonal-payment/`
- **hexagonal-payment JAR**: `hexagonal-payment/build/libs/hexagonal-payment-*.jar`
- **Dockerfile**: `hexagonal-payment/Dockerfile`
- **K8s 매니페스트**: `k8s/hexagonal-payment/`

### 빌드 컨텍스트

- Dockerfile이 `hexagonal-payment/` 안에 있을 때
- 빌드 컨텍스트는 `hexagonal-payment/` 디렉토리
- JAR 경로: `COPY build/libs/*.jar app.jar`

### 모듈 독립성

- `hexagonal-payment`는 **독립 모듈**
- `default-api`, `default-core` 등과 **의존성 없음**
- 각 모듈은 독립적으로 빌드 가능

---

## 📄 License

Proprietary - All rights reserved
