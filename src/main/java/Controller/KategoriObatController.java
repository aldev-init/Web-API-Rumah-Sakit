package Controller;

import Service.KategoriObatService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/kategoriobat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KategoriObatController {

    @Inject
    KategoriObatService kategoriObatService;

    @Operation(summary = "Untuk Menampilkan Enum List Data Kategori Obat",description = "API ini digunakan untuk melihat list enun data kategori obat," +
            "yang nantinya untuk validasi Kategori pada API tambah/filter/ubah <b>Obat</b>")
    @GET
    @RolesAllowed("SELECT")
    public Response getAll(){
        return kategoriObatService.GetAll();
    }
}
