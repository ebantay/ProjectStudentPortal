/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testlogin;


import java.sql.*;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class mysqlconnect {
    Connection conn =null;
    
    public static Connection Connectdb(){
        try{                                                          ////Query connect
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/login","root",""); // connect to DB 
             return conn;//if connected to dB successfully    
             
        }       
        catch(Exception e){
           JOptionPane.showMessageDialog(null, "NOT CONNECTED!"+"("+e+")");        // else if failed to connect to dB 
           return null;                                           //exit program
        }       
        
    }
}
   


