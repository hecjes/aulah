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

/**
 * Clase para gestionar los datos de ParcialAnual
 *
 * @author hector
 */
public class ParcialAnual {
    public IntegerProperty codigo;
    public StringProperty parcial;
    public Date fecha_inicio;
    public Date fecha_final;
    public Periodo cod_periodo;
    
    public static int codigo_generado = 0;//codigo_generado: se usa para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    public ParcialAnual(int codigo, String parcial, Date fecha_inicio, Date fecha_final, Periodo cod_periodo) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.parcial = new SimpleStringProperty(parcial);
            this.fecha_inicio = fecha_inicio;
            this.fecha_final = fecha_final;
            this.cod_periodo = cod_periodo;
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
    //Metodos atributo: cod_periodo
    public Periodo getCod_periodo() {
            return cod_periodo;
    }
    public void setCod_periodo(Periodo cod_periodo) {
            this.cod_periodo = cod_periodo;
    }
    
    public static void llenarInformacionParcialAnual(Connection conn, ObservableList<ParcialAnual> lista){
        String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.parcial, " +
                                    "A.fecha_inicio, " +
                                    "A.fecha_final, " +
                                    "A.cod_periodo, " +
                                    "B.periodo " +
                              "FROM tbl_parciales_anuales A " +
                              "INNER JOIN tbl_periodos B " +
                              "ON (A.cod_periodo = B.codigo) " +
                              "INNER JOIN tbl_tipos_periodo C " +
                              "ON (C.tipo_periodo = 'ANUAL')";
        try {
            Statement instruccion = conn.createStatement();
            ResultSet resultado = instruccion.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new ParcialAnual(
                                    resultado.getInt("codigo"), 
                                    resultado.getString("parcial"),
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
                        "Error al tratar de obtener los datos para llenar el Parcial." +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }
    
    public static void llenarInformacionParcialAnualPeriodo(Connection conn, ObservableList<ParcialAnual> lista, int codigo_periodo){
        String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.parcial, " +
                                    "A.fecha_inicio, " +
                                    "A.fecha_final, " +
                                    "A.cod_periodo, " +
                                    "B.periodo " +
                              "FROM tbl_parciales_anuales A " +
                              "INNER JOIN tbl_periodos B " +
                              "ON ((A.cod_periodo = B.codigo) AND (B.codigo = " + codigo_periodo + ")) " +
                              "INNER JOIN tbl_tipos_periodo C " +
                              "ON (C.tipo_periodo = 'ANUAL')";
        try {
            Statement instruccion = conn.createStatement();
            ResultSet resultado = instruccion.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new ParcialAnual(
                                    resultado.getInt("codigo"), 
                                    resultado.getString("parcial"),
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
                        "Error al tratar de obtener los datos para llenar el Parcial." +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }

    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_parciales_anuales(" +
                                    "parcial, " +
                                    "fecha_inicio, " +
                                    "fecha_final, " +
                                    "cod_periodo) " +
                              "VALUES(?,?,?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, parcial.get());
            instruccion.setDate(2, fecha_inicio);
            instruccion.setDate(3, fecha_final);
            instruccion.setInt(4, cod_periodo.getCodigo());
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
        String sentenciaSQL = "UPDATE tbl_parciales_anuales " +
                              "SET parcial = ?, " +
                                  "fecha_inicio = ?, " +
                                  "fecha_final = ?, " +
                                  "cod_periodo = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, parcial.get());
            instruccion.setDate(2, fecha_inicio);
            instruccion.setDate(3, fecha_final);
            instruccion.setInt(4, cod_periodo.getCodigo());
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
        String sentenciaSQL = "DELETE FROM tbl_parciales_anuales " + 
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
