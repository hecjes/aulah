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
import static com.aulah.modelo.Curso.codigo_generado;
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
 * Clase para gestionar la informacion de Curso.
 * @author hector
 */
public class Curso {
    
    public IntegerProperty codigo;
    public StringProperty curso;
    public Periodo periodo;
    public Nivel nivel;
    public IntegerProperty cod_nivel;
    
    public static int codigo_generado = 0;//codigo_generado: variable para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    public Curso(int codigo, String curso, Periodo cod_periodo, Nivel cod_nivel) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.curso = new SimpleStringProperty(curso);
            this.periodo = cod_periodo;
            this.nivel = cod_nivel;
    }
    
    public Curso(int codigo, String curso) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.curso = new SimpleStringProperty(curso);
    }
    
    public Curso(int codigo, String curso, int cod_nivel) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.curso = new SimpleStringProperty(curso);
            this.cod_nivel =  new SimpleIntegerProperty(cod_nivel);
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
    //Metodos atributo: curso
    public String getCurso() {
            return curso.get();
    }
    public void setCurso(String curso) {
            this.curso = new SimpleStringProperty(curso);
    }
    public StringProperty CursoProperty() {
            return curso;
    }
    //Metodos atributo: periodo
    public Periodo getCod_periodo() {
            return periodo;
    }
    public void setCod_periodo(Periodo cod_periodo) {
            this.periodo = cod_periodo;
    }
    //Metodos atributo: nivel
    public Nivel getCod_nivel() {
            return nivel;
    }
    public void setCod_nivel(Nivel cod_nivel) {
            this.nivel = cod_nivel;
    }
    //Metodos atributo: cod_nivel
    public IntegerProperty get_cod_nivel() {
            return cod_nivel;
    }
    public void set_cod_nivel(IntegerProperty cod_nivel) {
            this.cod_nivel = cod_nivel;
    }
    
    public static void llenarInformacionCurso(Connection conn, ObservableList<Curso> lista){
        String sentenciaSQL = "SELECT A.codigo, " + 
                                    "A.curso, " +
                                    "A.cod_periodo, " +
                                    "A.cod_nivel, " +
                                    "B.periodo, " +
                                    "C.nivel " +
                              "FROM tbl_cursos A " +
                              "INNER JOIN tbl_periodos B " +
                              "ON (A.cod_periodo = B.codigo) " +
                              "INNER JOIN tbl_niveles C " +
                              "ON (A.cod_nivel = C.codigo)";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Curso(resultado.getInt("codigo"), 
                                    resultado.getString("curso"), 
                                    new Periodo(resultado.getInt("cod_periodo"),
                                                resultado.getString("periodo")), 
                                    new Nivel(resultado.getInt("cod_nivel"),
                                              resultado.getString("nivel"))
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion del Curso.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarInformacionCursoPeriodo(Connection conn, ObservableList<Curso> lista, int codigo_periodo){
        String sentenciaSQL = "SELECT A.codigo, " + 
                                    "A.curso, " +
                                    "A.cod_periodo, " +
                                    "A.cod_nivel, " +
                                    "B.periodo, " +
                                    "C.nivel " +
                              "FROM tbl_cursos A " +
                              "INNER JOIN tbl_periodos B " +
                              "ON ((A.cod_periodo = B.codigo) AND (B.codigo = " + codigo_periodo + ")) " +
                              "INNER JOIN tbl_niveles C " +
                              "ON (A.cod_nivel = C.codigo)";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Curso(resultado.getInt("codigo"), 
                                    resultado.getString("curso"), 
                                    new Periodo(resultado.getInt("cod_periodo"),
                                                resultado.getString("periodo")), 
                                    new Nivel(resultado.getInt("cod_nivel"),
                                              resultado.getString("nivel"))
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion del Curso.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarNombreCurso(Connection conn, ObservableList<Curso> lista){
        String sentenciaSQL = "SELECT codigo, " + 
                                    "curso, " +
                                    "cod_nivel " +
                              "FROM tbl_cursos";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Curso(resultado.getInt("codigo"), 
                                    resultado.getString("curso")
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre del Curso.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarCursoNivel(Connection conn, ObservableList<Curso> lista){
        String sentenciaSQL = "SELECT codigo, " + 
                                    "curso, " +
                                    "cod_nivel " +
                              "FROM tbl_cursos";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Curso(resultado.getInt("codigo"), 
                                    resultado.getString("curso"),
                                    resultado.getInt("cod_nivel")
                         ));
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre del Curso y el Codigo de Nivel.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarNombreCursosNivel(Connection conn, ObservableList<Curso> lista, int codigo_nivel){
        String sentenciaSQL = "SELECT codigo, " + 
                                    "curso " +
                              "FROM tbl_cursos " +
                              "WHERE cod_nivel = " + codigo_nivel;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Curso(resultado.getInt("codigo"), 
                                    resultado.getString("curso")
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el nombre del Curso para el Nivel.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
        
    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_cursos(curso, " +
                                                    "cod_periodo, " + 
                                                    "cod_nivel) " + 
                              "VALUES(?,?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, curso.get());
            instruccion.setInt(2, periodo.getCodigo());
            instruccion.setInt(3, nivel.getCodigo());
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                codigo_generado = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el Curso.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            codigo_generado =0;
            return 0;
        }
    }
        
    public int modificarRegistro(Connection conn){
        String sentenciaSQL = "UPDATE tbl_cursos " +
                              "SET curso = ?, " +
                                  "cod_periodo = ?, " +
                                  "cod_nivel = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, curso.get());
            instruccion.setInt(2, periodo.getCodigo());
            instruccion.setInt(3, nivel.getCodigo());
            instruccion.setInt(4, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar el Curso.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    public int eliminarRegistro(Connection conn){
        String sentenciaSQL = "DELETE FROM tbl_cursos " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar el Curso.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    @Override
    public String toString(){
        return curso.get();
    }
    
}
