package Service;

import DTO.DokterDTO.CreateDokterRequest;
import Models.DaftarPertemuan;
import Models.Dokter;
import Util.GenerateReport;
import Util.ValidateInput;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.lowagie.text.pdf.PdfDocument;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import net.sf.jasperreports.export.pdf.classic.ClassicDocument;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.*;
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
    public Response exportPdf(int pagination) throws Exception {
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
        report.generatePdfReport(pagination);
        //get report name
        String fileNamePath = report.getNameFile();
        //get file from path filename
        File file = new File(fileNamePath);
        //create Response Builder
        Response.ResponseBuilder responseBuilder = Response.ok((Object) file);
        //add header untuk informasi tambahan bahwa ada file untuk didownload
        //sekaligus untuk memberi nama file yang didownload
        /*
            Response Builder untuk modifikasi Response
         */
        responseBuilder.header("Content-Disposition","attachment;filename="+fileName);

        //biar asynchronus bisa pake uni,tapi karena belum mengerti cara pakai jadi jangan dlu
        //casting to Uni<Response>
        /*Uni<Response> responseUni = Uni.createFrom().item(responseBuilder.build());*/


        return responseBuilder.build();
    }
    /*public Response exportWord(int pagination) throws Exception{
        String formatTime = "yyyy-MM-dd_HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTime);
        String timeDate = LocalDateTime.now().format(formatter);
        String fileName = "DaftarDokter_"+timeDate+".docx";
        String pathOutput = "export/docx/"+fileName;
        String jasperReportPath = "export/sample/DaftarDokterFlat.jrxml";
        GenerateReport report = new GenerateReport()
                .jasperReportSamplePath(jasperReportPath)
                .outputFileName(pathOutput)
                .Connection(dataSource)
                .Query("select nama_lengkap,email,phone_number,gaji,status from dokter");
        report.generateWordReport(pagination);
        String fileNamePath = report.getNameFile();
        File file = new File(fileNamePath);
        Response.ResponseBuilder responseBuilder = Response.ok((Object) file);
        responseBuilder.header("Content-Disposition","attachment;filename="+fileName);
        Uni<Response> responseUni = Uni.createFrom().item(responseBuilder.build());
        return responseBuilder.build();
    }*/
    public Response exportWord(int pagination) throws FileNotFoundException, IOException
            , org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        String image = "/home/aldev/Gambar/logo-rumahsakit.png";
        XWPFDocument doc = new XWPFDocument();
        // add png image
        XWPFRun r4 = doc.createParagraph().createRun();
        r4.addBreak();
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r = p.createRun();
        try (FileInputStream is = new FileInputStream(image)) {
            r.addPicture(is, Document.PICTURE_TYPE_PNG, image,
                    Units.toEMU(150), Units.toEMU(150));
        }
        XWPFParagraph p1 = doc.createParagraph();
        p1.setPageBreak(true);
        p1.setAlignment(ParagraphAlignment.CENTER);
        // Set Text to Bold and font size to 22 for first paragraph
        XWPFRun r1 = p1.createRun();
        r1.setBold(true);
        r1.setFontSize(22);
        r1.setText("Rumah Sakit Bakti Husada");
        r1.setFontFamily("Arial");
        r1.setColor("344762");
        r1.addBreak();

        XWPFRun r2 = p1.createRun();
        r2.setFontSize(15);
        r2.setText("Jalan Ceuri Jalan Terusan Kopo No.KM 13.5, Katapang, Kec. Katapang, Kabupaten Bandung, Jawa Barat 40971");
        r2.setFontFamily("Arial");
        r2.setColor("344762");
        r2.addBreak();

        XWPFRun r3 = p1.createRun();
        r3.setFontSize(15);
        r3.setText("Melayani Dengan Senyuman,Anda Sembuh Kami Nganggur");
        r3.setFontFamily("Arial");
        r3.setColor("344762");
        r3.addBreak();

        XWPFTable table = doc.createTable();
        // Creating first Row
        XWPFTableRow row1 = table.getRow(0);
        row1.getCell(0).setText("NAMA LENGKAP");
        row1.addNewTableCell().setText("EMAIL");
        row1.addNewTableCell().setText("NOMOR TELEPON");
        row1.addNewTableCell().setText("GAJI");
        row1.addNewTableCell().setText("STATUS");

        List<Dokter> dokter = Dokter.listAll();
        // Creating second Row
        for (int i = 0;i<dokter.size();i++){
            XWPFTableRow row2 = table.createRow();
            row2.getCell(0).setText(dokter.get(i).getNamaLengkap());
            row2.getCell(1).setText(dokter.get(i).getEmail());
            row2.getCell(2).setText(dokter.get(i).getPhoneNumber());
            row2.getCell(3).setText(String.valueOf(dokter.get(i).getGaji()));
            row2.getCell(4).setText(dokter.get(i).getStatus());
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        doc.write(b);
        String formatTime = "yyyy-MM-dd_HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTime);
        String timeDate = LocalDateTime.now().format(formatter);
        String fileName = "DaftarDokter_"+timeDate+".docx";
        Response.ResponseBuilder responseBuilder = Response.ok(b.toByteArray());
        responseBuilder.header("Content-Disposition","attachment; filename="+fileName);
        return responseBuilder.build();
    }


    public Response exportPPTX(int pagination) throws Exception{
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
        report.generatePPTXReport(pagination);
        String fileNamePath = report.getNameFile();
        File file = new File(fileNamePath);
        Response.ResponseBuilder responseBuilder = Response.ok((Object) file);
        responseBuilder.header("Content-Disposition","attachment;filename="+fileName);
        /*Uni<Response> responseUni = Uni.createFrom().item(responseBuilder.build());*/
        return responseBuilder.build();
    }

    public Response exportExcel(int pagination) throws Exception {
        String formatTime = "yyyy-MM-dd_HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatTime);
        String timeDate = LocalDateTime.now().format(formatter);
        String fileName = "DaftarDokter_"+timeDate+".xlsx";
        String pathOutput = "export/xlsx/"+fileName;
        String jasperReportPath = "export/sample/DaftarDokterv2.jrxml";
        GenerateReport report = new GenerateReport()
                .jasperReportSamplePath(jasperReportPath)
                .outputFileName(pathOutput)
                .Connection(dataSource)
                .Query("select nama_lengkap,email,phone_number,gaji,status from dokter");
        report.generateExcelReport(pagination);
        String fileNamePath = report.getNameFile();
        File file = new File(fileNamePath);
        Response.ResponseBuilder responseBuilder = Response.ok((Object) file);
        responseBuilder.header("Content-Disposition","attachment;filename="+fileName);
        Uni<Response> responseUni = Uni.createFrom().item(responseBuilder.build());
        return responseBuilder.build();
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
