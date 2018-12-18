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

public class Periodo{
    private IntegerProperty codigo;
    private StringProperty periodo;
    private Date fecha_inicio;
    private Date fecha_final;
    private Centro centro;
    private TipoPeriodo tipo_periodo;
    
    public static int codigo_generado = 0;//codigo_generado: se usa para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    public Periodo(int codigo, String periodo, Date fecha_inicio, Date fecha_final, Centro cod_centro, TipoPeriodo cod_tipo_periodo) { 
        this.codigo = new SimpleIntegerProperty(codigo);
        this.periodo = new SimpleStringProperty(periodo);
        this.fecha_inicio = fecha_inicio;
        this.fecha_final = fecha_final;
        this.centro = cod_centro;
        this.tipo_periodo = cod_tipo_periodo;
    }

    public Periodo(int codigo, String periodo) {
        this.codigo = new SimpleIntegerProperty(codigo);
        this.periodo = new SimpleStringProperty(periodo);
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
    //Metodos atributo: periodo
    public String getPeriodo() {
            return periodo.get();
    }
    public void setPeriodo(String periodo) {
            this.periodo = new SimpleStringProperty(periodo);
    }
    public StringProperty PeriodoProperty() {
            return periodo;
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
    //Metodos atributo: cod_centro
    public Centro getCod_centro() {
            return centro;
    }
    public void setCod_centro(Centro cod_centro) {
            this.centro = cod_centro;
    }
    //Metodos atributo: cod_tipo_periodo
    public TipoPeriodo getCod_tipo_periodo() {
            return tipo_periodo;
    }
    public void setCod_tipo_periodo(TipoPeriodo cod_tipo_periodo) {
            this.tipo_periodo = cod_tipo_periodo;
    }
    
    //Llena un ObservableList con el codigo y el periodo para alimentar algun ComboBox
    public static void llenarPeriodosAnuales(Connection connection, ObservableList<Periodo> lista){
	String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.periodo " +
                              "FROM tbl_periodos A " +
                              "INNER JOIN tbl_tipos_periodo B " +
                              "ON (A.cod_tipo_periodo = B.codigo AND B.tipo_periodo = 'ANUAL')";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while (resultado.next()){
                lista.add(new Periodo(
                        resultado.getInt("codigo"),
                        resultado.getString("periodo")
                    )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error al tratar de obtener los datos para llenar los Periodos." +
                        "Método: "+getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }
    
    //Permite saber si un determinado periodo es ANUAL
    public static boolean periodoEsAnual(Connection connection, int codigo_periodo){
        boolean esAnual = false;
	String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.periodo " +
                              "FROM tbl_periodos A " +
                              "INNER JOIN tbl_tipos_periodo B " +
                              "ON ((A.codigo = " + codigo_periodo + ") AND (A.cod_tipo_periodo = B.codigo) AND (B.tipo_periodo = 'ANUAL'))";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while (resultado.next()){
                esAnual = true;
            }
        } catch (SQLException ex) {
            esAnual = false;
            System.err.println(
                        "Error al tratar de saber si el Periodo es ANUAL." +
                        "Método: "+getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
        return  esAnual;
    }
    
    //Permite saber si un determinado periodo es SEMESTRAL
    public static boolean periodoEsSemestral(Connection connection, int codigo_periodo){
        boolean esSemestral = false;
	String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.periodo " +
                              "FROM tbl_periodos A " +
                              "INNER JOIN tbl_tipos_periodo B " +
                              "ON ((A.codigo = " + codigo_periodo + ") AND (A.cod_tipo_periodo = B.codigo) AND (B.tipo_periodo = 'SEMESTRAL'))";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while (resultado.next()){
                esSemestral = true;
            }
        } catch (SQLException ex) {
            esSemestral = false;
            System.err.println(
                        "Error al tratar de saber si el Periodo es SEMESTRAL." +
                        "Método: "+getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
        return  esSemestral;
    }
    
    //Llena un ObservableList con el codigo y el periodo para alimentar algun ComboBox
    public static void llenarNombrePeriodos(Connection connection, ObservableList<Periodo> lista){
	String sentenciaSQL = "SELECT codigo, " +
                                    "periodo " +
                              "FROM tbl_periodos";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while (resultado.next()){
                lista.add(new Periodo(
                        resultado.getInt("codigo"),
                        resultado.getString("periodo")
                    )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error al tratar de obtener los datos para llenar los Periodos." +
                        "Método: "+getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }
    
    public static void llenarInformacionPeriodo(Connection conn, ObservableList<Periodo> lista){
        String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.periodo, " +
                                    "A.fecha_inicio, " +
                                    "A.fecha_final, " +
                                    "A.cod_centro, " +
                                    "A.cod_tipo_periodo, " +
                                    "B.centro, " +
                                    "B.clave, " +
                                    "B.direccion, " +
                                    "C.tipo_periodo " +
                              "FROM tbl_periodos A " +
                              "INNER JOIN tbl_centros B " +
                              "ON (A.cod_centro = B.codigo) " +
                              "INNER JOIN tbl_tipos_periodo C " +
                              "ON (A.cod_tipo_periodo = C.codigo)";
        try {
            Statement instruccion = conn.createStatement();
            ResultSet resultado = instruccion.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Periodo(
                                    resultado.getInt("codigo"), 
                                    resultado.getString("periodo"),
                                    resultado.getDate("fecha_inicio"),
                                    resultado.getDate("fecha_final"),
                                    new Centro(
                                            resultado.getInt("cod_centro"),
                                            resultado.getString("centro"),
                                            resultado.getString("clave"),
                                            resultado.getString("direccion")
                                    ),
                                    new TipoPeriodo(
                                            resultado.getInt("cod_tipo_periodo"),
                                            resultado.getString("tipo_periodo")
                                    )
                          )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error al tratar de obtener los datos para llenar los Periodos." +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
        }
    }

    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_periodos(" +
                                    "periodo, " +
                                    "fecha_inicio, " +
                                    "fecha_final, " +
                                    "cod_centro, " +
                                    "cod_tipo_periodo) " +
                              "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, periodo.get());
            instruccion.setDate(2, fecha_inicio);
            instruccion.setDate(3, fecha_final);
            instruccion.setInt(4, centro.getCodigo());
            instruccion.setInt(5, tipo_periodo.getCodigo());
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                codigo_generado = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el Periodo.\n" +
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            codigo_generado =0;
            return 0;
        }

    }

    public int modificarRegistro(Connection conn){
        String sentenciaSQL = "UPDATE tbl_periodos " +
                              "SET periodo = ?, " +
                                  "fecha_inicio = ?, " +
                                  "fecha_final = ?, " +
                                  "cod_centro = ?, " +
                                  "cod_tipo_periodo = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, periodo.get());
            instruccion.setDate(2, fecha_inicio);
            instruccion.setDate(3, fecha_final);
            instruccion.setInt(4, centro.getCodigo());
            instruccion.setInt(5, tipo_periodo.getCodigo());
            instruccion.setInt(6, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar el Periodo.\n"+
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            return 0;
        }
    }

    public int eliminarRegistro(Connection conn){
        String sentenciaSQL = "DELETE FROM tbl_periodos " + 
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar el Periodo.\n"+
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL + "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            return 0;
        }
    }

    @Override
    public String toString(){
        return periodo.get();
    }
    
}
