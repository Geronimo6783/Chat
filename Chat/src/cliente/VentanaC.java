package cliente;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Clase que representa a la interfaz gráfica de la aplicación del cliente.
 */
public class VentanaC {

    /**
     * Clase que se encarga a la ventana de configuración en la que
     * el usuario pone la ip y puerto del servidor y su nombre en el chat.
     */
    public static class VentanaConfiguracion {

        /**
         * Campo de texto donde se introduce la ip del servidor.
         */
        public static JTextField ipServidor = new JTextField();

        /**
         * Campo de texto donde se introduce el puerto de conexión con el servidor.
         */
        public static JTextField puertoConexion = new JTextField();

        /**
         * Campo de texto donde se pone que tendrá el cliente en el chat.
         */
        public static JTextField nombreCliente = new JTextField();

        /**
         * Ventana de la configuración.
         */
        public static JFrame ventana = new JFrame("Configuraciones de la comunicación");

        /**
         * Botón de aceptar.
         */
        public static JButton aceptar = new JButton("Aceptar");

        /**
         * Botón de cancelar.
         */
        public static JButton cancelar = new JButton("Cancelar");

        /**
         * Muestra la ventana de configuración por pantalla.
         */
        public static void mostrarVentanaConfiguracion(){
            ipServidor.setColumns(10);
            puertoConexion.setColumns(10);
            nombreCliente.setColumns(10);

            cancelar.setBackground(Color.WHITE);
            aceptar.setBackground(Color.WHITE);

            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
            ventana.setBounds(300, 300, 500, 250);
            ventana.setResizable(false);

            JPanel panelIpServidor = new JPanel();
            panelIpServidor.add(new JLabel("Ip del Servidor: "));
            panelIpServidor.add(Box.createHorizontalStrut(50));
            panelIpServidor.add(ipServidor);

            JPanel panelPuertoConexion = new JPanel();
            panelPuertoConexion.add(new JLabel("Puerto de la conexión: "));
            panelPuertoConexion.add(Box.createHorizontalStrut(10));
            panelPuertoConexion.add(puertoConexion);

            JPanel panelNombreCliente = new JPanel();
            panelNombreCliente.add(new JLabel("Escriba su nombre: "));
            panelNombreCliente.add(Box.createHorizontalStrut(30));
            panelNombreCliente.add(nombreCliente);

            JPanel panelBotones = new JPanel();
            panelBotones.add(Box.createHorizontalStrut(300));
            panelBotones.add(cancelar);
            panelBotones.add(aceptar);

            ventana.add(panelIpServidor);
            ventana.add(panelPuertoConexion);
            ventana.add(panelNombreCliente);
            ventana.add(panelBotones);
            
            ventana.setVisible(true);

            cancelar.addActionListener(
                new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "La configuración inicial de la comunicación ha sido cancelada.", "Operación cancelada", JOptionPane.INFORMATION_MESSAGE);
                        ventana.dispatchEvent(new WindowEvent(ventana, WindowEvent.WINDOW_CLOSING));
                    };
                }
            );
        }
    }

    /**
     * Clase que representa a la ventana del chat.
     */
    public static class VentanaChat {

        /**
         * Área de texto en la que se muestra el historial de las conversaciones.
         */
        public static JTextArea historialConversaciones = new JTextArea();

        /**
         * Desplegable con los clientes con los que se puede chatear.
         */
        public static JComboBox contactosDisponibles = new JComboBox<String>();

        /**
         * Campo de texto en el que se introduce el mensaje.
         */
        public static JTextField textoMensaje = new JTextField();

        /**
         * Botón de enviar mensaje.
         */
        public static JButton enviar = new JButton("Enviar");

        /**
         * Muestra la ventana del chat por pantalla.
         */
        public static void mostrarVentanaChat(){
            JFrame ventana = new JFrame("--- 1 - " + VentanaConfiguracion.nombreCliente.getText() + "---");
            ventana.setBounds(300, 300, 150, 250);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        }
    }

    public static void main(String[] args) {
        VentanaConfiguracion.mostrarVentanaConfiguracion();
    }
}
