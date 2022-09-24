package Controller;

import DTO.DaftarRawatInapDTO.CreateDaftarRawatInapRequest;
import DTO.DaftarRawatInapDTO.UpdateDaftarRawatInapRequest;
import Service.DaftarRawatInapService;

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
    public Response getAll(
            @DefaultValue("0")
            @QueryParam("page") int page
    ){
        return daftarRawatInapService.GetAll(page);
    }

    @POST
    @Transactional
    public Response createDaftarRawatInap(CreateDaftarRawatInapRequest request){
        return daftarRawatInapService.CreateDaftarRawatInap(request);
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateDaftarRawatInap(
            @PathParam("id") long id,
            UpdateDaftarRawatInapRequest request
    ){
        return daftarRawatInapService.UpdateDaftarRawatInap(id,request);
    }

    @POST
    @Transactional
    @Path("/checkout/{id}")
    public Response Checkout(
            @PathParam("id") long id
    ){
        return daftarRawatInapService.CheckOut(id);
    }
}
