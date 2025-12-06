FROM amazoncorretto:21-alpine

WORKDIR /app

#COPY build/libs/*.jar app.jar
COPY hexagonal-payment/build/libs/*.jar app.jar


EXPOSE 10001 20002

# ✅ 기본 프로파일 설정
ENV SPRING_PROFILES_ACTIVE=local

ENTRYPOINT ["java", \
    "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:20002", \
    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
    "-XX:+UseZGC", \
    "-XX:+ZGenerational", \
    "-Xms512m", \
    "-Xmx1g", \
    "-jar", \
    "app.jar"]

# ⭐ JVM 옵션을 환경변수로 분리 (K8s에서 오버라이드 가능)
ENV JAVA_OPTS="-Xms512m -Xmx1536m -XX:MaxMetaspaceSize=256m -XX:ReservedCodeCacheSize=128m -XX:MaxDirectMemorySize=10m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
ENV DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:20002"
# ⭐ ENTRYPOINT를 쉘 형식으로 변경 (환경변수 확장 가능)
ENTRYPOINT sh -c "java $DEBUG_OPTS $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar"
