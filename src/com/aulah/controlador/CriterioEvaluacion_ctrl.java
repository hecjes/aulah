/*
 * Copyright (C) 2018 hector
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
import com.aulah.modelo.CriterioEvaluacion;
import com.aulah.modelo.Nivel;
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
 * FXML Controller class
 *
 * @author hector
 */
public class CriterioEvaluacion_ctrl implements Initializable {
    
    int codigo = 0;//codigo: Variable para guardar el Codigo del de la Jornada para usarlo cuando
    //se Actualiza o Elimina un registro ya que el Codigo no se muestra en ningun elemento del GUI.
    int codigo_generado = 0;//Se usa para saber el codigo generado cuando se guarda un nuevo registro,
    //ya que el codigo es autoincrementado en la BD.
    boolean registro_recien_guardado = false;//Se usa para saber si se ha guardado un registro antes de mandar
    //a modificar el mismo registro.
    boolean nuevo_registro = false;//Se usa para saber si se va a ingresar datos en el formulario
    //para un nuevo registro que luego se piensa guardar.
    
    //Para conectar a la a la BD
    Conexion conexion = new Conexion();
    
    //Componentes de la GUI
    @FXML private ComboBox<String> cmbCriterioEvaluacion;
    @FXML private ComboBox<Nivel> cmbNivel;
    @FXML private ComboBox<String> cmbValor;
    @FXML private TableView<CriterioEvaluacion> tblCriterioEvaluacion;
    @FXML private TableColumn<CriterioEvaluacion, String> clmCriterioEvaluacion;
    @FXML private TableColumn<CriterioEvaluacion, String> clmValor;
    @FXML private TableColumn<CriterioEvaluacion, String> clmNivel;
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //Lista para el combobox con valores predefinidos
    ObservableList<String> tiposEvaluacionPropuestas = FXCollections.observableArrayList(
        "Tareas en casa",
        "Trabajos en el aula",
        "Prueba(Examen)"
    );
    //Lista para el combobox cmbValores
    ObservableList<String> valoresPropuestos = FXCollections.observableArrayList(
        "5",
        "10",
        "15",
        "20",
        "25",
        "30",
        "35",
        "40",
        "45",
        "50"
    );
    //Lista para el Combobox cmbNiveles
    private ObservableList<Nivel> listaNiveles = FXCollections.observableArrayList();
    //Lista para la tabla
    private ObservableList<CriterioEvaluacion> listaCriterioEvaluacion = FXCollections.observableArrayList();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Hace una nueva conexion a la a la BD
        Connection conn = conexion.establecerConexion();
        //Llenar lista de Niveles
        Nivel.llenarInformacionNivel(conn, listaNiveles);
        //Llenar ComboBox Niveles
        cmbNivel.setItems(listaNiveles);
        //Llenar ComboBox Criterios de Evaluacion
        cmbCriterioEvaluacion.setItems(tiposEvaluacionPropuestas);
        //Llenar el ComboBox cmbValores
        cmbValor.setItems(valoresPropuestos);
        //Llenar la lista para la TablaView
        CriterioEvaluacion.llenarInformacionCriterioEvaluacion(conn, listaCriterioEvaluacion);
        //Agrega la lista a la tabla
        tblCriterioEvaluacion.setItems(listaCriterioEvaluacion);
        //Mostrar las columnas de la tabla
        clmCriterioEvaluacion.setCellValueFactory(new PropertyValueFactory<CriterioEvaluacion, String>("criterio"));
        clmValor.setCellValueFactory(new PropertyValueFactory<CriterioEvaluacion, String>("valor"));
        clmNivel.setCellValueFactory(new PropertyValueFactory<CriterioEvaluacion, String>("nivel"));
        //Comprobar para saber si la suma de criterios de evaluacion para cada nivel, estan en su valor maximo 100
        if(listaCriterioEvaluacion.size() > 0){
            int cont_valor = 0;
            int cont_nivel_lleno = 0;
            for(Nivel niveles : listaNiveles){
                for(CriterioEvaluacion criterios : listaCriterioEvaluacion){
                    if(niveles.getNivel().toString().equals(criterios.getNivel().toString())){
                        cont_valor = cont_valor + criterios.getValor();
                    }
                }
                if(cont_valor == 100){
                    cont_nivel_lleno++;
                    cont_valor = 0;
                }
            }
            if(cont_nivel_lleno == listaNiveles.size()){
                btnNuevo.setDisable(true);
                System.out.println("Los criterios para todos los niveles estan llenos");
            }
        }
        //Gestiona los eventos del formulario
        gestionarEventos();
        //Inhabilitar elementos
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }  
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblCriterioEvaluacion.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<CriterioEvaluacion>(){
                @Override
                public void changed(ObservableValue<? extends CriterioEvaluacion> ov, 
                    CriterioEvaluacion valorAnterior, CriterioEvaluacion valorSeleccionado) {
                    if(valorSeleccionado != null){
                        codigo = valorSeleccionado.getCodigo();
                        cmbNivel.setValue(valorSeleccionado.getNivel());
                        cmbNivel.setDisable(false);
                        cmbCriterioEvaluacion.setValue(valorSeleccionado.getCriterio());
                        cmbCriterioEvaluacion.setDisable(false);
                        cmbValor.setValue(String.valueOf(valorSeleccionado.getValor()));
                        cmbValor.setDisable(false);
                        btnGuardar.setDisable(true);
                        btnModificar.setDisable(false);
                        btnEliminar.setDisable(false);
                    }
                }  
            }
        );
        //Ocurre cada vez que se escribe en el ComboBox cmbCriterioEvaluacion
        cmbCriterioEvaluacion.getEditor().textProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(nuevo_registro){
                        cmbValor.setDisable(false);
                    }                   
                }
        });
        //Ocurre cada vez que se escribe en el ComboBox cmbValor
        cmbValor.getEditor().textProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(nuevo_registro){
                        btnGuardar.setDisable(false);
                    }                   
                }
        });
    }
    
    @FXML
    private void habilitarCriterioEvaluacion(){
        cmbCriterioEvaluacion.setDisable(false);
    }
    
    @FXML
    private void habilitarValor(){
        cmbValor.setDisable(false);
    }
    
    @FXML
    public void guardarRegistro(){
        if(cmbCriterioEvaluacion.getValue() != null){
            //Verificar si ya hay 100
            int cont_valor_anterior = 0;
            int cont_valor_nuevo = 0;
            for(CriterioEvaluacion criterio : listaCriterioEvaluacion){
                if(criterio.getNivel().toString().equals(cmbNivel.getValue().toString())){
                    cont_valor_anterior = cont_valor_anterior + criterio.getValor();
                    cont_valor_nuevo = cont_valor_anterior + Integer.parseInt(cmbValor.getValue());
                    System.out.println("El valor nuevo es " + cont_valor_nuevo);
                }
            }
            if(cont_valor_anterior == 100){
                    Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Valor completo");
                    mensaje.setContentText("");
                    mensaje.setHeaderText("El valor de los Criterios a Evaluar para el nivel <" + cmbNivel.getValue() + "> ya suman 100 puntos.\nNo es posible agregar mas Criterios de Evaluación para este Nivel");
                    mensaje.show();
                    inhabilitarLimpiarElementos();
            }else{
                if(cont_valor_nuevo <= 100){
                    CriterioEvaluacion ce = new CriterioEvaluacion(0, cmbCriterioEvaluacion.getValue(), Integer.parseInt(cmbValor.getValue()) , cmbNivel.getValue());
                    conexion.establecerConexion();
                    int resultado = ce.guardarRegistro(conexion.getConnection());
                    codigo_generado = CriterioEvaluacion.CODIGO_GENERADO;
                    conexion.cerrarConexion();
                    if(resultado == 1){
                        listaCriterioEvaluacion.add(ce);
                        registro_recien_guardado = true;
                        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                        mensaje.setTitle("Registro agregado");
                        mensaje.setContentText("");
                        mensaje.setHeaderText("El Criterio de Evaluacion <" + cmbCriterioEvaluacion.getValue() + "> ha sido agregada exitosamente al nivel " + cmbNivel.getValue() + ".");
                        mensaje.show();
                        inhabilitarLimpiarElementos();
                        nuevo_registro = false;
                        btnNuevo.setDisable(true);
                    }
                }else{
                    Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Valor total mayor que 100");
                    mensaje.setContentText("");
                    mensaje.setHeaderText("El valor " + cont_valor_nuevo + " para los Criterios a Evaluar del nivel <" + cmbNivel.getValue() + "> se pasa de 100 puntos.\nNo es posible agregar un Criterios de Evaluación con valor de " + cmbValor.getValue() + " puntos para este Nivel");
                    mensaje.show();
                    inhabilitarLimpiarElementos();
                }
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
        CriterioEvaluacion ce = new CriterioEvaluacion(codigo, cmbCriterioEvaluacion.getSelectionModel().getSelectedItem().toString(), Integer.parseInt(cmbValor.getSelectionModel().getSelectedItem()) , cmbNivel.getSelectionModel().getSelectedItem());
        conexion.establecerConexion();
        int resultado = ce.modificarRegistro(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            registro_recien_guardado = false;
            listaCriterioEvaluacion.set(tblCriterioEvaluacion.getSelectionModel().getSelectedIndex(), ce);
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro modificado");
            mensaje.setContentText("");
            mensaje.setHeaderText("El Criterio de Evaluación <" + cmbCriterioEvaluacion.getValue() + "> ha sido modificado exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    public void eliminarRegistro(){
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar registro");
        confirmar.setHeaderText("¿Está seguro que desea eliminar el Criterio de Evaluación <" + cmbCriterioEvaluacion.getValue() + ">?");
        confirmar.setContentText("");
        Optional<ButtonType> result = confirmar.showAndWait();
            if(result.get() == ButtonType.OK){
                if(registro_recien_guardado){
                codigo = codigo_generado;
            }
            CriterioEvaluacion tp = new CriterioEvaluacion(codigo, cmbCriterioEvaluacion.getSelectionModel().getSelectedItem().toString());
            conexion.establecerConexion();
            int resultado = tp.eliminarRegistro(conexion.getConnection());
            //int resultado = tblViewTipoPeriodo.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.getConnection());
            conexion.cerrarConexion();
            if(resultado == 1){
                registro_recien_guardado = false;
                listaCriterioEvaluacion.remove(tblCriterioEvaluacion.getSelectionModel().getSelectedIndex());
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Criterio de Evaluación ha sido eliminada exitosamente");
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
        cmbCriterioEvaluacion.requestFocus();
    }
    
    private void inhabilitarLimpiarElementos(){
        cmbNivel.setValue(null);
        cmbNivel.setDisable(true);
        cmbCriterioEvaluacion.setValue(null);
        cmbCriterioEvaluacion.setDisable(true);
        cmbValor.setValue(null);
        cmbValor.setDisable(true);
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
