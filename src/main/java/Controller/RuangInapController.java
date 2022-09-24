package Controller;

import DTO.RuangInapDTO.CreateRuangInapRequest;
import DTO.RuangInapDTO.UpdateRuangInapRequest;
import Service.RuangInapService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ruanginap")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RuangInapController {

    @Inject
    RuangInapService ruangInapService;

    @Operation(summary = "Untuk Menampilkan Data Ruang Inap",description = "API ini digunakan untuk menampilkan daftar data Ruang Inap dengan format Pagination.<br>" +
            "<br>" +
            "<b>*Keterangan</b>" +
            "<ul>" +
            "<li>untuk Field Kosong False artinya tidak kosong dan True artinya kosong" +
            "</ul>")
    @GET
    @RolesAllowed({"User","Admin","Super Admin"})
    public Response getAll(
            @DefaultValue("0")
            @QueryParam("page") int page
    ){
        return ruangInapService.GetAll(page);
    }

    @Operation(summary = "Untuk Membuat Data Ruang Inap",description = "API ini digunakan untuk membuat data Ruang Inap baru.<br>" +
            "<br>" +
            "<b>*Ketentuan mengisi body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"<b>prefix_ruangan</b>\":(Masukan prefix ruangan)<br>" +
            "&emsp;&emsp; \"<b>nomor_ruangan</b>\":(Masukan nomor ruangan)<br>" +
            "&emsp;&emsp; \"<b>kategori_ruangan</b>\":(Masukan kategori ruangan (lihat pada list enum API RuangInapKategori))<br>" +
            "}<br>")
    @POST
    @Transactional
    @RolesAllowed({"User","Admin","Super Admin"})
    public Response createRuangInap(CreateRuangInapRequest request){
        return ruangInapService.CreateRuangInap(request);
    }

    @Operation(summary = "Untuk Merubah Data Ruang Inap",description = "API ini digunakan untuk Merubah data Ruang Inap.<br>" +
            "<br>" +
            "Path parameter <b>id</b> diperlukan untuk proses merubah data<br>" +
            "<b>note: </b> Ruang Inap tidak bisa diubah,jika ruang tidak kosong/sedang digunakan.<br>" +
            "<b>*Ketentuan mengisi body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"<b>prefix_ruangan</b>\":(Masukan prefix ruangan)<br>" +
            "&emsp;&emsp; \"<b>nomor_ruangan</b>\":(Masukan nomor ruangan)<br>" +
            "&emsp;&emsp; \"<b>kategori_ruangan</b>\":(Masukan kategori ruangan (lihat pada list enum API RuangInapKategori))<br>" +
            "}<br>")
    @PUT
    @Transactional
    @Path("/{id}")
    @RolesAllowed({"User","Admin","Super Admin"})
    public Response updateRuangInap(
            @PathParam("id") long id,
            UpdateRuangInapRequest request
    ){
        return ruangInapService.UpdateRuangInap(id,request);
    }

    @Operation(summary = "Untuk Menghapus Data Ruang Inap",description = "API ini digunakan untuk menghapus data ruang inap<br>" +
            "<br>" +
            "Path parameter <b>id</b> diperlukan untuk proses penghapusan data<br>" +
            "<b>note: </b> Ruang Inap tidak bisa dihapus,jika ruang tidak kosong/sedang digunakan.")
    @DELETE
    @Transactional
    @Path("/{id}")
    @RolesAllowed({"User","Admin","Super Admin"})
    public Response deleteRuangInap(
            @PathParam("id") long id
    ){
        return ruangInapService.DeleteRuangInap(id);
    }



}
