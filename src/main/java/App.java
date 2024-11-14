import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.awt.GraphicsEnvironment;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**

 */
public class App {

    public static void main(String[] args) {
        // 1. Listing all fonts registered in the JVM
        listAvailableFonts();

        // 2. Generating Jasper Report
        String fileName = "report.pdf";
        File file = new File(fileName);
        Resource resource = new ClassPathResource("reports/jasperReport.jrxml");

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            SimpleOutputStreamExporterOutput simpleOutputStreamExporterOutput = new SimpleOutputStreamExporterOutput(bos);
            exporter.setExporterOutput(simpleOutputStreamExporterOutput);
            SimplePdfExporterConfiguration simplePdfExporterConfiguration = new SimplePdfExporterConfiguration();
            simplePdfExporterConfiguration.setMetadataAuthor("eleos");
            exporter.setConfiguration(simplePdfExporterConfiguration);
            exporter.exportReport();
            FileUtils.writeByteArrayToFile(file, bos.toByteArray());
            simpleOutputStreamExporterOutput.close();
            listAvailableFonts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to list all fonts registered in the JVM
    public static void listAvailableFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        System.out.println("Available fonts on this JVM:");
        for (String fontName : fontNames) {
            System.out.println(fontName);
        }
    }
}
