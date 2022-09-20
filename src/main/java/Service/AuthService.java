package Service;

import DTO.AuthDTO.LoginRequest;
import DTO.AuthDTO.RegisterRequest;
import Models.Users;
import Util.GenerateToken;
import Util.ValidateInput;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class AuthService {
    public Response Login(LoginRequest request){
        Optional<Users> user = Users.find("username = ?1",request.username).firstResultOptional();
        if(!user.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Anda Belum Terdaftar,Silahkan Daftar Terlebih Dahulu");
            return Response.status(Response.Status.NOT_FOUND).entity(result).build();
        }
        Optional userPassword = Users.find("password = ?1",request.password).firstResultOptional();
        if(!userPassword.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Kesalahan Password");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        Users userLogin = user.get();
        String token = GenerateToken.getToken(userLogin);
        JsonObject result = new JsonObject();
        result.put("data",userLogin);
        result.put("token",token);

        return Response.ok(result).build();
    }

    public Response Register(RegisterRequest request){
        //validasi
        if(!ValidateInput.EmailInput(request.email)){
            JsonObject result = new JsonObject();
            result.put("message","Pastikan Format Email Sudah Benar!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!ValidateInput.PhoneNumberInput(request.phone_number)){
            JsonObject result = new JsonObject();
            result.put("message","Gunakan Format +62 Untuk Format Nomor Telepon!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        return RegisterInsertData(request);
    }

    public Response RegisterInsertData(RegisterRequest request){

        //validate
        if(!ValidateInput.EmailInput(request.email)){
            JsonObject result = new JsonObject();
            result.put("message","Format input email Salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!ValidateInput.PhoneNumberInput(request.phone_number)){
            JsonObject result = new JsonObject();
            result.put("message","Format input phone_number Salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //===========================================================================

        LocalDateTime time = LocalDateTime.now();
        Users user = new Users();
        user.setName(request.name);
        user.setEmail(request.email);
        user.setUsername(request.username);
        user.setPassword(request.password);
        user.setPhoneNumber(request.phone_number);
        user.setUserType(request.user_type);
        user.setCreated_at(time);
        user.setUpdated_at(time);

        //save
        Users.persist(user);

        JsonObject result = new JsonObject();
        result.put("data",user);
        return Response.ok(result).build();
    }
}
