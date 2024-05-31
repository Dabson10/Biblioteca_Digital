package ConexionBd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class Verificar {
    
    
    /*El orden de las consulta sera 
    1-Verificar el correo
    2-Verificar si el usuario debe algun libro 
    3-Verificar si el libro existe 
      3.1Si el libro existe se comparara con el libro que ingreso el usuario 
      3.1.1 Si no es el mismo libro se rechaza 
    3.1.2Si es el mismo libro se cepta y continua 
    4.Se elimina la deuda del usuario 
    5.Se registra en la base de datos y se agrega en el espacio de libtos_disponibles de la base de datos 
    */
    
    public void verificarUsuario(JTextField correo, JTextField id, JTextField nombreLib,JTextField autor) {
        
        try{
            PreparedStatement ps = null; 
            ResultSet rs = null;
            
            ConexionBd.Conexion objetoConexion = new ConexionBd.Conexion();
            
            //la primera consulta es para verificar si el correo esta y a su vez se obtiene si es que debe algun libro 
            String consulta1="SELECT correo,idUsuario,nombreLibro FROM biblio_usuarios.almacenamiento WHERE correo=? AND idUsuario = ?;";
            ps=objetoConexion.establecerConexion().prepareStatement(consulta1);
            
            ps.setString(1, correo.getText());
            ps.setString(2, id.getText());
            
            rs=ps.executeQuery();
            if(rs.next()){
                //Se obtiene el nombre del libro que debe desde la base de datos 
                String libroNom = rs.getString("nombreLibro");
                //Se crea una variable String vacia por que si alguien ingresa sus datos y no debe na esto se muestre en pantalla 
                String nada="";
                String libroDeuda=nombreLib.getText();
                /*En el siguiente condicional compara el libro que ingresa el usuario con el que se atrae desde la base de datos 
                y cukple o si son iguales entonces avanzara con la condicional, si no son iguales terminara la devolucion  */
                if(libroDeuda.equals(libroNom)){
                    //Para poder hacer los updates requeridos primero se debe obtener la cantidad de libros que hay en la base de libreria 
                    //y 
                     String consulta2 ="SELECT nombre,autor,libros_disponibles,libros_total FROM biblio_usuarios.libros WHERE nombre=? AND autor=?; ";
                     ps=objetoConexion.establecerConexion().prepareStatement(consulta2);
                     
                     ps.setString(1,nombreLib.getText());
                     ps.setString(2, autor.getText());
                     rs=ps.executeQuery();
                     if(rs.next()){
                         //Las dos siguientes lineas son para obtener el valor de la columna libros_disponibles de la base de datos de librop
                         String cantidad = rs.getString("libros_disponibles");
                         int libDisp =Integer.valueOf(cantidad);
                         //las siguientes dos lineas representan el valor de la columna libros_total de la base de datos libros 
                         String totalLib = rs.getString("libros_total");
                         int libTot = Integer.valueOf(totalLib);      
                         
                         //Ahora dentro de un if se debe de hacer la suma de libro que se devolvera
                         //pero se utilizara la cantidad total de libros que tiene la libreria en almacen y prestado osea la totalidad de estos 
                         //para poder hacer este if correctamente se le debe de restar uno a la cantidad total para que no se almacenen de mas y sean los exactos 
                         libTot=libTot-1;
                         //Osea si hay menos de o 24 entonces procedera 
                         if(libDisp <= libTot){
                             //Ahora si se hace el update respectivo a libros 
                             
                             String consulta3 = "UPDATE biblio_usuarios.libros SET libros_disponibles=? WHERE nombre=? AND autor=?;";
                             ps=objetoConexion.establecerConexion().prepareStatement(consulta3);
                             libDisp=libDisp+1;
                             String cantLib =String.valueOf(libDisp);
                             
                             ps.setString(1, cantLib);
                             ps.setString(2, nombreLib.getText());
                             ps.setString(3, autor.getText());
                             
                             int numFilas1=ps.executeUpdate();
                             if(numFilas1>0){
                                 JOptionPane.showMessageDialog(null, "Se actualizo la base de datos ");
                                 //Se hace la ultima consulta o actualizacion en donde se borra la deuda del usuario 
                                 String consulta4="UPDATE biblio_usuarios.almacenamiento SET nombreLibro=? WHERE correo=? AND idUsuario=?; ";
                                 ps=objetoConexion.establecerConexion().prepareStatement(consulta4);
                                 //para limpiar el nombre del usuario solamente se crea una variable string sin ningun valor solo comillas 
                                 String vacio="";
                                 ps.setString(1, vacio);
                                 ps.setString(2, correo.getText());
                                 ps.setString(3, id.getText());
                                 
                                 
                                 int numFilas2=ps.executeUpdate();
                                 if(numFilas2 >0){
                                     JOptionPane.showMessageDialog(null, "Ahora no debes ningun libro. Gracias  <3");
                                 }
                             }else{
                                 JOptionPane.showMessageDialog(null, "No se actualizo la base ");
                             }
                             
                             
                             
                         }else{
                              libTot=libTot+1;
                              JOptionPane.showMessageDialog(null, "La cantidad de libros total es: "+libTot);
                         }
                  
                     }
                     
                     
                }else if (libroNom.equals(nada)){
                     JOptionPane.showMessageDialog(null, "Usted no debe nada");
                }
                else{
                     JOptionPane.showMessageDialog(null, "Ingrese el libro que debe");
                }
            }
            
        }catch(Exception e ){
            JOptionPane.showMessageDialog(null, "Te quedaste en el inicio");
        }
        
        
    }
    
}
