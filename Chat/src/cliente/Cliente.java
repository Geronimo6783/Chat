package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JComboBox;

/**
 * Clase que representa a un cliente en el sistema del chat.
 */
public class Cliente extends Thread {

    /**
     * Socket del servidor.
     */
    public Socket socketServidor;

    /**
     * Socket de datagramas.
     */
    public DatagramSocket socketDatagramas;

    /**
     * Nombre de los clientes disponibles.
     */
    public ArrayList<String> nombreClientesDisponibles = new ArrayList<>();

    /**
     * Direcciones de los clientes disponibles.
     */
    public ArrayList<InetAddress> direccionesClientesDisponibles = new ArrayList<>();

    /**
     * Indica si el cliente está ejecutándose.
     */
    public boolean ejecutandose = true;

    /**
     * Bufer de datagramas.
     */
    public byte[] buferDatagramas = new byte[256];

    /**
     * Paquete de datagramas usado para la entrada.
     */
    public DatagramPacket datagramaEntrada = new DatagramPacket(buferDatagramas, buferDatagramas.length);

    /**
     * Constructor de clientes.
     * @param ipServidor Ip del servidor.
     * @param puertoServidor Puerto del servidor.
     */
    public Cliente(InetAddress ipServidor, int puertoServidor){
        try{
            socketServidor = new Socket(ipServidor, puertoServidor);
        }
        catch(IOException e){

        }

        try{
            socketDatagramas = new DatagramSocket(15000);
        }
        catch(SocketException e){

        } 

        buferDatagramas[0] = 0;
        byte[] nombreCliente = VentanaC.VentanaConfiguracion.nombreCliente.getText().getBytes();
        
        for(int i = 0; i < nombreCliente.length; i++){
            buferDatagramas[i + 1] = nombreCliente[i];
        }

        DatagramPacket paqueteEnvio = new DatagramPacket(buferDatagramas, buferDatagramas.length, socketServidor.getInetAddress(), socketServidor.getPort());
        
        try{
            socketDatagramas.send(paqueteEnvio);
        }
        catch(IOException e){

        }
    }

    /**
     * Método que ejecuta el hilo del cliente.
     */
    public void run(){
        byte codigoComunicacion;
        byte[] ip;
        byte[] mensaje;

        while(ejecutandose){
            try{
                socketDatagramas.receive(datagramaEntrada);
                codigoComunicacion = buferDatagramas[0];
                ip = Arrays.copyOfRange(buferDatagramas, 1, 5);
                mensaje = Arrays.copyOfRange(buferDatagramas, 5, buferDatagramas.length);

                switch(codigoComunicacion){
                    case 0 -> {
                        nombreClientesDisponibles.add(new String(mensaje));
                        direccionesClientesDisponibles.add(InetAddress.getByAddress(ip));
                        VentanaC.VentanaChat.contactosDisponibles = new JComboBox<>(nombreClientesDisponibles.toArray());
                    }
                    case 1 -> {
                        int indice = direccionesClientesDisponibles.indexOf(InetAddress.getByAddress(ip));
                        nombreClientesDisponibles.remove(indice);
                        direccionesClientesDisponibles.remove(indice);
                        VentanaC.VentanaChat.contactosDisponibles = new JComboBox<>(nombreClientesDisponibles.toArray());
                    }
                    case 2 -> {
                        String nombreRemitente = nombreClientesDisponibles.get(direccionesClientesDisponibles.indexOf(InetAddress.getByAddress(ip)));
                        VentanaC.VentanaChat.historialConversaciones.setText(VentanaC.VentanaChat.historialConversaciones.getText() + "\n===== " + nombreRemitente + " =====\n" + new String(mensaje));
                    }
                }
            }
            catch(IOException e){

            }
        }
    }
}
