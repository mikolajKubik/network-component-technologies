package pl.edu.dik.tks;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
public class SharedContainerConfig {

    private static final String DOCKER_COMPOSE_PATH = "/Users/mikson/Projects/STUDIA_VI_sem/MKWA_SR_1015_07/tks/docker-compose.yml";

    @Container
    public static final DockerComposeContainer<?> container;

    static {
        container = new DockerComposeContainer<>(new File(DOCKER_COMPOSE_PATH))
                .withLocalCompose(true);

        container.start();

        try {
            // Wait for MongoDB cluster to warm up
            Thread.sleep(70000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Container startup wait was interrupted", e);
        }
    }

}
