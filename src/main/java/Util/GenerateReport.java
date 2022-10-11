package Util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.view.JasperViewer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class GenerateReport {
    private String jasperReportSamplePath;
    private String outputFileName;
    private DataSource dataSource;
    private String query;

    //setter
    public GenerateReport jasperReportSamplePath(String jasperReportSamplePath){
        this.jasperReportSamplePath = jasperReportSamplePath;
        return this;
    }

    public GenerateReport outputFileName(String outputFileName){
        this.outputFileName = outputFileName;
        return this;
    }

    public GenerateReport Connection(DataSource dataSource){
        this.dataSource = dataSource;
        return this;
    }

    public GenerateReport Query(String query){
        this.query = query;
        return this;
    }

    //getter
    public String getNameFile(){
        return this.outputFileName;
    }


    public void generatePdfReport() throws Exception {
        //load sample design dari jaspersoft studio
        JasperDesign jasperDesign = JRXmlLoader.load(this.jasperReportSamplePath);
        //object untuk buat query
        JRDesignQuery query = new JRDesignQuery();
        query.setText(this.query);
        //set query atau inject query ke sample yg diload
        jasperDesign.setQuery(query);
        //buat report
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        try{
            //buat koneksi
            Connection connection = this.dataSource.getConnection();
            //cetak report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,connection);
            //export
            JasperExportManager.exportReportToPdfFile(jasperPrint,this.outputFileName);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void generateWordReport() throws Exception {
        //load sample design dari jaspersoft studio
        JasperDesign jasperDesign = JRXmlLoader.load(this.jasperReportSamplePath);
        //object untuk buat query
        JRDesignQuery query = new JRDesignQuery();
        query.setText(this.query);
        //set query atau inject query ke sample yg diload
        jasperDesign.setQuery(query);
        //create report
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        try{
            //buat koneksi
            Connection connection = this.dataSource.getConnection();
            //cetak report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,connection);
            //export
            JRDocxExporter export = new JRDocxExporter();
            export.setExporterInput(new SimpleExporterInput(jasperPrint));
            File exportReportFile = new File(this.outputFileName);
            export.setExporterOutput(new SimpleOutputStreamExporterOutput(exportReportFile));
            export.exportReport();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void generatePPTXReport() throws Exception{
        //load sample dari design yg dibuat dari jaspersoft studio
        JasperDesign jasperDesign = JRXmlLoader.load(this.jasperReportSamplePath);
        //object untuk buat query
        JRDesignQuery query = new JRDesignQuery();
        query.setText(this.query);
        //set query atau inject query ke sample yg diload
        jasperDesign.setQuery(query);
        //buat report
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        try{
            //buat koneksi
            Connection connection = this.dataSource.getConnection();
            //cetak report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,connection);
            //export
            JRPptxExporter export = new JRPptxExporter();
            export.setExporterInput(new SimpleExporterInput(jasperPrint));
            File exportReportFile = new File(this.outputFileName);
            export.setExporterOutput(new SimpleOutputStreamExporterOutput(exportReportFile));
            export.exportReport();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
