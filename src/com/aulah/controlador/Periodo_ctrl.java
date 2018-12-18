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

import java.net.URL;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.util.converter.LocalDateStringConverter;
import com.aulah.modelo.Centro;
import com.aulah.modelo.Conexion;
import com.aulah.modelo.Periodo;
import com.aulah.modelo.TipoPeriodo;
import java.sql.Date;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hector
 */
public class Periodo_ctrl implements Initializable {
    
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
    @FXML private ComboBox<String> cmbPeriodo;
    @FXML private DatePicker dtpFechaInicio;
    @FXML private DatePicker dtpFechaFinal;
    @FXML private ComboBox<Centro> cmbCentro;
    @FXML private ComboBox<TipoPeriodo> cmbTipoPeriodo;
    //Tabla Periodos
    @FXML private TableView<Periodo> tblPeriodo;
    //Columnas de la tabla
    @FXML private TableColumn<Periodo,Number> clmnCodigo;
    @FXML private TableColumn<Periodo,String> clmnPeriodo;
    @FXML private TableColumn<Periodo,Date> clmnFecha_inicio;
    @FXML private TableColumn<Periodo,Date> clmnFecha_final;
    @FXML private TableColumn<Periodo,String> clmnCentro;
    @FXML private TableColumn<Periodo,String> clmnTipo_periodo;
    //Botones del formulario
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //Para obtener el año en curso
    Calendar fecha = Calendar.getInstance();
    int año = fecha.get(Calendar.YEAR);
    //Lista para el combobox con valores predefinidos
    ObservableList<String> nombresPeriodoPropuesto = FXCollections.observableArrayList(
        "Periodo " + año + " -Anual",
        "Periodo " + año + " -Semestral",
        "Periodo " + año + " -Trimestral",
        "Periodo Académico " + año + " -Anual",
        "Periodo Académico " + año + " -Semestral",
        "Periodo Académico " + año + " -Trimestral",
        "Año Escolar " + año + " -Anual",
        "Año Escolar " + año + " -Semestral",
        "Año Escolar " + año + " -Trimestral",
        "Año Lectivo " + año + " -Anual",
        "Año Lectivo " + año + " -Semestral",
        "Año Lectivo " + año + " -Trimestral"
    );
    //Definir los ObservableList para llenarlo con objetos Centro, TipoPeriodo y Periodo
    private ObservableList<Centro> listaCentros = FXCollections.observableArrayList();
    private ObservableList<TipoPeriodo> listaTiposPeriodo = FXCollections.observableArrayList();
    private ObservableList<Periodo> listaPeriodo = FXCollections.observableArrayList();
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conecta a la BD
        conexion = new Conexion();
        Connection conn = conexion.establecerConexion();
        //Muestra los nombres de periodos propuestos
        cmbPeriodo.setItems(nombresPeriodoPropuesto);
        //Formatear las fechas en los DatePicker
        dtpFechaInicio.setConverter(new LocalDateStringConverter(FormatStyle.FULL));
        dtpFechaFinal.setConverter(new LocalDateStringConverter(FormatStyle.FULL));
        //obtiene los Centros en listaCentros
        Centro.llenarNombreCentros(conn, listaCentros);
        //Llena el ComboBox con los Centros
        cmbCentro.setItems(listaCentros);
        //obtiene los Tipos de Periodo en listaTiposPeriodo
        TipoPeriodo.llenarInformacionTipoPeriodo(conn, listaTiposPeriodo);
        //Llena el ComboBox de tipos de periodo
        cmbTipoPeriodo.setItems(listaTiposPeriodo);
        //Obtiene los Periodos en listaPeriodo
        Periodo.llenarInformacionPeriodo(conn, listaPeriodo);
        //Agrega la lista a la tabla
        tblPeriodo.setItems(listaPeriodo);
        //Llenar la columna de la tabla
        clmnPeriodo.setCellValueFactory(new PropertyValueFactory<Periodo, String>("periodo"));
        clmnFecha_inicio.setCellValueFactory(new PropertyValueFactory<Periodo,Date>("fecha_inicio"));
        clmnFecha_final.setCellValueFactory(new PropertyValueFactory<Periodo,Date>("fecha_final"));
        clmnCentro.setCellValueFactory(new PropertyValueFactory<Periodo,String>("cod_centro"));
        clmnTipo_periodo.setCellValueFactory(new PropertyValueFactory<Periodo,String>("cod_tipo_periodo"));
        //Gestiona los eventos en los controles del formulario
        gestionarEventos();
        //Inhabilitar y limpiar los controles
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblPeriodo.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Periodo>(){
                @Override
                public void changed(ObservableValue<? extends Periodo> ov, 
                    Periodo valorAnterior, Periodo valorSeleccionado) {
                    if(valorSeleccionado != null){
                        //Obtiene el Codigo del registro
                        codigo = valorSeleccionado.getCodigo();
                        //Establece los elementos del formulario
                        cmbPeriodo.setValue(valorSeleccionado.getPeriodo());
                        dtpFechaInicio.setValue(valorSeleccionado.getFecha_inicio().toLocalDate());
                        dtpFechaFinal.setValue(valorSeleccionado.getFecha_final().toLocalDate());
                        cmbCentro.setValue(valorSeleccionado.getCod_centro());
                        cmbTipoPeriodo.setValue(valorSeleccionado.getCod_tipo_periodo());
                        //Habilita los elementos necesarios
                        cmbPeriodo.setDisable(false);
                        dtpFechaInicio.setDisable(false);
                        dtpFechaFinal.setDisable(false);
                        cmbCentro.setDisable(false);
                        cmbTipoPeriodo.setDisable(false);
                        //Establece los botones
                        btnGuardar.setDisable(true);
                        btnModificar.setDisable(false);
                        btnEliminar.setDisable(false);
                    }
                }  
            }
        );
        //Ocurre cada vez que se escribe en el txtCentro
        cmbPeriodo.getEditor().textProperty().addListener(
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
        if(cmbPeriodo.getValue() != null){
            Periodo pr = new Periodo(0, cmbPeriodo.getValue(), Date.valueOf(dtpFechaInicio.getValue()), Date.valueOf(dtpFechaFinal.getValue()), cmbCentro.getValue(), cmbTipoPeriodo.getValue());
            conexion.establecerConexion();
            int resultado = pr.guardarRegistro(conexion.getConnection());
            codigo_generado = Periodo.codigo_generado;
            conexion.cerrarConexion();
            if(resultado == 1){
                listaPeriodo.add(pr);
                registro_recien_guardado = true;
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Periodo " + cmbPeriodo.getValue() + " ha sido agregado exitosamente");
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
        Periodo pr = new Periodo(codigo, cmbPeriodo.getValue(), Date.valueOf(dtpFechaInicio.getValue()), Date.valueOf(dtpFechaFinal.getValue()), cmbCentro.getValue(), cmbTipoPeriodo.getValue());
        conexion.establecerConexion();
        int resultado = pr.modificarRegistro(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            registro_recien_guardado = false;
            listaPeriodo.set(tblPeriodo.getSelectionModel().getSelectedIndex(), pr);
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro modificado");
            mensaje.setContentText("");
            mensaje.setHeaderText("El Periodo " + cmbPeriodo.getValue() + " ha sido modificado exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    private void eliminarRegistro(ActionEvent event){
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar registro");
        confirmar.setHeaderText("¿Está seguro que desea eliminar el Periodo " + cmbPeriodo.getValue() + " ?");
        confirmar.setContentText("");
        Optional<ButtonType> result = confirmar.showAndWait();
        if(result.get() == ButtonType.OK){
            if(registro_recien_guardado){
                codigo = codigo_generado;
            }
            Periodo pr = new Periodo(codigo, cmbPeriodo.getValue(), Date.valueOf(dtpFechaInicio.getValue()), Date.valueOf(dtpFechaFinal.getValue()), cmbCentro.getValue(), cmbTipoPeriodo.getValue());
            conexion.establecerConexion();
            int resultado = pr.eliminarRegistro(conexion.getConnection());
            conexion.cerrarConexion();
            if(resultado == 1){
                registro_recien_guardado = false;
                listaPeriodo.remove(tblPeriodo.getSelectionModel().getSelectedIndex());
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Periodo ha sido eliminado exitosamente");
                mensaje.show();
                inhabilitarLimpiarElementos();
            }
        }
    }
    
    @FXML
    public void nuevoRegistro(){
        nuevo_registro = true;
        cmbPeriodo.setValue(null);
        cmbPeriodo.setDisable(false);
        dtpFechaInicio.setValue(null);
        dtpFechaInicio.setDisable(false);
        dtpFechaFinal.setValue(null);
        dtpFechaFinal.setDisable(false);
        cmbCentro.setValue(null);
        cmbCentro.setDisable(false);
        cmbTipoPeriodo.setValue(null);
        cmbTipoPeriodo.setDisable(false);
        btnGuardar.setDisable(true);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        //Establece el foco
        cmbPeriodo.requestFocus();
    }   
    
    private void inhabilitarLimpiarElementos(){
        cmbPeriodo.setValue(null);
        cmbPeriodo.setDisable(true);
        dtpFechaInicio.setValue(null);
        dtpFechaInicio.setDisable(true);
        dtpFechaFinal.setValue(null);
        dtpFechaFinal.setDisable(true);
        cmbCentro.setValue(null);
        cmbCentro.setDisable(true);
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
