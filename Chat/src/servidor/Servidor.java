package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Clase que representa al servidor del sistema de chat.
 */
public class Servidor extends Thread {

    /**
     * Socket del servidor.
     */
    private ServerSocket socketServidor;

    /**
     * Indica si el servidor está ejecutándose o no.
     */
    private boolean servidorEjecutandose;

    /**
     * Clientes conectados al servidor.
     */
    private ArrayList<HiloCliente> clientes;

    /**
     * Construtor de un servidor del servicio de chat.
     * @param puerto Puerto de escucha del servidor.
     * @throws IOException Cuando no se puede abrir el socket.
     */
    public Servidor(int puerto) throws IOException{
        this.socketServidor = new ServerSocket(puerto);
        this.servidorEjecutandose = true;
        this.clientes = new ArrayList<>();
    }

    /**
     * Permite establecer si el servidor está ejecutándose o no.
     * @param servidorEjecutandose Valor a establecer de si el servidor está ejecutándose o no.
     */
    public void setServidorEjecutandose(boolean servidorEjecutandose){
        this.servidorEjecutandose = servidorEjecutandose;
    }

    /**
     * Ejecuta el servidor.
     */
    @Override
    public void run(){
        while(servidorEjecutandose){
            try{
                clientes.add(new HiloCliente(socketServidor.accept()));
            }
            catch(IOException e){
                
            }
        }
    }
}
