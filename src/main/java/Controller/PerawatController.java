package Controller;

import DTO.PerawatDTO.CreatePerawatRequest;
import Service.PerawatService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/perawat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PerawatController {

    @Inject
    PerawatService perawatService;


    @Operation(summary = "Untuk Menampilkan Data Perawat",description = "API ini digunakan untuk menampilkan daftar data Perawat dengan format Pagination.<br>" +
            "Tidak hanya menampilkan saja,tetapi terdapat fitur filter didalam API ini,Filter tersebut berdasarkan:<br>" +
            "- Nama<br>- Email<br>- Phone Number<br>" +
            "<br>" +
            "<b>*Ketentuan untuk Filter Data Perawat</b>" +
            "<br>" +
            "<ul>" +
            "<li>query <b>Page</b> diperlukan dan tidak boleh kosong,karena berpengaruh terhadap pagination.</li>" +
            "<li>filter ini fleksible dapat diisi semua,hanya dua,atau salah satu saja.</li>" +
            "<li>untuk query <b>phone_number</b> gunakan format +62 diawal." +
            "<li>untuk query <b>email</b> gunakan @gmail.com,@yahoo.com dll.</li>")
    @GET
    @RolesAllowed({"User","Admin","Super Admin"})
    public Response getAll(
            @DefaultValue("0")
            @QueryParam("page") int page,
            @QueryParam("nama") String nama,
            @QueryParam("email") String email,
            @QueryParam("phone_number") String phone_number
    ){
        return perawatService.GetAll(page,nama,email,phone_number);
    }

    @Operation(summary = "Untuk Membuat Data Perawat",description = "API ini digunakan untuk membuat data Perawat baru.<br>" +
            "<br>" +
            "<b>*Ketentuan mengisi body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"<b>nama</b>\":(Masukan nama lengkap Perawat)<br>" +
            "&emsp;&emsp; \"<b>gender</b>\":(Masukan gender Perawat M(laki-laki) atau F(perempuan))<br>" +
            "&emsp;&emsp; \"<b>email</b>\":(Masukan email dengan format @gmail.com,@yahoo.com dan lainnya)<br>" +
            "&emsp;&emsp; \"<b>phone_number</b>\":(Masukan nomor telepon dengan format +62 diawal)<br>"+
            "&emsp;&emsp; \"<b>gaji</b>\":(masukan Gaji Perawat)<br>" +
            "&emsp;&emsp; \"<b>status</b>\":(Masukan status Perawat)<br>" +
            "}<br>")
    @POST
    @Path("/perawat")
    @Transactional
    @RolesAllowed({"Admin","Super Admin"})
    public Response createAddPerawat(CreatePerawatRequest request){
        return perawatService.createPerawat(request);
    }
}
