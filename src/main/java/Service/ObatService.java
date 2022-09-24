package Service;

import DTO.ObatDTO.CreateObatRequest;
import DTO.ObatDTO.UpdateObatRequest;
import Models.Obat;
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
public class ObatService {

    @ConfigProperty(name = "PAGINATE_PER_PAGE")
    int paginate;

    public Response GetAll(
            int page,
            String kategori,
            String produksi,
            String nama
    ){
        long obatSize;
        //validate
        if(kategori != null && !ValidateInput.kategoriObat(kategori)){
            JsonObject result = new JsonObject();
            result.put("message","Kategori Obat tidak tersedia");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //====================================================================
        List<Obat> obat = new ArrayList<>();
        //all filter
        if(kategori != null && produksi != null && nama != null){
            obatSize = Obat.find("nama_obat LIKE ?1 AND obat_kategori = ?2 AND produksi = ?3","%"+nama+"%",kategori,produksi).list().size();
            obat = Obat.find("nama_obat LIKE ?1 AND obat_kategori = ?2 AND produksi = ?3","%"+nama+"%",kategori,produksi).page(page,paginate).list();
            Object totalPage = (obatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",obat);
            result.put("total",obatSize);
            result.put("totalPage",totalPage);
            return Response.ok(result).build();
        }

        //============================================================================================

        //double filter
        //kategori,produksi
        if(kategori != null && produksi != null && nama == null){
            obatSize = Obat.find("obat_kategori = ?1 AND produksi = ?2",kategori,produksi).list().size();
            obat = Obat.find("obat_kategori = ?1 AND produksi = ?2",kategori,produksi).page(page,paginate).list();
            Object totalPage = (obatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",obat);
            result.put("total",obatSize);
            result.put("totalPage",totalPage);
            return Response.ok(result).build();
        }

        //kategori,nama
        if(kategori != null && produksi == null && nama != null){
            obatSize = Obat.find("nama_obat LIKE ?1 AND obat_kategori = ?2","%"+nama+"%",kategori).list().size();
            obat = Obat.find("nama_obat LIKE ?1 AND obat_kategori = ?2","%"+nama+"%",kategori).page(page,paginate).list();
            Object totalPage = (obatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",obat);
            result.put("total",obatSize);
            result.put("totalPage",totalPage);
            return Response.ok(result).build();
        }

        //produksi,nama
        if(kategori == null && produksi != null && nama != null){
            obatSize = Obat.find("nama_obat LIKE ?1 AND produksi = ?2","%"+nama+"%",produksi).list().size();
            obat = Obat.find("nama_obat LIKE ?1 AND produksi = ?2","%"+nama+"%",produksi).page(page,paginate).list();
            Object totalPage = (obatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",obat);
            result.put("total",obatSize);
            result.put("totalPage",totalPage);
            return Response.ok(result).build();
        }

        //============================================================================================

        //single filter
        //nama
        if(kategori == null && produksi == null && nama != null){
            obatSize = Obat.find("nama_obat LIKE ?1","%"+nama+"%").list().size();
            obat = Obat.find("nama_obat LIKE ?1","%"+nama+"%").page(page,paginate).list();
            Object totalPage = (obatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",obat);
            result.put("total",obatSize);
            result.put("totalPage",totalPage);
            return Response.ok(result).build();
        }

        //produksi
        if(kategori == null && produksi != null && nama == null){
            obatSize = Obat.find("produksi = ?1",produksi).list().size();
            obat = Obat.find("produksi = ?1",produksi).page(page,paginate).list();
            Object totalPage = (obatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",obat);
            result.put("total",obatSize);
            result.put("totalPage",totalPage);
            return Response.ok(result).build();
        }

        //kategori
        if(kategori != null && produksi == null && nama == null){
            obatSize = Obat.find("obat_kategori = ?1",kategori).list().size();
            obat = Obat.find("obat_kategori = ?1",kategori).page(page,paginate).list();
            Object totalPage = (obatSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",obat);
            result.put("total",obatSize);
            result.put("totalPage",totalPage);
            return Response.ok(result).build();
        }

        //============================================================================================

        obatSize = Obat.findAll().list().size();
        obat = Obat.findAll().page(page,paginate).list();
        Object totalPage = (obatSize + paginate - 1) / paginate;
        JsonObject result = new JsonObject();
        result.put("data",obat);
        result.put("total",obatSize);
        result.put("totalPage",totalPage);
        return Response.ok(result).build();
    }

    public Response CreateObat(CreateObatRequest request){
        //validate
        if(!ValidateInput.kategoriObat(request.obat_kategori)){
            JsonObject result = new JsonObject();
            result.put("message","Kategori Obat tidak tersedia");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //===============================================

        return ObatInsert(request);
    }

    public Response ObatInsert(CreateObatRequest request){
        LocalDateTime time = LocalDateTime.now();

        Obat obat = new Obat();
        obat.setNamaObat(request.nama_obat);
        obat.setProduksi(request.produksi);
        obat.setObatKategori(request.obat_kategori);
        obat.setDeskripsi(request.deskripsi);
        obat.setCreated_at(time);
        obat.setUpdated_at(time);

        //save
        Obat.persist(obat);
        JsonObject result = new JsonObject();
        result.put("data",obat);
        return Response.ok(result).build();
    }

    public Response UpdateObat(long id,UpdateObatRequest request){
        //validate
        if(!ValidateInput.kategoriObat(request.obat_kategori)){
            JsonObject result = new JsonObject();
            result.put("message","Kategori Obat tidak tersedia");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //==================================================================
        Optional<Obat> obatOptional = Obat.findByIdOptional(id);
        if(!obatOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Obat tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        LocalDateTime time = LocalDateTime.now();

        Obat obatUpdate = obatOptional.get();
        obatUpdate.setNamaObat(request.nama_obat);
        obatUpdate.setProduksi(request.produksi);
        obatUpdate.setObatKategori(request.obat_kategori);
        obatUpdate.setDeskripsi(request.deskripsi);
        obatUpdate.setUpdated_at(time);

        //save
        Obat.persist(obatUpdate);
        JsonObject result = new JsonObject();
        result.put("data",obatUpdate);
        return Response.ok(result).build();
    }

    public Response DeleteObat(long id){
        Optional<Obat> obatOptional = Obat.findByIdOptional(id);
        if(!obatOptional.isPresent()){
            JsonObject result = new JsonObject();
            result.put("message","Id Obat tidak ditemukan.");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        Obat obatDelete = obatOptional.get();
        obatDelete.delete();
        JsonObject result = new JsonObject();
        result.put("message","Data berhasil dihapus");
        return Response.ok(result).build();
    }
}
