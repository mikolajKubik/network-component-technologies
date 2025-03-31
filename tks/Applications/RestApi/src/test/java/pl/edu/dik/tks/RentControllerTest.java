package pl.edu.dik.tks;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TksApplication.class
)
@Testcontainers
@ContextConfiguration(classes = TestContainerConfig.class)
public class RentControllerTest {

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
                .path("id");
    }

    @Test
    void rentGameTest() {

        given()
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
                .statusCode(201);

    }

    @Test
    void rentGameInvalidDateTest() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
                {
                    "startDate": "2024-11-23",
                    "endDate": "2024-11-20",
                    "gameId": "%s"
                }
                """.formatted(gameId))
                .when()
                .post("/rents")
                .then()
                .statusCode(409);

    }

    @Test
    void endRentTest() {
        String id = given()
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
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/rents/end/" + id)
                .then()
                .statusCode(200);

    }

    @Test
    void endRentRentNotFoundTest() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/rents/end/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }
}
