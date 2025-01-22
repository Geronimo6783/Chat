package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase que representa a un cliente en el sistema del chat.
 */
public class Cliente extends Thread {

    /**
     * Socket del servidor.
     */
    public volatile Socket socketServidor;

    /**
     * Socket de datagramas.
     */
    public volatile DatagramSocket socketDatagramas;

    /**
     * Nombre de los clientes disponibles.
     */
    public volatile ArrayList<String> nombreClientesDisponibles = new ArrayList<>();

    /**
     * Direcciones de los clientes disponibles.
     */
    public volatile ArrayList<InetAddress> direccionesClientesDisponibles = new ArrayList<>();

    /**
     * Indica si el cliente está ejecutándose.
     */
    public volatile boolean ejecutandose = true;

    /**
     * Bufer de datagramas.
     */
    public volatile byte[] buferDatagramas = new byte[256];

    /**
     * Paquete de datagramas usado para la entrada.
     */
    public volatile DatagramPacket datagramaEntrada = new DatagramPacket(buferDatagramas, buferDatagramas.length);

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
            socketDatagramas = new DatagramSocket();
            socketDatagramas.connect(socketServidor.getInetAddress(), socketServidor.getPort());
        }
        catch(SocketException e){

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
                        String nombre = new String(mensaje);
                        nombreClientesDisponibles.add(nombre);
                        direccionesClientesDisponibles.add(InetAddress.getByAddress(ip));
                        VentanaC.VentanaChat.contactosDisponibles.addItem(nombre);
                    }
                    case 1 -> {
                        int indice = direccionesClientesDisponibles.indexOf(InetAddress.getByAddress(ip));
                        String nombreCliente = nombreClientesDisponibles.remove(indice);
                        direccionesClientesDisponibles.remove(indice);
                        VentanaC.VentanaChat.contactosDisponibles.removeItem(nombreCliente);
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
