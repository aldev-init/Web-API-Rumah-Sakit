package Service;

import Models.UserPremission;
import Models.Users;
import Util.HashingSystem;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@ApplicationScoped
public class InitialAdminService {

    @Inject
    HashingSystem hash;

    @ConfigProperty(name = "NAME_ADMIN")
    String name;

    @ConfigProperty(name = "EMAIL_ADMIN")
    String email;

    @ConfigProperty(name = "PASSWORD_ADMIN")
    String password;

    @ConfigProperty(name = "USERNAME_ADMIN")
    String username;

    @ConfigProperty(name = "PHONE_NUMBER_ADMIN")
    String phoneNumber;

    @ConfigProperty(name = "USER_TYPE_ADMIN")
    String userType;
    public Response GenerateInitialAdmin(){

        //cek is super Admin present ?
        Optional<Users> superAdmin = Users.find("user_type = ?1",userType).firstResultOptional();

        if(!superAdmin.isPresent()){
            LocalDateTime time = LocalDateTime.now();
            Users users = new Users();
            users.setName(name);
            users.setEmail(email);
            users.setPassword(hash.Encrypt(password));
            users.setUsername(username);
            users.setPhoneNumber(phoneNumber);
            users.setUserType(userType);
            users.setCreated_at(time);
            users.setUpdated_at(time);
            //save
            Users.persist(users);

            //premission
            Optional<UserPremission> userPremission = UserPremission.find("user_id = ?1",users.getId()).firstResultOptional();
            if(!userPremission.isPresent()){
                String[] validPremission = {"SELECT","CREATE","UPDATE","DELETE"};
                for(int i = 0;i<validPremission.length;i++){
                    UserPremission premission = new UserPremission();
                    premission.setName(validPremission[i]);
                    premission.setUserId(users);
                    UserPremission.persist(premission);
                }
            }
            JsonObject result = new JsonObject();
            result.put("message","Initialize Super Admin Successfull");
            return Response.ok(result).build();
        }
        JsonObject result = new JsonObject();
        result.put("message","Initialize Has Been Done!");
        return Response.ok(result).build();
    }
}
