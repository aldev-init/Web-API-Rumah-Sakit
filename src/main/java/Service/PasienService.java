package Service;

import DTO.PasienDTO.CreatePasienRequest;
import DTO.PasienDTO.UpdatePasienRequest;
import Models.Pasien;
import Util.ValidateInput;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PasienService {

    @ConfigProperty(name = "PAGINATE_PER_PAGE")
    int paginate;

    public Response GetAll(
            int page,
            String nama,
            String email,
            String phoneNumber
    ){
        long pasienSize;
        List<Pasien> pasien = new ArrayList<>();

        //validate
        if(email != null && !ValidateInput.EmailInput(email)){
            JsonObject result = new JsonObject();
            result.put("message","Format input email Salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(phoneNumber != null && !ValidateInput.PhoneNumberInput(phoneNumber)){
            JsonObject result = new JsonObject();
            result.put("message","Format input phone_number Salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //=============================================================================

        //allfilter
        if(nama != null && email != null && phoneNumber != null){
            pasienSize = Pasien.find("nama_lengkap LIKE ?1 AND email = ?2 AND phone_number = ?3","%"+nama+"%",email,phoneNumber).list().size();
            pasien = Pasien.find("nama_lengkap LIKE ?1 AND email = ?2 AND phone_number = ?3","%"+nama+"%",email,phoneNumber).page(page,paginate).list();
            Object totalPage = (pasienSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",pasien);
            result.put("total",pasienSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //===============================================================================

        //double filter
        //nama,email
        if(nama != null && email != null && phoneNumber == null){
            pasienSize = Pasien.find("nama_lengkap LIKE ?1 AND email = ?2","%"+nama+"%",email).list().size();
            pasien = Pasien.find("nama_lengkap LIKE ?1 AND email = ?2","%"+nama+"%",email).page(page,paginate).list();
            Object totalPage = (pasienSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",pasien);
            result.put("total",pasienSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //nama,phonenumber
        if(nama != null && email == null && phoneNumber != null){
            pasienSize = Pasien.find("nama_lengkap LIKE ?1 AND phone_number = ?2","%"+nama+"%",phoneNumber).list().size();
            pasien = Pasien.find("nama_lengkap LIKE ?1 AND phone_number = ?2","%"+nama+"%",phoneNumber).page(page,paginate).list();
            Object totalPage = (pasienSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",pasien);
            result.put("total",pasienSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //email,phonenumber
        if(nama == null && email != null && phoneNumber != null){
            pasienSize = Pasien.find("email = ?1 AND phone_number = ?2",email,phoneNumber).list().size();
            pasien = Pasien.find("email = ?1 AND phone_number = ?2",email,phoneNumber).page(page,paginate).list();
            Object totalPage = (pasienSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",pasien);
            result.put("total",pasienSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //=============================================================================================

        //once filter
        //nama
        if(nama != null && email == null && phoneNumber == null){
            pasienSize = Pasien.find("nama_lengkap LIKE ?1","%"+nama+"%").list().size();
            pasien = Pasien.find("nama_lengkap LIKE ?1","%"+nama+"%").page(page,paginate).list();
            Object totalPage = (pasienSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",pasien);
            result.put("total",pasienSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //email
        if(nama == null && email != null && phoneNumber == null){
            pasienSize = Pasien.find("email = ?1",email).list().size();
            pasien = Pasien.find("email = ?1",email).page(page,paginate).list();
            Object totalPage = (pasienSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",pasien);
            result.put("total",pasienSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //phonenumber
        if(nama == null && email == null && phoneNumber != null){
            pasienSize = Pasien.find("phone_number = ?1",phoneNumber).list().size();
            pasien = Pasien.find("phone_number = ?1",phoneNumber).page(page,paginate).list();
            Object totalPage = (pasienSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",pasien);
            result.put("total",pasienSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //===============================================================================

        pasienSize = Pasien.findAll().list().size();
        pasien = Pasien.findAll().page(page,paginate).list();
        Object totalPage = (pasienSize + paginate - 1) / paginate;
        JsonObject result = new JsonObject();
        result.put("data",pasien);
        result.put("total",pasienSize);
        result.put("totalPage",totalPage);

        return Response.ok(result).build();
    }

    public Response CreatePasien(CreatePasienRequest request){
        //validate
        if(!ValidateInput.EmailInput(request.email)){
            JsonObject result = new JsonObject();
            result.put("message","Format input email salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!ValidateInput.PhoneNumberInput(request.phone_number)){
            JsonObject result = new JsonObject();
            result.put("message","Format input phone_number salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //==========================================================

        return PasienInsert(request);
    }

    public Response PasienInsert(CreatePasienRequest request){
        LocalDateTime time = LocalDateTime.now();

        Pasien pasien =  new Pasien();
        pasien.setNamaLengkap(request.nama_lengkap);
        pasien.setGender(request.gender);
        pasien.setEmail(request.email);
        pasien.setPhoneNumber(request.phone_number);
        pasien.setAddress(request.address);
        pasien.setStatus(request.status);
        pasien.setCoverBpjs(request.is_cover_bpjs);
        pasien.setCreated_at(time);
        pasien.setUpdated_at(time);

        Pasien.persist(pasien);
        JsonObject result = new JsonObject();
        result.put("data",pasien);
        return Response.ok(result).build();
    }

    public Response UpdatePasien(long id,UpdatePasienRequest request){
        Optional<Pasien> pasien = Pasien.findByIdOptional(id);
        if(!pasien.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Pasien tidak ditemukan");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        LocalDateTime time = LocalDateTime.now();

        Pasien pasienUpdate = pasien.get();
        pasienUpdate.setNamaLengkap(request.nama_lengkap);
        pasienUpdate.setGender(request.gender);
        pasienUpdate.setEmail(request.email);
        pasienUpdate.setPhoneNumber(request.phone_number);
        pasienUpdate.setAddress(request.address);
        pasienUpdate.setStatus(request.status);
        pasienUpdate.setCoverBpjs(request.is_cover_bpjs);
        pasienUpdate.setUpdated_at(time);

        Pasien.persist(pasienUpdate);
        JsonObject result = new JsonObject();
        result.put("data",pasienUpdate);
        return Response.ok(result).build();
    }

    public  Response DeletePasien(long id){
        Optional<Pasien> pasienOptional = Pasien.findByIdOptional(id);
        if(!pasienOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Pasien tidak ditemukan");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        Pasien pasienDelete = pasienOptional.get();
        pasienDelete.delete();
        JsonObject result = new JsonObject();
        result.put("message","Pasien berhasil dihapus");
        return Response.ok(result).build();
    }

}
