package Controller;

import DTO.PasienDTO.CreatePasienRequest;
import DTO.PasienDTO.UpdatePasienRequest;
import Service.PasienService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/pasien")
public class PasienController {

    @Inject
    PasienService pasienService;

    @GET
    @RolesAllowed({"User","Admin","Super Admin"})
    @Operation(summary = "Untuk Menampilkan Data Pasien",description = "API ini digunakan untuk menampilkan daftar data Pasien dengan format Pagination.<br>" +
            "Tidak hanya menampilkan saja,tetapi terdapat fitur filter didalam API ini,Filter tersebut berdasarkan:<br>" +
            "- Email<br>- Nama<br>- Phone Number<br>" +
            "<br>" +
            "<b>*Ketentuan untuk Filter Data Pasien</b>" +
            "<br>" +
            "<ul>" +
            "<li>query <b>Page</b> diperlukan dan tidak boleh kosong,karena berpengaruh terhadap pagination.</li>" +
            "<li>filter ini fleksible dapat diisi semua,hanya dua,atau salah satu saja.</li>" +
            "<li>untuk query <b>phone_number</b> gunakan format +62 diawal." +
            "<li>untuk query <b>email</b> gunakan @gmail.com,@yahoo.com dll.</li>" +
            "</ul>")
    public Response getAll(
            @DefaultValue("0")
            @QueryParam("page") int page,
            @QueryParam("nama") String nama,
            @QueryParam("email") String email,
            @QueryParam("phone") String phonenumber
    ){
        return pasienService.GetAll(page, nama, email, phonenumber);
    }

    @POST
    @Transactional
    @RolesAllowed({"User","Admin","Super Admin"})
    @Operation(summary = "Untuk Membuat Data Pasien",description = "API ini digunakan untuk membuat data Pasien baru.<br>" +
            "<br>" +
            "<b>*Ketentuan mengisi body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"<b>nama_lengkap</b>\":(Masukan nama lengkap Pasien)<br>" +
            "&emsp;&emsp; \"<b>gender</b>\":(Masukan gender Pasien)<br>" +
            "&emsp;&emsp; \"<b>email</b>\":(Masukan email Pasien)<br>" +
            "&emsp;&emsp; \"<b>phone_number</b>\":(Masukan nomor telepon Pasien)<br>" +
            "&emsp;&emsp; \"<b>status</b>\":(Masukan status Pasien)<br>" +
            "&emsp;&emsp; \"<b>address</b>\":(Masukan alamat Pasien)<br>" +
            "&emsp;&emsp; \"<b>is_cover_bpjs</b>\":(Masukan true jika pasien menggunakan BPJS,masukan false jika tidak)<br>" +
            "}<br>")
    public Response createPasien(CreatePasienRequest request){
        return pasienService.CreatePasien(request);
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @RolesAllowed({"User","Admin","Super Admin"})
    @Operation(summary = "Untuk Merubah Data Pasien",description = "API ini digunakan untuk Merubah data Pasien baru.<br>" +
            "<br>" +
            "Path parameter <b>id</b> diperlukan untuk proses merubah data<br>" +
            "<b>*Ketentuan mengisi body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"<b>nama_lengkap</b>\":(Masukan nama lengkap Pasien)<br>" +
            "&emsp;&emsp; \"<b>gender</b>\":(Masukan gender Pasien)<br>" +
            "&emsp;&emsp; \"<b>email</b>\":(Masukan email Pasien)<br>" +
            "&emsp;&emsp; \"<b>phone_number</b>\":(Masukan nomor telepon Pasien)<br>" +
            "&emsp;&emsp; \"<b>status</b>\":(Masukan status Pasien)<br>" +
            "&emsp;&emsp; \"<b>address</b>\":(Masukan alamat Pasien)<br>" +
            "&emsp;&emsp; \"<b>is_cover_bpjs</b>\":(Masukan true jika pasien menggunakan BPJS,masukan false jika tidak)<br>" +
            "}<br>")
    public Response updatePasien(
            @PathParam("id") long id,
            UpdatePasienRequest request
    ){
        return pasienService.UpdatePasien(id,request);
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @RolesAllowed({"User","Admin","Super Admin"})
    @Operation(summary = "Untuk Hapus Pasien",description = "API ini digunakan untuk hapus Pasien.<br>" +
            "<br>" +
            "Path parameter <b>id</b> diperlukan untuk proses penghapusan data"+
            "<br>")
    public Response deletePasien(
            @PathParam("id") long id
    ){
        return pasienService.DeletePasien(id);
    }

}
