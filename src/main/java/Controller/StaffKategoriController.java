package Controller;

import Service.StaffKategoriService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/staffkategori")
@Produces(MediaType.APPLICATION_JSON)
public class StaffKategoriController {

    @Inject
    StaffKategoriService staffKategoriService;

    @Operation(summary = "Untuk Menampilkan List Enum Posisi/Kategori Staff",description = "API ini digunakan untuk " +
            "menampilkan List Data enum posisi/kategori Staff.")
    @GET
    @RolesAllowed("SELECT")
    public Response getAll(){
        return staffKategoriService.GetAll();
    }
}
