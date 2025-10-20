import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.Assert;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiTesting {
    public static void main(String[] args) {
        ApiTesting apiTesting = new ApiTesting();
        apiTesting.apiTestingPractice();
    }

    @Test
    public void apiTestingPractice() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // GET request (your first one is fine)
        given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("userId", equalTo(1))
                .extract().response();

        // POST request with body
        String requestBody = "{\n" +
                "  \"title\": \"My New Post\",\n" +
                "  \"body\": \"This is the content of my post\",\n" +
                "  \"userId\": 1\n" +
                "}";

        Response postResponse = given()

                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("title", equalTo("My New Post"))
                .body("userId", equalTo(1))
                .extract().response();

        // Print the response
        System.out.println("POST Response:");
        postResponse.prettyPrint();
    }
}
