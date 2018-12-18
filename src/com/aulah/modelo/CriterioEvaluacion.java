/*
 * Copyright (C) 2018 hector
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

import static com.aulah.modelo.Configurar.*;
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
 *
 * @author hector
 */
public class CriterioEvaluacion {
    public IntegerProperty codigo;
    public StringProperty criterio;
    public IntegerProperty valor;
    public int cod_nivel;
    public Nivel nivel;
    
    public static int CODIGO_GENERADO = 0;//CODIGO_GENERADO: variable para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    public CriterioEvaluacion(int codigo, String criterio, int valor, Nivel nivel) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.criterio = new SimpleStringProperty(criterio);
            this.valor = new SimpleIntegerProperty(valor);
            this.nivel = nivel;
    }
    
    public CriterioEvaluacion(int codigo, String criterio, int valor, int cod_nivel) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.criterio = new SimpleStringProperty(criterio);
            this.valor = new SimpleIntegerProperty(valor);
            this.cod_nivel = cod_nivel;
    }
    
    public CriterioEvaluacion(int codigo, String criterio) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.criterio = new SimpleStringProperty(criterio);
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
    //Metodos atributo: criterio
    public String getCriterio() {
            return criterio.get();
    }
    public void setCriterio(String criterio) {
            this.criterio = new SimpleStringProperty(criterio);
    }
    public StringProperty CriterioProperty() {
            return criterio;
    }
    //Metodos atributo: valor
    public int getValor() {
            return valor.get();
    }
    public void setValor(int valor) {
            this.valor = new SimpleIntegerProperty(valor);
    }
    public IntegerProperty ValorProperty() {
            return valor;
    }
    //Metodo atributo cod_nivel
     public int getCodNivel() {
            return cod_nivel;
    }
    public void setCodNivel(int cod_nivel) {
            this.cod_nivel = cod_nivel;
    }
    //Metodo atributo nivel
     public Nivel getNivel() {
            return nivel;
    }
    public void setNivel(Nivel nivel) {
            this.nivel = nivel;
    }
    
    public static void llenarInformacionCriterioEvaluacion(Connection conn, ObservableList<CriterioEvaluacion> lista){
        String sentenciaSQL = "SELECT A.codigo, " + 
                              "A.criterio, " +
                              "A.valor, " +
                              "A.cod_nivel, " +
                              "B.nivel " +
                              "FROM tbl_criterios_evaluacion A " +
                              "INNER JOIN tbl_niveles B " +
                              "ON A.cod_nivel = B.codigo " +
                              "ORDER BY B.nivel";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new CriterioEvaluacion(resultado.getInt("codigo"), 
                                                resultado.getString("criterio"),
                                                resultado.getInt("valor"),
                                                new Nivel(resultado.getInt("cod_nivel"),
                                                          resultado.getString("nivel"))
                              )
                          );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion del Criterio de Evaluacion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarInformacionCriterioEvaluacionNivel(Connection conn, ObservableList<CriterioEvaluacion> lista, int cod_nivel){
        String sentenciaSQL = "SELECT codigo, " + 
                              "criterio, " +
                              "valor " +
                              "FROM tbl_criterios_evaluacion " +
                              "WHERE cod_nivel = " + cod_nivel + 
                              " AND criterio <> 'Puntos extras'";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new CriterioEvaluacion(resultado.getInt("codigo"), 
                                        resultado.getString("criterio")));
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre de los Criterios de Evaluacion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static String obtenerNombreCriterioEvaluacion(Connection conn, int codigo_criterio){
        String sentenciaSQL = "SELECT criterio " + 
                              "FROM tbl_criterios_evaluacion " + 
                              "WHERE codigo = " + codigo_criterio;
        String jornada = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                    jornada = resultado.getString("criterio");
            }
            return jornada;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre del Criterio de Evaluación.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            return null;
        }
    }
    
    public static boolean criterioEvaluacioPuntosExtraExiste(int cod_nivel, Connection conn){
        String sentenciaSQL = "SELECT criterio " + 
                              "FROM tbl_criterios_evaluacion " + 
                              "WHERE criterio LIKE 'Puntos extras' " +
                              "AND cod_nivel = " + cod_nivel;
        boolean criterio_existe = false;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                    criterio_existe = true;
            }
            return criterio_existe;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de saber si el eriterio de evaluación Puntos Extras ya existe.\n"+
                        "Clase: " + getNombreClase() + "\n" +
                        "Método: "+ getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL +"\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            return criterio_existe;
        }
    }
    
    public static void obtenerCriterioEvaluacionPuntosExtra(int codigo_criterio, Connection conn, ObservableList<CriterioEvaluacion> lista){
        String sentenciaSQL = "SELECT codigo, criterio " + 
                              "FROM tbl_criterios_evaluacion " + 
                              "WHERE codigo = " + codigo_criterio;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new CriterioEvaluacion(resultado.getInt("codigo"), 
                                                resultado.getString("criterio")));
            }            
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre del Criterio de Evaluación para Puntos Extras.\n"+
                        "Clase: " + getNombreClase() + "\n"+
                        "Método: " + getNombreMetodo() + "\n"+
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }
    
    public static int obtenerCodigoCriterioPuntosExtras( int cod_nivel, Connection conn){
        String sentenciaSQL = "SELECT codigo " + 
                              "FROM tbl_criterios_evaluacion " + 
                              "WHERE criterio LIKE 'Puntos extras' " +
                              "AND cod_nivel = " + cod_nivel;
        int codigo = 0;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                    codigo = resultado.getInt("codigo");
            }
            return codigo;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el codigo del criterio de evaluación para Puntos extras.\n"+
                        "Clase: " + getNombreClase() + "\n" +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            return codigo;
        }
    }
           
    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_criterios_evaluacion(criterio, valor, cod_nivel) " + 
                              "VALUES(?,?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, criterio.get());
            instruccion.setInt(2, valor.get());
            instruccion.setInt(3, nivel.getCodigo());
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                CODIGO_GENERADO = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el Criterio de Evaluacion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            CODIGO_GENERADO =0;
            return 0;
        }
    }
    
    public static int crearCriterioPuntosExtras(int cod_nivel, Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_criterios_evaluacion(criterio, valor, cod_nivel) " + 
                              "VALUES(?,?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, "Puntos extras");
            instruccion.setInt(2, 0);
            instruccion.setInt(3, cod_nivel);
            instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                CODIGO_GENERADO = rs.getInt(1);
            }
            return CODIGO_GENERADO;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el registro para Puntos Extras.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    public int modificarRegistro(Connection conn){
        String sentenciaSQL = "UPDATE tbl_criterios_evaluacion " +
                              "SET criterio = ?, " +
                                    "valor = ?, " +
                                    "cod_nivel = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, criterio.get());
            instruccion.setInt(2, valor.get());
            instruccion.setInt(3, nivel.getCodigo());
            instruccion.setInt(4, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar el Criterio de Evaluacion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    public int eliminarRegistro(Connection conn){
        String sentenciaSQL = "DELETE FROM tbl_criterios_evaluacion " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar el Criterio de Evaluacion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    @Override
    public String toString(){
        return criterio.get();
    }
}
