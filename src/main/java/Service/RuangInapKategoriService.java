package Service;

import Util.ValidateInput;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class RuangInapKategoriService {

    public Response GetAll(){
        JsonObject result = new JsonObject();
        result.put("enum", ValidateInput.kategoriRuangan);
        return Response.ok(result).build();
    }

}
