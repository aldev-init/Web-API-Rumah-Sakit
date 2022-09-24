package Controller;

import DTO.JadwalPraktikDTO.CreateJadwalPraktikRequest;
import DTO.JadwalPraktikDTO.UpdateJadwalPraktikRequest;
import Service.JadwalPraktikService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/jadwalpraktik")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JadwalPraktikController {

    @Inject
    JadwalPraktikService jadwalPraktikService;

    @Operation(summary = "Untuk Membuat Jadwal Praktik Untuk Dokter",description = "API ini digunakan untuk membuat jadwal praktik dokter.<br>" +
            "<br>" +
            "<b>Ketentuan Mengisi Body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"hari\":(isi dengan nama hari(senin,selasa,rabu....minggu)).<br>" +
            "&emsp;&emsp; \"start_time\":(isi dengan tanggal dan waktu,<b>T</b> adalah pemisah untuk tanggal dan waktu).<br>" +
            "&emsp;&emsp; \"end_time\":(isi dengan tanggal dan waktu,<b>T</b> adalah pemisah untuk tanggal dan waktu).<br>" +
            "&emsp;&emsp; \"deskripsi\":(isi dengan deskripsi jadwal praktik).<br>" +
            "&emsp;&emsp; \"dokter_id\":(isi dengan id dokter).<br>" +
            "}<br>")
    @POST
    @Transactional
    @RolesAllowed({"User","Admin","Super Admin"})
    public Response createJadwalPraktik(CreateJadwalPraktikRequest request){
        return jadwalPraktikService.CreateJadwalPraktik(request);
    }

    @Operation(summary = "Untuk Merubah Jadwal Praktik Untuk Dokter",description = "API ini digunakan untuk merubah jadwal praktik dokter.<br>" +
            "<br>" +
            "PathParameter <b>Id</b>,diperlukan untuk mencari data secara spesifik dan merubah data." +
            "<br>" +
            "<b>Ketentuan Mengisi Body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"hari\":(isi dengan nama hari(senin,selasa,rabu....minggu)).<br>" +
            "&emsp;&emsp; \"start_time\":(isi dengan tanggal dan waktu,<b>T</b> adalah pemisah untuk tanggal dan waktu).<br>" +
            "&emsp;&emsp; \"end_time\":(isi dengan tanggal dan waktu,<b>T</b> adalah pemisah untuk tanggal dan waktu).<br>" +
            "&emsp;&emsp; \"deskripsi\":(isi dengan deskripsi jadwal praktik).<br>" +
            "&emsp;&emsp; \"dokter_id\":(isi dengan id dokter).<br>" +
            "}<br>")
    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"User","Admin","Super Admin"})
    public Response updateJadwalPraktik(
            @PathParam("id") long id,
            UpdateJadwalPraktikRequest request
    ){
        return jadwalPraktikService.UpdateJadwalPraktik(id,request);
    }
}
