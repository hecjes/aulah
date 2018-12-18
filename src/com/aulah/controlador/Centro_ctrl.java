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

import com.aulah.modelo.Centro;
import com.aulah.modelo.Conexion;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hector
 */
public class Centro_ctrl implements Initializable {
    
    int codigo = 0;//codigo: Variable para guardar el Codigo del del TipoPeriodo para usarlo cuando
    //se Actualiza o Elimina un registro ya que el Codigo no se muestra en ningun elemento del GUI.
    int codigo_generado = 0;//Se usa para saber el codigo generado cuando se guarda un nuevo registro.
    //ya que el codigo es autoincrementado en la BD.
    boolean registro_recien_guardado = false;//Se usa para saber si se ha guardado un registro antaes de mandar
    //a modificar el mismo registro.
    boolean nuevo_registro = false;//Se usa para saber si se va a ingresar datos en el formulario
    //para un nuevo registro que luego se piensa guardar.
    
    Conexion conexion;//conexion: para conectar a la BD
   
    //Elementos del GUI
    @FXML TextField txtCentro;
    @FXML TextField txtClave;
    @FXML TextField txtDireccion;
    @FXML Button btnGuardar;
    @FXML Button btnModificar;
    @FXML Button btnEliminar;
    @FXML Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    @FXML TableView<Centro> tblCentros;
    //Columnas de la tabla
    @FXML private TableColumn<Centro, String> clmCentro;
    @FXML private TableColumn<Centro, String> clmClave;
    @FXML private TableColumn<Centro, String> clmDireccion;
    
    //Crear la colexion
    private ObservableList<Centro> listaCentros = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conecta a la BD
        conexion = new Conexion();
        //Llenar lista
        Centro.llenarInformacionCentros(conexion.establecerConexion(), listaCentros);
        //Enlazar lista con TableView
        tblCentros.setItems(listaCentros);
        //Enlazar columnas con atributos
        clmCentro.setCellValueFactory(new PropertyValueFactory<Centro, String>("centro"));
        clmClave.setCellValueFactory(new PropertyValueFactory<Centro, String>("clave"));
        clmDireccion.setCellValueFactory(new PropertyValueFactory<Centro, String>("direccion"));
        //Gestiona los eventos en los controles del formulario
        gestionarEventos();
        //Inhabilitar los controles
        inhabilitarLimpiarElementos();
        //Cierra la conexion a la BD
        conexion.cerrarConexion();
    }
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblCentros.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Centro>(){
                @Override
                public void changed(ObservableValue<? extends Centro> ov, 
                    Centro valorAnterior, Centro valorSeleccionado) {
                    if(valorSeleccionado != null){
                        //Obtiene el Codigo del registro
                        codigo = valorSeleccionado.getCodigo();
                        //Establece los elementos del formulario
                        txtCentro.setText(valorSeleccionado.getCentro());
                        txtCentro.setDisable(false);
                        txtClave.setText(valorSeleccionado.getClave());
                        txtClave.setDisable(false);
                        txtDireccion.setText(valorSeleccionado.getDireccion());
                        txtDireccion.setDisable(false);
                        //Establece los botones
                        btnGuardar.setDisable(true);
                        btnModificar.setDisable(false);
                        btnEliminar.setDisable(false);
                    }
                }  
            }
        );
        //Ocurre cada vez que se escribe en el txtCentro
        txtCentro.textProperty().addListener(
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
        if(txtCentro.getText() != null){
            Centro ct = new Centro(0, txtCentro.getText(), txtClave.getText(), txtDireccion.getText());
            conexion.establecerConexion();
            int resultado = ct.guardarRegistro(conexion.getConnection());
            codigo_generado = Centro.codigo_generado;
            conexion.cerrarConexion();
            if(resultado == 1){
                listaCentros.add(ct);
                registro_recien_guardado = true;
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Centro " + txtCentro.getText() + " ha sido agregado exitosamente");
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
    private void modificarRegistro(ActionEvent event){
        if(registro_recien_guardado){
            codigo = codigo_generado;
        }
        Centro ct = new Centro(codigo, txtCentro.getText(), txtClave.getText(), txtDireccion.getText());
        conexion.establecerConexion();
        int resultado = ct.modificarRegistro(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            registro_recien_guardado = false;
            listaCentros.set(tblCentros.getSelectionModel().getSelectedIndex(), ct);
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro modificado");
            mensaje.setContentText("");
            mensaje.setHeaderText("El Centro " + txtCentro.getText() + " ha sido modificado exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    private void eliminarRegistro(ActionEvent event){
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar registro");
        confirmar.setHeaderText("¿Está seguro que desea eliminar el Centro " + txtCentro.getText() + "?");
        confirmar.setContentText("");
        Optional<ButtonType> result = confirmar.showAndWait();
        if(result.get() == ButtonType.OK){
            if(registro_recien_guardado){
                codigo = codigo_generado;
            }
            Centro ct = new Centro(codigo, txtCentro.getText(), txtClave.getText(), txtDireccion.getText());
            conexion.establecerConexion();
            int resultado = ct.eliminarRegistro(conexion.getConnection());
            conexion.cerrarConexion();
            if(resultado == 1){
                registro_recien_guardado = false;
                listaCentros.remove(tblCentros.getSelectionModel().getSelectedIndex());
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Centro ha sido eliminado exitosamente");
                mensaje.show();
                inhabilitarLimpiarElementos();
            }
        }
    }
    
    @FXML
    public void nuevoRegistro(){
        nuevo_registro = true;
        txtCentro.setText(null);
        txtCentro.setDisable(false);
        txtClave.setText(null);
        txtClave.setDisable(false);
        txtDireccion.setText(null);
        txtDireccion.setDisable(false);
        btnGuardar.setDisable(true);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        txtCentro.requestFocus();
    } 
    
    private void inhabilitarLimpiarElementos(){
        txtCentro.setText(null);
        txtCentro.setDisable(true);
        txtClave.setText(null);
        txtClave.setDisable(true);
        txtDireccion.setText(null);
        txtDireccion.setDisable(true);
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
