package ConexionBd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class SolicitarLibro {
    
     /*El orden de la consultas updates y insert a la base de datos sera este 
    
    1-Revisa si el correo es el correcto 
    2-Revisa si el usuario debe libros 
       2.1Si el usuariario debe libros entonces cerrara el programa y no procesara la peticion
       2.2Si el usuario no debe libros entonces continuara con la siguiente consulta 
    3-Revisa si el libro esta en la base de datos 
    4-Si el libro esta en almacen o disponibles para prestar 
      4.1 si hay libros, entrega el libro 
      4.1.1Actualiza la base de datos del libro cambiando el valor de cuantos libros hay en almacen 
      4.1.2 Actualiza la base de datos del usuario agregando el nombre del libro en la seccion de libros prestados 
               para que cuando vuelva a pedir un llibro a este no se procese el prestamo 
      5.Termina el proceso 
    
     */
//JTextField correo, JPasswordField clave, JTextField idUsuario, JTextField libro, JTextField autor
    public void solicitarLibro(JTextField idUsuario,JTextField correo,JPasswordField clave,JTextField libro,JTextField autor) {
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;

            ConexionBd.Conexion objetoConexion = new ConexionBd.Conexion();
            //1-Revisa si el correo es el correcto 
            String consulta1 = "SELECT correo,clave,idUsuario,nombreLibro FROM biblio_usuarios.almacenamiento WHERE correo=? AND clave=? AND idUsuario=?;";
            ps = objetoConexion.establecerConexion().prepareStatement(consulta1);

            String contra = String.valueOf(clave.getPassword());
            ps.setString(1, correo.getText());
            ps.setString(2, contra);
            ps.setString(3, idUsuario.getText());

            rs = ps.executeQuery();
            if (rs.next()) {
                String libroDeuda = rs.getString("nombreLibro");
                String vacio = "";
                if (vacio.equals(libroDeuda)) {
                    //2-Revisa si el usuario debe libros 
                    String consulta2 = "SELECT nombre,autor,libros_disponibles FROM biblio_usuarios.libros WHERE nombre=? AND autor=? ;";
                    ps = objetoConexion.establecerConexion().prepareStatement(consulta2);

                    ps.setString(1, libro.getText());
                    ps.setString(2, autor.getText());

                    rs = ps.executeQuery();

                    //String libNombre = rs.getString("nombre");
                    //JOptionPane.showMessageDialog(null, "nombre del libro de la base de datos ");
                    if (rs.next()) {

                        String libroCant = rs.getString("libros_disponibles");
                        int cantidadLib = Integer.valueOf(libroCant);
                        //El siguiente if dira que si hay una cantidad mayor a 0 entonces dara un libro
                        if (cantidadLib >= 1) {
                            //dentro de este if se actualizara la base de datos de los libros y de los usuarios 
                            JOptionPane.showMessageDialog(null, "En un momento le entregaremos el libro ");
                            //Para poder agregar el nombre del libro al usuario primero debemos obtener el nombre del libro, en la ultima consulta se agrega 
                            String libNombre = rs.getString("nombre");
                            
                            //Se prepara la siguiente consulta o en este caso un UPDATE
                            cantidadLib = cantidadLib - 1;
                            String librosDisp = String.valueOf(cantidadLib);
                            //Esta UPDATE es par que se reste un libro de los libros que hay en stock 
                            String consulta3 = "UPDATE biblio_usuarios.libros SET libros_disponibles=? WHERE nombre=?;";
                            ps = objetoConexion.establecerConexion().prepareStatement(consulta3);

                            ps.setString(1, librosDisp);
                            ps.setString(2, libro.getText());

                            int numFilas = ps.executeUpdate();
                            
                            if (numFilas> 0 ){
                                JOptionPane.showMessageDialog(null, "Libro actualizado");
                                //Esta consulta es para agregar el nombre del libro a al prefil del usuario y se mostrara en las tablas de afuera 
                                String consulta4="UPDATE biblio_usuarios.almacenamiento SET nombreLibro=? WHERE correo=? AND idUsuario=?;";
                                ps=objetoConexion.establecerConexion().prepareStatement(consulta4);
                                JOptionPane.showMessageDialog(null, libNombre);
                                ps.setString(1,libNombre);
                                ps.setString(2, correo.getText());
                                ps.setString(3, idUsuario.getText());
                                
                                int upda;
                                upda=ps.executeUpdate();
                                if (upda > 0){
                                    JOptionPane.showMessageDialog(null, "Ahora debes un libro");
                                }
                            }
                            

                            

                        } else {
                            JOptionPane.showMessageDialog(null, "Usted debe: " + libroDeuda);
                        }

                    }

                }//Fin del if para ver si no debes ningun libro 
                else {
                    //else si es que debes algun libro 
                    JOptionPane.showMessageDialog(null, "Debes el libro de: " + libroDeuda);
                }
            }

        } catch (Exception e) {

        }
    }
    

}
