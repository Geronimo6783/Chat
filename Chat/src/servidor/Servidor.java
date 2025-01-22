package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Clase que representa al servidor del sistema de chat.
 */
public class Servidor extends Thread {

    /**
     * Servidor del sistema del chat que se ejecuta.
     */
    public static volatile Servidor servidor;

    /**
     * Socket del servidor.
     */
    private final ServerSocket socketServidor;

    /**
     * Indica si el servidor está ejecutándose o no.
     */
    private volatile boolean servidorEjecutandose;

    /**
     * Clientes conectados al servidor.
     */
    private volatile ArrayList<HiloCliente> clientes ;

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
     * Notifica a todos los cliente del nuevo cliente conectado.
     * @param ip Dirección del nuevo cliente conectado.
     * @param nombre Nombre del nuevo cliente conectado.
     */
    public void mandarNuevaConexionCliente(byte[] ip, byte[] nombre){
        if(clientes.size() > 1){
            HiloCliente hiloNuevo = null;

            for(HiloCliente hilo : clientes){
                if(!hilo.getSocketCliente().getInetAddress().getAddress().equals(ip)){
                    hilo.mandarNuevaConexion(ip, nombre);
                }
                else{
                    hiloNuevo = hilo;
                }
            }

            for(HiloCliente hilo : clientes){
                if(!hilo.getSocketCliente().getInetAddress().getAddress().equals(ip)){
                    hiloNuevo.mandarNuevaConexion(hilo.getSocketCliente().getInetAddress().getAddress(), hilo.getNombre().getBytes());
                }
            }
        }
    }

    /**
     * Notifica a todos los clientes del cliente desconectado.
     * @param ip Dirección del cliente desconectado.
     * @param nombre Nombre del cliente desconectado.
     */
    public void mandarDesconexionCliente(byte[] ip, byte[] nombre){
        if(clientes.size() > 1){
            for(HiloCliente hilo : clientes){
                if(!hilo.getSocketCliente().getInetAddress().getAddress().equals(ip)){
                    hilo.mandarDesconexion(ip, nombre);
                }
            }
        }
    }

    /**
     * Permite obtener el socket del servidor.
     * @return Socket del servidor.
     */
    public ServerSocket getSocketServidor() {
        return socketServidor;
    }

    /**
     * Ejecuta el servidor.
     */
    @Override
    public void run(){
        VentanaS.VentanaLog.areaLog.setText("Servidor ejecutándose...");
        while(servidorEjecutandose){
            try{
                clientes.add(new HiloCliente(socketServidor.accept()));
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
