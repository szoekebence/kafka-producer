FROM openjdk:18
WORKDIR /app
COPY /private_data /app/private_data
ADD /target/kafka-producer-fat.jar /app/kafka-producer.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/kafka-producer.jar"]