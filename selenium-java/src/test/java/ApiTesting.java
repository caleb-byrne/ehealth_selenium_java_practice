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

        given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("userId", equalTo(1))
                .extract().response();

        // System.out.println(getResponse.asPrettyString());
        // System.out.println(getResponse.asString());
        // getResponse.prettyPrint();

        // Assert.assertEquals(200, getResponse.getStatusCode());
        // Assert.assertNotNull(getResponse.jsonPath().getString("body"));
    }
}
