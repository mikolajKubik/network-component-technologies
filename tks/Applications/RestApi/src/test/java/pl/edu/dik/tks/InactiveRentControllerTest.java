package pl.edu.dik.tks;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TksApplication.class
)
@Testcontainers
public class InactiveRentControllerTest {

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
    private UUID login;
    private String gameId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";

        login = UUID.randomUUID();

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

        gameId = given()
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
                .path("id")
                .toString();

    }

    @Test
    public void endRentTest() {
        // Create a rent and extract its id
        String rentId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
                {
                    "startDate": "2024-11-23",
                    "endDate": "2024-11-30",
                    "gameId": "%s"
                }
                """.formatted(gameId.toString()))
                .when()
                .post("/rents")
                .then()
                .statusCode(201)
                .extract()
                .path("id")
                .toString();

        // End the rent using the extracted id
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/rents/end/" + rentId)
                .then()
                .statusCode(200);
    }

    @Test
    public void endAlreadyEndedRentTest() {
        // Create a rent and extract its id
        String rentId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
            {
                "startDate": "2024-11-23",
                "endDate": "2024-11-30",
                "gameId": "%s"
            }
            """.formatted(gameId))
                .when()
                .post("/rents")
                .then()
                .statusCode(201)
                .extract()
                .path("id")
                .toString();

        // End the rent for the first time successfully
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/rents/end/" + rentId)
                .then()
                .statusCode(200);

        // Attempt to end the already ended rent and expect an error (404)
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/rents/end/" + rentId)
                .then()
                .statusCode(404);
    }

}
