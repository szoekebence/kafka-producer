FROM openjdk:18
WORKDIR /app
COPY /private_data /app/private_data
ADD /target/kafka-producer-fatjar.jar /app/kafka-producer.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/kafka-producer.jar"]