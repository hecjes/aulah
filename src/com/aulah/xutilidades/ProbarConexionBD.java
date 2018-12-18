
package com.aulah.xutilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class ProbarConexionBD {
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/COLEGIO_BD";
    private String usuario = "root";
    private String contrasena = "";
	
    public Connection getConnection() {
	return connection;
    }
        
    public void setConnection(Connection connection) {
	this.connection = connection;
    }
	
    public boolean HayConexion(){
        boolean hay = false;
	try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, usuario, contrasena);
            if(connection != null){
                hay = true;
                System.out.println("LA CONEXION CON LA BD SE HA ESTABLECIDO");
            }
	} catch (ClassNotFoundException ex) {
            System.out.println("Error al cargar el Driver conector: \"com.mysql.jdbc.Driver\"\n" + ex.toString());
	} catch (SQLException ex) {
            System.out.println("Error al establecer la conexion con la BD. \n" + ex.toString());
        }
        return hay;
    }
	
    public void Desconectar(){
        try {
            connection.close();
            System.out.println("LA CONEXION CON LA BD SE HA CERRADO");
        } catch (SQLException ex) {
            System.out.println("Error al cerrar la conexion con la BD \n " + ex.toString());
        }
    }
    
    public String getUrl(){
        return url;
    }
}
