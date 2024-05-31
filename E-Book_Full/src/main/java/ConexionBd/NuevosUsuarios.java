package ConexionBd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class NuevosUsuarios {

     public void nuevoUsuario(JTextField usuario1, JTextField correo1, JPasswordField secreto1, JPasswordField secreto2) {

        try {
            PreparedStatement ps = null;
            ResultSet rs = null;

            ConexionBd.Conexion objetoCall = new ConexionBd.Conexion();
            //Esta consulta es para que al iniciar sesion se agregen los datos del usuario en la base de datos 
            String agregar = "INSERT INTO biblio_usuarios.almacenamiento(nombre,correo,clave,idUsuario,nombreLibro) VALUES (?,?,?,?,?);";
            ps = objetoCall.establecerConexion().prepareStatement(agregar);

            String ClaveC1 = String.valueOf(secreto1.getPassword());
            String ClaveCV1 = String.valueOf(secreto2.getPassword());

            if (ClaveC1.equals(ClaveCV1)) {

                /*Bloque de codigo que genera un numero aleatorio para que el usuario pueda retirar un libro o regresarlo*/
                Set<Integer> numAle = new HashSet<>();
                Random rdm = new Random();

                int cantidadNumeros = 1;
                while (numAle.size() < cantidadNumeros) {
                    int numero = rdm.nextInt(1000);
                    numAle.add(numero);
                }

                String num = String.valueOf(numAle.iterator().next());
                //Cuando no se ingresa el valor vacio en nombreLibro , al querer pedri un libro no te dejara ya que es null por eso mismo 
                //se crea un espacio vacio para que el usuario pueda solicitar su libro 
                String libroNom = "";

                ps.setString(1, usuario1.getText());
                ps.setString(2, correo1.getText());
                ps.setString(3, ClaveC1);
                ps.setString(4, num);
                ps.setString(5,libroNom);

                int numAgregados;
                numAgregados = ps.executeUpdate();

                if (numAgregados > 0) {
                    JOptionPane.showMessageDialog(null, "Agregado correctamente");

                    JOptionPane.showMessageDialog(null, "Hola " + usuario1.getText() + ", tu id es: " + num);

                    

                }
            } else {
                JOptionPane.showMessageDialog(null, "Las contraseñas no son las mismas ");
                
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo agregar nada ");
        }

    }
    
}
