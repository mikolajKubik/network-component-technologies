package pl.edu.dik.tks;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;

import java.time.Duration;

@TestConfiguration
public class TestContainerConfig {
    private static final int MONGO_PORT = 27017;

    @Bean
    public GenericContainer<?> mongoContainer() {
        GenericContainer<?> mongoContainer = new GenericContainer<>("mongo:8.0.1")
                .withExposedPorts(MONGO_PORT)
                .withCommand("--replSet", "rs0", "--bind_ip_all")
                .withStartupTimeout(Duration.ofMinutes(2));

        mongoContainer.start();

        try {
            org.testcontainers.containers.Container.ExecResult initResult = mongoContainer.execInContainer(
                    "mongosh", "--eval",
                    "rs.initiate({_id: 'rs0', members: [{_id: 0, host: 'localhost:" + MONGO_PORT + "'}]})"
            );

            System.out.println("Replica set initialization: " + initResult.getStdout());

            // Wait for replica set to initialize
//            Thread.sleep(5000);

            // Set Spring property that matches your MongoConfig class
            System.setProperty("mongodb.connection.uri",
                    "mongodb://" + mongoContainer.getHost() + ":" +
                            mongoContainer.getMappedPort(MONGO_PORT) + "/?replicaSet=rs0&directConnection=true");

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MongoDB replica set", e);
        }

        return mongoContainer;
    }
}
