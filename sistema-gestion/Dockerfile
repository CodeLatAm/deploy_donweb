FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=target/sistema-gestion-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_venta_pro.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_venta_pro.jar"]