package Service;

import DTO.DokterDTO.CreateDokterRequest;
import Models.DaftarPertemuan;
import Models.Dokter;
import Util.GenerateReport;
import Util.ValidateInput;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class DokterService{

    @Inject
    DataSource dataSource;
    @ConfigProperty(name = "PAGINATE_PER_PAGE")
    int paginate;

    //pdf export
    public Uni<Response> exportPdf() throws Exception {
        String formatTime = "yyyy-MM-dd_HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTime);
        String timeDate = LocalDateTime.now().format(formatter);
        String fileName = "DaftarDokter_"+timeDate+".pdf";
        String pathOutput = "export/pdf/"+fileName;
        String jasperReportPath = "export/sample/DaftarDokter.jrxml";
        //generate report
        GenerateReport report = new GenerateReport()
                .jasperReportSamplePath(jasperReportPath)
                .outputFileName(pathOutput)
                .Connection(dataSource)
                .Query("select nama_lengkap,email,phone_number,gaji,status from dokter");
        report.generatePdfReport();
        //get report name
        String fileNamePath = report.getNameFile();
        //get file from path filename
        File file = new File(fileNamePath);
        //create Response Builder
        Response.ResponseBuilder responseBuilder = Response.ok((Object) file);
        //add header untuk informasi tambahan bahwa ada file untuk didownload
        responseBuilder.header("Content-Disposition","attachment;filename="+fileNamePath);
        //casting to Uni<Response>
        Uni<Response> responseUni = Uni.createFrom().item(responseBuilder.build());
        //return
        return responseUni;
        /*JsonObject result = new JsonObject();
        result.put("message","Export Success");
        return Response.ok(result).build();*/
    }
    public Uni<Response> exportWord() throws Exception{
        String formatTime = "yyyy-MM-dd_HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTime);
        String timeDate = LocalDateTime.now().format(formatter);
        String fileName = "DaftarDokter_"+timeDate+".docx";
        String pathOutput = "export/docx/"+fileName;
        String jasperReportPath = "export/sample/DaftarDokter.jrxml";
        GenerateReport report = new GenerateReport()
                .jasperReportSamplePath(jasperReportPath)
                .outputFileName(pathOutput)
                .Connection(dataSource)
                .Query("select nama_lengkap,email,phone_number,gaji,status from dokter");
        report.generateWordReport();
        String fileNamePath = report.getNameFile();
        File file = new File(fileNamePath);
        Response.ResponseBuilder responseBuilder = Response.ok((Object) file);
        responseBuilder.header("Content-Disposition","attachment;filename="+fileNamePath);
        Uni<Response> responseUni = Uni.createFrom().item(responseBuilder.build());
        return responseUni;
    }

    public Uni<Response> exportPPTX() throws Exception{
        String formatTime = "yyyy-MM-dd_HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTime);
        String timeDate = LocalDateTime.now().format(formatter);
        String fileName = "DaftarDokter_"+timeDate+".pptx";
        String pathOutput = "export/docx/"+fileName;
        String jasperReportPath = "export/sample/DaftarDokter.jrxml";
        GenerateReport report = new GenerateReport()
                .jasperReportSamplePath(jasperReportPath)
                .outputFileName(pathOutput)
                .Connection(dataSource)
                .Query("select nama_lengkap,email,phone_number,gaji,status from dokter");
        report.generatePPTXReport();
        String fileNamePath = report.getNameFile();
        File file = new File(fileNamePath);
        Response.ResponseBuilder responseBuilder = Response.ok((Object) file);
        responseBuilder.header("Content-Disposition","attachment;filename="+fileNamePath);
        Uni<Response> responseUni = Uni.createFrom().item(responseBuilder.build());
        return responseUni;
    }

    public Response CreateDockter(CreateDokterRequest request){
        if(!ValidateInput.BooleanInput(request.is_spesialis)){
            JsonObject result = new JsonObject();
            result.put("message","Format input is_spesialis salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        return DokterInsert(request);
    }

    public Response DokterInsert(CreateDokterRequest request){
        //validate
        if(!ValidateInput.EmailInput(request.email)){
            JsonObject result = new JsonObject();
            result.put("message","Format input email Salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!ValidateInput.PhoneNumberInput(request.phone_number)){
            JsonObject result = new JsonObject();
            result.put("message","Format input phone_number Salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(!ValidateInput.BooleanInput(request.is_spesialis)){
            JsonObject result = new JsonObject();
            result.put("message","Format input is_spesialis Salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        //===========================================================================

        LocalDateTime time = LocalDateTime.now();

        Dokter dokter = new Dokter();
        dokter.setNamaLengkap(request.nama_lengkap);
        dokter.setEmail(request.email);
        dokter.setPhoneNumber(request.phone_number);
        dokter.setSpesialisNama(request.spesialis_nama);
        dokter.setIsSpesialis(request.is_spesialis);
        dokter.setGaji(request.gaji);
        dokter.setStatus(request.status);
        dokter.setCreated_at(time);
        dokter.setUpdated_at(time);

        Dokter.persist(dokter);

        JsonObject result = new JsonObject();
        result.put("data",dokter);
        return Response.ok(result).build();
    }

    public Response GetAll(
            int page,
            //filter section
            String spesialis,
            String nama,
            String email,
            String phoneNumber

    ){
        long dokterSize;
        List<Dokter> dokter = new ArrayList<>();
        //validate input
        if(spesialis != null && !ValidateInput.BooleanInput(spesialis)){
            JsonObject result = new JsonObject();
            result.put("message","Format input is_spesialis salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(phoneNumber != null && !ValidateInput.PhoneNumberInput(phoneNumber)){
            JsonObject result = new JsonObject();
            result.put("message","Format input phone_number salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

        if(email != null && !ValidateInput.EmailInput(email)){
            JsonObject result = new JsonObject();
            result.put("message","Format input email salah!!");
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        //==============================================================================

        //all input filter
        if(nama != null && email != null && phoneNumber != null){
            if(spesialis != null){
                dokterSize = Dokter.find("nama_lengkap LIKE ?1 AND email = ?2 AND phone_number = ?3 AND is_spesialis = ?4","%"+nama+"%",email,phoneNumber,spesialis).list().size();
                dokter = Dokter.find("nama_lengkap LIKE ?1 AND email = ?2 AND phone_number = ?3 AND is_spesialis = ?4","%"+nama+"%",email,phoneNumber,spesialis).page(page,paginate).list();
                Object totalPage = (dokterSize + paginate - 1) / paginate;
                JsonObject result = new JsonObject();
                result.put("data",dokter);
                result.put("total",dokterSize);
                result.put("totalPage",totalPage);

                return Response.ok(result).build();
            }
            dokterSize = Dokter.find("nama_lengkap LIKE ?1 AND email = ?2 AND phone_number = ?3","%"+nama+"%",email,phoneNumber).list().size();
            dokter = Dokter.find("nama_lengkap LIKE ?1 AND email = ?2 AND phone_number = ?3","%"+nama+"%",email,phoneNumber).page(page,paginate).list();
            Object totalPage = (dokterSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",dokter);
            result.put("total",dokterSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //==============================================================================

        //double input query param
        //nama,email
        if(nama != null && email != null && phoneNumber == null){
            if(spesialis != null){
                dokterSize = Dokter.find("nama_lengkap LIKE ?1 AND email = ?2 AND is_spesialis = ?3","%"+nama+"%",email,spesialis).list().size();
                dokter = Dokter.find("nama_lengkap LIKE ?1 AND email = ?2 AND is_spesialis = ?3","%"+nama+"%",email,spesialis).page(page,paginate).list();
                Object totalPage = (dokterSize + paginate - 1) / paginate;
                JsonObject result = new JsonObject();
                result.put("data",dokter);
                result.put("total",dokterSize);
                result.put("totalPage",totalPage);

                return Response.ok(result).build();
            }
            dokterSize = Dokter.find("nama_lengkap LIKE ?1 AND email = ?2","%"+nama+"%",email).list().size();
            dokter = Dokter.find("nama_lengkap LIKE ?1 AND email = ?2","%"+nama+"%",email).page(page,paginate).list();
            Object totalPage = (dokterSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",dokter);
            result.put("total",dokterSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //nama,phone number
        if(nama != null && email == null && phoneNumber != null){
            if(spesialis != null){
                dokterSize = Dokter.find("nama_lengkap LIKE ?1 AND phone_number = ?2 AND is_spesialis = ?3","%"+nama+"%",phoneNumber,spesialis).list().size();
                dokter = Dokter.find("nama_lengkap LIKE ?1 AND phone_number = ?2 AND is_spesialis = ?3","%"+nama+"%",phoneNumber,spesialis).page(page,paginate).list();
                Object totalPage = (dokterSize + paginate - 1) / paginate;
                JsonObject result = new JsonObject();
                result.put("data",dokter);
                result.put("total",dokterSize);
                result.put("totalPage",totalPage);

                return Response.ok(result).build();
            }
            dokterSize = Dokter.find("nama_lengkap LIKE ?1 AND phone_number = ?2","%"+nama+"%",phoneNumber).list().size();
            dokter = Dokter.find("nama_lengkap LIKE ?1 AND phone_number = ?2","%"+nama+"%",phoneNumber).page(page,paginate).list();
            Object totalPage = (dokterSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",dokter);
            result.put("total",dokterSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //email,phone number
        if(nama == null && email != null && phoneNumber != null){
            if(spesialis != null){
                dokterSize = Dokter.find("email = ?1 AND phone_number = ?2 AND is_spesialis = ?3",email,phoneNumber,spesialis).list().size();
                dokter = Dokter.find("email = ?1 AND phone_number = ?2 AND is_spesialis = ?3",email,phoneNumber,spesialis).page(page,paginate).list();
                Object totalPage = (dokterSize + paginate - 1) / paginate;
                JsonObject result = new JsonObject();
                result.put("data",dokter);
                result.put("total",dokterSize);
                result.put("totalPage",totalPage);

                return Response.ok(result).build();
            }
            dokterSize = Dokter.find("email = ?1 AND phone_number = ?2",email,phoneNumber).list().size();
            dokter = Dokter.find("email = ?1 AND phone_number = ?2",email,phoneNumber).page(page,paginate).list();
            Object totalPage = (dokterSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",dokter);
            result.put("total",dokterSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //==============================================================================

        //single input 1 query param
        //only nama
        if(nama != null && email == null && phoneNumber == null){
            if(spesialis != null){
                dokterSize = Dokter.find("nama_lengkap LIKE ?1 AND is_spesialis = ?2","%"+nama+"%",spesialis).list().size();
                dokter = Dokter.find("nama_lengkap LIKE ?1 AND is_spesialis = ?2","%"+nama+"%",spesialis).page(page,paginate).list();
                Object totalPage = (dokterSize + paginate - 1) / paginate;
                JsonObject result = new JsonObject();
                result.put("data",dokter);
                result.put("total",dokterSize);
                result.put("totalPage",totalPage);

                return Response.ok(result).build();

            }
            dokterSize = Dokter.find("nama_lengkap LIKE ?1","%"+nama+"%").list().size();
            dokter = Dokter.find("nama_lengkap LIKE ?1","%"+nama+"%").page(page,paginate).list();
            Object totalPage = (dokterSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",dokter);
            result.put("total",dokterSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //only email
        if(nama == null && email != null && phoneNumber == null){
            if(spesialis != null){
                dokterSize = Dokter.find("email = ?1 AND is_spesialis = ?2",email,spesialis).list().size();
                dokter = Dokter.find("email = ?1 AND is_spesialis = ?2",email,spesialis).page(page,paginate).list();
                Object totalPage = (dokterSize + paginate - 1) / paginate;
                JsonObject result = new JsonObject();
                result.put("data",dokter);
                result.put("total",dokterSize);
                result.put("totalPage",totalPage);

                return Response.ok(result).build();
            }
            dokterSize = Dokter.find("email = ?1",email).list().size();
            dokter = Dokter.find("email = ?1",email).page(page,paginate).list();
            Object totalPage = (dokterSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",dokter);
            result.put("total",dokterSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }

        //only phone number
        if(nama == null && email == null && phoneNumber != null){
            if(spesialis != null){
                dokterSize = Dokter.find("phone_number = ?1 AND is_spesialis = ?2",phoneNumber,spesialis).list().size();
                dokter = Dokter.find("phone_number = ?1 AND is_spesialis = ?2",phoneNumber,spesialis).page(page,paginate).list();
                Object totalPage = (dokterSize + paginate - 1) / paginate;
                JsonObject result = new JsonObject();
                result.put("data",dokter);
                result.put("total",dokterSize);
                result.put("totalPage",totalPage);

                return Response.ok(result).build();
            }
            dokterSize = Dokter.find("phone_number = ?1",phoneNumber).list().size();
            dokter = Dokter.find("phone_number = ?1",phoneNumber).page(page,paginate).list();
            Object totalPage = (dokterSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",dokter);
            result.put("total",dokterSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }
        if(spesialis != null){
            dokterSize = Dokter.find("is_spesialis = ?1",spesialis).list().size();
            dokter = Dokter.find("is_spesialis = ?1",spesialis).page(page,paginate).list();
            Object totalPage = (dokterSize + paginate - 1) / paginate;
            JsonObject result = new JsonObject();
            result.put("data",dokter);
            result.put("total",dokterSize);
            result.put("totalPage",totalPage);

            return Response.ok(result).build();
        }else{
            dokter = Dokter.findAll().page(page,paginate).list();
            dokterSize = Dokter.count();
        }
        Object totalPage = (dokterSize + paginate - 1) / paginate;
        JsonObject result = new JsonObject();
        result.put("data",dokter);
        result.put("total",dokterSize);
        result.put("totalPage",totalPage);

        return Response.ok(result).build();
    }
}
