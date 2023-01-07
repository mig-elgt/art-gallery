
package galeriadearte;
import java.awt.Toolkit;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 * @author Miguel Angel Galicia Torrez
 */
public class FArtistas extends FPersonas {

    public FArtistas(){
            super();
    }
    /**
     * @param u Nombre del usuario 
     * @param c Constraseña del usuario
     */
    public FArtistas(String u, String c) {
        
        //Llamado al cosntructor padre, para inicializar los atributos heredados
        super(u,c,"select * from ARTISTAS.Artistas");
        
        initComponents();
        
        //Método de la clase padre (Persona)
        ActualizaTabla(tablaA);///Se llena la tabla con los registros actuales del artista
        Limpia();
        setLocationRelativeTo(this);
    }
    
    private void Limpia(){
        
        this.etIdArtista.setText("#");
        this.tbNombreA.setText("");
        this.tbApellidosA.setText("");
        this.tbDireccionA.setText("");
        this.tbEmailA.setText("");
        this.tbPaisA.setText("");
        this.tbReseñaA.setText("");
        this.tbTelefonoA.setText("");
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        etIdArtista = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tbReseñaA = new javax.swing.JTextPane();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaA = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        tbNombreA = new javax.swing.JTextField();
        tbApellidosA = new javax.swing.JTextField();
        tbTelefonoA = new javax.swing.JTextField();
        tbDireccionA = new javax.swing.JTextField();
        tbPaisA = new javax.swing.JTextField();
        tbEmailA = new javax.swing.JTextField();
        Calendar = new org.freixas.jcalendar.JCalendarCombo();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ARTISTAS");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Id Artistas:");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Nombre:");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Apellidos:");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Fecha de Nacimiento:");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Teléfono:");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Dirección:");

        etIdArtista.setBackground(new java.awt.Color(255, 102, 204));
        etIdArtista.setText("1");

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setText("Pais:");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setText("Email:");

        tbReseñaA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbReseñaAKeyTyped(evt);
            }
        });
        jScrollPane9.setViewportView(tbReseñaA);

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setText("Reseña:");

        tablaA.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaA.setAutoscrolls(false);
        tablaA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaAMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaA);

        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tbNombreA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbNombreAKeyTyped(evt);
            }
        });

        tbApellidosA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbApellidosAKeyTyped(evt);
            }
        });

        tbTelefonoA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbTelefonoAKeyTyped(evt);
            }
        });

        tbPaisA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbPaisAKeyTyped(evt);
            }
        });

        jMenuBar1.setBackground(new java.awt.Color(153, 153, 153));
        jMenuBar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenuBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuBar1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jMenuBar1.setDoubleBuffered(true);
        jMenuBar1.setPreferredSize(new java.awt.Dimension(355, 60));

        jMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/galeriadearte/artista.png"))); // NOI18N
        jMenu1.setText("    Agregar   ");
        jMenu1.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu3.setForeground(new java.awt.Color(255, 255, 255));
        jMenu3.setText("    Eliminar   ");
        jMenu3.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        jMenu4.setForeground(new java.awt.Color(255, 255, 255));
        jMenu4.setText("    Modificar   ");
        jMenu4.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jMenu4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu4MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu4);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/galeriadearte/edit-clear.png"))); // NOI18N
        jMenu2.setText("Limpiar");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Calendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(etIdArtista, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(tbTelefonoA)
                                        .addComponent(tbApellidosA)
                                        .addComponent(tbDireccionA)
                                        .addComponent(tbNombreA, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tbEmailA)
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tbPaisA, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 22, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tbEmailA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tbPaisA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(etIdArtista))
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(tbNombreA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tbApellidosA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tbTelefonoA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tbDireccionA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(Calendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
      
    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        InsertArtista();
    }//GEN-LAST:event_jMenu1MouseClicked
    /**
     * Método para registrar un artista en la base de datos.
     */
    public void InsertArtista(){
        int dia, mes, anio;
        String fecha;
     
        dia  = Calendar.getCalendar().get(5);
        mes  = Calendar.getCalendar().get(2)+1;
        anio =Calendar.getCalendar().get(1);
        
        fecha = dia+"/"+mes+"/"+anio;
        
        //Se hacen las validaciones previas a la insercion
        if(this.camposVacios() == 0)
        {
            if(anio <= 2000)
            {
                //Consulta DML para almacenar un registro del Artista
                sql = " INSERT INTO ARTISTAS.Artistas VALUES (default,'"
                        + this.tbNombreA.getText() + "','"
                        + this.tbApellidosA.getText()+ "','"
                        + fecha+"','"
                        + this.tbDireccionA.getText()+"','"
                        + this.tbTelefonoA.getText()+"','"
                        + this.tbPaisA.getText()+"','"
                        + this.tbEmailA.getText()+"','"
                        + this.tbReseñaA.getText()+"');";

                EjecutaSentencia(tablaA);
            }
            else
            {
                 JOptionPane.showMessageDialog(null, "La fecha de nacimiento debe ser menor al año 2000","Inserción", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "No puede haber campos vacios","Inserción", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int camposVacios(){
 
        int res = 0;
        
        if(this.tbApellidosA.getText().trim().length() == 0 ||
           this.tbDireccionA.getText().trim().length() == 0 ||
           this.tbEmailA.getText().trim().length() == 0 ||
           this.tbNombreA.getText().trim().length() == 0 ||
           this.tbPaisA.getText().trim().length() == 0 ||
           this.tbReseñaA.getText().trim().length() == 0 ||
           this.tbTelefonoA.getText().trim().length() == 0 )
            res = 1;
        
        return(res);
    }
    
    private void tablaAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaAMouseClicked
      
        String fecha;
        int tuplaSel = tablaA.getSelectedRow();

        this.etIdArtista.setText( String.valueOf(modelo.getValueAt(tuplaSel, 0)));
        this.tbNombreA.setText((String)modelo.getValueAt(tuplaSel, 1));
        this.tbApellidosA.setText((String)modelo.getValueAt(tuplaSel, 2));
        fecha = String.valueOf(modelo.getValueAt(tuplaSel, 3));
        this.Calendar.setDate(Date.valueOf(fecha));
        this.tbDireccionA.setText((String)modelo.getValueAt(tuplaSel, 4));
        this.tbTelefonoA.setText((String)modelo.getValueAt(tuplaSel, 5));
        this.tbPaisA.setText((String)modelo.getValueAt(tuplaSel, 6));
        this.tbEmailA.setText((String)modelo.getValueAt(tuplaSel, 7));
        this.tbReseñaA.setText((String)modelo.getValueAt(tuplaSel, 8));
       
    }//GEN-LAST:event_tablaAMouseClicked

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
       DeleteArtista();
    }//GEN-LAST:event_jMenu3MouseClicked
    
    /**
     * Método para dar de baja un Artista de la base de datos
     */
    public void DeleteArtista(){
        int opc;
        
        if(tablaA.getSelectedRow() >= 0)
        {
            opc = JOptionPane.showConfirmDialog(null, "¿Esta seguro que quiere eliminar el registro?", "Eliminación", JOptionPane.YES_NO_OPTION);
            if(opc == 0)
            {
                //Consulta DML para la eleminacion de un Artista
                sql = "DELETE FROM ARTISTAS.Artistas WHERE Id_Artista = "+this.etIdArtista.getText();
                EjecutaSentencia(tablaA);
            }
        }else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro","Eliminación", JOptionPane.ERROR_MESSAGE);
        } 
    }
    
    private void jMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu4MouseClicked
        UpdateArtista();
    }//GEN-LAST:event_jMenu4MouseClicked
    
    /**
     * Método para actualizar los datos de un Artista
     */
    public void UpdateArtista(){
        int dia, mes, anio;
        String fecha;
     
        dia  = Calendar.getCalendar().get(5);
        mes  = Calendar.getCalendar().get(2)+1;
        anio =Calendar.getCalendar().get(1);
        
        fecha = dia+"/"+mes+"/"+anio;
        
        if(this.camposVacios() == 0)
        {
            //Consulta DML para modificar los datos del algun Artistas
            sql = "UPDATE ARTISTAS.Artistas SET Id_Artista="
                    + this.etIdArtista.getText()+",Nombre='"
                    + this.tbNombreA.getText() + "',Apellidos='"
                    + this.tbApellidosA.getText()+ "',Fecha_Nacimiento='"
                    + fecha+"',Direccion='"
                    + this.tbDireccionA.getText()+"',Telefono='"
                    + this.tbTelefonoA.getText()+"',Pais='"
                    + this.tbPaisA.getText()+"',Email='"
                    + this.tbEmailA.getText()+"',Resena='"
                    + this.tbReseñaA.getText()+"'"
                    + "WHERE Id_Artista ="+this.etIdArtista.getText();

            EjecutaSentencia(tablaA); ///Se ejecuta la sentencias DML
        }else
        {
            JOptionPane.showMessageDialog(null, "No puede haber campos vacios","Inserción", JOptionPane.ERROR_MESSAGE);
            tbReseñaA.setFocusTraversalKeysEnabled(true);
        }
    }
    
    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
       this.Limpia();
    }//GEN-LAST:event_jMenu2MouseClicked

    private void tbTelefonoAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbTelefonoAKeyTyped
        
        if((tbTelefonoA.getText().length() >= 8) || (!Character.isDigit(evt.getKeyChar()) && !Character.isISOControl(evt.getKeyChar())))
        {
            Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_tbTelefonoAKeyTyped

    private void tbNombreAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbNombreAKeyTyped
       
        validaCadena(evt);
    }//GEN-LAST:event_tbNombreAKeyTyped

    private void tbApellidosAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbApellidosAKeyTyped
        validaCadena(evt);
    }//GEN-LAST:event_tbApellidosAKeyTyped

    private void tbPaisAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPaisAKeyTyped
        validaCadena(evt);
    }//GEN-LAST:event_tbPaisAKeyTyped

    private void tbReseñaAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbReseñaAKeyTyped
        validaCadena(evt);
    }//GEN-LAST:event_tbReseñaAKeyTyped
    
    private void validaCadena(java.awt.event.KeyEvent evt){
        
        if(!Character.isSpaceChar(evt.getKeyChar()) && !Character.isISOControl(evt.getKeyChar()) &&!Character.isLetter(evt.getKeyChar()))
        { 
            Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
    }
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
            java.util.logging.Logger.getLogger(FArtistas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FArtistas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FArtistas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FArtistas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FArtistas().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.freixas.jcalendar.JCalendarCombo Calendar;
    private javax.swing.JLabel etIdArtista;
    private javax.swing.JButton jButton1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable tablaA;
    private javax.swing.JTextField tbApellidosA;
    private javax.swing.JTextField tbDireccionA;
    private javax.swing.JTextField tbEmailA;
    private javax.swing.JTextField tbNombreA;
    private javax.swing.JTextField tbPaisA;
    private javax.swing.JTextPane tbReseñaA;
    private javax.swing.JTextField tbTelefonoA;
    // End of variables declaration//GEN-END:variables
}
