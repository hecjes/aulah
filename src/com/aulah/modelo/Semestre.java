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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Semestre{
    public IntegerProperty codigo;
    public StringProperty semestre;
    public Date fecha_inicio;
    public Date fecha_final;
    public Periodo periodo = new Periodo(0, null);
    
    public static int codigo_generado = 0;//codigo_generado: se usa para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    public Semestre(int codigo, String semestre, Date fecha_inicio, Date fecha_final, Periodo cod_periodo) { 
        this.codigo = new SimpleIntegerProperty(codigo);
        this.semestre = new SimpleStringProperty(semestre);
        this.fecha_inicio = fecha_inicio;
        this.fecha_final = fecha_final;
        this.periodo = cod_periodo;
    }
    
    public Semestre(int codigo, String semestre) { 
        this.codigo = new SimpleIntegerProperty(codigo);
        this.semestre = new SimpleStringProperty(semestre);
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
    //Metodos atributo: semestre
    public String getSemestre() {
            return semestre.get();
    }
    public void setSemestre(String semestre) {
            this.semestre = new SimpleStringProperty(semestre);
    }
    public StringProperty SemestreProperty() {
            return semestre;
    }
    //Metodos atributo: fecha_inicio
    public Date getFecha_inicio() {
            return fecha_inicio;
    }
    public void setFecha_inicio(Date fecha_inicio) {
            this.fecha_inicio = fecha_inicio;
    }
    //Metodos atributo: fecha_final
    public Date getFecha_final() {
            return fecha_final;
    }
    public void setFecha_final(Date fecha_final) {
            this.fecha_final = fecha_final;
    }
    //Metodos atributo: cod_periodo
    public Periodo getCod_periodo() {
            return periodo;
    }
    public void setCod_periodo(Periodo cod_periodo) {
            this.periodo = cod_periodo;
    }
    
   
    
    public static void llenarInformacionSemestres(Connection conn, ObservableList<Semestre> lista){
        String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.semestre, " +
                                    "A.fecha_inicio, " +
                                    "A.fecha_final, " +
                                    "A.cod_periodo, " +
                                    "B.periodo " +
                              "FROM tbl_semestres A " +
                              "INNER JOIN tbl_periodos B " +
                              "ON (A.cod_periodo = B.codigo)";
        try {
            Statement instruccion = conn.createStatement();
            ResultSet resultado = instruccion.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Semestre(
                                    resultado.getInt("codigo"), 
                                    resultado.getString("semestre"),
                                    resultado.getDate("fecha_inicio"),
                                    resultado.getDate("fecha_final"),
                                    new Periodo(
                                            resultado.getInt("cod_periodo"),
                                            resultado.getString("periodo")
                                    )
                          )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error al tratar de obtener los datos para llenar los Semestres." +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }
    
    public static void llenarInformacionSemestresPeriodo(Connection conn, ObservableList<Semestre> lista, int codigo_periodo){
        String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.semestre, " +
                                    "A.fecha_inicio, " +
                                    "A.fecha_final, " +
                                    "A.cod_periodo, " +
                                    "B.periodo " +
                              "FROM tbl_semestres A " +
                              "INNER JOIN tbl_periodos B " +
                              "ON ((A.cod_periodo = B.codigo) AND (B.codigo = " + codigo_periodo + "))" +
                              "INNER JOIN tbl_tipos_periodo C " + 
                              "ON ((B.cod_tipo_periodo = C.codigo) AND (C.tipo_periodo = 'SEMESTRAL'))";
        try {
            Statement instruccion = conn.createStatement();
            ResultSet resultado = instruccion.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Semestre(
                                    resultado.getInt("codigo"), 
                                    resultado.getString("semestre"),
                                    resultado.getDate("fecha_inicio"),
                                    resultado.getDate("fecha_final"),
                                    new Periodo(
                                            resultado.getInt("cod_periodo"),
                                            resultado.getString("periodo")
                                    )
                          )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error al tratar de obtener los datos para llenar los Semestres del Periodo" +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }
    
    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_semestres(" +
                                    "semestre, " +
                                    "fecha_inicio, " +
                                    "fecha_final, " +
                                    "cod_periodo) " +
                              "VALUES(?,?,?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, semestre.get());
            instruccion.setDate(2, fecha_inicio);
            instruccion.setDate(3, fecha_final);
            instruccion.setInt(4, periodo.getCodigo());
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                codigo_generado = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el Semestre.\n" +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            codigo_generado =0;
            return 0;
        }

    }

    public int modificarRegistro(Connection conn){
        String sentenciaSQL = "UPDATE tbl_semestres " +
                              "SET semestre = ?, " +
                                  "fecha_inicio = ?, " +
                                  "fecha_final = ?, " +
                                  "cod_periodo = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, semestre.get());
            instruccion.setDate(2, fecha_inicio);
            instruccion.setDate(3, fecha_final);
            instruccion.setInt(4, periodo.getCodigo());
            instruccion.setInt(5, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar el Semestre.\n"+
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            return 0;
        }
    }

    public int eliminarRegistro(Connection conn){
        String sentenciaSQL = "DELETE FROM tbl_semestres " + 
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar el Semestre.\n"+
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            return 0;
        }
    }

    @Override
    public String toString(){
        return semestre.get();
    }
    
    
}
