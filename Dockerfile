FROM openjdk:17

COPY target/*.jar kpo-shop.jar

ENTRYPOINT ["java", "-jar", "kpo-shop.jar"]

EXPOSE 8081
