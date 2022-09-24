package Service;

import Util.ValidateInput;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class KategoriObatService {

    public Response GetAll(){
        JsonObject result = new JsonObject();
        result.put("enum", ValidateInput.kategoriObat);
        return Response.ok(result).build();
    }
}
