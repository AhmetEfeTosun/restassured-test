package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestAssuredTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    public void testGetAvailablePets() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .queryParam("status", "available")
                .when()
                .get("/pet/findByStatus")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].status", equalTo("available"))
                .time(lessThan(1800L));
    }

    @Test
    public void testCreatePet() {
        String requestBody = """
            {
              "id": 98765,
              "name": "TestPet",
              "photoUrls": ["http://example.com/photo.jpg"],
              "status": "available"
            }
            """;

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/pet")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo("TestPet"))
                .body("status", equalTo("available"))
                .time(lessThan(1500L));
    }
}