#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

MODULE_NAME="hexagonal-payment"
MODULE_DEPLOY_SCRIPT="$PROJECT_ROOT/$MODULE_NAME/scripts/deploy-only.sh"

echo "🚀 $MODULE_NAME 배포만 시작..."
echo "📍 프로젝트 루트: $PROJECT_ROOT"
echo ""

# 모듈 배포 스크립트 존재 확인
if [ ! -f "$MODULE_DEPLOY_SCRIPT" ]; then
    echo "❌ 배포 스크립트를 찾을 수 없습니다: $MODULE_DEPLOY_SCRIPT"
    exit 1
fi

# 실행 권한 부여
chmod +x "$MODULE_DEPLOY_SCRIPT"

# 배포 실행
cd "$PROJECT_ROOT"
"$MODULE_DEPLOY_SCRIPT"

echo ""
echo "✅ $MODULE_NAME 배포 완료!"
