package Controller;

import DTO.DaftarRawatInapDTO.CreateDaftarRawatInapRequest;
import DTO.DaftarRawatInapDTO.UpdateDaftarRawatInapRequest;
import Service.DaftarRawatInapService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/daftarrawatinap")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DaftarRawatInapController {

    @Inject
    DaftarRawatInapService daftarRawatInapService;

    @GET
    @RolesAllowed({"User","Admin","Super Admin"})
    @Operation(summary = "Untuk Menampilkan Data Daftar Rawat Inap",description = "API ini digunakan untuk menampilkan daftar data Daftar Rawat Inap dengan format Pagination.<br>" +
            "<ul>" +
            "<li>query <b>Page</b> diperlukan dan tidak boleh kosong,karena berpengaruh terhadap pagination.</li>" +
            "</ul>")
    public Response getAll(
            @DefaultValue("0")
            @QueryParam("page") int page
    ){
        return daftarRawatInapService.GetAll(page);
    }

    @POST
    @Transactional
    @RolesAllowed({"User","Admin","Super Admin"})
    @Operation(summary = "Untuk Membuat Data Daftar Rawat Inap",description = "API ini digunakan untuk membuat data Daftar Rawat Inap baru.<br>" +
            "<br>" +
            "<b>*Ketentuan mengisi body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"<b>ruang_inap_id</b>\":(Masukan id Ruang Inap)<br>" +
            "&emsp;&emsp; \"<b>dokter_id</b>\":(Masukan id Dokter)<br>" +
            "&emsp;&emsp; \"<b>pasien_id</b>\":(Masukan id Pasien)<br>" +
            "&emsp;&emsp; \"<b>perawat_satu_id</b>\":(Masukan id Perawat ke satu)<br>" +
            "&emsp;&emsp; \"<b>perawat_dua_id</b>\":(Masukan id Perawat ke dua)<br>" +
            "&emsp;&emsp; \"<b>start_date_time</b>\":(masukan tanggal dan waktu mulai rawat inap)<br>" +
            "&emsp;&emsp; \"<b>status</b>\":(masukan tanggal dan waktu berakhir rawat inap)<br>" +
            "}<br>")
    public Response createDaftarRawatInap(CreateDaftarRawatInapRequest request){
        return daftarRawatInapService.CreateDaftarRawatInap(request);
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @RolesAllowed({"User","Admin","Super Admin"})
    @Operation(summary = "Untuk Merubah Data Daftar Rawat Inap",description = "API ini digunakan untuk Merubah data Daftar Rawat Inap baru.<br>" +
            "<br>" +
            "Path parameter <b>id</b> diperlukan untuk proses merubah data<br>" +
            "<b>*Ketentuan mengisi body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"<b>ruang_inap_id</b>\":(Masukan id Ruang Inap)<br>" +
            "&emsp;&emsp; \"<b>dokter_id</b>\":(Masukan id Dokter)<br>" +
            "&emsp;&emsp; \"<b>pasien_id</b>\":(Masukan id Pasien)<br>" +
            "&emsp;&emsp; \"<b>perawat_satu_id</b>\":(Masukan id Perawat ke satu)<br>" +
            "&emsp;&emsp; \"<b>perawat_dua_id</b>\":(Masukan id Perawat ke dua)<br>" +
            "&emsp;&emsp; \"<b>start_date_time</b>\":(masukan tanggal dan waktu mulai rawat inap)<br>" +
            "&emsp;&emsp; \"<b>status</b>\":(masukan tanggal dan waktu berakhir rawat inap)<br>" +
            "}<br>")
    public Response updateDaftarRawatInap(
            @PathParam("id") long id,
            UpdateDaftarRawatInapRequest request
    ){
        return daftarRawatInapService.UpdateDaftarRawatInap(id,request);
    }

    @POST
    @Transactional
    @Path("/checkout/{id}")
    @RolesAllowed({"User","Admin","Super Admin"})
    @Operation(summary = "Untuk Checkout Daftar Rawat Inap",description = "API ini digunakan untuk checkout rawat inap.<br>" +
            "<br>" +
            "*Masukan <b>Id</b> pada Daftar Rawat Inap<br>" +
            "<br>")
    public Response Checkout(
            @PathParam("id") long id
    ){
        return daftarRawatInapService.CheckOut(id);
    }
}
