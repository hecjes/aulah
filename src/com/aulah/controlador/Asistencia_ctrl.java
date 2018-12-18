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
import com.aulah.modelo.Curso;
import com.aulah.modelo.Asistencia;
import com.aulah.modelo.ParcialAnual;
import com.aulah.modelo.ParcialSemestral;
import com.aulah.modelo.Periodo;
import com.aulah.modelo.Seccion;
import com.aulah.modelo.Semestre;
import com.aulah.utilidades.AsistenciaArrLst;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;


/**
 * Clase controlador para Alumno.fxml
 *
 * @author hector
 */
public class Asistencia_ctrl implements Initializable {
    
    int codigo_asistencia = 0;//codigo: Variable para guardar el Codigo del de la Asistencia para usarlo cuando
                   //se Actualiza o Elimina un registro ya que el Codigo no se muestra en ningun elemento del GUI.
    
    boolean registro_recien_guardado = false;//Se usa para saber si se ha guardado un registro antes de mandar
                                             //a modificar el mismo registro.
    boolean nuevo_registro = false;//Se usa para saber si se va a ingresar datos en el formulario
                                   //para un nuevo registro que luego se piensa guardar.
    Asistencia.Opciones ASISTIR = Asistencia.Opciones.SI;//Se usa para establecer SI como opcion por defecto.
    
    Calendar tiempo = new GregorianCalendar();
    int hora = tiempo.get(Calendar.HOUR_OF_DAY);
    int minutos = tiempo.get(Calendar.MINUTE);
    int segundos = tiempo.get(Calendar.SECOND);
    
//Hace una nueva conexion a la a la BD
    Conexion conexion = new Conexion();
    
    //Elementos GUI
    @FXML private ComboBox<Periodo> cmbPeriodo;
    @FXML private ComboBox<Semestre> cmbSemestre;
    @FXML private ComboBox<ParcialAnual> cmbParcialAnual;
    @FXML private ComboBox<ParcialSemestral> cmbParcialSemestral;
    @FXML private ComboBox<Curso> cmbCurso;
    @FXML private ComboBox<Seccion> cmbSeccion;
    @FXML private ComboBox<Clase> cmbClase;
    @FXML private DatePicker dtpFecha;
    @FXML private TextArea txtDescripcion;
    @FXML private TableView<Asistencia> tblAsistencia;
    
    //Define las columnas de la tabla
    @FXML private TableColumn<Asistencia,Integer> clmNo;
    @FXML private TableColumn<Asistencia,String> clmNombre;
    @FXML private TableColumn<Asistencia,Asistencia.Opciones> clmAsistio;
    
    //Botones del formulario
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //ArrayList para llenar el ObservableList
    private ArrayList<AsistenciaArrLst> lista = new ArrayList<AsistenciaArrLst>();
    //Listas para los ComboBox
    private ObservableList<Periodo> listaPeriodo = FXCollections.observableArrayList();
    private ObservableList<Semestre> listaSemestre = FXCollections.observableArrayList();
    private ObservableList<ParcialAnual> listaParcialAnual = FXCollections.observableArrayList();
    private ObservableList<ParcialSemestral> listaParcialSemestral = FXCollections.observableArrayList();
    private ObservableList<Curso> listaCurso = FXCollections.observableArrayList();
    private ObservableList<Seccion> listaSeccion = FXCollections.observableArrayList();
    private ObservableList<Clase> listaClase = FXCollections.observableArrayList();
    //Lista para la tabla
    private ObservableList<Asistencia> listaAsistencia = FXCollections.observableArrayList();
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Llenar las listas desde la BD
        Periodo.llenarNombrePeriodos(conn, listaPeriodo);
        System.out.println("\t\tMostrando Periodos " + listaPeriodo.size());
        //Hace editable el TableView
        tblAsistencia.setEditable(true);
        //Hace editable la columna Asistio
        clmAsistio.setCellValueFactory(new PropertyValueFactory<Asistencia, Asistencia.Opciones>("opcion"));
        clmAsistio.setCellFactory(ChoiceBoxTableCell.forTableColumn(Asistencia.Opciones.SI, Asistencia.Opciones.NO, Asistencia.Opciones.EXCUSA));
        clmAsistio.setOnEditCommit(data -> {
            Asistencia p = data.getRowValue();
            p.setOpciones(data.getNewValue());
            System.out.println("Se Editó: " + data.getRowValue());
        });
        //Asigna los items de los ComboBox
        cmbPeriodo.setItems(listaPeriodo);
        //Configura el TextArea
        txtDescripcion.setWrapText(true);
        //Gestiona los eventos del formulario
        //gestionarEventos();
        //Inhabilitar elementos
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }   
    
    @FXML
    private void mostrarSemestresOParciales(){
        if(cmbPeriodo.getValue() != null){
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            if(Periodo.periodoEsSemestral(conn, codigo_periodo)){
                mostrarSemestresPeriodo(conn);
                cmbParcialSemestral.setDisable(true);
                cmbSemestre.setDisable(false);
                cmbParcialSemestral.setVisible(true);
                cmbParcialAnual.setVisible(false);
                cmbParcialAnual.setDisable(true);
            }
            if(Periodo.periodoEsAnual(conn, codigo_periodo)){
                mostrarParcialesAnuales(conn);
                cmbSemestre.setValue(null);
                cmbSemestre.setDisable(true);
                cmbParcialAnual.setVisible(true);
                cmbParcialSemestral.setVisible(false);
                cmbParcialAnual.setDisable(false);
            }
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
    
    public void mostrarSemestresPeriodo(Connection con){
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            //Limpia el ComboBox
            cmbSemestre.getItems().clear();
            //Llena la lista desde la BD
            Semestre.llenarInformacionSemestresPeriodo(con, listaSemestre, codigo_periodo);
            System.out.println("La lista de Semestres del Periodo tiene " + listaSemestre.size());
            //Muestra el contenido de la lista en el ComboBox
            cmbSemestre.setItems(listaSemestre);
    }
    
    public void mostrarParcialesAnuales(Connection con){
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            //Limpia el ComboBox
            cmbParcialAnual.getItems().clear();
            //Llena la lista desde la BD
            ParcialAnual.llenarInformacionParcialAnualPeriodo(con, listaParcialAnual, codigo_periodo);
            System.out.println("La lista de Parciales Anuales tiene " + listaParcialAnual.size());
            //Muestra el contenido de la lista en el ComboBox
            cmbParcialAnual.setItems(listaParcialAnual);
    }
    
    @FXML
    public void mostrarParcialesSemestrales(){
        if(cmbSemestre.getValue() != null){
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            int codigo_semestre = cmbSemestre.getSelectionModel().getSelectedItem().getCodigo();
            //Limpia el ComboBox
            cmbParcialSemestral.getItems().clear();
            //Llena la lista desde la BD
            ParcialSemestral.llenarInformacionParcialSemestre(conn, listaParcialSemestral, codigo_semestre);
            System.out.println("La lista de Parciales Semestrales tiene " + listaParcialSemestral.size());
            //Muestra el contenido de la lista en el ComboBox
            cmbParcialSemestral.setItems(listaParcialSemestral);
            cmbParcialSemestral.setDisable(false);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }   
    }
 
    @FXML 
    public void mostrarCursosPeriodo(){
        if(cmbPeriodo.getValue() != null){
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            //Limpia el ComboBox
            cmbCurso.getItems().clear();
            //Llena la lista desde la BD
            Curso.llenarInformacionCursoPeriodo(conn, listaCurso, codigo_periodo);
            //Muestra el contenido de la lista en el ComboBox
            cmbCurso.setItems(listaCurso);
            cmbCurso.setDisable(false);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
      
    @FXML 
    public void mostrarSeccionesCursos(){
        if(cmbCurso.getValue() != null){
            int codigo_curso = cmbCurso.getSelectionModel().getSelectedItem().getCodigo();
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            //Limpia el TableView
            tblAsistencia.getItems().clear();
            //Limpia el ComboBox
            cmbSeccion.getItems().clear();
            //Llena la lista desde la BD
            Seccion.llenarNombreSeccionCurso(conn, listaSeccion, codigo_curso);
            //Muestra el contenido de la lista en el ComboBox
            cmbSeccion.setItems(listaSeccion);
            cmbSeccion.setDisable(false);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
    
    @FXML 
    public void mostrarClasesPeriodo(){
        if((cmbPeriodo.getValue() != null) && (cmbCurso.getValue() != null)){
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            int codigo_curso = cmbCurso.getSelectionModel().getSelectedItem().getCodigo();
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            //Limpia el ComboBox
            cmbClase.getItems().clear();
            //Llena la lista desde la BD
            Clase.llenarInformacionClasePeriodoCurso(conn, listaClase, codigo_periodo, codigo_curso);
            //Muestra el contenido de la lista en el ComboBox
            cmbClase.setItems(listaClase);
            cmbClase.setDisable(false);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
    
    @FXML
    private void mostrarAlumnos(){
        if((cmbPeriodo.getValue() != null) && (cmbCurso.getValue() != null) && (cmbSeccion.getValue() != null)){
            //Obtener el codigo de periodo, curso y seccion
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            int codigo_curso = cmbCurso.getSelectionModel().getSelectedItem().getCodigo();
            int codigo_seccion = cmbSeccion.getSelectionModel().getSelectedItem().getCodigo();
            //Limpiar la lista de Alumnos listaAsistencia
            lista.clear();
            //Limpia el ObservableListView
            listaAsistencia.clear();
            //Limpiar el TableView
            tblAsistencia.getItems().clear();
            int contador = 0;
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            //Llena la lista de Alumnos desde la BD
            Asistencia.llenarInformacionNombresAlumnosPeriodoCursoSeccion(conn, lista, codigo_periodo, codigo_curso, codigo_seccion);
            //Agrega los elementos del ArrayList al modelo del ObservableList
            for(AsistenciaArrLst aa: lista){
                contador++;
                listaAsistencia.add(new Asistencia(contador, aa.getCodigo(), aa.getNombre(), ASISTIR));
            }
            if(listaAsistencia.size() > 0){
                //Muestra el contenido de la lista en el TableView
                tblAsistencia.setItems(listaAsistencia);
                clmNo.setCellValueFactory(new PropertyValueFactory<Asistencia,Integer>("numero"));
                clmNombre.setCellValueFactory(new PropertyValueFactory<Asistencia,String>("nombre"));
                clmAsistio.setCellValueFactory(new PropertyValueFactory<Asistencia,Asistencia.Opciones>("opcion"));
                if(listaAsistencia.size() > 0){
                    btnGuardar.setDisable(false);
                    btnCancelar.setDisable(false);
                }
            }else{
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("No hay Alumnos(as)");
                mensaje.setContentText("");
                mensaje.setHeaderText("La Sección " + cmbSeccion.getValue() + " del " + cmbCurso.getValue() + " no tiene Alumnos(as) asignados.");
                mensaje.show();
            }
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
        habilitarFechaDescripcion();
    }
    
    private void habilitarFechaDescripcion(){
        dtpFecha.setValue(LocalDate.now());
        dtpFecha.setConverter(new LocalDateStringConverter(FormatStyle.FULL));
        dtpFecha.setDisable(false);
        txtDescripcion.setDisable(false);
        txtDescripcion.setText("Asistencia a " + cmbClase.getValue() + ", el " + dtpFecha.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
    }

    @FXML
    public void guardarRegistroSemestral(){
        if(listaAsistencia.size() > 0){
            Asistencia a = new Asistencia(0,
                                          txtDescripcion.getText(), 
                                          Date.valueOf(dtpFecha.getValue()), 
                                          Time.valueOf(String.valueOf(hora) + ":" + String.valueOf(minutos) + ":" + String.valueOf(segundos)), 
                                          cmbPeriodo.getValue(),
                                          cmbSemestre.getValue(),
                                          cmbParcialSemestral.getValue(), 
                                          cmbCurso.getValue(), 
                                          cmbSeccion.getValue(),
                                          cmbClase.getValue());
            conexion.establecerConexion();
            int resultado = a.guardarAsistenciaSemestral(conexion.getConnection());
            codigo_asistencia = Asistencia.codigo_generado;
            conexion.cerrarConexion();
            if(resultado == 1){
                String opcion = "SI";
                conexion.establecerConexion();
                String nombre_tabla = "tbl_asist" +                                       
                                      "_" + String.valueOf(cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo()) + 
                                      "_" + String.valueOf(cmbSemestre.getSelectionModel().getSelectedItem().getCodigo()) + 
                                      "_" + String.valueOf(cmbParcialSemestral.getSelectionModel().getSelectedItem().getCodigo()) + 
                                      "_" + String.valueOf(cmbCurso.getSelectionModel().getSelectedItem().getCodigo()) + 
                                      "_" + String.valueOf(cmbSeccion.getSelectionModel().getSelectedItem().getCodigo()) + 
                                      "_" + String.valueOf(cmbClase.getSelectionModel().getSelectedItem().getCodigo());
                String nombre_columna = "_" + String.valueOf(codigo_asistencia) + "_";
                //Se crea si no existe ta tabla para guardar los detalles de las Asistencias para la clase especificada
                Asistencia.crearTablaDetalleAsistencia(nombre_tabla, conexion.getConnection());
                if(Asistencia.tablaDetalleAsistenciaTieneCodigosAlumnos(nombre_tabla, conexion.getConnection())){
                    Asistencia.agregarNuevaColumnaTabla(nombre_tabla, nombre_columna, conexion.getConnection());
                    System.out.println("\t---Guardando <detalles de la asistencia> en la tabla " + nombre_tabla + "...");
                    for(Asistencia lista : listaAsistencia){
                        if(lista.getOpcion() == Asistencia.Opciones.SI){
                            opcion = "SI";
                        }if(lista.getOpcion() == Asistencia.Opciones.NO){
                            opcion = "NO";
                        }if(lista.getOpcion() == Asistencia.Opciones.EXCUSA){
                            opcion = "EXCUSA";
                        }
                        Asistencia.guardarDetalleAsistencia(nombre_tabla, nombre_columna, lista.getCodigo(), opcion, conexion.getConnection());
                    }
                }else{
                    System.out.println("\t---Guardando <codigoos de alumnos> en la tabla " + nombre_tabla + "...");
                    listaAsistencia.forEach((lista) -> {
                        Asistencia.guardarCodigosAlumnos(nombre_tabla, lista.getCodigo(), conexion.getConnection());
                    });
                    Asistencia.agregarNuevaColumnaTabla(nombre_tabla, nombre_columna, conexion.getConnection());
                    System.out.println("\t---Guardando <detalles de la asistencia> en la tabla " + nombre_tabla + "...");
                    for(Asistencia lista : listaAsistencia){
                        if(lista.getOpcion() == Asistencia.Opciones.SI){
                            opcion = "SI";
                        }if(lista.getOpcion() == Asistencia.Opciones.NO){
                            opcion = "NO";
                        }if(lista.getOpcion() == Asistencia.Opciones.EXCUSA){
                            opcion = "EXCUSA";
                        }
                        Asistencia.guardarDetalleAsistencia(nombre_tabla, nombre_columna, lista.getCodigo(), opcion, conexion.getConnection());
                    }
                }
                conexion.cerrarConexion();                
            }
            btnCancelar.setDisable(true);
            btnGuardar.setDisable(true);
        }else{
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Datos vacíos");
            mensaje.setContentText("");
            mensaje.setHeaderText("No hay Alumnos a quienes guardar la asistencia");
            mensaje.show();
        } 
    }
    
    private void inhabilitarLimpiarElementos(){
        cmbPeriodo.setValue(null);
        cmbPeriodo.setDisable(false);
        cmbSemestre.setValue(null);
        cmbSemestre.setDisable(true);
        cmbParcialAnual.setValue(null);
        cmbParcialAnual.setDisable(true);
        cmbParcialSemestral.setValue(null);
        cmbParcialSemestral.setDisable(true);
        cmbCurso.setValue(null);
        cmbCurso.setDisable(true);
        cmbSeccion.setValue(null);
        cmbSeccion.setDisable(true);
        cmbClase.setValue(null);
        cmbClase.setDisable(true);
        dtpFecha.setDisable(true);
        txtDescripcion.setText(null);
        txtDescripcion.setDisable(true);
        btnGuardar.setDisable(true);
        btnCancelar.setDisable(true);
        btnSalir.setDisable(false);
        btnSalir.requestFocus();
    }  
   
    @FXML
    private void cerrarFormulario(){
        // Obtener el escenario (Stage)
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        // Cerrar el formulario.
        stage.close();
    }
    
}
