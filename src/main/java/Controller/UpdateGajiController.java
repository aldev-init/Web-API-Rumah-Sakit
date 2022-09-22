package Controller;

import DTO.UpdateGajiDTO.CreateUpdateGajiRequest;
import Service.UpdateGajiService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/updategaji")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UpdateGajiController {

    @Inject
    UpdateGajiService updateGajiService;

    @Operation(summary = "Untuk Menambah/Merubah Gaji Pegawai",description = "API ini digunakan untuk menambah/merubah gaji pegawai.<br>" +
            "<br>" +
            "<b>Pegawai</b><br>" +
            "<ul>" +
            "<li> Dokter </li>" +
            "<li> Perawat </li>" +
            "<li> Staff </li>" +
            "</ul>" +
            "<br>" +
            "<b>Ketentuan Mengisi Body Request</b><br>" +
            "{<br>" +
            "&emsp;&emsp; \"pegawai_kategori\":(isi dengan kategori pegawai,seperti list diatas)<br>" +
            "&emsp;&emsp; \"pegawaiId\":(isi dengan Id Pegawai)<br>" +
            "&emsp;&emsp; \"gaji\":(isi dengan gaji pegawai)<br>" +
            "}<br>")
    @POST
    @Transactional
    @RolesAllowed({"User","Admin","Super Admin"})
    public Response UpdateGaji(CreateUpdateGajiRequest request){
        return updateGajiService.UpdateGaji(request);
    }
}
