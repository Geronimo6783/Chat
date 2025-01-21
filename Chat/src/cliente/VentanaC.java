package cliente;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
     * Hilo cliente que se ejecuta.
     */
    private static Cliente cliente;

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
            ipServidor.setColumns(20);
            puertoConexion.setColumns(20);
            nombreCliente.setColumns(20);

            cancelar.setBackground(Color.WHITE);
            aceptar.setBackground(Color.WHITE);

            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
            ventana.setBounds(300, 300, 500, 250);
            ventana.setResizable(false);

            JPanel panelIpServidor = new JPanel();
            panelIpServidor.add(new JLabel("Ip del Servidor: "));
            panelIpServidor.add(Box.createHorizontalStrut(70));
            panelIpServidor.add(ipServidor);

            JPanel panelPuertoConexion = new JPanel();
            panelPuertoConexion.add(new JLabel("Puerto de la conexión: "));
            panelPuertoConexion.add(Box.createHorizontalStrut(30));
            panelPuertoConexion.add(puertoConexion);

            JPanel panelNombreCliente = new JPanel();
            panelNombreCliente.add(new JLabel("Escriba su nombre: "));
            panelNombreCliente.add(Box.createHorizontalStrut(50));
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
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(ventana, "La configuración inicial de la comunicación ha sido cancelada.", "Operación cancelada", JOptionPane.INFORMATION_MESSAGE);
                        ventana.dispatchEvent(new WindowEvent(ventana, WindowEvent.WINDOW_CLOSING));
                    };
                }
            );

            aceptar.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        try{
                            String nombre = nombreCliente.getText();
                            if(!nombre.isEmpty()){
                                InetAddress direccionServidor = InetAddress.getByName(ipServidor.getText());
                                int puerto = Integer.parseInt(puertoConexion.getText());
                                cliente = new Cliente(direccionServidor, puerto);
                                ventana.setDefaultCloseOperation(JFrame.ICONIFIED);
                                ventana.dispatchEvent(new WindowEvent(ventana, WindowEvent.WINDOW_CLOSING));
                                VentanaChat.mostrarVentanaChat();
                                cliente.start();
                            }
                            else{
                                JOptionPane.showMessageDialog(ventana, "No se ha introducido ningún nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        catch(NumberFormatException ex){        
                            JOptionPane.showMessageDialog(ventana, "El puerto introducido no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        catch(UnknownHostException ex){
                            JOptionPane.showMessageDialog(ventana, "La ip del servidor no es válida.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
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
        public static JComboBox contactosDisponibles = new JComboBox<String>(new String[]{"                                                                                         "});

        /**
         * Campo de texto en el que se introduce el mensaje.
         */
        public static JTextField textoMensaje = new JTextField();

        /**
         * Botón de enviar mensaje.
         */
        public static JButton enviar = new JButton("Enviar");

        /**
         * Ventana del chat.
         */
        public static JFrame ventana = new JFrame("--- 1 - " + VentanaConfiguracion.nombreCliente.getText() + " ---");

        /**
         * Muestra la ventana del chat por pantalla.
         */
        public static void mostrarVentanaChat(){
            enviar.setBackground(Color.WHITE);
            contactosDisponibles.setBackground(Color.WHITE);
            contactosDisponibles.setSize(350, 20);

            textoMensaje.setColumns(30);

            JPanel panelDestinatario = new JPanel();
            JPanel panelMensaje = new JPanel();

            panelDestinatario.add(new JLabel("Destinatario: "));
            panelDestinatario.add(Box.createHorizontalStrut(5));
            panelDestinatario.add(contactosDisponibles);

            panelMensaje.add(textoMensaje);
            panelMensaje.add(Box.createHorizontalStrut(10));
            panelMensaje.add(enviar);

            ventana.setBounds(200, 200, 450, 750);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
            ventana.setBackground(Color.GRAY);

            historialConversaciones.setEditable(false);
            historialConversaciones.setSize(450, 550);
            ventana.add(historialConversaciones);
            ventana.add(panelDestinatario);
            ventana.add(panelMensaje);

            ventana.setResizable(false);
            ventana.setVisible(true);

            enviar.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        byte[] mensajeEnviar = textoMensaje.getText().getBytes();
                        textoMensaje.setText(" ");
                        cliente.buferDatagramas[0] = 2;
                        byte[] direccionDestinatario = cliente.direccionesClientesDisponibles.get(cliente.nombreClientesDisponibles.indexOf(contactosDisponibles.getSelectedItem())).getAddress();

                        for(int i = 1; i < cliente.buferDatagramas.length; i++){
                            if(i < 5){
                                cliente.buferDatagramas[i] = direccionDestinatario[i - 1];
                            }
                            else{
                                cliente.buferDatagramas[i] = mensajeEnviar[i - 5];
                            }
                        }

                        DatagramPacket paqueteEnvio = new DatagramPacket(cliente.buferDatagramas, cliente.buferDatagramas.length, cliente.socketServidor.getInetAddress(), cliente.socketServidor.getPort());
                        
                        try{
                            cliente.socketDatagramas.send(paqueteEnvio);
                        }
                        catch(IOException ex){
                            JOptionPane.showMessageDialog(ventana, "No se ha podido enviar el mensaje.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            );

            ventana.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e){
                        cliente.buferDatagramas[0] = 1;

                        DatagramPacket paqueteEnvio = new DatagramPacket(cliente.buferDatagramas, cliente.buferDatagramas.length, cliente.socketServidor.getInetAddress(), cliente.socketServidor.getPort());
                        
                        try{
                            cliente.socketDatagramas.send(paqueteEnvio);
                        }
                        catch(IOException ex){

                        }
                    }
                }
            );
        }
    }

    /**
     * Método principal del porgrama de chat en el entorno cliente.
     * @param args Argumentos pasados por línea de comandos.
     */
    public static void main(String[] args) {
        VentanaConfiguracion.mostrarVentanaConfiguracion();
    }
}
