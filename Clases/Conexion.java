
package galeriadearte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Autor: Miguel Angel Galicia Torrez 
 * Fecha de Inicio: 23 Marzo 2013
 * 
 * Esta clase se utiliza para poder hacer poder hacer la conexion
 * con la base de datos asi como ejecutar las consultas de insercion, eleminacion y 
 * modificaciond de datos en las entidades
 */
public class Conexion {

    private Connection conexion;
    private Statement sentencia;
    private DefaultTableModel modelo;
    private ResultSetMetaData rsMd;
    private ResultSet rs;
    private String user;
    private String password;
    private String url;
    private String sql;
    private String driver;
    private JTable tabla;
    private String queryTabla;
    
    /**
     * 
     * @param u Nombre del usuario 
     * @param p Contraseña del usuario
     * @param t Tabla que contiene los registros de algun formulario
     * @param qT Consulta SQL que será ejecutada para llenar la tabla en el formulario 
     */
    public Conexion(String u, String p, JTable t, String qT){
      
       user = u;
       password = p;
       tabla = t;
       queryTabla = qT;
       url = "jdbc:postgresql://localhost:5432/GaleriaDeArte";
       driver = "org.postgresql.Driver";
       
       ActualizaTabla();
    }
    
    ///Establece la consulta SQL para llenar la tabla
    public void setQueryTabla(String sql){
        queryTabla = sql;
    }
    
    ///Establece la consulta DML para ser ejecutar en la base de datos
    public void setSQL(String sql){
        this.sql = sql;
    }
    
    public Statement getSentencia(){
        
        return(sentencia);
    }
    
    public DefaultTableModel getModelo(){
        
        return(modelo);
    }
    
    public String getUser(){
        return(user);
    }
    
    public String getPassword(){
        return (password);
    }
    
    public void setTabla(JTable t){
        tabla = t;
    }
    
    /**
     * Método encargado de hacer la conexion con la base de datos
     */
    public void  EstableceConexion(){
        
        try
        {
            //Se carga el controlador de postgresql para permitir la conexion
            Class.forName(driver);
            try
            {
                conexion = DriverManager.getConnection(url,user,password);//Creacion del objeto conexion
                sentencia = conexion.createStatement();//Se define un objeto de la clase Statement para permitir ejecutar las consultas SQL
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
     * Este método se encarga de hacer la ejecucion de las consultas de tipo
     * SQL y DML en la base de datos.
     */
    public void EjecutaSentencia(){
        
        try
        {
            EstableceConexion();
            sentencia.executeUpdate(sql);
            cierraConexion();
            ActualizaTabla();
        }catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    /**
     * Método encargado de actulizar el contenido de alguna tabla perteneciente
     * en un formulario.
     */
    public void ActualizaTabla(){
  
    int nCol;
    
    try
    {
        //Se establece la conexion con la base de datos
         EstableceConexion();
         
         modelo = new DefaultTableModel();
         tabla.setModel(modelo);

         rs = sentencia.executeQuery(queryTabla);//Se ejcuta la consulta
         rsMd = rs.getMetaData();
         nCol = rsMd.getColumnCount();

         //Se inicializa el encabezado de la tabla
         for(int i=1; i <= nCol; i++) {
            modelo.addColumn(rsMd.getColumnLabel(i));
         }

         //Con este ciclo se agregan los registros a la tabla
         while(rs.next())
         {
            Object [] tupla = new Object[nCol];

            for(int i =0; i < nCol; i++) {
                 tupla[i] = rs.getObject(i+1);
             }

            modelo.addRow(tupla);
         }

         cierraConexion();

    }catch(Exception e)
    {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
   }
   
    /**
     * Se cierra la conexion una vez finalizando alguna ejecución en la base de 
     * datos.
     */
    public void cierraConexion() throws SQLException{
       
        if(sentencia != null )
        {
            sentencia.close();
        }
        if(conexion != null)
        {
            conexion.close();
        }
    }
}

