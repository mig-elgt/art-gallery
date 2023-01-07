
package galeriadearte;

import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.freixas.jcalendar.JCalendarCombo;

/**
 * Clase utilizada para llevar a cabo las ventas en la galeía
 */

public class FVentas extends javax.swing.JFrame {
    
    private Connection conexion;
    private Statement sentencia;
    private DefaultTableModel modelo;
    private ResultSetMetaData rsMd;
    private ResultSet rs;
    private String driver;
    private String url;
    private String sql;
    private int[][] ID;
    private float TotalVenta;
    private int bandera;
    private long ID_Venta;
    private String user;
    private String password;
    private ReporteVenta reporteVenta;
    
    public FVentas(){
        
    }
    
    /**
     * @param u Nombre del usuario
     * @param p Contraseña del usuario
     */
    public FVentas(String u, String p) {
        initComponents();
        
        this.user = u;
        this.password = p;
        this.url = "jdbc:postgresql://localhost:5432/GaleriaDeArte";
        this.driver = "org.postgresql.Driver";
        this.TotalVenta = 0;
        this.bandera = 0;
        this.ID_Venta = 1;
        
        //Reservar de espacio para los Id de Exposición, Clientes y Vendedores
        ID = new int[3][]; //Fila 0=Exposiciones, 1=Clientes y 2=Vendedores
        for(int i = 0; i < 3; i++){
            ID[i] = new int[200];
        }
        
        //Cargar los ComboBox
        IniComboBox(this.cbExposicion,0,"SELECT E.Id_Exposicion, E.Titulo FROM EXPOSICIONES.Exposicion E");
        IniComboBox(this.cbClientes,1,"SELECT C.Id_Cliente, (Nombre || ' ' || apellido_paterno || ' ' || apellido_materno) as NombreCompleto  FROM VENTAS.Clientes C");
        IniComboBox(this.cbVendedores,2,"SELECT V.Id_Vendedor,(Nombre || ' ' || apellido_paterno || ' ' || apellido_materno) as NombreCompleto FROM VENTAS.Vendedores V WHERE V.Id_Vendedor IN ( SELECT C.Codigo_Vendedor FROM VENTAS.ComisionesPorExposicion C WHERE C.Codigo_Exposicion ="+ID[0][0]+")");
        this.bandera = 1;
        
        //Llenar tablas de Obras Disponibles
        IniTablaObrasDispo();
        IniFechaCreacion();
        InicializaNoVenta();
        setLocationRelativeTo(this);
    }
    
    /**
     * Este método es utilizado para controlar el codigo de la venta
     */
    public void InicializaNoVenta(){
    
        ResultSet r,r2;
        Object dato;
        try
        {
            EstableceConexion();
            r = sentencia.executeQuery("select Max(Codigo)+1 from VENTAS.Ventas");
            
            while(r.next())
            {
               dato = r.getObject(1);
               //En caso que el numero de venta es igual a cero, se inicializa la secuencia de la llave primari que pertenece a la venta
               if(dato == null)
               {
                   r2 = sentencia.executeQuery("SELECT setval('ventas.ventas_codigo_seq',1000,'t');");
                   ID_Venta = 1001;
                   break;
                }else
               {
                   this.ID_Venta = Integer.parseInt( String.valueOf(r.getObject(1)));
               }
            }
            
            etNoVenta.setText(String.valueOf(ID_Venta));
            sentencia.close();
            conexion.close();
        }catch(SQLException e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    private void IniFechaCreacion(){
        
        JCalendarCombo cal;
        Calendar  cl;
        int []date;
        
        date = new int[3];
        cal = new JCalendarCombo();
        
        cl = cal.getCalendar();
        date[0]=cl.get(5); //Dia
        date[1]=cl.get(2)+1; //Mes
        date[2]=cl.get(1); //Año
        
        this.etFechaVenta.setText(date[0]+"/"+date[1]+"/"+date[2]);
      }
      
    /**
     * Este método se encarga de ejecutar algunas de las sentencias SQL o DML.
     */ 
    public void EjecutaSentencia(){
        
        try
        {
            EstableceConexion();
            sentencia.executeUpdate(sql);
            sentencia.close();
            conexion.close();
        }catch(SQLException e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    /**
     * Este método es para inicializar algun control ComboBox con el nombre de las entidades derivadas
     * del formulario Persona. 
     * @param cb Control de tipo ComboBox puede ser (Exposicion,Clientes o Vendedores)
     * @param f  Indice entre(0-2) que indica la fila en la matriz, donde se guardara las llaves primarias de alguna de las entidades
     * @param query Consulta SQL asociada al ComboBox y a la entidad{.
     */
    public void IniComboBox(JComboBox cb, int f, String query){
     
        ResultSet rsAux;
        int cont = 0;
        
        try
        {
             EstableceConexion();
             rsAux = sentencia.executeQuery(query);
    
             while(rsAux.next())
             {
               cb.addItem(rsAux.getObject(2));
               this.ID[f][cont++] = Integer.parseInt( String.valueOf(rsAux.getObject(1)));
             }
             
             sentencia.close();
             conexion.close();
        }catch(Exception e)
        {
           JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    /**
     * Este método se encarga de hacer la conexion con la base. Para ello se carga el 
     * driver jdbc de postgresql.
     */
    public void  EstableceConexion(){
        
        try
        {
            Class.forName(driver);
            try
            {
                conexion = DriverManager.getConnection(url, this.user,this.password);
                sentencia = conexion.createStatement();
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
     * Este método se encarga de inicializar la tabla que contendra las obras disponibles.
     */
    public void IniTablaObrasDispo(){
  
        int nCol;
        
        try
        {
             EstableceConexion();//Se hace la conexion con la base de datos
             modelo = new DefaultTableModel();
             tablaObrasDisp.setModel(modelo);
             //Se define la consulta sql para el llenado de la tabla
             rs = sentencia.executeQuery("select O.Id_Obra, O.Titulo, O.Precio "
                                        + "from ARTISTAS.Obras O inner join Exposiciones.det_artistas_exposicion E on O.Id_Artista = E.Id_Artista "
                                        + "where E.Id_Exposicion = "+this.ID[0][this.cbExposicion.getSelectedIndex()]+" and O.Estado = 0;");
             rsMd = rs.getMetaData();
             nCol = rsMd.getColumnCount();
             
             //Se crean los encabezados de la tabal
             for(int i=1; i <= nCol; i++) {
                modelo.addColumn(rsMd.getColumnLabel(i));
             }
             
             while(rs.next())
             {
                Object [] tupla = new Object[nCol];

                for(int i =0; i < nCol; i++) {
                     tupla[i] = rs.getObject(i+1);
                 }
                modelo.addRow(tupla);
             }
             sentencia.close();
             conexion.close();
        }catch(Exception e)
        {
               JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaObras = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        etTotalV = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cbExposicion = new javax.swing.JComboBox();
        cbClientes = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cbVendedores = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        etNoVenta = new javax.swing.JLabel();
        etFechaVenta = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaObrasDisp = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Captura de Ventas");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Ventas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 36))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Lista de Obras", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14))); // NOI18N

        listaObras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Obra", "Nombre", "Precio", "SubTotal"
            }
        ));
        listaObras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaObrasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listaObras);

        jButton4.setText("Imprimir Venta");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        jButton5.setText("Cancelar");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Total : $");

        etTotalV.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        etTotalV.setForeground(new java.awt.Color(255, 0, 51));
        etTotalV.setText("0.00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addGap(82, 82, 82)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(etTotalV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jLabel1)
                    .addComponent(etTotalV))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(232, 231, 231));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Exposición:");

        cbExposicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbExposicionActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Cliente:");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Vendedor:");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("No Venta:");

        etNoVenta.setText("jLabel7");

        etFechaVenta.setText("17/03/2013");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Fecha:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(etNoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbExposicion, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(cbVendedores, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(etNoVenta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbExposicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etFechaVenta)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cbVendedores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Obras Disponibles", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14))); // NOI18N

        tablaObrasDisp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaObrasDisp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaObrasDispMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaObrasDisp);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(165, 165, 165))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        jButton3.setText("Salir");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaObrasDispMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaObrasDispMouseClicked
        
        DefaultTableModel mod, mod2;
        Object []pro;
        int j, obraSel;
        
        pro = new Object[4];
        obraSel = this.tablaObrasDisp.getSelectedRow();
        mod2 = (DefaultTableModel)this.tablaObrasDisp.getModel();
        
        for(j = 0; j < 3; j++){
            pro[j] = mod2.getValueAt(obraSel, j);
        }
        pro[j] = mod2.getValueAt(obraSel, 2);
        
        mod = (DefaultTableModel)this.listaObras.getModel();
        mod.addRow(pro);
        mod2.removeRow(obraSel);
        
        this.TotalVenta += Float.parseFloat(String.valueOf(pro[2]));
        this.etTotalV.setText(String.valueOf(TotalVenta));
    }//GEN-LAST:event_tablaObrasDispMouseClicked

    private void listaObrasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaObrasMouseClicked
        
        DefaultTableModel mod, mod2;
        Object []pro;
        int j, obraSel;
        
        obraSel = this.listaObras.getSelectedRow();
        mod2 = (DefaultTableModel)this.listaObras.getModel();
       
        pro = new Object[3];
        for(j = 0; j < 3; j++){
            pro[j] = mod2.getValueAt(obraSel, j);
        }
        
        mod = (DefaultTableModel)this.tablaObrasDisp.getModel();
        mod.addRow(pro);
        mod2.removeRow(obraSel); 
 
        this.TotalVenta -= Float.parseFloat(String.valueOf(pro[2]));
        this.etTotalV.setText(String.valueOf(TotalVenta));
 
    }//GEN-LAST:event_listaObrasMouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
       
        DefaultTableModel mod, mod2;
        Object []pro;
        int nRow;
        
        mod2 = (DefaultTableModel)this.listaObras.getModel();
        mod = (DefaultTableModel)this.tablaObrasDisp.getModel();
        nRow = mod2.getRowCount();
        
        while(nRow > 0)
            {
                pro = new Object[3];
                for(int j = 0; j < 3; j++){
                    pro[j] = mod2.getValueAt(nRow-1, j);
                }

                mod.addRow(pro);
                mod2.removeRow(nRow-1); 
                nRow--;
            }

            this.TotalVenta = 0;
            this.etTotalV.setText(String.valueOf(TotalVenta));
        
    }//GEN-LAST:event_jButton5MouseClicked

    private void cbExposicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbExposicionActionPerformed
        
        int expSel = this.cbExposicion.getSelectedIndex();
        
        if(bandera == 1)
        {
            this.cbVendedores.removeAllItems();
            String query = "SELECT V.Id_Vendedor,(Nombre || ' ' || apellido_paterno || ' ' || apellido_materno) as NombreCompleto  FROM VENTAS.Vendedores V WHERE V.Id_Vendedor IN ( SELECT C.Codigo_Vendedor FROM VENTAS.ComisionesPorExposicion C WHERE C.Codigo_Exposicion ="+ID[0][expSel]+")";
            IniComboBox(this.cbVendedores,2,query);
            IniTablaObrasDispo();
        }
    }//GEN-LAST:event_cbExposicionActionPerformed

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
       
         DefaultTableModel mod;
         long idV, idO;
         float subTotal;
         
         if(this.cbExposicion.getSelectedIndex() != -1)
         {
             if(this.cbClientes.getSelectedIndex() != -1)
             {
                 if(this.cbVendedores.getSelectedIndex() != -1)
                 {
                     if(this.listaObras.getModel().getRowCount() > 0)
                     {
                         //Insertar la Venta junto con las obras
                         sql = "INSERT INTO VENTAS.Ventas values(default,"
                                 + ID[0][this.cbExposicion.getSelectedIndex()]+","
                                 + ID[1][this.cbClientes.getSelectedIndex()]+","
                                 + ID[2][this.cbVendedores.getSelectedIndex()]+",'"
                                 + this.etFechaVenta.getText()+"',"
                                 + this.TotalVenta+")";
                        this.EjecutaSentencia();
                        this.etNoVenta.setText(String.valueOf(++this.ID_Venta));
                        
                        mod = (DefaultTableModel)this.listaObras.getModel();
                       
                        //Insertar el Detalle de Ventas
                        for(int i = 0; i < mod.getRowCount(); i++)
                        {
                                idV = (this.ID_Venta-1);
                                idO = Long.parseLong(String.valueOf(mod.getValueAt(i,0)));
                                subTotal = Float.parseFloat(String.valueOf(mod.getValueAt(i,2)));
                                
                                sql = "INSERT INTO VENTAS.DetalleVenta values("+idV+","+idO+","+subTotal+")";
                                
                                this.EjecutaSentencia();
                        }
                        
                        //Se limpia la lista de Obras
                        int nR = mod.getRowCount();
                        for(int w = 0; w < nR; w++)
                        {
                            mod.removeRow(0);
                        }
                        
                        this.reporteVenta = new ReporteVenta(this.user,this.password);
                        try {
                          this.reporteVenta.EjecutaReporte(this.ID_Venta-1,
                                                           (String)this.cbExposicion.getSelectedItem(),
                                                           (String)this.cbClientes.getSelectedItem(),
                                                            (String)this.cbVendedores.getSelectedItem(),
                                                            String.valueOf(TotalVenta));
                         } catch (SQLException ex) {
                             Logger.getLogger(FVentas.class.getName()).log(Level.SEVERE, null, ex);
                         }
                        
                        this.TotalVenta = 0;
                        this.etTotalV.setText(String.valueOf(TotalVenta));
                        
                     }else
                     {
                         JOptionPane.showMessageDialog(null, "Agregue una obra a la Lista de Obras","Ventas", JOptionPane.INFORMATION_MESSAGE);
                     }
                 
                 }else
                 {
                    //Seleccione a un Vendedor
                     JOptionPane.showMessageDialog(null, "Seleccione un Vendedor","Ventas", JOptionPane.INFORMATION_MESSAGE);
                 }
             }else
             {
                 //Seleccione una Cliente
                 JOptionPane.showMessageDialog(null, "Seleccione un Cliente","Ventas", JOptionPane.INFORMATION_MESSAGE);
             }
         }else
         {
             //Seleccione una Exposicion
             JOptionPane.showMessageDialog(null, "Seleccione una Exposición","Ventas", JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
       this.dispose();
    }//GEN-LAST:event_jButton3MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FVentas().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbClientes;
    private javax.swing.JComboBox cbExposicion;
    private javax.swing.JComboBox cbVendedores;
    private javax.swing.JLabel etFechaVenta;
    private javax.swing.JLabel etNoVenta;
    private javax.swing.JLabel etTotalV;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable listaObras;
    private javax.swing.JTable tablaObrasDisp;
    // End of variables declaration//GEN-END:variables
}
