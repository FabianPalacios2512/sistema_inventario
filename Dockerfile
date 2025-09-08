# Etapa 1: Compilación con Maven y Java 17/21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución en un entorno ligero de Java
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Render asignará un puerto a la variable PORT
EXPOSE 8080
CMD ["java", "-Dserver.port=${PORT}", "-jar", "/app/app.jar"]