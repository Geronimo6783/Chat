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
     * Socket de datagramas con los clientes.
     */
    private DatagramSocket socketDatagramas;

    /**
     * Socket del cliente.
     */
    private final Socket socketCliente;

    /**
     * Nombre con el que se conectó el cliente.
     */
    private volatile String nombre;

    /**
     * Indica si el hilo cliente está ejecutándose.
     */
    private volatile boolean ejecutandose;

    /**
     * Búfer de lectura y escritura a través de la red.
     */
    private volatile byte[] buferDatagramas = new byte[1024];

    /**
     * Gestor de paquetes de datagramas de entrada.
     */
    private final DatagramPacket datagramaEntrada = new DatagramPacket(buferDatagramas, buferDatagramas.length);

    /**
     * Objeto de bloqueo para la sincronización de hilos.
     */
    private static final Object bloqueo = new Object();

    /**
     * Constructor de un hilo cliente.
     * @param socketCliente Socket del cliente.
     * @throws SocketException Cuando no se ha podido crear el socket para la recepción de datagramas.
     */
    public HiloCliente(Socket socketCliente) throws SocketException{
        this.socketCliente = socketCliente;
        ejecutandose = false;
        this.socketDatagramas = new DatagramSocket();
        this.socketDatagramas.connect(socketCliente.getLocalSocketAddress());

        byte[] puerto = Integer.toString(this.socketDatagramas.getLocalPort()).getBytes();
        DatagramPacket paquetePuerto = new DatagramPacket(puerto, puerto.length);

        try{
            this.socketDatagramas.send(paquetePuerto);
        }
        catch(IOException e){

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
     * @param ejecutandose Valor a establecer de si el hilo cliente está ejecutándose o no.
     */
    public void setEjecutandose(boolean ejecutandose) {
        this.ejecutandose = ejecutandose;
    }

    /**
     * Manda que la dirección y nombre del nuevo cliente en el chat.
     * @param ip Dirección del nuevo cliente en el chat.
     * @param nombre Nombre del nuevo cliente en el chat.
     */
    public void mandarNuevaConexion(byte[] ip, byte[] nombre){
        synchronized(bloqueo){
            byte[] datagramaEnviar = new byte[ip.length + nombre.length + 1];
            DatagramPacket datagramaSalida;
            datagramaEnviar[0] = 0;

            for(int i = 1; i < datagramaEnviar.length; i++){
                if(i < 5){
                    datagramaEnviar[i] = ip[i - 1];
                }
                else{
                    if((i - 5) < nombre.length){
                        datagramaEnviar[i] = nombre[i - 5];
                    }
                }
            }
            
            datagramaSalida = new DatagramPacket(datagramaEnviar, datagramaEnviar.length);

            try{
                socketDatagramas.send(datagramaSalida);
            }
            catch(IOException e){
                VentanaS.VentanaLog.areaLog.setText(VentanaS.VentanaLog.areaLog.getText() + "\nNo se ha podido enviar el datagrama con la nueva conexión a " + nombre + " (" + socketCliente.getLocalAddress().getHostAddress() + ")");
            }
        }
    }

    /**
     * Manda la dirección y nombre del cliente que se ha desconectado del chat.
     * @param ip Dirección del cliente que se ha desconectado.
     * @param nombre Nombre del cliente que se ha desconectado.
     */
    public void mandarDesconexion(byte[] ip, byte[] nombre){
        synchronized(bloqueo){
            byte[] datagramaEnviar = new byte[ip.length + nombre.length + 1];
            DatagramPacket datagramaSalida;
            datagramaEnviar[0] = 1;

            for(int i = 1; i < datagramaEnviar.length; i++){
                if(i < 5){
                    datagramaEnviar[i] = ip[i - 1];
                }
                else{
                    datagramaEnviar[i] = nombre[i - 5];
                }
            }
            
            datagramaSalida = new DatagramPacket(datagramaEnviar, datagramaEnviar.length, socketCliente.getLocalAddress(), socketCliente.getLocalPort());
            
            try{
                socketDatagramas.send(datagramaSalida);
            }
            catch(IOException e){
                VentanaS.VentanaLog.areaLog.setText(VentanaS.VentanaLog.areaLog.getText() + "\nNo se ha podido enviar el datagrama con la desconexión a " + nombre + " (" + socketCliente.getLocalAddress().getHostAddress() + ")");
            }
        }
    }

    /**
     * Permite obtener el nombre que se puso el cliente en el programa.
     * @return Nombre que se puso el cliente en el programa.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Permite obtener el socket del cliente.
     * @return Socket del cliente.
     */
    public Socket getSocketCliente() {
        return socketCliente;
    }

    /**
     * Gestiona la ejecución de un hilo que se encarga de prestar servicios de chat.
     */
    @Override
    public void run(){
        DatagramPacket datagramaSalida;
        byte[] ip = new byte[4];
        byte[] mensaje;
        byte[] datagramaEnviar;
        byte codigoComunicacion;

        while(ejecutandose){
            try{
                socketDatagramas.receive(datagramaEntrada);
                if(datagramaEntrada.getAddress().equals(socketCliente.getInetAddress())){
                    codigoComunicacion = buferDatagramas[0];
                    switch(codigoComunicacion){
                        case 0 -> {
                            nombre = new String(Arrays.copyOfRange(buferDatagramas, 1, buferDatagramas.length));
                            VentanaS.VentanaLog.areaLog.setText(VentanaS.VentanaLog.areaLog.getText() + "\nNuevo cliente " + nombre + ".");
                            Servidor.servidor.mandarNuevaConexionCliente(socketCliente.getInetAddress().getAddress(), nombre.getBytes());
                        }
                        case 1 -> {
                            VentanaS.VentanaLog.areaLog.setText(VentanaS.VentanaLog.areaLog.getText() + "\nEl cliente " + nombre + " se ha desconectado.");
                            Servidor.servidor.mandarDesconexionCliente(socketCliente.getInetAddress().getAddress(), nombre.getBytes());
                            ejecutandose = false;
                        }
                        case 2 -> {
                            ip = Arrays.copyOfRange(buferDatagramas, 1, 5);
                            mensaje = Arrays.copyOfRange(buferDatagramas, 5, buferDatagramas.length);
                            datagramaEnviar = new byte[mensaje.length + 5];
                            datagramaEnviar[0] = 2;

                            for(int i = 1; i < datagramaEnviar.length; i++){
                                if(i < 5){
                                    datagramaEnviar[i] = socketCliente.getInetAddress().getAddress()[i - 1];
                                }
                                else{
                                    datagramaEnviar[i] = mensaje[i - 5];
                                }
                            }
                            datagramaSalida = new DatagramPacket(datagramaEnviar, datagramaEnviar.length, InetAddress.getByAddress(ip), 15000);
                            socketDatagramas.send(datagramaSalida);
                        }
                    }
                }
            }
            catch(IOException e){
                VentanaS.VentanaLog.areaLog.setText(VentanaS.VentanaLog.areaLog.getText() + "\nNo se ha podido recibir el paquete de " + nombre + " (" + socketCliente.getLocalAddress().getHostAddress() + ")");
            }
        }
    }
}