package Service;

import DTO.StaffDTO.CreateStaffRequest;
import Models.Staff;
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
public class StaffService {

    @ConfigProperty(name = "PAGINATE_PER_PAGE")
    int paginate;

    public Response CreateStaff(CreateStaffRequest request){
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

        if(!ValidateInput.positionStaffInput(request.posisi)){
            JsonObject result = new JsonObject();
            result.put("message","Posisi tidak tersedia.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        return StaffInsert(request);
    }

    public Response StaffInsert(CreateStaffRequest request){
        LocalDateTime time = LocalDateTime.now();

        Staff staff = new Staff();
        staff.setNamaLengkap(request.nama);
        staff.setEmail(request.email);
        staff.setGender(request.gender);
        staff.setPhoneNumber(request.phone_number);
        staff.setPosisi(request.posisi);
        staff.setGaji(request.gaji);
        staff.setStatus(request.status);
        staff.setCreated_at(time);
        staff.setUpdated_at(time);

        Staff.persist(staff);
        JsonObject result = new JsonObject();
        result.put("data",staff);
        return Response.ok(result).build();
    }

    public Response GetAll(
            @QueryParam("page") int page,
            //filter section
            @QueryParam("nama") String nama,
            @QueryParam("email") String email,
            @QueryParam("phone_number") String phone_number
    ){
        long staffSize;
        List<Staff> staff= new ArrayList<>();
        //validate
        if(email != null && !ValidateInput.EmailInput(email)){
            JsonObject result = new JsonObject();
            result.put("message","Format input email salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(phone_number != null && !ValidateInput.PhoneNumberInput(phone_number)){
            JsonObject result = new JsonObject();
            result.put("message","Format input phone_number salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        //===========================================================================

        //filter all
        if(nama != null && email != null && phone_number != null){
            staffSize = Staff.find("nama_lengkap LIKE ?1 AND email = ?2 AND phone_number = ?3","%"+nama+"%",email,phone_number).list().size();
            staff = Staff.find("nama_lengkap LIKE ?1 AND email = ?2 AND phone_number = ?3","%"+nama+"%",email,phone_number).page(page,paginate).list();
            Object totalPage = (staffSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",staff);
            result.put("total",staffSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //===========================================================================

        //double filter
        if(nama != null && email != null && phone_number == null){
            staffSize = Staff.find("nama_lengkap LIKE ?1 AND email = ?2","%"+nama+"%",email).list().size();
            staff = Staff.find("nama_lengkap LIKE ?1 AND email = ?2","%"+nama+"%",email).page(page,paginate).list();
            Object totalPage = (staffSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",staff);
            result.put("total",staffSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        if(nama != null && email == null && phone_number != null){
            staffSize = Staff.find("nama_lengkap LIKE ?1 AND phone_number = ?2","%"+nama+"%",phone_number).list().size();
            staff = Staff.find("nama_lengkap LIKE ?1 AND phone_number = ?2","%"+nama+"%",phone_number).page(page,paginate).list();
            Object totalPage = (staffSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",staff);
            result.put("total",staffSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        if(nama == null && email != null && phone_number != null){
            staffSize = Staff.find("email = ?1 AND phone_number = ?2",email,phone_number).list().size();
            staff = Staff.find("email = ?1 AND phone_number = ?2",email,phone_number).page(page,paginate).list();
            Object totalPage = (staffSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",staff);
            result.put("total",staffSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //===========================================================================

        //one filter
        //only nama
        if(nama != null && email == null && phone_number == null){
            staffSize = Staff.find("nama_lengkap LIKE ?1","%"+nama+"%").list().size();
            staff = Staff.find("nama_lengkap LIKE ?1","%"+nama+"%").page(page,paginate).list();
            Object totalPage = (staffSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",staff);
            result.put("total",staffSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //only email
        if(nama == null && email != null && phone_number == null){
            staffSize = Staff.find("email = ?1",email).list().size();
            staff = Staff.find("email = ?1",email).page(page,paginate).list();
            Object totalPage = (staffSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",staff);
            result.put("total",staffSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //only phone_number
        if(nama == null && email == null && phone_number != null){
            staffSize = Staff.find("phone_number = ?1",phone_number).list().size();
            staff = Staff.find("phone_number = ?1",phone_number).page(page,paginate).list();
            Object totalPage = (staffSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",staff);
            result.put("total",staffSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //===========================================================================

        staffSize = Staff.findAll().list().size();
        staff = Staff.findAll().page(page,paginate).list();
        Object totalPage = (staffSize + paginate - 1) / paginate;
        JsonObject result = new JsonObject();
        result.put("data",staff);
        result.put("total",staffSize);
        result.put("totalPage",totalPage);

        return Response.ok(result).build();
    }
}
