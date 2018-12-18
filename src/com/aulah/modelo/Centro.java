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

import static com.aulah.modelo.Configurar.getNombreMetodo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Centro {
    
    private IntegerProperty codigo;
    private StringProperty centro;
    private StringProperty clave;
    private StringProperty direccion;
   
    public static int codigo_generado = 0;//codigo_generado: se usa para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()
    
    public Centro(int codigo, String centro, String clave, String direccion) { 
        this.codigo = new SimpleIntegerProperty(codigo);
	this.centro = new SimpleStringProperty(centro);
	this.clave = new SimpleStringProperty(clave);
	this.direccion = new SimpleStringProperty(direccion);
    }
    
    public Centro(int codigo, String centro) { 
        this.codigo = new SimpleIntegerProperty(codigo);
	this.centro = new SimpleStringProperty(centro);
    }
    
    //Metodos atributo: codigo
    public int getCodigo() {
        return codigo.get();
    }
    public void setCodigo(int codigo) {
	this.codigo = new SimpleIntegerProperty(codigo);
    }
    public IntegerProperty CodigoProperty() {
	return codigo;
    }
    //Metodos atributo: centro
    public String getCentro() {
        return centro.get();
    }
    public void setCentro(String centro) {
	this.centro = new SimpleStringProperty(centro);
    }
    public StringProperty CentroProperty() {
	return centro;
    }
    //Metodos atributo: clave
    public String getClave() {
       return clave.get();
    }
    public void setClave(String clave) {
	this.clave = new SimpleStringProperty(clave);
    }
    public StringProperty ClaveProperty() {
	return clave;
    }
    //Metodos atributo: direccion
    public String getDireccion() {
	return direccion.get();
    }
    public void setDireccion(String direccion) {
	this.direccion = new SimpleStringProperty(direccion);
    }
    public StringProperty DireccionProperty() {
	return direccion;
    }
    
    //Llena un ObservableList con el codigo y el centro para alimentar algun ComboBox
    public static void llenarNombreCentros(Connection connection, ObservableList<Centro> lista){
	String sentenciaSQL = "SELECT codigo, " +
                                    "centro " +
                              "FROM tbl_centros";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while (resultado.next()){
                lista.add( 
                    new Centro(
                        resultado.getInt("codigo"),
                        resultado.getString("centro")
                    )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error al tratar de obtener los datos para llenar los Centros." +
                        "Método: "+getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }
    
    //Llena un ObservableList con el contenido completo de la tabla para alimentar algun TableView
    public static void llenarInformacionCentros(Connection connection, ObservableList<Centro> lista){
	String sentenciaSQL = "SELECT codigo, "
                            +       "centro, "
                            +       "clave, "
                            +       "direccion "
                            + "FROM tbl_centros";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while (resultado.next()){
                lista.add( 
                    new Centro(
                        resultado.getInt("codigo"),
                        resultado.getString("centro"),
                        resultado.getString("clave"),
                        resultado.getString("direccion")
                    )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error al tratar de obtener los datos para llenar los Centros." +
                        "Método: "+getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }
    
    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_centros (" + 
                                    "centro, " +
                                    "clave, " +
                                    "direccion) " +
                              "VALUES (?, ?, ?)";
	try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, centro.get());
            instruccion.setString(2, clave.get());
            instruccion.setString(3, direccion.get());
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                codigo_generado = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el Centro.\n" +
                        "Método: "+getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            codigo_generado =0;
            return 0;
        }
    }

    public int modificarRegistro(Connection connection){
        String sentenciaSQL = "UPDATE tbl_centros "+
                              "SET centro = ?, "+
                                  "clave = ?, "+
                                  "direccion = ? "+
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion =
            connection.prepareStatement(sentenciaSQL);
            instruccion.setString(1, centro.get());
            instruccion.setString(2, clave.get());
            instruccion.setString(3, direccion.get());
            instruccion.setInt(4, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar el Centro.\n"+
                        "Método: "+getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            return 0;
        }
    }

    public int eliminarRegistro(Connection connection){
        String sentenciaSQL = "DELETE FROM tbl_centros "+
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = connection.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar el Centro.\n"+
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            return 0;
        }
    }

    @Override
    public String toString(){
        return centro.get();
    }
    
}
