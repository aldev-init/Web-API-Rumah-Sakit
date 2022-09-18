import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title="API Rumah Sakit",
                version = "0.0.1",
                contact = @Contact(
                        name = "Muhammad Alghifari",
                        url = "https://wa.me/6282117707292",
                        email = "muhammadalghifari4321@gmail.com"),
                license = @License(
                        name = "Kawah Edukasi",
                        url = "https://kawahedukasi.id/"))
)

public class ProjectDocumentation extends Application {
}
