FROM eclipse-temurin:22-jdk-alpine AS build

WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:22-jdk-alpine AS runtime
COPY --from=build /app/target/*.jar app.jar

EXPOSE 3344
EXPOSE 3355

ENTRYPOINT ["java", "-jar", "app.jar"]
