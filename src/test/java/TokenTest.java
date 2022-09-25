import DTO.AuthDTO.LoginRequest;
import Service.AuthService;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TokenTest {

    @Inject
    AuthService authService;

    //memasukan token asal
    @Test
    @Order(1)
    void tokenInvalid(){
        given()
                .header("Authorization","Bearer asdasldkjjw772329kad_q")
                .when().get("/daftarrawatinap")
                .then()
                .statusCode(401);
    }

    @Test
    @Order(2)
    void noToken(){
        given()
                .when().get("/daftarrawatinap")
                .then()
                .statusCode(401);
    }

    @Test
    @Order(3)
    void tokenValid(){
        LoginRequest request = new LoginRequest();
        request.username = "adminaja";
        request.password = "admin@133";

        Assertions.assertDoesNotThrow(()->{
            Response response = authService.Login(request);

            //cek status code
            Assertions.assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());

            //cek data yang dikembalikan
            JsonObject result = new JsonObject(response.getEntity().toString());
            Assertions.assertTrue(result.containsKey("data"));
            Assertions.assertTrue(result.containsKey("token"));

            String token  =result.getString("token");
            //cek premission
            given()
                    .header("Authorization","Bearer "+token)
                    .when().get("/daftarrawatinap")
                    .then()
                    .statusCode(200);
        });
    }

}
