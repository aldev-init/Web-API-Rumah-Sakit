package Service;

import DTO.DaftarRawatInapDTO.CreateDaftarRawatInapRequest;
import DTO.DaftarRawatInapDTO.UpdateDaftarRawatInapRequest;
import Models.*;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DaftarRawatInapService {

    @ConfigProperty(name = "PAGINATE_PER_PAGE")
    int paginate;

    public Response GetAll(
            int page
    ){
        long daftarRawatInapSize = DaftarRawatInap.findAll().list().size();
        List<DaftarRawatInap> daftarRawatInap = DaftarRawatInap.findAll().page(page,paginate).list();
        Object totalPage = (daftarRawatInapSize + paginate - 1) / paginate;
        JsonObject result = new JsonObject();
        result.put("data",daftarRawatInap);
        result.put("total",daftarRawatInapSize);
        result.put("totalPage",totalPage);
        return Response.ok(result).build();
    }

    public Response CreateDaftarRawatInap(CreateDaftarRawatInapRequest request){
        //validate

        //======================================================================
        Optional<Dokter> dokterOptional = Dokter.findByIdOptional(request.dokter_id);
        Optional<Pasien> pasienOptional = Pasien.findByIdOptional(request.pasien_id);
        Optional<Perawat> perawatSatuOptional = Perawat.findByIdOptional(request.perawat_satu_id);
        Optional<Perawat> perawatDuaOptional = Perawat.findByIdOptional(request.perawat_dua_id);
        Optional<RuangInap> ruangInapOptional = RuangInap.findByIdOptional(request.ruang_inap_id);

        //cek data isPresent?
        if(!dokterOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Dokter tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!pasienOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Pasien tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!perawatSatuOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Perawat Satu tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!perawatDuaOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Perawat Dua tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!ruangInapOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Ruang Inap tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        //===========================================================================
        return DaftarRawatInapInsert(request);
    }

    public Response DaftarRawatInapInsert(CreateDaftarRawatInapRequest request){
        LocalDateTime time = LocalDateTime.now();

        DaftarRawatInap daftarRawatInap = new DaftarRawatInap();
        daftarRawatInap.setDokterId(Dokter.findById(request.dokter_id));
        daftarRawatInap.setPasienId(Pasien.findById(request.pasien_id));
        daftarRawatInap.setPerawatSatuId(Perawat.findById(request.perawat_satu_id));
        daftarRawatInap.setPerawatDuaId(Perawat.findById(request.perawat_dua_id));
        daftarRawatInap.setRuangInapId(RuangInap.findById(request.ruang_inap_id));
        daftarRawatInap.setStartDateTime(request.start_date_time);
        daftarRawatInap.setEndDateTime(request.end_date_time);
        daftarRawatInap.setCheckout(false);
        daftarRawatInap.setCreated_at(time);
        daftarRawatInap.setUpdated_at(time);

        DaftarRawatInap.persist(daftarRawatInap);

        JsonObject result = new JsonObject();
        result.put("data",daftarRawatInap);
        return Response.ok(result).build();
    }

    public Response UpdateDaftarRawatInap(long id,UpdateDaftarRawatInapRequest request){

        Optional<Dokter> dokterOptional = Dokter.findByIdOptional(request.dokter_id);
        Optional<Pasien> pasienOptional = Pasien.findByIdOptional(request.pasien_id);
        Optional<Perawat> perawatSatuOptional = Perawat.findByIdOptional(request.perawat_satu_id);
        Optional<Perawat> perawatDuaOptional = Perawat.findByIdOptional(request.perawat_dua_id);
        Optional<RuangInap> ruangInapOptional = RuangInap.findByIdOptional(request.ruang_inap_id);
        Optional<DaftarRawatInap> daftarRawatInapOptional = DaftarRawatInap.findByIdOptional(id);

        //cek data isPresent?
        if(!daftarRawatInapOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Daftar Rawat Inap tidak ditemukan");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!dokterOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Dokter tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!pasienOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Pasien tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!perawatSatuOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Perawat Satu tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!perawatDuaOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Perawat Dua tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!ruangInapOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Ruang Inap tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        //===========================================================================

        LocalDateTime time = LocalDateTime.now();

        DaftarRawatInap daftarRawatInapUpdate = daftarRawatInapOptional.get();
        daftarRawatInapUpdate.setDokterId(Dokter.findById(request.dokter_id));
        daftarRawatInapUpdate.setPasienId(Pasien.findById(request.pasien_id));
        daftarRawatInapUpdate.setPerawatSatuId(Perawat.findById(request.perawat_satu_id));
        daftarRawatInapUpdate.setPerawatDuaId(Perawat.findById(request.perawat_dua_id));
        daftarRawatInapUpdate.setRuangInapId(RuangInap.findById(request.ruang_inap_id));
        daftarRawatInapUpdate.setStartDateTime(request.start_date_time);
        daftarRawatInapUpdate.setEndDateTime(request.end_date_time);
        daftarRawatInapUpdate.setCheckout(false);
        daftarRawatInapUpdate.setUpdated_at(time);

        DaftarRawatInap.persist(daftarRawatInapUpdate);

        JsonObject result = new JsonObject();
        result.put("data",daftarRawatInapUpdate);
        return Response.ok(result).build();

    }

    public Response CheckOut(long id){
        Optional<DaftarRawatInap> daftarRawatInapOptional = DaftarRawatInap.findByIdOptional(id);
        if(!daftarRawatInapOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Daftar Rawat Inap tidak ditemukan");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        DaftarRawatInap checkout = daftarRawatInapOptional.get();
        RuangInap ruangInap = RuangInap.findById(checkout.getRuangInapId());

        //update
        checkout.setCheckout(true);
        DaftarRawatInap.persist(checkout);
        ruangInap.setKosong(true);
        RuangInap.persist(ruangInap);

        JsonObject result = new JsonObject();
        result.put("data",checkout);
        return Response.ok(result).build();

    }
}
