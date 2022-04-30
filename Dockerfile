FROM openjdk:16-alpine3.13
WORKDIR /app
COPY /private_data /app/private_data
ADD /target/kafka-producer-fatjar.jar /app/kafka-producer.jar
CMD ["java", "-jar", "/app/kafka-producer.jar"]