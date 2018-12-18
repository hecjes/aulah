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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Hector Armando Herrera
 Principal_ctrl es la clase principla donde se muestran todas las opciones de la
 aplicación AulaH y desde esta clase se muestran los demás formularios que forman el programa.
 */
public class Principal_ctrl implements Initializable {
    
    private boolean VER = false;
    private boolean MODIFICAR = false;
    private boolean ELIMINAR = false;
    
    @FXML private Label lblTitulo;
    @FXML private TabPane tpnAulaH;
    @FXML private Label lblMensaje;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Pone un título a la ventana Principal
        lblTitulo.setText("Aula H");
        //Prueba si la BD está funcionando
        //Si hay conexion a la BD habilita, sino deshabilita el TabPane que muestra las opciones.
        if(probarConexionBD()){
            tpnAulaH.setDisable(false);
            lblMensaje.setText("...");
        }else{ 
            tpnAulaH.setDisable(true);
            lblMensaje.setText("Aviso: ¡El sistema está sin conexión con la Base de Datos!");
        }
    }
    
    
    //******************************* Pestaña ASISTENCIA *************************
    @FXML
    private void mostrarNuevaAsistencia(ActionEvent event) {
        String formulario = "/com/aulah/vista/Asistencia.fxml";
        try {                
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Nueva Asistencia");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
                System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }  
    }
    
    @FXML
    private void mostrarVerAsistencia(ActionEvent event) {
        String formulario = "/com/aulah/vista/VerAsistencia.fxml";
        try {
            VER = true;
            MODIFICAR = false;
            ELIMINAR = false;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formulario));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene( new Scene((Pane) loader.load()));
            VerAsistencia_ctrl controlador =  loader.<VerAsistencia_ctrl>getController();
            controlador.inicializarDatos(VER, MODIFICAR, ELIMINAR);
            stage.setTitle("Ver Asistencias");
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + "l: \n" + e.getMessage());
        }  
    }
    
    @FXML
    private void mostrarModificarAsistencia(ActionEvent event) {
        String formulario = "/com/aulah/vista/VerAsistencia.fxml";
        try {
            MODIFICAR = true;
            VER = false;
            ELIMINAR = false;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formulario));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene( new Scene((Pane) loader.load()));
            VerAsistencia_ctrl controlador =  loader.<VerAsistencia_ctrl>getController();
            controlador.inicializarDatos(VER, MODIFICAR, ELIMINAR);
            stage.setTitle("Modificar Asistencias");
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarEliminarAsistencia(ActionEvent event) {
        String formulario = "/com/aulah/vista/VerAsistencia.fxml";
        try {
            ELIMINAR = true;
            VER = false;
            MODIFICAR = false;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formulario));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene( new Scene((Pane) loader.load()));
            VerAsistencia_ctrl controlador =  loader.<VerAsistencia_ctrl>getController();
            controlador.inicializarDatos(VER, MODIFICAR, ELIMINAR);
            stage.setTitle("Eliminar Asistencias");
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        } 
    }
    
    
    //******************************* Pestaña EVALUACION *************************
    @FXML
    private void mostrarNuevaEvaluacion(ActionEvent event) {
        String formulario = "/com/aulah/vista/Evaluacion.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Nueva Evaluacion");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }  
    }
    
    @FXML
    private void mostrarVerEvaluacion(ActionEvent event) {
        String formulario = "/com/aulah/vista/VerEvaluacion.fxml";
        try {
            VER = true;
            MODIFICAR = false;
            ELIMINAR = false;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formulario));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene( new Scene((Pane) loader.load()));
            VerEvaluacion_ctrl controlador =  loader.<VerEvaluacion_ctrl>getController();
            controlador.inicializarDatos(VER, MODIFICAR, ELIMINAR);
            stage.setTitle("Ver Evaluaciones");
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }  
    }
    
    @FXML
    private void mostrarModificarEvaluacion(ActionEvent event) {
        String formulario = "/com/aulah/vista/VerEvaluacion.fxml";
        try {
            MODIFICAR = true;
            VER = false;
            ELIMINAR = false;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formulario));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene( new Scene((Pane) loader.load()));
            VerAsistencia_ctrl controlador =  loader.<VerAsistencia_ctrl>getController();
            controlador.inicializarDatos(VER, MODIFICAR, ELIMINAR);
            stage.setTitle("Modificar Evaluaciones");
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarEliminarEvaluacion(ActionEvent event) {
        String formulario = "/com/aulah/vista/VerEvaluacion.fxml";
        try {
            ELIMINAR = true;
            VER = false;
            MODIFICAR = false;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formulario));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene( new Scene((Pane) loader.load()));
            VerAsistencia_ctrl controlador =  loader.<VerAsistencia_ctrl>getController();
            controlador.inicializarDatos(VER, MODIFICAR, ELIMINAR);
            stage.setTitle("Eliminar Evaluaciones");
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        } 
    }
    
    //******************************* Pestaña REPORTES *************************
    @FXML
    private void mostrarCursoClases(){
        String formulario = "/com/aulah/vista/VerCursoClases.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Curso y Clases (VerCursoClases.fxml)");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }        
    }
    
    //******************************* Pestaña Configuración *************************
    @FXML
    private void mostrarFormularioCentro(ActionEvent event) {
        String formulario = "/com/aulah/vista/Centro.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar el Centro Educativo");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarFormularioJornada(ActionEvent event) {
        String formulario = "/com/aulah/vista/Jornada.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar las Jornadas");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarFormularioNivel(ActionEvent event) {
        String formulario = "/com/aulah/vista/Nivel.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar los Niveles");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarFormularioCurso(ActionEvent event) {
        String formulario = "/com/aulah/vista/Curso.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar los Cursos");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarFormularioSeccion(ActionEvent event) {
        String formulario = "/com/aulah/vista/Seccion.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar las Secciones");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarFormularioTipoPeriodo(ActionEvent event) {
        String formulario = "/com/aulah/vista/TipoPeriodo.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar el Tipo de Periodo");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarFormularioPeriodo(ActionEvent event) {
        String formulario = "/com/aulah/vista/Periodo.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar el Periodo de Clase");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarFormularioSemestre(ActionEvent event) {
        String formulario = "/com/aulah/vista/Semestre.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar los Semestres");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarFormularioParcialAnual(ActionEvent event) {
        String formulario = "/com/aulah/vista/ParcialAnual.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar los Parciales Anuales");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarFormularioParcialSemestral(ActionEvent event) {
        String formulario = "/com/aulah/vista/ParcialSemestral.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar los Parciales Semestrales");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarFormularioClases(ActionEvent event) {
        String formulario = "/com/aulah/vista/Clase.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar Clases");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }     
    }
    
    @FXML
    private void mostrarFormularioCursoClases(ActionEvent event) {
        String formulario = "/com/aulah/vista/CursoClases.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Asignar Clases al Curso");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }        
    }
    
    @FXML
    private void mostrarFormularioCriterioEvaluacion(ActionEvent event){
        String formulario = "/com/aulah/vista/CriterioEvaluacion.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Tipo de Evaluación");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        } 
    }
    
    @FXML
    private void mostrarFormularioAlumno(ActionEvent event) {
        String formulario = "/com/aulah/vista/Alumno.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Configurar Alumno");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }  
    }
    
    @FXML
    private void mostrarFormularioImportarAlumnos(ActionEvent event) {
        String formulario = "/com/aulah/vista/ImportarAlumnos.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Importar alumnos(as)");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }
    }
    
    @FXML
    private void mostrarLimpiarTablas(){
        String formulario = "/com/aulah/vista/LimpiarTablas.fxml";
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(formulario));
            Scene scene = new Scene(root);
            stage.setTitle("Limpiar Tablas");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            System.out.println("Mostrado formulario: " + formulario);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar mostrar la ventana " + formulario + ": \n" + e.getMessage());
        }        
    }
    
    
    //******************************* Sin Pestaña *************************
    private boolean probarConexionBD(){
        Conexion conexion = new Conexion();
        if(conexion.hayConexion()){
            System.out.println(":) El Servidor de Base de Datos está funcionando y se conecta correctamente... esto es una prueba\n- - - - - - - - - -");
            return true;
        }else{
            Alert aviso = new Alert(Alert.AlertType.INFORMATION);
            aviso.setTitle(":( Error fatal");
            aviso.setHeaderText("Lamentablemente no se pudo establecer la conexión con el Servidor de Base de Datos.\n" + 
                    "¡El sistema Aula-H no puede funcionar adecuadamente!");
            aviso.setContentText("El servidor de BD << " + conexion.getUrl() + " >> podría estar detenido.");
            aviso.showAndWait();
            return  false;
        }
    }

    @FXML
    private void cerrarAplicacion(ActionEvent event){
        System.exit(0);
    }    
    
}
