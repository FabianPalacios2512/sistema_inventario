# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos solo pom primero para aprovechar cache de Docker si cambian solo fuentes
COPY pom.xml .
# Si usas mvnw y .mvn, cópialos también:
COPY mvnw .
COPY .mvn .mvn

# Copiamos las fuentes
COPY src ./src

# Construimos el jar (sin tests para acelerar en CI)
RUN mvn -B -DskipTests clean package

# ---------- Run stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Exponemos el puerto (documentativo; Render asigna PORT por env)
EXPOSE 8080

# Copiamos el jar generado (cualquier jar en target/)
COPY --from=build /app/target/*.jar app.jar

# Usamos sh -c para que la expansión de ${PORT} funcione
# y permitimos pasar JAVA_OPTS si quieres tunear memory, GC, etc.
ENV JAVA_OPTS=""

CMD ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
