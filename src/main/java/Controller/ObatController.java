package Controller;

import DTO.ObatDTO.CreateObatRequest;
import DTO.ObatDTO.UpdateObatRequest;
import Service.ObatService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/obat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ObatController {

    @Inject
    ObatService obatService;

    @Operation(summary = "Untuk Menampilkan Data Obat",description = "API ini digunakan untuk menampilkan daftar data Obat dengan format Pagination.<br>" +
            "Tidak hanya menampilkan saja,tetapi terdapat fitur filter didalam API ini,Filter tersebut berdasarkan:<br>" +
            "- Nama<br>- Kategori<br>- Produksi<br>" +
            "<br>" +
            "<b>*Ketentuan untuk Filter Data Obat</b>" +
            "<br>" +
            "<ul>" +
            "<li>query <b>Page</b> diperlukan dan tidak boleh kosong,karena berpengaruh terhadap pagination.</li>" +
            "<li>filter ini fleksible dapat diisi semua,hanya dua,atau salah satu saja.</li>" +
            "<li>untuk query <b>kategori</b> hanya bisa diisi oleh list enum yang ada di API Kategori Obat.")
    @GET
    @RolesAllowed("SELECT")
    public Response getAll(
            @DefaultValue("0")
            @QueryParam("page") int page,
            //filter section
            @QueryParam("nama") String nama,
            @QueryParam("produksi") String produksi,
            @QueryParam("kategori") String kategori
    ){
        return obatService.GetAll(page,kategori,produksi,nama);
    }

    @Operation(summary = "Untuk Membuat Data Obat",description = "API ini digunakan untuk membuat data Obat baru.<br>" +
            "<br>" +
            "<b>*Ketentuan mengisi body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"<b>nama_obat</b>\":(Masukan nama obat)<br>" +
            "&emsp;&emsp; \"<b>produksi</b>\":(Masukan produksi Obat)<br>" +
            "&emsp;&emsp; \"<b>obat_kategori</b>\":(Masukan kategori obat (lihat pada list enum API KategoriObat))<br>" +
            "&emsp;&emsp; \"<b>deskripsi</b>\":(Masukan deskripsi obat)<br>"+
            "}<br>")
    @POST
    @Transactional
    @RolesAllowed("CREATE")
    public Response createObat(CreateObatRequest request){
        return obatService.CreateObat(request);
    }

    @Operation(summary = "Untuk Merubah Data Obat",description = "API ini digunakan untuk Merubah data Obat baru.<br>" +
            "<br>" +
            "Path parameter <b>id</b> diperlukan untuk proses merubah data<br>" +
            "<b>*Ketentuan mengisi body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"<b>nama_obat</b>\":(Masukan nama obat)<br>" +
            "&emsp;&emsp; \"<b>produksi</b>\":(Masukan produksi Obat)<br>" +
            "&emsp;&emsp; \"<b>obat_kategori</b>\":(Masukan kategori obat (lihat pada list enum API KategoriObat))<br>" +
            "&emsp;&emsp; \"<b>deskripsi</b>\":(Masukan deskripsi obat)<br>"+
            "}<br>")
    @PUT
    @Transactional
    @Path("/{id}")
    @RolesAllowed("UPDATE")
    public Response updateObat(
            @PathParam("id") long id,
            UpdateObatRequest request
    ){
        return obatService.UpdateObat(id,request);
    }

    @Operation(summary = "Untuk Menghapus Data Obat",description = "API ini digunakan untuk menghapus data obat<br>" +
            "<br>" +
            "Path parameter <b>id</b> diperlukan untuk proses penghapusan data")
    @DELETE
    @Transactional
    @Path("/{id}")
    @RolesAllowed("DELETE")
    public Response deleteObat(
            @PathParam("id") long id
    ){
        return obatService.DeleteObat(id);
    }
}
