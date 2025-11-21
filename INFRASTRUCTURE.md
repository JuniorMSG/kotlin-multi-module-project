# 🚀 로컬 개발 인프라 환경 가이드

## 📋 목차

- [개요](#개요)
- [사전 요구사항](#사전-요구사항)
- [서비스 구성](#서비스-구성)
- [빠른 시작](#빠른-시작)
- [서비스별 접속 정보](#서비스별-접속-정보)
- [상세 설정](#상세-설정)
- [문제 해결](#문제-해결)

---

## 📌 개요

이 Docker Compose 설정은 로컬 개발 환경에서 필요한 모든 인프라 서비스를 제공합니다.
포트 충돌을 방지하기 위해 모든 포트에 10000을 더한 값을 사용합니다.

### 제공 서비스

- **MySQL 8.0**: 관계형 데이터베이스
- **Apache Kafka**: 메시지 큐 시스템
- **MinIO**: S3 호환 오브젝트 스토리지
- **SonarQube**: 코드 품질 분석 도구

---

## 🔧 사전 요구사항

### 필수 설치 항목

- [Docker Desktop](https://www.docker.com/products/docker-desktop) (v20.10 이상)
- [Docker Compose](https://docs.docker.com/compose/install/) (v2.0 이상)

### 시스템 요구사항

- **메모리**: 최소 8GB RAM (권장 16GB)
- **디스크**: 최소 20GB 여유 공간
- **OS**: Windows 10/11, macOS 10.15+, Linux

### 설치 확인

```bash
# Docker 버전 확인
docker --version
# Docker Compose 버전 확인
docker compose version
```

---

## 🏗️ 서비스 구성

| 서비스           | 이미지                             | 호스트 포트       | 컨테이너 포트    | 용도          |
|---------------|---------------------------------|--------------|------------|-------------|
| **MySQL**     | mysql:8.0                       | 13306        | 3306       | 메인 데이터베이스   |
| **Zookeeper** | confluentinc/cp-zookeeper:7.6.0 | 12181        | 2181       | Kafka 코디네이터 |
| **Kafka**     | confluentinc/cp-kafka:7.6.0     | 19092        | 9092       | 메시지 브로커     |
| **Kafka UI**  | provectuslabs/kafka-ui:latest   | 18090        | 8080       | Kafka 관리 UI |
| **MinIO**     | minio/minio:latest              | 19000, 19001 | 9000, 9001 | 오브젝트 스토리지   |
| **SonarQube** | sonarqube:latest                | 19090        | 9000       | 코드 품질 분석    |

---

## 🚀 빠른 시작

### 1. 전체 서비스 시작

```bash
# 백그라운드에서 모든 서비스 실행
docker compose up -d

# 로그 확인
docker compose logs -f
```

### 2. 특정 서비스만 시작

```bash
# MySQL만 시작
docker compose up -d mysql

# Kafka 스택만 시작 (Zookeeper + Kafka + Kafka UI)
docker compose up -d zookeeper kafka kafka-ui

# MinIO만 시작
docker compose up -d minio createbuckets
```

### 3. 서비스 상태 확인

```bash
# 실행 중인 컨테이너 확인
docker compose ps

# 서비스 헬스체크 확인
docker compose ps --format json | jq '.[] | {name: .Name, health: .Health}'
```

### 4. 서비스 중지 및 제거

```bash
# 서비스 중지
docker compose stop

# 서비스 중지 및 컨테이너 제거
docker compose down

# 볼륨까지 모두 제거 (데이터 삭제)
docker compose down -v
```

---

## 🔐 서비스별 접속 정보

### MySQL

```yaml
호스트: localhost
포트: 13306
데이터베이스: module
사용자: admin
비밀번호: admin
Root 비밀번호: root
```

**연결 예시**

```bash
# MySQL CLI
mysql -h 127.0.0.1 -P 13306 -u admin -p

# JDBC URL
jdbc:mysql://localhost:13306/module?useSSL=false&serverTimezone=Asia/Seoul
```

**IntelliJ Database 설정**

```
Host: localhost
Port: 13306
Database: module
User: admin
Password: admin
Driver: MySQL
```

---

### Kafka

**Broker 접속 정보**

```yaml
외부 접속: localhost:19092
내부 접속: kafka:9093
Zookeeper: localhost:12181
```

**Kafka UI 접속**

- URL: http://localhost:18090
- 클러스터명: module-local

**토픽 생성 예시**

```bash
# Kafka 컨테이너 접속
docker exec -it local-kafka bash

# 토픽 생성
kafka-topics --create \
  --bootstrap-server localhost:9092 \
  --topic test-topic \
  --partitions 3 \
  --replication-factor 1

# 토픽 목록 확인
kafka-topics --list --bootstrap-server localhost:9092
```

**애플리케이션 설정 (Spring Boot)**

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:19092
    consumer:
      group-id: my-group
      auto-offset-reset: earliest
    producer:
      acks: all
```

---

### MinIO (S3 호환 스토리지)

**접속 정보**

```yaml
Console URL: http://localhost:19001
API Endpoint: http://localhost:19000
Access Key: admin
Secret Key: admin123
기본 버킷: public
```

**MinIO Console 접속**

1. 브라우저에서 http://localhost:19001 접속
2. Username: `admin`
3. Password: `admin123`

**AWS CLI 설정**

```bash
# AWS CLI 설치 (Mac)
brew install awscli

# MinIO 프로파일 설정
aws configure --profile minio
# AWS Access Key ID: admin
# AWS Secret Access Key: admin123
# Default region name: us-east-1
# Default output format: json

# 버킷 목록 확인
aws --profile minio --endpoint-url http://localhost:19000 s3 ls

# 파일 업로드
aws --profile minio --endpoint-url http://localhost:19000 \
  s3 cp test.txt s3://public/
```

**Spring Boot 설정**

```yaml
cloud:
  aws:
    s3:
      endpoint: http://localhost:19000
    credentials:
      access-key: admin
      secret-key: admin123
    region:
      static: us-east-1
```

---

### SonarQube

**접속 정보**

```yaml
URL: http://localhost:19090
초기 계정:
  - Username: admin
  - Password: admin
```

**초기 설정**

1. http://localhost:19090 접속
2. 초기 비밀번호로 로그인 (admin/admin)
3. 새 비밀번호 설정 요청 시 변경
4. 프로젝트 생성 및 토큰 발급

**Gradle 프로젝트 연동**

```gradle
// build.gradle.kts
plugins {
    id("org.sonarqube") version "4.4.1.3373"
}

sonar {
    properties {
        property("sonar.projectKey", "my-project")
        property("sonar.host.url", "http://localhost:19090")
        property("sonar.login", "your-token-here")
    }
}
```

**분석 실행**

```bash
./gradlew sonar \
  -Dsonar.projectKey=my-project \
  -Dsonar.host.url=http://localhost:19090 \
  -Dsonar.login=your-token-here
```

---

## ⚙️ 상세 설정

### 볼륨 관리

**생성된 볼륨 목록**

```bash
# 볼륨 확인
docker volume ls | grep docker

# 볼륨 상세 정보
docker volume inspect docker_mysql-data
```

**데이터 백업**

```bash
# MySQL 데이터 백업
docker exec local-mysql mysqldump -u admin -padmin module > backup.sql

# MinIO 데이터 백업
docker run --rm -v docker_minio-data:/data -v $(pwd):/backup \
  alpine tar czf /backup/minio-backup.tar.gz -C /data .
```

**데이터 복원**

```bash
# MySQL 데이터 복원
docker exec -i local-mysql mysql -u admin -padmin module < backup.sql

# MinIO 데이터 복원
docker run --rm -v docker_minio-data:/data -v $(pwd):/backup \
  alpine tar xzf /backup/minio-backup.tar.gz -C /data
```

---

### 환경 변수 커스터마이징

**.env 파일 생성** (docker-compose.yml과 같은 디렉토리)

```bash
# MySQL 설정
MYSQL_ROOT_PASSWORD=custom_root_password
MYSQL_DATABASE=custom_database
MYSQL_USER=custom_user
MYSQL_PASSWORD=custom_password

# MinIO 설정
MINIO_ROOT_USER=custom_admin
MINIO_ROOT_PASSWORD=custom_password123

# Kafka 설정
KAFKA_BROKER_ID=1
```

---

### 네트워크 설정

**기본 네트워크**

- Docker Compose는 자동으로 `docker_default` 네트워크 생성
- 모든 서비스는 서비스명으로 서로 통신 가능

**커스텀 네트워크 추가**

```yaml
networks:
  backend:
    driver: bridge
  frontend:
    driver: bridge

services:
  mysql:
    networks:
      - backend

  kafka:
    networks:
      - backend
      - frontend
```

---

## 🔍 문제 해결

### 포트 충돌 발생 시

**사용 중인 포트 확인**

```bash
# Mac/Linux
lsof -i :13306

# Windows
netstat -ano | findstr :13306
```

**포트 변경**

```yaml
# docker-compose.yml에서 호스트 포트만 변경
services:
  mysql:
    ports:
      - "23306:3306"  # 다른 포트로 변경
```

---

### 컨테이너 시작 실패

**로그 확인**

```bash
# 전체 로그
docker compose logs

# 특정 서비스 로그
docker compose logs mysql

# 실시간 로그 추적
docker compose logs -f kafka
```

**컨테이너 재시작**

```bash
# 특정 서비스 재시작
docker compose restart mysql

# 전체 재시작
docker compose restart
```

---

### 디스크 공간 부족

**사용하지 않는 리소스 정리**

```bash
# 중지된 컨테이너 제거
docker container prune

# 사용하지 않는 이미지 제거
docker image prune -a

# 사용하지 않는 볼륨 제거
docker volume prune

# 전체 정리 (주의!)
docker system prune -a --volumes
```

---

### MySQL 연결 오류

**헬스체크 확인**

```bash
docker compose ps mysql
```

**수동 연결 테스트**

```bash
docker exec -it local-mysql mysql -u admin -padmin -e "SELECT 1"
```

**권한 문제 해결**

```bash
docker exec -it local-mysql mysql -u root -proot

# MySQL 콘솔에서
GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%';
FLUSH PRIVILEGES;
```

---

### Kafka 연결 오류

**Kafka 상태 확인**

```bash
# Zookeeper 연결 확인
docker exec local-zookeeper zkServer.sh status

# Kafka 브로커 확인
docker exec local-kafka kafka-broker-api-versions \
  --bootstrap-server localhost:9092
```

**토픽 목록 확인**

```bash
docker exec local-kafka kafka-topics \
  --list --bootstrap-server localhost:9092
```

---

### MinIO 버킷 생성 실패

**수동 버킷 생성**

```bash
# MinIO 클라이언트 설정
docker exec -it local-minio mc alias set localminio \
  http://localhost:9000 admin admin123

# 버킷 생성
docker exec -it local-minio mc mb localminio/my-bucket

# 버킷 목록 확인
docker exec -it local-minio mc ls localminio
```

---

### SonarQube 메모리 부족

**메모리 제한 증가**

```yaml
services:
  sonarqube:
    environment:
      - SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true
      - SONAR_JAVA_OPTS=-Xmx2g -Xms512m  # 메모리 증가
    deploy:
      resources:
        limits:
          memory: 4g
```

---

## 📚 추가 리소스

### 공식 문서

- [Docker Compose 문서](https://docs.docker.com/compose/)
- [MySQL 8.0 문서](https://dev.mysql.com/doc/refman/8.0/en/)
- [Apache Kafka 문서](https://kafka.apache.org/documentation/)
- [MinIO 문서](https://min.io/docs/minio/linux/index.html)
- [SonarQube 문서](https://docs.sonarqube.org/latest/)

### 유용한 명령어 모음

**전체 상태 확인**

```bash
# 한 번에 모든 서비스 상태 확인
docker compose ps && \
docker compose top && \
docker stats --no-stream
```

**리소스 사용량 모니터링**

```bash
# 실시간 리소스 사용량
docker stats

# 특정 컨테이너만 모니터링
docker stats local-mysql local-kafka
```

**로그 필터링**

```bash
# 에러 로그만 확인
docker compose logs | grep -i error

# 최근 100줄만 확인
docker compose logs --tail=100
```

---

## 🎯 베스트 프랙티스

### 1. 개발 시작 전

```bash
# 모든 서비스 시작 및 헬스체크 대기
docker compose up -d && \
docker compose ps --format json | jq '.[] | {name: .Name, health: .Health}'
```

### 2. 개발 종료 시

```bash
# 데이터 유지하면서 컨테이너만 중지
docker compose stop
```

### 3. 완전 초기화가 필요한 경우

```bash
# 모든 데이터 삭제 후 재시작
docker compose down -v && \
docker compose up -d
```

### 4. 정기적인 정리

```bash
# 주 1회 사용하지 않는 리소스 정리
docker system prune -f
```

---

## 📝 변경 이력

| 날짜         | 버전    | 변경 내용    |
|------------|-------|----------|
| 2025-11-21 | 1.0.0 | 초기 버전 작성 |

---

## 👥 문의 및 지원

문제가 발생하거나 개선 사항이 있다면:

1. 로그 확인: `docker compose logs -f`
2. 이슈 등록 또는 팀 채널에 문의
3. 이 문서의 [문제 해결](#문제-해결) 섹션 참고

---
