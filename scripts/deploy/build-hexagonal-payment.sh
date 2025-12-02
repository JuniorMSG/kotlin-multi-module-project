#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

MODULE_NAME="hexagonal-payment"
MODULE_BUILD_SCRIPT="$PROJECT_ROOT/$MODULE_NAME/scripts/build.sh"

echo "🏗️  $MODULE_NAME 빌드 시작..."
echo "📍 프로젝트 루트: $PROJECT_ROOT"
echo ""

# 모듈 빌드 스크립트 존재 확인
if [ ! -f "$MODULE_BUILD_SCRIPT" ]; then
    echo "❌ 빌드 스크립트를 찾을 수 없습니다: $MODULE_BUILD_SCRIPT"
    exit 1
fi

# 실행 권한 부여
chmod +x "$MODULE_BUILD_SCRIPT"

# 빌드 실행
cd "$PROJECT_ROOT"
"$MODULE_BUILD_SCRIPT"

echo ""
echo "✅ $MODULE_NAME 빌드 완료!"
