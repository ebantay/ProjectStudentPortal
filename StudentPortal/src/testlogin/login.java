
package testlogin;

import com.sun.corba.se.spi.orb.OperationFactory;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Random;
import javax.swing.*;
import jdk.nashorn.internal.runtime.JSType;
import net.proteanit.sql.DbUtils;


////////////////////////////////

/**
 *
 * @author Earl
 */
public class login extends javax.swing.JFrame {

    /**
     *
     */
    public Statement st;
public int xtry =1,

    /**
     *
     */
    ytry=4,

    /**
     *
     */
    trig,

    /**
     *
     */
    x1,

    /**
     *
     */
    ctr;
Connection conn=null;
PreparedStatement pst =null;
ResultSet rss =null,rs=null;
String pass,pass2;


 ////////////////////////////////////////////////////////////////

    /**
     *
     */
        public login() {
        
         Color color = new Color(0,153,51);  //bgcolor 
        getContentPane().setBackground(color); 
        initComponents();
         conn=mysqlconnect.Connectdb();    //class connection 
                            
         
         ////enter keydown code for login
         pass1.addKeyListener(new KeyAdapter(){       //pass
             @Override
             public void keyPressed(KeyEvent e){
                 if (e.getKeyCode() == KeyEvent.VK_ENTER){
                     loginkeydown();
                 }
             }
              });
         
         user1.addKeyListener(new KeyAdapter(){     //user
             @Override
             public void keyPressed(KeyEvent e){
                 if (e.getKeyCode() == KeyEvent.VK_ENTER){
                     loginkeydown();
                 }               
             }
              });
         
        field_search.addKeyListener(new KeyAdapter(){     //search
            @Override
             public void keyPressed(KeyEvent e){
                 if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    searchkeydown();
                 }               
             }
              });
         
    }
    
 ////////////////////////////////////////////////////////////////
    private void UpdateJTable(){
        String query = "select username, fname, mname, lname, course, address, contact, nname from tablelogin";
    try{
        pst =conn.prepareStatement(query);
        rss=pst.executeQuery();
        table_records.setModel(DbUtils.resultSetToTableModel(rss));
        
    }
    catch (Exception e){
        JOptionPane.showMessageDialog(null, e);
    }
    }
     
////////////////////////////////////////////////////////////////
    private void clear(){
        field_address.setText("");              //clear fields     
        field_fname.setText("");
        field_lname.setText("");
        field_mname.setText("");
        field_number.setText("");
        field_user.setText("");
        passfield_confirm.setText("");
        passfield_pass.setText("");
        field_bal.setText("");
    }
////////////////////////////////////////////////////////////////
    private void error(){
        if (xtry<=4){                       //condition for number of tries
                    lbl_errorlog.setText("INVALID USERNAME OR PASSWORD!\n(Only "+ytry+" tries left)");
       
                    xtry++;                     //increment & decrement for tries
                    ytry--;
                }
                else {                          //dialog exit
                    JOptionPane.showMessageDialog(null, "Maximum number of tries exceeded. Program will exit..");
                    System.exit(0);
                }  
    }
///////////////////////////////////////////////////////////////

    /**
     *
     * @param rss
     */
     public void editdisplay (ResultSet rss){       ///displays info
      try {
 rss.next();
 
int count = rss.getInt( 1 );

 if ( count != 0 ) {
 field_id.setText( rss.getString( 1 ));
 field_user.setText( rss.getString( 2 ));
 passfield_pass.setText(rss.getString( 3 ));
 passfield_confirm.setText(rss.getString( 3 ));
 field_fname.setText(rss.getString( 4 ));
 field_mname.setText(rss.getString( 5 ));
 field_lname.setText(rss.getString( 6 ));
 field_address.setText(rss.getString( 8 ));
 field_number.setText(rss.getString( 9 ));
 combo_course.addItem(rss.getString(7));                //restore the slected item of combobox from sql
 combo_course.setSelectedItem(rss.getString(7));    //
 field_bal.setText(rss.getString( 10 ));
 field_nname.setText(rss.getString(12));
            
 
 JOptionPane.showMessageDialog(null,("RECORD FOUND!"));
    
 }

 }
 catch ( Exception e){
            clear();
          JOptionPane.showMessageDialog(null,("NO RECORD FOUND!"));
          panel_create.hide();
          panel_update.show();
          clear();
         }
 }
 /////////////////////////////////////////////////////////
 private void logout(){
        int choice= JOptionPane.showConfirmDialog(null, "ARE YOU SURE?");
     if (choice==JOptionPane.YES_OPTION){
         JOptionPane.showMessageDialog(null,"LOGGED OUT!");
         panel_adminmain.hide();
         panel_studmain.hide();
         panel_choice.show();       ////logout code condition
         pass1.setText("");
         user1.setText("");
         lbl_errorlog.setText("");
         lbl_errorlog.setForeground(Color.red);
     }
 }
 //////////////////////////////////////////////////////
 private void exit(){
        int choice= JOptionPane.showConfirmDialog(null, "ARE YOU SURE?");
     if (choice==JOptionPane.YES_OPTION){
         JOptionPane.showMessageDialog(null,"EXIT!");
         System.exit(0);
     }
     else{
         opt_close.enable(true);opt_close.setVisible(true);
     }
 }
 //////////////////////////////////////////////////////
 private void jcombobox(){
        //CONDITION FOR TUITION FEES
     
      lbl_combo.setText(combo_course.getSelectedItem().toString().toUpperCase()); 
           if ((lbl_combo.getText().equals("BSCS"))||(lbl_combo.getText().equals("BSINFOTECH"))){
               field_bal.setText("10000");
          }
            else if((lbl_combo.getText().equals("BSBM"))||(lbl_combo.getText().equals("BSHRM"))||(lbl_combo.getText().equals("BSE"))){
               field_bal.setText("8000");
          }
           else if((lbl_combo.getText().equals("ACT"))||(lbl_combo.getText().equals("DHRM"))||(lbl_combo.getText().equals("DMT"))){
                field_bal.setText("6000");
            }
          else if((lbl_combo.getText().equals("BSEE"))||(lbl_combo.getText().equals("BSCPE"))||(lbl_combo.getText().equals("BTTE"))||(lbl_combo.getText().equals("AT"))){
                field_bal.setText("7000");
          }
          else if(lbl_combo.getText().equals("<SELECT>")){
                field_bal.setText("0");
            
            }
}
////////////////login code

    /**
     *
     */
     public void loginkeydown(){
       if (trig==1){
          try{
          String query="select *from tableadmin where username = ? and password = ?";
          pst = conn.prepareStatement(query);
             pst.setString(1,user1.getText().toUpperCase());      //gets user text from textbox
             pst.setString(2,pass1.getText().toUpperCase());       //gets pass text from textbox
             rss = pst.executeQuery(); 
             if (rss.next()){                            //query will test if username & password == user1 & user2
            if (trig==1){                  //triggers the condition if user or admin
                lbl_errorlog.setText("                              CONNECTED!");
                lbl_errorlog.setForeground(Color.green);
                lbl_errorlog.setAlignmentY(50);
            
            JOptionPane.showMessageDialog(null, "YOU ARE LOGGED IN AS AN ADMIN!");
             panel_login.hide();
             panel_adminmain.show();
            
            }
             }
            else{
                error();        
             }       
          }
          catch(Exception e){
              JOptionPane.showMessageDialog(null, e);
          } 
      }
  /////////////////////    login stud
      else{
            String query = "select *from tablelogin where username = ? and password = ?";      //check Password and Username
       try{
             pst = conn.prepareStatement(query);
             pst.setString(1,user1.getText().toUpperCase());      //gets user text from textbox
             pst.setString(2,pass1.getText().toUpperCase());       //gets pass text from textbox
             rss = pst.executeQuery();               //send to query
             
        if (rss.next()){                            //query will test if username & password == user1 & user2
            if (trig==0){                  //triggers the condition if user or admin
                lbl_errorlog.setText("                              CONNECTED!");
                lbl_errorlog.setForeground(Color.green);
                lbl_errorlog.setAlignmentY(50);
            JOptionPane.showMessageDialog(null, "YOU ARE LOGGED IN AS A STUDENT!");
            panel_login.hide();
            tab_stud.show();
            panel_studprofile.hide();
             panel_studmain.show();
             lbl_studno.setText(user1.getText());
             
             bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_DEIT.png")));
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_DEIT.png")));
        opt_back.setVisible(false);opt_back.enable(false);
        opt_next.setVisible(true);opt_next.enable(true);
        plusnext=0;
             ret_close_studmain();
            }
        }
            else{
                error();   
             }                   
      }
        
       catch(Exception e){ 
           JOptionPane.showMessageDialog(null, e);
         
        }    
      }
 }
 ////////////search code

    /**
     *
     */
    public void searchkeydown(){
       //////SEARCH 
        btn_payment.show();
     lbl_combo.hide();
     btn_removeaccnt.show();
     
        try {     
                
                 
                if ( !field_search.getText().equals(" "))  {
                    String query = "SELECT * FROM tablelogin " +
                "WHERE username = '" +field_search.getText().toUpperCase() + "'"; 
                   pst = conn.prepareStatement(query);
                  rss = pst.executeQuery();
                        
                            panel_update.hide();
                             panel_create.show();
                             btn_createconfirm.hide();
                            btn_createreset.hide();
                            btn_editok.show();
                            editdisplay(rss);
                            btn_search.show();
                            field_search.show();
                            lbl_nameoutput.show();
                            lbl_searchoutput.show();
                            btn_removeaccnt.show();
                            jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_update_new.png")));
                            
                }      
                    else{
                            JOptionPane.showMessageDialog(null,"FILL THE EMPTY FIELD");
                            
                            }  
        }
                catch(Exception e){
                      JOptionPane.showMessageDialog(null,e);                 
                        }
}

    /**
     *
     */
    public void ret_close_studmain()
{if(studplus==0){
    opt_close_log.setVisible(false);
     opt_close.enable(false);
     
     opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_log_light.png")));
        opt_label_studmain.setIcon(null);
        
        opt_btn_about_studmain.enable(true);opt_btn_credits_studmain.enable(true);opt_exit_studmain.enable(true);
        opt_btn_about_studmain.setVisible(true);opt_btn_credits_studmain.setVisible(true);opt_exit_studmain.setVisible(true);
        
        jLabel24.setLocation(0,0);
        opt_btn3_studmain.setLocation(980,200);
        opt_btn_about_studmain.setLocation(980,260);
        opt_btn_credits_studmain.setLocation(980,320);
        opt_exit_studmain.setLocation(980,380);
        opt_label_studmain.setLocation(980,260);
        
        jLayeredPane2.setLocation(20,180);
        btn_studhome.setLocation(40,120);btn_studprofile.setLocation(200,120);
        btn_studgrade.setLocation(580,120);btn_studbal.setLocation(750,120);
        
        opt_close_studmain.enable(false);opt_close_studmain.setVisible(false);opt_close_studmain.setLocation(885,10);
        opt_open_studmain.enable(true);opt_open_studmain.setVisible(true);opt_open_studmain.setLocation(885,70);
        }
        else{
        opt_close_log.setVisible(false);
     opt_close.enable(false);
     
     opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_log_light.png")));
        opt_label_studmain.setIcon(null);
        
        opt_btn_about_studmain.enable(true);opt_btn_credits_studmain.enable(true);opt_exit_studmain.enable(true);
        opt_btn_about_studmain.setVisible(true);opt_btn_credits_studmain.setVisible(true);opt_exit_studmain.setVisible(true);
        
        jLabel24.setLocation(0,0);
        opt_btn3_studmain.setLocation(980,200);
        opt_btn_about_studmain.setLocation(980,260);
        opt_btn_credits_studmain.setLocation(980,320);
        opt_exit_studmain.setLocation(980,380);
        opt_label_studmain.setLocation(980,260);
        
        jLayeredPane2.setLocation(20,180);
        btn_studhome.setLocation(260,90);btn_studprofile.setLocation(420,90);
        btn_studgrade.setLocation(580,90);btn_studbal.setLocation(750,90);
        
        opt_close_studmain.enable(false);opt_close_studmain.setVisible(false);opt_close_studmain.setLocation(885,10);
        opt_open_studmain.enable(true);opt_open_studmain.setVisible(true);opt_open_studmain.setLocation(885,70);
        };
}

    /**
     *
     */
    public void ret_open_studmain()
{   
    if(studplus==0){
        opt_btn3_studmain.enable(true);
        opt_btn3_studmain.setVisible(true);
        
        jLabel24.setLocation(-281,0);
        opt_btn3_studmain.setLocation(699,200);
        opt_btn_about_studmain.setLocation(699,260);
        opt_btn_credits_studmain.setLocation(699,320);
        opt_exit_studmain.setLocation(699,380);
        opt_label_studmain.setLocation(699,260);
        
        jLayeredPane2.setLocation(-261,180);
        btn_studhome.setLocation(-241,120);btn_studprofile.setLocation(-81,120);
        btn_studgrade.setLocation(299,120);btn_studbal.setLocation(469,120);
        
        opt_open_studmain.enable(false);
        opt_open_studmain.setVisible(false);
        opt_open_studmain.setLocation(885,10);
        
        opt_close_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_light.png")));
        opt_close_studmain.enable(true);
        opt_close_studmain.setVisible(true);
        opt_close_studmain.setLocation(604,70);
        }
        else{
        opt_btn3_studmain.enable(true);
        opt_btn3_studmain.setVisible(true);
        
        jLabel24.setLocation(-281,0);
        opt_btn3_studmain.setLocation(699,200);
        opt_btn_about_studmain.setLocation(699,260);
        opt_btn_credits_studmain.setLocation(699,320);
        opt_exit_studmain.setLocation(699,380);
        opt_label_studmain.setLocation(699,260);
        
        jLayeredPane2.setLocation(-261,180);
        btn_studhome.setLocation(-21,90);btn_studprofile.setLocation(139,90);
        btn_studgrade.setLocation(299,90);btn_studbal.setLocation(469,90);
        
        opt_open_studmain.enable(false);
        opt_open_studmain.setVisible(false);
        opt_open_studmain.setLocation(885,10);
        
        opt_close_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_light.png")));
        opt_close_studmain.enable(true);
        opt_close_studmain.setVisible(true);
        opt_close_studmain.setLocation(604,70);
        };
}
    
    /**
     *
     */
    public void ret_main()
        {opt_btn3.setIcon(null);opt_label.setIcon(null);
        
        opt_btn_about.enable(true);opt_btn_credits.enable(true);opt_exit.enable(true);
        opt_btn_about.setVisible(true);opt_btn_credits.setVisible(true);opt_exit.setVisible(true);
        
        jLabel21.setLocation(0,0);
        opt_btn3.setLocation(980,200);
        opt_btn_about.setLocation(980,260);
        opt_btn_credits.setLocation(980,320);
        opt_exit.setLocation(980,380);
        
        btn_admin.setLocation(70,260);btn_student.setLocation(70,370);
        
        opt_close.enable(false);opt_close.setVisible(false);opt_close.setLocation(885,350);
        opt_open.enable(true);opt_open.setVisible(true);opt_open.setLocation(885,400);
        }

    /**
     *
     */
    public void ret_login()
{opt_btn3_log.setIcon(null);opt_label_log.setIcon(null);
        
        opt_btn_about_log.enable(true);opt_btn_credits_log.enable(true);opt_exit_log.enable(true);
        opt_btn_about_log.setVisible(true);opt_btn_credits_log.setVisible(true);opt_exit_log.setVisible(true);
        
        jLabel4.setLocation(0,0);
        opt_btn3_log.setLocation(980,200);
        opt_btn_about_log.setLocation(980,260);
        opt_btn_credits_log.setLocation(980,320);
        opt_exit_log.setLocation(980,380);
        opt_label_log.setLocation(9800,260);
        
        jLabel2.setLocation(430,237);jLabel3.setLocation(430,317);
        jLabel22.setLocation(470,260);jLabel23.setLocation(470,340);
        user1.setLocation(480,270);pass1.setLocation(480,350);lbl_errorlog.setLocation(490,390);
        btn_backhome.setLocation(410,440);submitbtn.setLocation(610,440);pic_rnd.setLocation(10,130);
        
        opt_close_log.enable(false);opt_close_log.setVisible(false);opt_close_log.setLocation(885,10);
        opt_open_log.enable(true);opt_open_log.setVisible(true);opt_open_log.setLocation(885,70);
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        panel_choice = new javax.swing.JPanel();
        btn_admin = new javax.swing.JLabel();
        btn_student = new javax.swing.JLabel();
        opt_open = new javax.swing.JLabel();
        opt_close = new javax.swing.JLabel();
        opt_btn_about = new javax.swing.JLabel();
        opt_btn_credits = new javax.swing.JLabel();
        opt_exit = new javax.swing.JLabel();
        opt_btn3 = new javax.swing.JLabel();
        opt_label = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        panel_login = new javax.swing.JPanel();
        lbl_errorlog = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        user1 = new javax.swing.JTextField();
        pass1 = new javax.swing.JPasswordField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        submitbtn = new javax.swing.JLabel();
        pic_rnd = new javax.swing.JLabel();
        opt_btn_about_log = new javax.swing.JLabel();
        opt_btn_credits_log = new javax.swing.JLabel();
        opt_exit_log = new javax.swing.JLabel();
        opt_btn3_log = new javax.swing.JLabel();
        opt_label_log = new javax.swing.JLabel();
        opt_open_log = new javax.swing.JLabel();
        opt_close_log = new javax.swing.JLabel();
        btn_backhome = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panel_adminmain = new javax.swing.JPanel();
        btn_reglist = new javax.swing.JLabel();
        btn_create = new javax.swing.JLabel();
        btn_update = new javax.swing.JLabel();
        btn_logout = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        panel_create = new javax.swing.JPanel();
        field_mname = new javax.swing.JTextField();
        btn_upload = new javax.swing.JButton();
        field_lname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        field_address = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        field_number = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btn_createreset = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        btn_createconfirm = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        field_user = new javax.swing.JTextField();
        passfield_pass = new javax.swing.JPasswordField();
        field_fname = new javax.swing.JTextField();
        passfield_confirm = new javax.swing.JPasswordField();
        lbl_passcheck = new javax.swing.JLabel();
        combo_course = new javax.swing.JComboBox();
        btn_editok = new javax.swing.JButton();
        lbl_combo = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        img_pic = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        field_id = new javax.swing.JTextField();
        btn_payment = new javax.swing.JButton();
        field_bal = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btn_removeaccnt = new javax.swing.JButton();
        btn_back = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        field_nname = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        panel_records = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_records = new javax.swing.JTable();
        btn_back1 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        panel_update = new javax.swing.JPanel();
        field_search = new javax.swing.JTextField();
        lbl_searchoutput = new javax.swing.JLabel();
        lbl_nameoutput = new javax.swing.JLabel();
        btn_back2 = new javax.swing.JLabel();
        btn_search = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        panel_studmain = new javax.swing.JPanel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        tab_stud = new javax.swing.JTabbedPane();
        tab_panel2 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        tab_panel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabel27 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        tab_panel4 = new javax.swing.JPanel();
        bgd_faculty_names = new javax.swing.JLabel();
        opt_back = new javax.swing.JLabel();
        opt_next = new javax.swing.JLabel();
        lbl_faculty_title = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        tab_panel1 = new javax.swing.JPanel();
        lbl_total = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        panel_studgrade = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        panel_studprofile = new javax.swing.JPanel();
        lbl_studfname = new javax.swing.JLabel();
        lbl_studmname = new javax.swing.JLabel();
        lbl_studlname = new javax.swing.JLabel();
        lbl_studcourse = new javax.swing.JLabel();
        lbl_studnumber = new javax.swing.JLabel();
        lbl_studaddress = new javax.swing.JLabel();
        lbl_studdept = new javax.swing.JLabel();
        lbl_studno = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        panel_studbal = new javax.swing.JPanel();
        lbl_balcourse = new javax.swing.JLabel();
        lbl_ballpayfull = new javax.swing.JLabel();
        lbl_balmidterm1 = new javax.swing.JLabel();
        lbl_baldown1 = new javax.swing.JLabel();
        lbl_balfinals1 = new javax.swing.JLabel();
        lbl_balance = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        btn_studbal = new javax.swing.JLabel();
        btn_studgrade = new javax.swing.JLabel();
        btn_studprofile = new javax.swing.JLabel();
        btn_studhome = new javax.swing.JLabel();
        opt_open_studmain = new javax.swing.JLabel();
        opt_close_studmain = new javax.swing.JLabel();
        opt_btn_about_studmain = new javax.swing.JLabel();
        opt_btn_credits_studmain = new javax.swing.JLabel();
        opt_exit_studmain = new javax.swing.JLabel();
        opt_btn3_studmain = new javax.swing.JLabel();
        opt_label_studmain = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        panel_payment = new javax.swing.JPanel();
        lbl_currentbalance = new javax.swing.JLabel();
        field_paymententer = new javax.swing.JTextField();
        btn_back3 = new javax.swing.JLabel();
        btn_paymententer = new javax.swing.JLabel();
        jLabel = new javax.swing.JLabel();
        panel_admingrade = new javax.swing.JPanel();
        btn_backgrade = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        panel_remove = new javax.swing.JPanel();
        field_remove = new javax.swing.JTextField();
        btn_okremove = new javax.swing.JLabel();
        btn_remback = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();

        jFileChooser1.setCurrentDirectory(new java.io.File("C:\\Users\\a\\Pictures"));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(350, 100));
        setName("Login"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLayeredPane1.setPreferredSize(new java.awt.Dimension(950, 540));

        panel_choice.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        panel_choice.setPreferredSize(new java.awt.Dimension(950, 540));
        panel_choice.setRequestFocusEnabled(false);
        panel_choice.setLayout(null);

        btn_admin.setFont(new java.awt.Font("AR JULIAN", 1, 24)); // NOI18N
        btn_admin.setForeground(new java.awt.Color(255, 255, 255));
        btn_admin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_admin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_light.png"))); // NOI18N
        btn_admin.setText("LOGIN AS ADMIN");
        btn_admin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_admin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_adminMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_adminMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_adminMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_adminMousePressed(evt);
            }
        });
        panel_choice.add(btn_admin);
        btn_admin.setBounds(70, 260, 480, 102);

        btn_student.setFont(new java.awt.Font("AR JULIAN", 1, 24)); // NOI18N
        btn_student.setForeground(new java.awt.Color(255, 255, 255));
        btn_student.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_student.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_light.png"))); // NOI18N
        btn_student.setText("LOGIN AS STUDENT");
        btn_student.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_student.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_studentMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_studentMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_studentMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_studentMousePressed(evt);
            }
        });
        panel_choice.add(btn_student);
        btn_student.setBounds(70, 370, 480, 102);

        opt_open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_light.png"))); // NOI18N
        opt_open.setToolTipText("");
        opt_open.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_openMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_openMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_openMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_openMousePressed(evt);
            }
        });
        panel_choice.add(opt_open);
        opt_open.setBounds(885, 400, 60, 50);

        opt_close.setToolTipText("");
        opt_close.setEnabled(false);
        opt_close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_closeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_closeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_closeMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_closeMousePressed(evt);
            }
        });
        panel_choice.add(opt_close);
        opt_close.setBounds(885, 330, 60, 50);

        opt_btn_about.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light.png"))); // NOI18N
        opt_btn_about.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_btn_aboutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_btn_aboutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_btn_aboutMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_btn_aboutMousePressed(evt);
            }
        });
        panel_choice.add(opt_btn_about);
        opt_btn_about.setBounds(980, 260, 220, 40);

        opt_btn_credits.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light.png"))); // NOI18N
        opt_btn_credits.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_btn_creditsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_btn_creditsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_btn_creditsMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_btn_creditsMousePressed(evt);
            }
        });
        panel_choice.add(opt_btn_credits);
        opt_btn_credits.setBounds(980, 320, 220, 40);

        opt_exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_light.png"))); // NOI18N
        opt_exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_exitMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_exitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_exitMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_exitMousePressed(evt);
            }
        });
        panel_choice.add(opt_exit);
        opt_exit.setBounds(980, 380, 220, 40);

        opt_btn3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_btn3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_btn3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_btn3MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_btn3MousePressed(evt);
            }
        });
        panel_choice.add(opt_btn3);
        opt_btn3.setBounds(980, 200, 220, 40);
        panel_choice.add(opt_label);
        opt_label.setBounds(980, 260, 220, 250);

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_main_long.png"))); // NOI18N
        jLabel21.setText("jLabel21");
        jLabel21.setPreferredSize(new java.awt.Dimension(1225, 540));
        panel_choice.add(jLabel21);
        jLabel21.setBounds(0, 0, 1230, 540);

        panel_login.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_login.setPreferredSize(new java.awt.Dimension(950, 540));
        panel_login.setLayout(null);

        lbl_errorlog.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_errorlog.setForeground(new java.awt.Color(204, 51, 0));
        panel_login.add(lbl_errorlog);
        lbl_errorlog.setBounds(490, 390, 390, 20);

        jLabel2.setFont(new java.awt.Font("AR DESTINE", 0, 18)); // NOI18N
        jLabel2.setText("STUDENT NUMBER:");
        panel_login.add(jLabel2);
        jLabel2.setBounds(430, 237, 170, 20);

        jLabel3.setFont(new java.awt.Font("AR DESTINE", 0, 18)); // NOI18N
        jLabel3.setText("PASSWORD:");
        panel_login.add(jLabel3);
        jLabel3.setBounds(430, 317, 150, 20);

        user1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        user1.setBorder(null);
        user1.setOpaque(false);
        panel_login.add(user1);
        user1.setBounds(480, 270, 360, 40);

        pass1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        pass1.setBorder(null);
        pass1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pass1MouseClicked(evt);
            }
        });
        pass1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pass1ActionPerformed(evt);
            }
        });
        panel_login.add(pass1);
        pass1.setBounds(480, 350, 360, 40);

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/input_login.png"))); // NOI18N
        jLabel22.setText("jLabel22");
        panel_login.add(jLabel22);
        jLabel22.setBounds(470, 260, 380, 50);

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/input_login.png"))); // NOI18N
        panel_login.add(jLabel23);
        jLabel23.setBounds(470, 340, 390, 50);

        submitbtn.setFont(new java.awt.Font("AR JULIAN", 1, 18)); // NOI18N
        submitbtn.setForeground(new java.awt.Color(240, 240, 240));
        submitbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_login_light.png"))); // NOI18N
        submitbtn.setText("SUBMIIT");
        submitbtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        submitbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                submitbtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitbtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitbtnMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                submitbtnMousePressed(evt);
            }
        });
        panel_login.add(submitbtn);
        submitbtn.setBounds(610, 440, 270, 40);

        pic_rnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/student4.png"))); // NOI18N
        panel_login.add(pic_rnd);
        pic_rnd.setBounds(10, 130, 350, 410);

        opt_btn_about_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light.png"))); // NOI18N
        opt_btn_about_log.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_btn_about_logMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_btn_about_logMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_btn_about_logMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_btn_about_logMousePressed(evt);
            }
        });
        panel_login.add(opt_btn_about_log);
        opt_btn_about_log.setBounds(980, 260, 220, 40);

        opt_btn_credits_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light.png"))); // NOI18N
        opt_btn_credits_log.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_btn_credits_logMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_btn_credits_logMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_btn_credits_logMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_btn_credits_logMousePressed(evt);
            }
        });
        panel_login.add(opt_btn_credits_log);
        opt_btn_credits_log.setBounds(980, 320, 220, 40);

        opt_exit_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_light.png"))); // NOI18N
        opt_exit_log.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_exit_logMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_exit_logMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_exit_logMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_exit_logMousePressed(evt);
            }
        });
        panel_login.add(opt_exit_log);
        opt_exit_log.setBounds(980, 380, 220, 40);

        opt_btn3_log.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_btn3_logMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_btn3_logMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_btn3_logMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_btn3_logMousePressed(evt);
            }
        });
        panel_login.add(opt_btn3_log);
        opt_btn3_log.setBounds(980, 200, 220, 40);
        panel_login.add(opt_label_log);
        opt_label_log.setBounds(980, 260, 220, 250);

        opt_open_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_light.png"))); // NOI18N
        opt_open_log.setToolTipText("");
        opt_open_log.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_open_logMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_open_logMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_open_logMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_open_logMousePressed(evt);
            }
        });
        panel_login.add(opt_open_log);
        opt_open_log.setBounds(885, 70, 60, 50);

        opt_close_log.setToolTipText("");
        opt_close_log.setEnabled(false);
        opt_close_log.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_close_logMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_close_logMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_close_logMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_close_logMousePressed(evt);
            }
        });
        panel_login.add(opt_close_log);
        opt_close_log.setBounds(885, 10, 60, 50);

        btn_backhome.setFont(new java.awt.Font("AR JULIAN", 1, 18)); // NOI18N
        btn_backhome.setForeground(new java.awt.Color(255, 255, 255));
        btn_backhome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_back_light.png"))); // NOI18N
        btn_backhome.setText("BACK");
        btn_backhome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_backhome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_backhomeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_backhomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_backhomeMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_backhomeMousePressed(evt);
            }
        });
        panel_login.add(btn_backhome);
        btn_backhome.setBounds(410, 440, 160, 40);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_log_long.png"))); // NOI18N
        jLabel4.setText("jLabel4");
        panel_login.add(jLabel4);
        jLabel4.setBounds(0, 0, 1230, 540);

        panel_adminmain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_adminmain.setPreferredSize(new java.awt.Dimension(950, 540));
        panel_adminmain.setLayout(null);

        btn_reglist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_check_light.png"))); // NOI18N
        btn_reglist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_reglistMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_reglistMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_reglistMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_reglistMousePressed(evt);
            }
        });
        panel_adminmain.add(btn_reglist);
        btn_reglist.setBounds(390, 210, 420, 80);

        btn_create.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_create_light.png"))); // NOI18N
        btn_create.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_createMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_createMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_createMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_createMousePressed(evt);
            }
        });
        panel_adminmain.add(btn_create);
        btn_create.setBounds(390, 300, 420, 80);

        btn_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_up_light.png"))); // NOI18N
        btn_update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_updateMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_updateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_updateMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_updateMousePressed(evt);
            }
        });
        panel_adminmain.add(btn_update);
        btn_update.setBounds(390, 390, 420, 80);

        btn_logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_log_light.png"))); // NOI18N
        btn_logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_logoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_logoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_logoutMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_logoutMousePressed(evt);
            }
        });
        panel_adminmain.add(btn_logout);
        btn_logout.setBounds(70, 440, 220, 40);

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_adminmain.png"))); // NOI18N
        panel_adminmain.add(jLabel46);
        jLabel46.setBounds(0, 0, 950, 540);

        panel_create.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_create.setPreferredSize(new java.awt.Dimension(950, 540));
        panel_create.setLayout(null);
        panel_create.add(field_mname);
        field_mname.setBounds(210, 290, 270, 20);

        btn_upload.setText("CHOOSE PHOTO");
        btn_upload.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_uploadMouseClicked(evt);
            }
        });
        btn_upload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_uploadActionPerformed(evt);
            }
        });
        panel_create.add(btn_upload);
        btn_upload.setBounds(710, 310, 129, 23);
        panel_create.add(field_lname);
        field_lname.setBounds(210, 320, 270, 20);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("STUDENT NUMBER:");
        panel_create.add(jLabel5);
        jLabel5.setBounds(10, 150, 140, 17);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("FIRST NAME:");
        panel_create.add(jLabel6);
        jLabel6.setBounds(10, 260, 100, 17);
        panel_create.add(field_address);
        field_address.setBounds(210, 400, 270, 20);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("LAST NAME:");
        panel_create.add(jLabel7);
        jLabel7.setBounds(10, 320, 90, 17);
        panel_create.add(field_number);
        field_number.setBounds(210, 430, 270, 20);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("MIDDLE NAME:");
        panel_create.add(jLabel8);
        jLabel8.setBounds(10, 290, 110, 17);

        btn_createreset.setText("RESET");
        btn_createreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createresetActionPerformed(evt);
            }
        });
        panel_create.add(btn_createreset);
        btn_createreset.setBounds(660, 430, 96, 23);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("COURSE:");
        panel_create.add(jLabel9);
        jLabel9.setBounds(10, 370, 70, 17);

        btn_createconfirm.setText("CREATE!");
        btn_createconfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createconfirmActionPerformed(evt);
            }
        });
        panel_create.add(btn_createconfirm);
        btn_createconfirm.setBounds(780, 430, 121, 23);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("HOME ADDRESS:");
        panel_create.add(jLabel10);
        jLabel10.setBounds(10, 400, 120, 17);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("PASSWORD:");
        panel_create.add(jLabel11);
        jLabel11.setBounds(10, 180, 100, 17);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("CONTACT NUMBER:");
        panel_create.add(jLabel12);
        jLabel12.setBounds(10, 430, 150, 17);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("CONFIRM PASSWORD:");
        panel_create.add(jLabel13);
        jLabel13.setBounds(10, 210, 160, 17);

        field_user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field_userActionPerformed(evt);
            }
        });
        panel_create.add(field_user);
        field_user.setBounds(210, 150, 274, 20);

        passfield_pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passfield_passActionPerformed(evt);
            }
        });
        panel_create.add(passfield_pass);
        passfield_pass.setBounds(210, 180, 274, 20);

        field_fname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field_fnameFocusGained(evt);
            }
        });
        field_fname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field_fnameActionPerformed(evt);
            }
        });
        field_fname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                field_fnameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                field_fnameKeyTyped(evt);
            }
        });
        panel_create.add(field_fname);
        field_fname.setBounds(210, 260, 270, 20);
        panel_create.add(passfield_confirm);
        passfield_confirm.setBounds(210, 210, 274, 20);

        lbl_passcheck.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_passcheck.setForeground(new java.awt.Color(204, 0, 0));
        panel_create.add(lbl_passcheck);
        lbl_passcheck.setBounds(230, 230, 390, 21);

        combo_course.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Select>", "BSCS", "BSCPE", "BSINFOTECH", "ACT", "BSEE", "BSIT", "BSBM", "BTTE", "BSHRM", "BSE", "AT", "DHRM", "DMT" }));
        combo_course.setToolTipText("");
        combo_course.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_courseItemStateChanged(evt);
            }
        });
        combo_course.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                combo_courseMouseClicked(evt);
            }
        });
        combo_course.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                combo_courseInputMethodTextChanged(evt);
            }
        });
        combo_course.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_courseActionPerformed(evt);
            }
        });
        panel_create.add(combo_course);
        combo_course.setBounds(210, 370, 100, 20);

        btn_editok.setText("CONFIRM");
        btn_editok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_editokMouseClicked(evt);
            }
        });
        btn_editok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editokActionPerformed(evt);
            }
        });
        panel_create.add(btn_editok);
        btn_editok.setBounds(780, 380, 121, 41);
        panel_create.add(lbl_combo);
        lbl_combo.setBounds(697, 500, 0, 0);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setMaximumSize(new java.awt.Dimension(142, 153));
        jPanel1.setMinimumSize(new java.awt.Dimension(142, 153));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(img_pic, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(img_pic, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel_create.add(jPanel1);
        jPanel1.setBounds(700, 120, 142, 153);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("DB ID:");
        panel_create.add(jLabel16);
        jLabel16.setBounds(10, 120, 60, 17);

        field_id.setEnabled(false);
        panel_create.add(field_id);
        field_id.setBounds(210, 120, 55, 20);

        btn_payment.setText("PAYMENT");
        btn_payment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_paymentActionPerformed(evt);
            }
        });
        panel_create.add(btn_payment);
        btn_payment.setBounds(11, 500, 120, 23);

        field_bal.setEnabled(false);
        panel_create.add(field_bal);
        field_bal.setBounds(210, 430, 111, 20);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("TUITION FEE/BALANCE:");
        panel_create.add(jLabel17);
        jLabel17.setBounds(10, 460, 180, 17);

        btn_removeaccnt.setText("REMOVE ACCOUNT");
        btn_removeaccnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_removeaccntActionPerformed(evt);
            }
        });
        panel_create.add(btn_removeaccnt);
        btn_removeaccnt.setBounds(161, 500, 171, 23);

        btn_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png"))); // NOI18N
        btn_back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_backMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_backMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_backMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_backMousePressed(evt);
            }
        });
        panel_create.add(btn_back);
        btn_back.setBounds(750, 480, 170, 40);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("NICKNAME:");
        panel_create.add(jLabel15);
        jLabel15.setBounds(10, 340, 110, 14);
        panel_create.add(field_nname);
        field_nname.setBounds(210, 340, 270, 20);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_create.png"))); // NOI18N
        panel_create.add(jLabel1);
        jLabel1.setBounds(0, 0, 950, 540);

        panel_records.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_records.setPreferredSize(new java.awt.Dimension(950, 540));
        panel_records.setLayout(null);

        table_records.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "    Student #", "    First Name", "    Middle Name", "    Last Name", "    Course", "    Address", "    Contact #", "Balance", "Nick Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table_records);

        panel_records.add(jScrollPane1);
        jScrollPane1.setBounds(11, 89, 920, 390);

        btn_back1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png"))); // NOI18N
        btn_back1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_back1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_back1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_back1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_back1MousePressed(evt);
            }
        });
        panel_records.add(btn_back1);
        btn_back1.setBounds(760, 490, 170, 40);

        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_records.png"))); // NOI18N
        panel_records.add(jLabel47);
        jLabel47.setBounds(0, 0, 950, 540);

        panel_update.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_update.setMaximumSize(new java.awt.Dimension(738, 548));
        panel_update.setMinimumSize(new java.awt.Dimension(738, 548));
        panel_update.setPreferredSize(new java.awt.Dimension(950, 540));
        panel_update.setLayout(null);

        field_search.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        field_search.setBorder(null);
        panel_update.add(field_search);
        field_search.setBounds(260, 293, 400, 50);

        lbl_searchoutput.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        panel_update.add(lbl_searchoutput);
        lbl_searchoutput.setBounds(570, 247, 363, 29);
        panel_update.add(lbl_nameoutput);
        lbl_nameoutput.setBounds(48, 282, 364, 28);

        btn_back2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png"))); // NOI18N
        btn_back2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_back2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_back2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_back2MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_back2MousePressed(evt);
            }
        });
        panel_update.add(btn_back2);
        btn_back2.setBounds(740, 480, 170, 40);

        btn_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_search_light.png"))); // NOI18N
        btn_search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_searchMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_searchMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_searchMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_searchMousePressed(evt);
            }
        });
        panel_update.add(btn_search);
        btn_search.setBounds(370, 370, 180, 50);

        jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_update.png"))); // NOI18N
        panel_update.add(jLabel48);
        jLabel48.setBounds(0, 0, 940, 550);

        panel_studmain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_studmain.setPreferredSize(new java.awt.Dimension(944, 540));
        panel_studmain.setVerifyInputWhenFocusTarget(false);
        panel_studmain.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                panel_studmainComponentShown(evt);
            }
        });
        panel_studmain.setLayout(null);

        jLayeredPane2.setPreferredSize(new java.awt.Dimension(900, 350));

        tab_stud.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        tab_stud.setToolTipText("");
        tab_stud.setName(""); // NOI18N

        tab_panel2.setLayout(null);

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_studtab1.png"))); // NOI18N
        tab_panel2.add(jLabel25);
        jLabel25.setBounds(2, 0, 890, 320);

        tab_stud.addTab("Message From The Campus Dean", tab_panel2);

        tab_panel3.setLayout(null);

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_courses1.png"))); // NOI18N
        jScrollPane2.setViewportView(jLabel27);

        tab_panel3.add(jScrollPane2);
        jScrollPane2.setBounds(60, 90, 765, 196);

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_course.png"))); // NOI18N
        tab_panel3.add(jLabel26);
        jLabel26.setBounds(2, 0, 890, 320);

        tab_stud.addTab("Courses Offered", tab_panel3);

        tab_panel4.setLayout(null);

        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_DEIT.png"))); // NOI18N
        tab_panel4.add(bgd_faculty_names);
        bgd_faculty_names.setBounds(50, 100, 460, 200);

        opt_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png"))); // NOI18N
        opt_back.setEnabled(false);
        opt_back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_backMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_backMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_backMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_backMousePressed(evt);
            }
        });
        tab_panel4.add(opt_back);
        opt_back.setBounds(620, 100, 170, 40);

        opt_next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_next_light.png"))); // NOI18N
        opt_next.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_nextMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_nextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_nextMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_nextMousePressed(evt);
            }
        });
        tab_panel4.add(opt_next);
        opt_next.setBounds(620, 160, 170, 40);

        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_DEIT.png"))); // NOI18N
        tab_panel4.add(lbl_faculty_title);
        lbl_faculty_title.setBounds(380, 20, 470, 50);

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_faculty.png"))); // NOI18N
        tab_panel4.add(jLabel28);
        jLabel28.setBounds(2, 0, 890, 320);

        tab_stud.addTab("Faculty Profile", tab_panel4);

        tab_panel1.setLayout(null);

        lbl_total.setFont(new java.awt.Font("Arial Black", 0, 48)); // NOI18N
        lbl_total.setForeground(new java.awt.Color(255, 255, 255));
        lbl_total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_total.setText("0");
        lbl_total.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tab_panel1.add(lbl_total);
        lbl_total.setBounds(610, 180, 160, 70);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("0");
        tab_panel1.add(jLabel42);
        jLabel42.setBounds(400, 190, 50, 10);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("0");
        tab_panel1.add(jLabel30);
        jLabel30.setBounds(400, 98, 50, 10);

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("0");
        tab_panel1.add(jLabel31);
        jLabel31.setBounds(400, 112, 50, 14);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("0");
        tab_panel1.add(jLabel32);
        jLabel32.setBounds(400, 131, 50, 10);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("0");
        tab_panel1.add(jLabel33);
        jLabel33.setBounds(400, 143, 50, 14);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("0");
        tab_panel1.add(jLabel34);
        jLabel34.setBounds(400, 160, 50, 10);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("0");
        tab_panel1.add(jLabel35);
        jLabel35.setBounds(400, 175, 50, 10);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("0");
        tab_panel1.add(jLabel36);
        jLabel36.setBounds(400, 205, 50, 14);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("0");
        tab_panel1.add(jLabel37);
        jLabel37.setBounds(400, 220, 50, 10);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("0");
        tab_panel1.add(jLabel38);
        jLabel38.setBounds(400, 234, 50, 14);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("0");
        tab_panel1.add(jLabel39);
        jLabel39.setBounds(400, 250, 50, 14);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("0");
        tab_panel1.add(jLabel40);
        jLabel40.setBounds(400, 268, 50, 10);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("0");
        tab_panel1.add(jLabel41);
        jLabel41.setBounds(400, 280, 50, 10);

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_enroll.png"))); // NOI18N
        jLabel29.setToolTipText("");
        tab_panel1.add(jLabel29);
        jLabel29.setBounds(2, 0, 890, 320);

        tab_stud.addTab("Enrollment Status", tab_panel1);

        jLayeredPane2.add(tab_stud);
        tab_stud.setBounds(0, 0, 900, 350);
        tab_stud.getAccessibleContext().setAccessibleName("1");

        panel_studgrade.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_studgrade.setOpaque(false);
        panel_studgrade.setPreferredSize(new java.awt.Dimension(900, 350));
        panel_studgrade.setLayout(null);

        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_studgrade.png"))); // NOI18N
        panel_studgrade.add(jLabel45);
        jLabel45.setBounds(0, 0, 900, 350);

        jLayeredPane2.add(panel_studgrade);
        panel_studgrade.setBounds(0, 0, 900, 350);

        panel_studprofile.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_studprofile.setOpaque(false);
        panel_studprofile.setPreferredSize(new java.awt.Dimension(900, 350));
        panel_studprofile.setLayout(null);

        lbl_studfname.setFont(new java.awt.Font("AR JULIAN", 1, 21)); // NOI18N
        lbl_studfname.setForeground(new java.awt.Color(255, 255, 255));
        lbl_studfname.setText("jLabel16");
        panel_studprofile.add(lbl_studfname);
        lbl_studfname.setBounds(220, 110, 160, 40);

        lbl_studmname.setFont(new java.awt.Font("AR JULIAN", 1, 21)); // NOI18N
        lbl_studmname.setForeground(new java.awt.Color(255, 255, 255));
        lbl_studmname.setText("jLabel16");
        panel_studprofile.add(lbl_studmname);
        lbl_studmname.setBounds(390, 110, 180, 40);

        lbl_studlname.setFont(new java.awt.Font("AR JULIAN", 1, 21)); // NOI18N
        lbl_studlname.setForeground(new java.awt.Color(255, 255, 255));
        lbl_studlname.setText("jLabel16");
        panel_studprofile.add(lbl_studlname);
        lbl_studlname.setBounds(40, 110, 170, 40);

        lbl_studcourse.setFont(new java.awt.Font("AR JULIAN", 1, 18)); // NOI18N
        lbl_studcourse.setForeground(new java.awt.Color(255, 255, 255));
        lbl_studcourse.setText("jLabel16");
        panel_studprofile.add(lbl_studcourse);
        lbl_studcourse.setBounds(310, 190, 390, 21);

        lbl_studnumber.setFont(new java.awt.Font("AR JULIAN", 1, 18)); // NOI18N
        lbl_studnumber.setForeground(new java.awt.Color(255, 255, 255));
        lbl_studnumber.setText("jLabel16");
        panel_studprofile.add(lbl_studnumber);
        lbl_studnumber.setBounds(310, 310, 390, 21);

        lbl_studaddress.setFont(new java.awt.Font("AR JULIAN", 1, 18)); // NOI18N
        lbl_studaddress.setForeground(new java.awt.Color(255, 255, 255));
        lbl_studaddress.setText("jLabel16");
        panel_studprofile.add(lbl_studaddress);
        lbl_studaddress.setBounds(310, 270, 390, 21);

        lbl_studdept.setFont(new java.awt.Font("AR JULIAN", 1, 18)); // NOI18N
        lbl_studdept.setForeground(new java.awt.Color(255, 255, 255));
        lbl_studdept.setText("jLabel16");
        panel_studprofile.add(lbl_studdept);
        lbl_studdept.setBounds(310, 230, 390, 21);

        lbl_studno.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbl_studno.setForeground(new java.awt.Color(255, 255, 255));
        lbl_studno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_studno.setText("jLabel17");
        panel_studprofile.add(lbl_studno);
        lbl_studno.setBounds(525, 90, 340, 29);

        jLabel43.setFont(new java.awt.Font("AR JULIAN", 1, 18)); // NOI18N
        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_studprofile.png"))); // NOI18N
        panel_studprofile.add(jLabel43);
        jLabel43.setBounds(0, 0, 900, 350);

        jLayeredPane2.add(panel_studprofile);
        panel_studprofile.setBounds(0, 0, 900, 350);

        panel_studbal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_studbal.setOpaque(false);
        panel_studbal.setPreferredSize(new java.awt.Dimension(900, 350));
        panel_studbal.setLayout(null);

        lbl_balcourse.setFont(new java.awt.Font("AR JULIAN", 1, 24)); // NOI18N
        lbl_balcourse.setForeground(new java.awt.Color(255, 255, 255));
        panel_studbal.add(lbl_balcourse);
        lbl_balcourse.setBounds(260, 80, 230, 50);

        lbl_ballpayfull.setFont(new java.awt.Font("AR JULIAN", 1, 18)); // NOI18N
        lbl_ballpayfull.setForeground(new java.awt.Color(255, 255, 255));
        lbl_ballpayfull.setText("   ");
        panel_studbal.add(lbl_ballpayfull);
        lbl_ballpayfull.setBounds(340, 160, 140, 40);

        lbl_balmidterm1.setFont(new java.awt.Font("AR JULIAN", 1, 18)); // NOI18N
        lbl_balmidterm1.setForeground(new java.awt.Color(255, 255, 255));
        lbl_balmidterm1.setText("jLabel16");
        panel_studbal.add(lbl_balmidterm1);
        lbl_balmidterm1.setBounds(340, 260, 140, 40);

        lbl_baldown1.setFont(new java.awt.Font("AR JULIAN", 1, 18)); // NOI18N
        lbl_baldown1.setForeground(new java.awt.Color(255, 255, 255));
        lbl_baldown1.setText("jLabel16");
        panel_studbal.add(lbl_baldown1);
        lbl_baldown1.setBounds(340, 210, 140, 40);

        lbl_balfinals1.setFont(new java.awt.Font("AR JULIAN", 1, 18)); // NOI18N
        lbl_balfinals1.setForeground(new java.awt.Color(255, 255, 255));
        lbl_balfinals1.setText("jLabel16");
        panel_studbal.add(lbl_balfinals1);
        lbl_balfinals1.setBounds(340, 310, 140, 40);

        lbl_balance.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbl_balance.setForeground(new java.awt.Color(255, 255, 255));
        lbl_balance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panel_studbal.add(lbl_balance);
        lbl_balance.setBounds(500, 180, 340, 110);

        jLabel44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_studbal.png"))); // NOI18N
        panel_studbal.add(jLabel44);
        jLabel44.setBounds(0, 0, 900, 350);

        jLayeredPane2.add(panel_studbal);
        panel_studbal.setBounds(0, 0, 900, 350);

        panel_studmain.add(jLayeredPane2);
        jLayeredPane2.setBounds(20, 180, 900, 350);

        btn_studbal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_home_light.png"))); // NOI18N
        btn_studbal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_studbalMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_studbalMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_studbalMouseExited(evt);
            }
        });
        panel_studmain.add(btn_studbal);
        btn_studbal.setBounds(750, 120, 130, 50);

        btn_studgrade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_viewGrades_light.png"))); // NOI18N
        btn_studgrade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_studgradeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_studgradeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_studgradeMouseExited(evt);
            }
        });
        panel_studmain.add(btn_studgrade);
        btn_studgrade.setBounds(580, 120, 130, 50);

        btn_studprofile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_viewPro_light.png"))); // NOI18N
        btn_studprofile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_studprofileMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_studprofileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_studprofileMouseExited(evt);
            }
        });
        panel_studmain.add(btn_studprofile);
        btn_studprofile.setBounds(200, 120, 130, 50);

        btn_studhome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_home_light1.png"))); // NOI18N
        btn_studhome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_studhomeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_studhomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_studhomeMouseExited(evt);
            }
        });
        panel_studmain.add(btn_studhome);
        btn_studhome.setBounds(40, 120, 130, 50);

        opt_open_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_light.png"))); // NOI18N
        opt_open_studmain.setToolTipText("");
        opt_open_studmain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_open_studmainMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_open_studmainMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_open_studmainMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_open_studmainMousePressed(evt);
            }
        });
        panel_studmain.add(opt_open_studmain);
        opt_open_studmain.setBounds(885, 70, 60, 50);

        opt_close_studmain.setToolTipText("");
        opt_close_studmain.setEnabled(false);
        opt_close_studmain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_close_studmainMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_close_studmainMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_close_studmainMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_close_studmainMousePressed(evt);
            }
        });
        panel_studmain.add(opt_close_studmain);
        opt_close_studmain.setBounds(885, 10, 60, 50);

        opt_btn_about_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light.png"))); // NOI18N
        opt_btn_about_studmain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_btn_about_studmainMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_btn_about_studmainMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_btn_about_studmainMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_btn_about_studmainMousePressed(evt);
            }
        });
        panel_studmain.add(opt_btn_about_studmain);
        opt_btn_about_studmain.setBounds(980, 260, 220, 40);

        opt_btn_credits_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light.png"))); // NOI18N
        opt_btn_credits_studmain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_btn_credits_studmainMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_btn_credits_studmainMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_btn_credits_studmainMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_btn_credits_studmainMousePressed(evt);
            }
        });
        panel_studmain.add(opt_btn_credits_studmain);
        opt_btn_credits_studmain.setBounds(980, 320, 220, 40);

        opt_exit_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_light.png"))); // NOI18N
        opt_exit_studmain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_exit_studmainMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_exit_studmainMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_exit_studmainMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_exit_studmainMousePressed(evt);
            }
        });
        panel_studmain.add(opt_exit_studmain);
        opt_exit_studmain.setBounds(980, 380, 220, 40);

        opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_log_light.png"))); // NOI18N
        opt_btn3_studmain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opt_btn3_studmainMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                opt_btn3_studmainMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                opt_btn3_studmainMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                opt_btn3_studmainMousePressed(evt);
            }
        });
        panel_studmain.add(opt_btn3_studmain);
        opt_btn3_studmain.setBounds(980, 200, 220, 40);
        panel_studmain.add(opt_label_studmain);
        opt_label_studmain.setBounds(980, 260, 220, 250);

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_studmain_long.png"))); // NOI18N
        panel_studmain.add(jLabel24);
        jLabel24.setBounds(0, 0, 1220, 540);

        panel_payment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_payment.setPreferredSize(new java.awt.Dimension(950, 540));
        panel_payment.setLayout(null);

        lbl_currentbalance.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lbl_currentbalance.setForeground(new java.awt.Color(255, 255, 255));
        lbl_currentbalance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panel_payment.add(lbl_currentbalance);
        lbl_currentbalance.setBounds(550, 270, 279, 71);

        field_paymententer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        field_paymententer.setBorder(null);
        panel_payment.add(field_paymententer);
        field_paymententer.setBounds(90, 284, 290, 40);

        btn_back3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png"))); // NOI18N
        btn_back3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_back3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_back3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_back3MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_back3MousePressed(evt);
            }
        });
        panel_payment.add(btn_back3);
        btn_back3.setBounds(760, 480, 170, 40);

        btn_paymententer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_enter_light.png"))); // NOI18N
        btn_paymententer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_paymententerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_paymententerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_paymententerMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_paymententerMousePressed(evt);
            }
        });
        panel_payment.add(btn_paymententer);
        btn_paymententer.setBounds(140, 350, 170, 40);

        jLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_payment.png"))); // NOI18N
        panel_payment.add(jLabel);
        jLabel.setBounds(0, 0, 950, 540);

        panel_admingrade.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_admingrade.setPreferredSize(new java.awt.Dimension(950, 540));
        panel_admingrade.setLayout(null);

        btn_backgrade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png"))); // NOI18N
        btn_backgrade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_backgradeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_backgradeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_backgradeMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_backgradeMousePressed(evt);
            }
        });
        panel_admingrade.add(btn_backgrade);
        btn_backgrade.setBounds(750, 480, 170, 40);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_grade.png"))); // NOI18N
        panel_admingrade.add(jLabel14);
        jLabel14.setBounds(0, 0, 950, 540);

        panel_remove.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_remove.setPreferredSize(new java.awt.Dimension(950, 540));
        panel_remove.setLayout(null);

        field_remove.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        field_remove.setBorder(null);
        panel_remove.add(field_remove);
        field_remove.setBounds(290, 282, 360, 50);

        btn_okremove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_remove_light.png"))); // NOI18N
        btn_okremove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_okremoveMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_okremoveMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_okremoveMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_okremoveMousePressed(evt);
            }
        });
        panel_remove.add(btn_okremove);
        btn_okremove.setBounds(380, 370, 180, 50);

        btn_remback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png"))); // NOI18N
        btn_remback.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_rembackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_rembackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_rembackMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_rembackMousePressed(evt);
            }
        });
        panel_remove.add(btn_remback);
        btn_remback.setBounds(760, 480, 170, 40);

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_remove.png"))); // NOI18N
        panel_remove.add(jLabel19);
        jLabel19.setBounds(0, 0, 950, 540);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_choice, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_login, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_adminmain, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_create, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_records, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_update, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_studmain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_payment, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_admingrade, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_remove, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(panel_choice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_adminmain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_create, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_records, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_update, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_studmain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_payment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_admingrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(panel_remove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jLayeredPane1.setLayer(panel_choice, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(panel_login, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(panel_adminmain, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(panel_create, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(panel_records, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(panel_update, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(panel_studmain, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(panel_payment, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(panel_admingrade, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(panel_remove, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 944, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pass1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pass1ActionPerformed
     
    }//GEN-LAST:event_pass1ActionPerformed

    private void pass1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pass1MouseClicked
        pass1.setText("");
    }//GEN-LAST:event_pass1MouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
       panel_login.hide(); 
       panel_adminmain.hide();
       panel_create.hide();
       panel_records.hide();
       panel_update.hide();
       panel_studmain.hide();
       panel_studbal.hide();
       panel_studgrade.hide();
       panel_payment.hide();
       panel_admingrade.hide();
       tab_stud.hide();
       panel_remove.hide();
       btn_backgrade.hide();
       opt_close.setVisible(false);
       
    }//GEN-LAST:event_formWindowOpened

    private void btn_uploadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_uploadMouseClicked

    }//GEN-LAST:event_btn_uploadMouseClicked

    private void btn_uploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_uploadActionPerformed
 
         JFileChooser fc=new JFileChooser(); 
         fc.showOpenDialog(this); 
         File f=fc.getSelectedFile();
         String path=f.getAbsolutePath();
         img_pic.setIcon(new ImageIcon(path));
         img_pic.setSize(120, 129);
         try{ FileInputStream fin=new FileInputStream(f);
         int len=(int)f.length();
       
         pst=conn.prepareStatement("insert into tablelogin (Image) values(?) where id = "+field_id.getText()+""); 
         pst.setBinaryStream(1, fin, len); 
         int status=pst.executeUpdate();
         if(status >0) { 
            JOptionPane.showMessageDialog(null, "Image uploaded");
         }
         else{ 
                JOptionPane.showMessageDialog(null,"Image not inserted!");
                 }
         }
         catch(Exception e){
             System.out.println(e); }
    }//GEN-LAST:event_btn_uploadActionPerformed

    private void btn_createresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_createresetActionPerformed
        clear();
    }//GEN-LAST:event_btn_createresetActionPerformed

    private void btn_createconfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_createconfirmActionPerformed
          try{
             pass=passfield_pass.getText();
            pass2=passfield_confirm.getText();
            lbl_passcheck.setText("(PASSWORD MATCHED)");    //condition for password
            lbl_passcheck.setForeground(Color.green);
        if (pass.equals(pass2)){
        String sql = "insert into tablelogin (username, password, fname, mname, lname, course, address, contact, balance, nname) values(?,?,?,?,?,?,?,?,?);";
        
            
            pst = conn.prepareStatement(sql);
            pst.setString(1,field_user.getText().toUpperCase());
            pst.setString(2,passfield_pass.getText().toUpperCase());
            pst.setString(3,field_fname.getText().toUpperCase());
            pst.setString(4,field_mname.getText().toUpperCase());
            pst.setString(5,field_lname.getText().toUpperCase());
            pst.setString(6,combo_course.getSelectedItem().toString().toUpperCase());
            pst.setString(7,field_address.getText().toUpperCase());
            pst.setString(8,field_number.getText().toUpperCase());
            pst.setString(9,field_bal.getText().toUpperCase());
            pst.setString(10,field_nname.getText().toUpperCase());
            pst.execute();
            lbl_passcheck.setText("(PASSWORD MATCHED)");
            lbl_passcheck.setForeground(Color.green);
            JOptionPane.showMessageDialog(null, "REGISTRATION SUCCESSFUL!");
            clear();
            UpdateJTable();
            lbl_passcheck.setText("      ");
            
        }
        
        else{
                lbl_passcheck.setText("(PASSWORD DIDN'T MATCH)");
                lbl_passcheck.setForeground(Color.red);
        }
          }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
 
        
    }//GEN-LAST:event_btn_createconfirmActionPerformed

    private void passfield_passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passfield_passActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passfield_passActionPerformed

    private void field_fnameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_field_fnameFocusGained
      
    }//GEN-LAST:event_field_fnameFocusGained

    private void field_fnameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_field_fnameKeyTyped
       
    }//GEN-LAST:event_field_fnameKeyTyped

    private void field_fnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_fnameActionPerformed
      
    }//GEN-LAST:event_field_fnameActionPerformed

    private void field_fnameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_field_fnameKeyPressed
     
    }//GEN-LAST:event_field_fnameKeyPressed

    private void combo_courseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_courseActionPerformed
          jcombobox();
          lbl_combo.hide();
    }//GEN-LAST:event_combo_courseActionPerformed

    private void btn_editokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editokActionPerformed
        
    }//GEN-LAST:event_btn_editokActionPerformed

    private void btn_editokMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_editokMouseClicked
      ///EDIT/UPDATE
   
        try {
            Statement statement = conn.createStatement();
            pass=passfield_pass.getText();
            pass2=passfield_confirm.getText();
            lbl_passcheck.setText("(PASSWORD MATCHED)");    //condition for password
            lbl_passcheck.setForeground(Color.green);
 
  if (pass.equals(pass2)){
       lbl_combo.setText(combo_course.getSelectedItem().toString()); 
        lbl_combo.hide();
        if ( ! field_search.getText().equals( "" ) ) {     //condition
            String query = "UPDATE tablelogin SET " +           //sql code
            "username='" + field_user.getText().toUpperCase() +          //inputs
            "', password='" + passfield_pass.getText().toUpperCase() +
            "', fname='" + field_fname.getText().toUpperCase() +
            "', mname='" + field_mname.getText().toUpperCase() +
            "', lname='" + field_lname.getText().toUpperCase() +
            "', course='" + lbl_combo.getText().toUpperCase() +
            "', address='" + field_address.getText().toUpperCase() +
            "', contact='" + field_number.getText().toUpperCase() +
                    "' where id = "+field_id.getText()+"";
   
         //basis of search
            int ctr = statement.executeUpdate( query );
                if ( ctr == 1 ){
                    JOptionPane.showMessageDialog(null,"UPDATE SUCCESSFUL!");
                    panel_adminmain.show();
                    panel_create.hide();
                    clear();
                    lbl_passcheck.setText("      ");
                    }
        else 
            JOptionPane.showMessageDialog(null,"UPDATE FAILED!");

                }
            }
  else{
                lbl_passcheck.setText("(PASSWORD DIDN'T MATCH)");
                lbl_passcheck.setForeground(Color.red);
  }
        }
 catch ( Exception e ) {
     JOptionPane.showMessageDialog(null, e);
 }    
                     
    }//GEN-LAST:event_btn_editokMouseClicked

    private void panel_studmainComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panel_studmainComponentShown
        try {               ////CODE FOR STUDENT PROFILE

            String query = "select username, fname, mname, lname, course, address, contact, balance from tablelogin WHERE username = '" +user1.getText().toUpperCase() + "'";

           
            pst =conn.prepareStatement(query);
            rss=pst.executeQuery();
            rss.next();

            int count = rss.getInt( 1 );

            if ( count != 0 ) {

                lbl_studno.setText( rss.getString( 1 ));
                lbl_studfname.setText(rss.getString( 2 ));
                lbl_studmname.setText(rss.getString( 3 ));
                lbl_studlname.setText(rss.getString( 4 )+",");
                lbl_studaddress.setText(rss.getString( 6 ));
                lbl_studcourse.setText(rss.getString( 5 ));
                 lbl_balance.setText(rss.getString( 8 ));
     
                lbl_studnumber.setText(rss.getString( 7 ));
                //////////// DISPLAY FOR ACCOUNT BALANCE
                if ((lbl_studcourse.getText().equals("BSCS"))||(lbl_studcourse.getText().equals("BSINFOTECH"))){
                    lbl_studdept.setText("FICT");
                    lbl_balcourse.setText(lbl_studcourse.getText());
                    lbl_ballpayfull.setText("P10,000");
                    lbl_baldown1.setText("P5,000");
                    lbl_balmidterm1.setText("P2,500");
                    lbl_balfinals1.setText("P2,500");

                }
                else if((lbl_studcourse.getText().equals("BSBM"))||(lbl_studcourse.getText().equals("BSHRM"))||(lbl_studcourse.getText().equals("BSE"))){
                    lbl_studdept.setText("FEMA");
                    lbl_balcourse.setText(lbl_studcourse.getText());
                    lbl_ballpayfull.setText("P8,000");
                    lbl_baldown1.setText("P4,000");
                    lbl_balmidterm1.setText("P2,000");
                    lbl_balfinals1.setText("P2,000");
                }
                 else if((lbl_studcourse.getText().equals("ACT"))||(lbl_studcourse.getText().equals("DHRM"))||(lbl_studcourse.getText().equals("DMT"))){
                     lbl_balcourse.setText(lbl_studcourse.getText());
                     lbl_ballpayfull.setText("P6,000");
                     lbl_baldown1.setText("P3,000");
                    lbl_balmidterm1.setText("P1,500");
                    lbl_balfinals1.setText("P1,500");
                 
                 }
                else if((lbl_studcourse.getText().equals("BSEE"))||(lbl_studcourse.getText().equals("BSCPE"))||(lbl_studcourse.getText().equals("BTTE"))||(lbl_studcourse.getText().equals("AT"))){
                    lbl_balcourse.setText(lbl_studcourse.getText());
                    lbl_ballpayfull.setText("P7,000");
                    lbl_baldown1.setText("P3,500");
                    lbl_balmidterm1.setText("P1,250");
                    lbl_balfinals1.setText("P1,250");
            
                 }

            }

        }
        catch ( Exception e){
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_panel_studmainComponentShown

    private void combo_courseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_combo_courseMouseClicked
       
    }//GEN-LAST:event_combo_courseMouseClicked

    private void btn_paymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_paymentActionPerformed
      
        panel_payment.show();
       panel_create.hide();
       lbl_currentbalance.setText(field_bal.getText());
    }//GEN-LAST:event_btn_paymentActionPerformed

    private void combo_courseInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_combo_courseInputMethodTextChanged
  
    }//GEN-LAST:event_combo_courseInputMethodTextChanged

    private void combo_courseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_courseItemStateChanged
       
    }//GEN-LAST:event_combo_courseItemStateChanged

    private void btn_removeaccntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_removeaccntActionPerformed
       
        int ans = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO DELETE THIS ACCOUNT?","delete", JOptionPane.OK_CANCEL_OPTION);
        if (ans== JOptionPane.YES_OPTION){
        String query="DELETE FROM tablelogin WHERE id= " +field_id.getText()+ "";
          try{
              pst = conn.prepareStatement(query);
              pst.executeUpdate();
              JOptionPane.showMessageDialog(null, "ACCOUNT: "+field_remove.getText()+"  SUCCESFULLY DELETED!");
              UpdateJTable();
              field_remove.setText("");
              clear();
              field_id.setText("");
              
          }
          catch(Exception e){
              JOptionPane.showMessageDialog(null, e);
          }
        }
        
    
    }//GEN-LAST:event_btn_removeaccntActionPerformed

    private void btn_adminMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_adminMouseEntered
        btn_admin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_dark.png")));
    }//GEN-LAST:event_btn_adminMouseEntered

    private void btn_adminMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_adminMouseExited
        btn_admin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_light.png")));
    }//GEN-LAST:event_btn_adminMouseExited

    private void btn_adminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_adminMouseClicked
       opt_close_log.setVisible(false);
        panel_login.show();                                    //show panel1
       panel_choice.hide();                                      // hide panel2
       jLabel2.setText("USERNAME:");                        //set text to username
       trig=1; 
       pic_rnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/teacher.png")));
       ret_login();
    }//GEN-LAST:event_btn_adminMouseClicked

    private void btn_studentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studentMouseClicked
        opt_close_log.setVisible(false);
       panel_login.show();
       panel_choice.hide(); 
       jLabel2.setText("STUDENT NUMBER:");
       trig=0;
        Random num = new Random();
       int number,counter = 1;
               
               while(counter==1)
               {
               number = 1+num.nextInt(4);
               pic_rnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/student"+number+".png")));
               counter++;
               }
      
       ret_login();
       
       
    }//GEN-LAST:event_btn_studentMouseClicked

    private void btn_studentMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studentMouseEntered
        btn_student.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_dark.png")));
    }//GEN-LAST:event_btn_studentMouseEntered

    private void btn_studentMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studentMouseExited
        btn_student.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_light.png")));
    }//GEN-LAST:event_btn_studentMouseExited

    private void btn_adminMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_adminMousePressed
        btn_admin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_darker.png")));
    }//GEN-LAST:event_btn_adminMousePressed

    private void btn_studentMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studentMousePressed
        btn_student.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_darker.png")));
    }//GEN-LAST:event_btn_studentMousePressed

    private void submitbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitbtnMouseClicked
        //////////////////// Login admin
     loginkeydown();
     
    }//GEN-LAST:event_submitbtnMouseClicked

    private void submitbtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitbtnMouseEntered
        submitbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_login_dark.png")));
    }//GEN-LAST:event_submitbtnMouseEntered

    private void submitbtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitbtnMouseExited
        submitbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_login_light.png")));
    }//GEN-LAST:event_submitbtnMouseExited

    private void submitbtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitbtnMousePressed
        submitbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_login_darker.png")));
    }//GEN-LAST:event_submitbtnMousePressed

    private void opt_openMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_openMouseEntered
        opt_open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_dark.png")));
    }//GEN-LAST:event_opt_openMouseEntered
    private void opt_openMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_openMouseExited
        opt_open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_light.png")));
    }//GEN-LAST:event_opt_openMouseExited
    private void opt_openMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_openMousePressed
        opt_open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_darker.png")));
    }//GEN-LAST:event_opt_openMousePressed
    private void opt_openMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_openMouseClicked
        opt_btn3.enable(false);opt_btn3.setVisible(false);
        
        jLabel21.setLocation(-281,0);
        opt_btn3.setLocation(699,200);
        opt_btn_about.setLocation(699,260);
        opt_btn_credits.setLocation(699,320);
        opt_exit.setLocation(699,380);
        opt_label.setLocation(699,260);
        
        btn_admin.setLocation(-211,260);
        btn_student.setLocation(-211,370);
        
        
        opt_open.enable(false);
        opt_open.setVisible(false);
        opt_open.setLocation(885,350);
        
        opt_close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_light.png")));
        opt_close.enable(true);
        opt_close.setVisible(true);
        opt_close.setLocation(604,400);
        
    }//GEN-LAST:event_opt_openMouseClicked

    private void opt_closeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_closeMouseEntered
        opt_close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_dark.png")));
    }//GEN-LAST:event_opt_closeMouseEntered
    private void opt_closeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_closeMouseExited
        opt_close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_light.png")));
    }//GEN-LAST:event_opt_closeMouseExited
    private void opt_closeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_closeMousePressed
        opt_close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_darker.png")));
    }//GEN-LAST:event_opt_closeMousePressed
    private void opt_closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_closeMouseClicked
        opt_btn3.setIcon(null);opt_label.setIcon(null);
        
        opt_btn_about.enable(true);opt_btn_credits.enable(true);opt_exit.enable(true);
        opt_btn_about.setVisible(true);opt_btn_credits.setVisible(true);opt_exit.setVisible(true);
        
        jLabel21.setLocation(0,0);
        opt_btn3.setLocation(980,200);
        opt_btn_about.setLocation(980,260);
        opt_btn_credits.setLocation(980,320);
        opt_exit.setLocation(980,380);
        opt_label.setLocation(980,260);
        
        btn_admin.setLocation(70,260);btn_student.setLocation(70,370);
        
        opt_close.enable(false);opt_close.setVisible(false);opt_close.setLocation(885,350);
        opt_open.enable(true);opt_open.setVisible(true);opt_open.setLocation(885,400);
    }//GEN-LAST:event_opt_closeMouseClicked

    private void opt_exitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exitMouseClicked
        exit();
    }//GEN-LAST:event_opt_exitMouseClicked
    private void opt_exitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exitMouseEntered
        opt_exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_dark.png")));
    }//GEN-LAST:event_opt_exitMouseEntered
    private void opt_exitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exitMouseExited
        opt_exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_light.png")));
    }//GEN-LAST:event_opt_exitMouseExited
    private void opt_exitMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exitMousePressed
        opt_exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_darker.png")));
    }//GEN-LAST:event_opt_exitMousePressed

    int plus=0;
    
    private void opt_btn_aboutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_aboutMouseClicked
        plus+=1;
        opt_btn3.enable(true);opt_btn3.setVisible(true);
        opt_btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light1.png")));
        opt_label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_info1.png")));
        opt_btn_about.enable(false);opt_btn_credits.enable(false);opt_exit.enable(false);
        opt_btn_about.setVisible(false);opt_btn_credits.setVisible(false);opt_exit.setVisible(false);
        
    }//GEN-LAST:event_opt_btn_aboutMouseClicked
    private void opt_btn3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3MouseClicked
        if(plus==1){
        plus-=1;
        }
        else{
            plus+=0;
        }
        
        opt_btn3.setIcon(null);opt_label.setIcon(null);
        opt_btn3.enable(false);opt_btn3.setVisible(false);
        
        opt_btn_about.enable(true);opt_btn_credits.enable(true);opt_exit.enable(true);
        opt_btn_about.setVisible(true);opt_btn_credits.setVisible(true);opt_exit.setVisible(true);
    }//GEN-LAST:event_opt_btn3MouseClicked

    private void opt_btn_creditsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_creditsMouseClicked
        opt_btn3.enable(true);opt_btn3.setVisible(true);
        opt_btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light1.png")));
        opt_label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_info.png")));
        opt_btn_about.enable(false);opt_btn_credits.enable(false);opt_exit.enable(false);
        opt_btn_about.setVisible(false);opt_btn_credits.setVisible(false);opt_exit.setVisible(false);
    }//GEN-LAST:event_opt_btn_creditsMouseClicked
    private void opt_btn3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3MouseEntered
        if(plus==0){
        opt_btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_dark1.png")));
        }
        else{
        opt_btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_dark1.png")));
        }
    }//GEN-LAST:event_opt_btn3MouseEntered
    private void opt_btn3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3MouseExited
        if(plus==0){
        opt_btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light1.png")));
        }
        else{
        opt_btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light1.png")));
        }
    }//GEN-LAST:event_opt_btn3MouseExited
    private void opt_btn3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3MousePressed
        if(plus==0){
        opt_btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_darker1.png")));
        }
        else{
        opt_btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_darker1.png")));
        }
    }//GEN-LAST:event_opt_btn3MousePressed

    private void opt_btn_aboutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_aboutMouseEntered
        opt_btn_about.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_dark.png")));
    }//GEN-LAST:event_opt_btn_aboutMouseEntered
    private void opt_btn_aboutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_aboutMouseExited
        opt_btn_about.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light.png")));
    }//GEN-LAST:event_opt_btn_aboutMouseExited
    private void opt_btn_aboutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_aboutMousePressed
        opt_btn_about.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_darker.png")));
    }//GEN-LAST:event_opt_btn_aboutMousePressed

    private void opt_btn_creditsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_creditsMouseEntered
        opt_btn_credits.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_dark.png")));
    }//GEN-LAST:event_opt_btn_creditsMouseEntered
    private void opt_btn_creditsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_creditsMouseExited
        opt_btn_credits.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light.png")));
    }//GEN-LAST:event_opt_btn_creditsMouseExited
    private void opt_btn_creditsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_creditsMousePressed
        opt_btn_credits.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_darker.png")));
    }//GEN-LAST:event_opt_btn_creditsMousePressed

    
    
////////////Panel_Login///////////////   
    
    private void opt_btn_about_logMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_about_logMouseClicked
        plus+=1;
        opt_btn3_log.enable(true);opt_btn3_log.setVisible(true);
        opt_btn3_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light1.png")));
        opt_label_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_info1.png")));
        opt_btn_about_log.enable(false);opt_btn_credits_log.enable(false);opt_exit_log.enable(false);
        opt_btn_about_log.setVisible(false);opt_btn_credits_log.setVisible(false);opt_exit_log.setVisible(false);
    }//GEN-LAST:event_opt_btn_about_logMouseClicked
    private void opt_btn_about_logMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_about_logMouseEntered
        opt_btn_about_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_dark.png")));
    }//GEN-LAST:event_opt_btn_about_logMouseEntered
    private void opt_btn_about_logMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_about_logMouseExited
        opt_btn_about_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light.png")));
    }//GEN-LAST:event_opt_btn_about_logMouseExited
    private void opt_btn_about_logMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_about_logMousePressed
        opt_btn_about_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_darker.png")));
    }//GEN-LAST:event_opt_btn_about_logMousePressed

    private void opt_btn_credits_logMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_credits_logMouseClicked
        opt_btn3_log.enable(true);opt_btn3_log.setVisible(true);
        opt_btn3_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light1.png")));
        opt_label_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_info.png")));
        opt_btn_about_log.enable(false);opt_btn_credits_log.enable(false);opt_exit_log.enable(false);
        opt_btn_about_log.setVisible(false);opt_btn_credits_log.setVisible(false);opt_exit_log.setVisible(false);
    }//GEN-LAST:event_opt_btn_credits_logMouseClicked
    private void opt_btn_credits_logMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_credits_logMouseEntered
        opt_btn_credits_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_dark.png")));
    }//GEN-LAST:event_opt_btn_credits_logMouseEntered
    private void opt_btn_credits_logMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_credits_logMouseExited
        opt_btn_credits_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light.png")));
    }//GEN-LAST:event_opt_btn_credits_logMouseExited
    private void opt_btn_credits_logMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_credits_logMousePressed
        opt_btn_credits_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_darker.png")));
    }//GEN-LAST:event_opt_btn_credits_logMousePressed

    private void opt_exit_logMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exit_logMouseClicked
        exit();
    }//GEN-LAST:event_opt_exit_logMouseClicked
    private void opt_exit_logMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exit_logMouseEntered
        opt_exit_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_dark.png")));
    }//GEN-LAST:event_opt_exit_logMouseEntered
    private void opt_exit_logMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exit_logMouseExited
        opt_exit_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_light.png")));
    }//GEN-LAST:event_opt_exit_logMouseExited
    private void opt_exit_logMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exit_logMousePressed
        opt_exit_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_darker.png")));
    }//GEN-LAST:event_opt_exit_logMousePressed

    private void opt_btn3_logMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3_logMouseClicked
        if(plus==1){
        plus-=1;
        }
        else{
            plus+=0;
        }
        
        opt_btn3_log.setIcon(null);opt_label_log.setIcon(null);
        opt_btn3_log.enable(false);opt_btn3_log.setVisible(false);
        
        opt_btn_about_log.enable(true);opt_btn_credits_log.enable(true);opt_exit_log.enable(true);
        opt_btn_about_log.setVisible(true);opt_btn_credits_log.setVisible(true);opt_exit_log.setVisible(true);
    }//GEN-LAST:event_opt_btn3_logMouseClicked
    private void opt_btn3_logMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3_logMouseEntered
        if(plus==0){
        opt_btn3_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_dark1.png")));
        }
        else{
        opt_btn3_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_dark1.png")));
        }
    }//GEN-LAST:event_opt_btn3_logMouseEntered
    private void opt_btn3_logMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3_logMouseExited
        if(plus==0){
        opt_btn3_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light1.png")));
        }
        else{
        opt_btn3_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light1.png")));
        }
    }//GEN-LAST:event_opt_btn3_logMouseExited
    private void opt_btn3_logMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3_logMousePressed
        if(plus==0){
        opt_btn3_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_darker1.png")));
        }
        else{
        opt_btn3_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_darker1.png")));
        }
    }//GEN-LAST:event_opt_btn3_logMousePressed

    private void opt_open_logMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_open_logMouseClicked
        opt_btn3_log.enable(false);opt_btn3_log.setVisible(false);
        
        jLabel4.setLocation(-281,0);
        opt_btn3_log.setLocation(699,200);
        opt_btn_about_log.setLocation(699,260);
        opt_btn_credits_log.setLocation(699,320);
        opt_exit_log.setLocation(699,380);
        opt_label_log.setLocation(699,260);
        
        jLabel2.setLocation(149,237);jLabel3.setLocation(149,317);
        jLabel22.setLocation(189,260);jLabel23.setLocation(189,340);
        user1.setLocation(199,270);pass1.setLocation(199,350);lbl_errorlog.setLocation(209,390);
        btn_backhome.setLocation(129,440);submitbtn.setLocation(329,440);pic_rnd.setLocation(-271,130);
        
        opt_open_log.enable(false);
        opt_open_log.setVisible(false);
        opt_open_log.setLocation(885,10);
        
        opt_close_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_light.png")));
        opt_close_log.enable(true);
        opt_close_log.setVisible(true);
        opt_close_log.setLocation(604,70);
        
    }//GEN-LAST:event_opt_open_logMouseClicked
    private void opt_open_logMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_open_logMouseEntered
        opt_open_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_dark.png")));
    }//GEN-LAST:event_opt_open_logMouseEntered
    private void opt_open_logMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_open_logMouseExited
        opt_open_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_light.png")));
    }//GEN-LAST:event_opt_open_logMouseExited
    private void opt_open_logMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_open_logMousePressed
        opt_open_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_darker.png")));
    }//GEN-LAST:event_opt_open_logMousePressed

    private void opt_close_logMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_close_logMouseClicked
        ret_login();
    }//GEN-LAST:event_opt_close_logMouseClicked
    private void opt_close_logMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_close_logMouseEntered
        opt_close_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_dark.png")));
    }//GEN-LAST:event_opt_close_logMouseEntered
    private void opt_close_logMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_close_logMouseExited
        opt_close_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_light.png")));
    }//GEN-LAST:event_opt_close_logMouseExited
    private void opt_close_logMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_close_logMousePressed
        opt_close_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_darker.png")));
    }//GEN-LAST:event_opt_close_logMousePressed

    
    
    int studplus=0;
    private void btn_studbalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studbalMouseClicked
        ret_close_studmain();
        studplus+=1;
        panel_studbal.show();
        panel_studprofile.hide();
        panel_studgrade.hide();
        tab_stud.hide();
        
        panel_studgrade.setAlignmentY(0);panel_studprofile.setAlignmentY(0);
        jLayeredPane2.setAlignmentY(-30);panel_studbal.setAlignmentY(-30);
        btn_studhome.setLocation(260,90);btn_studprofile.setLocation(420,90);
        btn_studgrade.setLocation(580,90);btn_studbal.setLocation(750,90);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_studmain1_long.png")));
    }//GEN-LAST:event_btn_studbalMouseClicked
    private void btn_studbalMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studbalMouseEntered
        btn_studbal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_home_dark.png")));
    }//GEN-LAST:event_btn_studbalMouseEntered

    private void btn_studgradeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studgradeMouseClicked
        ret_close_studmain();
        studplus+=1;
        panel_studgrade.show();
        panel_studbal.hide();
        panel_studprofile.hide();
        tab_stud.hide();
        
        panel_studprofile.setAlignmentY(0);panel_studbal.setAlignmentY(0);
        jLayeredPane2.setAlignmentY(-30);panel_studgrade.setAlignmentY(-30);
        btn_studhome.setLocation(260,90);btn_studprofile.setLocation(420,90);
        btn_studgrade.setLocation(580,90);btn_studbal.setLocation(750,90);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_studmain1_long.png")));
    }//GEN-LAST:event_btn_studgradeMouseClicked
    private void btn_studbalMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studbalMouseExited
        btn_studbal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_home_light.png")));
    }//GEN-LAST:event_btn_studbalMouseExited
    private void btn_studgradeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studgradeMouseEntered
        btn_studgrade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_viewGrades_dark.png")));
    }//GEN-LAST:event_btn_studgradeMouseEntered
    private void btn_studgradeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studgradeMouseExited
        btn_studgrade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_viewGrades_light.png")));
    }//GEN-LAST:event_btn_studgradeMouseExited

    private void btn_studprofileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studprofileMouseClicked
        ret_close_studmain();
        studplus+=1;
        panel_studbal.hide();
        panel_studprofile.show();
        panel_studgrade.hide();
        tab_stud.hide();
        
        panel_studgrade.setAlignmentY(0);panel_studbal.setAlignmentY(0);
        jLayeredPane2.setAlignmentY(-30);panel_studprofile.setAlignmentY(-30);
        btn_studhome.setLocation(260,90);btn_studprofile.setLocation(420,90);
        btn_studgrade.setLocation(580,90);btn_studbal.setLocation(750,90);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_studmain1_long.png")));
    }//GEN-LAST:event_btn_studprofileMouseClicked
    private void btn_studprofileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studprofileMouseEntered
        btn_studprofile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_viewPro_dark.png")));
    }//GEN-LAST:event_btn_studprofileMouseEntered
    private void btn_studprofileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studprofileMouseExited
        btn_studprofile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_viewPro_light.png")));
    }//GEN-LAST:event_btn_studprofileMouseExited

    private void btn_studhomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studhomeMouseClicked
        ret_close_studmain();
        studplus-=1;
        panel_studbal.hide();
        panel_studprofile.hide();
        panel_studgrade.hide();
        tab_stud.show();
        
        
        jLayeredPane2.setAlignmentY(0);panel_studprofile.setAlignmentY(0);
        panel_studgrade.setAlignmentY(0);panel_studbal.setAlignmentY(0);
        btn_studhome.setLocation(40,120);btn_studprofile.setLocation(200,120);
        btn_studgrade.setLocation(580,120);btn_studbal.setLocation(750,120);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_studmain_long.png")));
    }//GEN-LAST:event_btn_studhomeMouseClicked
    private void btn_studhomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studhomeMouseEntered
        btn_studhome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_home_dark1.png")));
    }//GEN-LAST:event_btn_studhomeMouseEntered
    private void btn_studhomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_studhomeMouseExited
        btn_studhome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_home_light1.png")));
    }//GEN-LAST:event_btn_studhomeMouseExited

    private void btn_backhomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backhomeMouseClicked
        opt_btn3.setIcon(null);opt_label.setIcon(null);
        
        opt_btn_about.enable(true);opt_btn_credits.enable(true);opt_exit.enable(true);
        opt_btn_about.setVisible(true);opt_btn_credits.setVisible(true);opt_exit.setVisible(true);
        
        jLabel21.setLocation(0,0);
        opt_btn3.setLocation(980,200);
        opt_btn_about.setLocation(980,260);
        opt_btn_credits.setLocation(980,320);
        opt_exit.setLocation(980,380);
        
        btn_admin.setLocation(70,260);btn_student.setLocation(70,370);
        
        opt_close.enable(false);opt_close.setVisible(false);opt_close.setLocation(885,350);
        opt_open.enable(true);opt_open.setVisible(true);opt_open.setLocation(885,400);
        
       panel_login.hide();
       panel_choice.show();
       user1.setText("");
       pass1.setText("");
       lbl_errorlog.setText("");
    }//GEN-LAST:event_btn_backhomeMouseClicked
    private void btn_backhomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backhomeMouseEntered
        btn_backhome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_back_dark.png")));
    }//GEN-LAST:event_btn_backhomeMouseEntered
    private void btn_backhomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backhomeMouseExited
        btn_backhome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_back_light.png")));
    }//GEN-LAST:event_btn_backhomeMouseExited
    private void btn_backhomeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backhomeMousePressed
        btn_backhome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_back_darker.png")));
    }//GEN-LAST:event_btn_backhomeMousePressed

    
    
    
    /////////////////////Panel_studmain//////////////
    int plus1=0;
    private void opt_open_studmainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_open_studmainMouseClicked
        ret_open_studmain();
    }//GEN-LAST:event_opt_open_studmainMouseClicked
    private void opt_open_studmainMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_open_studmainMouseEntered
        opt_open_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_dark.png")));
    }//GEN-LAST:event_opt_open_studmainMouseEntered
    private void opt_open_studmainMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_open_studmainMouseExited
        opt_open_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_light.png")));
    }//GEN-LAST:event_opt_open_studmainMouseExited
    private void opt_open_studmainMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_open_studmainMousePressed
        opt_open_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_open_darker.png")));
    }//GEN-LAST:event_opt_open_studmainMousePressed

    private void opt_close_studmainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_close_studmainMouseClicked
        ret_close_studmain();
    }//GEN-LAST:event_opt_close_studmainMouseClicked
    private void opt_close_studmainMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_close_studmainMouseEntered
        opt_close_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_dark.png")));
    }//GEN-LAST:event_opt_close_studmainMouseEntered
    private void opt_close_studmainMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_close_studmainMouseExited
        opt_close_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_light.png")));
    }//GEN-LAST:event_opt_close_studmainMouseExited
    private void opt_close_studmainMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_close_studmainMousePressed
        opt_close_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_close_darker.png")));
    }//GEN-LAST:event_opt_close_studmainMousePressed

    private void opt_btn_about_studmainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_about_studmainMouseClicked
        plus1+=2;
        opt_btn3_studmain.enable(true);opt_btn3_studmain.setVisible(true);
        opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light1.png")));
        opt_label_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_info1.png")));
        opt_btn_about_studmain.enable(false);opt_btn_credits_studmain.enable(false);opt_exit_studmain.enable(false);
        opt_btn_about_studmain.setVisible(false);opt_btn_credits_studmain.setVisible(false);opt_exit_studmain.setVisible(false);
    }//GEN-LAST:event_opt_btn_about_studmainMouseClicked
    private void opt_btn_about_studmainMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_about_studmainMouseEntered
        opt_btn_about_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_dark.png")));
    }//GEN-LAST:event_opt_btn_about_studmainMouseEntered
    private void opt_btn_about_studmainMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_about_studmainMouseExited
        opt_btn_about_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light.png")));
    }//GEN-LAST:event_opt_btn_about_studmainMouseExited
    private void opt_btn_about_studmainMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_about_studmainMousePressed
        opt_btn_about_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_darker.png")));
    }//GEN-LAST:event_opt_btn_about_studmainMousePressed

    private void opt_btn_credits_studmainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_credits_studmainMouseClicked
        plus1+=1;
        opt_btn3_studmain.enable(true);opt_btn3_studmain.setVisible(true);
        opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light1.png")));
        opt_label_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_info.png")));
        opt_btn_about_studmain.enable(false);opt_btn_credits_studmain.enable(false);opt_exit_studmain.enable(false);
        opt_btn_about_studmain.setVisible(false);opt_btn_credits_studmain.setVisible(false);opt_exit_studmain.setVisible(false);
    }//GEN-LAST:event_opt_btn_credits_studmainMouseClicked
    private void opt_btn_credits_studmainMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_credits_studmainMouseEntered
        opt_btn_credits_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_dark.png")));
    }//GEN-LAST:event_opt_btn_credits_studmainMouseEntered
    private void opt_btn_credits_studmainMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_credits_studmainMouseExited
        opt_btn_credits_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light.png")));
    }//GEN-LAST:event_opt_btn_credits_studmainMouseExited
    private void opt_btn_credits_studmainMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn_credits_studmainMousePressed
        opt_btn_credits_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_darker.png")));
    }//GEN-LAST:event_opt_btn_credits_studmainMousePressed

    private void opt_exit_studmainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exit_studmainMouseClicked
        exit();
    }//GEN-LAST:event_opt_exit_studmainMouseClicked
    private void opt_exit_studmainMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exit_studmainMouseEntered
        opt_exit_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_dark.png")));
    }//GEN-LAST:event_opt_exit_studmainMouseEntered
    private void opt_exit_studmainMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exit_studmainMouseExited
        opt_exit_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_light.png")));
    }//GEN-LAST:event_opt_exit_studmainMouseExited
    private void opt_exit_studmainMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_exit_studmainMousePressed
        opt_exit_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_exit_darker.png")));
    }//GEN-LAST:event_opt_exit_studmainMousePressed

    private void opt_btn3_studmainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3_studmainMouseClicked
        
        if(plus1==1){
        plus1-=1;
        }
        else if(plus1==2){
        plus1-=2;    
        }
        else{
            plus1+=0;logout();
        }
        
        opt_label_studmain.setIcon(null);opt_label_studmain.setIcon(null);
                
        opt_btn_about_studmain.enable(true);opt_btn_credits_studmain.enable(true);opt_exit_studmain.enable(true);
        opt_btn_about_studmain.setVisible(true);opt_btn_credits_studmain.setVisible(true);opt_exit_studmain.setVisible(true);
    }//GEN-LAST:event_opt_btn3_studmainMouseClicked
    private void opt_btn3_studmainMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3_studmainMouseEntered
        
        if(plus1==1){
        opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_dark1.png")));
        }
        else if(plus1==2){
        opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_dark1.png")));
        }
        else{
            opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_log_dark.png")));
        }
    }//GEN-LAST:event_opt_btn3_studmainMouseEntered
    private void opt_btn3_studmainMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3_studmainMouseExited
        
        if(plus1==1){
        opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_light1.png")));
        }
        else if(plus1==2){
        opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_light1.png")));
        }
        else{
        opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_log_light.png")));
        }
    }//GEN-LAST:event_opt_btn3_studmainMouseExited
    private void opt_btn3_studmainMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_btn3_studmainMousePressed
        
        if(plus1==1){
        opt_btn3_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_credits_darker1.png")));
        }
        else if(plus1==2){
        opt_btn3_log.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_about_darker1.png")));
        }
        else{
            opt_btn3_studmain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_log_darker.png")));
        }
    }//GEN-LAST:event_opt_btn3_studmainMousePressed

    
    
    ////////////////////////////panel_enroll/////////////////////////////
    
    int plusnext=0;
    private void opt_nextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_nextMouseClicked
        
        if (plusnext==0)
        {opt_back.setVisible(true);opt_back.enable(true);
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_MS.png")));plusnext+=1;
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_MS.png")));
        }
        else if (plusnext==1)
        {
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_SPEAR.png")));plusnext+=1;
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_SPEAR.png")));
        }
        else if (plusnext==2)
        {
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_TE1.png")));plusnext+=1;
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_TE.png")));
        }
        else if (plusnext==3)
        {
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_TE2.png")));plusnext+=1;
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_TE.png")));
        }
        else if (plusnext==4)
        {
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_IT1.png")));plusnext+=1;
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_IT.png")));
        }
        else if (plusnext==5)
        {
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_IT2.png")));
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_IT.png")));
        opt_next.setVisible(false);opt_next.enable(false);
        }
        
    }//GEN-LAST:event_opt_nextMouseClicked

    private void opt_nextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_nextMouseEntered
        opt_next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_next_dark.png")));
    }//GEN-LAST:event_opt_nextMouseEntered
    private void opt_nextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_nextMouseExited
        opt_next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_next_light.png")));
    }//GEN-LAST:event_opt_nextMouseExited
    private void opt_nextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_nextMousePressed
        opt_next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_next_darker.png")));
    }//GEN-LAST:event_opt_nextMousePressed

    private void opt_backMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_backMouseClicked
       if (plusnext==0)
        {
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_DEIT.png")));
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_DEIT.png")));
        opt_back.setVisible(false);opt_back.enable(false);
        }
       else if (plusnext==1)
        {
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_MS.png")));plusnext-=1;
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_MS.png")));
        }
       else if (plusnext==2)
        {
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_SPEAR.png")));plusnext-=1;
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_SPEAR.png")));
        }
       else if (plusnext==3)
        {
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_TE1.png")));plusnext-=1;
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_TE.png")));
        }
       else if (plusnext==4)
        {
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_TE2.png")));plusnext-=1;
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_TE.png")));
        }
       else if (plusnext==5)
        {
        bgd_faculty_names.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_IT1.png")));plusnext-=1;
        lbl_faculty_title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/lbl_IT.png")));
        opt_next.setVisible(true);opt_next.enable(true);
        }
        
    }//GEN-LAST:event_opt_backMouseClicked

    private void opt_backMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_backMouseEntered
        opt_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_dark.png")));
    }//GEN-LAST:event_opt_backMouseEntered
    private void opt_backMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_backMouseExited
   opt_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png")));
    }//GEN-LAST:event_opt_backMouseExited
    private void opt_backMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opt_backMousePressed
        opt_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_darker.png")));
    }//GEN-LAST:event_opt_backMousePressed

    
    
    ////////////////////////////panel_adminmmain///////////////////////////////
    private void btn_reglistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reglistMouseClicked
        panel_records.show();
                panel_adminmain.hide();
                
                UpdateJTable();
                clear();
    }//GEN-LAST:event_btn_reglistMouseClicked
    private void btn_createMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_createMouseClicked
       jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_create.png")));
        panel_adminmain.hide();
       panel_create.show();
       btn_editok.hide();
       btn_createconfirm.show();
       btn_createreset.show();
       btn_payment.hide();
       btn_removeaccnt.hide();
       field_id.hide();
       jLabel16.hide();
       clear();
    }//GEN-LAST:event_btn_createMouseClicked
    private void btn_updateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_updateMouseClicked
        panel_update.show();
      panel_adminmain.hide();
      field_search.show();
      btn_search.show();
      btn_payment.hide();
      btn_removeaccnt.hide();
      field_search.setText("");
      lbl_nameoutput.hide();
      lbl_searchoutput.hide();
      field_id.show();
       jLabel16.show();
      
      clear();
    }//GEN-LAST:event_btn_updateMouseClicked

    private void btn_reglistMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reglistMouseEntered
        btn_reglist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_check_dark.png")));
    }//GEN-LAST:event_btn_reglistMouseEntered
    private void btn_reglistMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reglistMouseExited
        btn_reglist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_check_light.png")));
    }//GEN-LAST:event_btn_reglistMouseExited
    private void btn_reglistMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reglistMousePressed
        btn_reglist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_check_darker.png")));
    }//GEN-LAST:event_btn_reglistMousePressed

    private void btn_createMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_createMouseEntered
        btn_create.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_create_dark.png")));
    }//GEN-LAST:event_btn_createMouseEntered
    private void btn_createMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_createMouseExited
        btn_create.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_create_light.png")));
    }//GEN-LAST:event_btn_createMouseExited
    private void btn_createMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_createMousePressed
        btn_create.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_create_darker.png")));
    }//GEN-LAST:event_btn_createMousePressed

    private void btn_updateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_updateMouseEntered
        btn_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_up_dark.png")));
    }//GEN-LAST:event_btn_updateMouseEntered
    private void btn_updateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_updateMouseExited
        btn_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_up_light.png")));
    }//GEN-LAST:event_btn_updateMouseExited
    private void btn_updateMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_updateMousePressed
        btn_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_up_darker.png")));
    }//GEN-LAST:event_btn_updateMousePressed

    private void btn_back1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back1MouseClicked
        panel_records.hide();
        panel_adminmain.show();
    }//GEN-LAST:event_btn_back1MouseClicked
    private void btn_back1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back1MouseEntered
        btn_back1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_dark.png")));
    }//GEN-LAST:event_btn_back1MouseEntered
    private void btn_back1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back1MouseExited
        btn_back1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png")));
    }//GEN-LAST:event_btn_back1MouseExited
    private void btn_back1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back1MousePressed
        btn_back1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_darker.png")));
    }//GEN-LAST:event_btn_back1MousePressed

    private void btn_back2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back2MouseClicked
        panel_update.hide();
       panel_adminmain.show();
       jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_update.png")));
    }//GEN-LAST:event_btn_back2MouseClicked
    private void btn_back2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back2MouseEntered
        btn_back2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_dark.png")));
    }//GEN-LAST:event_btn_back2MouseEntered
    private void btn_back2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back2MouseExited
        btn_back2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png")));
    }//GEN-LAST:event_btn_back2MouseExited
    private void btn_back2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back2MousePressed
        btn_back2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_darker.png")));
    }//GEN-LAST:event_btn_back2MousePressed

    private void btn_searchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_searchMouseClicked
        searchkeydown();
    }//GEN-LAST:event_btn_searchMouseClicked

    private void btn_searchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_searchMouseEntered
        btn_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_search_dark.png")));
    }//GEN-LAST:event_btn_searchMouseEntered

    private void btn_searchMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_searchMouseExited
        btn_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_search_light.png")));
    }//GEN-LAST:event_btn_searchMouseExited

    private void btn_searchMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_searchMousePressed
        btn_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/btn_search_darker.png")));
    }//GEN-LAST:event_btn_searchMousePressed

    private void btn_back3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back3MouseClicked
        panel_payment.hide();
        panel_create.show();
    }//GEN-LAST:event_btn_back3MouseClicked
    private void btn_back3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back3MouseEntered
        btn_back3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_dark.png")));
    }//GEN-LAST:event_btn_back3MouseEntered
    private void btn_back3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back3MouseExited
        btn_back3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png")));
    }//GEN-LAST:event_btn_back3MouseExited
    private void btn_back3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_back3MousePressed
        btn_back3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_darker.png")));
    }//GEN-LAST:event_btn_back3MousePressed

    private void btn_paymententerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_paymententerMouseClicked
        int payment= Integer.parseInt(field_paymententer.getText());          ///condition to payments made
       int balance =  Integer.parseInt(lbl_currentbalance.getText());
       int currentbalance = balance - payment;
       String cb = ""; 
       cb+=currentbalance;
       lbl_currentbalance.setText(cb);          //display balance to admin  
       field_paymententer.setText("");
       
       
       //display of balance in admin
        try {
            Statement statement = conn.createStatement();

            String query = "UPDATE tablelogin SET " +           //sql code
            "balance='" + lbl_currentbalance.getText().toUpperCase() +          //inputs
                    "' where id = "+field_id.getText()+"";
   

            int ctr = statement.executeUpdate( query );
                if ( ctr == 1 ){
                    JOptionPane.showMessageDialog(null,"PAYMENT SUCCESSFUL!");          //balance condition
                       if(currentbalance==0){
                      JOptionPane.showMessageDialog(null, "FULLY PAID!");
                         }
                     else if(currentbalance<0){
         
                    int change=Integer.parseInt(cb)/-1;
                    JOptionPane.showMessageDialog(null, "CHANGE IS "+change+" PESOS");
                    
                    }
                }
        else {
            JOptionPane.showMessageDialog(null,"PAYMENT FAILED!");
                }
        }

 catch ( Exception e ) {
     JOptionPane.showMessageDialog(null, e);
 }
    }//GEN-LAST:event_btn_paymententerMouseClicked
    private void btn_paymententerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_paymententerMouseEntered
        btn_paymententer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_enter_dark.png")));
    }//GEN-LAST:event_btn_paymententerMouseEntered
    private void btn_paymententerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_paymententerMouseExited
        btn_paymententer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_enter_light.png")));
    }//GEN-LAST:event_btn_paymententerMouseExited
    private void btn_paymententerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_paymententerMousePressed
        btn_paymententer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_enter_darker.png")));
    }//GEN-LAST:event_btn_paymententerMousePressed

    private void btn_backgradeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backgradeMouseClicked
        panel_admingrade.hide();
        panel_create.show();
    }//GEN-LAST:event_btn_backgradeMouseClicked

    private void btn_backgradeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backgradeMouseEntered
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_dark.png")));
    }//GEN-LAST:event_btn_backgradeMouseEntered

    private void btn_backgradeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backgradeMouseExited
        btn_backgrade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png")));
    }//GEN-LAST:event_btn_backgradeMouseExited

    private void btn_backgradeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backgradeMousePressed
        btn_backgrade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_darker.png")));
    }//GEN-LAST:event_btn_backgradeMousePressed

    private void btn_okremoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_okremoveMouseClicked
        ///////////////////////////////delete
        if (!field_remove.getText().equals("")){
            String query="DELETE FROM tablelogin WHERE username= " +field_remove.getText()+ "";
            try{
                pst = conn.prepareStatement(query);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "ACCOUNT: "+field_remove.getText()+"  SUCCESFULLY DELETED!");
                UpdateJTable();
                field_remove.setText("");

            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, e);
            }

        }

        else
        JOptionPane.showMessageDialog(null, "FILL EMPTY FIELD!");

    }//GEN-LAST:event_btn_okremoveMouseClicked
    private void btn_okremoveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_okremoveMouseEntered
        btn_okremove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_remove_dark.png")));
    }//GEN-LAST:event_btn_okremoveMouseEntered
    private void btn_okremoveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_okremoveMouseExited
        btn_okremove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_remove_light.png")));
    }//GEN-LAST:event_btn_okremoveMouseExited
    private void btn_okremoveMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_okremoveMousePressed
        btn_okremove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_remove_darker.png")));
    }//GEN-LAST:event_btn_okremoveMousePressed

    private void btn_rembackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_rembackMouseClicked
        panel_remove.hide();
       panel_adminmain.show();
    }//GEN-LAST:event_btn_rembackMouseClicked
    private void btn_rembackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_rembackMouseEntered
        btn_remback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_dark.png")));
    }//GEN-LAST:event_btn_rembackMouseEntered
    private void btn_rembackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_rembackMouseExited
        btn_remback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png")));
    }//GEN-LAST:event_btn_rembackMouseExited
    private void btn_rembackMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_rembackMousePressed
        btn_remback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_darker.png")));
    }//GEN-LAST:event_btn_rembackMousePressed

    private void btn_backMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backMouseClicked
        panel_create.hide();
        panel_adminmain.show();
        combo_course.removeAll();
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/bgd_update.png")));
    }//GEN-LAST:event_btn_backMouseClicked
    private void btn_backMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backMouseEntered
        btn_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_dark.png")));
    }//GEN-LAST:event_btn_backMouseEntered
    private void btn_backMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backMouseExited
        btn_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_light.png")));
    }//GEN-LAST:event_btn_backMouseExited
    private void btn_backMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_backMousePressed
        btn_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_back_darker.png")));
    }//GEN-LAST:event_btn_backMousePressed

    private void btn_logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_logoutMouseClicked
        logout();
    }//GEN-LAST:event_btn_logoutMouseClicked

    private void btn_logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_logoutMouseEntered
        btn_logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_log_dark.png")));
    }//GEN-LAST:event_btn_logoutMouseEntered

    private void btn_logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_logoutMouseExited
        btn_logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_log_light.png")));
    }//GEN-LAST:event_btn_logoutMouseExited

    private void btn_logoutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_logoutMousePressed
        btn_logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pics/opt_log_darker.png")));
    }//GEN-LAST:event_btn_logoutMousePressed

    private void field_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_userActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field_userActionPerformed

    /**
     *
     * @param args
     */
    public static void main(String args[]) {   
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bgd_faculty_names;
    private javax.swing.JLabel btn_admin;
    private javax.swing.JLabel btn_back;
    private javax.swing.JLabel btn_back1;
    private javax.swing.JLabel btn_back2;
    private javax.swing.JLabel btn_back3;
    private javax.swing.JLabel btn_backgrade;
    private javax.swing.JLabel btn_backhome;
    private javax.swing.JLabel btn_create;
    private javax.swing.JButton btn_createconfirm;
    private javax.swing.JButton btn_createreset;
    private javax.swing.JButton btn_editok;
    private javax.swing.JLabel btn_logout;
    private javax.swing.JLabel btn_okremove;
    private javax.swing.JButton btn_payment;
    private javax.swing.JLabel btn_paymententer;
    private javax.swing.JLabel btn_reglist;
    private javax.swing.JLabel btn_remback;
    private javax.swing.JButton btn_removeaccnt;
    private javax.swing.JLabel btn_search;
    private javax.swing.JLabel btn_studbal;
    private javax.swing.JLabel btn_student;
    private javax.swing.JLabel btn_studgrade;
    private javax.swing.JLabel btn_studhome;
    private javax.swing.JLabel btn_studprofile;
    private javax.swing.JLabel btn_update;
    private javax.swing.JButton btn_upload;
    private javax.swing.JComboBox combo_course;
    private javax.swing.JTextField field_address;
    private javax.swing.JTextField field_bal;
    private javax.swing.JTextField field_fname;
    private javax.swing.JTextField field_id;
    private javax.swing.JTextField field_lname;
    private javax.swing.JTextField field_mname;
    private javax.swing.JTextField field_nname;
    private javax.swing.JTextField field_number;
    private javax.swing.JTextField field_paymententer;
    private javax.swing.JTextField field_remove;
    private javax.swing.JTextField field_search;
    private javax.swing.JTextField field_user;
    private javax.swing.JLabel img_pic;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_balance;
    private javax.swing.JLabel lbl_balcourse;
    private javax.swing.JLabel lbl_baldown1;
    private javax.swing.JLabel lbl_balfinals1;
    private javax.swing.JLabel lbl_ballpayfull;
    private javax.swing.JLabel lbl_balmidterm1;
    private javax.swing.JLabel lbl_combo;
    private javax.swing.JLabel lbl_currentbalance;
    private javax.swing.JLabel lbl_errorlog;
    private javax.swing.JLabel lbl_faculty_title;
    private javax.swing.JLabel lbl_nameoutput;
    private javax.swing.JLabel lbl_passcheck;
    private javax.swing.JLabel lbl_searchoutput;
    private javax.swing.JLabel lbl_studaddress;
    private javax.swing.JLabel lbl_studcourse;
    private javax.swing.JLabel lbl_studdept;
    private javax.swing.JLabel lbl_studfname;
    private javax.swing.JLabel lbl_studlname;
    private javax.swing.JLabel lbl_studmname;
    private javax.swing.JLabel lbl_studno;
    private javax.swing.JLabel lbl_studnumber;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JLabel opt_back;
    private javax.swing.JLabel opt_btn3;
    private javax.swing.JLabel opt_btn3_log;
    private javax.swing.JLabel opt_btn3_studmain;
    private javax.swing.JLabel opt_btn_about;
    private javax.swing.JLabel opt_btn_about_log;
    private javax.swing.JLabel opt_btn_about_studmain;
    private javax.swing.JLabel opt_btn_credits;
    private javax.swing.JLabel opt_btn_credits_log;
    private javax.swing.JLabel opt_btn_credits_studmain;
    private javax.swing.JLabel opt_close;
    private javax.swing.JLabel opt_close_log;
    private javax.swing.JLabel opt_close_studmain;
    private javax.swing.JLabel opt_exit;
    private javax.swing.JLabel opt_exit_log;
    private javax.swing.JLabel opt_exit_studmain;
    private javax.swing.JLabel opt_label;
    private javax.swing.JLabel opt_label_log;
    private javax.swing.JLabel opt_label_studmain;
    private javax.swing.JLabel opt_next;
    private javax.swing.JLabel opt_open;
    private javax.swing.JLabel opt_open_log;
    private javax.swing.JLabel opt_open_studmain;
    private javax.swing.JPanel panel_admingrade;
    private javax.swing.JPanel panel_adminmain;
    private javax.swing.JPanel panel_choice;
    private javax.swing.JPanel panel_create;
    private javax.swing.JPanel panel_login;
    private javax.swing.JPanel panel_payment;
    private javax.swing.JPanel panel_records;
    private javax.swing.JPanel panel_remove;
    private javax.swing.JPanel panel_studbal;
    private javax.swing.JPanel panel_studgrade;
    private javax.swing.JPanel panel_studmain;
    private javax.swing.JPanel panel_studprofile;
    private javax.swing.JPanel panel_update;
    private javax.swing.JPasswordField pass1;
    private javax.swing.JPasswordField passfield_confirm;
    private javax.swing.JPasswordField passfield_pass;
    private javax.swing.JLabel pic_rnd;
    private javax.swing.JLabel submitbtn;
    private javax.swing.JPanel tab_panel1;
    private javax.swing.JPanel tab_panel2;
    private javax.swing.JPanel tab_panel3;
    private javax.swing.JPanel tab_panel4;
    private javax.swing.JTabbedPane tab_stud;
    private javax.swing.JTable table_records;
    private javax.swing.JTextField user1;
    // End of variables declaration//GEN-END:variables
}
