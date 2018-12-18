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
import com.aulah.modelo.Nivel;
import com.aulah.modelo.Periodo;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Clase controlador para Nivel_ctrl.fxml
 *
 * @author hector
 */
public class Nivel_ctrl implements Initializable {
    
    int codigo = 0;//codigo: Variable para guardar el Codigo del de la Jornada para usarlo cuando
    //se Actualiza o Elimina un registro ya que el Codigo no se muestra en ningun elemento del GUI.
    int codigo_generado = 0;//Se usa para saber el codigo generado cuando se guarda un nuevo registro,
    //ya que el codigo es autoincrementado en la BD.
    boolean registro_recien_guardado = false;//Se usa para saber si se ha guardado un registro antes de mandar
    //a modificar el mismo registro.
    boolean nuevo_registro = false;//Se usa para saber si se va a ingresar datos en el formulario
    //para un nuevo registro que luego se piensa guardar.
    
    //Hace una nueva conexion a la a la BD
    Conexion conexion = new Conexion();
    
    //Componentes de la GUI
    @FXML private ComboBox<Periodo> cmbPeriodo;
    @FXML private ComboBox<String> cmbNivel;
    @FXML private TableView<Nivel> tblNivel;
    @FXML private TableColumn<Nivel, String> clmNivel;
    @FXML private TableColumn<Periodo, String> clmPeriodo;
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //Lista para el combobox con valores predefinidos
    ObservableList<Periodo> listaPeriodos = FXCollections.observableArrayList();
    //Lista para el ComboBox de Niveles con valores predefinidos
    ObservableList<String> tiposNivelesPropuestos = FXCollections.observableArrayList(
        "Primer Ciclo",
        "Segundo Ciclo",
        "Tercer Ciclo",
        "Educación Media"
    );
    //Lista para la tabla
    private ObservableList<Nivel> listaNiveles = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ///Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Llenar ObservableList listaPeriodos
        Periodo.llenarNombrePeriodos(conn, listaPeriodos);
        //Llenar el ComboBox cmbPeriodo
        cmbPeriodo.setItems(listaPeriodos);
        //Llenar ComboBox cmbNiveles
        cmbNivel.setItems(tiposNivelesPropuestos);
        //Llenar la lista
        Nivel.llenarInformacionNivel(conn, listaNiveles);
        //Agrega la lista a la tabla
        tblNivel.setItems(listaNiveles);
        //Llenar la columna de la tabla
        clmNivel.setCellValueFactory(new PropertyValueFactory<Nivel, String>("nivel"));
        clmPeriodo.setCellValueFactory(new PropertyValueFactory<Periodo, String>("cod_periodo"));
        //Gestiona los eventos del formulario
        gestionarEventos();
        //Inhabilitar elementos
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }  
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblNivel.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Nivel>(){
                @Override
                public void changed(ObservableValue<? extends Nivel> ov, 
                    Nivel valorAnterior, Nivel valorSeleccionado) {
                    if(valorSeleccionado != null){
                        codigo = valorSeleccionado.getCodigo();
                        cmbNivel.setValue(valorSeleccionado.getNivel());
                        cmbNivel.setDisable(false);
                        cmbPeriodo.setValue(valorSeleccionado.getCod_periodo());
                        cmbPeriodo.setDisable(false);
                        btnGuardar.setDisable(true);
                        btnModificar.setDisable(false);
                        btnEliminar.setDisable(false);
                    }
                }  
            }
        );
        //Ocurre cada vez que se escribe en el ComboBox
        cmbNivel.getEditor().textProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(nuevo_registro){
                        cmbPeriodo.setDisable(false);
                    }                   
                }
        });
    }
    
    @FXML
    private void habilitarGuardar(){
        btnGuardar.setDisable(false);
    }
    
    @FXML
    public void guardarRegistro(){
        if(cmbNivel.getValue() != null){
            Nivel nv = new Nivel(0, cmbNivel.getValue(), cmbPeriodo.getValue());
            conexion.establecerConexion();
            int resultado = nv.guardarRegistro(conexion.getConnection());
            codigo_generado = Nivel.codigo_generado;
            conexion.cerrarConexion();
            if(resultado == 1){
                listaNiveles.add(nv);
                registro_recien_guardado = true;
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Nivel " + cmbNivel.getValue() + " ha sido agregada exitosamente");
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
        Nivel jd = new Nivel(codigo, cmbNivel.getValue(),cmbPeriodo.getValue());
        conexion.establecerConexion();
        int resultado = jd.modificarRegistro(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            registro_recien_guardado = false;
            listaNiveles.set(tblNivel.getSelectionModel().getSelectedIndex(), jd);
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro modificado");
            mensaje.setContentText("");
            mensaje.setHeaderText("El Nivel " + cmbNivel.getValue() + " ha sido modificado exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    public void eliminarRegistro(){
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar registro");
        confirmar.setHeaderText("¿Está seguro que desea eliminar el Nivel " + cmbNivel.getValue() + "?");
        confirmar.setContentText("");
        Optional<ButtonType> result = confirmar.showAndWait();
            if(result.get() == ButtonType.OK){
                if(registro_recien_guardado){
                codigo = codigo_generado;
            }
            Nivel tp = new Nivel(codigo, cmbNivel.getValue(),cmbPeriodo.getValue());
            conexion.establecerConexion();
            int resultado = tp.eliminarRegistro(conexion.getConnection());
            conexion.cerrarConexion();
            if(resultado == 1){
                registro_recien_guardado = false;
                listaNiveles.remove(tblNivel.getSelectionModel().getSelectedIndex());
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Nivel ha sido eliminada exitosamente");
                mensaje.show();
                inhabilitarLimpiarElementos();
            }
        }
    }
       
    @FXML
    public void nuevoRegistro(){
        nuevo_registro = true;
        cmbNivel.setValue(null);
        cmbNivel.setDisable(false);
        btnGuardar.setDisable(true);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        cmbNivel.requestFocus();
    }
    
    private void inhabilitarLimpiarElementos(){
        cmbNivel.setValue(null);
        cmbNivel.setDisable(true);
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
