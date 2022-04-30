FROM openjdk:16-alpine3.13
WORKDIR /app
ADD kafka-producer/target/kafka-producer-0.0.1.jar /app/kafka-producer.jar
CMD ["java", "-jar", "/app/kafka-producer.jar"]