package POJO;

import POJO.Task1;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Tasks {
    /** Task 1
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting Into POJO
     */
    @Test
    public void Task1(){
        Task1 task1=
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                //.log().body()
                .statusCode(200)
                .extract().as(Task1.class);

        System.out.println("task1 = " + task1);
    }
    /**
     * Task 2
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */
    @Test
    public void Task2(){
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .statusCode(203)
                .contentType(ContentType.TEXT);
    }
    /**
     * Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */
    @Test
    public void Taks3(){
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                //.log().body()
                .body("title",equalToIgnoringCase("quis ut nam facilis et officia qui"));
    }
    /** Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * expect content type JSON
     * expect third item have:
     *      title = "fugiat veniam minus"
     *      userId = 1
     */
    @Test
    public void Task4(){
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                //.log().body();
                .body("title[2]",equalToIgnoringCase("fugiat veniam minus"))
                .body("userId[2]",equalTo(1));
    }
}
