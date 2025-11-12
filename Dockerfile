FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17-aarch64
WORKDIR /app
COPY --from=build /app/target/blog-0.0.1-SNAPSHOT.jar blog.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "blog.jar"]
