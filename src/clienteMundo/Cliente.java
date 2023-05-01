package clienteMundo;

import clienteInterfaz.InterfazAppCliente;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Cliente implements Runnable {

    private String ip;
    private int PuertoEnvia;
    private int PuertoRecibe;
    ArrayList<String> mensajesNoprocesados = new ArrayList();

    public String getMsgServer() {
        if (!this.mensajesNoprocesados.isEmpty()) {
            return this.mensajesNoprocesados.get(0);
        } else {
            return "";
        }
    }

    public void EliminarListaespera() {
        if (!this.mensajesNoprocesados.isEmpty()) {
            this.mensajesNoprocesados.remove(0);
        } 
    }

    public void EliminarListaespera(String temp) {
        if (!this.mensajesNoprocesados.isEmpty()) {
            this.mensajesNoprocesados.remove(temp);
        } 
    }

    /* Constructor */
    public Cliente(String ipServer, int puertoEnvia, int puertoResive) {
        this.ip = ipServer;
        this.PuertoEnvia = puertoEnvia;
        this.PuertoRecibe = puertoResive;
        Thread treadListener = new Thread(this);
        treadListener.start();
    }

    /* Client:Data >> Socket >> Server */
    public void socket(String msg) {
        try {
            Socket client = new Socket(ip, PuertoEnvia); // portSend 5000
            DataOutputStream outBuffer = new DataOutputStream(client.getOutputStream());
            outBuffer.writeUTF(msg);
            client.close();
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "Client: socket(1) : UnknownHostException: " + e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Client: socket(2) : IOException: " + e.getMessage());
        }
    }

    @Override
    /* Client: Listen */
    public void run() {
        ServerSocket serverSocket;
        Socket socket;
        DataInputStream inDataBuffer;

        try {
            serverSocket = new ServerSocket(PuertoRecibe); // portListen 5050           
            while (true) {
                socket = serverSocket.accept();
                inDataBuffer = new DataInputStream(socket.getInputStream());
                String msg = inDataBuffer.readUTF();
                mensajesNoprocesados.add(msg);
                System.out.println(msg);
                socket.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Client run() : IOException: " + e.getMessage());
        }
    }
}
