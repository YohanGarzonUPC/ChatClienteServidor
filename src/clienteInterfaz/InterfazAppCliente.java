/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clienteInterfaz;

import java.awt.Color;
import javax.swing.JFrame;
import clienteControlador.ControladorCliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import util.Util;

/**
 *
 * @author User
 */
public class InterfazAppCliente extends JFrame {
    // Relaciones

    private ControladorCliente ctrl;

    // Atributos   
    private String ipCliente;
    private String IpServer;
    private String usuario;
    private int PuertoLlegada;
    private int PuertoSalida;
    private Envia enviapanel;
    private ConversacionCliente conversacionpanel;
    private Usuarios usuariospanel;

    public void lectorConfigCliente() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("data\\configCliente.txt"));
            String linea;
            String[] datos = new String[5];
            int i = 0;
            while ((linea = br.readLine()) != null) {
                datos[i] = linea;
                i++;
            }
            br.close();
            this.usuario = datos[0];
            this.ipCliente = datos[1];
            this.IpServer = datos[2];
            this.PuertoLlegada = Integer.parseInt(datos[3]);
            this.PuertoSalida = Integer.parseInt(datos[4]);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un problema");
        }
    }

    // Constructor
    public InterfazAppCliente() {
        lectorConfigCliente();

        getContentPane().setLayout(null);

        // Enlaza el controlador	  
        this.ctrl = new ControladorCliente(IpServer, PuertoLlegada, PuertoSalida);
         ctrl.socket("1 " + usuario + " " + ipCliente);

        setTitle("Chat Cliente");
        while (true) {
            if (ctrl.returnMsgServidor() != null) {
                usuariospanel = new Usuarios();
                ctrl.EliminarMsgServidor();
                usuariospanel.setBounds(10, 10, 450, 75);
                add(usuariospanel);

                conversacionpanel = new ConversacionCliente();
                conversacionpanel.setBounds(10, 95, 450, 550);
                add(conversacionpanel);

                enviapanel = new Envia();
                enviapanel.setBounds(10, 655, 450, 80);
                add(enviapanel);

                getContentPane().setBackground(Color.white);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                setSize(485, 805);
                setResizable(false);

                Util.centrarVentana(this);
                setVisible(true);
                break;
            }
        }

        ActionListener btnEnviar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!enviapanel.MsgTexto().equals("")) {
                    String encriptado = encriptar(enviapanel.MsgTexto());
                    ctrl.socket("3 " + usuariospanel.usuarioSelecionado() + " " + usuario + " " + encriptado);
                    enviapanel.setMsgTexto("");
                }
            }
        };
        enviapanel.jButton1.addActionListener(btnEnviar);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ctrl.socket("4 " + usuario);
                System.exit(0);
            }
        });

        ActionListener btnChat = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ctrl.socket("0 " + usuario);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(InterfazAppCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                String msgServerTemp = ctrl.returnMsgServidor();
                if (msgServerTemp.startsWith("0")) {
                    String[] Arraytemp = msgServerTemp.split(" ");
                    String[] usuario = new String[Arraytemp.length - 1];

                    for (int i = 0; i < usuario.length; i++) {
                        usuario[i] = Arraytemp[i + 1];
                    }
                    usuariospanel.setAmigos(usuario);
                    ctrl.EliminarMsgServidor(msgServerTemp);
                }
            }
        };
        usuariospanel.jButton1.addActionListener(btnChat);

        String chat = "";
        
       
        while (true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(InterfazAppCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            String msgServerTemp = ctrl.returnMsgServidor();
            if (msgServerTemp.startsWith("1")) {
                String[] Arraytemp = msgServerTemp.split(" ");
                String msgNuevo = "";

                for (int i = 2; i < Arraytemp.length; i++) {
                    msgNuevo += Arraytemp[i] + " ";
                }
                chat += Arraytemp[1] + " " + Desencriptar(msgNuevo) + "\n";
                conversacionpanel.getjTextArea1().setText(chat);
                ctrl.EliminarMsgServidor();
            } else {
                if (msgServerTemp.startsWith("0")) {
                    String[] Arraytemp = msgServerTemp.split(" ");
                    String[] usuario = new String[Arraytemp.length - 1];

                    for (int i = 0; i < usuario.length; i++) {
                        usuario[i] = Arraytemp[i + 1];
                    }
                    usuariospanel.setAmigos(usuario);
                    ctrl.EliminarMsgServidor(msgServerTemp);
                }
            }

        }
    } //  Ejecucion.		

    public static String encriptar(String msg) {
        String encriptado = "";
        char[] temp = msg.toCharArray();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != ' ') {
                int tempInt = temp[i];
                tempInt += 3;
                encriptado += (char) tempInt;
            } else {
                encriptado += " ";
            }
        }
        return encriptado;
    }

    public static String Desencriptar(String msg) {
        String desEncriptado = "";
        char[] temp = msg.toCharArray();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != ' ') {
                int tempInt = temp[i];
                tempInt -= 3;
                desEncriptado += (char) tempInt;
            } else {
                desEncriptado += " ";
            }
        }
        return desEncriptado;
    }

    public static void main(String args[]) {
        InterfazAppCliente frmMain = new InterfazAppCliente();
    }
}
