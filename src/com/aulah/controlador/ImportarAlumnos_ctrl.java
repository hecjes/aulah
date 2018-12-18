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
import com.aulah.modelo.Nivel;
import com.aulah.modelo.Periodo;
import com.aulah.modelo.Seccion;
import com.aulah.modelo.ImportarAlumnos;
import com.aulah.utilidades.AlumnoArrLst;
import com.aulah.utilidades.ImportarHojaCalculo;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImportarAlumnos_ctrl implements Initializable {
    
    //Hace una nueva conexion a la a la BD
    Conexion conexion = new Conexion();
    //Elementos GUI
    @FXML private TextField txtRuta;
    @FXML private Button btnBuscar;
    @FXML private TableView<ImportarAlumnos> tblAlumnos;
    @FXML private TableColumn<ImportarAlumnos, String> clmRne; 
    @FXML private TableColumn<ImportarAlumnos, String> clmNombre; 
    @FXML private TableColumn<ImportarAlumnos, ImportarAlumnos.Genero> clmSexo; 
    @FXML private ComboBox<Periodo> cmbPeriodo;
    @FXML private ComboBox<Nivel> cmbNivel;
    @FXML private ComboBox<Curso> cmbCurso;
    @FXML private ComboBox<Seccion> cmbSeccion;
    @FXML private Button btnMatricular;
    @FXML private Button btnEliminarFila;
    @FXML private Button btnMasculino;
    @FXML private Button btnFemenino;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalir;
    
    //ArrayList para llenar el ObservableList
    private ArrayList<AlumnoArrLst> lista = new ArrayList<AlumnoArrLst>();
    //ObservableList para los llenar el TableView
    private ObservableList<ImportarAlumnos> lista_alumnos = FXCollections.observableArrayList(); 
    //ObservableList para los llenar los ComboBox
    private ObservableList<Periodo> listaPeriodos = FXCollections.observableArrayList(); 
    private ObservableList<Nivel> listaNiveles = FXCollections.observableArrayList();
    private ObservableList<Curso> listaCursos = FXCollections.observableArrayList();
    private ObservableList<Seccion> listaSecciones = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conecta a la BD
        Connection conn = conexion.establecerConexion();
        //Llenar las listas desde la BD
        Periodo.llenarNombrePeriodos(conn, listaPeriodos);
        //Asigna los items de los ComboBox
        cmbPeriodo.setItems(listaPeriodos);
        //Cierra la conexion con la BD
        conexion.cerrarConexion();
        //Hace editable el TableView
        tblAlumnos.setEditable(true);
        clmRne.setCellFactory(TextFieldTableCell.<ImportarAlumnos>forTableColumn());
        clmNombre.setCellFactory(TextFieldTableCell.<ImportarAlumnos>forTableColumn());
        //Hace editable la columna Rne
        clmRne.setCellFactory(TextFieldTableCell.forTableColumn());
        clmRne.setOnEditCommit(new EventHandler<CellEditEvent<ImportarAlumnos, String>>(){
                public void handle(CellEditEvent<ImportarAlumnos, String> t) {
                    ((ImportarAlumnos) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRne(t.getNewValue());
                }
            }
        );
        //Hace editable la columna Nombre
        clmNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        clmNombre.setOnEditCommit(new EventHandler<CellEditEvent<ImportarAlumnos, String>>(){
                public void handle(CellEditEvent<ImportarAlumnos, String> t) {
                    ((ImportarAlumnos) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
                }
            }
        );
        //Hace editable la columna Sexo
        clmSexo.setCellValueFactory(new PropertyValueFactory<ImportarAlumnos, ImportarAlumnos.Genero>("Genero"));
        clmSexo.setCellFactory(ChoiceBoxTableCell.forTableColumn(ImportarAlumnos.Genero.FEMENINO, ImportarAlumnos.Genero.MASCULINO, ImportarAlumnos.Genero.SEXO));
        clmSexo.setOnEditCommit(data -> {
            ImportarAlumnos p = data.getRowValue();
            p.setGenero(data.getNewValue());
            System.out.println(data.getRowValue());
        });
       inhabilitarLimpiarElementos();
       gestionarEventos();
       btnBuscar.requestFocus();
    } 
    
    public void gestionarEventos(){
        //Ocurre cada vez que se selecciona un item del TableView
        tblAlumnos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ImportarAlumnos>(){
                @Override
                public void changed(ObservableValue<? extends ImportarAlumnos> ov, 
                    ImportarAlumnos valorAnterior, ImportarAlumnos valorSeleccionado) {
                    if(valorSeleccionado != null){
                        btnEliminarFila.setDisable(false);
                    }
                }  
            }
        );
    }
    
    @FXML
    private void buscarArchivo(){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Buscar archivo de Hoja de Cálculo");
            //Muestra por defecto el directorio de usuario (Home)
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            // Agregar filtros para facilitar la busqueda
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Archivos de Hoja de Calc", "*.xls")
            );
            // Mostrar el FileChooser
            File archivo = fileChooser.showOpenDialog(null);
            // Mostar la ruta del archivo
            if (archivo != null) {
                txtRuta.setText(archivo.getAbsolutePath());
                ImportarHojaCalculo importar = new ImportarHojaCalculo();
                importar.leerArchivoExcel(archivo);
                llenarTablaAlumnos();
            }
    }
    
    private void llenarTablaAlumnos(){
        tblAlumnos.getItems().clear();
        //Limpia el ArrayList
        lista.clear();
        //Limpiar el ObservableList
        lista_alumnos.clear();
        //Trae los datos del ArrayList
        lista = ImportarHojaCalculo.obtenerListaAlumnos();
        //Agrega los elementos del ArrayList al modelo del ObservableList
        for(AlumnoArrLst aa: lista){
            lista_alumnos.add(new ImportarAlumnos(aa.getRne(), aa.getNombre().toUpperCase(), aa.getSexo()));
        }
        //Agrega el ObservableList al TableView
        tblAlumnos.setItems(lista_alumnos);
        //Muestra los datos en las columnas del TableView
        clmRne.setCellValueFactory(new PropertyValueFactory<ImportarAlumnos, String>("rne"));
        clmNombre.setCellValueFactory(new PropertyValueFactory<ImportarAlumnos, String>("nombre"));
        clmSexo.setCellValueFactory(new PropertyValueFactory<ImportarAlumnos, ImportarAlumnos.Genero>("genero"));
        if(tblAlumnos.getItems().isEmpty()){
            inhabilitarLimpiarElementos();
        }else{
            cmbPeriodo.setDisable(false);
            cmbNivel.setDisable(false);
            cmbCurso.setDisable(false);
            cmbSeccion.setDisable(false);
            btnMatricular.setDisable(true);
            btnCancelar.setDisable(false);
            btnMasculino.setDisable(false);
            btnFemenino.setDisable(false);
        }
    }
    
    @FXML 
    public void mostrarNivelesPeriodo(){
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
    
    @FXML 
    public void mostrarCursosNivel(){
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
    
    @FXML 
    public void mostrarSeccionesCursos(){
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
    
    @FXML
    private void eliminarFila(){
        lista_alumnos.remove(tblAlumnos.getSelectionModel().getSelectedIndex());
        if(tblAlumnos.getItems().isEmpty()){
            inhabilitarLimpiarElementos();
        }
    }
    
    @FXML
    private void matricularAlumnos(){
        if(lista_alumnos.size() > 0){
            Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
            confirmar.setTitle("Matricular Alumnos(as)");
            confirmar.setHeaderText("Va a matricular " + lista_alumnos.size() + " Alumnos(as) en " + cmbCurso.getValue() + ", Sección " + cmbSeccion.getValue() + ".\n" +
                    "\n\n¿Desea continuar?");
            confirmar.setContentText("");
            Optional<ButtonType> result = confirmar.showAndWait();
            if(result.get() == ButtonType.OK){
                int cont = 0;
                int codigo_seccion = cmbSeccion.getSelectionModel().getSelectedItem().getCodigo();
                int codigo_jornada = obtenerCodigoJornada(codigo_seccion);
                //Conecta a la BD
                Connection conn = conexion.establecerConexion();
                lista_alumnos.forEach((registro) -> {
                    new Alumno(0, 
                               registro.getNombre().toUpperCase(), 
                               registro.getRne(), 
                               registro.getGenero().toString(), 
                               "MATRICULADO", 
                               cmbPeriodo.getValue(), 
                               cmbNivel.getValue(), 
                               cmbCurso.getValue(), 
                               cmbSeccion.getValue(),
                               codigo_jornada).guardarRegistro(conn);

                });
                conexion.cerrarConexion();
                Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
                mensaje.setTitle("Alumnos(as) guardados");
                mensaje.setContentText("");
                mensaje.setHeaderText("Se matricularon " + lista_alumnos.size() + " nuevos Alumnos(as),\n" + 
                                        "en " + cmbCurso.getValue() + ", Sección " + cmbSeccion.getValue() + ".");
                mensaje.show();
                limpiarListas();
                inhabilitarLimpiarElementos();
            }
        }
    }
    
    private void limpiarListas(){
        lista_alumnos.clear();
        lista.clear();
        tblAlumnos.getItems().clear();
    }
    
    private int obtenerCodigoJornada(int p_codigo_seccion){
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
    
    @FXML
    private void habilitarBotonMatricular(){
        if(cmbSeccion.selectionModelProperty().getValue() != null){
            btnMatricular.setDisable(false);
        }
    }
    
    @FXML
    private void feminizarAlumnos(){
        
    }
    
    @FXML
    private void masculinizarAlumnos(){
        System.out.println("Las filas son " + tblAlumnos.getItems().size());
        for(int i=0; i<tblAlumnos.getItems().size();i++){
            System.out.println("Las filas son " + clmSexo.getCellObservableValue(i)); 
        }
    }
    
    private void inhabilitarLimpiarElementos(){
        txtRuta.setText(null);
        txtRuta.setDisable(false);
        btnBuscar.setDisable(false);
        cmbPeriodo.setValue(null);
        cmbPeriodo.setDisable(true);
        cmbNivel.setValue(null);
        cmbNivel.setDisable(true);
        cmbCurso.setValue(null);
        cmbCurso.setDisable(true);
        cmbSeccion.setValue(null);
        cmbSeccion.setDisable(true);
        btnMatricular.setDisable(true);
        btnCancelar.setDisable(true);
        btnEliminarFila.setDisable(true);
        btnMasculino.setDisable(true);
        btnFemenino.setDisable(true);
    } 
    
    @FXML
    private void cerrarFormulario(){
        // Obtener el escenario (Stage)
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        // Cerrar el formulario.
        stage.close();
    }
    
}
