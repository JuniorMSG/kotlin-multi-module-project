#!/bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() { echo -e "${GREEN}ℹ️  $1${NC}"; }
log_success() { echo -e "${GREEN}✅ $1${NC}"; }
log_error() { echo -e "${RED}❌ $1${NC}"; }

get_modules() {
    echo "hexagonal-payment"
    echo "api-gateway"
    echo "user-service"
}

select_module() {
    local modules=()
    while IFS= read -r module; do
        [ -n "$module" ] && modules+=("$module")
    done < <(get_modules)

    # ⭐ 메뉴 출력 (stderr로만 출력)
    echo "" >&2
    echo "📦 사용 가능한 모듈" >&2
    echo "" >&2

    local i=1
    for module in "${modules[@]}"; do
        echo "  ${i}) ${module}" >&2
        i=$((i + 1))
    done

    echo "" >&2
    echo "  0) 전체 모듈" >&2
    echo "  q) 종료" >&2
    echo "" >&2

    # ⭐ 입력 받기
    read -p "선택: " choice >&2

    # ⭐ 종료
    if [ "$choice" = "q" ]; then
        return 1
    fi

    # ⭐ 전체 모듈
    if [ "$choice" = "0" ]; then
        echo "all"  # stdout으로만 출력
        return 0
    fi

    # ⭐ 특정 모듈
    if [[ "$choice" =~ ^[0-9]+$ ]] && [ "$choice" -ge 1 ] && [ "$choice" -le ${#modules[@]} ]; then
        echo "${modules[$((choice - 1))]}"  # stdout으로만 출력
        return 0
    fi

    # ⭐ 잘못된 입력
    log_error "잘못된 선택" >&2
    return 1
}
