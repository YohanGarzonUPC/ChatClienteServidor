/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidorControlador;

import servidorMundo.Server;

public class ControladorServidor {
    // Atributos

    public String getErrores() {
        return server.errores;
    }

    public String getConversaciones() {
        return server.conversaciones;
    }

    // Relaciones 	
    private Server server;

    // Constructor	
    public ControladorServidor() {
        server = new Server();
    }

    // Recibe las referencias de los objetos a controlar en la interfaz	
    public void conectar() {
    }

    public String returnUsuarios() {
        return server.getUsuarios();
    }
    // --------------------------------------------------------------	
    // Implementacion de los requerimientos de usuario.	
    // --------------------------------------------------------------

    public void socket(String msg) {
        server.socket(msg);
    }

}
