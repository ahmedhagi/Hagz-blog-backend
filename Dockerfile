FROM maven:3.8.5-openjdk-17-slim AS build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17 AS extract
COPY --from=build /target/blog-0.0.1-SNAPSHOT.jar blog.jar
RUN java -Djarmode=layertools -jar blog.jar extract

FROM bellsoft/liberica-openjdk-alpine:17
COPY --from=extract dependencies/ ./
COPY --from=extract snapshot-dependencies/ ./
COPY --from=extract spring-boot-loader/ ./
COPY --from=extract application/ ./

EXPOSE 8080
ENTRYPOINT ["java", \
            "-XX:+UseShenandoahGC", \
            "-Xms512m", \
            "-Xmx2g", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "org.springframework.boot.loader.JarLauncher"]