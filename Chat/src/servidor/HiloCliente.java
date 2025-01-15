package servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.io.IOException;

/**
 * Clase que representa a un hilo que se encarga de dar servicios a un cliente.
 */
public class HiloCliente extends Thread {

    /**
     * Socket de recepción de datagramas.
     */
    private DatagramSocket socketRecepcionDatagramas;

    /**
     * Socket del cliente.
     */
    private Socket socketCliente;

    /**
     * Indica si el hilo cliente está ejecutándose.
     */
    private boolean ejecutandose;

    /**
     * Búfer de lectura y escritura a través de la red.
     */
    private byte[] buferDatagramas = new byte[256];

    /**
     * Gestor de paquetes de datagramas de entrada.
     */
    private DatagramPacket datagramaEntrada = new DatagramPacket(buferDatagramas, buferDatagramas.length);

    /**
     * Constructor de un hilo cliente.
     * @param socketCliente Socket del cliente.
     * @throws SocketException Cuando no se ha podido crear el socket para la recepción de datagramas.
     */
    public HiloCliente(Socket socketCliente, ServerSocket socketServidor) throws SocketException{
        this.socketCliente = socketCliente;
        ejecutandose = false;
        socketRecepcionDatagramas = new DatagramSocket(socketServidor.getLocalSocketAddress());
    }

    /**
     * Permite saber si el hilo cliente está ejecutándose.
     * @return Si el hilo cliente está ejectándose.
     */
    public boolean isEjecutandose() {
        return ejecutandose;
    }

    /**
     * Permite establecer si el hilo cliente está ejecutándose.
     * @param ejecutandose Valor a establecer de si el hilo cliente está ejecutándose o no.l
     */
    public void setEjecutandose(boolean ejecutandose) {
        this.ejecutandose = ejecutandose;
    }

    @Override
    public void run(){
        byte[] ip;
        byte[] mensaje;
        DatagramPacket datagramaSalida;

        while(ejecutandose){
            try{
                socketRecepcionDatagramas.receive(datagramaEntrada);
                ip = Arrays.copyOfRange(buferDatagramas, 0, 3);
                mensaje = Arrays.copyOfRange(buferDatagramas, 4, buferDatagramas.length);
                if(ip.equals(socketCliente.getInetAddress().getAddress())){
                    datagramaSalida = new DatagramPacket(mensaje, mensaje.length, socketCliente.getInetAddress(), socketCliente.getPort());
                    socketRecepcionDatagramas.send(datagramaSalida);
                }
            }
            catch(IOException e){

            }

        }
    }
}
