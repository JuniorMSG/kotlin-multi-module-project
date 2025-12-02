# 루트에서 빌드 (현재 방식)
docker build -t hexagonal-payment:1.0.0 -f hexagonal-payment/Dockerfile .

# 또는 hexagonal-payment 디렉토리에서 빌드
cd hexagonal-payment
docker build -t hexagonal-payment:1.0.0 .
cd ..
