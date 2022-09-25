import DTO.AuthDTO.LoginRequest;
import DTO.AuthDTO.RegisterRequest;
import Models.Users;
import Service.AuthService;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Optional;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest {

    @Inject
    AuthService authService;

    @ConfigProperty(name = "USERNAME_ADMIN")
    String username;
    @ConfigProperty(name = "PASSWORD_ADMIN")
    String password;

    //login test
    @Test
    @Order(1)
    void login(){
        LoginRequest request = new LoginRequest();
        request.username = username;
        request.password = password;

        Assertions.assertDoesNotThrow(() ->{
            Response response = authService.Login(request);
            //cek status
            Assertions.assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());

            //cek data/response yang dikembalikan
            JsonObject result = new JsonObject(response.getEntity().toString());
            Assertions.assertTrue(result.containsKey("data"));
            Assertions.assertTrue(result.containsKey("token"));
        });
    }

    //loginfailed test
    @Test
    @Order(2)
    void loginUsernameFailed(){
        LoginRequest request = new LoginRequest();
        request.username = "adasdsad";
        request.password = "kajsdkjhas";

        Assertions.assertDoesNotThrow(() ->{
            Response response = authService.Login(request);
            //cek status
            Assertions.assertEquals(Response.Status.NOT_FOUND.getStatusCode(),response.getStatus());

            //cek data/response yang dikembalikan
            JsonObject result = new JsonObject(response.getEntity().toString());
            Assertions.assertTrue(result.containsKey("message"));
        });
    }

    @Test
    @Order(3)
    void loginPasswordFailed(){
        LoginRequest request = new LoginRequest();
        request.username = username;
        request.password = "kajsdkjhas";

        Assertions.assertDoesNotThrow(() ->{
            Response response = authService.Login(request);
            //cek status
            Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),response.getStatus());

            //cek data/response yang dikembalikan
            JsonObject result = new JsonObject(response.getEntity().toString());
            Assertions.assertTrue(result.containsKey("message"));
        });
    }

    @Test
    @Order(4)
    void Register(){
        PanacheMock.mock(Users.class);
        //jangan save data
        PanacheMock.doNothing().when(Users.class).persist();

        RegisterRequest request = new RegisterRequest();
        request.name = "Muhammad Alghifari";
        request.email = "muhammadalghifari4321@gmail.com";
        request.username = "aldev";
        request.password = "aldev123";
        request.phone_number = "+6282117707292";
        request.user_type = "Operator";

        Assertions.assertDoesNotThrow(()->{
            Response response = authService.Register(request);

            //cek status code
            Assertions.assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());

            //cek data yang dikembalikan
            JsonObject result = new JsonObject(response.getEntity().toString());
            Assertions.assertTrue(result.containsKey("data"));
        });
    }
}
