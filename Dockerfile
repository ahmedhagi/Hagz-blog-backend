FROM maven:3.8.5-openjdk-17-slim AS build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17
COPY --from=build /target/blog-0.0.1-SNAPSHOT.jar blog.jar
EXPOSE 8080
ENTRYPOINT ["java", \
            "-XX:+UseShenandoahGC", \
            "-Xms512m", \
            "-Xmx2g", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-jar", "blog.jar"]
