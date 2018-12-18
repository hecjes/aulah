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
import com.aulah.modelo.Seccion;
import com.aulah.modelo.Curso;
import com.aulah.modelo.Jornada;
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
 * Clase controlador para Seccion_ctrl
 *
 * @author hector
 */
public class Seccion_ctrl implements Initializable {

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
    @FXML private ComboBox<String> cmbSeccion;
    @FXML private ComboBox<Curso> cmbCurso;
    @FXML private ComboBox<Jornada> cmbJornada;
    @FXML private TableView<Seccion> tblSeccion;
    @FXML private TableColumn<Seccion, String> clmSeccion;
    @FXML private TableColumn<Seccion, String> clmCurso;
    @FXML private TableColumn<Seccion, String> clmJornada;
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //Lista para el combobox con valores predefinidos
    ObservableList<String> listaSeccionesPropuestas = FXCollections.observableArrayList(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H"
    );
    //Listas para los ComboBox
    private ObservableList<Curso> listaCursos = FXCollections.observableArrayList();
    private ObservableList<Jornada> listaJornadas = FXCollections.observableArrayList();
    //Lista para la tabla
    private ObservableList<Seccion> listaSecciones = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Llenar ComboBox
        cmbSeccion.setItems(listaSeccionesPropuestas);
        //Llenar la listas
        Curso.llenarInformacionCurso(conn, listaCursos);
        Jornada.llenarInformacionJornada(conn, listaJornadas);
        Seccion.llenarInformacionSeccion(conn, listaSecciones);
        //Agregar la listas a los ComboBox
        cmbCurso.setItems(listaCursos);
        cmbJornada.setItems(listaJornadas);
        //Agrega la lista a la tabla
        tblSeccion.setItems(listaSecciones);
        //Llenar la columna de la tabla
        clmSeccion.setCellValueFactory(new PropertyValueFactory<Seccion, String>("seccion"));
        clmCurso.setCellValueFactory(new PropertyValueFactory<Seccion, String>("cod_curso"));
        clmJornada.setCellValueFactory(new PropertyValueFactory<Seccion, String>("cod_jornada"));
        //Gestiona los eventos del formulario
        gestionarEventos();
        //Inhabilitar elementos
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }  
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblSeccion.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Seccion>(){
                @Override
                public void changed(ObservableValue<? extends Seccion> ov, 
                    Seccion valorAnterior, Seccion valorSeleccionado) {
                    if(valorSeleccionado != null){
                        codigo = valorSeleccionado.getCodigo();
                        cmbSeccion.setValue(valorSeleccionado.getSeccion());
                        cmbSeccion.setDisable(false);
                        cmbCurso.setValue(valorSeleccionado.getCod_curso());
                        cmbCurso.setDisable(false);
                        cmbJornada.setValue(valorSeleccionado.getCod_jornada());
                        cmbJornada.setDisable(false);
                        btnGuardar.setDisable(true);
                        btnModificar.setDisable(false);
                        btnEliminar.setDisable(false);
                    }
                }  
            }
        );
        //Ocurre cada vez que se escribe en el ComboBox
        cmbSeccion.getEditor().textProperty().addListener(
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
        if(cmbSeccion.getValue() != null){
            Seccion tp = new Seccion(0, cmbSeccion.getValue(), cmbCurso.getValue(), cmbJornada.getValue());
            conexion.establecerConexion();
            int resultado = tp.guardarRegistro(conexion.getConnection());
            codigo_generado = Curso.codigo_generado;
            conexion.cerrarConexion();
            if(resultado == 1){
                listaSecciones.add(tp);
                registro_recien_guardado = true;
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro agregado");
                mensaje.setContentText("");
                mensaje.setHeaderText("La Sección " + cmbSeccion.getValue() + " ha sido agregada exitosamente");
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
        Seccion jd = new Seccion(codigo, cmbSeccion.getValue(), cmbCurso.getValue(), cmbJornada.getValue());
        conexion.establecerConexion();
        int resultado = jd.modificarRegistro(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            registro_recien_guardado = false;
            listaSecciones.set(tblSeccion.getSelectionModel().getSelectedIndex(), jd);
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro modificado");
            mensaje.setContentText("");
            mensaje.setHeaderText("La Sección " + cmbSeccion.getValue() + " ha sido modificada exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    public void eliminarRegistro(){
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar registro");
        confirmar.setHeaderText("¿Está seguro que desea eliminar la Sección " + cmbSeccion.getValue() + "?");
        confirmar.setContentText("");
        Optional<ButtonType> result = confirmar.showAndWait();
            if(result.get() == ButtonType.OK){
                if(registro_recien_guardado){
                codigo = codigo_generado;
            }
            Seccion tp = new Seccion(codigo, cmbSeccion.getValue(), cmbCurso.getValue(), cmbJornada.getValue());
            conexion.establecerConexion();
            int resultado = tp.eliminarRegistro(conexion.getConnection());
            conexion.cerrarConexion();
            if(resultado == 1){
                registro_recien_guardado = false;
                listaSecciones.remove(tblSeccion.getSelectionModel().getSelectedIndex());
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("La Sección ha sido eliminada exitosamente");
                mensaje.show();
                inhabilitarLimpiarElementos();
            }
        }
    }
       
    @FXML
    public void nuevoRegistro(){
        nuevo_registro = true;
        cmbSeccion.setValue(null);
        cmbSeccion.setDisable(false);
        cmbCurso.setValue(null);
        cmbCurso.setDisable(false);
        cmbJornada.setValue(null);
        cmbJornada.setDisable(false);
        btnGuardar.setDisable(true);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        cmbSeccion.requestFocus();
    }
    
    private void inhabilitarLimpiarElementos(){
        cmbSeccion.setValue(null);
        cmbSeccion.setDisable(true);
        cmbCurso.setValue(null);
        cmbCurso.setDisable(true);
        cmbJornada.setValue(null);
        cmbJornada.setDisable(true);
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
