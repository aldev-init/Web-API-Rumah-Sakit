package Service;

import DTO.DaftarShift.CreateDaftarShiftRequest;
import DTO.DaftarShift.UpdateDaftarShiftRequest;
import Models.DaftarShift;
import Models.Perawat;
import Models.Staff;
import Util.ValidateInput;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class DaftarShiftService {
    public Response CreateShift(CreateDaftarShiftRequest request){
        String kategori = request.kategori;
        //validate
        if(!ValidateInput.daftarShiftKategori(request.kategori)){
            JsonObject result = new JsonObject();
            result.put("message","Kategori Pegawai tidak tersedia!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //=============================================================================

        //cek pegawai
        String lower = kategori.toLowerCase();
        Optional pegawai;
        if(lower.equals("perawat")){
            pegawai = Perawat.findByIdOptional(request.foreign_id);
            if(!pegawai.isPresent()){
                JsonObject result = new JsonObject();
                result.put("message","Id Pegawai tidak ditemukan dalam kategori "+kategori);
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }

        }

        if(lower.equals("staff")){
            pegawai = Staff.findByIdOptional(request.foreign_id);
            if(!pegawai.isPresent()){
                JsonObject result = new JsonObject();
                result.put("message","Id Pegawai tidak ditemukan dalam kategori "+kategori);
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }

        }
        LocalDateTime time = LocalDateTime.now();

        DaftarShift daftarShift = new DaftarShift();
        daftarShift.setKategori(kategori);
        daftarShift.setForeignId(request.foreign_id);
        daftarShift.setStartDateTime(request.startDateTime);
        daftarShift.setEndDateTime(request.endDateTime);
        daftarShift.setCreated_at(time);
        daftarShift.setUpdated_at(time);

        //save
        DaftarShift.persist(daftarShift);

        JsonObject result = new JsonObject();
        result.put("data",daftarShift);
        return Response.ok(result).build();
    }

    public Response UpdateShift(
            long id,
            UpdateDaftarShiftRequest request
    ){

        String kategori = request.kategori;
        //validate
        if(!ValidateInput.daftarShiftKategori(request.kategori)){
            JsonObject result = new JsonObject();
            result.put("message","Kategori Pegawai tidak tersedia!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //=============================================================================
        //cek pegawai
        String lower = kategori.toLowerCase();
        Optional pegawai;
        if(lower.equals("perawat")){
            pegawai = Perawat.findByIdOptional(request.foreign_id);
            if(!pegawai.isPresent()){
                JsonObject result = new JsonObject();
                result.put("message","Id Pegawai tidak ditemukan dalam kategori "+kategori);
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }

        }

        if(lower.equals("staff")){
            pegawai = Staff.findByIdOptional(request.foreign_id);
            if(!pegawai.isPresent()){
                JsonObject result = new JsonObject();
                result.put("message","Id Pegawai tidak ditemukan dalam kategori "+kategori);
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }

        }

        LocalDateTime time = LocalDateTime.now();
        Optional<DaftarShift> daftarShift = DaftarShift.findByIdOptional(id);
        if(!daftarShift.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Daftar Shift tidak ditemukan!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        DaftarShift updateDaftarShift = daftarShift.get();
        updateDaftarShift.setKategori(request.kategori);
        updateDaftarShift.setForeignId(request.foreign_id);
        updateDaftarShift.setStartDateTime(request.startDateTime);
        updateDaftarShift.setEndDateTime(request.startDateTime);
        updateDaftarShift.setUpdated_at(time);

        //update
        DaftarShift.persist(updateDaftarShift);

        JsonObject result = new JsonObject();
        result.put("data",updateDaftarShift);
        return Response.ok(result).build();
    }
}
