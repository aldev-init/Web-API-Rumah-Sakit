package Service;

import DTO.JadwalPraktikDTO.CreateJadwalPraktikRequest;
import DTO.JadwalPraktikDTO.UpdateJadwalPraktikRequest;
import Models.Dokter;
import Models.JadwalPraktik;
import Util.ValidateInput;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class JadwalPraktikService {

    public Response CreateJadwalPraktik(CreateJadwalPraktikRequest request){
        //validate
        if(!ValidateInput.hariInput(request.hari)){
            JsonObject result = new JsonObject();
            result.put("message","input hari salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //========================================================================

        //cek dokter
        Optional<Dokter> dokterOptional = Dokter.findByIdOptional(request.dokter_id);
        if(!dokterOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Dokter tidak ditemukan");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        LocalDateTime time = LocalDateTime.now();

        JadwalPraktik jadwalPraktik = new JadwalPraktik();
        jadwalPraktik.setHari(request.hari);
        jadwalPraktik.setStartTime(request.start_time);
        jadwalPraktik.setEndTime(request.end_time);
        jadwalPraktik.setDeskripsi(request.deskripsi);
        jadwalPraktik.setDokterId(Dokter.findById(request.dokter_id));
        jadwalPraktik.setCreated_at(time);
        jadwalPraktik.setUpdated_at(time);

        //save
        JadwalPraktik.persist(jadwalPraktik);

        JsonObject result = new JsonObject();
        result.put("data",jadwalPraktik);
        return Response.ok(result).build();
    }

    public Response UpdateJadwalPraktik(long id, UpdateJadwalPraktikRequest request){
        //validate
        if(!ValidateInput.hariInput(request.hari)){
            JsonObject result = new JsonObject();
            result.put("message","input hari salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //========================================================================

        //cek id jadwal praktik
        Optional<JadwalPraktik> jadwalPraktikOptional = JadwalPraktik.findByIdOptional(id);
        if(!jadwalPraktikOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Jadwal Praktik tidak ditemukan");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        //cek dokter
        Optional<Dokter> dokterOptional = Dokter.findByIdOptional(request.dokter_id);
        if(!dokterOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Dokter tidak ditemukan");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        LocalDateTime time = LocalDateTime.now();

        JadwalPraktik praktikUpdate = jadwalPraktikOptional.get();
        praktikUpdate.setHari(request.hari);
        praktikUpdate.setStartTime(request.start_time);
        praktikUpdate.setEndTime(request.end_time);
        praktikUpdate.setDeskripsi(request.deskripsi);
        praktikUpdate.setDokterId(Dokter.findById(request.dokter_id));
        praktikUpdate.setUpdated_at(time);

        //save
        JadwalPraktik.persist(praktikUpdate);

        JsonObject result = new JsonObject();
        result.put("data",praktikUpdate);
        return Response.ok(result).build();

    }
}
