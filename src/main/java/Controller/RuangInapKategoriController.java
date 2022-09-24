package Controller;

import Service.RuangInapKategoriService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ruanginapkategori")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RuangInapKategoriController {

    @Inject
    RuangInapKategoriService ruangInapKategoriService;

    @Operation(summary = "Untuk Menampilkan List Enum Kategori Ruang Inap",description = "API ini digunakan untuk " +
            "menampilkan List Data enum Kategori Ruang Inap.")
    @GET
    @RolesAllowed({"User","Admin","Super Admin"})
    public Response getAll(){
        return ruangInapKategoriService.GetAll();
    }

}
