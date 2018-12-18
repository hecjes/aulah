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

/**
 *Clase para gestionar los datos de Jornada
 * 
 * @author hector
 */
public class Jornada {
    public IntegerProperty codigo;
    public StringProperty jornada;
    
    public static int codigo_generado = 0;//codigo_generado: variable para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    public Jornada(int codigo, String jornada) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.jornada = new SimpleStringProperty(jornada);
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
    //Metodos atributo: jornada
    public String getJornada() {
            return jornada.get();
    }
    public void setJornada(String jornada) {
            this.jornada = new SimpleStringProperty(jornada);
    }
    public StringProperty JornadaProperty() {
            return jornada;
    }
    
    public static void llenarInformacionJornada(Connection conn, ObservableList<Jornada> lista){
        String sentenciaSQL = "SELECT codigo, " + 
                              "jornada " +
                              "FROM tbl_jornadas";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Jornada(resultado.getInt("codigo"), 
                                        resultado.getString("jornada")));
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion de las Jornadas.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarNombreJornadas(Connection conn, ObservableList<Jornada> lista){
        String sentenciaSQL = "SELECT codigo, " + 
                              "jornada " +
                              "FROM tbl_jornadas";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Jornada(resultado.getInt("codigo"), 
                                        resultado.getString("jornada")));
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre de las Jornadas.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static String obtenerNombreJornada(Connection conn, int codigo_jornada){
        String sentenciaSQL = "SELECT jornada " + 
                              "FROM tbl_jornadas " + 
                              "WHERE codigo = " + codigo_jornada;
        String jornada = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                    jornada = resultado.getString("jornada");
            }
            return jornada;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre de la Jornada.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            return null;
        }
    }

    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_jornadas(jornada) " + 
                              "VALUES(?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, jornada.get());
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                codigo_generado = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar la Jornada.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            codigo_generado =0;
            return 0;
        }
    }
        
    public int modificarRegistro(Connection conn){
        String sentenciaSQL = "UPDATE tbl_jornadas " +
                              "SET jornada = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, jornada.get());
            instruccion.setInt(2, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar la Jornada.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    public int eliminarRegistro(Connection conn){
        String sentenciaSQL = "DELETE FROM tbl_jornadas " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar la Jornada.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    @Override
    public String toString(){
        return jornada.get();
    }
}
