#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

source "$SCRIPT_DIR/../common/module-utils.sh"

MODULE_NAME="${1}"
IMAGE_TAG="${2:-0.0.1-SNAPSHOT}"
SKIP_BUILD="${3:-false}"

if [ -z "$MODULE_NAME" ]; then
    MODULE_NAME=$(select_module)
    [ $? -ne 0 ] && exit 1
    [ -z "$MODULE_NAME" ] && exit 1
    [ "$MODULE_NAME" = "all" ] && exec "$SCRIPT_DIR/deploy-all.sh" "$IMAGE_TAG"
    
    echo ""
    echo -n "빌드를 건너뛰시겠습니까? (y/N): "
    read -n 1 -r
    echo
    [[ $REPLY =~ ^[Yy]$ ]] && SKIP_BUILD="true"
fi

echo ""
log_info "$MODULE_NAME 배포 시작..."
echo "📍 이미지 태그: $IMAGE_TAG"
echo "📍 빌드 건너뛰기: $SKIP_BUILD"
echo ""

export IMAGE_TAG="$IMAGE_TAG"

if [ "$SKIP_BUILD" != "true" ]; then
    echo "════════════════════════════════════════"
    echo "🏗️  1단계: 빌드"
    echo "════════════════════════════════════════"
    "$SCRIPT_DIR/../build/build-module.sh" "$MODULE_NAME" "$IMAGE_TAG"
fi

echo ""
echo "════════════════════════════════════════"
echo "🚀 2단계: 배포"
echo "════════════════════════════════════════"

MODULE_DEPLOY_SCRIPT="$PROJECT_ROOT/$MODULE_NAME/scripts/deploy-only.sh"

if [ ! -f "$MODULE_DEPLOY_SCRIPT" ]; then
    log_error "배포 스크립트 없음: $MODULE_DEPLOY_SCRIPT"
    exit 1
fi

chmod +x "$MODULE_DEPLOY_SCRIPT"
cd "$PROJECT_ROOT"
"$MODULE_DEPLOY_SCRIPT"

echo ""
log_success "$MODULE_NAME 배포 완료!"
