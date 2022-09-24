package Controller;

import DTO.PasienDTO.CreatePasienRequest;
import DTO.PasienDTO.UpdatePasienRequest;
import Service.PasienService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/pasien")
public class PasienController {

    @Inject
    PasienService pasienService;

    @GET
    public Response getAll(
            @DefaultValue("0")
            @QueryParam("page") int page,
            @QueryParam("nama") String nama,
            @QueryParam("email") String email,
            @QueryParam("phone") String phonenumber
    ){
        return pasienService.GetAll(page, nama, email, phonenumber);
    }

    @POST
    @Transactional
    public Response createPasien(CreatePasienRequest request){
        return pasienService.CreatePasien(request);
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updatePasien(
            @PathParam("id") long id,
            UpdatePasienRequest request
    ){
        return pasienService.UpdatePasien(id,request);
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deletePasien(
            @PathParam("id") long id
    ){
        return pasienService.DeletePasien(id);
    }

}
