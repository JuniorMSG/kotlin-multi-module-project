#!/bin/bash
# k8s/hexagonal-payment/deploy.sh

set -e

echo "🔍 호스트 IP 확인..."
HOST_IP=$(ipconfig getifaddr en0)
if [ -z "$HOST_IP" ]; then
    echo "❌ 호스트 IP를 찾을 수 없습니다!"
    exit 1
fi
echo "✅ 호스트 IP: $HOST_IP"

echo "🧪 Redis 연결 테스트..."
docker exec -it module-project-redis redis-cli -a admin123 ping || {
    echo "❌ Redis 연결 실패!"
    exit 1
}

echo "🧹 기존 리소스 삭제..."
kubectl delete deployment hexagonal-payment -n payment --force --grace-period=0 2>/dev/null || true
kubectl delete configmap hexagonal-payment-config -n payment 2>/dev/null || true
kubectl delete secret hexagonal-payment-secret -n payment 2>/dev/null || true

echo "⏳ 삭제 대기..."
sleep 10

echo "📝 ConfigMap 적용 (IP 치환: ${HOST_IP})..."
cat configmap.yaml | sed "s/\${HOST_IP}/${HOST_IP}/g" | kubectl apply -f -

echo "🔐 Secret 적용..."
kubectl apply -f secret.yaml

echo "🚀 Deployment 적용..."
kubectl apply -f deployment.yaml

echo "🌐 Service 적용..."
kubectl apply -f service.yaml

echo "⏳ Pod 시작 대기..."
sleep 15

echo ""
echo "📊 현재 상태:"
kubectl get pods -n payment -o wide

echo ""
echo "🔍 ConfigMap 검증:"
kubectl get configmap hexagonal-payment-config -n payment -o jsonpath='{.data.SPRING_DATASOURCE_URL}'
echo ""

echo ""
echo "📝 로그 확인 (Ctrl+C로 종료):"
kubectl logs -f -n payment -l app=hexagonal-payment --tail=100
