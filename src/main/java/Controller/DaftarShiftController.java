package Controller;

import DTO.DaftarShift.CreateDaftarShiftRequest;
import DTO.DaftarShift.UpdateDaftarShiftRequest;
import Service.DaftarShiftService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/daftarshift")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DaftarShiftController {

    @Inject
    DaftarShiftService daftarShiftService;

    @Operation(summary = "Untuk Membuat Shift Non Dokter",description = "API ini digunakan untuk membuat shift non dokter.<br>" +
            "<br>" +
            "Non Dokter yaitu <b>Perawat</b> dan <b>Staff</b>" +
            "<br>" +
            "<b>Ketentuan Mengisi Body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"kategori\":(isi dengan kategori pegawai non dokter(staff | perawat)).<br>" +
            "&emsp;&emsp; \"foreign_id\":(isi dengan id pegawai non dokter(staff | perawat)).<br>" +
            "&emsp;&emsp; \"startDateTime\":(isi dengan tanggal dan waktu,<b>T</b> adalah pemisah untuk tanggal dan waktu).<br>" +
            "&emsp;&emsp; \"endDateTime\":(isi dengan tanggal dan waktu,<b>T</b> adalah pemisah untuk tanggal dan waktu).<br>" +
            "}<br>")
    @POST
    @Transactional
    @RolesAllowed({"User","Admin","Super Admin"})
    public Response CreateDaftarShift(CreateDaftarShiftRequest request){
        return daftarShiftService.CreateShift(request);
    }


    @Operation(summary = "Untuk Merubah Shift Non Dokter",description = "API ini digunakan untuk merubah shift non dokter.<br>" +
            "<br>" +
            "Non Dokter yaitu <b>Perawat</b> dan <b>Staff</b>" +
            "<br>" +
            "PathParameter <b>Id</b>,diperlukan untuk mencari data secara spesifik dan merubah data." +
            "<br>" +
            "<b>Ketentuan Mengisi Body Request</b><br>" +
            "<br>" +
            "{<br>" +
            "&emsp;&emsp; \"kategori\":(isi dengan kategori pegawai non dokter(staff | perawat)).<br>" +
            "&emsp;&emsp; \"foreign_id\":(isi dengan id pegawai non dokter(staff | perawat)).<br>" +
            "&emsp;&emsp; \"startDateTime\":(isi dengan tanggal dan waktu,<b>T</b> adalah pemisah untuk tanggal dan waktu).<br>" +
            "&emsp;&emsp; \"endDateTime\":(isi dengan tanggal dan waktu,<b>T</b> adalah pemisah untuk tanggal dan waktu).<br>" +
            "}<br>")
    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"User","Admin","Super Admin"})
    public Response UpdateDaftarShift(
            @PathParam("id") long id,
            UpdateDaftarShiftRequest request){
        return daftarShiftService.UpdateShift(id,request);
    }
}
