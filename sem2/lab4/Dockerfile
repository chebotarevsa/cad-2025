



FROM gradle:8.14.5-jdk17 AS gradle




FROM node:20-bookworm-slim AS build
RUN apt-get update && apt-get install -y --no-install-recommends openjdk-17-jdk-headless \
    && rm -rf /var/lib/apt/lists/*
COPY --from=gradle /opt/gradle /opt/gradle
ENV PATH="/opt/gradle/bin:${PATH}"
WORKDIR /app
COPY . .





RUN gradle bootJar --no-daemon -x test \
    && mv build/libs/*.jar app.jar \
    && rm -rf /root/.gradle /root/.npm frontend/node_modules build


FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
