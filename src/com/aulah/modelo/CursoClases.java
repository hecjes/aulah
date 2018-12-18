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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *Clase modelo para el ObservableList que llena el TableView de Clases en CursosClases.fxml
 * 
 * @author hector
 */
public class CursoClases {
    private IntegerProperty codigo;
    private StringProperty clase;
    //private Nivel cod_nivel;
    private Nivel nivel;
    private BooleanProperty seleccionado;
    
    public CursoClases(int codigo, String clase, Nivel nivel, boolean seleccionado){
        this.codigo = new SimpleIntegerProperty(codigo);
        this.clase = new SimpleStringProperty(clase);
        this.nivel = nivel;
        this.seleccionado  = new SimpleBooleanProperty(seleccionado);
    }
    
    public CursoClases(int codigo, String clase, Nivel nivel){
        this.codigo = new SimpleIntegerProperty(codigo);
        this.clase = new SimpleStringProperty(clase);
        //this.cod_nivel = cod_nivel;
        this.nivel = nivel;
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
    //Metodos atributo: cod_nivel
//    public Nivel getCodNivel(){
//        return cod_nivel;
//    }
//    public void setCodNivel(Nivel cod_nivel){
//        this.cod_nivel = cod_nivel;
//    }
    //Metodos atributo: nivel
    public Nivel getNivel(){
        return nivel;
    }
    public void setNivel(Nivel nivel){
        this.nivel = nivel;
    }
    
    //Metodos atributo: selecionado
    public final Boolean getSelecionado() {
        return seleccionado.get();
    }
    public final void setSelecionado(Boolean seleccionado) {
        this.seleccionado.set(seleccionado);
    }
    public final BooleanProperty selecionadoProperty(){
        return this.seleccionado;
    }
    
    public static void llenarInformacionClasesNivel(Connection conn, ObservableList<CursoClases> lista, int codigo_nivel, int codigo_curso){
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
                                "ON ((A.cod_nivel = C.codigo) AND (C.codigo = " + codigo_nivel + ")) " +
                                "LEFT JOIN tbl_cursos_clases D " +
                                "ON (A.codigo = D.cod_clase) " +
                                "LEFT JOIN tbl_cursos E " +
                                "ON ((D.cod_curso = E.codigo) AND (E.codigo = " + codigo_curso + ")) " +
                                "WHERE D.cod_clase IS NULL";
        try {
            boolean SELECCIONADO = false;
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new CursoClases(resultado.getInt("codigo"), 
                                          resultado.getString("clase"), 
                                          new Nivel(resultado.getInt("cod_nivel"),
                                              resultado.getString("nivel")),
                                          SELECCIONADO
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion de la Clase por Nivel.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarInformacionClasesAsignadas(Connection conn, ObservableList<CursoClases> lista, int codigo_curso){
        String sentenciaSQL = 
            "SELECT A.codigo, " +
		"A.clase, " +
                "A.cod_periodo, " +
                "A.cod_nivel, " +
                "E.periodo, " +
                "B.nivel " +
            "FROM tbl_clases A " +
            "INNER JOIN tbl_niveles B " +
            "ON (A.cod_nivel = B.codigo) " +
            "INNER JOIN tbl_cursos C " +
            "ON ((B.codigo = C.cod_nivel) AND (C.codigo = " + codigo_curso +")) " +
            "INNER JOIN tbl_cursos_clases D " +
            "ON ((A.codigo = D.cod_clase) AND (C.codigo = D.cod_curso)) " +
            "INNER JOIN tbl_periodos E " +
            "ON (A.cod_periodo = E.codigo)";
        try {
            boolean SELECCIONADO = false;
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new CursoClases(resultado.getInt("codigo"), 
                                          resultado.getString("clase"), 
                                          new Nivel(resultado.getInt("cod_nivel"),
                                              resultado.getString("nivel")),
                                          SELECCIONADO
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion de la Clase para el Curso.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void llenarInformacionClasesSinAsignar(Connection conn, ObservableList<CursoClases> lista, int codigo_curso){
        String sentenciaSQL = 
            "SELECT A.codigo, " +
		"A.clase, " +
                "A.cod_periodo, " +
                "A.cod_nivel, " +
                "E.periodo, " +
                "B.nivel " +
            "FROM tbl_clases A " +
            "INNER JOIN tbl_niveles B " +
            "ON (A.cod_nivel = B.codigo) " +
            "INNER JOIN tbl_cursos C " +
            "ON ((B.codigo = C.cod_nivel) AND (C.codigo = " + codigo_curso +")) " +
            "INNER JOIN tbl_cursos_clases D " +
            "ON ((A.codigo != D.cod_clase) AND (C.codigo != D.cod_curso)) " +
            "INNER JOIN tbl_periodos E " +
            "ON (A.cod_periodo = E.codigo)";
        try {
            boolean SELECCIONADO = false;
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new CursoClases(resultado.getInt("codigo"), 
                                          resultado.getString("clase"), 
                                          new Nivel(resultado.getInt("cod_nivel"),
                                              resultado.getString("nivel")),
                                          SELECCIONADO
                                    )
                         );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la informacion de la Clase para el Curso.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static int guardarRegistro(Connection conn, int cod_curso, int cod_clase){
        String sentenciaSQL = "INSERT INTO tbl_cursos_clases(cod_curso, " +
                                                          "cod_clase) " + 
                              "VALUES(?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, cod_curso);
            instruccion.setInt(2, cod_clase);
            int num = instruccion.executeUpdate();
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el cod_curso y cod_clase.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            return 0;
        }
    }
    
    public static int eliminarRegistro(Connection conn, int cod_curso, int cod_clase){
        String sentenciaSQL = "DELETE FROM tbl_cursos_clases " +
                                "WHERE cod_curso = ? " +
                                "AND cod_clase = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, cod_curso);
            instruccion.setInt(2, cod_clase);
            int num = instruccion.executeUpdate();
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar el cod_curso y cod_clase.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            return 0;
        }
    }
    
}
