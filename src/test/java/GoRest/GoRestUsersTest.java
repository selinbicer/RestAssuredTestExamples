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
                        //API methoduna gitmeden önceki hazırlıklar: token, gidecek body,parametreleri
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
        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
        ;
        System.out.println("userID = " + userID);
        newUser.setId(userID);
    }

    @Test(dependsOnMethods = "createUserObject", priority = 1)
    public void updateUser() {
        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "selin bicer");
        //new user ı class objesi haline getirirsek, bu method içerisinde sadece setName ile yeni ismi gönderebiliriz.
        //bu durumda method değiştirilmeyen diğer bilgileri aynı şekilde yeniden gönderir
        //böylece ayrıca map oluşturmamıza gerek kalmaz- ancak map oluşturduğumuzda da sadece update değeri gönderiyor,
        // değişmeyen değerlere herhangi bir işlem uygulanmıyor/yeniden gönderilmiyor

        given()
                //API methoduna gitmeden önceki hazırlıklar: token, gidecek body,parametreleri
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
                //API methoduna gitmeden önceki hazırlıklar: token, gidecek body,parametreleri
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
                //API methoduna gitmeden önceki hazırlıklar: token, gidecek body,parametreleri
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

        int user3IdPath = response.path("[2].id");  //vallahi hoca böyle çözdü ben koskoca 2 test yazdım aferin Selincim:)))
        System.out.println("user3IdPath = " + user3IdPath);

        int user3IdJsonPath = response.jsonPath().getInt("[2].id");
        System.out.println("user3IdJsonPath = " + user3IdJsonPath);
    }

    @Test
    public void getUserIDPath() {
        // TODO : 3. user'ın id sini alınız (path ve jsonPath ile ayrı ayrı yapınız)
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
        // TODO : 3 tane userın id sini alınız (path ve jsonPath ile ayrı ayrı yapınız)
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
    public void getUsersExractAsArray() {   // TODO : Tüm gelen veriyi bir nesneye atınız (google araştırması)
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
        // TODO : GetUserByID testinde dönen user ı bir nesneye atınız.
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
                        .extract().jsonPath().getObject("", User.class) //path olmadığı, direkt tüm elemanı aldığımız için path boş
                ;

        System.out.println("user = " + user);
    }

    @Test
    public void getUsersV1() { //****************PATH VE JSON PATH ARASINDAKİ ASIL FARK ************************
        Response response =
                given()
                        .header("Authorization", "Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response();

        // response.as(); -> response'un tamamına uygun nesnenin oluşturulması lazım

        List<User> userList = response.jsonPath().getList("data", User.class);
        //jsonPath ile response'un bir kısmını parçalayıp alabiliyoruz
        System.out.println("userList = " + userList);

        // Daha önceki örneklerde (as) Class dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.

    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String getRandomEmail() {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase() + "@outlook.com";
    }

}

/*

    @Test(enabled = false)
    public void createUser(){

        int userID =
        given()
                //API methoduna gitmeden önceki hazırlıklar: token, gidecek body,parametreleri
                .header("Authorization","Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")
                .contentType(ContentType.JSON)
                .body("{\"name\":\""+ getRandomName() +"\", \"gender\":\"female\", \"email\":\"" + getRandomEmail()+ "\", \"status\":\"active\"}")

                .when()
                .post("users")

                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().path("id")
                ;
        System.out.println("userID = " + userID);
    }
    @Test(enabled = false)
    public void createUserMap(){
        Map<String,String> newUser = new HashMap<>();
        newUser.put("name",getRandomName());
        newUser.put("gender","female");
        newUser.put("email",getRandomEmail());
        newUser.put("status","active");

        int userID =
                given()
                        //API methoduna gitmeden önceki hazırlıklar: token, gidecek body,parametreleri
                        .header("Authorization","Bearer 525a9a56a4710f97bf3a30286d3fc602764f1c50ad195436ed56af704a6c5281")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .log().body() //nasıl gittiğini görmek istersek

                        .when()
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
                ;
        System.out.println("userID = " + userID);
    }
 */
