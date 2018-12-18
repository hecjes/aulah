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

import com.aulah.modelo.Clase;
import com.aulah.modelo.Conexion;
import com.aulah.modelo.CriterioEvaluacion;
import com.aulah.modelo.Curso;
import com.aulah.modelo.Evaluacion;
import com.aulah.modelo.Nivel;
import com.aulah.modelo.ParcialAnual;
import com.aulah.modelo.ParcialSemestral;
import com.aulah.modelo.Periodo;
import com.aulah.modelo.Seccion;
import com.aulah.modelo.Semestre;
import com.aulah.utilidades.EvaluacionArrLst;
import com.aulah.utilidades.Comunes;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

/**
 * FXML Controller class
 *
 * @author hector
 */
public class Evaluacion_ctrl implements Initializable {

    int codigo_evaluacion = 0;//codigo: Variable para guardar el Codigo de la Evaluacion para usarlo cuando
                   //se Actualiza o Elimina un registro ya que el Codigo no se muestra en ningun elemento del GUI.
    
    boolean registro_recien_guardado = false;//Se usa para saber si se ha guardado un registro antes de mandar
                                             //a modificar el mismo registro.
    boolean nuevo_registro = false;//Se usa para saber si se va a ingresar datos en el formulario
                                   //para un nuevo registro que luego se piensa guardar.
    int VALOR = 0; //para conocer el Valor asignado al Criterio.
    int VALOR_CRITERIO = 0; //para conocer desde la BD el valor del Criterio Elegido.
    int SUMA_CRITERIO = 0; //para conocer desde la BD la suma de valores del Criterio.
    boolean CRITERIO_PUNTOS_EXTRA = false;//Para saber si se esta trabajando con Criterios Puntos Extras
    boolean CRITERIO_NORMALES = false;//Para saber si se esta trabajando con Criterios Normales
        
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
    @FXML private ComboBox<CriterioEvaluacion> cmbCriterio;
    @FXML private DatePicker dtpFecha;
    @FXML private ComboBox<String> cmbValor;
    @FXML private TextArea txtDescripcion;
    @FXML private TableView<Evaluacion> tblEvaluacion;
    
    //Define las columnas de la tabla
    @FXML private TableColumn<Evaluacion,Integer> clmNo;
    @FXML private TableColumn<Evaluacion,String> clmNombre;
    @FXML private TableColumn<Evaluacion,String> clmValor;
    
    //Botones del formulario
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //ArrayList para llenar el ObservableList
    private ArrayList<EvaluacionArrLst> lista_alumnos = new ArrayList<EvaluacionArrLst>();
    //Listas para los ComboBox
    private ObservableList<Periodo> listaPeriodo = FXCollections.observableArrayList();
    private ObservableList<Semestre> listaSemestre = FXCollections.observableArrayList();
    private ObservableList<ParcialAnual> listaParcialAnual = FXCollections.observableArrayList();
    private ObservableList<ParcialSemestral> listaParcialSemestral = FXCollections.observableArrayList();
    private ObservableList<Curso> listaCurso = FXCollections.observableArrayList();
    private ObservableList<Seccion> listaSeccion = FXCollections.observableArrayList();
    private ObservableList<Clase> listaClase = FXCollections.observableArrayList();
    private ObservableList<CriterioEvaluacion> lista_criterio_normales = FXCollections.observableArrayList();
    private ObservableList<CriterioEvaluacion> lista_criterio_puntos_extra = FXCollections.observableArrayList();
    private ObservableList<String> lista_valor = FXCollections.observableArrayList(
    "5","10","15","20","25","30","35","40");
    //Lista para la tabla
    private ObservableList<Evaluacion> listaEvaluacion = FXCollections.observableArrayList();
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Llenar las listas desde la BD
        Periodo.llenarNombrePeriodos(conn, listaPeriodo);
        System.out.println("\t\t...mostrando Periodos " + listaPeriodo.size());
        //Hace editable el TableView
        tblEvaluacion.setEditable(true);
        //Asigna los items de los ComboBox
        cmbPeriodo.setItems(listaPeriodo);
        //Agregar valores al ComboBox cmbValor
        cmbValor.setItems(lista_valor);
        cmbValor.setEditable(true);
        //Hace editable la columna cmlValor
        clmValor.setCellValueFactory(new PropertyValueFactory<Evaluacion, String>("valor"));
        clmValor.setCellFactory(TextFieldTableCell.forTableColumn());
        //Configura el TextArea
        txtDescripcion.setWrapText(true);
        //Gestiona los eventos del formulario
        gestionarEventos();
        //Inhabilitar elementos
        inhabilitarLimpiarElementos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }   
    
    private void gestionarEventos(){
        //Ocurre cada vez que se escribe en el txtDescripcion
        txtDescripcion.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(txtDescripcion.getText() != null){
                    if(txtDescripcion.isDisable() == false){
                        if(txtDescripcion.getText().length() > 0){
                            btnGuardar.setDisable(false);
                        }else{
                            btnGuardar.setDisable(true);
                        }
                    }
                }
            }
        });
        //Ocurre cada vez que se quita el foco en el ComboBox cmbValor
        cmbValor.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if(ov.getValue() == true){
                    System.out.println("\n...se obtuvo el foco en cmbValor.");
                }else{
                    System.out.println("\n...se pierde el foco en cmbValor.");                    
                }
            }
        });
        //Ocurre cada vez que se edita el contenido en el ComboBox cmbValor
        cmbValor.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtDescripcion.setDisable(false);
                if(SUMA_CRITERIO < VALOR_CRITERIO){                        
                    System.out.println("\n...se pueden agregar puntos al Criterio");
                    if(!newValue.equals("")){
                        if((SUMA_CRITERIO + Integer.parseInt(newValue)) <= VALOR_CRITERIO){
                            System.out.println("\n...el Valor agregado se puede guardar para el Criterio");
                        }else{
                            System.out.println("El Valor que se intenta guardar hace que la suma sobrepase al valor establecido para el Criterio");
                            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                            mensaje.setTitle("Valor de Criterio fuera de rango");
                            mensaje.setContentText("Ingrese otro valor.");
                            mensaje.setHeaderText("El Valor ingresado de " + newValue + " puntos, hace que se sobrepase el Valor máximo de " + VALOR_CRITERIO + " puntos establecido para el Criterio " + cmbCriterio.getValue() + ".\nSolamente puede agregar un Valor de " + (VALOR_CRITERIO - SUMA_CRITERIO) + " puntos.");
                            mensaje.show();
                        } 
                    }
                }
            }
        });
        //Ocurre cada vez que se edita el contenido en la columna clmValor
        clmValor.setOnEditCommit(data -> {            
            Evaluacion p = data.getRowValue();
            p.setValor(data.getNewValue());
            System.out.println("\t...se editó a " + p + "\tNuevo Valor: " +  data.getNewValue());            
        });
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
            System.out.println("\t\t...mostrando Semestres " + listaSemestre.size());
            //Muestra el contenido de la lista en el ComboBox
            cmbSemestre.setItems(listaSemestre);
    }
    
    public void mostrarParcialesAnuales(Connection con){
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            //Limpia el ComboBox
            cmbParcialAnual.getItems().clear();
            //Llena la lista desde la BD
            ParcialAnual.llenarInformacionParcialAnualPeriodo(con, listaParcialAnual, codigo_periodo);
            System.out.println("\t\t...mostrando Parciales Anuales " + listaParcialAnual.size());
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
            System.out.println("\t\t...mostrando Parciales Semestrales  " + listaParcialSemestral.size());
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
            System.out.println("\t\t...mostrando Cursos  " + listaCurso.size());
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
            tblEvaluacion.getItems().clear();
            //Limpia el ComboBox
            cmbSeccion.getItems().clear();
            //Llena la lista desde la BD
            Seccion.llenarNombreSeccionCurso(conn, listaSeccion, codigo_curso);
            System.out.println("\t\t...mostrando Secciones  " + listaSeccion.size());
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
            System.out.println("\t\t...mostrando Clases  " + listaClase.size());
            //Muestra el contenido de la lista en el ComboBox
            cmbClase.setItems(listaClase);
            cmbClase.setDisable(false);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
    
    @FXML
    private void mostrarAlumnosCriterios(){        
        int cod_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
        int cod_semestre = cmbSemestre.getSelectionModel().getSelectedItem().getCodigo();
        int cod_parcial_semestral = cmbParcialSemestral.getSelectionModel().getSelectedItem().getCodigo();
        int cod_curso = cmbCurso.getSelectionModel().getSelectedItem().getCodigo();
        int cod_seccion = cmbSeccion.getSelectionModel().getSelectedItem().getCodigo();
        int cod_clase = cmbClase.getSelectionModel().getSelectedItem().getCodigo();
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        int suma_valor = Evaluacion.obtenerSumaValorTodosCriterios(cod_periodo, cod_semestre, cod_parcial_semestral, cod_curso, cod_seccion, cod_clase, conn);
        System.out.println("\t...obteniendo la suma total de los valores para los Criterios de Evaluacion para la clase: " + cmbClase.getValue() + "   Curso: " + cmbCurso.getValue() + "  Seccion: " + cmbSeccion.getValue());
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
        if(suma_valor < 100){
            mostrarAlumnos();
            if(lista_alumnos.size() > 0){
                mostrarCriteriosNormales();
            }
        }else{
            System.out.println("La evaluación de la clase " + cmbClase.getValue() + " para este Parcial ya está completa( ya suma 100 puntos)");
            Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
            confirmar.setTitle("Evaluación completada");
            confirmar.setContentText("");
            confirmar.setHeaderText("La Evaluación para la clase " + cmbClase.getValue() + ", del Curso <" + cmbCurso.getValue() + ">, para la Sección <" + cmbSeccion.getValue() + ">\n ya está completada (suma 100 puntos).\n\n\t¿Desea agregar Puntos Extras (sobre 100) para esta Clase " + cmbClase.getValue() + "?");
            Optional<ButtonType> result = confirmar.showAndWait();
            if(result.get() == ButtonType.OK){
                System.out.println("\t...agregar Puntos Extras");
                mostrarAlumnos();
                if(lista_alumnos.size() > 0){
                    int cod_nivel = cmbClase.getSelectionModel().getSelectedItem().getCod_nivel().getCodigo();
                    if(criterioEvaluacionPuntosExtraExiste(cod_nivel)){
                        System.out.println("\t...el criterio Puntos Extras ya existe");
                        int cod_criterio = obtenerCodigoCriterioPuntosExtras(cod_nivel);
                        mostrarCriterioPuntosExtras(cod_criterio);
                    }else{
                        System.out.println("\t...el criterio Puntos Extras NO existe, ...se creará");
                        int cod_criterio_creado = crearCriterioEvaluacionPuntosExtra();
                        if(cod_criterio_creado > 0){
                            mostrarCriterioPuntosExtras(cod_criterio_creado);
                        }else{
                            System.out.println("\t...No se creó el criterio para Puntos Extras");
                        }
                    }
                }
            }
        }        
    }
    
    private int obtenerCodigoCriterioPuntosExtras(int cod_nivel){
        int cod_criterio = 0;
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        cod_criterio = CriterioEvaluacion.obtenerCodigoCriterioPuntosExtras(cod_nivel, conn);
        System.out.println("\t...se obtuvo el codigo del criterio de evaluacion " + cod_criterio);
        //Cierra conexion a la BD
        conexion.cerrarConexion();
        return cod_criterio;
    }
    
    private boolean criterioEvaluacionPuntosExtraExiste(int cod_nivel){
        boolean criterio_existe = false;
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        criterio_existe = CriterioEvaluacion.criterioEvaluacioPuntosExtraExiste(cod_nivel, conn);
        if(criterio_existe){
            System.out.println("\t...el criterio de evaluacion para Puntos Extras ya existe");
        }
        conexion.cerrarConexion();
        return criterio_existe;
    }
    
    private int crearCriterioEvaluacionPuntosExtra(){
        int cod_nivel = cmbClase.getSelectionModel().getSelectedItem().getCod_nivel().getCodigo();
        conexion.establecerConexion();
        int cod_criterio_generado = CriterioEvaluacion.crearCriterioPuntosExtras(cod_nivel, conexion.getConnection());
        System.out.println("\t\t...creando criterio de evaluacion para Puntos Extras");
        conexion.cerrarConexion();
        return cod_criterio_generado;
    }
    
    private void mostrarAlumnos(){
        if((cmbPeriodo.getValue() != null) && (cmbCurso.getValue() != null) && (cmbSeccion.getValue() != null)){
            //Obtener el codigo de periodo, curso y seccion
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            int codigo_curso = cmbCurso.getSelectionModel().getSelectedItem().getCodigo();
            int codigo_seccion = cmbSeccion.getSelectionModel().getSelectedItem().getCodigo();
            //Limpiar la lista de Alumnos listaAsistencia
            lista_alumnos.clear();
            //Limpia el ObservableListView
            listaEvaluacion.clear();
            //Limpiar el TableView
            tblEvaluacion.getItems().clear();
            int contador = 0;
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            //Comprobacion
            System.out.println("\t...se va a mostrar Alumnos para: Periodo = " + codigo_periodo + "   Curso = " +  codigo_curso + "   Seccion = " + codigo_seccion);
            //Llena la lista de Alumnos desde la BD
            Evaluacion.llenarInformacionNombresAlumnosPeriodoCursoSeccion(conn, lista_alumnos, codigo_periodo, codigo_curso, codigo_seccion);
            //Agrega los elementos del ArrayList al modelo del ObservableList
            for(EvaluacionArrLst aa: lista_alumnos){
                contador++;
                listaEvaluacion.add(new Evaluacion(contador, aa.getCodigo(), aa.getNombre(), String.valueOf(VALOR)));
            }
            if(listaEvaluacion.size() > 0){
                //Muestra el contenido de la lista en el TableView
                tblEvaluacion.setItems(listaEvaluacion);
                System.out.println("\t...mostrando lista de Alumnos " + listaEvaluacion.size());
                clmNo.setCellValueFactory(new PropertyValueFactory<Evaluacion,Integer>("numero"));
                clmNombre.setCellValueFactory(new PropertyValueFactory<Evaluacion,String>("nombre"));
                clmValor.setCellValueFactory(new PropertyValueFactory<Evaluacion,String>("valor"));
                clmValor.setEditable(true);
            }else{
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Datos vacios");
                mensaje.setContentText("");
                mensaje.setHeaderText("No se obtuvo datos de Alumnos para Sección <" + cmbSeccion.getValue() + "> del <" + cmbCurso.getValue() + ">.");
                mensaje.show();
            }
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
    
    public void mostrarCriteriosNormales(){
        if((cmbClase.getValue() != null)){
            int cod_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            int cod_nivel = Nivel.obtenerCodigoNivelPeriodo(conn, cod_periodo);
            //Limpia el ComboBox
            cmbCriterio.getItems().clear();
            //Llena la lista desde la BD
            CriterioEvaluacion.llenarInformacionCriterioEvaluacionNivel(conn, lista_criterio_normales, cod_nivel);
            System.out.println("\t\t...mostrando criterios de Evaluacion normales " + lista_criterio_normales.size());
            //Muestra el contenido de la lista en el ComboBox
            cmbCriterio.setItems(lista_criterio_normales);
            if(lista_criterio_normales.size() > 0){
                CRITERIO_NORMALES = true;
            }
            cmbCriterio.setDisable(false);           
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
            }
    }
    
    public void mostrarCriterioPuntosExtras(int cod_criterio){
        if((cmbClase.getValue() != null)){
            //Conecta a la BD
            Connection conn = conexion.establecerConexion(); 
            //Limpia el ComboBox
            cmbCriterio.getItems().clear();
            //Llena la lista desde el Modelo
            CriterioEvaluacion.obtenerCriterioEvaluacionPuntosExtra(cod_criterio, conn, lista_criterio_puntos_extra);
            System.out.println("\t\t...mostrando criterios de evaluacion para Puntos Extras " + lista_criterio_puntos_extra.size());
            //Muestra el contenido de la lista en el ComboBox            
            cmbCriterio.setItems(lista_criterio_puntos_extra);
            if(lista_criterio_puntos_extra.size() > 0){
                CRITERIO_PUNTOS_EXTRA = true;
            }
            cmbCriterio.setDisable(false);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
    
    @FXML
    public void obtenerValorCriterio(){
        int cod_clase = cmbClase.getSelectionModel().getSelectedItem().getCodigo();
        int cod_criterio = cmbCriterio.getSelectionModel().getSelectedItem().getCodigo();        
        System.out.println("\n...el cod_criterio es: " + cod_criterio);
        if(CRITERIO_NORMALES){
            //Conecta a la BD
            Connection conn = conexion.establecerConexion(); 
            VALOR_CRITERIO = Evaluacion.obtenerValorCriterioNormal(cod_criterio, conn);
            System.out.println("\t...el valor desde la BD para el Criterio Normal elegido es: " + VALOR_CRITERIO);
            SUMA_CRITERIO = Evaluacion.obtenerSumaCriterioNormal(cod_clase, cod_criterio, conn);
            System.out.println("\t...la suma del valor de los Criterios Normales ya guardados es: " + SUMA_CRITERIO);
            conexion.cerrarConexion(); 
        }
        if(CRITERIO_PUNTOS_EXTRA){            
            VALOR_CRITERIO = 0;
            SUMA_CRITERIO = 0;
            System.out.println("\t...se esta utilizando criterio de evaluacion Puntos Extras");
        }
        if(VALOR_CRITERIO > 0){
            if(VALOR_CRITERIO == SUMA_CRITERIO){
                System.out.println("El Criterio ya está evaluado, no se pueden sumar mas valores");
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Criterio de Evaluacion lleno");
                mensaje.setContentText("Puede probar con otro Criterio de Evaluación");
                mensaje.setHeaderText("El Criterio " + cmbCriterio.getValue() + " para la Clase " + cmbClase.getValue() + " ya está completo con " + VALOR_CRITERIO + " puntos, no se puede agregar mas puntos.");
                mensaje.show();
            }else{
                dtpFecha.setValue(LocalDate.now());
                dtpFecha.setConverter(new LocalDateStringConverter(FormatStyle.FULL));
                dtpFecha.setDisable(false);
                cmbValor.setDisable(false);            
            }
        }else{
            dtpFecha.setValue(LocalDate.now());
            dtpFecha.setConverter(new LocalDateStringConverter(FormatStyle.FULL));
            dtpFecha.setDisable(false);
            cmbValor.setDisable(false);     
        }
    }
    
    @FXML
    public void modificarValor(){
        listaEvaluacion.clear();
        int contador = 0;
        VALOR = Integer.parseInt(cmbValor.getValue());
        for(EvaluacionArrLst aa: lista_alumnos){
                contador++;
                listaEvaluacion.add(new Evaluacion(contador, aa.getCodigo(), aa.getNombre(), String.valueOf(VALOR)));
        }
        habilitarDescripcion();
    }
    
    public void habilitarDescripcion(){
        txtDescripcion.setDisable(false);
        txtDescripcion.setText(null);
    }

    @FXML
    public void guardarRegistroSemestral(){
        int contador_numeros = 0;
        int contador_vacios = 0;
        String estado = "";
        boolean valores_en_rango = true; //Para saber si se ha ingresado un balor mayor que el Valor base establecido
        boolean valores_correctos = false;//Para saber si los valores contienen numeros y vacios solamente
        //Validar si hay datos en la tabla
        if(listaEvaluacion.size() >= 0){
            //Validar si hay valores vacios y si los valores son numéricos
            for(Evaluacion lista : listaEvaluacion){   
                if(Comunes.esNumero(lista.getValor())){
                    contador_numeros++;
                    if(Integer.parseInt(lista.getValor()) > Integer.parseInt(cmbValor.getValue())){
                        valores_en_rango = false;
                    }
                }else{
                    System.out.println(lista.getNumero() + " -- su valor \"" + lista.getValor() + "\" ---------> NO es un numeros");
                    if(lista.getValor().equals("")){
                        contador_vacios++;
                    }
                }
            }
            if(valores_en_rango){
                if((contador_numeros + contador_vacios) == listaEvaluacion.size()){
                    valores_correctos = true;
                }
                if(contador_vacios > 0){
                    estado = "PENDIENTE";
                }else{
                    estado = "TERMINADO";
                }
                if(valores_correctos){
                    System.out.println("\t...los valores de la Evaluacion estan bien, la Evaluacion se puede guradar :-)");
                    Evaluacion ev = new Evaluacion(0,
                                              txtDescripcion.getText(), 
                                              Date.valueOf(dtpFecha.getValue()),
                                              cmbValor.getValue(),
                                              estado,
                                              cmbCriterio.getValue(),
                                              cmbPeriodo.getValue(),
                                              cmbSemestre.getValue(),
                                              cmbParcialSemestral.getValue(), 
                                              cmbCurso.getValue(), 
                                              cmbSeccion.getValue(),
                                              cmbClase.getValue());
                    conexion.establecerConexion();
                    //Se guardan los datos básicos de la Evaluacion
                    int resultado = ev.guardarEvaluacionSemestral(conexion.getConnection());
                    System.out.println("\t...se guarda la Evaluacion semestral");
                    codigo_evaluacion = Evaluacion.codigo_generado;
                    conexion.cerrarConexion();
                    if(resultado == 1){
                        conexion.establecerConexion();
                        //Se crea un nombre para la tabla en donde se van a guardar los detalles de la Evaluacion
                        String nombre_tabla = "tbl_eval" +                                       
                                              "_" + String.valueOf(cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo()) + 
                                              "_" + String.valueOf(cmbSemestre.getSelectionModel().getSelectedItem().getCodigo()) + 
                                              "_" + String.valueOf(cmbParcialSemestral.getSelectionModel().getSelectedItem().getCodigo()) + 
                                              "_" + String.valueOf(cmbCurso.getSelectionModel().getSelectedItem().getCodigo()) + 
                                              "_" + String.valueOf(cmbSeccion.getSelectionModel().getSelectedItem().getCodigo()) + 
                                              "_" + String.valueOf(cmbClase.getSelectionModel().getSelectedItem().getCodigo());
                        String nombre_columna = "_" + String.valueOf(codigo_evaluacion) + "_";
                        //Se crea si no existe ta tabla para guardar los detalles de las Evaluaciones para la clase especificada
                        Evaluacion.crearTablaDetalleEvaluacion(nombre_tabla, conexion.getConnection());
                        //Se verifica si la tabla de detalle contiene la columna con los códigos de los Alumnos
                        if(Evaluacion.tablaDetalleEvaluacionTieneCodigosAlumnos(nombre_tabla, conexion.getConnection())){
                            //Se agraga una nueva columna a la tabla para registrar ahi los detalles de la Evaluacion
                            Evaluacion.agregarNuevaColumnaTabla(nombre_tabla, nombre_columna, conexion.getConnection());
                            System.out.println("\t...guardando detalles de la Asistencia en la tabla " + nombre_tabla + "... se guardan " + listaEvaluacion.size() + " registros.");
                            for(Evaluacion lista : listaEvaluacion){                        
                                Evaluacion.guardarDetalleEvaluacion(nombre_tabla, nombre_columna, lista.getCodigo(), lista.getValor(), conexion.getConnection());
                            }
                        }else{//Sino existe la columna <codigo_alumno> se crea en la tabla y se guardan los codigos de los Alumnos
                            System.out.println("\t...guardando codigos de Alumnos para la Asistencia en la tabla " + nombre_tabla);
                            listaEvaluacion.forEach((lista) -> {
                                Evaluacion.guardarCodigosAlumnos(nombre_tabla, lista.getCodigo(), conexion.getConnection());
                            });
                            Evaluacion.agregarNuevaColumnaTabla(nombre_tabla, nombre_columna, conexion.getConnection());
                            System.out.println("\t...guardando detalles de la Asistencia en la tabla " + nombre_tabla + "...se guardan " + listaEvaluacion.size() + " registros.");
                            for(Evaluacion lista : listaEvaluacion){                        
                                Evaluacion.guardarDetalleEvaluacion(nombre_tabla, nombre_columna, lista.getCodigo(), lista.getValor(), conexion.getConnection());
                            }
                        }
                        conexion.cerrarConexion();              
                    }
                    btnCancelar.setDisable(true);
                    btnGuardar.setDisable(true);
                }else{
                    Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                    mensaje.setTitle("Valores no numéricos");
                    mensaje.setContentText("No se puede guardar la Evaluación, verifique los valores ingresados.");
                    mensaje.setHeaderText("Se han encontrado Valores que no son números.\nAsegúrese de no escribir <espacios> o <letras> en el Valor.\nSi el Alumno(a) no tiene nota, deje el Valor totalmente <vacío>.");
                    mensaje.show();
                }
            }else{
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Valor fuera de rango");
                mensaje.setContentText("No se puede guardar la Evaluación, ingrese un valor menor que <" + cmbValor.getValue() + ">.");
                mensaje.setHeaderText("Se ha ingresado un Valor mayor que " + cmbValor.getValue() + " que el Valor base establecido.\nAsegúrese de ingresar valores menores o iguales que " + cmbValor.getValue() + ".");
                mensaje.show();
            }
        }else{
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Datos vacíos");
            mensaje.setContentText("");
            mensaje.setHeaderText("No hay Alumnos a quienes guardar la Evaluacion");
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
        cmbCriterio.setValue(null);
        cmbCriterio.setDisable(true);
        cmbValor.setValue(null);
        cmbValor.setDisable(true);
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
