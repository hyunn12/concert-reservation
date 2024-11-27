FROM eclipse-temurin:21-jdk-alpine AS build

CMD ["./gradlew", "clean", "build"]

VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar
