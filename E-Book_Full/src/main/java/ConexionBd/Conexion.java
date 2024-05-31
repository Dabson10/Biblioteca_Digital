package ConexionBd;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;


public class Conexion {

    private String nombre = "root";
    private String contra = "Dabson12";
    private String ip = "localhost";
    private String puerto = "3306";
    private String baseD = "biblio_usuarios";
    String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + baseD;
    
    
    
    Connection conexion=null;
    
   public Connection establecerConexion(){
       
       try{
           Class.forName("com.mysql.cj.jdbc.Driver");
           conexion=DriverManager.getConnection(cadena,nombre,contra);
           JOptionPane.showMessageDialog(null, "Conexion establecida");
           
       }catch(Exception e){
           JOptionPane.showMessageDialog(null,"No se pudo establecer la conexion"); 
       }
       
       
       return conexion;
   }    

}
