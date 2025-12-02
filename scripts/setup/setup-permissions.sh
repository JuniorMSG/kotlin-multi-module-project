#!/bin/bash
# reset-and-fix.sh

set -e

echo "════════════════════════════════════════"
echo "🔧 프로젝트 스크립트 권한 완전 초기화"
echo "════════════════════════════════════════"
echo ""

# 1. 현재 위치 확인
CURRENT_DIR=$(pwd)
echo "📍 현재 디렉토리: $CURRENT_DIR"

# 2. 프로젝트 루트 확인
if [ ! -f "gradlew" ]; then
    echo "❌ 프로젝트 루트가 아닙니다!"
    echo "   gradlew 파일을 찾을 수 없습니다."
    exit 1
fi
echo "✅ 프로젝트 루트 확인됨"
echo ""

# 3. 모든 .sh 파일 찾기
echo "🔍 .sh 파일 검색 중..."
SCRIPT_FILES=$(find . -type f -name "*.sh" | wc -l | tr -d ' ')
echo "   발견된 파일: ${SCRIPT_FILES}개"
echo ""

# 4. 권한 제거 (초기화)
echo "🗑️  기존 권한 초기화 중..."
find . -type f -name "*.sh" -exec chmod 644 {} \;
echo "✅ 권한 초기화 완료"
echo ""

# 5. 실행 권한 부여
echo "🔧 실행 권한 부여 중..."
find . -type f -name "*.sh" -exec chmod +x {} \;
echo "✅ 실행 권한 부여 완료"
echo ""

# 6. 검증
echo "🔍 권한 검증 중..."
FAILED=0
SUCCESS=0

while IFS= read -r file; do
    if [ -x "$file" ]; then
        SUCCESS=$((SUCCESS + 1))
        echo "  ✅ $file"
    else
        FAILED=$((FAILED + 1))
        echo "  ❌ $file"
    fi
done < <(find . -type f -name "*.sh")

echo ""
echo "════════════════════════════════════════"
echo "📊 결과"
echo "════════════════════════════════════════"
echo "  총 파일: ${SCRIPT_FILES}개"
echo "  성공: ${SUCCESS}개"
echo "  실패: ${FAILED}개"
echo ""

if [ "$FAILED" -eq 0 ]; then
    echo "✅ 모든 스크립트 실행 가능!"
    echo ""
    echo "💡 이제 다음 명령어를 실행할 수 있습니다:"
    echo "   ./scripts/build/build-module.sh hexagonal-payment"
    echo "   ./scripts/deploy/deploy-module.sh hexagonal-payment"
else
    echo "❌ ${FAILED}개 파일 권한 부여 실패"
    echo ""
    echo "🔧 수동으로 권한 부여:"
    echo "   chmod +x scripts/build/build-module.sh"
    echo "   chmod +x scripts/deploy/deploy-module.sh"
fi

echo "════════════════════════════════════════"
