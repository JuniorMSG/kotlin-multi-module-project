#!/bin/bash
# hexagonal-payment/scripts/deploy.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

SKIP_BUILD="${SKIP_BUILD:-false}"

echo "🚀 hexagonal-payment 전체 배포 시작..."

# ⭐ 스크립트 권한 자동 부여
chmod +x "$SCRIPT_DIR/build.sh"
chmod +x "$SCRIPT_DIR/deploy-only.sh"

# 빌드 단계
if [ "$SKIP_BUILD" = "true" ]; then
    echo "⏭️  빌드 단계 건너뛰기"
else
    echo ""
    echo "════════════════════════════════════════"
    echo "🏗️  1단계: 빌드"
    echo "════════════════════════════════════════"
    "$SCRIPT_DIR/build.sh"
fi

# 배포 단계
echo ""
echo "════════════════════════════════════════"
echo "🚀 2단계: 배포"
echo "════════════════════════════════════════"
"$SCRIPT_DIR/deploy-only.sh"

echo ""
echo "✅ 전체 배포 완료!"
