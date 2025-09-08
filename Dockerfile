# Usa una imagen base que ya tiene Java 21 instalado
FROM eclipse-temurin:21-jdk AS build

# Establece el directorio de trabajo
WORKDIR /workspace/app

# Copia el wrapper de Maven y el pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Descarga las dependencias
RUN ./mvnw dependency:go-offline

# Copia el código fuente y construye la aplicación
COPY src src
RUN ./mvnw package -DskipTests

# Ahora, crea la imagen final usando solo el JRE (más ligera)
FROM eclipse-temurin:21-jre
VOLUME /tmp

# Copia el archivo .jar desde la etapa de 'build'
COPY --from=build /workspace/app/target/*.jar app.jar

# Expone el puerto que usará Render
EXPOSE 10000

# Comando para arrancar la aplicación
ENTRYPOINT ["java","-jar","/app.jar"]