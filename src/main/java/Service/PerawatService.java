package Service;

import DTO.PerawatDTO.CreatePerawatRequest;
import Models.Perawat;
import Util.ValidateInput;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PerawatService {

    @ConfigProperty(name = "PAGINATE_PER_PAGE")
    int paginate;

    public Response GetAll(
            int page,
            //filter section
            String nama,
            String email,
            String phone_number
    ){
        long perawatSize;
        List<Perawat> perawat = new ArrayList<>();
        //validate
        if(email != null && !ValidateInput.EmailInput(email)){
            JsonObject result = new JsonObject();
            result.put("message","Format input email Salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(phone_number != null && !ValidateInput.PhoneNumberInput(phone_number)){
            JsonObject result = new JsonObject();
            result.put("message","Format input phone_number Salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        //============================================================================

        //all input filter
        if(nama != null && email != null && phone_number != null){
            perawatSize = Perawat.find("nama_lengkap LIKE ?1 AND email = ?2 AND phone_number = ?3","%"+nama+"%",email,phone_number).list().size();
            perawat = Perawat.find("nama_lengkap LIKE ?1 AND email = ?2 AND phone_number = ?3","%"+nama+"%",email,phone_number).page(page,paginate).list();
            Object totalPage = (perawatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",perawat);
            result.put("total",perawatSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //=============================================================================

        //double input
        if(nama != null && email != null && phone_number == null){
            perawatSize = Perawat.find("nama_lengkap LIKE ?1 AND email = ?2","%"+nama+"%",email).list().size();
            perawat = Perawat.find("nama_lengkap LIKE ?1 AND email = ?2","%"+nama+"%",email).page(page,paginate).list();
            Object totalPage = (perawatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",perawat);
            result.put("total",perawatSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        if(nama != null &&  email == null && phone_number != null){
            perawatSize = Perawat.find("nama_lengkap LIKE ?1 AND phone_number = ?2","%"+nama+"%",phone_number).list().size();
            perawat = Perawat.find("nama_lengkap LIKE ?1 AND phone_number = ?2","%"+nama+"%",phone_number).page(page,paginate).list();
            Object totalPage = (perawatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",perawat);
            result.put("total",perawatSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        if(nama == null && email != null && phone_number != null){
            perawatSize = Perawat.find("email = ?1 AND phone_number = ?2",email,phone_number).list().size();
            perawat = Perawat.find("email = ?1 AND phone_number = ?2",email,phone_number).page(page,paginate).list();
            Object totalPage = (perawatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",perawat);
            result.put("total",perawatSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //=============================================================================

        //single input
        if(nama != null && email == null && phone_number == null){
            perawatSize = Perawat.find("nama_lengkap LIKE ?1","%"+nama+"%").list().size();
            perawat = Perawat.find("nama_lengkap LIKE ?1","%"+nama+"%").page(page,paginate).list();
            Object totalPage = (perawatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",perawat);
            result.put("total",perawatSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        if(nama == null && email != null && phone_number == null){
            perawatSize = Perawat.find("email = ?1",email).list().size();
            perawat = Perawat.find("email = ?1",email).page(page,paginate).list();
            Object totalPage = (perawatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",perawat);
            result.put("total",perawatSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        if(nama == null && email == null && phone_number != null){
            perawatSize = Perawat.find("phone_number = ?1",phone_number).list().size();
            perawat = Perawat.find("phone_number = ?1",phone_number).page(page,paginate).list();
            Object totalPage = (perawatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",perawat);
            result.put("total",perawatSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        perawatSize = Perawat.findAll().list().size();
        perawat = Perawat.findAll().page(page,paginate).list();
        Object totalPage = (perawatSize + paginate - 1) / paginate;
        JsonObject result = new JsonObject();
        result.put("data",perawat);
        result.put("total",perawatSize);
        result.put("totalPage",totalPage);

        return Response.ok(result).build();
    }

    public Response createPerawat(CreatePerawatRequest request){
        //validate
        if(!ValidateInput.EmailInput(request.email)){
            JsonObject result = new JsonObject();
            result.put("message","Format input email Salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!ValidateInput.PhoneNumberInput(request.phone_number)){
            JsonObject result = new JsonObject();
            result.put("message","Format input phone_number Salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //===========================================================================
        return PerawatInsert(request);
    }

    public Response PerawatInsert(CreatePerawatRequest request){
        LocalDateTime time = LocalDateTime.now();

        Perawat perawat = new Perawat();
        perawat.setNamaLengkap(request.nama);
        perawat.setGender(request.gender);
        perawat.setEmail(request.email);
        perawat.setPhoneNumber(request.phone_number);
        perawat.setGaji(request.gaji);
        perawat.setStatus(request.status);
        perawat.setCreated_at(time);
        perawat.setUpdated_at(time);

        //save
        Perawat.persist(perawat);

        JsonObject result = new JsonObject();
        result.put("data",perawat);
        return Response.ok(result).build();
    }
}
