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
import com.aulah.modelo.VerDetalleAsistencia;
import com.aulah.utilidades.AsistenciaArrLst;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
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
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hector
 */
public class VerDetalleAsistencia_ctrl implements Initializable {
    
    private int codigo_asistencia;
    private int cod_periodo;
    private int cod_semestre;
    private int cod_parcial_semestral;
    private int cod_parcial_anual;
    private int cod_curso;
    private int cod_seccion;
    private int cod_clase;
    //Hace una nueva conexion a la a la BD
    Conexion conexion = new Conexion();
    VerDetalleAsistencia.Opciones ASISTIR;//Se usa para saber que opcion de asistencia tiene el Alumno.

    @FXML private ComboBox<Clase> cmbClase;
    @FXML private DatePicker dtpFecha;
    @FXML private TableView<VerDetalleAsistencia> tblAsistencia;
    @FXML private TableColumn<VerDetalleAsistencia,Integer> clmNo;
    @FXML private TableColumn<VerDetalleAsistencia,String> clmNombre;
    @FXML private TableColumn<VerDetalleAsistencia,VerDetalleAsistencia.Opciones> clmAsistio;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //ArrayList para llenar el ObservableList
    private ArrayList<AsistenciaArrLst> lista = new ArrayList<AsistenciaArrLst>();
    private ObservableList<VerDetalleAsistencia> lista_asistencia = FXCollections.observableArrayList();
    
    public void setCodigoAsistencia(int codigo_asistencia){
        this.codigo_asistencia = codigo_asistencia;
    }
    public int getCodigoAsistencia(){
        return codigo_asistencia;
    }
    public void setCodPeriodo(int cod_periodo){
        this.cod_periodo = cod_periodo;
    }
    public int getCodPeriodo(){
        return cod_periodo;
    }
    public void setCodSemestre(int cod_semestre){
        this.cod_semestre = cod_semestre;
    }
    public int getCodSemestre(){
        return cod_semestre;
    }
    public void setCodParcialSemestral(int cod_parcial_semestral){
        this.cod_parcial_semestral = cod_parcial_semestral;
    }
    public int getCodParcialAnual(){
        return cod_parcial_anual;
    }
    public void setCodCurso(int cod_curso){
        this.cod_curso = cod_curso;
    }
    public int getCodCurso(){
        return cod_curso;
    }
    public void setCodSeccion(int cod_seccion){
        this.cod_seccion = cod_seccion;
    }
    public int getCodSeccion(){
        return cod_seccion;
    }
    public void setCodClase(int cod_clase){
        this.cod_clase = cod_clase;
    }
    public int getCodClase(){
        return cod_clase;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    public void inicializarDatos(boolean ver, boolean modificar, boolean eliminar, Clase clase, Date fecha, int codigo_asistencia, int cod_periodo, int cod_semestre, int cod_parcial_semestral, int cod_curso, int cod_seccion, int cod_clase){
        this.codigo_asistencia = codigo_asistencia;
        this.cod_periodo = cod_periodo;
        this.cod_semestre = cod_semestre;
        this.cod_parcial_semestral = cod_parcial_semestral;
        this.cod_curso = cod_curso;
        this.cod_seccion = cod_seccion;
        this.cod_clase = cod_clase;
        String nombre_tabla = "tbl_asist_" + String.valueOf(cod_periodo) + "_" + String.valueOf(cod_semestre) + "_" + String.valueOf(cod_parcial_semestral) + "_" + String.valueOf(cod_curso) + "_" + String.valueOf(cod_seccion)+ "_" + String.valueOf(cod_clase);
        String nombre_columna = "_" + codigo_asistencia + "_";
        int contador = 0;
        if(ver){
            System.out.println("VER ASISTENCIA");
            conexion.establecerConexion();    
            //Limpia el ObservableList
            lista_asistencia.clear();
            //Limpia la tabla
            tblAsistencia.getItems().clear();
            VerDetalleAsistencia.llenarInformacionDetalleAsistenciaAlumno(conexion.getConnection(), lista, nombre_tabla, nombre_columna);
            for(AsistenciaArrLst aa: lista){
                contador++;
                if(aa.getNombreColumna().equals("SI")){
                    ASISTIR = VerDetalleAsistencia.Opciones.SI;
                }
                if(aa.getNombreColumna().equals("NO")){
                    ASISTIR = VerDetalleAsistencia.Opciones.NO;
                }
                if(aa.getNombreColumna().equals("EXCUSA")){
                    ASISTIR = VerDetalleAsistencia.Opciones.EXCUSA;
                }
                lista_asistencia.add(new VerDetalleAsistencia(contador, aa.getCodigo(), aa.getNombre(), ASISTIR));
            }
            tblAsistencia.setItems(lista_asistencia);
            //clmNo.setCellValueFactory(new PropertyValueFactory<VerDetalleAsistencia,Integer>("numero"));
            clmNombre.setCellValueFactory(new PropertyValueFactory<VerDetalleAsistencia,String>("nombre"));
            clmAsistio.setCellValueFactory(new PropertyValueFactory<VerDetalleAsistencia, VerDetalleAsistencia.Opciones>("opcion"));
            conexion.cerrarConexion();
            cmbClase.setValue(clase);
            dtpFecha.setValue(fecha.toLocalDate());
            dtpFecha.setEditable(false);
            btnGuardar.setDisable(true);
            btnCancelar.setDisable(true);
        }
        if(modificar){
            System.out.println("MODIFICAR ASISTENCIA");
            conexion.establecerConexion();  
            //Limpiar la lista de Alumnos listaAsistencia
            lista.clear();
            //Limpia el ObservableListView
            lista_asistencia.clear();
            //Limpiar el TableView
            tblAsistencia.getItems().clear();
            //Llena la lista de Alumnos desde la BD
            VerDetalleAsistencia.llenarInformacionDetalleAsistenciaAlumno(conexion.getConnection(), lista, nombre_tabla, nombre_columna);
            //Agrega los elementos del ArrayList al modelo del ObservableList
            for(AsistenciaArrLst aa: lista){
                contador++;
                if(aa.getNombreColumna().equals("SI")){
                    ASISTIR = VerDetalleAsistencia.Opciones.SI;
                }
                if(aa.getNombreColumna().equals("NO")){
                    ASISTIR = VerDetalleAsistencia.Opciones.NO;
                }
                if(aa.getNombreColumna().equals("EXCUSA")){
                    ASISTIR = VerDetalleAsistencia.Opciones.EXCUSA;
                }
                lista_asistencia.add(new VerDetalleAsistencia(contador, aa.getCodigo(), aa.getNombre(), ASISTIR));
            }
            if(lista_asistencia.size() > 0){
                //Muestra el contenido de la lista en el TableView
                tblAsistencia.setItems(lista_asistencia);
                //clmNo.setCellValueFactory(new PropertyValueFactory<VerDetalleAsistencia,Integer>("numero"));
                //clmNombre.setCellValueFactory(new PropertyValueFactory<VerDetalleAsistencia,String>("nombre"));
                //clmAsistio.setCellValueFactory(new PropertyValueFactory<VerDetalleAsistencia,VerDetalleAsistencia.Opciones>("opcion"));
                //Hace editable el TableView
                tblAsistencia.setEditable(true);
                clmNo.setCellValueFactory(new PropertyValueFactory<VerDetalleAsistencia,Integer>("numero"));
                clmNombre.setCellValueFactory(new PropertyValueFactory<VerDetalleAsistencia,String>("nombre"));
                //Hace editable la columna Asistio
                clmAsistio.setCellValueFactory(new PropertyValueFactory<VerDetalleAsistencia, VerDetalleAsistencia.Opciones>("opcion"));
                clmAsistio.setCellFactory(ChoiceBoxTableCell.forTableColumn(VerDetalleAsistencia.Opciones.SI, VerDetalleAsistencia.Opciones.NO, VerDetalleAsistencia.Opciones.EXCUSA));
                clmAsistio.setOnEditCommit(data -> {
                    VerDetalleAsistencia p = data.getRowValue();
                    p.setOpciones(data.getNewValue());
                    System.out.println("Se Editó: " + data.getRowValue());
                    btnCancelar.setDisable(false);
                    btnGuardar.setDisable(false);
                });
                btnGuardar.setText("Modificar");
            }else{
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("No hay Alumnos(as)");
                mensaje.setContentText("");
                mensaje.setHeaderText("No se encontró Alumnos(as) en la Sección.");
                mensaje.show();
            }
            conexion.cerrarConexion();
            cmbClase.setValue(clase);
            dtpFecha.setValue(fecha.toLocalDate());
            dtpFecha.setEditable(false);
            btnGuardar.setDisable(true);
            btnCancelar.setDisable(true);
        }
        btnSalir.requestFocus();
    }
    
    @FXML
    public void modificarRegistroSemestral(){
        if(lista_asistencia.size() > 0){
            conexion.establecerConexion();
            String nombre_tabla = "tbl_asist" +                                       
                                  "_" + String.valueOf(cod_periodo) + 
                                  "_" + String.valueOf(cod_semestre) + 
                                  "_" + String.valueOf(cod_parcial_semestral) + 
                                  "_" + String.valueOf(cod_curso) + 
                                  "_" + String.valueOf(cod_seccion) + 
                                  "_" + String.valueOf(cod_clase);
            String nombre_columna = "_" + String.valueOf(codigo_asistencia) + "_";
            String opcion = "";
            System.out.println("\t---Modificando <detalles de la asistencia> en la tabla " + nombre_tabla + "...");
                for(VerDetalleAsistencia lista : lista_asistencia){
                    if(lista.getOpcion() == VerDetalleAsistencia.Opciones.SI){
                        opcion = "SI";
                    }if(lista.getOpcion() == VerDetalleAsistencia.Opciones.NO){
                        opcion = "NO";
                    }if(lista.getOpcion() == VerDetalleAsistencia.Opciones.EXCUSA){
                        opcion = "EXCUSA";
                    }
                    VerDetalleAsistencia.modificarDetalleAsistencia(nombre_tabla, nombre_columna, lista.getCodigo(), opcion, conexion.getConnection());
                }
            conexion.cerrarConexion();                
            btnCancelar.setDisable(true);
            btnGuardar.setDisable(true);
        }else{
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Datos vacíos");
            mensaje.setContentText("");
            mensaje.setHeaderText("No hay datos de Asistencia para guardar");
            mensaje.show();
        } 
    }
    
    @FXML
    private void cerrarFormulario(){
        // Obtener el escenario (Stage)
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        // Cerrar el formulario.
        stage.close();
    }
    
}
