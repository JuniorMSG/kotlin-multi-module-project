#!/bin/bash
# build-deploy-forward.sh

set -e

echo "🔨 === 완전 자동화: 빌드 → 배포 → 포트포워딩 ==="
echo ""

# 색상
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 변수
IMAGE_NAME="hexagonal-payment"
VERSION="1.0.0"
NAMESPACE="payment"
SERVICE_PORT="10001"
LOCAL_PORT="10001"
DEBUG_PORT="5005"

# 1. Gradle 빌드
echo -e "${BLUE}1️⃣ Gradle 빌드...${NC}"
./gradlew :hexagonal-payment:clean :hexagonal-payment:bootJar

# 2. Docker 이미지 빌드
echo ""
echo -e "${BLUE}2️⃣ Docker 이미지 빌드...${NC}"
docker build -t ${IMAGE_NAME}:${VERSION} -f hexagonal-payment/Dockerfile .

# 3. 기존 배포 삭제
echo ""
echo -e "${BLUE}3️⃣ 기존 배포 삭제...${NC}"
kubectl delete deployment ${IMAGE_NAME} -n ${NAMESPACE} --force --grace-period=0 2>/dev/null || true
sleep 3

# 4. 컨테이너 정리
echo ""
echo -e "${BLUE}4️⃣ 컨테이너 정리...${NC}"
docker ps -a | grep ${IMAGE_NAME} | awk '{print $1}' | xargs -r docker rm -f

# 5. 이미지 정리
echo ""
echo -e "${BLUE}5️⃣ 이미지 정리...${NC}"
docker images | grep ${IMAGE_NAME} | grep -v ${VERSION} | awk '{print $3}' | xargs -r docker rmi -f

# 6. Kubernetes 배포
echo ""
echo -e "${BLUE}6️⃣ Kubernetes 배포...${NC}"
kubectl apply -f k8s/hexagonal-payment/deployment.yaml

# 7. Pod 준비 대기
echo ""
echo -e "${BLUE}7️⃣ Pod 준비 대기...${NC}"
kubectl wait --for=condition=ready pod -l app=${IMAGE_NAME} -n ${NAMESPACE} --timeout=120s

# 8. 상태 확인
echo ""
echo -e "${GREEN}✅ 배포 완료!${NC}"
kubectl get pods -n ${NAMESPACE}
echo ""

# 9. 기존 포트포워딩 종료
echo -e "${BLUE}8️⃣ 기존 포트포워딩 종료...${NC}"
pkill -f "kubectl port-forward.*${NAMESPACE}.*${IMAGE_NAME}" || true
sleep 2

# 10. 포트포워딩 시작 (백그라운드)
echo ""
echo -e "${BLUE}9️⃣ 포트포워딩 시작...${NC}"
kubectl port-forward -n ${NAMESPACE} svc/${IMAGE_NAME} ${LOCAL_PORT}:${SERVICE_PORT} > /tmp/port-forward.log 2>&1 &
PF_PID=$!

# 포트포워딩 확인
sleep 3
if ps -p $PF_PID > /dev/null; then
    echo -e "${GREEN}✅ 포트포워딩 성공! (PID: $PF_PID)${NC}"
else
    echo -e "${RED}❌ 포트포워딩 실패!${NC}"
    cat /tmp/port-forward.log
    exit 1
fi

# 11. 최종 정보 출력
echo ""
echo -e "${GREEN}🎉 === 모든 작업 완료! ===${NC}"
echo ""
echo -e "${YELLOW}📋 접속 정보:${NC}"
echo -e "  • Application: ${GREEN}http://localhost:${LOCAL_PORT}${NC}"
echo -e "  • Health Check: ${GREEN}http://localhost:${LOCAL_PORT}/actuator/health${NC}"
echo -e "  • Debug Port: ${GREEN}localhost:${DEBUG_PORT}${NC}"
echo ""
echo -e "${YELLOW}📊 유용한 명령어:${NC}"
echo -e "  • 로그 확인: ${BLUE}kubectl logs -f -n ${NAMESPACE} -l app=${IMAGE_NAME}${NC}"
echo -e "  • Pod 상태: ${BLUE}kubectl get pods -n ${NAMESPACE}${NC}"
echo -e "  • 포트포워딩 종료: ${BLUE}kill $PF_PID${NC}"
echo ""
echo -e "${YELLOW}🔍 테스트:${NC}"
echo -e "  ${BLUE}curl http://localhost:${LOCAL_PORT}/actuator/health${NC}"
echo ""

# 12. 로그 실시간 확인 옵션
read -p "kubectl logs -f -n payment -l app=hexagonal-payment"
# kubectl logs -f -n payment -l app=hexagonal-payment
