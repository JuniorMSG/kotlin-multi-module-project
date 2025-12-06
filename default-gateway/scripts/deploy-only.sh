#!/bin/bash
# default-gatewa/scripts/deploy-only.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

MODULE_NAME="default-gateway"
IMAGE_TAG="${IMAGE_TAG:-0.0.1-SNAPSHOT}"
NAMESPACE="${NAMESPACE:-payment}"
K8S_DIR="$PROJECT_ROOT/k8s/default-gateway"

echo "🚀 $MODULE_NAME 배포 시작..."
echo "   - Image Tag: $IMAGE_TAG"
echo "   - Namespace: $NAMESPACE"

# 이미지 존재 확인
if ! docker images | grep -q "$MODULE_NAME.*$IMAGE_TAG"; then
    echo "⚠️  경고: Docker 이미지를 찾을 수 없습니다."
    echo "   먼저 빌드를 실행하세요: ./scripts/build.sh"
    read -p "계속하시겠습니까? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# K8s 배포
echo "☸️  Kubernetes 배포 중..."
cd "$K8S_DIR"

# 환경변수로 이미지 태그 전달
export IMAGE_TAG="$IMAGE_TAG"

chmod +x deploy.sh
./deploy.sh

echo "✅ $MODULE_NAME 배포 완료!"
