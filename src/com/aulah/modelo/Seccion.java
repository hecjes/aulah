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
 *
 * Clase para gestionar los datos de Seccion
 * 
 * @author hector
 */
public class Seccion {
    
    public IntegerProperty codigo;
    public StringProperty seccion;
    public Curso curso;
    public Jornada jornada;
    
    public static int codigo_generado = 0;//codigo_generado: variable para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    public Seccion(int codigo, String seccion, Curso cod_curso, Jornada cod_jornada){ 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.seccion = new SimpleStringProperty(seccion);
            this.curso = cod_curso;
            this.jornada = cod_jornada;
    }
    
    public Seccion(int codigo, String seccion){ 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.seccion = new SimpleStringProperty(seccion);
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
    //Metodos atributo: seccion
    public String getSeccion() {
            return seccion.get();
    }
    public void setSeccion(String seccion) {
            this.seccion = new SimpleStringProperty(seccion);
    }
    public StringProperty SeccionProperty() {
            return seccion;
    }
    //Metodos atributo: cod_curso
    public Curso getCod_curso() {
            return curso;
    }
    public void setCod_curso(Curso cod_curso) {
            this.curso = cod_curso;
    }
    //Metodos atributo: cod_jornada
    public Jornada getCod_jornada() {
            return jornada;
    }
    public void setCod_jornada(Jornada cod_jornada) {
            this.jornada = cod_jornada;
    }
    
    public static void llenarInformacionSeccion(Connection conn, ObservableList<Seccion> lista){
        String sentenciaSQL = "SELECT A.codigo, " + 
                                    "A.seccion, " +
                                    "A.cod_curso, " +
                                    "A.cod_jornada, " +
                                    "B.curso, " +
                                    "C.jornada " +
                              "FROM tbl_secciones A " +
                              "INNER JOIN tbl_cursos B " +
                              "ON (A.cod_curso = B.codigo) " +
                              "INNER JOIN tbl_jornadas C " +
                              "ON (A.cod_jornada = C.codigo)";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Seccion(resultado.getInt("codigo"), 
                                    resultado.getString("seccion"), 
                                    new Curso(resultado.getInt("cod_curso"),
                                                resultado.getString("curso")), 
                                    new Jornada(resultado.getInt("cod_jornada"),
                                              resultado.getString("jornada"))
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion de la Seccion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarNombreSeccion(Connection conn, ObservableList<Seccion> lista){
        String sentenciaSQL = "SELECT codigo, " + 
                                    "seccion " +
                              "FROM tbl_secciones";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Seccion(resultado.getInt("codigo"), 
                                    resultado.getString("seccion")
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre de la Seccion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarNombreSeccionCurso(Connection conn, ObservableList<Seccion> lista, int codigo_curso){
        String sentenciaSQL = "SELECT codigo, " + 
                                    "seccion " +
                              "FROM tbl_secciones " +
                              "WHERE cod_curso = " + codigo_curso;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Seccion(resultado.getInt("codigo"), 
                                    resultado.getString("seccion")
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre de las Secciones del Curso.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static int obtenerCodigoJornadaSeccion(Connection conn, int codigo_seccion){
        String sentenciaSQL = "SELECT cod_jornada " + 
                              "FROM tbl_secciones " +
                              "WHERE codigo = " + codigo_seccion;
        int codigo_jornada = 0;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                    codigo_jornada = resultado.getInt("cod_jornada");
            }
            return codigo_jornada;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre de las Secciones del Curso.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            return 0;
        }
    }
          
    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_secciones(seccion, " +
                                                    "cod_curso, " + 
                                                    "cod_jornada) " + 
                              "VALUES(?,?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, seccion.get());
            instruccion.setInt(2, curso.getCodigo());
            instruccion.setInt(3, jornada.getCodigo());
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                codigo_generado = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar la Seccion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            codigo_generado =0;
            return 0;
        }
    }
        
    public int modificarRegistro(Connection conn){
        String sentenciaSQL = "UPDATE tbl_secciones " +
                              "SET seccion = ?, " +
                                  "cod_curso = ?, " +
                                  "cod_jornada = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, seccion.get());
            instruccion.setInt(2, curso.getCodigo());
            instruccion.setInt(3, jornada.getCodigo());
            instruccion.setInt(4, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar la Seccion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    public int eliminarRegistro(Connection conn){
        String sentenciaSQL = "DELETE FROM tbl_secciones " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar la Seccion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    @Override
    public String toString(){
        return seccion.get();
    }
    
}
