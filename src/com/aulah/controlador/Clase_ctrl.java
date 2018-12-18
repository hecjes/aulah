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

import com.aulah.modelo.Clase;
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
 * Clase cotrolador para Clase_ctrl.fxml
 *
 * @author hector
 */
public class Clase_ctrl implements Initializable {

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
    @FXML private ComboBox<String> cmbClase;
    @FXML private ComboBox<Periodo> cmbPeriodo;
    @FXML private ComboBox<Nivel> cmbNivel;
    @FXML private TableView<Clase> tblClase;
    @FXML private TableColumn<Clase, String> clmClase;
    @FXML private TableColumn<Clase, String> clmPeriodo;
    @FXML private TableColumn<Clase, String> clmNivel;
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //private ObservableList<Clase> listaClasesPropuestas = FXCollections.observableArrayList();
    //Listas para el ComboBox cmbPeriodo
    private ObservableList<Periodo> listaPeriodos = FXCollections.observableArrayList();
    //Listas para el ComboBox cmbNivel
    private ObservableList<Nivel> listaNiveles = FXCollections.observableArrayList();
    //Lista para la tabla tlbClase
    private ObservableList<Clase> listaClases = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
                
        //Llenar la listas
        //Clase.llenarInformacionNombreClases(conn, listaClasesPropuestas);
        Clase.llenarInformacionClase(conn, listaClases);
        Periodo.llenarInformacionPeriodo(conn, listaPeriodos);
        //Nivel.llenarInformacionNivel(conn, listaNiveles);
        
        //Agregar la listas a los ComboBox
        //cmbClase.setItems(listaClasesPropuestas);
        cmbPeriodo.setItems(listaPeriodos);
        cmbNivel.setItems(listaNiveles);
        //Agrega la lista a la tabla
        tblClase.setItems(listaClases);
        //Llenar la columna de la tabla
        clmClase.setCellValueFactory(new PropertyValueFactory<Clase, String>("clase"));
        clmPeriodo.setCellValueFactory(new PropertyValueFactory<Clase, String>("cod_periodo"));
        clmNivel.setCellValueFactory(new PropertyValueFactory<Clase, String>("cod_nivel"));
        //Gestiona los eventos del formulario
        gestionarEventos();
        //Inhabilitar elementos
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }  
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblClase.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Clase>(){
                @Override
                public void changed(ObservableValue<? extends Clase> ov, 
                    Clase valorAnterior, Clase valorSeleccionado) {
                    if(valorSeleccionado != null){
                        codigo = valorSeleccionado.getCodigo();
                        cmbClase.setValue(valorSeleccionado.getClase());
                        cmbClase.setDisable(false);
                        cmbPeriodo.setValue(valorSeleccionado.getCod_periodo());
                        cmbPeriodo.setDisable(false);
                        cmbNivel.setValue(valorSeleccionado.getCod_nivel());
                        cmbNivel.setDisable(false);
                        btnGuardar.setDisable(true);
                        btnModificar.setDisable(false);
                        btnEliminar.setDisable(false);
                    }
                }  
            }
        );
        //Ocurre cada vez que se escribe en el ComboBox
        cmbClase.getEditor().textProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(nuevo_registro){
                        btnGuardar.setDisable(false);
                    }                   
                }
        });
    }
    
    @FXML public void mostrarNivelesPeriodo(){
        if(cmbPeriodo.getValue() != null){
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            //Limpia el ComboBox
            cmbNivel.getItems().clear();
            //Llena la lista desde la BD
            Nivel.llenarNivelesPeriodo(conn, listaNiveles, codigo_periodo);
            //Muestra el contenido de la lista en el ComboBox
            cmbNivel.setItems(listaNiveles);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
    
    @FXML
    public void guardarRegistro(){
        if(cmbClase.getValue() != null){
            Clase tp = new Clase(0, cmbClase.getValue().toString(), cmbPeriodo.getValue(), cmbNivel.getValue());
            conexion.establecerConexion();
            int resultado = tp.guardarRegistro(conexion.getConnection());
            codigo_generado = Clase.codigo_generado;
            conexion.cerrarConexion();
            if(resultado == 1){
                listaClases.add(tp);
                registro_recien_guardado = true;
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("");
                mensaje.setHeaderText("La Clase " + cmbClase.getValue() + " ha sido agregada exitosamente");
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
        Clase jd = new Clase(codigo, cmbClase.getValue().toString(), cmbPeriodo.getValue(), cmbNivel.getValue());
        conexion.establecerConexion();
        int resultado = jd.modificarRegistro(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            registro_recien_guardado = false;
            listaClases.set(tblClase.getSelectionModel().getSelectedIndex(), jd);
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro modificado");
            mensaje.setContentText("");
            mensaje.setHeaderText("La Clase" + cmbClase.getValue() + " ha sido modificado exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    public void eliminarRegistro(){
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar registro");
        confirmar.setHeaderText("¿Está seguro que desea eliminar la Clase " + cmbClase.getValue() + "?");
        confirmar.setContentText("");
        Optional<ButtonType> result = confirmar.showAndWait();
            if(result.get() == ButtonType.OK){
                if(registro_recien_guardado){
                codigo = codigo_generado;
            }
            Clase tp = new Clase(codigo, cmbClase.getValue().toString(), cmbPeriodo.getValue(), cmbNivel.getValue());
            conexion.establecerConexion();
            int resultado = tp.eliminarRegistro(conexion.getConnection());
            conexion.cerrarConexion();
            if(resultado == 1){
                registro_recien_guardado = false;
                listaClases.remove(tblClase.getSelectionModel().getSelectedIndex());
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("La Clase ha sido eliminada exitosamente");
                mensaje.show();
                inhabilitarLimpiarElementos();
            }
        }
    }
       
    @FXML
    public void nuevoRegistro(){
        nuevo_registro = true;
        cmbClase.setValue(null);
        cmbClase.setDisable(false);
        cmbPeriodo.setValue(null);
        cmbPeriodo.setDisable(false);
        cmbNivel.setValue(null);
        cmbNivel.setDisable(false);
        btnGuardar.setDisable(true);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        cmbClase.requestFocus();
    }
    
    private void inhabilitarLimpiarElementos(){
        cmbClase.setValue(null);
        cmbClase.setDisable(true);
        cmbPeriodo.setValue(null);
        cmbPeriodo.setDisable(true);
        cmbNivel.setValue(null);
        cmbNivel.setDisable(true);
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
