package Util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
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
        //load jasper sample from jasper soft studio
        JasperDesign jasperDesign = JRXmlLoader.load(this.jasperReportSamplePath);
        //initialize query and set query
        JRDesignQuery query = new JRDesignQuery();
        query.setText(this.query);
        jasperDesign.setQuery(query);
        //create report
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        try{
            //create connection
            Connection connection = this.dataSource.getConnection();
            //print report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,connection);
            //export
            JasperExportManager.exportReportToPdfFile(jasperPrint,this.outputFileName);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
