import Controller.DaftarRawatInapController;
import DTO.AuthDTO.LoginRequest;
import Models.UserPremission;
import Models.Users;
import Service.AuthService;
import Service.DaftarRawatInapService;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Optional;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PremissionTest {

    @Inject
    AuthService authService;
    @Inject
    DaftarRawatInapController daftarRawatInapController;

    Users dummyAccount(){
        LocalDateTime time = LocalDateTime.now();

        Users user = new Users();
        user.setName("Muhammad Alghifari");
        user.setEmail("muhammadalghifari4321@gmail.com");
        user.setUsername("aldev");
        user.setPassword("aldev112");
        user.setPhoneNumber("+6282117707292");
        user.setUserType("Operator");
        user.setCreated_at(time);
        user.setUpdated_at(time);
        return user;
    }

    @Test
    @Order(1)
    //simulasi user yang tidak memiliki izin,mengakses resource API Daftar Rawat Inap
    void CantAccess(){
        PanacheMock.mock(Users.class);
        PanacheQuery query = Mockito.mock(PanacheQuery.class);
        Mockito.when(Users.find("username = ?1","aldev")).thenReturn(query);
        Mockito.when(query.firstResultOptional()).thenReturn(Optional.of(dummyAccount()));
        Mockito.when(Users.find("password = ?1","aldev112")).thenReturn(query);
        Mockito.when(query.firstResultOptional()).thenReturn(Optional.of(dummyAccount()));

        LoginRequest request = new LoginRequest();
        request.username = "aldev";
        request.password = "aldev112";

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
                    .statusCode(403);
        });

    }

    //simulasi user yang memiliki izin,mengakses resource API Daftar Rawat Inap
    @Test
    @Order(2)
    void CanAccess(){
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
