# Etapa 1: Compilar el proyecto con Maven y Java 17
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Usamos -B para modo batch y -DskipTests para acelerar
RUN ./mvnw -B package -DskipTests

# Etapa 2: Crear la imagen final de ejecución
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el archivo .jar compilado desde la etapa anterior
COPY --from=build /workspace/app/target/*.jar app.jar

# Expone el puerto que la plataforma asignará
EXPOSE 8080

# Comando para arrancar la aplicación, usando el puerto que nos dé la plataforma
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar","/app/app.jar"]