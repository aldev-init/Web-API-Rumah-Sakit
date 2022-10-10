package Controller;

import DTO.DokterDTO.CreateDokterRequest;
import Service.DokterService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/dokter")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DokterController {

    @Inject
    DokterService dokterService;

    @GET
    @RolesAllowed("SELECT")
    @Operation(summary = "Untuk Menampilkan Data Dokter",description = "API ini digunakan untuk menampilkan daftar data Dokter dengan format Pagination.<br>" +
            "Tidak hanya menampilkan saja,tetapi terdapat fitur filter didalam API ini,Filter tersebut berdasarkan:<br>" +
            "- Email<br>- Nama<br>- Phone Number<br>- is_Spesialis<br>" +
            "<br>" +
            "<b>*Ketentuan untuk Filter Data Dokter</b>" +
            "<br>" +
            "<ul>" +
            "<li>query <b>Page</b> diperlukan dan tidak boleh kosong,karena berpengaruh terhadap pagination.</li>" +
            "<li>filter ini fleksible dapat diisi semua,hanya dua,atau salah satu saja.</li>" +
            "<li>untuk query <b>spesialis</b> hanya diisi 0 (tidak spesialis) atau 1 (spesialis)." +
            "<li>untuk query <b>phone_number</b> gunakan format +62 diawal." +
            "<li>untuk query <b>email</b> gunakan @gmail.com,@yahoo.com dll.</li>" +
            "</ul>")
    public Response GetAll(
            @DefaultValue("0")
            @QueryParam("page") int page,
            //filter section
            @QueryParam("spesialis") String spesialis,
            @QueryParam("nama") String nama,
            @QueryParam("email") String email,
            @QueryParam("phone_number") String phoneNumber
    ){
        return dokterService.GetAll(page,spesialis,nama,email,phoneNumber);
    }

    @GET
    @Path("/export/pdf")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Uni<Response> ExportPdf() throws Exception {
        return dokterService.exportPdf();
    }

    @GET
    @Path("/export/word")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Uni<Response> ExportWord() throws Exception {
        return dokterService.exportWord();
    }

    @GET
    @Path("/export/pptx")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Uni<Response> ExportPPTX() throws Exception {
        return dokterService.exportPPTX();
    }

    @POST
    @RolesAllowed("CREATE")
    @Transactional
    @Operation(summary = "Untuk Membuat Data Dokter",description = "API ini digunakan untuk membuat data Dokter baru.<br>" +
            "<br>" +
            "<b>*Ketentuan mengisi body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"<b>nama_lengkap</b>\":(Masukan nama lengkap Dokter)<br>" +
            "&emsp;&emsp; \"<b>email</b>\":(Masukan email dengan format @gmail.com,@yahoo.com dan lainnya)<br>" +
            "&emsp;&emsp; \"<b>phone_number</b>\":(Masukan nomor telepon dengan format +62 diawal)<br>" +
            "&emsp;&emsp; \"<b>spesialis_nama</b>\":(Masukan nama spesialis Dokter)<br>" +
            "&emsp;&emsp; \"<b>is_spesialis</b>\":(diisi dengan angka 0 (tidak spesialis) atau 1 (spesialis))<br>" +
            "&emsp;&emsp; \"<b>gaji</b>\":(masukan Gaji Dokter)<br>" +
            "&emsp;&emsp; \"<b>status</b>\":(diisi dengan angka 0 (tidak aktif) atau 1 (aktif))<br>" +
            "}<br>")
    public Response addDokter(CreateDokterRequest request){
        return dokterService.CreateDockter(request);
    }
}
