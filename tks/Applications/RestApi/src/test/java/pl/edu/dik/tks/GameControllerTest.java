package pl.edu.dik.tks;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.edu.dik.domain.model.game.Game;

import java.io.File;
import java.time.Duration;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TksApplication.class
)
@Testcontainers
public class GameControllerTest {

    private static final int MONGO_PORT = 27017;

    @Container
    static final GenericContainer<?> mongoContainer = new GenericContainer<>("mongo:8.0.1")
            .withExposedPorts(MONGO_PORT)
            .withCommand(
                    "--replSet", "rs0",
                    "--bind_ip_all"
            )
            .withStartupTimeout(Duration.ofMinutes(2));

    static {
        mongoContainer.start();

        try {
            // Initialize replica set using container's internal hostname
            org.testcontainers.containers.Container.ExecResult initResult = mongoContainer.execInContainer(
                    "mongosh", "--eval",
                    "rs.initiate({_id: 'rs0', members: [{_id: 0, host: 'localhost:" + MONGO_PORT + "'}]})"
            );

            System.out.println("Replica set initialization: " + initResult.getStdout());

            // Wait for replica set to initialize
            Thread.sleep(5000);

            // Set Spring property that matches your MongoConfig class
            System.setProperty("mongodb.connection.test.uri",
                    "mongodb://" + mongoContainer.getHost() + ":" +
                            mongoContainer.getMappedPort(MONGO_PORT) + "/?replicaSet=rs0&directConnection=true");

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MongoDB replica set", e);
        }
    }

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";

        UUID login = UUID.randomUUID();

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "firstName": "Maciek",
                    "lastName": "Kowalski",
                    "login": "%s",
                    "password": "Kowal"
                }
                """.formatted(login.toString()))
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);

        token = given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "login": "%s",
                    "password": "Kowal"
                }
                """.formatted(login.toString()))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .asString();

    }

    @Test
    public void createGameTest() {
        String id = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
                {
                    "name": "Chess",
                    "pricePerDay": 5,
                    "minPlayers": 2,
                    "maxPlayers": 4
                }
                """)
                .when()
                .post("/games")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/games/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    public void createGameWithInvalidMinPlayersTest() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
            {
                "name": "Chess",
                "pricePerDay": 5,
                "minPlayers": 0,
                "maxPlayers": 4
            }
            """)
                .when()
                .post("/games")
                .then()
                .statusCode(400);
    }

    @Test
    public void updateGameTest() {
        // Create a new game to later update.
        String gameId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
                {
                    "name": "Chess",
                    "pricePerDay": 5,
                    "minPlayers": 2,
                    "maxPlayers": 4
                }
                """)
                .when()
                .post("/games")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Create update JSON with the new details.
        String updateGameJson = """
            {
                "id": "%s",
                "name": "Updated Chess",
                "pricePerDay": 7,
                "minPlayers": 2,
                "maxPlayers": 5
            }
            """.formatted(gameId);

        // Submit the update request.
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(updateGameJson)
                .when()
                .put("/games")
                .then()
                .statusCode(200);
    }


}
