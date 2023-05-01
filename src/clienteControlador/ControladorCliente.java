/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clienteControlador;

import clienteMundo.Cliente;

public class ControladorCliente {
    // Atributos

    // Relaciones 	
    private Cliente cliente;

    // Constructor	
    public ControladorCliente(String ipServer, int puertoEnvia, int puertoResive) {
        cliente = new Cliente(ipServer, puertoEnvia, puertoResive);
    }

    // Recibe las referencias de los objetos a controlar en la interfaz	
    public void conectar() {
    }

    // --------------------------------------------------------------	
    // Implementacion de los requerimientos de usuario.	
    // --------------------------------------------------------------
    public void socket(String msg) {
        cliente.socket(msg);
    }

    public String returnMsgServidor() {
        return cliente.getMsgServer();
    }
    // 1 2 3 
    public void EliminarMsgServidor() {
        cliente.EliminarListaespera();
    }
    
    public void EliminarMsgServidor(String temp) {
        cliente.EliminarListaespera(temp);
    }

}
