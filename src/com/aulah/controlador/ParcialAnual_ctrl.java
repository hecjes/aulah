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
import com.aulah.modelo.ParcialAnual;
import com.aulah.modelo.Periodo;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.time.format.FormatStyle;
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
import javafx.util.converter.LocalDateStringConverter;

/**
 * Clase controlador de ParcialAnual.FXML
 *
 * @author Hector Armando Herrera
 */
public class ParcialAnual_ctrl implements Initializable {

   
    int codigo = 0;//codigo: Variable para guardar el Codigo del del TipoPeriodo para usarlo cuando
    //se Actualiza o Elimina un registro ya que el Codigo no se muestra en ningun elemento del GUI.
    int codigo_generado = 0;//Se usa para saber el codigo generado cuando se guarda un nuevo registro.
    //ya que el codigo es autoincrementado en la BD.
    boolean registro_recien_guardado = false;//Se usa para saber si se ha guardado un registro antaes de mandar
    //a modificar el mismo registro.
    boolean nuevo_registro = false;//Se usa para saber si se va a ingresar datos en el formulario
    //para un nuevo registro que luego se piensa guardar.
    
    //conexion: para conectar a la BD
    Conexion conexion;
               
    //Elementos GUI
    @FXML private ComboBox<String> cmbParcial;
    @FXML private DatePicker dtpFechaInicio;
    @FXML private DatePicker dtpFechaFinal;
    @FXML private ComboBox<Periodo> cmbPeriodo;
    //Tabla Periodos
    @FXML private TableView<ParcialAnual> tblParcial;
    //Columnas de la tabla
    @FXML private TableColumn<ParcialAnual,String> clmParcial;
    @FXML private TableColumn<ParcialAnual,Date> clmFechaInicio;
    @FXML private TableColumn<ParcialAnual,Date> clmFechaFinal;
    @FXML private TableColumn<ParcialAnual,String> clmPeriodo;
    //Botones del formulario
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //Lista para el combobox con valores predefinidos
    ObservableList<String> nombresParcialPropuesto = FXCollections.observableArrayList(
        "I - Parcial",
        "II - Parcial",
        "III - Parcial",
        "IV - Parcial",
        "1° Parcial",
        "2° Parcial",
        "3° Parcial",
        "4° Parcial",
        "Primer Parcial",
        "Segundo Parcial",
        "Tercer Parcial",
        "Cuarto Parcial"
        );
    //Definir los ObservableList para llenarlo con objetos ParcialAnual y Periodo
    private ObservableList<ParcialAnual> listaParciales = FXCollections.observableArrayList();
    private ObservableList<Periodo> listaPeriodos = FXCollections.observableArrayList();
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conecta a la BD
        conexion = new Conexion();
        Connection conn = conexion.establecerConexion();
        //Muestra los nombres de periodos propuestos
        cmbParcial.setItems(nombresParcialPropuesto);
        //Formatear las fechas en los DatePicker
        dtpFechaInicio.setConverter(new LocalDateStringConverter(FormatStyle.FULL));
        dtpFechaFinal.setConverter(new LocalDateStringConverter(FormatStyle.FULL));
        //obtiene los Parciales en listaParciales
        ParcialAnual.llenarInformacionParcialAnual(conn, listaParciales);
        //obtiene los Periodo en listaPeriodo
        Periodo.llenarPeriodosAnuales(conn, listaPeriodos);
        //Llena el ComboBox
        cmbPeriodo.setItems(listaPeriodos);
        //Agrega la lista a la tabla
        tblParcial.setItems(listaParciales);
        //Llenar la columna de la tabla
        clmParcial.setCellValueFactory(new PropertyValueFactory<ParcialAnual, String>("parcial"));
        clmFechaInicio.setCellValueFactory(new PropertyValueFactory<ParcialAnual,Date>("fecha_inicio"));
        clmFechaFinal.setCellValueFactory(new PropertyValueFactory<ParcialAnual,Date>("fecha_final"));
        clmPeriodo.setCellValueFactory(new PropertyValueFactory<ParcialAnual,String>("cod_periodo"));
        //Gestiona los eventos en los controles del formulario
        gestionarEventos();
        //Inhabilitar y limpiar los controles
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblParcial.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<ParcialAnual>(){
                @Override
                public void changed(ObservableValue<? extends ParcialAnual> ov, 
                    ParcialAnual valorAnterior, ParcialAnual valorSeleccionado) {
                    if(valorSeleccionado != null){
                        //Obtiene el Codigo del registro
                        codigo = valorSeleccionado.getCodigo();
                        //Establece los elementos del formulario
                        cmbParcial.setValue(valorSeleccionado.getParcial());
                        dtpFechaInicio.setValue(valorSeleccionado.getFecha_inicio().toLocalDate());
                        dtpFechaFinal.setValue(valorSeleccionado.getFecha_final().toLocalDate());
                        cmbPeriodo.setValue(valorSeleccionado.getCod_periodo());
                        //Habilita los elementos necesarios
                        cmbParcial.setDisable(false);
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
        cmbParcial.getEditor().textProperty().addListener(
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
        if(cmbParcial.getValue() != null){
            ParcialAnual pr = new ParcialAnual(0, cmbParcial.getValue(), Date.valueOf(dtpFechaInicio.getValue()), Date.valueOf(dtpFechaFinal.getValue()), cmbPeriodo.getValue());
            conexion.establecerConexion();
            int resultado = pr.guardarRegistro(conexion.getConnection());
            codigo_generado = ParcialAnual.codigo_generado;
            conexion.cerrarConexion();
            if(resultado == 1){
                listaParciales.add(pr);
                registro_recien_guardado = true;
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Parcial " + cmbParcial.getValue() + " ha sido agregado exitosamente");
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
        ParcialAnual pr = new ParcialAnual(codigo, cmbParcial.getValue(), Date.valueOf(dtpFechaInicio.getValue()), Date.valueOf(dtpFechaFinal.getValue()), cmbPeriodo.getValue());
        conexion.establecerConexion();
        int resultado = pr.modificarRegistro(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            registro_recien_guardado = false;
            listaParciales.set(tblParcial.getSelectionModel().getSelectedIndex(), pr);
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro modificado");
            mensaje.setContentText("");
            mensaje.setHeaderText("El Parcial " + cmbParcial.getValue() + " ha sido modificado exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    private void eliminarRegistro(ActionEvent event){
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar registro");
        confirmar.setHeaderText("¿Está seguro que desea eliminar el Parcial" + cmbParcial.getValue() + " ?");
        confirmar.setContentText("");
        Optional<ButtonType> result = confirmar.showAndWait();
        if(result.get() == ButtonType.OK){
            if(registro_recien_guardado){
                codigo = codigo_generado;
            }
            ParcialAnual pr = new ParcialAnual(codigo, cmbParcial.getValue(), Date.valueOf(dtpFechaInicio.getValue()), Date.valueOf(dtpFechaFinal.getValue()), cmbPeriodo.getValue());
            conexion.establecerConexion();
            int resultado = pr.eliminarRegistro(conexion.getConnection());
            conexion.cerrarConexion();
            if(resultado == 1){
                registro_recien_guardado = false;
                listaParciales.remove(tblParcial.getSelectionModel().getSelectedIndex());
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Parcial ha sido eliminado exitosamente");
                mensaje.show();
                inhabilitarLimpiarElementos();
            }
        }
    }
    
    @FXML
    public void nuevoRegistro(){
        nuevo_registro = true;
        cmbParcial.setValue(null);
        cmbParcial.setDisable(false);
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
        cmbParcial.requestFocus();
    }   
    
    private void inhabilitarLimpiarElementos(){
        cmbParcial.setValue(null);
        cmbParcial.setDisable(true);
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
