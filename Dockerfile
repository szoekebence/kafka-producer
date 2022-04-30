FROM openjdk:16-alpine3.13
WORKDIR /app
COPY
ADD kafka-producer/target/kafka-producer.jar /opt/middleware/java/app.jar
ENTRYPOINT [ "sh", "-c", "java -Xms$JAVA_XMS -Xmx$JAVA_XMX -XX:MaxMetaspaceSize=$JAVA_MAX_META" ]