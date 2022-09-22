package Service;

import DTO.UpdateGajiDTO.CreateUpdateGajiRequest;
import Models.Dokter;
import Models.Perawat;
import Models.Staff;
import Util.ValidateInput;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.Optional;

@ApplicationScoped
public class UpdateGajiService {

    public Response UpdateGaji(CreateUpdateGajiRequest request){
        //validate
        if(!ValidateInput.pegawaiKategori(request.pegawai_kategori)){
            JsonObject result = new JsonObject();
            result.put("message","Kategori Pegawai tidak tersedia!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //===============================================================

        //tolowercase request
        String pegawaiKategori = request.pegawai_kategori.toLowerCase();
        //case
        if(pegawaiKategori.equals("dokter")){
            Optional<Dokter> dokter = Dokter.find("id = ?1",request.pegawaiId).firstResultOptional();
            if(!dokter.isPresent()){
                JsonObject result = new JsonObject();
                result.put("message","Pegawai tidak ditemukan.");
                return Response.status(Response.Status.NOT_FOUND).entity(result).build();
            }
            //update
            Dokter dokterObj = dokter.get();
            dokterObj.setGaji(request.gaji);
            Dokter.persist(dokterObj);

            Dokter resultDokter = Dokter.find("id = ?1",request.pegawaiId).singleResult();
            JsonObject result = new JsonObject();
            result.put("data",resultDokter);
            return Response.ok(result).build();
        }
        if(pegawaiKategori.equals("perawat")){
            Optional<Perawat> perawat = Perawat.find("id = ?1",request.pegawaiId).firstResultOptional();
            if(!perawat.isPresent()){
                JsonObject result = new JsonObject();
                result.put("message","Pegawai tidak ditemukan.");
                return Response.status(Response.Status.NOT_FOUND).entity(result).build();
            }
            //update
            Perawat perawatObj = perawat.get();
            perawatObj.setGaji(request.gaji);
            Perawat.persist(perawatObj);

            Perawat resultPerawat = Perawat.find("id = ?1",request.pegawaiId).singleResult();
            JsonObject result = new JsonObject();
            result.put("data",resultPerawat);
            return Response.ok(result).build();
        }
        if(pegawaiKategori.equals("staff")){
            Optional<Staff> staff = Staff.find("id = ?1",request.pegawaiId).firstResultOptional();
            if(!staff.isPresent()){
                JsonObject result = new JsonObject();
                result.put("message","Pegawai tidak ditemukan.");
                return Response.status(Response.Status.NOT_FOUND).entity(result).build();
            }
            //update
            Staff staffObj = staff.get();
            staffObj.setGaji(request.gaji);
            Staff.persist(staffObj);

            Staff resultStaff = Staff.find("id = ?1",request.pegawaiId).singleResult();
            JsonObject result = new JsonObject();
            result.put("data",resultStaff);
            return Response.ok(result).build();
        }
        return Response.noContent().build();
    }
}
