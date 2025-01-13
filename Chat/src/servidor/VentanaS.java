package servidor;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Clase que representa a la interfaz gráfica de la aplicación del servidor.
 */
public class VentanaS {

    /**
     * Clase que representa una ventana en el que se puede inciar el servidor.
     */
    private static class VentanaIniciarServidor extends JFrame {

        /**
         * Campo de texto en el que el usuario puede introducir el puerto de escucha del servidor.
         */
        private JTextArea campoPuerto;

        /**
         * Botón de aceptar de la interfaz gráfica.
         */
        private JButton botonAceptar;

        /**
         * Botón de cancelar de la interfaz gráfica.
         */
        private JButton botonCancelar;

        /**
         * Constructor de la ventana en el que se puede inicia el servidor.
         */
        public VentanaIniciarServidor(){
            super();
            setTitle("Servidor");
            setBackground(Color.GRAY);
            campoPuerto = new JTextArea();
            botonAceptar = new JButton("Aceptar");
            botonAceptar.setBackground(Color.LIGHT_GRAY);
            botonCancelar = new JButton("Cancelar");
            botonCancelar.setBackground(Color.LIGHT_GRAY);
            JPanel panelBotones = new JPanel();
            panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
            panelBotones.add(botonAceptar);
            panelBotones.add(botonCancelar);
            campoPuerto.setSize(100, 10);
            setSize(550, 300);
            add(new JLabel("Puerto de la conexión: "));
            add(campoPuerto);
            add(panelBotones);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    /**
     * Clase principal del programa del servidor.
     * @param args Argumentos pasados por línea de comandos.
     */
    public static void main(String[] args) {
        new VentanaIniciarServidor();
    }
}
