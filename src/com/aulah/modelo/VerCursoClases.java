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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *Clase modelo para el ObservableList que llena el TableView de Clases en CursosClases.fxml
 * 
 * @author hector
 */
public class VerCursoClases {
    private IntegerProperty codigo;
    private StringProperty clase;
    private Nivel nivel;
        
    public VerCursoClases(int codigo, String clase, Nivel nivel, boolean seleccionado){
        this.codigo = new SimpleIntegerProperty(codigo);
        this.clase = new SimpleStringProperty(clase);
        this.nivel = nivel;
    }
    
    public VerCursoClases(int codigo, String clase, Nivel nivel){
        this.codigo = new SimpleIntegerProperty(codigo);
        this.clase = new SimpleStringProperty(clase);
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
    //Metodos atributo: nivel
    public Nivel getNivel(){
        return nivel;
    }
    public void setNivel(Nivel nivel){
        this.nivel = nivel;
    }
      
    public static void llenarInformacionClaseCurso(Connection conn, ObservableList<VerCursoClases> lista, int codigo_curso){
        String sentenciaSQL = "SELECT A.codigo, " + 
                                    "A.clase, " +
                                    "A.cod_nivel, " +
                                    "B.nivel " +
                              "FROM tbl_clases A " +
                              "INNER JOIN tbl_niveles B " +                              
                              "ON (A.cod_nivel = B.codigo) " +
                              "INNER JOIN tbl_cursos_clases C " +
                              "ON ((C.cod_curso = " + codigo_curso +") AND (A.codigo = C.cod_clase))";
        try {
            boolean SELECCIONADO = false;
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new VerCursoClases(resultado.getInt("codigo"), 
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
       
}
