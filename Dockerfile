FROM gradle:8-jdk AS builder
WORKDIR /home/gradle/project

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=builder /home/gradle/project/build/libs/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
