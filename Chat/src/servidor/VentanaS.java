package servidor;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * Clase que representa a la interfaz gráfica de la aplicación del servidor.
 */
public class VentanaS {

    /**
     * Clase que se encarga de mostrar la ventana del log del servidor.
     */
    public static class VentanaLog {

        /**
         * Área de texto donde se escribe el log del servidor.
         */
        public static JTextArea areaLog = new JTextArea();

        /**
         * Muestra la ventana que muestra el log del servidor.
         */
        public static void mostrarVentanaLog(){
            JFrame ventana = new JFrame("Log Servidor");
            ventana.setBounds(400, 100, 400, 600);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            areaLog.setEditable(false);
            ventana.add(areaLog);
            ventana.setVisible(true);
        }
    }

    /**
     * Clase principal del programa del servidor.
     * @param args Argumentos pasados por línea de comandos.
     */
    public static void main(String[] args) {
        String puerto = JOptionPane.showInputDialog("Puerto de la conexión: ");
        
        try{
            if(puerto != null){
                new Servidor(Integer.parseInt(puerto)).start();
                VentanaLog.mostrarVentanaLog();
            }
            else{
                JOptionPane.showMessageDialog(null, "Operación cancelada.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido iniciar el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
