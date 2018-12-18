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
 * Permite gestionar la informacion del Nivel
 * @author hector
 */
public class Nivel {
    public IntegerProperty codigo;
    public StringProperty nivel;
    public Periodo periodo;
    //private int cod_periodo;
    
    public static int codigo_generado = 0;//codigo_generado: variable para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    public Nivel(int codigo, String nivel) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.nivel = new SimpleStringProperty(nivel);
    }
    
    public Nivel(int codigo, String nivel, Periodo cod_periodo) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.nivel = new SimpleStringProperty(nivel);
            this.periodo = cod_periodo;
    }
    
    public Nivel(int codigo) { 
            this.codigo = new SimpleIntegerProperty(codigo);
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
    //Metodos atributo: nivel
    public String getNivel() {
            return nivel.get();
    }
    public void setNivel(String nivel) {
            this.nivel = new SimpleStringProperty(nivel);
    }
    public StringProperty NivelProperty() {
            return nivel;
    }
    //Metodos atributo: periodo
    public Periodo getCod_periodo() {
            return periodo;
    }
    public void setCod_periodo(Periodo cod_periodo) {
            this.periodo = cod_periodo;
    }
    
    public static void llenarInformacionNivel(Connection conn, ObservableList<Nivel> lista){
        String sentenciaSQL = "SELECT A.codigo, " + 
                              "A.nivel, " +
                              "A.cod_periodo, " +
                              "B.periodo " +
                              "FROM tbl_niveles A " +
                              "INNER JOIN tbl_periodos B " +
                              "ON (A.cod_periodo = B.codigo)";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Nivel(resultado.getInt("codigo"), 
                                    resultado.getString("nivel"),
                                    new Periodo(resultado.getInt("cod_periodo"), resultado.getString("periodo"))
                                    )
                        );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion de los Niveles.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarNombreNiveles(Connection conn, ObservableList<Nivel> lista){
        String sentenciaSQL = "SELECT codigo, " + 
                              "nivel " +
                              "FROM tbl_niveles";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Nivel(resultado.getInt("codigo"), 
                                    resultado.getString("nivel")));
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre de los Niveles.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarNivelesPeriodo(Connection conn, ObservableList<Nivel> lista, int codigo_periodo){
        String sentenciaSQL = "SELECT codigo, " + 
                              "nivel " +
                              "FROM tbl_niveles " +
                              "WHERE cod_periodo = " + codigo_periodo;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Nivel(resultado.getInt("codigo"), 
                                    resultado.getString("nivel")));
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre de los Niveles del Periodo.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static int obtenerCodigoNivelPeriodo(Connection conn, int cod_periodo){
        int codigo = 0;
        String sentenciaSQL = "SELECT codigo " +
                              "FROM tbl_niveles " +
                              "WHERE cod_periodo = " + cod_periodo;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                codigo = resultado.getInt("codigo");
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el codigo del Nivel del Periodo.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
        return codigo;
    }
        
    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_niveles(nivel, cod_periodo) " + 
                              "VALUES(?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, nivel.get());
            instruccion.setInt(2, periodo.getCodigo());
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                codigo_generado = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el Nivel.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            codigo_generado =0;
            return 0;
        }
    }
        
    public int modificarRegistro(Connection conn){
        String sentenciaSQL = "UPDATE tbl_niveles " +
                              "SET nivel = ?, " +
                                  "cod_periodo = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, nivel.get());
            instruccion.setInt(2, periodo.getCodigo());
            instruccion.setInt(3, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar el Nivel.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    public int eliminarRegistro(Connection conn){
        String sentenciaSQL = "DELETE FROM tbl_niveles " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar el Nivel.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    @Override
    public String toString(){
        return nivel.get();
    }
    
}
