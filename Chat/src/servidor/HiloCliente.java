package servidor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.io.IOException;

/**
 * Clase que representa a un hilo que se encarga de dar servicios a un cliente.
 */
public class HiloCliente extends Thread {

    /**
     * Socket de datagramas.
     */
    private static volatile DatagramSocket socketDatagramas;

    /**
     * Indica si el hilo cliente está ejecutándose.
     */
    private volatile boolean ejecutandose;

    /**
     * Búfer de lectura y escritura a través de la red.
     */
    private volatile byte[] buferDatagramas = new byte[256];

    /**
     * Gestor de paquetes de datagramas de entrada.
     */
    private final DatagramPacket datagramaEntrada = new DatagramPacket(buferDatagramas, buferDatagramas.length);

    /**
     * Constructor de un hilo cliente.
     * @param socketCliente Socket del cliente.
     * @throws SocketException Cuando no se ha podido crear el socket para la recepción de datagramas.
     */
    public HiloCliente(Socket socketCliente, int puertoServidor) throws SocketException{
        VentanaS.VentanaLog.areaLog.setText(VentanaS.VentanaLog.areaLog.getText() + "\nNuevo cliente " + socketCliente.getInetAddress().getHostAddress());;
        ejecutandose = false;
        if(socketDatagramas == null){
            HiloCliente.socketDatagramas = new DatagramSocket(puertoServidor);
        }
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

    /**
     * Gestiona la ejecución de un hilo que se encarga de prestar servicios de chat.
     */
    @Override
    public void run(){
        DatagramPacket datagramaSalida;
        byte[] ip = new byte[4];

        while(ejecutandose){
            try{
                socketDatagramas.receive(datagramaEntrada);
                ip = Arrays.copyOfRange(buferDatagramas, 0, 4);
                datagramaSalida = new DatagramPacket(buferDatagramas, buferDatagramas.length, InetAddress.getByAddress(ip), 15000);
                socketDatagramas.send(datagramaSalida);
            }
            catch(IOException e){

            }
        }
    }
}
