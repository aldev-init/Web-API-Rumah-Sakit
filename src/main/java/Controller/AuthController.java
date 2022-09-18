package Controller;

import DTO.AuthDTO.LoginRequest;
import DTO.AuthDTO.RegisterRequest;
import Service.AuthService;
import Service.InitialAdminService;
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
            "</div>")
    public Response Login(LoginRequest request){
        return authService.Login(request);
    }

    @POST
    @Transactional
    @Path("/register")
    @RolesAllowed({"Admin","Super Admin"})
    @Operation(summary = "Untuk Registrasi Akun Admin",description = "Digunakan untuk membuat akun Admin baru.<br>" +
            "<br>" +
            "untuk membuat akun admin baru, diperlukan akun \"<b>Super Admin</b>\" (jika baru pertama kali membuka project ini)" +
            " atau \"<b>Admin</b>\".")
    public Response Register(RegisterRequest request){
        return authService.Register(request);
    }
}
