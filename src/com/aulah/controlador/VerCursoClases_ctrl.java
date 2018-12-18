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

import com.aulah.modelo.Conexion;
import com.aulah.modelo.Curso;
import com.aulah.modelo.VerCursoClases;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Clase controlador de CursoClase.fxml
 *
 * @author hector
 */
public class VerCursoClases_ctrl implements Initializable {
    
    //Hace una nueva conexion a la a la BD
    Conexion conexion = new Conexion();
        
    //Elementos GUI
    @FXML private ComboBox<Curso> cmbCurso;
    @FXML private TableView<VerCursoClases> tblClase;
    @FXML private TableColumn<VerCursoClases, String> clmClase;
    @FXML private TableColumn<VerCursoClases, String> clmNivel;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //Listas para el ComboBox cmbCurso
    private ObservableList<Curso> listaCursos = FXCollections.observableArrayList();
    //Lista para la tabla tlbClase
    private ObservableList<VerCursoClases> listaClases = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblClase.setEditable(true);
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Llenar el ObservableList
        Curso.llenarCursoNivel(conn, listaCursos);
        //Agregar los items del ObservableList al ComboBox
        cmbCurso.setItems(listaCursos);
        
        //Inhabilitar elementos
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }  
    
    @FXML
    private void llenarClasesCurso(){
        listaClases.clear();
        tblClase.getItems().clear();
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Obtiene el codigo del curso
        int codigo_curso = cmbCurso.getSelectionModel().getSelectedItem().getCodigo();
        //Llenar el ObservableList
        VerCursoClases.llenarInformacionClaseCurso(conn, listaClases, codigo_curso);
        if(listaClases.size() > 0){
            //Agrega la lista a la tabla
            tblClase.setItems(listaClases);
            //Llenar la columna de la tabla
            clmClase.setCellValueFactory(new PropertyValueFactory<VerCursoClases, String>("clase"));
            clmNivel.setCellValueFactory(new PropertyValueFactory<VerCursoClases, String>("nivel"));
            //Cerrar la conexion a la BD
        }else{
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("No hay Clases");
            mensaje.setContentText("");
            mensaje.setHeaderText("El Curso " + cmbCurso.getValue() + " no tiene Clases asignadas.");
            mensaje.show();
        }
        conexion.cerrarConexion();
    }
    
    private void inhabilitarLimpiarElementos(){
        btnGuardar.setDisable(false);
        btnCancelar.setDisable(true);
        btnSalir.setDisable(false);
    }
       
    @FXML
    private void cerrarFormulario(){
        // Obtener el escenario (Stage)
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        // Cerrar el formulario.
        stage.close();
    }
   
}
