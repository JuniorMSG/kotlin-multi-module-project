#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

MODULE_NAME="hexagonal-payment"
SKIP_BUILD="${SKIP_BUILD:-false}"

echo "🚀 $MODULE_NAME 전체 배포 시작..."
echo "📍 프로젝트 루트: $PROJECT_ROOT"
echo ""

# 빌드 단계
if [ "$SKIP_BUILD" = "true" ]; then
    echo "⏭️  빌드 단계 건너뛰기"
else
    echo "════════════════════════════════════════"
    echo "🏗️  1단계: 빌드"
    echo "════════════════════════════════════════"
    "$SCRIPT_DIR/../build/build-hexagonal-payment.sh"
fi

# 배포 단계
echo ""
echo "════════════════════════════════════════"
echo "🚀 2단계: 배포"
echo "════════════════════════════════════════"
"$SCRIPT_DIR/deploy-only-hexagonal-payment.sh"

echo ""
echo "✅ $MODULE_NAME 전체 배포 완료!"
