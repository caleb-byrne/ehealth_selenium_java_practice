# API Testing with REST Assured

This guide covers API testing using REST Assured in Java, based on practical examples and best practices.

## Table of Contents
- [Setup](#setup)
- [Basic API Testing](#basic-api-testing)
- [Request and Response Handling](#request-and-response-handling)
- [JSON Path and Assertions](#json-path-and-assertions)
- [Request Bodies](#request-bodies)
- [Complex JSON Structures](#complex-json-structures)
- [POJO Classes](#pojo-classes)
- [Best Practices](#best-practices)

## Setup

### Step 1: Add REST Assured Dependency

Add REST Assured to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.15.0</version>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
    <!-- REST Assured for API testing -->
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>5.3.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Step 2: Import Required Libraries

```java
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.Assert;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
```

## Basic API Testing

### Understanding REST Assured Syntax

REST Assured uses a **BDD-style (Given-When-Then)** syntax:

```java
@Test
public void basicGetRequest() {
    // Set base URI (root URL for all requests)
    RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    
    // Make API call with assertions
    Response response = given()           // Setup/Prerequisites
        .when()                          // The Action
            .get("/posts/1")             // GET https://jsonplaceholder.typicode.com/posts/1
        .then()                          // Assertions/Validations
            .statusCode(200)             // Assert HTTP status = 200 OK
            .contentType("application/json")  // Assert response is JSON
            .body("userId", equalTo(1))  // Assert JSON field userId = 1
            .body("id", equalTo(1))      // Assert JSON field id = 1
            .body("title", notNullValue()) // Assert title field exists
        .extract().response();           // Get full response for additional checks
    
    // Print response for debugging
    System.out.println("Response: " + response.asString());
}
```

### Running Tests

```bash
# Run specific test class
mvn test -Dtest=ApiTesting

# Run all tests
mvn test
```

## Request and Response Handling

### GET Requests

```java
@Test
public void getAllPosts() {
    RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    
    given()
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))        // Assert array is not empty
            .body("[0].userId", notNullValue())    // Assert first post has userId
            .body("[0].title", notNullValue());     // Assert first post has title
}
```

### POST Requests with Request Body

```java
@Test
public void createPost() {
    RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

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
            .statusCode(201)  // POST usually returns 201 Created
            .body("title", equalTo("My New Post"))
            .body("userId", equalTo(1))
            .extract().response();

    System.out.println("POST Response:");
    postResponse.prettyPrint();
}
```

## JSON Path and Assertions

### Basic JSON Path Extraction

```java
@Test
public void jsonPathExamples() {
    RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

    Response response = given()
            .when()
            .get("/posts/1")
            .then()
            .statusCode(200)
            .extract().response();
            
    // Extract specific fields
    System.out.println("Title: " + response.jsonPath().getString("title"));
    System.out.println("User ID: " + response.jsonPath().getInt("userId"));
    System.out.println("Post ID: " + response.jsonPath().getInt("id"));
    System.out.println("Body: " + response.jsonPath().getString("body"));
}
```

### Nested JSON Assertions

For nested objects:
```json
{
  "user": {
    "name": "John",
    "address": {
      "street": "123 Main St",
      "city": "Boston"
    }
  }
}
```

```java
@Test
public void nestedJsonAssertions() {
    given()
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body("name", notNullValue())
            .body("address.street", notNullValue())      // Nested object
            .body("address.city", notNullValue())        // Nested object
            .body("address.geo.lat", notNullValue());    // Deeply nested
}
```

### Array Assertions

For JSON arrays:
```json
{
  "posts": [
    {"id": 1, "title": "Post 1"},
    {"id": 2, "title": "Post 2"}
  ],
  "tags": ["java", "api", "testing"]
}
```

```java
@Test
public void arrayAssertions() {
    given()
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))              // Array size
            .body("[0].id", equalTo(1))                  // First item's id
            .body("[0].title", equalTo("Post 1"))        // First item's title
            .body("id", hasItems(1, 2))                  // All IDs in array
            .body("title", hasItem("Post 1"));           // Contains specific title
}
```

### Flatten for Complex Nested Arrays

```java
@Test
public void flattenExample() {
    // For JSON like: {"users": [{"posts": [...]}, {"posts": [...]}]}
    given()
        .when()
            .get("/complex-data")
        .then()
            .statusCode(200)
            // Get ALL titles from ALL users' posts
            .body("users.posts.flatten().title", 
                  hasItems("Post A", "Post B", "Post C", "Post D"));
}
```

**What flatten() does:**
- Converts `[[a,b], [c,d]]` into `[a,b,c,d]`
- Use when you have arrays inside arrays and want to access all elements together

## Request Bodies

### Using Maps (Recommended for most cases)

```java
@Test
public void postWithMap() {
    RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("title", "My New Post");
    requestBody.put("body", "This is the content of my post");
    requestBody.put("userId", 1);

    Response response = given()
            .contentType("application/json")  // Cleaner than .header()
            .body(requestBody)                // Auto-converts Map to JSON
            .when()
            .post("/posts")
            .then()
            .statusCode(201)
            .body("title", equalTo("My New Post"))
            .extract().response();

    response.prettyPrint();
}
```

### Complex Nested Request Bodies

```java
@Test
public void complexNestedRequest() {
    RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

    // Nested Maps for complex JSON
    Map<String, Object> address = new HashMap<>();
    address.put("street", "123 Main St");
    address.put("city", "Boston");
    address.put("zipcode", "02101");
    
    Map<String, Object> geo = new HashMap<>();
    geo.put("lat", "42.3601");
    geo.put("lng", "-71.0589");
    address.put("geo", geo);  // Nested object
    
    List<String> hobbies = Arrays.asList("coding", "reading", "gaming");
    
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("name", "John Doe");
    requestBody.put("email", "john@example.com");
    requestBody.put("address", address);      // Nested object
    requestBody.put("hobbies", hobbies);      // Array
    requestBody.put("isActive", true);        // Boolean

    Response response = given()
            .contentType("application/json")
            .body(requestBody)
            .when()
            .post("/users")
            .then()
            .statusCode(201)
            .body("name", equalTo("John Doe"))
            .body("address.city", equalTo("Boston"))           // Nested assertion
            .body("address.geo.lat", equalTo("42.3601"))       // Deeply nested
            .body("hobbies", hasItems("coding", "reading"))    // Array assertion
            .extract().response();

    response.prettyPrint();
}
```

## POJO Classes

### Simple POJO Example

Create `PostRequest.java`:
```java
public class PostRequest {
    private String title;
    private String body;
    private int userId;
    
    // Default constructor (required for JSON deserialization)
    public PostRequest() {}
    
    // Constructor with parameters
    public PostRequest(String title, String body, int userId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
    }
    
    // Getters and setters (required for JSON serialization)
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
```

### Using POJO in Tests

```java
@Test
public void postWithPojo() {
    RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

    PostRequest requestBody = new PostRequest(
        "My New Post",
        "This is the content of my post", 
        1
    );

    Response response = given()
            .contentType("application/json")
            .body(requestBody)  // Auto-converts POJO to JSON
            .when()
            .post("/posts")
            .then()
            .statusCode(201)
            .body("title", equalTo("My New Post"))
            .extract().response();

    response.prettyPrint();
}
```

### Complex POJO with Nested Objects

Create separate classes for nested structures:

`Address.java`:
```java
public class Address {
    private String street;
    private String city;
    private String zipcode;
    
    public Address() {}
    
    public Address(String street, String city, String zipcode) {
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }
    
    // Getters and setters
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
}
```

`UserRequest.java`:
```java
import java.util.List;

public class UserRequest {
    private String name;
    private String email;
    private Address address;  // References the Address POJO
    private List<String> hobbies;
    
    public UserRequest() {}
    
    public UserRequest(String name, String email, Address address, List<String> hobbies) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.hobbies = hobbies;
    }
    
    // Getters and setters...
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    
    public List<String> getHobbies() { return hobbies; }
    public void setHobbies(List<String> hobbies) { this.hobbies = hobbies; }
}
```

Using complex POJOs:
```java
@Test
public void complexPojoTest() {
    Address address = new Address("123 Main St", "Boston", "02101");
    UserRequest userRequest = new UserRequest(
        "John Doe",
        "john@example.com", 
        address,
        Arrays.asList("coding", "reading")
    );

    Response response = given()
            .contentType("application/json")
            .body(userRequest)
            .when()
            .post("/users")
            .then()
            .statusCode(201)
            .body("name", equalTo("John Doe"))
            .body("address.city", equalTo("Boston"))
            .extract().response();
    
    response.prettyPrint();
}
```

## Best Practices

### When to Use Each Approach

#### Arrays.asList() vs POJO Classes

**Use `Arrays.asList()` for:**
- Simple arrays of primitive values (strings, numbers, booleans)
- When array elements don't need complex structure

```java
List<String> hobbies = Arrays.asList("coding", "reading", "gaming");
List<Integer> scores = Arrays.asList(85, 92, 78, 96);
```

**Use POJO classes for:**
- Complex objects with multiple properties
- Arrays of complex objects
- Reusable data structures

```java
// Each address has multiple fields - needs a POJO
List<Address> addresses = Arrays.asList(homeAddress, workAddress);
```

### Request Body Recommendations

- **Simple to moderate complexity:** Use **Maps**
- **Very complex or reusable:** Use **POJOs**
- **Dynamic/conditional JSON:** Use JSON Builder patterns

### Assertion Strategy

1. **Use REST Assured's built-in assertions** in the `.then()` section for immediate validation
2. **Extract response** only when you need custom assertions or detailed response analysis
3. **Combine both approaches** for comprehensive testing

### File Organization

- Put POJOs in `src/test/java` for test-specific models
- Create a separate package like `src/test/java/models/` for multiple POJOs
- Keep test logic separate from data models

### Common Hamcrest Matchers

```java
// Equality
.body("field", equalTo(value))

// Null checks
.body("field", notNullValue())
.body("field", nullValue())

// Array/Collection matchers
.body("array", hasSize(3))
.body("array", hasItems("a", "b"))
.body("array", hasItem("a"))

// Numeric comparisons
.body("size()", greaterThan(0))
.body("price", lessThan(100.0))

// String matchers
.body("title", containsString("Post"))
.body("email", endsWith("@example.com"))
```

## Example Test Class

```java
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.Assert;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.util.*;

public class ApiTesting {
    
    public static void main(String[] args) {
        ApiTesting apiTesting = new ApiTesting();
        apiTesting.apiTestingPractice();
    }

    @Test
    public void apiTestingPractice() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // GET request with assertions
        Response response = given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("userId", equalTo(1))
                .body("title", notNullValue())
                .extract().response();

        // Print response details
        System.out.println("Response: " + response.asString());
        System.out.println("Title: " + response.jsonPath().getString("title"));
        System.out.println("User ID: " + response.jsonPath().getInt("userId"));

        // POST request with Map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "My New Post");
        requestBody.put("body", "This is the content");
        requestBody.put("userId", 1);

        Response postResponse = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("title", equalTo("My New Post"))
                .extract().response();

        System.out.println("POST Response:");
        postResponse.prettyPrint();

        // Additional assertions
        Assert.assertEquals(201, postResponse.getStatusCode());
        Assert.assertNotNull(postResponse.jsonPath().getString("title"));
    }
}
```

This guide covers the essential concepts and patterns for API testing with REST Assured, from basic requests to complex nested JSON structures and POJO implementations.