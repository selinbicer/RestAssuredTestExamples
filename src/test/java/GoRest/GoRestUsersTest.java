package GoRest;

import GoRest.Model.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTest {
    int userID = 0;

    @BeforeClass
    void Setup() {
        baseURI = "https://gorest.co.in/public/v2/";
    }

    @Test
    public void createUserObject() {
        User newUser = new User();
        newUser.setName(getRandomName());
        newUser.setGender("female");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");

        userID =
                given()
                        .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")
                        .contentType(ContentType.JSON)
                        .body(newUser)

                        .when()
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        //.extract().path("id")
                        .extract().jsonPath().getInt("id")

        ;
        System.out.println("userID = " + userID);
        newUser.setId(userID);
    }

    @Test(dependsOnMethods = "createUserObject", priority = 1)
    public void updateUser() {
        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "selin bicer");

        given()

                .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")
                .contentType(ContentType.JSON)
                .body(updateUser)
                .log().body()
                .pathParam("userID", userID)

                .when()
                .put("users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo("selin bicer"))
        ;

    }

    @Test(dependsOnMethods = "createUserObject", priority = 2)
    public void getUserById() {
        given()

                .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", userID)

                .when()
                .get("users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
        ;
    }

    @Test(dependsOnMethods = "createUserObject", priority = 3)
    public void deleteUserByID() {
        given()

                .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", userID)

                .when()
                .delete("users/{userID}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteUserByID")
    public void deleteUserByIdNegative() {
        given()
                .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", userID)

                .when()
                .delete("users/{userID}")

                .then()
                .log().body()
                .statusCode(404)
        ;
    }

    @Test
    public void getUsers() {
        Response response =
                given()
                        .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")

                        .when()
                        .get("users")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response();

        int user3IdPath = response.path("[2].id");
        System.out.println("user3IdPath = " + user3IdPath);

        int user3IdJsonPath = response.jsonPath().getInt("[2].id");
        System.out.println("user3IdJsonPath = " + user3IdJsonPath);
    }

    @Test
    public void getUserIDPath() {

        int idOfUser3 =
                given()
                        .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")

                        .when()
                        .get("users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        //.extract().path("[2].id")
                        .extract().jsonPath().getInt("[2].id");

        System.out.println("idOfUser3 = " + idOfUser3);

    }

    @Test
    public void getUserIDJsonPath() {

        List<Integer> idLİst =
                given()
                        .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")

                        .when()
                        .get("users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().jsonPath().getList("id");
        for (int i = 1; i <= 3; i++) {
            System.out.println(i + ". item's id is " + idLİst.get(i));

        }
    }

    @Test
    public void getUsersExractAsArray() {
        Response response =
                given()
                        .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")

                        .when()
                        .get("users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response();
        User[] usersArray = response.as(User[].class);
        System.out.println("Arrays.toString(usersArray) = " + Arrays.toString(usersArray));

        List<User> userList = response.jsonPath().getList("", User.class);
        System.out.println("userList = " + userList);

    }

    @Test(dependsOnMethods = "createUserObject", priority = 4)
    public void getUserByIdExtract() {

        User user =
                given()
                        .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")
                        .pathParam("userID", userID)

                        .when()
                        .get("users/{userID}")

                        .then()
                        .log().body()
                        .statusCode(200)
                        //.extract().as(User.class)
                        .extract().jsonPath().getObject("", User.class)
                ;

        System.out.println("user = " + user);
    }

    @Test
    public void getUsersV1() {
        Response response =
                given()
                        .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response();

        List<User> userList = response.jsonPath().getList("data", User.class);
        System.out.println("userList = " + userList);

    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String getRandomEmail() {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase() + "@outlook.com";
    }

}
