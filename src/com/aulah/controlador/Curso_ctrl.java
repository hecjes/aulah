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
 * Clase controlador de Curso_ctrl
 *
 * @author hector
 */
public class Curso_ctrl implements Initializable {

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
    @FXML private ComboBox<String> cmbCurso;
    @FXML private ComboBox<Periodo> cmbPeriodo;
    @FXML private ComboBox<Nivel> cmbNivel;
    @FXML private TableView<Curso> tblCurso;
    @FXML private TableColumn<Curso, String> clmCurso;
    @FXML private TableColumn<Curso, String> clmPeriodo;
    @FXML private TableColumn<Curso, String> clmNivel;
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //Lista para el combobox con valores predefinidos
    ObservableList<String> listaCursosPropuestos = FXCollections.observableArrayList(
        "1° CBT",
        "2° CBT",
        "3° CBT",
        "1° BTP - ",
        "2° BTP - ",
        "3° BTP - ",
        "1° BCH",
        "2° BCHs",
        "I Ciclo Básico Técnico",
        "II Ciclo Básico Técnico",
        "III Ciclo Básico Técnico",
        "I Bach. Tec. Prof - ",
        "II Bach. Tec. Prof - ",
        "III Bach. Tec. Prof - ",
        "I Bach. Cienc. Humanidades",
        "II Bach. Cienc. Humanidades"
    );
    //Listas para los ComboBox
    private ObservableList<Periodo> listaPeriodos = FXCollections.observableArrayList();
    private ObservableList<Nivel> listaNiveles = FXCollections.observableArrayList();
    //Lista para la tabla
    private ObservableList<Curso> listaCursos = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Llenar ComboBox
        cmbCurso.setItems(listaCursosPropuestos);
        //Llenar la listas
        Periodo.llenarInformacionPeriodo(conn, listaPeriodos);
        Nivel.llenarInformacionNivel(conn, listaNiveles);
        Curso.llenarInformacionCurso(conn, listaCursos);
        //Agregar la listas a los ComboBox
        cmbPeriodo.setItems(listaPeriodos);
        cmbNivel.setItems(listaNiveles);
        //Agrega la lista a la tabla
        tblCurso.setItems(listaCursos);
        //Llenar la columna de la tabla
        clmCurso.setCellValueFactory(new PropertyValueFactory<Curso, String>("curso"));
        clmPeriodo.setCellValueFactory(new PropertyValueFactory<Curso, String>("cod_periodo"));
        clmNivel.setCellValueFactory(new PropertyValueFactory<Curso, String>("cod_nivel"));
        //Gestiona los eventos del formulario
        gestionarEventos();
        //Inhabilitar elementos
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }  
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblCurso.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Curso>(){
                @Override
                public void changed(ObservableValue<? extends Curso> ov, 
                    Curso valorAnterior, Curso valorSeleccionado) {
                    if(valorSeleccionado != null){
                        codigo = valorSeleccionado.getCodigo();
                        cmbCurso.setValue(valorSeleccionado.getCurso());
                        cmbCurso.setDisable(false);
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
        cmbCurso.getEditor().textProperty().addListener(
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
    public void guardarRegistro(){
        if(cmbCurso.getValue() != null){
            Curso tp = new Curso(0, cmbCurso.getValue(), cmbPeriodo.getValue(), cmbNivel.getValue());
            conexion.establecerConexion();
            int resultado = tp.guardarRegistro(conexion.getConnection());
            codigo_generado = Curso.codigo_generado;
            conexion.cerrarConexion();
            if(resultado == 1){
                listaCursos.add(tp);
                registro_recien_guardado = true;
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Curso " + cmbCurso.getValue() + " ha sido agregada exitosamente");
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
        Curso jd = new Curso(codigo, cmbCurso.getValue(), cmbPeriodo.getValue(), cmbNivel.getValue());
        conexion.establecerConexion();
        int resultado = jd.modificarRegistro(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            registro_recien_guardado = false;
            listaCursos.set(tblCurso.getSelectionModel().getSelectedIndex(), jd);
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro modificado");
            mensaje.setContentText("");
            mensaje.setHeaderText("El Curso " + cmbCurso.getValue() + " ha sido modificado exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    public void eliminarRegistro(){
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar registro");
        confirmar.setHeaderText("¿Está seguro que desea eliminar el Curso " + cmbCurso.getValue() + "?");
        confirmar.setContentText("");
        Optional<ButtonType> result = confirmar.showAndWait();
            if(result.get() == ButtonType.OK){
                if(registro_recien_guardado){
                codigo = codigo_generado;
            }
            Curso tp = new Curso(codigo, cmbCurso.getValue(), cmbPeriodo.getValue(), cmbNivel.getValue());
            conexion.establecerConexion();
            int resultado = tp.eliminarRegistro(conexion.getConnection());
            conexion.cerrarConexion();
            if(resultado == 1){
                registro_recien_guardado = false;
                listaCursos.remove(tblCurso.getSelectionModel().getSelectedIndex());
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Curso ha sido eliminada exitosamente");
                mensaje.show();
                inhabilitarLimpiarElementos();
            }
        }
    }
       
    @FXML
    public void nuevoRegistro(){
        nuevo_registro = true;
        cmbCurso.setValue(null);
        cmbCurso.setDisable(false);
        cmbPeriodo.setValue(null);
        cmbPeriodo.setDisable(false);
        cmbNivel.setValue(null);
        cmbNivel.setDisable(false);
        btnGuardar.setDisable(true);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        cmbCurso.requestFocus();
    }
    
    private void inhabilitarLimpiarElementos(){
        cmbCurso.setValue(null);
        cmbCurso.setDisable(true);
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
