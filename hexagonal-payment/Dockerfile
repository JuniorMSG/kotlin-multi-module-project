FROM amazoncorretto:21-alpine

WORKDIR /app

#COPY build/libs/*.jar app.jar
COPY hexagonal-payment/build/libs/*.jar app.jar


EXPOSE 10001 5005

# ✅ 기본 프로파일 설정
ENV SPRING_PROFILES_ACTIVE=local

ENTRYPOINT ["java", \
    "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", \
    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
    "-XX:+UseZGC", \
    "-XX:+ZGenerational", \
    "-Xms512m", \
    "-Xmx1g", \
    "-jar", \
    "app.jar"]
