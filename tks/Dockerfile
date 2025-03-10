FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

COPY . .

RUN mvn clean verify install site -Dmaven.test.skip=true

EXPOSE 8087

ENTRYPOINT ["sh", "-c", "echo 'Waiting for 60 seconds...' && sleep 60 && java -jar ./target/SpringMongoRest.jar"]