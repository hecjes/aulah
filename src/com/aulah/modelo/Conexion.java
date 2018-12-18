/*
 * Copyright (C) 2018 Hector Armando Herrera
 *
 * Este programa es software libre: usted puede distribuir y/o modificarlo
 * bajo los términos de la GNU General Public License publicada por
 * la Free Software Foundation, ya sea la versión 3 de la Licencia, o
 * versiones posteriores.
 *
 * Este programa se distribuye con la esperanza de que sea útil.,
 * pero SIN NINGUNA GARANTÍA; sin ni siquiera la garantía implícita de
 * COMERCIABILIDAD o APTITUD PARA UN PROPÓSITO PARTICULAR. Ver la
 * GNU General Public License para mas detalles.
 *
 * Debería haber recibido una copia de la GNU General Public License
 * junto con este programa. Si no, visite <http://www.gnu.org/licenses/>.
 */

package com.aulah.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

public class Conexion {
           
    //public static String CadenaConexion = "jdbc:derby://localhost:1527/COLEGIO_BD;user=hector;password=h3c7r0zon2";
    
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/AULAH";
    private String usuario = "hector";
    private String contrasena = "h3c7r0zon2";
	
    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, usuario, contrasena);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
	return connection;
    }
        
    public void setConnection(Connection connection) {
	this.connection = connection;
    }
	
    public Connection establecerConexion(){
	try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("\n- - - - - - - - - > Conexión con la Base de Datos ESTABLECIDA");
	} catch (ClassNotFoundException ex) {
            System.out.println("Error al cargar el Driver conector: " + ex.toString());
	} catch (SQLException ex) {
            //Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al establecer la conexion: " + ex.toString());
            Alert aviso = new Alert(Alert.AlertType.INFORMATION);
            aviso.setHeaderText("No se pudo establecer la conexión con el Servidor de Base de Datos." +
                    "\n URL = " + url);
            aviso.setContentText("El servidor de BD podría no estar funcionando.");
            aviso.showAndWait();
	}
        return connection;
    }
	
    public void cerrarConexion(){
        try {
            connection.close();
            System.out.println("- - - - - - - - - > Conexión con la Base de Datos CERRADA\n");
        } catch (SQLException ex) {
            System.out.println("Error al cerrar la conexion: " + ex.toString());
        }
    }	
    
    public boolean hayConexion(){
        boolean hay_conexion = false;
	try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, usuario, contrasena);
            if(connection != null){
                hay_conexion = true;
                System.out.println("La conexion de prueba con la BD se ha establecido CORRECTAMENTE");
                cerrarConexion();
            }
	} catch (ClassNotFoundException ex) {
            System.out.println("Error al cargar el Driver conector: \"com.mysql.jdbc.Driver\"\n" + ex.toString());
	} catch (SQLException ex) {
            //Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al establecer la conexion de prueba con la BD. \n" + ex.toString());
        }
        return hay_conexion;
    }
    
    public String getUrl(){
        return url;
    }
}
