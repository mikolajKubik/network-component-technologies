//package pl.edu.dik.rest.controller;
//
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ContextConfiguration;
//
//
//import static io.restassured.RestAssured.given;
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
//
////@ContextConfiguration(
////    classes = TestApplicationConfig.class
////)
//@SpringBootTest(
//        webEnvironment = RANDOM_PORT
//)
////@Import(TestSecurityConfig.class)
//class GameControllerTest {
//
//    @BeforeAll
//    public static void setup() {
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = 8080;
//    }
//
//    @Test
//    public void createGameTest() {
//        String id = given()
//                .contentType(ContentType.JSON)
//                .body("""
//                  {
//                    "name": "Monopoly",
//                    "pricePerDay": 5,
//                    "minPlayers": 2,
//                    "maxPlayers": 4
//                  }
//                """)
//                .when()
//                .post("/api/games")
//                .then()
//                .statusCode(201)
//                .extract()
//                .path("id");
//
//        given()
//                .when()
//                .get("/api/games/{id}", id)
//                .then()
//                .statusCode(200);
//    }
//}