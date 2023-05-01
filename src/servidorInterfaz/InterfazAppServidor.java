/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidorInterfaz;

import servidorControlador.ControladorServidor;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import util.Util;




/**
 *
 * @author User
 */
public class InterfazAppServidor extends JFrame {
    // Relaciones

    private ControladorServidor ctrl;

// Constructor
    public InterfazAppServidor(ControladorServidor ctrl) {
        this.ctrl = ctrl;
        System.out.println("Server listening...");
        
        setTitle("Chat Cliente");

        
        getContentPane().setLayout(null);

        // Enlaza el controlador	  
        ConversacionServidor conversacionpanel = new servidorInterfaz.ConversacionServidor();
        conversacionpanel.setBounds(10, 10, 350, 430);
        add(conversacionpanel);

        Errores errorpanel = new Errores();
        errorpanel.setBounds(10, 450, 350, 180);
        add(errorpanel);

        getContentPane().setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(395, 715);
        setResizable(false);

        setVisible(true);
        Util.centrarVentana( this );
        
        while(true){
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(InterfazAppServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            conversacionpanel.getjTextArea1().setText(ctrl.getConversaciones());
            errorpanel.getjTextArea1().setText(ctrl.getErrores());
        }
    }

//  Ejecucion.		
    public static void main(String args[]) {
        InterfazAppServidor frmMain = new InterfazAppServidor(new ControladorServidor());
    }
}
