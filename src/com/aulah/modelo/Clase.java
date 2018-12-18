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
 * Clase para gestionar los datos de Clase
 * @author hector
 */
public class Clase {
    public IntegerProperty codigo;
    public StringProperty clase;
    public Periodo cod_periodo;
    public Nivel cod_nivel;
    
    public static int codigo_generado = 0;//codigo_generado: variable para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    public Clase(int codigo, String clase, Periodo cod_periodo, Nivel cod_nivel) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.clase = new SimpleStringProperty(clase);
            this.cod_periodo = cod_periodo;
            this.cod_nivel = cod_nivel;
    }
    
    public Clase(int codigo, String clase) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.clase = new SimpleStringProperty(clase);
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
    //Metodos atributo: clase
    public String getClase() {
            return clase.get();
    }
    public void setClase(String clase) {
            this.clase = new SimpleStringProperty(clase);
    }
    public StringProperty ClaseProperty() {
            return clase;
    }
    //Metodos atributo: cod_periodo
    public Periodo getCod_periodo() {
            return cod_periodo;
    }
    public void setCod_periodo(Periodo cod_periodo) {
            this.cod_periodo = cod_periodo;
    }
    //Metodos atributo: cod_nivel
    public Nivel getCod_nivel() {
            return cod_nivel;
    }
    public void setCod_nivel(Nivel cod_nivel) {
            this.cod_nivel = cod_nivel;
    }
    
    public static void llenarInformacionNombreClases(Connection conn, ObservableList<Clase> lista){
        String sentenciaSQL = "SELECT codigo, " + 
                                    "clase " +
                              "FROM tbl_clases";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Clase(resultado.getInt("codigo"), 
                                    resultado.getString("clase")
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion de la Clase.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }   
    
    public static void llenarInformacionClase(Connection conn, ObservableList<Clase> lista){
        String sentenciaSQL = "SELECT A.codigo, " + 
                                    "A.clase, " +
                                    "A.cod_periodo, " +
                                    "A.cod_nivel, " +
                                    "B.periodo, " +
                                    "C.nivel " +
                              "FROM tbl_clases A " +
                              "INNER JOIN tbl_periodos B " +
                              "ON (A.cod_periodo = B.codigo) " +
                              "INNER JOIN tbl_niveles C " +
                              "ON (A.cod_nivel = C.codigo)";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Clase(resultado.getInt("codigo"), 
                                    resultado.getString("clase"), 
                                    new Periodo(resultado.getInt("cod_periodo"),
                                                resultado.getString("periodo")), 
                                    new Nivel(resultado.getInt("cod_nivel"),
                                              resultado.getString("nivel"))
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion de la Clase.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarInformacionClasePeriodoCurso(Connection conn, ObservableList<Clase> lista, int codigo_periodo, int codigo_curso){
        String sentenciaSQL = "SELECT A.codigo, " + 
                                    "A.clase, " +
                                    "A.cod_periodo, " +
                                    "A.cod_nivel, " +
                                    "B.periodo, " +
                                    "C.nivel " +
                              "FROM tbl_clases A " +
                              "INNER JOIN tbl_periodos B " +
                              "ON ((A.cod_periodo = B.codigo) AND (B.codigo = " + codigo_periodo + ")) " +
                              "INNER JOIN tbl_niveles C " +
                              "ON (A.cod_nivel = C.codigo) " +
                              "INNER JOIN tbl_cursos_clases D " + 
                              "ON ((A.codigo = D.cod_clase) AND (D.cod_curso = " + codigo_curso + "))";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new Clase(resultado.getInt("codigo"), 
                                    resultado.getString("clase"), 
                                    new Periodo(resultado.getInt("cod_periodo"),
                                                resultado.getString("periodo")), 
                                    new Nivel(resultado.getInt("cod_nivel"),
                                              resultado.getString("nivel"))
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion de la Clase para el Periodo.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
                
    public int guardarRegistro(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_clases(clase, " +
                                                    "cod_periodo, " + 
                                                    "cod_nivel) " + 
                              "VALUES(?,?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setString(1, clase.get());
            instruccion.setInt(2, cod_periodo.getCodigo());
            instruccion.setInt(3, cod_nivel.getCodigo());
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                codigo_generado = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar la Clase.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            codigo_generado =0;
            return 0;
        }
    }
        
    public int modificarRegistro(Connection conn){
        String sentenciaSQL = "UPDATE tbl_clases " +
                              "SET clase = ?, " +
                                  "cod_periodo = ?, " +
                                  "cod_nivel = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, clase.get());
            instruccion.setInt(2, cod_periodo.getCodigo());
            instruccion.setInt(3, cod_nivel.getCodigo());
            instruccion.setInt(4, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar la Clase.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    public int eliminarRegistro(Connection conn){
        String sentenciaSQL = "DELETE FROM tbl_clases " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar la Clase.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    @Override
    public String toString(){
        return clase.get();
    }
    
}
