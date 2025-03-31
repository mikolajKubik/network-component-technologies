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
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TksApplication.class
)
@Testcontainers
public class AccountControllerTest {

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
            System.setProperty("mongodb.connection.uri",
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
                    "firstName": "Jan",
                    "lastName": "Kowalski",
                    "login": "%s",
                    "password": "Haslo123"
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
                    "password": "Haslo123"
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
    public void findAllAccountsTest() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/accounts")
                .then()
                .statusCode(200);
    }

    @Test
    public void findAccountByIdTest() {
        UUID accountId = UUID.randomUUID();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/accounts/" + accountId)
                .then()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    public void findAccountByLoginTest() {
        given()
                .header("Authorization", "Bearer " + token)
                .queryParam("login", "testUser")
                .when()
                .get("/accounts/by-login")
                .then()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    public void findByMatchingLoginTest() {
        given()
                .header("Authorization", "Bearer " + token)
                .queryParam("regex", "test.*")
                .when()
                .get("/accounts/search")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void toggleAccountStatusTest() {
        UUID accountId = UUID.randomUUID();

        given()
                .header("Authorization", "Bearer " + token)
                .queryParam("isActive", false)
                .when()
                .patch("/accounts/" + accountId + "/toggle-status")
                .then()
                .statusCode(anyOf(is(200), is(404)));
    }
}
