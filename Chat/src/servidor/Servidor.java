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
    private static ArrayList<HiloCliente> clientes;

    /**
     * Construtor de un servidor del servicio de chat.
     * @param puerto Puerto de escucha del servidor.
     * @throws IOException Cuando no se puede abrir el socket.
     */
    public Servidor(int puerto) throws IOException{
        this.socketServidor = new ServerSocket(puerto);
        this.servidorEjecutandose = true;
        Servidor.clientes = new ArrayList<>();
    }

    /**
     * Permite establecer si el servidor está ejecutándose o no.
     * @param servidorEjecutandose Valor a establecer de si el servidor está ejecutándose o no.
     */
    public void setServidorEjecutandose(boolean servidorEjecutandose){
        this.servidorEjecutandose = servidorEjecutandose;
    }

    /**
     * Notifica a todos los cliente del nuevo cliente conectado.
     * @param ip Dirección del nuevo cliente conectado.
     * @param nombre Nombre del nuevo cliente conectado.
     */
    public static void mandarNuevaConexionCliente(byte[] ip, byte[] nombre){
        for(HiloCliente hilo : clientes){
            hilo.mandarNuevaConexion(ip, nombre);
        }
    }

    /**
     * Notifica a todos los clientes del cliente desconectado.
     * @param ip Dirección del cliente desconectado.
     * @param nombre Nombre del cliente desconectado.
     */
    public static void mandarDesconexionCliente(byte[] ip, byte[] nombre){
        for(HiloCliente hilo : clientes){
            hilo.mandarDesconexion(ip, nombre);
        }
    }

    /**
     * Ejecuta el servidor.
     */
    @Override
    public void run(){
        VentanaS.VentanaLog.areaLog.setText("Servidor ejecutándose...");
        while(servidorEjecutandose){
            try{
                clientes.add(new HiloCliente(socketServidor.accept(), socketServidor.getLocalPort()));
                for(HiloCliente cliente : clientes){
                    if(!cliente.isEjecutandose()){
                        cliente.setEjecutandose(true);
                        cliente.start();
                    }
                }
            }
            catch(IOException e){
                e.printStackTrace();
                System.out.println("No se ha podido añadir el cliente.");
            }
        }
    }
}
