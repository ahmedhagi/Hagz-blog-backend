FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-oracle
COPY --from=build /target/blog-0.0.1-SNAPSHOT.jar blog.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "blog.jar"]
