FROM amazoncorretto:23-alpine-jdk
LABEL maintainer="Ernestas"
COPY out/artifacts/demo_jar/demo.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]