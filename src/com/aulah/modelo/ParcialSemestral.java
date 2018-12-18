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
import static com.aulah.modelo.ParcialSemestral.codigo_generado;
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

/**
 *
 * @author hector
 */
public class ParcialSemestral {
    public IntegerProperty codigo;
    public StringProperty parcial;
    public Date fecha_inicio;
    public Date fecha_final;
    public Semestre cod_semestre;
    
    public static int codigo_generado = 0;//codigo_generado: se usa para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    public ParcialSemestral(int codigo, String parcial, Date fecha_inicio, Date fecha_final, Semestre cod_semestre) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.parcial = new SimpleStringProperty(parcial);
            this.fecha_inicio = fecha_inicio;
            this.fecha_final = fecha_final;
            this.cod_semestre = cod_semestre;
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
    //Metodos atributo: parcial
    public String getParcial() {
            return parcial.get();
    }
    public void setParcial(String parcial) {
            this.parcial = new SimpleStringProperty(parcial);
    }
    public StringProperty ParcialProperty() {
            return parcial;
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
    //Metodos atributo: cod_semestre
    public Semestre getCod_semestre() {
            return cod_semestre;
    }
    public void setCod_semestre(Semestre cod_semestre) {
            this.cod_semestre = cod_semestre;
    }
    
    public static void llenarInformacionParcialSemestrales(Connection conn, ObservableList<ParcialSemestral> lista){
        String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.parcial, " +
                                    "A.fecha_inicio, " +
                                    "A.fecha_final, " +
                                    "A.cod_semestre, " +
                                    "B.semestre " +
                              "FROM tbl_parciales_semestrales A " +
                              "INNER JOIN tbl_semestres B " +
                              "ON ((A.cod_semestre = B.codigo))";
        try {
            Statement instruccion = conn.createStatement();
            ResultSet resultado = instruccion.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new ParcialSemestral(
                                    resultado.getInt("codigo"), 
                                    resultado.getString("parcial"),
                                    resultado.getDate("fecha_inicio"),
                                    resultado.getDate("fecha_final"),
                                    new Semestre(
                                            resultado.getInt("cod_semestre"),
                                            resultado.getString("semestre")
                                        )
                              )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error al tratar de obtener los datos para llenar el Parcial." +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }
    
    public static void llenarInformacionParcialSemestre(Connection conn, ObservableList<ParcialSemestral> lista, int codigo_semestre){
        String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.parcial, " +
                                    "A.fecha_inicio, " +
                                    "A.fecha_final, " +
                                    "A.cod_semestre, " +
                                    "B.semestre " +
                              "FROM tbl_parciales_semestrales A " +
                              "INNER JOIN tbl_semestres B " +
                              "ON ((A.cod_semestre = B.codigo) AND (B.codigo = " + codigo_semestre + "))";
        try {
            Statement instruccion = conn.createStatement();
            ResultSet resultado = instruccion.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new ParcialSemestral(
                                    resultado.getInt("codigo"), 
                                    resultado.getString("parcial"),
                                    resultado.getDate("fecha_inicio"),
                                    resultado.getDate("fecha_final"),
                                    new Semestre(
                                            resultado.getInt("cod_semestre"),
                                            resultado.getString("semestre")
                                        )
                              )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error al tratar de obtener los datos para llenar el Parcial." +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }
    
    public static void llenarInformacionParcialSemestralPeriodo(Connection conn, ObservableList<ParcialSemestral> lista, int codigo_periodo){
        String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.parcial, " +
                                    "A.fecha_inicio, " +
                                    "A.fecha_final, " +
                                    "A.cod_semestre, " +
                                    "B.semestre " +
                              "FROM tbl_parciales_semestrales A " +
                              "INNER JOIN tbl_semestres B " +
                              "ON (A.cod_semestre = B.codigo) " +
                              "INNER JOIN tbl_periodos C " +
                              "ON((B.cod_periodo = C.codigo) AND (C.codigo = " + codigo_periodo + ")) " +
                              "INNER JOIN tbl_tipos_periodo D " + 
                              "ON((C.cod_tipo_periodo = D.codigo) AND (D.tipo_periodo = 'SEMESTRAL'))";
        try {
            Statement instruccion = conn.createStatement();
            ResultSet resultado = instruccion.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new ParcialSemestral(
                                    resultado.getInt("codigo"), 
                                    resultado.getString("parcial"),
                                    resultado.getDate("fecha_inicio"),
                                    resultado.getDate("fecha_final"),
                                    new Semestre(
                                            resultado.getInt("cod_semestre"),
                                            resultado.getString("semestre")
                                        )
                              )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error al tratar de obtener los Semestres para el Periodo especificado." +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }

    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_parciales_semestrales(" +
                                    "parcial, " +
                                    "fecha_inicio, " +
                                    "fecha_final, " +
                                    "cod_semestre) " +
                              "VALUES(?,?,?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, parcial.get());
            instruccion.setDate(2, fecha_inicio);
            instruccion.setDate(3, fecha_final);
            instruccion.setInt(4, cod_semestre.getCodigo());
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                codigo_generado = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el Parcial.\n" +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            codigo_generado =0;
            return 0;
        }

    }

    public int modificarRegistro(Connection conn){
        String sentenciaSQL = "UPDATE tbl_parciales_semestrales " +
                              "SET parcial = ?, " +
                                  "fecha_inicio = ?, " +
                                  "fecha_final = ?, " +
                                  "cod_semestre = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, parcial.get());
            instruccion.setDate(2, fecha_inicio);
            instruccion.setDate(3, fecha_final);
            instruccion.setInt(4, cod_semestre.getCodigo());
            instruccion.setInt(5, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar el Parcial.\n"+
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            return 0;
        }
    }

    public int eliminarRegistro(Connection conn){
        String sentenciaSQL = "DELETE FROM tbl_parciales_semestrales " + 
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar el Parcial.\n"+
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            return 0;
        }
    }

    @Override
    public String toString(){
        return parcial.get();
    }
    
}
