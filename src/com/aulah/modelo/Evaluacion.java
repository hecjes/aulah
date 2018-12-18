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
package com.aulah.modelo;

import static com.aulah.modelo.Configurar.getNombreMetodo;
import com.aulah.utilidades.EvaluacionArrLst;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author hector
 */
public class Evaluacion {
    public IntegerProperty numero;
    public IntegerProperty codigo;
    public StringProperty nombre_alumno;
    public StringProperty rne;
    public StringProperty sexo;
    public StringProperty evaluacion;
    public Date fecha;
    public StringProperty valor_base;
    public StringProperty estado;
    public CriterioEvaluacion criterio;
    public Periodo periodo;
    public Semestre semestre;
    public ParcialSemestral parcial_semestral;
    public ParcialAnual parcial_anual;
    public Curso curso;
    public Seccion seccion;
    public Clase clase;
    
    
    public static int codigo_generado = 0;//codigo_generado: variable para obtener el codigo generado por el PreparedStatement al
    //autoincrementar la llave cuando se guarda un nuevo registro. Se llena en guardarRegistro()

    //Para manejar Evaluacion de Parciales Semestrales
    public Evaluacion(int codigo, String evaluacion, Date fecha, String valor_base, String estado, CriterioEvaluacion criterio, Periodo periodo, Semestre semestre, ParcialSemestral parcial_semestral, Curso curso, Seccion seccion, Clase clase) { 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.evaluacion = new SimpleStringProperty(evaluacion);
            this.fecha = fecha;
            this.valor_base = new SimpleStringProperty(valor_base);
            this.estado = new SimpleStringProperty(estado);
            this.criterio = criterio;
            this.periodo = periodo;
            this.semestre = semestre;
            this.parcial_semestral = parcial_semestral;
            this.curso = curso;
            this.seccion = seccion;
            this.clase = clase;
    }
    
    //Para manejar Evaluacion de Parciales Anuales
    public Evaluacion(int codigo, String evaluacion, Date fecha, String valor_base, String estado, CriterioEvaluacion criterio, Periodo periodo, ParcialAnual parcial_anual, Curso curso, Seccion seccion, Clase clase){ 
            this.codigo = new SimpleIntegerProperty(codigo);
            this.evaluacion = new SimpleStringProperty(evaluacion);
            this.fecha = fecha;
            this.valor_base = new SimpleStringProperty(valor_base);
            this.estado = new SimpleStringProperty(estado);
            this.criterio = criterio;
            this.periodo = periodo;
            this.parcial_anual = parcial_anual;
            this.curso = curso;
            this.seccion = seccion;
            this.clase = clase;
    }
    
    //Para obtener los Nombres y la Opcion de los Alumnos 
    public Evaluacion(int numero, int codigo, String nombre_alumno, String valor_base) { 
            this.numero = new SimpleIntegerProperty(numero);
            this.codigo = new SimpleIntegerProperty(codigo);
            this.nombre_alumno = new SimpleStringProperty(nombre_alumno);
            this.valor_base = new SimpleStringProperty(valor_base);
    }
    
    public Evaluacion(int codigo){
        this.codigo = new SimpleIntegerProperty(codigo);
    }
   
    //Metodos atributo : numero
    public int getNumero(){
        return numero.get();
    }
    public void setNumero(int numero){
        this.numero = new SimpleIntegerProperty(numero);
    }
    public IntegerProperty NumeroProperty() {
            return numero;
    }
    //Metodos atributo: codigo
    public int getCodigo() {
            return codigo.get();
    }
    public void setCodigo(int codigo) {
            this.codigo = new SimpleIntegerProperty(codigo);
    }
    public IntegerProperty CodigoProperty() {
            return codigo;
    }
    //Metodos atributo: nombre
    public String getNombre() {
            return nombre_alumno.get();
    }
    public void setNombre(String nombre) {
            this.nombre_alumno = new SimpleStringProperty(nombre);
    }
    public StringProperty NombreProperty() {
            return nombre_alumno;
    }
    //Metodos atributo: rne
    public String getRne() {
            return rne.get();
    }
    public void setRne(String rne) {
            this.rne = new SimpleStringProperty(rne);
    }
    public StringProperty RneProperty() {
            return rne;
    }
    //Metodos atributo: sexo
    public String getSexo() {
            return sexo.get();
    }
    public void setSexo(String sexo) {
            this.sexo = new SimpleStringProperty(sexo);
    }
    public StringProperty SexoProperty() {
            return sexo;
    }
    //Metodo atributo: evaluacion
    public String getEvaluacion(){
        return evaluacion.get();
    }
    public void setEvaluacion(String evaluacion){
        this.evaluacion = this.evaluacion;
    }
    public StringProperty EvaluacionProperty(){
        return evaluacion;
    }
    //Metodo atributo: fecha
    public Date getFecha(){
        return fecha;
    }
    public void setFecha(Date fecha){
        this.fecha = this.fecha;
    }
    //Metodos atributo : valor_base
    public String getValor(){
        return valor_base.get();
    }
    public void setValor(String valor_base){
        this.valor_base = new SimpleStringProperty(valor_base);
    }
    public StringProperty ValorProperty() {
            return valor_base;
    }
    //Metodos atributo: estado
    public String getEstado() {
            return estado.get();
    }
    public void setEstado(String estado) {
            this.estado = new SimpleStringProperty(estado);
    }
    public StringProperty EstadoProperty() {
            return estado;
    }
    //Metodos atributo: periodo
    public Periodo getPeriodo() {
            return periodo;
    }
    public void setPeriodo(Periodo periodo) {
            this.periodo = periodo;
    }
    //Metodos atributo: semestre
    public Semestre getSemestre() {
            return semestre;
    }
    public void setSemestre(Semestre semestre) {
            this.semestre = semestre;
    }
    //Metodos atributo: parcial_semestral
    public ParcialSemestral getParcial_Semestral() {
            return parcial_semestral;
    }
    public void setParcial_Semestral(ParcialSemestral parcial_semestral) {
            this.parcial_semestral = parcial_semestral;
    }
    //Metodos atributo: parcial_anual
    public ParcialAnual getParcial_Anual() {
            return parcial_anual;
    }
    public void setParcial_Anual(ParcialAnual parcial_anual) {
            this.parcial_anual = parcial_anual;
    }
    //Metodos atributo: curso
    public Curso getCurso() {
            return curso;
    }
    public void setCurso(Curso curso) {
            this.curso = curso;
    }
    //Metodos atributo: seccion
    public Seccion getSeccion() {
            return seccion;
    }
    public void setSeccion(Seccion seccion) {
            this.seccion = seccion;
    }
    //Metodos atributo: clase
    public Clase getClase() {
            return clase;
    }
    public void setClase(Clase clase) {
            this.clase = clase;
    }
    
    public static void llenarInformacionNombresAlumnosPeriodoCursoSeccion(Connection conn, ArrayList<EvaluacionArrLst> lista, int codigo_periodo, int codigo_curso, int codigo_seccion){
        String sentenciaSQL = "SELECT A.codigo, " + 
                                    "A.nombre, " +
                                    "A.rne, " +
                                    "A.sexo, " +
                                    "A.cod_periodo, " +
                                    "A.cod_nivel, " +
                                    "A.cod_curso, " +
                                    "A.cod_seccion, " +
                                    "A.cod_jornada, " +
                                    "B.periodo, " +
                                    "D.curso, " +
                                    "E.seccion " +
                              "FROM tbl_alumnos A " +
                              "INNER JOIN tbl_periodos B " +
                              "ON ((A.cod_periodo = B.codigo) AND (B.codigo = " + codigo_periodo + ")) " +
                              "INNER JOIN tbl_cursos D " +
                              "ON ((A.cod_curso = D.codigo) AND (D.codigo = " + codigo_curso + ")) " +
                              "INNER JOIN tbl_secciones E " +
                              "ON ((A.cod_seccion = E.codigo) AND (E.codigo = " + codigo_seccion + "))";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                lista.add(new EvaluacionArrLst(
                                    resultado.getInt("codigo"),
                                    resultado.getString("nombre")
                              )
                );
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener los datos de los Alumnos para la Evaluacion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public int guardarEvaluacionSemestral(Connection conn){
        String sentenciaSQL = "INSERT INTO tbl_evaluaciones(codigo, " +
                                                    "evaluacion, " + 
                                                    "fecha, " + 
                                                    "valor_base, " +
                                                    "estado, " +
                                                    "cod_criterio, " + 
                                                    "cod_periodo, " + 
                                                    "cod_semestre, " + 
                                                    "cod_parcial_semestral, " + 
                                                    "cod_curso, " + 
                                                    "cod_seccion, " +
                                                    "cod_clase)" + 
                              " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            instruccion.setInt(1, codigo.get());
            instruccion.setString(2, evaluacion.get());
            instruccion.setDate(3, fecha);
            instruccion.setInt(4, Integer.parseInt(valor_base.get()));
            instruccion.setString(5, estado.get());
            instruccion.setInt(6, criterio.getCodigo());
            instruccion.setInt(7, periodo.getCodigo());
            instruccion.setInt(8, semestre.getCodigo());
            instruccion.setInt(9, parcial_semestral.getCodigo());
            instruccion.setInt(10, curso.getCodigo());
            instruccion.setInt(11, seccion.getCodigo());
            instruccion.setInt(12, clase.getCodigo());
            int num = instruccion.executeUpdate();
            ResultSet rs = instruccion.getGeneratedKeys();
            if (rs != null && rs.next()) {
                codigo_generado = rs.getInt(1);
            }
            return num;
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar la Evaluacion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            codigo_generado = 0;
            return 0;
        }
    }
    
    //Se crea si no existe ta tabla para guardar los detalles de las Evaluaciones para la clase especificada
    public static void crearTablaDetalleEvaluacion(String nombre_tabla, Connection conn){
        String sentenciaSQL = "CREATE TABLE IF NOT EXISTS " + nombre_tabla + " (codigo_alumno INT(6) NOT NULL, PRIMARY KEY (codigo_alumno))";
        try {
            PreparedStatement statement = conn.prepareStatement(sentenciaSQL);
            int resulatado = statement.executeUpdate();
            if(resulatado==1){
                System.out.println("\t...la tabla " + nombre_tabla + " ha sido creada.");
            }else{
                System.out.println("\t...la tabla " + nombre_tabla + " ya existe.");
            }
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de crear la tabla " + nombre_tabla +  " para el detalle de Evaluacion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void agregarNuevaColumnaTabla(String nombre_tabla, String nombre_columna, Connection conn){
        String sentenciaSQL = "ALTER TABLE `" + nombre_tabla + "` ADD COLUMN `" + nombre_columna + "` TINYINT(4)";
        try {
            PreparedStatement statement = conn.prepareStatement(sentenciaSQL);
            statement.executeUpdate();
            System.out.println("\t...la columna " + nombre_columna + " ha sido agregada a la tabla " + nombre_tabla + ".");
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de agregar la columna: " + nombre_columna + " en la tabla: " + nombre_tabla +  " para el detalle de la Evaluacion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static boolean tablaDetalleEvaluacionTieneCodigosAlumnos(String nombre_tabla, Connection conn){
        boolean hay_datos = false;
        String sentenciaSQL = "SELECT COUNT(codigo_alumno) FROM " + nombre_tabla;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                if(resultado.getInt(1) > 0){
                    hay_datos = true;
                    System.out.println("\t...la tabla " + nombre_tabla + " ya contiene la columna <codigos de alumnos>");
                }else{
                    hay_datos = false;
                    System.out.println("\t...la tabla " + nombre_tabla + " aun no contiene la columna <codigos de alumnos>");
                }
            }
        } catch (Exception ex) {
            System.err.println(
                        "Error de Base de Datos tratando de saber si ya hay codigos de alumnos en la tabla " + nombre_tabla + " de detalle de Evaluacion.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            hay_datos = false;
        }
        return hay_datos;
    }
    
    public static int obtenerValorCriterioNormal(int cod_criterio, Connection conn){
        int valor = 0;
        String sentenciaSQL = "SELECT valor FROM tbl_criterios_evaluacion WHERE codigo = " + cod_criterio;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                valor = resultado.getInt(1);    
            }
        } catch (Exception ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el Valor del Criterio.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            valor = 0;
        }
        return valor;
    }
    
    public static int obtenerSumaCriterioNormal(int cod_clase, int cod_criterio, Connection conn){
        int valor_total = 0;
        String sentenciaSQL = "SELECT SUM(valor_base) FROM tbl_evaluaciones WHERE cod_criterio = " + cod_criterio + " AND cod_clase = " + cod_clase;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                valor_total = resultado.getInt(1);    
            }
        } catch (Exception ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener el total de la suma de los Valores del Criterio.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
            valor_total = 0;
        }
        return valor_total;
    }
    
    public static int obtenerSumaValorTodosCriterios(int cod_periodo, int cod_semestre, int cod_parcial_semestral, int cod_curso, int cod_seccion, int cod_clase, Connection conn){
        int suma_valor = 0;
        String sentenciaSQL = "SELECT SUM(valor_base) " +
                              "FROM tbl_evaluaciones " +
                              "WHERE cod_periodo = " + cod_periodo + 
                              " AND cod_semestre = " + cod_semestre + 
                              " AND cod_parcial_semestral = " + cod_parcial_semestral +
                              " AND cod_curso = " + cod_curso +
                              " AND cod_seccion = " + cod_seccion +
                              " AND cod_clase = " + cod_clase;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultado = statement.executeQuery(sentenciaSQL);
            while(resultado.next()){
                suma_valor = resultado.getInt(1);    
            }
        } catch (Exception ex) {
            System.err.println(
                        "Error de Base de Datos tratando de obtener la suma total de los Valores de Criterios de Evaluacion.\n"+
                        "Método: " + getNombreMetodo() + "\n" +
                        "Sentencia SQL: \"" + sentenciaSQL+ "\"\n" +
                        "Mensaje de error(SQL): " + ex.getMessage() + "\n"
            );
            suma_valor = 0;
        }
        return suma_valor;
    }
    
    public static void guardarCodigosAlumnos(String nombre_tabla, int codigo_alumno, Connection conn){
        String sentenciaSQL = "INSERT INTO " + nombre_tabla + "(codigo_alumno) VALUES (?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sentenciaSQL);
            statement.setInt(1, codigo_alumno);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el codigo_alumno en la tabla " + nombre_tabla + " de detalle de Asistencia.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
    
    public static void guardarDetalleEvaluacion(String nombre_tabla, String nombre_columna, int codigo_alumno, String valor, Connection conn){
        String sentenciaSQL = "UPDATE " + nombre_tabla + " SET " + nombre_columna + " = ? WHERE codigo_alumno = " + codigo_alumno + "";
        if(valor.equals("")){
            valor = "-1";
        }
        int valor_nota = Integer.parseInt(valor);
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, valor_nota);
            instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de guardar el registro de detalle de Evaluacion en la tabla: " + nombre_tabla + ", en la columna: " + nombre_columna + ".\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): "+ex.getMessage()+"\n"
            );
        }
    }
        
    public int modificarRegistro(Connection conn){
        String sentenciaSQL = "UPDATE tbl_alumnos " +
                              "SET nombre = ?, " +
                                  "rne = ?, " +
                                  "sexo = ?, " +
                                  "estado = ?, " +
                                  "cod_periodo = ?, " +
                                  "cod_nivel = ?, " +
                                  "cod_curso = ?, " +
                                  "cod_seccion = ?, " +
                                  "cod_jornada = ? " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setString(1, nombre_alumno.get());
            instruccion.setString(2, rne.get());
            instruccion.setString(3, sexo.get());
            instruccion.setInt(5, periodo.getCodigo());
            instruccion.setInt(6, curso.getCodigo());
            instruccion.setInt(7, seccion.getCodigo());
            instruccion.setInt(8, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de modificar el Alumno.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    public int eliminarAsistencia(Connection conn){
        String sentenciaSQL = "DELETE FROM tbl_asistencias " +
                              "WHERE codigo = ?";
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            instruccion.setInt(1, codigo.get());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de eliminar la Asistencia.\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
        
    @Override
    public String toString(){
        return "Nobre: " + nombre_alumno.get();
    }
}
