#!/bin/bash

set -e

MODULE_NAME="default-gateway"
IMAGE_NAME="default-gateway"
IMAGE_TAG="1.0.0"
K8S_DIR="../k8s/gateway"

echo "🚀 $MODULE_NAME 배포 시작..."

# 1. 루트 디렉토리로 이동하여 빌드
echo "📦 Gradle 빌드 중..."
cd ../../                                    # 루트로 이동
./gradlew :$MODULE_NAME:clean bootJar   # 루트의 gradlew 사용
cd $MODULE_NAME                          # 다시 모듈로 돌아옴

# 2. Docker 이미지 빌드
echo "🐳 Docker 이미지 빌드 중..."
docker build -t $IMAGE_NAME:$IMAGE_TAG .

# 3. 이미지 확인
echo "✅ 이미지 확인:"
docker images | grep $IMAGE_NAME

# 4. Kubernetes 배포
echo "☸️  Kubernetes 배포 중..."
cd $K8S_DIR
./deploy.sh

echo ""
echo "✅ $MODULE_NAME 배포 완료!"
