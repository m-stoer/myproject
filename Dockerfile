FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY target/MyClusterApp-0.1.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]
