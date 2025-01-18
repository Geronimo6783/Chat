package servidor;

import javax.swing.JOptionPane;

/**
 * Clase que representa a la interfaz gráfica de la aplicación del servidor.
 */
public class VentanaS {

    /**
     * Clase principal del programa del servidor.
     * @param args Argumentos pasados por línea de comandos.
     */
    public static void main(String[] args) {
        String puerto = JOptionPane.showInputDialog("Puerto de la conexión: ");
        
        try{
            new Servidor(Integer.parseInt(puerto)).start();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido iniciar el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
