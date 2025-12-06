#!/bin/bash
# k8s/default-gateway/deploy.sh

set -e

NAMESPACE="default-gateway"

echo "🚪 default-gateway 배포 시작..."

# 1. Namespace 생성
echo "📦 Namespace 생성..."
kubectl apply -f namespace.yaml
echo "✅ Namespace 준비 완료"

# 2. 호스트 IP 확인
echo ""
echo "🔍 호스트 IP 확인..."
HOST_IP=$(ipconfig getifaddr en0)
if [ -z "$HOST_IP" ]; then
    echo "❌ 호스트 IP를 찾을 수 없습니다!"
    exit 1
fi
echo "✅ 호스트 IP: $HOST_IP"

# 3. Redis 연결 테스트
echo ""
echo "🧪 Redis 연결 테스트..."
docker exec module-project-redis redis-cli -a admin123 ping > /dev/null 2>&1 || {
    echo "❌ Redis 연결 실패!"
    exit 1
}
echo "✅ Redis 연결 성공"

# 4. 기존 리소스 삭제 (⭐ default-gateway로 통일)
echo ""
echo "🧹 기존 리소스 삭제..."
kubectl delete deployment default-gateway -n $NAMESPACE --force --grace-period=0 2>/dev/null || true
kubectl delete deployment api-gateway -n $NAMESPACE --force --grace-period=0 2>/dev/null || true
kubectl delete service default-gateway -n $NAMESPACE 2>/dev/null || true
kubectl delete service api-gateway -n $NAMESPACE 2>/dev/null || true
kubectl delete ingress default-gateway-ingress -n $NAMESPACE 2>/dev/null || true
kubectl delete ingress api-gateway-ingress -n $NAMESPACE 2>/dev/null || true
kubectl delete configmap api-gateway-config -n $NAMESPACE 2>/dev/null || true
kubectl delete secret api-gateway-secret -n $NAMESPACE 2>/dev/null || true

echo "⏳ 삭제 대기..."
sleep 5

# 5. Secret 적용
echo ""
echo "🔐 Secret 적용..."
kubectl apply -f secret.yaml

# Secret 검증
kubectl get secret api-gateway-secret -n $NAMESPACE -o jsonpath='{.data}' | jq 'keys' || {
    echo "❌ Secret 생성 실패!"
    exit 1
}
echo "✅ Secret 생성 완료"

# 6. ConfigMap 적용
echo ""
echo "📝 ConfigMap 적용 (IP 치환: ${HOST_IP})..."
cat configmap.yaml | sed "s/\${HOST_IP}/${HOST_IP}/g" | kubectl apply -f -

# ConfigMap 검증
kubectl get configmap api-gateway-config -n $NAMESPACE || {
    echo "❌ ConfigMap 생성 실패!"
    exit 1
}
echo "✅ ConfigMap 생성 완료"

# 7. Deployment 적용
echo ""
echo "🚀 Deployment 적용..."
kubectl apply -f deployment.yaml

# 8. Service 적용
echo ""
echo "🌐 Service 적용..."
kubectl apply -f service.yaml

# 9. Ingress 적용 (있다면)
if [ -f ingress.yaml ]; then
    echo ""
    echo "🌍 Ingress 적용..."
    kubectl apply -f ingress.yaml
fi

# 10. Pod 시작 대기
echo ""
echo "⏳ Pod 시작 대기 (5초)..."
sleep 5

# 11. 상태 확인 (⭐ default-gateway label 사용)
echo ""
echo "📊 Pod 상태:"
kubectl get pods -n $NAMESPACE -l app=default-gateway -o wide

# Pod 상세 정보
POD_NAME=$(kubectl get pod -n $NAMESPACE -l app=default-gateway -o jsonpath='{.items[0].metadata.name}')
if [ -n "$POD_NAME" ]; then
    POD_STATUS=$(kubectl get pod $POD_NAME -n $NAMESPACE -o jsonpath='{.status.phase}')
    echo "Pod 이름: $POD_NAME"
    echo "Pod 상태: $POD_STATUS"

    if [ "$POD_STATUS" != "Running" ]; then
        echo ""
        echo "⚠️  Pod가 Running 상태가 아닙니다!"
        echo ""
        echo "📝 Pod 상세 정보:"
        kubectl describe pod $POD_NAME -n $NAMESPACE | tail -30
    fi
fi

# 12. 전체 리소스
echo ""
echo "🔍 전체 리소스:"
kubectl get all -n $NAMESPACE

# 13. ConfigMap 검증
echo ""
echo "📝 ConfigMap 데이터:"
echo "  SPRING_PROFILES_ACTIVE: $(kubectl get configmap api-gateway-config -n $NAMESPACE -o jsonpath='{.data.SPRING_PROFILES_ACTIVE}')"
echo "  HOST_IP: $(kubectl get configmap api-gateway-config -n $NAMESPACE -o jsonpath='{.data.HOST_IP}')"
echo "  REDIS_HOST: $(kubectl get configmap api-gateway-config -n $NAMESPACE -o jsonpath='{.data.REDIS_HOST}')"

# 14. Service 정보
echo ""
echo "🌐 Service 정보:"
kubectl get svc default-gateway -n $NAMESPACE

SERVICE_PORT=$(kubectl get svc default-gateway -n $NAMESPACE -o jsonpath='{.spec.ports[0].port}')
NODE_PORT=$(kubectl get svc default-gateway -n $NAMESPACE -o jsonpath='{.spec.ports[0].nodePort}')

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ 배포 완료!"
echo ""
echo "🌐 접근 방법:"
echo "  Service Port: http://localhost:${SERVICE_PORT}"
echo "  NodePort:     http://localhost:${NODE_PORT}"
echo ""
echo "🧪 테스트 명령어:"
echo "  curl http://localhost:${SERVICE_PORT}/actuator/health"
echo "  curl http://localhost:${SERVICE_PORT}/api/payment/methods"
echo ""
echo "📝 로그 확인:"
echo "  kubectl logs -f -n $NAMESPACE -l app=default-gateway"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# 15. 로그 확인 옵션
echo ""
read -p "📝 실시간 로그를 확인하시겠습니까? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo ""
    echo "📝 실시간 로그 (Ctrl+C로 종료):"
    kubectl logs -f -n $NAMESPACE -l app=default-gateway --tail=100
fi
