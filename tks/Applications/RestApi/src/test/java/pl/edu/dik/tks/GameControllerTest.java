package pl.edu.dik.tks;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import pl.edu.dik.domain.model.game.Game;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TksApplication.class
)
public class GameControllerTest {

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
