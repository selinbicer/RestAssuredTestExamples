package Campus;

import Campus.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class CountryTest {
    public Cookies cookies;

    @BeforeClass
    public void loginCampus(){
        baseURI = "https://demo.mersys.io/";

        //{"username": "richfield.edu", "password": "Richfield2020!", "rememberMe": "true" }
        Map<String,String> credential = new HashMap<>();
        credential.put("username","richfield.edu");
        credential.put("password","Richfield2020!");
        credential.put("rememberMe","true");

        cookies =
        given()
                .contentType(ContentType.JSON)
                .body(credential)
                .when()
                .post("auth/login")
                .then()
                .statusCode(200)
                //.log().all()
                .extract().response().getDetailedCookies()
        ;
    }

    String countryID;
    String countryName;
    String countryCode;
    @Test
    public void createCountry(){

        countryName = getRandomCountryName();
        countryCode = getRandomCode();

        Country country = new Country();
        country.setName(countryName); //generate country name
        country.setCode(countryCode); //generate country code

        countryID =
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("school-service/api/countries")
                .then()
                .statusCode(201)
                .log().body()
                .extract().jsonPath().getString("id")
                ;

    }
    @Test (dependsOnMethods = "createCountry", priority = 1)
    public void createCountryNegative(){
        Country country = new Country();
        country.setName(countryName);
        country.setCode(countryCode);

                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(country)
                        .when()
                        .post("school-service/api/countries")
                        .then()
                        .statusCode(400)
                        .body("message",equalTo("The Country with Name \""+countryName+"\" already exists."))
                        .log().body()
        ;
    }
    @Test (dependsOnMethods = "createCountry", priority = 2)
    public void updateCountry(){
        countryName = getRandomCountryName();

       Country country = new Country();
       country.setId(countryID);
       country.setName(countryName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .put("school-service/api/countries")
                .then()
                .statusCode(200)
                .body("name",equalTo(countryName))
                .log().body()
        ;
    }
    @Test (dependsOnMethods = "updateCountry")
    public void deleteCountryById(){

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("countryID",countryID)
                .when()
                .delete("school-service/api/countries/{countryID}")
                .then()
                .statusCode(200)
                .log().body()
        ;
    }
    @Test (dependsOnMethods = "deleteCountryById", priority = 1)
    public void deleteCountryByIdNegative(){

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("countryID",countryID)
                .when()
                .delete("school-service/api/countries/{countryID}")
                .then()
                .statusCode(400)
                .log().body()
        ;
    }
    @Test (dependsOnMethods = "deleteCountryById", priority = 2)
    public void updateCountryByIdNegative(){
        countryName = getRandomCountryName();

        Country country = new Country();
        country.setId(countryID);
        country.setName(countryName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)

                .when()
                .put("school-service/api/countries/")
                .then()
                .statusCode(400)
                .log().body()
        ;
    }
    public String getRandomCountryName(){
        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }

    public String getRandomCode(){
        return RandomStringUtils.randomNumeric(3).toLowerCase();
    }
}
