#  빌드 스테이지
FROM openjdk:17-jdk-alpine AS build
WORKDIR /app

COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle
COPY gradlew .
COPY src src

RUN chmod +x gradlew
RUN ./gradlew clean build

# 런타임 스테이지
FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/chat-0.0.1-SNAPSHOT.jar  app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
