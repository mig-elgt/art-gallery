/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galeriadearte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Clase utilizada para implementar un reporte.
 * @author Miguel
 */
public class ReporteVenta {
    
    private Connection conexion;
    
    /**
     * @param user Nombre del usuario
     * @param password Contraseña del usuario
     */
    public ReporteVenta(String user, String password){
        
        this.IniciaReporte(user, password);
    }
   
    /**
     * Este método se encarga de hacer la conexion con la base de datos.
     * @param user Nombre del usuario
     * @param password Contraseña del usuario
     */
    public void IniciaReporte(String user, String password){
        
        try
        {
            Class.forName("org.postgresql.Driver");//Se carga el driver de postgresql
            try
            {
                conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/GaleriaDeArte", user,password);
            }catch(SQLException ex)
            {
                  JOptionPane.showMessageDialog(null,ex.getMessage());
            }
        }catch(Exception e)
        {
             JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
    
    /**
     * Este método se utiliza para ejecutar el reporte. Mostrandolo en un archivo pdf
     * @param venta Clave de venta
     * @param exp   Título de la exposición
     * @param cliente   Nombre del cliente
     * @param vendedor  Nombre del vendedor
     * @param total Total de la venta
     * @throws SQLException Manejo de expeciones de SQL
     */
    public void EjecutaReporte(long venta,String exp, String cliente, String vendedor, String total) throws SQLException{
       
        JasperReport reporte;
        JasperPrint jasperPrint;
        Map<String, Object> parametros;
        JRExporter exporter = new JRPdfExporter();
        JasperViewer jV;
           
        try
        {
            
            reporte = (JasperReport)JRLoader.loadObject("boucher.jasper");//Se carga el formato del reporte
            parametros = new HashMap<String, Object>();
            
            //Se agregan los parametros que utlizara el reporte, en un mapa
            parametros.put("venta", venta);
            parametros.put("exposicion", exp);
            parametros.put("cliente",cliente);
            parametros.put("vendedor",vendedor);
            parametros.put("total",total);
            
            //Este objeto es la representacion de nuestro reporte
            jasperPrint = JasperFillManager.fillReport(reporte, parametros, conexion);   
            jV = new JasperViewer(jasperPrint,false);
            
            jV.setTitle("Reporte de ventas");
            jV.setVisible(true);
            
            exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint); 
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE,new java.io.File("ventaPdf.pdf"));
            exporter.exportReport();
            
            conexion.close();
       
       }catch(JRException E)
       {
           E.printStackTrace();
       }
    }
}
