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

import com.aulah.modelo.Asistencia;
import com.aulah.modelo.Clase;
import com.aulah.modelo.Conexion;
import com.aulah.modelo.Curso;
import com.aulah.modelo.ParcialAnual;
import com.aulah.modelo.ParcialSemestral;
import com.aulah.modelo.Periodo;
import com.aulah.modelo.Seccion;
import com.aulah.modelo.Semestre;
import com.aulah.modelo.VerEvaluacion;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author hector
 */
public class VerEvaluacion_ctrl implements Initializable {
    
    private boolean VER = false;
    private boolean MODIFICAR = false;
    private boolean ELIMINAR = false;
    
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
    @FXML private TextArea txtEvaluacion;
    @FXML private TableView<VerEvaluacion> tblEvaluacion;
    @FXML private TableColumn<VerEvaluacion,Integer> clmNo;
    @FXML private TableColumn<VerEvaluacion,String> clmEvaluacion;
    @FXML private TableColumn<VerEvaluacion,Date> clmFecha;
    
    //Botones del formulario
    @FXML private Button btnVer;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
   
    //Listas para los ComboBox
    private ObservableList<Periodo> listaPeriodo = FXCollections.observableArrayList();
    private ObservableList<Semestre> listaSemestre = FXCollections.observableArrayList();
    private ObservableList<ParcialAnual> listaParcialAnual = FXCollections.observableArrayList();
    private ObservableList<ParcialSemestral> listaParcialSemestral = FXCollections.observableArrayList();
    private ObservableList<Curso> listaCurso = FXCollections.observableArrayList();
    private ObservableList<Seccion> listaSeccion = FXCollections.observableArrayList();
    private ObservableList<Clase> listaClase = FXCollections.observableArrayList();
    //Lista para el TableView de Asistencias
    private ObservableList<VerEvaluacion> listaAsistencia = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  
    
    public void inicializarDatos(boolean ver, boolean modificar, boolean eliminar){
        VER = ver;
        MODIFICAR = modificar;
        ELIMINAR = eliminar;
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Llenar las listas desde la BD
        Periodo.llenarNombrePeriodos(conn, listaPeriodo);
        System.out.println("\t...mostrando Periodos " + listaPeriodo.size());
        //Establecer las columnas a mostrar en el TableView
        clmNo.setCellValueFactory(new PropertyValueFactory<VerEvaluacion,Integer>("numero"));
        //clmCodigo.setCellValueFactory(new PropertyValueFactory<VerEvaluacion,Integer>("codigo"));
        clmEvaluacion.setCellValueFactory(new PropertyValueFactory<VerEvaluacion,String>("evaluacion"));
        clmFecha.setCellValueFactory(new PropertyValueFactory<VerEvaluacion,Date>("fecha"));
        //Asigna los items de los ComboBox
        cmbPeriodo.setItems(listaPeriodo);
        inhabilitarLimpiarElementos();
        //Configura el TextArea
        txtEvaluacion.setWrapText(true);
        if(VER){
            btnVer.setText("Ver");
        }
        if(MODIFICAR){
            btnVer.setText("Modificar");
        }
        if(ELIMINAR){
            btnVer.setText("Eliminar");
        }
        //Gestiona los eventos del TableView
        gestionarEventos();
        //Cerrar la conexion a la BD
        conexion.cerrarConexion();
    }
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item de la tabla
        tblEvaluacion.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<VerEvaluacion>(){
                @Override
                public void changed(ObservableValue<? extends VerEvaluacion> ov, 
                    VerEvaluacion valorAnterior, VerEvaluacion valorSeleccionado) {
                    if(valorSeleccionado != null){
                        txtEvaluacion.setDisable(false);
                        txtEvaluacion.setText(valorSeleccionado.getEvaluacion() + ", el día " + valorSeleccionado.getFecha());
                        btnVer.setDisable(false);
                        btnCancelar.setDisable(false);
                    }
                }  
            }
        );
    }
    
    @FXML
    private void mostrarSemestresOParciales(){
        if(cmbPeriodo.getValue() != null){
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            if(Periodo.periodoEsSemestral(conn, codigo_periodo)){
                mostrarSemestresPeriodo(conn);
                System.out.println("\t...mostrando Semestres");
                cmbParcialSemestral.setDisable(true);
                cmbSemestre.setDisable(false);
                cmbParcialSemestral.setVisible(true);
                cmbParcialAnual.setVisible(false);
                cmbParcialAnual.setDisable(true);
            }
            if(Periodo.periodoEsAnual(conn, codigo_periodo)){
                mostrarParcialesAnuales(conn);
                System.out.println("\t...mostrando Parciales Anuales");
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
            System.out.println("\t\tMostrando Semestres " + listaSemestre.size());
            //Muestra el contenido de la lista en el ComboBox
            cmbSemestre.setItems(listaSemestre);
    }
    
    public void mostrarParcialesAnuales(Connection con){
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            //Limpia el ComboBox
            cmbParcialAnual.getItems().clear();
            //Llena la lista desde la BD
            ParcialAnual.llenarInformacionParcialAnualPeriodo(con, listaParcialAnual, codigo_periodo);
            System.out.println("\t\tMostrando Parciales Anuales " + listaParcialAnual.size());
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
            System.out.println("\t\tMostrando Parciales Semestrales  " + listaParcialSemestral.size());
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
            System.out.println("\t\tMostrando Cursos  " + listaCurso.size());
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
            //Limpiar el TexArea
            txtEvaluacion.clear();
            //Llena la lista desde la BD
            Seccion.llenarNombreSeccionCurso(conn, listaSeccion, codigo_curso);
            System.out.println("\t\tMostrando Secciones  " + listaSeccion.size());
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
            System.out.println("\t\tMostrando Clases  " + listaClase.size());
            //Muestra el contenido de la lista en el ComboBox
            cmbClase.setItems(listaClase);
            cmbClase.setDisable(false);
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }
    }
    
    @FXML 
    public void mostrarEvaluacionSemestral(){
        if((cmbCurso.getValue() != null) && (cmbSeccion.getValue() != null) && (cmbSeccion.getValue() != null)){
            int codigo_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
            int codigo_semestre = cmbSemestre.getSelectionModel().getSelectedItem().getCodigo();
            int codigo_parcial_semestral = cmbParcialSemestral.getSelectionModel().getSelectedItem().getCodigo();
            int codigo_curso = cmbCurso.getSelectionModel().getSelectedItem().getCodigo();
            int codigo_seccion = cmbSeccion.getSelectionModel().getSelectedItem().getCodigo();
            int codigo_clase = cmbClase.getSelectionModel().getSelectedItem().getCodigo();
            //Conecta a la BD
            Connection conn = conexion.establecerConexion();
            //Limpia el ObservableList
            listaAsistencia.clear();
            //Limpia el TableView
            tblEvaluacion.getItems().clear();
            //Limpiar el TextArea
            txtEvaluacion.clear();
            //Llena la lista desde la BD
            VerEvaluacion.llenarInformacionEvaluacionesSemestrales(conn, listaAsistencia, codigo_periodo, codigo_semestre, codigo_parcial_semestral, codigo_curso, codigo_seccion, codigo_clase);
            System.out.println("\tMostrando Evaluacion para: Periodo = " + codigo_periodo + "   Semestre = "  + codigo_semestre + "   Parcial semestral = " + codigo_parcial_semestral + "   Curso = " +  codigo_curso + "   Seccion = " + codigo_seccion + "   Clase =" + codigo_clase);
            if(listaAsistencia.size() > 0){
                //Muestra el contenido de la lista en el TableView
                tblEvaluacion.setItems(listaAsistencia);            
            }else{
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("No hay Evaluaciones");
                mensaje.setContentText("");
                mensaje.setHeaderText("No hay Evaluaciones para la Clase " + cmbClase.getValue() + " del Curso " + cmbCurso.getValue() + ", Sección " + cmbSeccion.getValue() + " en el " + cmbParcialSemestral.getValue() + ".");
                mensaje.show();
            }
            //Cerrar la conexion a la BD
            conexion.cerrarConexion();
        }  
    }

    @FXML  
    public void verDetalleEvaluacion() {
        int codigo_evaluacion = tblEvaluacion.getSelectionModel().getSelectedItem().getCodigo();
        int cod_periodo = cmbPeriodo.getSelectionModel().getSelectedItem().getCodigo();
        int cod_semestre = cmbSemestre.getSelectionModel().getSelectedItem().getCodigo();
        int cod_parcial = cmbParcialSemestral.getSelectionModel().getSelectedItem().getCodigo();
        int cod_curso = cmbCurso.getSelectionModel().getSelectedItem().getCodigo();
        int cod_seccion = cmbSeccion.getSelectionModel().getSelectedItem().getCodigo();
        int cod_clase = cmbClase.getSelectionModel().getSelectedItem().getCodigo();
        Clase clase = cmbClase.getValue();
        Date fecha = tblEvaluacion.getSelectionModel().getSelectedItem().getFecha();
        try {
            if(VER || MODIFICAR){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aulah/vista/VerDetalleEvaluacion.fxml"));
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setScene( new Scene((Pane) loader.load()));
                VerDetalleEvaluacion_ctrl controlador =  loader.<VerDetalleEvaluacion_ctrl>getController();
                controlador.inicializarDatos(VER, MODIFICAR, ELIMINAR, clase, fecha, codigo_evaluacion, cod_periodo, cod_semestre, cod_parcial, cod_curso, cod_seccion, cod_clase);
                if(VER){
                    stage.setTitle("Ver Asistencia");
                }
                if(MODIFICAR){
                    stage.setTitle("Modificar Asistencias");                    
                }
                stage.initModality(Modality.APPLICATION_MODAL);
                System.out.println("Mostrando formulario...");
                stage.show();
            }
            if(ELIMINAR){
                Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
                confirmar.setTitle("Eliminar Evaluacion");
                btnVer.setText("Eliminar");
                confirmar.setHeaderText("¿Está seguro que desea eliminar la \n" + tblEvaluacion.getSelectionModel().getSelectedItem().getEvaluacion() + "?");
                confirmar.setContentText("");
                Optional<ButtonType> result = confirmar.showAndWait();
                if(result.get() == ButtonType.OK){
                    System.out.println("Eliminar Evaluacion codigo: " + codigo_evaluacion);
                    int resultado = eliminarAsistencia(codigo_evaluacion);
                    if(resultado == 1){
                        String nombre_tabla = "tbl_eval" + 
                                      "_" + String.valueOf(cod_periodo) + 
                                      "_" + String.valueOf(cod_semestre) + 
                                      "_" + String.valueOf(cod_parcial) + 
                                      "_" + String.valueOf(cod_curso) + 
                                      "_" + String.valueOf(cod_seccion) + 
                                      "_" + String.valueOf(cod_clase);
                        String nombre_columna = "_" + String.valueOf(codigo_evaluacion) + "_";
                        eliminarColumnaTabla(nombre_tabla, nombre_columna);
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Error al intentar mostrar la ventana : VerDetalleEvaluacion.fxml\n" + ex.getMessage());
        }
    }

    private int eliminarAsistencia(int codigo_evaluacion){
        Asistencia as = new Asistencia(codigo_evaluacion);
        conexion.establecerConexion();
        int resultado = as.eliminarAsistencia(conexion.getConnection());
        conexion.cerrarConexion();
        if(resultado == 1){
            listaAsistencia.remove(tblEvaluacion.getSelectionModel().getSelectedIndex());
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro eliminado");
            mensaje.setContentText("");
            mensaje.setHeaderText("La Asistencia ha sido eliminada exitosamente");
            mensaje.show();
            inhabilitarLimpiarElementos();
        }
        return resultado;
    }
    
    private void eliminarColumnaTabla(String nombre_tabla, String nombre_columna){
        conexion.establecerConexion();
        VerEvaluacion.eliminarColumnaTabla(conexion.getConnection(), nombre_tabla, nombre_columna);
        conexion.cerrarConexion();
    }
    
    private void modificarAsistencia(){
        
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
        txtEvaluacion.setText(null);
        txtEvaluacion.setDisable(true);
        btnVer.setDisable(true);
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
