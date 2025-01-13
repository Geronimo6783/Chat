package servidor;

import java.net.Socket;

/**
 * Clase que representa a un hilo que se encarga de dar servicios a un cliente.
 */
public class HiloCliente extends Thread {

    /**
     * Socket del cliente.
     */
    private Socket socketCliente;

    /**
     * Constructor de un hilo cliente.
     * @param socketCliente Socket del cliente.
     */
    public HiloCliente(Socket socketCliente){
        this.socketCliente = socketCliente;
    }
}
