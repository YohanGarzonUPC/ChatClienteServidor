/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidorMundo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class Server implements Runnable {

    Map<String, String> usuarios = new HashMap<>();

    public String getUsuarios() {
        String temp = "";
        for (Map.Entry<String, String> entry : usuarios.entrySet()) {
            temp += entry.getKey() + " ";
        }
        return temp;
    }

// IP_Usuario -----------------------------------------------------------------------//
    public void cargarDatosIP_Usuario() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("data\\IP_Nick.txt"));
            String linea;
            usuarios.clear();
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(" ");
                usuarios.put(datos[0], datos[1]);
            }
            br.close();
        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Ocurrió un problema");
            errores += "Error: " + e.toString() + "\n";
        }
    }

    public void agregarRegistrosIP_Usuario(String ip, String nick) {
        try {
            FileWriter fw = new FileWriter("data\\IP_Nick.txt", true);
            fw.write(nick + " " + ip);
            fw.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public String seleccionarIP_Usuario(String nick) {
        try {
            if (usuarios.containsKey(nick)) {
                return (usuarios.get(nick));
            }
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Ocurrio un error" + e.toString());
            errores += "Error: " + e.toString() + "\n";
        }
        return "";
    }

    public void EliminarIP_Usuario(String nick) {

        //Eliminación visual de la tabla
        usuarios.remove(nick);

        //Limpieza del archivo .txt
        try {
            PrintWriter writer = new PrintWriter("data\\IP_Nick.txt");
            writer.print("");
            writer.close();
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Ocurrió un problema" + e.toString());
            errores += "Error: " + e.toString() + "\n";
        }

        //Creación de los nuevos registros luego de la eliminación
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data\\IP_Nick.txt")))) {
            int cont = 0;
            for (Map.Entry<String, String> entry : usuarios.entrySet()) {
                if (cont > 0) {
                    agregarRegistrosIP_Usuario(entry.getValue(), "\n" + entry.getKey());
                } else {
                    agregarRegistrosIP_Usuario(entry.getValue(), entry.getKey());
                    cont++;
                }
            }
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Ocurrio un error");
            errores += "Error: " + e.toString() + "\n";
        }

    }

    public void CambiarIP(String nick, String ip) {

        //Eliminación visual de la tabla
        usuarios.replace(nick, ip);

        //Limpieza del archivo .txt
        try {
            PrintWriter writer = new PrintWriter("data\\IP_Nick.txt");
            writer.print("");
            writer.close();
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Ocurrió un problema" + e.toString());
            errores += "Error: " + e.toString() + "\n";
        }

        //Creación de los nuevos registros luego de la eliminación
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data\\IP_Nick.txt")))) {
            int cont = 0;
            for (Map.Entry<String, String> entry : usuarios.entrySet()) {
                if (cont > 0) {
                    agregarRegistrosIP_Usuario(entry.getValue(), "\n" + entry.getKey());
                } else {
                    agregarRegistrosIP_Usuario(entry.getValue(), entry.getKey());
                    cont++;
                }
            }
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Ocurrio un error");
            errores += "Error: " + e.toString() + "\n";
        }

    }

//------------------------------------------------------------------------//
// MsgEspera -----------------------------------------------------------------------//    
    ArrayList<String[]> msgespera = new ArrayList<String[]>();

    public void cargarDatosMsgEspera() {

        try {
            BufferedReader br = new BufferedReader(new FileReader("data\\MensajeNoEnviados.txt"));
            String linea;
            msgespera.clear();
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(" ");
                String[] datos2 = new String[3];
                for (int i = 3; i < datos.length; i++) {
                    datos[2] += " " + datos[i];

                }
                for (int i = 0; i < 3; i++) {
                    datos2[i] = datos[i];
                }
                msgespera.add(datos2);

            }
            br.close();
        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Ocurrió un problema");
            errores += "Error: " + e.toString() + "\n";
        }
    }

    public void agregarRegistrosMsgEspera(String llegada, String envia, String msg) {
        try {
            FileWriter fw = new FileWriter("data\\MensajeNoEnviados.txt", true);
            fw.write(llegada + " " + envia + " " + msg);
            fw.close();
        } catch (IOException ex) {
            errores += "Error: " + ex.toString() + "\n";
        }
    }

    public String enviarMsgEspera(String nick) {
        String ip = usuarios.get(nick);
        try {
            String temp = "";
            String tempfinal = "";
            if (nick != null) {

                for (int i = 0; i < msgespera.size(); i++) {
                    if (msgespera.get(i)[0].equals(nick)) {
                        temp += msgespera.get(i)[1] + ": ";
                        temp += msgespera.get(i)[2] + "\n";
                        EliminarMsgEspera(msgespera.get(i)[0]);
                        i--;
                        response(ip + " 1 " + temp);
                        tempfinal += temp;
                        temp = "";
                    }
                }

                return tempfinal;
            }
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Ocurrio un error" + e.toString());
            errores += "Ocurrió un problema: " + e.toString() + "\n";
        }
        return "No existe";
    }

    public String[] seleccionarnickllegada(String nick) {
        try {
            for (String[] temp : msgespera) {
                if (nick.equals(temp[0])) {
                    return temp;
                }
            }
        } catch (Exception e) {
            errores += "Error: " + e.toString() + "\n";
//            JOptionPane.showMessageDialog(null, "Ocurrio un error" + e.toString());
        }
        return null;
    }

    public void EliminarMsgEspera(String nickllegada) {

        //Eliminación visual de la tabla
        msgespera.remove(seleccionarnickllegada(nickllegada));

        //Limpieza del archivo .txt
        try {
            PrintWriter writer = new PrintWriter("data\\MensajeNoEnviados.txt");
            writer.print("");
            writer.close();
        } catch (Exception e) {
            errores += "Error: " + e.toString() + "\n";
//            JOptionPane.showMessageDialog(null, "Ocurrió un problema" + e.toString());
        }

        //Creación de los nuevos registros luego de la eliminación
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data\\MensajeNoEnviados.txt")))) {
            for (int i = 0; i < msgespera.size(); i++) {
                if (i != msgespera.size() - 1) {
                    agregarRegistrosMsgEspera(msgespera.get(i)[0], msgespera.get(i)[1], msgespera.get(i)[2] + "\n");
                } else {
                    agregarRegistrosMsgEspera(msgespera.get(i)[0], msgespera.get(i)[1], msgespera.get(i)[2]);
                }
            }
        } catch (Exception e) {
            errores += "Error: " + e.toString() + "\n";
//            JOptionPane.showMessageDialog(null, "Ocurrio un error");
        }

    }

//------------------------------------------------------------------------//
    public String errores = "";
    public String conversaciones = "";
    public ArrayList Conectados = new ArrayList();

    public String getErrores() {
        return errores;
    }

    public String getConversaciones() {
        return conversaciones;
    }

    /* Constructor */
    public Server() {
        Thread treadListener = new Thread(this);
        treadListener.start();
    }


    /* Server:Data >> Socket >> Client */
    public static void socket(String msg) { // ip - action - contenido  // casos Cliente 0 = actualizar lista , 1 = actuarlizar msg recibidos
        String[] Arraytemp = msg.split(" ");
        String msgNuevo = "";

        for (int i = 1; i < Arraytemp.length; i++) {
            msgNuevo += Arraytemp[i] + " ";
        }
        try {
            Socket server = new Socket(Arraytemp[0], 5050); // portSend 5050
            DataOutputStream outBuffer = new DataOutputStream(server.getOutputStream());
            outBuffer.writeUTF(msgNuevo);
            server.close();
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "Server : socket() : UnknownHostException: " + e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Server : socket() : IOException: " + e.getMessage());
        }
    }

    @Override
    /* Server: Listen */
    public void run() {
        ServerSocket serverSocket;
        Socket socket;
        DataInputStream inDataBuffer;

        try {
            serverSocket = new ServerSocket(5000); // portListen 5000

            while (true) {
                cargarDatosIP_Usuario();
                cargarDatosMsgEspera();
                socket = serverSocket.accept();
                inDataBuffer = new DataInputStream(socket.getInputStream());
                String msg = inDataBuffer.readUTF();
                String[] temp = msg.split(" ");
                String tempUs = "";
                // 1 usario ip/msg
                switch (temp[0]) {
                    case "0":
                        cargarDatosIP_Usuario();
                        response(usuarios.get(temp[1]) + " 0 " + getUsuarios());
                        break;
                    case "1":
                        if (seleccionarIP_Usuario(temp[1]).equals("")) {
                            agregarRegistrosIP_Usuario(temp[2], "\n" + temp[1]);

                        } else {
                            if (!(seleccionarIP_Usuario(temp[1]).equals(temp[2]))) {
                                CambiarIP(temp[1], temp[2]);
                            }
                        }
                        Conectados.add(temp[1]);
                        cargarDatosMsgEspera();
                        response(temp[2] + " 0 " + getUsuarios());
                        String MsgReenviar = enviarMsgEspera(temp[1]);
                        if (!MsgReenviar.equals("")) {
                            conversaciones += "Para " + temp[1] + "  de: \n " + MsgReenviar;
                        }
                        break;

                    case "2":
                        EliminarIP_Usuario(temp[1]);
                        break;

                    case "3": // 3 usuario_llegara usuario mensaje msg
                        String msgTemp = "";

                        for (int i = 3; i < temp.length; i++) {
                            msgTemp += temp[i] + " ";
                        }

                        if (Conectados.contains(temp[1])) {
                            conversaciones += "Para " + temp[1] + "  de: \n " + msgTemp;
                            response(usuarios.get(temp[1]) + " 1 " + temp[2] + ": " + msgTemp);
                        } else {
                            agregarRegistrosMsgEspera("\n" + temp[1], temp[2], msgTemp);
                        }
                        break;

                    case "4":
                        Conectados.remove((String)temp[1]);
                        for (Object Busca : Conectados) {
                            System.out.println(Busca);
                        }
                        break;

                }

                socket.close();
            }

        } catch (IOException e) {
            errores += "Error: " + e.toString() + "\n";
//            JOptionPane.showMessageDialog(null, "Server : run (): IOException: " + e.getMessage());
        }
    }

    public static void response(String msg) {
        socket(msg);
    }
}
