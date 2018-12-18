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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import com.aulah.modelo.TipoPeriodo;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TipoPeriodo_ctrl implements Initializable {
    
    int codigo = 0;//codigo: Variable para guardar el Codigo del del TipoPeriodo para usarlo cuando
    //se Actualiza o Elimina un registro ya que el Codigo no se muestra en ningun elemento del GUI.
    int codigo_generado = 0;//Se usa para saber el codigo generado cuando se guarda un nuevo registro.
    //ya que el codigo es autoincrementado en la BD.
    boolean registro_recien_guardado = false;//Se usa para saber si se ha guardado un registro antaes de mandar
    //a modificar el mismo registro.
    boolean nuevo_registro = false;//Se usa para saber si se va a ingresar datos en el formulario
    //para un nuevo registro que luego se piensa guardar.
    
    Conexion conexion;//conexion: para conectar a la BD
    
    //Componentes de la GUI
    @FXML private ComboBox<String> cmbTipoPeriodo;
    @FXML private TableView<TipoPeriodo> tblViewTipoPeriodo;
    @FXML private TableColumn<TipoPeriodo, String> clmTipoPeriodo;
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //Lista para el combobox con valores predefinidos
    ObservableList<String> tiposPeriodoPropuestos = FXCollections.observableArrayList(
        "ANUAL",
        "SEMESTRAL",
        "TRIMESTRAL"
    );
    //Lista para la tabla
    private ObservableList<TipoPeriodo> listaTiposPeriodo = FXCollections.observableArrayList();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Hace una nueva conexion a la a la BD
        conexion = new Conexion();
        //Llenar ComboBox
        cmbTipoPeriodo.setItems(tiposPeriodoPropuestos);
        
        //Llenar la lista
        TipoPeriodo.llenarInformacionTipoPeriodo(conexion.establecerConexion(), listaTiposPeriodo);
        //Agrega la lista a la tabla
        tblViewTipoPeriodo.setItems(listaTiposPeriodo);
        //Llenar la columna de la tabla
        clmTipoPeriodo.setCellValueFactory(new PropertyValueFactory<TipoPeriodo, String>("tipo_periodo"));
        //Gestiona los eventos del formulario
        gestionarEventos();
        //Inhabilitar elementos
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }  
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblViewTipoPeriodo.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<TipoPeriodo>(){
                @Override
                public void changed(ObservableValue<? extends TipoPeriodo> ov, 
                    TipoPeriodo valorAnterior, TipoPeriodo valorSeleccionado) {
                    if(valorSeleccionado != null){
                        codigo = valorSeleccionado.getCodigo();
                        cmbTipoPeriodo.setValue(valorSeleccionado.getTipo_periodo());
                        cmbTipoPeriodo.setDisable(false);
                        btnGuardar.setDisable(true);
                        btnModificar.setDisable(false);
                        btnEliminar.setDisable(false);
                    }
                }  
            }
        );
        //Ocurre cada vez que se escribe en el ComboBox
        /*cmbTipoPeriodo.getEditor().textProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(nuevo_registro){
                        btnGuardar.setDisable(false);
                    }                   
                }
        });*/
    }
    
    @FXML
    public void habilitarGuardar(){
        btnGuardar.setDisable(false);
    }
    
    @FXML
    public void guardarRegistro(){
        if(cmbTipoPeriodo.getValue() != null){
            TipoPeriodo tp = new TipoPeriodo(0, cmbTipoPeriodo.getValue());
            conexion.establecerConexion();
            int resultado = tp.guardarRegistro(conexion.getConnection());
            codigo_generado = TipoPeriodo.codigo_generado;
            conexion.cerrarConexion();
            if(resultado == 1){
                listaTiposPeriodo.add(tp);
                registro_recien_guardado = true;
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Tipo de Periodo " + cmbTipoPeriodo.getValue() + " ha sido agregado exitosamente");
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
    public void modificarRegistro(){
        if(registro_recien_guardado){
            codigo = codigo_generado;
        }
        TipoPeriodo tp = new TipoPeriodo(codigo, cmbTipoPeriodo.getSelectionModel().getSelectedItem().toString());
        conexion.establecerConexion();
        int resultado = tp.modificarRegistro(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            registro_recien_guardado = false;
            listaTiposPeriodo.set(tblViewTipoPeriodo.getSelectionModel().getSelectedIndex(), tp);
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro modificado");
            mensaje.setContentText("");
            mensaje.setHeaderText("El Tipo de Periodo " + cmbTipoPeriodo.getValue() + " ha sido modificado exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    public void eliminarRegistro(){
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar registro");
        confirmar.setHeaderText("¿Está seguro que desea eliminar el Tipo de Periodo " + cmbTipoPeriodo.getValue() + "?");
        confirmar.setContentText("");
        Optional<ButtonType> result = confirmar.showAndWait();
            if(result.get() == ButtonType.OK){
                if(registro_recien_guardado){
                codigo = codigo_generado;
            }
            TipoPeriodo tp = new TipoPeriodo(codigo, cmbTipoPeriodo.getSelectionModel().getSelectedItem().toString());
            conexion.establecerConexion();
            int resultado = tp.eliminarRegistro(conexion.getConnection());
            //int resultado = tblViewTipoPeriodo.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.getConnection());
            conexion.cerrarConexion();
            if(resultado == 1){
                registro_recien_guardado = false;
                listaTiposPeriodo.remove(tblViewTipoPeriodo.getSelectionModel().getSelectedIndex());
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Tipo de Periodo ha sido eliminado exitosamente");
                mensaje.show();
                inhabilitarLimpiarElementos();
            }
        }
    }
       
    @FXML
    public void nuevoRegistro(){
        nuevo_registro = true;
        cmbTipoPeriodo.setValue(null);
        cmbTipoPeriodo.setDisable(false);
        btnGuardar.setDisable(true);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        cmbTipoPeriodo.requestFocus();
    }
    
    private void inhabilitarLimpiarElementos(){
        cmbTipoPeriodo.setValue(null);
        cmbTipoPeriodo.setDisable(true);
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
