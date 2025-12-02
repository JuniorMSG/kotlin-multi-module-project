#!/bin/bash
# hexagonal-payment/scripts/build.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

cd "$PROJECT_ROOT"

MODULE_NAME="hexagonal-payment"
IMAGE_NAME="hexagonal-payment"
IMAGE_TAG="${IMAGE_TAG:-0.0.1-SNAPSHOT}"

echo "🏗️  $MODULE_NAME 빌드 시작..."

# 1. Gradle 빌드
echo "📦 Gradle 빌드 중..."
./gradlew :$MODULE_NAME:clean bootJar

# 2. Docker 이미지 빌드
echo "🐳 Docker 이미지 빌드 중..."
docker build \
    -f "$MODULE_NAME/Dockerfile" \
    -t "$IMAGE_NAME:$IMAGE_TAG" \
    .

# 3. (선택) Docker 이미지 푸시
if [ "$PUSH_IMAGE" = "true" ]; then
    echo "📤 Docker 이미지 푸시 중..."
    docker push "$IMAGE_NAME:$IMAGE_TAG"
fi

echo "✅ 빌드 완료!"
echo "   - JAR: $MODULE_NAME/build/libs/$MODULE_NAME.jar"
echo "   - Image: $IMAGE_NAME:$IMAGE_TAG"
