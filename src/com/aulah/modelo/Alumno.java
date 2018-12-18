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
import static com.aulah.modelo.Alumno.codigo_generado;
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


public class Alumno {
    
    public IntegerProperty codigo;
    public StringProperty nombre;
    public StringProperty rne;
    public StringProperty sexo;
    public StringProperty estado;
    public Periodo periodo;
    public Nivel nivel;
    public Curso curso;
    public Seccion seccion;
    public Jornada jornada;
    public String nombre_jornada;
    public int cod_jornada;
    
    public static int codigo_generado = 0;//codigo_generado: variable para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    public Alumno(int codigo, String nombre, String rne, String sexo, String estado, Periodo periodo, Nivel nivel, Curso curso, Seccion seccion, Jornada jornada) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.nombre = new SimpleStringProperty(nombre);
            this.rne = new SimpleStringProperty(rne);
            this.sexo = new SimpleStringProperty(sexo);
            this.estado = new SimpleStringProperty(estado);
            this.periodo = periodo;
            this.nivel = nivel;
            this.curso = curso;
            this.seccion = seccion;
            this.jornada = jornada;
    }
    
    public Alumno(int codigo, String nombre, String rne, String sexo, String estado, Periodo periodo, Nivel nivel, Curso curso, Seccion seccion, int p_cod_jornada) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.nombre = new SimpleStringProperty(nombre);
            this.rne = new SimpleStringProperty(rne);
            this.sexo = new SimpleStringProperty(sexo);
            this.estado = new SimpleStringProperty(estado);
            this.periodo = periodo;
            this.nivel = nivel;
            this.curso = curso;
            this.seccion = seccion;
            this.cod_jornada = p_cod_jornada;
    }
    
    public Alumno(int codigo, String nombre, String rne, String sexo, String estado, Periodo periodo, Nivel nivel, Curso curso, Seccion seccion, String p_nombre_jornada, int p_cod_jornada) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.nombre = new SimpleStringProperty(nombre);
            this.rne = new SimpleStringProperty(rne);
            this.sexo = new SimpleStringProperty(sexo);
            this.estado = new SimpleStringProperty(estado);
            this.periodo = periodo;
            this.nivel = nivel;
            this.curso = curso;
            this.seccion = seccion;
            this.nombre_jornada = p_nombre_jornada;
            this.cod_jornada = p_cod_jornada;
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
    //Metodos atributo: nombre
    public String getNombre() {
            return nombre.get();
    }
    public void setNombre(String nombre) {
            this.nombre = new SimpleStringProperty(nombre);
    }
    public StringProperty NombreProperty() {
            return nombre;
    }
    //Metodos atributo: rne
    public String getRne() {
            return rne.get();
    }
    public void setRne(String rne) {
            this.rne = new SimpleStringProperty(rne);
    }
    public StringProperty RneProperty() {
            return rne;
    }
    //Metodos atributo: sexo
    public String getSexo() {
            return sexo.get();
    }
    public void setSexo(String sexo) {
            this.sexo = new SimpleStringProperty(sexo);
    }
    public StringProperty SexoProperty() {
            return sexo;
    }
    //Metodos atributo: estado
    public String getEstado() {
            return estado.get();
    }
    public void setEstado(String estado) {
            this.estado = new SimpleStringProperty(estado);
    }
    public StringProperty EstadoProperty() {
            return estado;
    }
    //Metodos atributo: periodo
    public Periodo getPeriodo() {
            return periodo;
    }
    public void setPeriodo(Periodo periodo) {
            this.periodo = periodo;
    }
    //Metodos atributo: nivel
    public Nivel getNivel() {
            return nivel;
    }
    public void setNivel(Nivel nivel) {
            this.nivel = nivel;
    }
    //Metodos atributo: curso
    public Curso getCurso() {
            return curso;
    }
    public void setCurso(Curso curso) {
            this.curso = curso;
    }
    //Metodos atributo: seccion
    public Seccion getSeccion() {
            return seccion;
    }
    public void setSeccion(Seccion seccion) {
            this.seccion = seccion;
    }
    //Metodos atributo: jornada
    public Jornada getJornada() {
            return jornada;
    }
    public void setJornada(Jornada jornada) {
            this.jornada = jornada;
    }
    
    public static void llenarInformacionAlumno(Connection conn, ObservableList<Alumno> lista){
        String sentenciaSQL = "SELECT A.codigo, " + 
                                    "A.nombre, " +
                                    "A.rne, " +
                                    "A.sexo, " +
                                    "A.estado, " +
                                    "A.cod_periodo, " +
                                    "A.cod_nivel, " +
                                    "A.cod_curso, " +
                                    "A.cod_seccion, " +
                                    "A.cod_jornada, " +
                                    "B.periodo, " +
                                    "C.nivel, " +
                                    "D.curso, " +
                                    "E.seccion, " +
                                    "F.jornada " +
                              "FROM tbl_alumnos A " +
                              "INNER JOIN tbl_periodos B " +
                              "ON (A.cod_periodo = B.codigo) " +
                              "INNER JOIN tbl_niveles C " +
                              "ON (A.cod_nivel = C.codigo) " +
                              "INNER JOIN tbl_cursos D " +
                              "ON (A.cod_curso = D.codigo) " +
                              "INNER JOIN tbl_secciones E " +
                              "ON (A.cod_seccion = E.codigo) " +
                              "INNER JOIN tbl_jornadas F " +
                              "ON (A.cod_jornada = F.codigo)";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            int num = 0;
            while(resultado.next()){
                lista.add(new Alumno(resultado.getInt("codigo"), 
                                    resultado.getString("nombre"),
                                    resultado.getString("rne"),
                                    resultado.getString("sexo"),
                                    resultado.getString("estado"),
                                    new Periodo(resultado.getInt("cod_periodo"),
                                                resultado.getString("periodo")), 
                                    new Nivel(resultado.getInt("cod_nivel"),
                                              resultado.getString("nivel")),
                                    new Curso(resultado.getInt("cod_curso"),
                                              resultado.getString("curso")),
                                    new Seccion(resultado.getInt("cod_seccion"),
                                              resultado.getString("seccion")),
                                    new Jornada(resultado.getInt("cod_jornada"),
                                              resultado.getString("jornada"))
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion de los Alumnos.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
        
    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_alumnos(nombre, " +
                                                    "rne, " + 
                                                    "sexo, " + 
                                                    "estado, " + 
                                                    "cod_periodo, " + 
                                                    "cod_nivel, " + 
                                                    "cod_curso, " + 
                                                    "cod_seccion, " +
                                                    "cod_jornada)" + 
                              "VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, nombre.get());
            instruccion.setString(2, rne.get());
            instruccion.setString(3, sexo.get());
            instruccion.setString(4, estado.get());
            instruccion.setInt(5, periodo.getCodigo());
            instruccion.setInt(6, nivel.getCodigo());
            instruccion.setInt(7, curso.getCodigo());
            instruccion.setInt(8, seccion.getCodigo());
            instruccion.setInt(9, cod_jornada);
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                codigo_generado = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el Alumno.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            codigo_generado =0;
            return 0;
        }
    }
        
    public int modificarRegistro(Connection conn){
        String sentenciaSQL = "UPDATE tbl_alumnos " +
                              "SET nombre = ?, " +
                                  "rne = ?, " +
                                  "sexo = ?, " +
                                  "estado = ?, " +
                                  "cod_periodo = ?, " +
                                  "cod_nivel = ?, " +
                                  "cod_curso = ?, " +
                                  "cod_seccion = ?, " +
                                  "cod_jornada = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, nombre.get());
            instruccion.setString(2, rne.get());
            instruccion.setString(3, sexo.get());
            instruccion.setString(4, estado.get());
            instruccion.setInt(5, periodo.getCodigo());
            instruccion.setInt(6, nivel.getCodigo());
            instruccion.setInt(7, curso.getCodigo());
            instruccion.setInt(8, seccion.getCodigo());
            instruccion.setInt(9, cod_jornada);
            instruccion.setInt(10, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar el Alumno.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    public int eliminarRegistro(Connection conn){
        String sentenciaSQL = "DELETE FROM tbl_alumnos " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar el Alumno.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    @Override
    public String toString(){
        return nombre.get();
    }
    
}