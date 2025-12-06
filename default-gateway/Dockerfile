# Gateway Dockerfile
FROM amazoncorretto:21-alpine

WORKDIR /app

# Gateway JAR 복사
COPY default-gateway/build/libs/*.jar app.jar

# Gateway 포트 노출
EXPOSE 10000 20000

# 기본 프로파일 설정
ENV SPRING_PROFILES_ACTIVE=local

# ⭐ Gateway용 JVM 옵션 (경량화)
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:MaxMetaspaceSize=128m -XX:ReservedCodeCacheSize=64m -XX:MaxDirectMemorySize=10m -XX:+UseG1GC -XX:MaxGCPauseMillis=100"

# ⭐ 디버그 옵션 (포트 20000)
ENV DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:20000"

# ⭐ Gateway 특화 옵션
ENV GATEWAY_OPTS="-Dserver.port=10000 -Dmanagement.server.port=10001"

# ⭐ ENTRYPOINT (환경변수 확장)
ENTRYPOINT sh -c "java $DEBUG_OPTS $JAVA_OPTS $GATEWAY_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar"
