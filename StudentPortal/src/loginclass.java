import javax.swing.JOptionPane;
public class loginclass {
    
    public static void main(String args[]){
        String choice,name,user = null,pass = null,loguser,logpass;
        int ch,cond = 0;
        do{  
        choice=JOptionPane.showInputDialog("LOGIN\n\n[1]Sign-up\n[2]Login\n[3]Exit");
        ch=Integer.parseInt(choice);
      
        if (ch==1){
            name=JOptionPane.showInputDialog("Enter desired name:");
            user=JOptionPane.showInputDialog("Enter desired username:");
            pass=JOptionPane.showInputDialog("Enter desired password:");
            
            cond=Integer.parseInt(JOptionPane.showInputDialog("Name:"+name+"\nUsername:"+user+"\nPassword:"+pass+"\n\nEnter [1] to go back to menu"));
        }
        else if (ch==2){
            loguser=JOptionPane.showInputDialog("Enter username");
            logpass=JOptionPane.showInputDialog("Enter password:");
            
            if ((loguser.equals(user))&&(logpass.equals(pass))){
                
                JOptionPane.showMessageDialog(null,"YOU ARE NOW LOGGED IN!");
            }
            
            else
                    JOptionPane.showMessageDialog(null,"ERROR: WRONG USERNAME OR WRONG PASSWORD!");           
            
        }
        else if (ch==3){
            System.exit(0);
             JOptionPane.showMessageDialog(null,"PROGRAM WILL NOW EXIT");
        }
        
        else
            
            JOptionPane.showMessageDialog(null,"INVALID INPUT!");
      
     }
     while(cond==1);
    }
    
}
