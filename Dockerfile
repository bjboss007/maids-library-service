FROM openjdk:17
VOLUME /tmp
COPY target/library-system-0.0.1-SNAPSHOT.jar library-system.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "library-system.jar"]