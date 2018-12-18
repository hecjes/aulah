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

import com.aulah.modelo.Semestre;
import com.aulah.modelo.Conexion;
import com.aulah.modelo.Periodo;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Semestre_ctrl implements Initializable {
    
    int codigo = 0;//codigo: Variable para guardar el Codigo del del TipoPeriodo para usarlo cuando
    //se Actualiza o Elimina un registro ya que el Codigo no se muestra en ningun elemento del GUI.
    int codigo_generado = 0;//Se usa para saber el codigo generado cuando se guarda un nuevo registro.
    //ya que el codigo es autoincrementado en la BD.
    boolean registro_recien_guardado = false;//Se usa para saber si se ha guardado un registro antaes de mandar
    //a modificar el mismo registro.
    boolean nuevo_registro = false;//Se usa para saber si se va a ingresar datos en el formulario
    //para un nuevo registro que luego se piensa guardar.
    
    Conexion conexion = new Conexion();
    
    //Elementos GUI
    @FXML private ComboBox<String> cmbSemestre;
    @FXML private DatePicker dtpFechaInicio;
    @FXML private DatePicker dtpFechaFinal;
    @FXML private ComboBox<Periodo> cmbPeriodo;
    @FXML private TableView<Semestre> tblSemestres;
    //Columnas del TableView
    @FXML private TableColumn<Semestre, String> clmSemestre;
    @FXML private TableColumn<Semestre, Date> clmFecha_inicio;
    @FXML private TableColumn<Semestre, Date> clmFecha_final;
    @FXML private TableColumn<Semestre, String> clmPeriodo;
    //Botones del formulario
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //Lista para el combobox con valores predefinidos
    ObservableList<String> nombresPeriodoPropuesto = FXCollections.observableArrayList(
        "I Semestre",
        "II Semestre",
        "Primer Semestre",
        "Segundo Semestre"
    );
    //Definir un ObservableList para llenarlo con objetos Semestre.
    private ObservableList<Semestre> listaSemestres = FXCollections.observableArrayList();
    //Definir un ObservableList para llenarlo con objetos Periodo.
    private ObservableList<Periodo> listaPeriodos = FXCollections.observableArrayList();
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Muestra de la lista el nombre de los Semestres propuestos
        cmbSemestre.setItems(nombresPeriodoPropuesto);
        //obtiene los Periodos en listaPeriodo
        Periodo.llenarNombrePeriodos(conn, listaPeriodos);
        //Llenar lista
        Semestre.llenarInformacionSemestres(conn, listaSemestres);
        //Enlazar lista con el ComboBox
        cmbPeriodo.setItems(listaPeriodos);
        //Enlazar lista con TableView
        tblSemestres.setItems(listaSemestres);
        //Enlazar columnas con atributos
        clmSemestre.setCellValueFactory(new PropertyValueFactory<Semestre, String>("semestre"));
        clmFecha_inicio.setCellValueFactory(new PropertyValueFactory<Semestre, Date>("fecha_inicio"));
        clmFecha_final.setCellValueFactory(new PropertyValueFactory<Semestre, Date>("fecha_final"));
        clmPeriodo.setCellValueFactory(new PropertyValueFactory<Semestre, String>("cod_periodo"));
    
        //Gestiona los eventos en los controles del formulario
        gestionarEventos();
        //Inhabilita y limpia los controles
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    } 
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblSemestres.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Semestre>(){
                @Override
                public void changed(ObservableValue<? extends Semestre> ov, 
                    Semestre valorAnterior, Semestre valorSeleccionado) {
                    if(valorSeleccionado != null){
                        //Obtiene el Codigo del registro
                        codigo = valorSeleccionado.getCodigo();
                        //Establece los elementos del formulario
                        cmbSemestre.setValue(valorSeleccionado.getSemestre());
                        dtpFechaInicio.setValue(valorSeleccionado.getFecha_inicio().toLocalDate());
                        dtpFechaFinal.setValue(valorSeleccionado.getFecha_final().toLocalDate());
                        cmbPeriodo.setValue(valorSeleccionado.getCod_periodo());
                        //Habilita los elementos necesarios
                        cmbSemestre.setDisable(false);
                        dtpFechaInicio.setDisable(false);
                        dtpFechaFinal.setDisable(false);
                        cmbPeriodo.setDisable(false);
                        //Establece los botones
                        btnGuardar.setDisable(true);
                        btnModificar.setDisable(false);
                        btnEliminar.setDisable(false);
                    }
                }  
            }
        );
        //Ocurre cada vez que se escribe en el txtCentro
        cmbSemestre.getEditor().textProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String valorAnterior, String valorNuevo) {
                    if(nuevo_registro){
                        btnGuardar.setDisable(false);
                    }                   
                }
        });
    }
    
    @FXML
    private void guardarRegistro(ActionEvent event){
        if(cmbSemestre.getValue() != null){
            Semestre sm = new Semestre(0, cmbSemestre.getValue(), Date.valueOf(dtpFechaInicio.getValue()), Date.valueOf(dtpFechaFinal.getValue()), cmbPeriodo.getValue());
            conexion.establecerConexion();
            int resultado = sm.guardarRegistro(conexion.getConnection());
            codigo_generado = Periodo.codigo_generado;
            conexion.cerrarConexion();
            if(resultado == 1){
                listaSemestres.add(sm);
                registro_recien_guardado = true;
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Semestre " + cmbSemestre.getValue() + " ha sido agregado exitosamente");
                mensaje.show();
                inhabilitarLimpiarElementos();
                nuevo_registro = false;
            }
        }else{
            registro_recien_guardado = false;
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro vacío");
            mensaje.setContentText("");
            mensaje.setHeaderText("Ingrese la información requerida");
            mensaje.show();
        }
    }
    
    @FXML
    private void  modificarRegistro(ActionEvent event){
        if(registro_recien_guardado){
            codigo = codigo_generado;
        }
        Semestre sm = new Semestre(codigo, cmbSemestre.getValue(), Date.valueOf(dtpFechaInicio.getValue()), Date.valueOf(dtpFechaFinal.getValue()), cmbPeriodo.getValue());
        conexion.establecerConexion();
        int resultado = sm.modificarRegistro(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            registro_recien_guardado = false;
            listaSemestres.set(tblSemestres.getSelectionModel().getSelectedIndex(), sm);
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro modificado");
            mensaje.setContentText("");
            mensaje.setHeaderText("El Semestre " + cmbSemestre.getValue() + " ha sido modificado exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    private void eliminarRegistro(ActionEvent event){
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar registro");
        confirmar.setHeaderText("¿Está seguro que desea eliminar el Semestre " + cmbSemestre.getValue() + " ?");
        confirmar.setContentText("");
        Optional<ButtonType> result = confirmar.showAndWait();
        if(result.get() == ButtonType.OK){
            if(registro_recien_guardado){
                codigo = codigo_generado;
            }
            Semestre sm = new Semestre(codigo, cmbSemestre.getValue(), Date.valueOf(dtpFechaInicio.getValue()), Date.valueOf(dtpFechaFinal.getValue()), cmbPeriodo.getValue());
            conexion.establecerConexion();
            int resultado = sm.eliminarRegistro(conexion.getConnection());
            conexion.cerrarConexion();
            if(resultado == 1){
                registro_recien_guardado = false;
                listaSemestres.remove(tblSemestres.getSelectionModel().getSelectedIndex());
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Semestre ha sido eliminado exitosamente");
                mensaje.show();
                inhabilitarLimpiarElementos();
            }
        }
    }
    
    @FXML
    public void nuevoRegistro(){
        nuevo_registro = true;
        cmbSemestre.setValue(null);
        cmbSemestre.setDisable(false);
        dtpFechaInicio.setValue(null);
        dtpFechaInicio.setDisable(false);
        dtpFechaFinal.setValue(null);
        dtpFechaFinal.setDisable(false);
        cmbPeriodo.setValue(null);
        cmbPeriodo.setDisable(false);
        btnGuardar.setDisable(true);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        //Establece el foco
        cmbSemestre.requestFocus();
    }   
    
    private void inhabilitarLimpiarElementos(){
        cmbSemestre.setValue(null);
        cmbSemestre.setDisable(true);
        dtpFechaInicio.setValue(null);
        dtpFechaInicio.setDisable(true);
        dtpFechaFinal.setValue(null);
        dtpFechaFinal.setDisable(true);
        cmbPeriodo.setValue(null);
        cmbPeriodo.setDisable(true);
        btnGuardar.setDisable(true);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }
    
    @FXML
    private void cerrarFormulario(){
        // Obtener el escenario (Stage)
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        // Cerrar el formulario.
        stage.close();
    }
    
}
