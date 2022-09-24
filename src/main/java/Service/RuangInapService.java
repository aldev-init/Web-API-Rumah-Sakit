package Service;

import DTO.RuangInapDTO.CreateRuangInapRequest;
import DTO.RuangInapDTO.UpdateRuangInapRequest;
import Models.Perawat;
import Models.RuangInap;
import Util.ValidateInput;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RuangInapService {

    @ConfigProperty(name = "PAGINATE_PER_PAGE")
    int paginate;

    public Response GetAll(int page){
        long ruangInapSize;
        List<RuangInap> ruangInap;
        ruangInapSize = RuangInap.findAll().list().size();
        ruangInap = RuangInap.findAll().page(page,paginate).list();
        Object totalPage = (ruangInapSize + paginate - 1) / paginate;
        JsonObject result = new JsonObject();
        result.put("data",ruangInap);
        result.put("total",ruangInapSize);
        result.put("totalPage",totalPage);

        return Response.ok(result).build();
    }

    public Response CreateRuangInap(CreateRuangInapRequest request){
        //validate
        if(!ValidateInput.kategoriRuangan(request.kategori_ruangan)){
            JsonObject result = new JsonObject();
            result.put("message","Kategori Ruangan tidak tersedia.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //===========================================================

        return RuangInapInsert(request);
    }

    public Response RuangInapInsert(CreateRuangInapRequest request){
        LocalDateTime time = LocalDateTime.now();

        RuangInap ruangInap = new RuangInap();
        ruangInap.setPrefixRuangan(request.prefix_ruangan);
        ruangInap.setNomorRuangan(request.nomor_ruangan);
        ruangInap.setKategoriRuangan(request.kategori_ruangan);
        ruangInap.setKosong(false);
        ruangInap.setCreated_at(time);
        ruangInap.setUpdated_at(time);

        RuangInap.persist(ruangInap);

        JsonObject result = new JsonObject();
        result.put("data",ruangInap);
        return Response.ok(result).build();
    }

   public Response UpdateRuangInap(long id,UpdateRuangInapRequest request){
        Optional<RuangInap> ruangInapOptional = RuangInap.findByIdOptional(id);
        if(!ruangInapOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Ruang Inap tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!ruangInapOptional.get().isKosong()){
            JsonObject result = new JsonObject();
            result.put("message","Ruang Inap tidak bisa diubah,Ruang inap tidak kosong!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        LocalDateTime time = LocalDateTime.now();

        RuangInap ruangInapUpdate = ruangInapOptional.get();
        ruangInapUpdate.setKategoriRuangan(request.kategori_ruangan);
        ruangInapUpdate.setPrefixRuangan(request.prefix_ruangan);
        ruangInapUpdate.setNomorRuangan(request.nomor_ruangan);
        ruangInapUpdate.setUpdated_at(time);

        RuangInap.persist(ruangInapUpdate);

        JsonObject result = new JsonObject();
        result.put("data",ruangInapUpdate);
        return Response.ok(result).build();
    }

    public Response DeleteRuangInap(long id){
        Optional<RuangInap> ruangInapOptional = RuangInap.findByIdOptional(id);
        if(!ruangInapOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Ruang Inap tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!ruangInapOptional.get().isKosong()){
            JsonObject result = new JsonObject();
            result.put("message","Ruang Inap tidak bisa diubah,Ruang inap tidak kosong!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        RuangInap ruangInapDelete = ruangInapOptional.get();
        ruangInapDelete.delete();
        JsonObject result = new JsonObject();
        result.put("message","Data berhasil dihapus.");
        return Response.ok(result).build();
    }
}
