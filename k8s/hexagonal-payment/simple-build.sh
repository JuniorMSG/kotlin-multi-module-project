# 1. 기존 삭제                                                                 ok | at rancher-desktop kube | at 15:53:44
kubectl delete deployment hexagonal-payment -n payment 2>/dev/null || true

# 2. 순서대로 적용
kubectl apply -f configmap.yaml
kubectl apply -f secret.yaml
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml

# 3. 확인
kubectl get endpoints,svc,pods -n payment
