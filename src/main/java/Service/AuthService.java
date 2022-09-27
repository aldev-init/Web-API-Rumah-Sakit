package Service;

import DTO.AuthDTO.LoginRequest;
import DTO.AuthDTO.PremissionRequest;
import DTO.AuthDTO.RegisterRequest;
import Models.UserPremission;
import Models.Users;
import Util.GenerateToken;
import Util.HashingSystem;
import Util.UserUtil;
import Util.ValidateInput;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    @Inject
    JWTParser parser;
    @Inject
    HashingSystem hash;

    @ConfigProperty(name = "PAGINATE_PER_PAGE")
    int paginate;

    public Response Login(LoginRequest request) throws ParseException {
        //hashing initialize
        String password = hash.Encrypt(request.password);

        Optional<Users> user = Users.find("username = ?1",request.username).firstResultOptional();
        if(!user.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Anda Belum Terdaftar,Silahkan Daftar Terlebih Dahulu");
            return Response.status(Response.Status.NOT_FOUND).entity(result).build();
        }
        Optional userPassword = Users.find("password = ?1",password).firstResultOptional();
        Users userLogin = user.get();
        //verifiy password
        if(userPassword.isPresent() && hash.Verify(userLogin.getPassword(),hash.Encrypt(request.password)) || !userPassword.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Kesalahan Password");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        String token = GenerateToken.getToken(userLogin);
        JsonObject result = new JsonObject();
        result.put("data",userLogin);
        result.put("token",token);
        /*JsonWebToken jwt = parser.parse(token);
        result.put("parse",jwt);*/

        //put information about login user
        UserUtil.setRole_type(userLogin.getUserType());

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
        user.setPassword(hash.Encrypt(request.password));
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


    //premission
    public Response UserPremission(long id, PremissionRequest request){
        //is super admin?
        if(!UserUtil.getRole_type().equals("Super Admin")){
            JsonObject result = new JsonObject();
            result.put("message","Anda tidak memiliki izin untuk mengelola izin akun lain.");
            return Response.ok(result).build();
        }

        Optional<Users> premissionOptional = Users.find("id = ?1",id).firstResultOptional();
        if(!premissionOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","user_id tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        //jika akun yang diubah premission,akun super admin
        if(premissionOptional.get().getUserType().equals("Super Admin")){
            JsonObject result = new JsonObject();
            result.put("message","Anda tidak bisa mengubah izin akun super admin,karena akun tersebut mempunyai hak Istimewa.");
            return Response.ok(result).build();
        }


        //list optional premission
        Optional<UserPremission> SELECT = UserPremission.find("user_id = ?1 AND name = ?2",id,"SELECT").firstResultOptional();
        Optional<UserPremission> CREATE = UserPremission.find("user_id = ?1 AND name = ?2",id,"CREATE").firstResultOptional();
        Optional<UserPremission> UPDATE = UserPremission.find("user_id = ?1 AND name = ?2",id,"UPDATE").firstResultOptional();
        Optional<UserPremission> DELETE = UserPremission.find("user_id = ?1 AND name = ?2",id,"DELETE").firstResultOptional();

        LocalDateTime time = LocalDateTime.now();

        //SELECT
        if(SELECT.isPresent()){
            //hapus premission jika request false,dan premission ada
            if(request.SELECT == false && SELECT.isPresent()){
                SELECT.get().delete();
            }
        }

        if(!SELECT.isPresent()){
            //jika belum ada premission lalu user membuat baru
            if(request.SELECT == true && !SELECT.isPresent()){
                UserPremission select = new UserPremission();
                select.setUserId(Users.findById(id));
                select.setName("SELECT");
                select.setCreated_at(time);
                select.setUpdated_at(time);
                //save
                UserPremission.persist(select);
            }
        }

        //CREATE
        if(CREATE.isPresent()){
            //hapus premission jika request false,dan premission ada
            if(request.CREATE == false && CREATE.isPresent()){
                CREATE.get().delete();
            }
        }

        if(!CREATE.isPresent()){
            //jika belum ada premission lalu user membuat baru
            if(request.CREATE == true && !CREATE.isPresent()){
                UserPremission create = new UserPremission();
                create.setUserId(Users.findById(id));
                create.setName("CREATE");
                create.setCreated_at(time);
                create.setUpdated_at(time);
                //save
                UserPremission.persist(create);
            }
        }

        //UPDATE
        if(UPDATE.isPresent()){
            //hapus premission jika request false,dan premission ada
            if(request.UPDATE == false && UPDATE.isPresent()){
                UPDATE.get().delete();
            }
        }

        if(!UPDATE.isPresent()){
            //jika belum ada premission lalu user membuat baru
            if(request.UPDATE == true && !UPDATE.isPresent()){
                UserPremission update = new UserPremission();
                update.setUserId(Users.findById(id));
                update.setName("UPDATE");
                update.setCreated_at(time);
                update.setUpdated_at(time);
                //save
                UserPremission.persist(update);
            }
        }

        //DELETE
        if(DELETE.isPresent()){
            //hapus premission jika request false,dan premission ada
            if(request.DELETE == false && DELETE.isPresent()){
                DELETE.get().delete();
            }
        }

        if(!DELETE.isPresent()){
            //jika belum ada premission lalu user membuat baru
            if(request.DELETE == true && !DELETE.isPresent()){
                UserPremission delete = new UserPremission();
                delete.setUserId(Users.findById(id));
                delete.setName("DELETE");
                delete.setCreated_at(time);
                delete.setUpdated_at(time);
                //save
                UserPremission.persist(delete);
            }
        }

        JsonObject result = new JsonObject();
        result.put("message","User Premission Suskes Diubah");
        return Response.ok(result).build();
    }

    //get all premission account
    public Response GetAllPremission(int page){
        //is super admin?
        if(!UserUtil.getRole_type().equals("Super Admin")){
            JsonObject result = new JsonObject();
            result.put("message","Anda tidak dapat melihat izin akun lain.");
            return Response.ok(result).build();
        }

        List<UserPremission> premissionList = UserPremission.findAll().page(page,paginate).list();
        long premissionSize = UserPremission.findAll().list().size();
        Object totalPage = (premissionSize + paginate - 1) / paginate;
        JsonObject result = new JsonObject();
        result.put("data",premissionList);
        result.put("total",premissionSize);
        result.put("totalPage",totalPage);

        return Response.ok(result).build();
    }
}
