
package com.aulah.utilidades;

import com.aulah.modelo.Asistencia;

/**
 *
 * Clase modelo para los elementos (Objetos Alumno) del Arraylist de Asistencia
 * Se usa para luego llenar un ObservableList
 * 
 * @author Hector Armando Herrera
 */
public class AsistenciaArrLst {
    private int codigo;
    private String nombre;
    private Asistencia.Opciones asistir;
    private String nombre_columna;
    
    public AsistenciaArrLst(int codigo, String nombre, Asistencia.Opciones asistir) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.asistir = asistir;
    }
    
    public AsistenciaArrLst(int codigo, String nombre, String nombre_columna) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.nombre_columna = nombre_columna;
    }
    
    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
            this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
            this.nombre = nombre;
    }
    
    public Asistencia.Opciones getAsistir(){
        return asistir;
    }
    public void setAsistir(Asistencia.Opciones asistir){
        this.asistir = asistir;
    }
    
    public String getNombreColumna() {
        return nombre_columna;
    }
    public void setNombreColumna(String nombre_columna) {
            this.nombre_columna = nombre_columna;
    }
    
    public String mostrarDatos(){
        return "El alumno es " + getCodigo() + " : " + getNombre();
    }
}
