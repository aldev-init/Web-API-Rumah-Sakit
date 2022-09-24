package Controller;

import DTO.AuthDTO.LoginRequest;
import DTO.AuthDTO.PremissionRequest;
import DTO.AuthDTO.RegisterRequest;
import Service.AuthService;
import Service.InitialAdminService;
import io.smallrye.jwt.auth.principal.ParseException;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    AuthService authService;

    @Inject
    InitialAdminService adminService;

    //init for super admin
    @POST
    @Path("/init")
    @Transactional
    @PermitAll
    @Operation(summary = "Untuk Inisialisasi Super Admin",description = "Jika anda baru membuka project ini," +
            "diharuskan untuk inisialisasi akun Super Admin.<br>" +
            "<br>"+
            "tekan tombol \"<b>Try Out</b>\", lalu tekan tombol \"<b>Execute</b>\".<br>" +
            "Voila!! Akun Super Admin Akan Terbuat Otomatis<br>")
    public Response InitSuperAdmin(){
        return adminService.GenerateInitialAdmin();
    }


    @POST
    @Path("/login")
    @PermitAll
    @Operation(summary = "Untuk Login Akun",description = "Sebelum anda mengakses semua API dalam web ini anda diharuskan " +
            "untuk login menggunakan akun anda terlebih dahulu untuk mendapatkan token.nantinya token itu digunakan " +
            "untuk mengakses semua resource dalam web ini.<br>" +
            "<br>" +
            "Tekan Tombol \"<b>Try Out</b>\", lalu anda akan melihat struktur json,masukan username dan password " +
            "anda seperti contoh dibawah ini.<br>" +
            "<br>" +
            "<div>" +
            "{<br>" +
            "&emsp;&emsp; \"username\":\"(Masukan username anda)\"<br>" +
            "&emsp;&emsp; \"password\":\"(Masukan password anda)\"<br>" +
            "}<br>" +
            "</div>" +
            "<br>" +
            "Lalu tekan tombol <b>execute</b>,setelah itu nanti akan muncul data login dan token.")
    public Response Login(LoginRequest request) throws ParseException {
        return authService.Login(request);
    }

    @POST
    @Transactional
    @Path("/register")
    @RolesAllowed("CREATE")
    @Operation(summary = "Untuk Registrasi Akun Admin",description = "Digunakan untuk membuat akun Admin baru.<br>" +
            "<br>" +
            "untuk membuat akun admin baru, diperlukan akun \"<b>Super Admin</b>\" (jika baru pertama kali membuka project ini)" +
            " atau \"<b>Admin</b>\".")
    public Response Register(RegisterRequest request){
        return authService.Register(request);
    }

    @POST
    @Transactional
    @Path("/premission/{id}")
    @RolesAllowed({"UPDATE","CREATE"})
    @Operation(summary = "Untuk Mengatur Premission Akun",description = "API ini hanya bisa diakses oleh Super Admin. " +
            "API ini memungkinkan Super Admin Menambahkan/Menghapus Premission setiap akun<br>" +
            "<br>" +
            "Path parameter <b>id</b> diperlukan untuk proses merubah izin pada akun<br>" +
            "<b>Ketentuan Mengisi Body Request</b>" +
            "<br>" +
            "<div>" +
            "{<br>" +
            "&emsp;&emsp; \"SELECT\":(true(jika anda mengizinkan user melihat resource pada web api ini) | false(jika anda tidak mengizinkan user melihat resource pada web api ini))<br>" +
            "&emsp;&emsp; \"CREATE\":(true(jika anda mengizinkan user membuat resource pada web api ini) | false(jika anda tidak mengizinkan user membuat resource pada web api ini))<br>" +
            "&emsp;&emsp; \"UPDATE\":(true(jika anda mengizinkan user mengubah resource pada web api ini) | false(jika anda tidak mengizinkan user mengubah resource pada web api ini))<br>" +
            "&emsp;&emsp; \"DELETE\":(true(jika anda mengizinkan user menghapus resource pada web api ini) | false(jika anda tidak mengizinkan user menghapus resource pada web api ini))<br>" +
            "}<br>" +
            "</div>" +
            "<br>" )
    public Response userPremission(
            @PathParam("id") long id,
            PremissionRequest request
    ){
        return authService.UserPremission(id,request);
    }

    @GET
    @RolesAllowed("SELECT")
    @Operation(summary = "Untuk Melihat Premission Semua Akun",description = "API ini digunakan untuk melihat premission setiap akun,hanya Super Admin saja yang bisa mengakses API ini.")
    public Response getAll(
            @DefaultValue("0")
            @QueryParam("page") int page
    ){
        return authService.GetAllPremission(page);
    }

}
