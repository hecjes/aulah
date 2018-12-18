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

package com.aulah.controlador;

import com.aulah.modelo.LimpiarTablas;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Clase controlador para LimpiarTablas.fxml
 *
 * @author hector
 */
public class LimpiarTablas_ctrl implements Initializable {
    
    @FXML private Button btnSalir;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  
    
    @FXML
    private void limpiarTabla_tbl_alumnos(){
        String nombre_tabla = "tbl_alumnos";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_cursos_clases(){
        String nombre_tabla = "tbl_cursos_clases";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_secciones(){
        String nombre_tabla = "tbl_secciones";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_cursos(){
        String nombre_tabla = "tbl_cursos";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_clases(){
        String nombre_tabla = "tbl_clases";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_niveles(){
        String nombre_tabla = "tbl_niveles";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_parciales_anuales(){
        String nombre_tabla = "tbl_parciales_anuales";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_parciales_semestrales(){
        String nombre_tabla = "tbl_parciales_semestrales";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_semestres(){
        String nombre_tabla = "tbl_semestres";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_jornadas(){
        String nombre_tabla = "tbl_jornadas";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_periodos(){
        String nombre_tabla = "tbl_periodos";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_tipos_periodo(){
        String nombre_tabla = "tbl_tipos_periodo";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void limpiarTabla_tbl_centros(){
        String nombre_tabla = "tbl_centros";
        int cont = LimpiarTablas.limpiarTabla(nombre_tabla);
        if(cont > 0){
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registros Eliminados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se eliminó " + cont + " registros de la tabla [" + nombre_tabla + "], la tabla ahora está vacía.");
                mensaje.show();
        }else{
            System.out.println("La tabla [" + nombre_tabla + "] está vacía.");
        }
    }
    
    @FXML
    private void cerrarFormulario(){
        // Obtener el escenario (Stage)
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        // Cerrar el formulario.
        stage.close();
    }
    
}
