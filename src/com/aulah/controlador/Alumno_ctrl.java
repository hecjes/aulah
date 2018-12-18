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

import com.aulah.modelo.Alumno;
import com.aulah.modelo.Conexion;
import com.aulah.modelo.Curso;
import com.aulah.modelo.Jornada;
import com.aulah.modelo.Nivel;
import com.aulah.modelo.Periodo;
import com.aulah.modelo.Seccion;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


/**
 * Clase controlador para Alumno.fxml
 *
 * @author hector
 */
public class Alumno_ctrl implements Initializable {
    
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
    
    //Elementos GUI
    @FXML private TextField txtNombre;
    @FXML private TextField txtRne;
    @FXML private RadioButton rdbMasculino;
    @FXML private RadioButton rdbFemenino;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private ComboBox<Periodo> cmbPeriodo;
    @FXML private ComboBox<Nivel> cmbNivel;
    @FXML private ComboBox<Curso> cmbCurso;
    @FXML private ComboBox<Seccion> cmbSeccion;
    @FXML private TableView<Alumno> tblAlumno;
    
    //Define las columnas de la tabla
    @FXML private TableColumn<Alumno,String> clmNombre;
    @FXML private TableColumn<Alumno,String> clmRne;
    @FXML private TableColumn<Alumno,String> clmSexo;
    @FXML private TableColumn<Alumno,String> clmEstado;
    @FXML private TableColumn<Alumno,Periodo> clmPeriodo;
    @FXML private TableColumn<Alumno,Nivel> clmNivel;
    @FXML private TableColumn<Alumno,Jornada> clmJornada;
    @FXML private TableColumn<Alumno,Curso> clmCurso;
    @FXML private TableColumn<Alumno,Seccion> clmSeccion;
    //Botones del formulario
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //Listas para los ComboBox
    ObservableList<String> listaEstadoAlumno = FXCollections.observableArrayList(
                                                                    "MATRICULADO",
                                                                    "RETIRADO",
                                                                    "TRASLADADO"
                                                                    );
    private ObservableList<Periodo> listaPeriodos = FXCollections.observableArrayList();
    private ObservableList<Nivel> listaNiveles = FXCollections.observableArrayList();
    private ObservableList<Curso> listaCursos = FXCollections.observableArrayList();
    private ObservableList<Seccion> listaSecciones = FXCollections.observableArrayList();
    //Lista para la tabla
    private ObservableList<Alumno> listaAlumnos = FXCollections.observableArrayList();
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Asigna la lista listaEstadoAlumno al combobox cmbEstado
        cmbEstado.setItems(listaEstadoAlumno);
        //Llenar las listas desde la BD
        Periodo.llenarNombrePeriodos(conn, listaPeriodos);
        
        //llena la lista desde Alumno desde la BD
        Alumno.llenarInformacionAlumno(conn, listaAlumnos);
        //Asigna los items de los ComboBox
        cmbPeriodo.setItems(listaPeriodos);
                       
        //Asigna los items de la tabla con los valores de la lista
        tblAlumno.setItems(listaAlumnos);
        //Establece los valores para cada una de las columnas de la tabla
        clmNombre.setCellValueFactory(new PropertyValueFactory<Alumno,String>("nombre"));
        clmRne.setCellValueFactory(new PropertyValueFactory<Alumno,String>("rne"));
        clmSexo.setCellValueFactory(new PropertyValueFactory<Alumno,String>("sexo"));
        clmEstado.setCellValueFactory(new PropertyValueFactory<Alumno,String>("estado"));
        clmPeriodo.setCellValueFactory(new PropertyValueFactory<Alumno,Periodo>("periodo"));
        clmNivel.setCellValueFactory(new PropertyValueFactory<Alumno,Nivel>("nivel"));
        clmJornada.setCellValueFactory(new PropertyValueFactory<Alumno,Jornada>("jornada"));
        clmCurso.setCellValueFactory(new PropertyValueFactory<Alumno,Curso>("curso"));
        clmSeccion.setCellValueFactory(new PropertyValueFactory<Alumno,Seccion>("seccion"));
        //Gestiona los eventos del formulario
        gestionarEventos();
        //Inhabilitar elementos
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }   
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblAlumno.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Alumno>(){
                @Override
                public void changed(ObservableValue<? extends Alumno> ov, 
                    Alumno valorAnterior, Alumno valorSeleccionado) {
                    if(valorSeleccionado != null){
                        codigo = valorSeleccionado.getCodigo();
                        txtNombre.setText(valorSeleccionado.getNombre());
                        txtNombre.setDisable(false);
                        txtRne.setText(valorSeleccionado.getRne());
                        txtRne.setDisable(false);                        
                        if(valorSeleccionado.getSexo().equals("MASCULINO")){
                            rdbMasculino.setSelected(true);
                        }
                        rdbMasculino.setDisable(false);                        
                        if(valorSeleccionado.getSexo().equals("FEMENINO")){
                            rdbFemenino.setSelected(true);
                        }   
                        rdbFemenino.setDisable(false);
                        cmbEstado.setValue(valorSeleccionado.getEstado());
                        cmbEstado.setDisable(false);
                        cmbPeriodo.setValue(valorSeleccionado.getPeriodo());
                        cmbPeriodo.setDisable(false);
                        cmbNivel.setValue(valorSeleccionado.getNivel());
                        cmbNivel.setDisable(false);
                        cmbCurso.setValue(valorSeleccionado.getCurso());
                        cmbCurso.setDisable(false);
                        cmbSeccion.setValue(valorSeleccionado.getSeccion());
                        cmbSeccion.setDisable(false);
                        btnGuardar.setDisable(true);
                        btnModificar.setDisable(false);
                        btnEliminar.setDisable(false);
                    }
                }  
            }
        );
        //Ocurre cada vez que se escribe en el ComboBox
        txtNombre.textProperty().addListener(
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
    
    @FXML public void mostrarCursosNivel(){
        if(cmbNivel.getValue() != null){
            int codigo_nivel = cmbNivel.getSelectionModel().getSelectedItem().getCodigo();
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            //Limpia el ComboBox
            cmbCurso.getItems().clear();
            //Llena la lista desde la BD
            Curso.llenarNombreCursosNivel(conn, listaCursos, codigo_nivel);
            //Muestra el contenido de la lista en el ComboBox
            cmbCurso.setItems(listaCursos);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
    
    @FXML public void mostrarSeccionesCursos(){
        if(cmbCurso.getValue() != null){
            int codigo_curso = cmbCurso.getSelectionModel().getSelectedItem().getCodigo();
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            //Limpia el ComboBox
            cmbSeccion.getItems().clear();
            //Llena la lista desde la BD
            Seccion.llenarNombreSeccionCurso(conn, listaSecciones, codigo_curso);
            //Muestra el contenido de la lista en el ComboBox
            cmbSeccion.setItems(listaSecciones);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
    
    @FXML public int obtenerCodigoJornada(int p_codigo_seccion){
        if(cmbSeccion.getValue() != null){
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            int codigo_jornada = Seccion.obtenerCodigoJornadaSeccion(conn, p_codigo_seccion);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
            return codigo_jornada;
        }else{
            return 0;
        }
    }
    
    @FXML public String obtenerJornada(int p_codigo_jornada){
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            String jornada = Jornada.obtenerNombreJornada(conn, p_codigo_jornada);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
            return jornada;
    }
    
    @FXML
    public void guardarRegistro(){
        if((txtNombre.getText() == null) | (txtRne.getText() == null)){
            registro_recien_guardado = false;
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Datos vacíos");
            mensaje.setContentText("");
            mensaje.setHeaderText("Ingrese la información requerida");
            mensaje.show();
        }else{
            if(rdbFemenino.isSelected() || rdbMasculino.isSelected()){
                if(cmbEstado.getValue().isEmpty() || cmbPeriodo.getSelectionModel().isEmpty() || cmbNivel.getSelectionModel().isEmpty() || cmbCurso.getSelectionModel().isEmpty() || cmbSeccion.getSelectionModel().isEmpty()){
                    Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Datos vacíos");
                    mensaje.setContentText("");
                    mensaje.setHeaderText("Seleccione los datos requeridos");
                    mensaje.show();
                }else{
                    String sexo = "";
                    if(rdbFemenino.isSelected()){sexo = "FEMENINO";}
                    if(rdbMasculino.isSelected()){sexo = "MASCULINO";}
                    int codigo_seccion = cmbSeccion.getSelectionModel().getSelectedItem().getCodigo();
                    int codigo_jornada = obtenerCodigoJornada(codigo_seccion);
                    String nombre_jornada = obtenerJornada(codigo_jornada);
                    Alumno al = new Alumno(0, txtNombre.getText(), txtRne.getText(), sexo, cmbEstado.getValue(), cmbPeriodo.getValue(), cmbNivel.getValue(), cmbCurso.getValue(), cmbSeccion.getValue(), nombre_jornada, codigo_jornada);
                    conexion.establecerConexion();
                    int resultado = al.guardarRegistro(conexion.getConnection());
                    codigo_generado = Curso.codigo_generado;
                    conexion.cerrarConexion();
                    if(resultado == 1){
                        listaAlumnos.add(al);
                        registro_recien_guardado = true;
                        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                        mensaje.setTitle("Registro agregado");
                        mensaje.setContentText("");
                        mensaje.setHeaderText("El Alumno(a) < " + txtNombre.getText() + " > ha sido agregado(a) exitosamente");
                        mensaje.show();
                        inhabilitarLimpiarElementos();
                        nuevo_registro = false;
                    }
                }
            }else{
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Datos vacíos");
                mensaje.setContentText("");
                mensaje.setHeaderText("Seleccione el sexo del Alumno(a)");
                mensaje.show();
            }
        } 
    }
    
    @FXML
    public void modificarRegistro(){
        if(registro_recien_guardado){
            codigo = codigo_generado;
        }
        String sexo = "";
        if(rdbFemenino.isSelected()){sexo = "FEMENINO";}
        if(rdbMasculino.isSelected()){sexo = "MASCULINO";}
        int codigo_seccion = cmbSeccion.getSelectionModel().getSelectedItem().getCodigo();
        System.out.println("codigo_seccion = " + codigo_seccion);
        int codigo_jornada = obtenerCodigoJornada(codigo_seccion);
        Alumno jd = new Alumno(codigo, txtNombre.getText(), txtRne.getText(), sexo, cmbEstado.getValue(), cmbPeriodo.getValue(), cmbNivel.getValue(), cmbCurso.getValue(), cmbSeccion.getValue(), codigo_jornada);
        conexion.establecerConexion();
        int resultado = jd.modificarRegistro(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            registro_recien_guardado = false;
            listaAlumnos.set(tblAlumno.getSelectionModel().getSelectedIndex(), jd);
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro modificado");
            mensaje.setContentText("");
            mensaje.setHeaderText("El(la) Alumno(a) < " + txtNombre.getText() + " > ha sido modificado(a) exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    public void eliminarRegistro(){
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Eliminar registro");
        confirmar.setHeaderText("¿Está seguro que desea eliminar el(la) Alumno(a) < " + txtNombre.getText() + " >?");
        confirmar.setContentText("");
        Optional<ButtonType> result = confirmar.showAndWait();
            if(result.get() == ButtonType.OK){
                if(registro_recien_guardado){
                codigo = codigo_generado;
            }
            String sexo = "";
            if(rdbFemenino.isSelected()){sexo = "FEMENINO";}
            if(rdbMasculino.isSelected()){sexo = "MASCULINO";}
            int codigo_seccion = cmbSeccion.getSelectionModel().getSelectedItem().getCodigo();
            int codigo_jornada = obtenerCodigoJornada(codigo_seccion);
            Alumno tp = new Alumno(codigo, txtNombre.getText(), txtRne.getText(), sexo, cmbEstado.getValue(), cmbPeriodo.getValue(), cmbNivel.getValue(), cmbCurso.getValue(), cmbSeccion.getValue(), codigo_jornada);
            conexion.establecerConexion();
            int resultado = tp.eliminarRegistro(conexion.getConnection());
            conexion.cerrarConexion();
            if(resultado == 1){
                registro_recien_guardado = false;
                listaAlumnos.remove(tblAlumno.getSelectionModel().getSelectedIndex());
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Registro eliminado");
                mensaje.setContentText("");
                mensaje.setHeaderText("El Alumno(a) ha sido eliminada(a) exitosamente");
                mensaje.show();
                inhabilitarLimpiarElementos();
            }
        }
    }
    
    @FXML
    public void nuevoRegistro(){
        nuevo_registro = true;
        txtNombre.setText(null);
        txtNombre.setDisable(false);
        txtRne.setText(null);
        txtRne.setDisable(false);
        rdbMasculino.setSelected(false);
        rdbMasculino.setDisable(false);
        rdbFemenino.setSelected(false);
        rdbFemenino.setDisable(false);
        cmbEstado.setValue(null);
        cmbEstado.setDisable(false);
        cmbPeriodo.setValue(null);
        cmbPeriodo.setDisable(false);
        cmbNivel.setValue(null);
        cmbNivel.setDisable(false);
        cmbCurso.setValue(null);
        cmbCurso.setDisable(false);
        cmbSeccion.setValue(null);
        cmbSeccion.setDisable(false);
        btnGuardar.setDisable(true);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        txtNombre.requestFocus();
    }
    
    private void inhabilitarLimpiarElementos(){
        txtNombre.setText(null);
        txtNombre.setDisable(true);
        txtRne.setText(null);
        txtRne.setDisable(true);
        rdbMasculino.setSelected(false);
        rdbMasculino.setDisable(true);
        rdbFemenino.setSelected(false);
        rdbFemenino.setDisable(true);
        cmbEstado.setValue(null);
        cmbEstado.setDisable(true);
        cmbPeriodo.setValue(null);
        cmbPeriodo.setDisable(true);
        cmbNivel.setValue(null);
        cmbNivel.setDisable(true);
        cmbCurso.setValue(null);
        cmbCurso.setDisable(true);
        cmbSeccion.setValue(null);
        cmbSeccion.setDisable(true);
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
