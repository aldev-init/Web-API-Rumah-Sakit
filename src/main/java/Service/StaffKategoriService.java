package Service;

import Util.ValidateInput;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class StaffKategoriService {

    public Response GetAll(){
        JsonObject result = new JsonObject();
        result.put("enum", ValidateInput.posisiStaff);
        return Response.ok(result).build();
    }

}
