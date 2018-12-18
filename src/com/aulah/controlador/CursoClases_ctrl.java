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
import com.aulah.modelo.CursoClases;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Clase controlador de CursoClase.fxml
 *
 * @author hector
 */
public class CursoClases_ctrl implements Initializable {
    
    //Variables para saber que acción se ha elegido.
    String ASIGNAR = "Asignar clase";
    String QUITAR = "Quitar clase";
    
    //Hace una nueva conexion a la a la BD
    Conexion conexion = new Conexion();
        
    //Elementos GUI
    @FXML private ComboBox<String> cmbAccion;
    @FXML private ComboBox<Curso> cmbCurso;
    @FXML private TableView<CursoClases> tblClase;
    @FXML private TableColumn<CursoClases, String> clmClase;
    @FXML private TableColumn<CursoClases, String> clmNivel;
    @FXML private TableColumn<CursoClases, Boolean> clmSeleccionar;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //Lista para el ComboBox cmbAccion
    private ObservableList<String> listaAccion = FXCollections.observableArrayList(ASIGNAR, QUITAR);
    //Listas para el ComboBox cmbCurso
    private ObservableList<Curso> listaCursos = FXCollections.observableArrayList();
    //Lista para la tabla tlbClase
    private ObservableList<CursoClases> listaClases = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbAccion.setItems(listaAccion);
        tblClase.setEditable(true);
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Llenar el ObservableList
        Curso.llenarCursoNivel(conn, listaCursos);
        //Agregar los items del ObservableList al ComboBox
        cmbCurso.setItems(listaCursos);
        cmbAccion.setDisable(true);
        clmSeleccionar.setCellValueFactory(cell -> cell.getValue().selecionadoProperty());
        clmSeleccionar.setCellFactory(CheckBoxTableCell.forTableColumn(clmSeleccionar));
        clmSeleccionar.setOnEditCommit(data -> {
            CursoClases p = data.getRowValue();
            p.setSelecionado(data.getNewValue());
            System.out.println(data.getRowValue());
        });
        //Gestiona los eventos del formulario
        gestionarEventos();
        //Inhabilitar elementos
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }  
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item del TableView
        tblClase.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CursoClases>(){
                @Override
                public void changed(ObservableValue<? extends CursoClases> ov, 
                    CursoClases valorAnterior, CursoClases valorSeleccionado) {
                    
                    if(valorSeleccionado != null){
                        //if(valorSeleccionado.getSelecionado()){
                            //valorSeleccionado.setSelecionado(Boolean.TRUE);
                            //System.out.println("Seleccionado es: " + valorSeleccionado.getSelecionado());
                            btnGuardar.setDisable(false);
                        //}
                    }
                }  
            }
        );
    }
    
    @FXML
    private void llenarClasesCurso(){
        if(cmbAccion.getValue() != null){            
            listaClases.clear();
            tblClase.getItems().clear();
            int codigo_nivel = cmbCurso.getSelectionModel().getSelectedItem().get_cod_nivel().getValue();
            int codigo_curso = cmbCurso.getSelectionModel().getSelectedItem().getCodigo();
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            //Llenar el ObservableList
            if(cmbAccion.getSelectionModel().getSelectedItem() == ASIGNAR){
                CursoClases.llenarInformacionClasesNivel(conn, listaClases, codigo_nivel, codigo_curso);
                if(listaClases.size() == 0){
                    Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("No hay clases para el curso");
                    mensaje.setContentText("");
                    mensaje.setHeaderText("No hay Clases para asignar al Curso " + cmbCurso.getValue() + ".");
                    mensaje.show();
                    btnGuardar.setDisable(true);
                }
            }
            if(cmbAccion.getSelectionModel().getSelectedItem() == QUITAR){
                CursoClases.llenarInformacionClasesAsignadas(conn, listaClases, codigo_curso);
                if(listaClases.size() == 0){
                    Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("El Curso no tiene clases");
                    mensaje.setContentText("");
                    mensaje.setHeaderText("El Curso " + cmbCurso.getValue() + " aun no tiene Clases asignadas.");
                    mensaje.show();
                    btnGuardar.setDisable(true);
                }
            }
            //Agrega la lista a la tabla
            tblClase.setItems(listaClases);
            //Llenar la columna de la tabla
            clmClase.setCellValueFactory(new PropertyValueFactory<CursoClases, String>("clase"));
            clmNivel.setCellValueFactory(new PropertyValueFactory<CursoClases, String>("nivel"));
            //clmSeleccionar.setCellValueFactory(new PropertyValueFactory<CursoClases, Boolean>("seleccionado"));
            clmSeleccionar.setCellValueFactory(cell -> cell.getValue().selecionadoProperty());
            clmSeleccionar.setCellFactory(CheckBoxTableCell.forTableColumn(clmSeleccionar));
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
    
    private void inhabilitarLimpiarElementos(){
        btnGuardar.setDisable(true);
        btnCancelar.setDisable(true);
        btnSalir.setDisable(false);
    }
    
    @FXML
    private void asignaQuitarClasesCurso(){
        if(cmbAccion.getValue().equals(ASIGNAR)){
            asignarClasesCurso();
        }
        if(cmbAccion.getValue().equals(QUITAR)){
            quitarClasesCurso();
        }
    }
    
    private void asignarClasesCurso(){
        int cont = 0;
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        for (CursoClases lista : listaClases) {
            if(lista.getSelecionado()){
                if(CursoClases.guardarRegistro(conn, cmbCurso.getSelectionModel().getSelectedItem().getCodigo(), lista.getCodigo()) == 1){
                    cont++;
                }
            }
        }
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
        listaClases.clear();
        btnGuardar.setDisable(true);
        btnCancelar.setDisable(true);
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Clases asignadas");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se asignó " + cont + " Clases al Curso " + cmbCurso.getValue() + ".");
                mensaje.show();
    }
    
    private void quitarClasesCurso(){
        int cont = 0;
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        for (CursoClases lista : listaClases) {
            if(lista.getSelecionado()){
                if(CursoClases.eliminarRegistro(conn, cmbCurso.getSelectionModel().getSelectedItem().getCodigo(), lista.getCodigo()) == 1){
                    cont++;
                }
            }
        }
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
        listaClases.clear();
        btnGuardar.setDisable(true);
        btnCancelar.setDisable(true);
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Clases quitadas");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se quitó " + cont + " Clases al Curso " + cmbCurso.getValue() + ".");
                mensaje.show();
    }
    
    @FXML
    private void limpiarAccion(){
        listaClases.clear();
        tblClase.getItems().clear();
        cmbAccion.getSelectionModel().clearSelection();
        cmbAccion.setValue(null);
        cmbAccion.setDisable(false);
    }
    
    @FXML
    private void cerrarFormulario(){
        // Obtener el escenario (Stage)
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        // Cerrar el formulario.
        stage.close();
    }
    
}
