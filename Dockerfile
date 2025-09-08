# =================================
#  Etapa 1: Compilación (Build)
# =================================
# Usamos una imagen oficial de Maven con la versión de Java (Temurin 21) que coincide con tu pom.xml
FROM maven:3.9-eclipse-temurin-21 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero el pom.xml para aprovechar la caché de Docker
# Si las dependencias no cambian, Docker no las descargará de nuevo
COPY pom.xml .

# Copiamos el Maven Wrapper para usar la versión correcta de Maven
COPY mvnw .
COPY .mvn .mvn

# Copiamos el resto del código fuente de la aplicación
COPY src ./src

# Ejecutamos el comando de Maven para compilar el proyecto y crear el .jar
# -B (batch mode) para que no sea interactivo
# -DskipTests para acelerar la compilación en el despliegue
RUN ./mvnw -B -DskipTests clean package


# =================================
#  Etapa 2: Ejecución (Run)
# =================================
# Usamos una imagen JRE (Java Runtime Environment) ligera, solo para ejecutar
FROM eclipse-temurin:21-jre

# Establecemos el directorio de trabajo
WORKDIR /app

# Render inyectará la variable de entorno PORT. La exponemos para documentación.
EXPOSE 8080

# Copiamos el .jar compilado desde la etapa de 'build' al directorio actual
# El comodín (*) asegura que funcione sin importar el nombre exacto del .jar
COPY --from=build /app/target/*.jar app.jar

# Este es el comando que ejecutará Render para iniciar tu aplicación
# Usa sh -c para que la variable ${PORT} se expanda correctamente
# Si PORT no está definida, usará 8080 por defecto
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar /app/app.jar"]