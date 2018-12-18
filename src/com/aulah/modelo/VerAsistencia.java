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
import java.sql.Time;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Hector Armando Herrera
 */
public class VerAsistencia {
    
    private IntegerProperty codigo;
    private StringProperty asistencia;
    private Date fecha;
    private Time hora;
    private Periodo cod_periodo;
    private Semestre cod_semestre;
    private ParcialSemestral cod_parcial_semestral;
    private ParcialAnual cod_parcial_anual;
    private Curso cod_curso;
    private Seccion cod_seccion;
    private Clase cod_clase;

    public VerAsistencia(int codigo, String asistencia, Date fecha, Time hora, Periodo cod_periodo, Semestre cod_semestre, ParcialSemestral cod_parcial_semestral, Curso cod_curso, Seccion cod_seccion, Clase cod_clase){ 
	this.codigo = new SimpleIntegerProperty(codigo);
	this.asistencia = new SimpleStringProperty(asistencia);
	this.fecha = fecha;
	this.hora = hora;
	this.cod_periodo = cod_periodo;
	this.cod_semestre = cod_semestre;
	this.cod_parcial_semestral = cod_parcial_semestral;
	this.cod_curso = cod_curso;
	this.cod_seccion = cod_seccion;
	this.cod_clase = cod_clase;
    }
    
    public VerAsistencia(int codigo, String asistencia, Date fecha, Time hora, Periodo cod_periodo, ParcialAnual cod_parcial_anual, Curso cod_curso, Seccion cod_seccion, Clase cod_clase){ 
	this.codigo = new SimpleIntegerProperty(codigo);
	this.asistencia = new SimpleStringProperty(asistencia);
	this.fecha = fecha;
	this.hora = hora;
	this.cod_periodo = cod_periodo;
	this.cod_parcial_anual = cod_parcial_anual;
	this.cod_curso = cod_curso;
	this.cod_seccion = cod_seccion;
	this.cod_clase = cod_clase;
    }
    
    public VerAsistencia(int codigo, String asistencia, Date fecha, Time hora){ 
	this.codigo = new SimpleIntegerProperty(codigo);
	this.asistencia = new SimpleStringProperty(asistencia);
	this.fecha = fecha;
	this.hora = hora;
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
    //Metodos atributo: asistencia
    public String getAsistencia() {
            return asistencia.get();
    }
    public void setAsistencia(String asistencia) {
            this.asistencia = new SimpleStringProperty(asistencia);
    }
    public StringProperty AsistenciaProperty() {
            return asistencia;
    }
    //Metodos atributo: fecha
    public Date getFecha() {
            return fecha;
    }
    public void setFecha(Date fecha) {
            this.fecha = fecha;
    }
    //Metodos atributo: hora
    public Time getHora() {
            return hora;
    }
    public void setHora(Time hora) {
            this.hora = hora;
    }
    //Metodos atributo: cod_periodo
    public Periodo getCod_periodo() {
            return cod_periodo;
    }
    public void setCod_periodo(Periodo cod_periodo) {
            this.cod_periodo = cod_periodo;
    }
    //Metodos atributo: cod_semestre
    public Semestre getCod_semestre() {
            return cod_semestre;
    }
    public void setCod_semestre(Semestre cod_semestre) {
            this.cod_semestre = cod_semestre;
    }
    //Metodos atributo: cod_parcial_semestral
    public ParcialSemestral getCod_parcial_semestral() {
            return cod_parcial_semestral;
    }
    public void setCod_parcial_semestral(ParcialSemestral cod_parcial_semestral) {
            this.cod_parcial_semestral = cod_parcial_semestral;
    }
    //Metodos atributo: cod_parcial_anual
    public ParcialAnual getCod_parcial_anual() {
            return cod_parcial_anual;
    }
    public void setCod_parcial_anual(ParcialAnual cod_parcial_anual) {
            this.cod_parcial_anual = cod_parcial_anual;
    }
    //Metodos atributo: cod_curso
    public Curso getCod_curso() {
            return cod_curso;
    }
    public void setCod_curso(Curso cod_curso) {
            this.cod_curso = cod_curso;
    }
    //Metodos atributo: cod_seccion
    public Seccion getCod_seccion() {
            return cod_seccion;
    }
    public void setCod_seccion(Seccion cod_seccion) {
            this.cod_seccion = cod_seccion;
    }
    //Metodos atributo: cod_clase
    public Clase getCod_clase() {
            return cod_clase;
    }
    public void setCod_clase(Clase cod_clase) {
            this.cod_clase = cod_clase;
    } 
        
        public static void llenarInformacionAsistenciasSemestrales(Connection conn, ObservableList lista, int cod_periodo, int cod_semestre, int cod_parcial_semestral, int cod_curso, int cod_seccion, int cod_clase){
        String sentenciaSQL = "SELECT A.codigo, " + 
                                    "A.asistencia, " +
                                    "A.fecha, " +
                                    "A.hora " +
                              "FROM tbl_asistencias A " +
                              "WHERE (A.cod_periodo = " + cod_periodo + ") " +
                              "AND (A.cod_semestre = " + cod_semestre + ") " +
                              "AND (A.cod_parcial_semestral = " + cod_parcial_semestral + ") " +
                              "AND (A.cod_curso = " + cod_curso + ") " +                              
                              "AND (A.cod_seccion = " + cod_seccion + ") " +
                              "AND (A.cod_clase = " + cod_clase + ")";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            int num = 0;
            while(resultado.next()){
                lista.add(new VerAsistencia(
                                    resultado.getInt("codigo"),
                                    resultado.getString("asistencia"),
                                    resultado.getDate("fecha"),
                                    resultado.getTime("hora")
                              )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener las Asistencias.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
        
    public static void eliminarColumnaTabla(Connection conn, String nombre_tabla, String nombre_columna){
        String sentenciaSQL = "ALTER TABLE " + nombre_tabla + " DROP COLUMN " + nombre_columna;
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.executeUpdate();
            System.out.println("\tSe eliminó la columna " + nombre_columna + " de la tabla " + nombre_tabla);
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar la columna " + nombre_columna + " en la tabla " + nombre_tabla + ".\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
        }
    }
        
}
