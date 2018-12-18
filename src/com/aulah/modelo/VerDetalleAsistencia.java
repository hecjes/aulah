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
import com.aulah.utilidades.AsistenciaArrLst;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author hector
 */
public class VerDetalleAsistencia {
    
    public IntegerProperty numero;
    public IntegerProperty codigo_alumno;
    public StringProperty nombre_alumno;
    public int codigo_asistencia;
    public StringProperty asistio;
    public Opciones opcion;
    
    public VerDetalleAsistencia(String nombre, String asistio){
        this.nombre_alumno = new SimpleStringProperty(nombre);
        this.asistio = new SimpleStringProperty(asistio);
    }
    
    public VerDetalleAsistencia(String nombre, Opciones opcion){
        this.nombre_alumno = new SimpleStringProperty(nombre);
        this.opcion = opcion;
    }
    
    public VerDetalleAsistencia(int numero, int codigo, String nombre, Opciones opcion){
        this.numero = new SimpleIntegerProperty(numero);
        this.codigo_alumno = new SimpleIntegerProperty(codigo);
        this.nombre_alumno = new SimpleStringProperty(nombre);
        this.opcion = opcion;
    }
    
    public enum Opciones {
        SI, NO, EXCUSA
    }
    
    //Metodos atributo : numero
    public int getNumero(){
        return numero.get();
    }
    public void setNumero(int numero){
        this.numero = new SimpleIntegerProperty(numero);
    }
    public IntegerProperty NumeroProperty() {
            return numero;
    }
    //Metodos atributo: codigo_alumno
    public int getCodigo() {
            return codigo_alumno.get();
    }
    public void setCodigo(int codigo) {
            this.codigo_alumno = new SimpleIntegerProperty(codigo);
    }
    public IntegerProperty CodigoProperty() {
            return codigo_alumno;
    }
    //Metodos atributo: nombre_alumno
    public String getNombre() {
            return nombre_alumno.get();
    }
    public void setNombre(String nombre) {
            this.nombre_alumno = new SimpleStringProperty(nombre);
    }
    public StringProperty NombreProperty() {
            return nombre_alumno;
    }
    //Metodo atributo> opciones
    public Opciones getOpcion() {
        return opcion;
    }
    public void setOpciones(VerDetalleAsistencia.Opciones opcion){
        this.opcion = opcion;
    }
    //Metodo atributo: codigo_asistencia
    public void setCodigoAsistencia(int codigo_asistencia){
        this.codigo_asistencia = codigo_asistencia;
    }
    public int getCodigoAsistencia(){
        return codigo_asistencia;
    }
    //Metodos atributo: asistio
    public String getAsistio() {
            return asistio.get();
    }
    public void setAsistio(String asistio) {
            this.asistio = new SimpleStringProperty(asistio);
    }
    public StringProperty Asistio() {
            return asistio;
    }
        
    public static void llenarInformacionDetalleAsistenciaAlumno(Connection conn, ArrayList<AsistenciaArrLst> lista, String nombre_tabla, String nombre_columna){
        String sentenciaSQL = "SELECT A.codigo, " +
                                    "A.nombre, " +
                                    "B." + nombre_columna + " " +
                              "FROM tbl_alumnos A " +
                              "INNER JOIN " + nombre_tabla + " B " + 
                              "ON (A.codigo = B.codigo_alumno)";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            int num = 0;
            while(resultado.next()){
                lista.add(new AsistenciaArrLst(
                                    resultado.getInt("codigo"),
                                    resultado.getString("nombre"),
                                    resultado.getString(nombre_columna)
                              )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener los nombres de los Alumnos para para el Detalle de la Asistencia.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void modificarDetalleAsistencia(String nombre_tabla, String nombre_columna, int codigo_alumno, String opcion, Connection conn){
        String sentenciaSQL = "UPDATE `" + nombre_tabla + "` SET `" + nombre_columna + "` = ? WHERE codigo_alumno = " + codigo_alumno + "";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, opcion);
            instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar el registro de detalle de Asistencia en la tabla: " + nombre_tabla + " en la columna: " + nombre_columna + ".\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    @Override
    public String toString(){
        return nombre_alumno.get();
    }
}
