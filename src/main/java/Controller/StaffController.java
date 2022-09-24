package Controller;

import DTO.StaffDTO.CreateStaffRequest;
import Service.StaffService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/staff")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StaffController {

    @Inject
    StaffService staffService;

    @Operation(summary = "Untuk Menampilkan Data Staff",description = "API ini digunakan untuk menampilkan daftar data Staff dengan format Pagination.<br>" +
            "Tidak hanya menampilkan saja,tetapi terdapat fitur filter didalam API ini,Filter tersebut berdasarkan:<br>" +
            "- Nama<br>- Email<br>- Phone Number<br>" +
            "<br>" +
            "<b>*Ketentuan untuk Filter Data Staff</b>" +
            "<br>" +
            "<ul>" +
            "<li>query <b>Page</b> diperlukan dan tidak boleh kosong,karena berpengaruh terhadap pagination.</li>" +
            "<li>filter ini fleksible dapat diisi semua,hanya dua,atau salah satu saja.</li>" +
            "<li>untuk query <b>phone_number</b> gunakan format +62 diawal." +
            "<li>untuk query <b>email</b> gunakan @gmail.com,@yahoo.com dll.</li>")
    @GET
    @RolesAllowed("SELECT")
    public Response getAll(
            @DefaultValue("0")
            @QueryParam("page") int page,
            //filter section
            @QueryParam("nama") String nama,
            @QueryParam("email") String email,
            @QueryParam("phone_number") String phone_number
    ){
        return staffService.GetAll(page,nama,email,phone_number);
    }

    @Operation(summary = "Untuk Membuat Data Staff",description = "API ini digunakan untuk membuat data Staff baru.<br>" +
            "<br>" +
            "<b>*Ketentuan mengisi body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"<b>nama</b>\":(Masukan nama lengkap Staff)<br>" +
            "&emsp;&emsp; \"<b>gender</b>\":(Masukan gender Perawat M(laki-laki) atau F(perempuan))<br>" +
            "&emsp;&emsp; \"<b>email</b>\":(Masukan email dengan format @gmail.com,@yahoo.com dan lainnya)<br>" +
            "&emsp;&emsp; \"<b>phone_number</b>\":(Masukan nomor telepon dengan format +62 diawal)<br>" +
            "&emsp;&emsp; \"<b>posisi</b>\":(Masukan posisi,<i>Lihat daftar list posisi dibagian API Staff Kategori</i>)<br>"+
            "&emsp;&emsp; \"<b>gaji</b>\":(masukan Gaji Staff)<br>" +
            "&emsp;&emsp; \"<b>status</b>\":(Masukan status Staff)<br>" +
            "}<br>")
    @POST
    @Transactional
    @RolesAllowed("CREATE")
    public Response CreateStaff(CreateStaffRequest request){
        return staffService.CreateStaff(request);
    }

}
