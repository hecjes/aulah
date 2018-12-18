
package com.aulah.utilidades;

import com.aulah.modelo.ImportarAlumnos;

/**
 *
 * Clase modelo para los elementos (Objetos Alumno) del Arraylist
 * Se usa para luego llenar un ObservableList
 * 
 * @author Hector Armando Herrera
 */
public class AlumnoArrLst {
    private String rne;
    private String nombre;
    private ImportarAlumnos.Genero sexo;
    
    public AlumnoArrLst(String rne, String nombre, ImportarAlumnos.Genero sexo) {
        this.rne = rne;
        this.nombre = nombre;
        this.sexo = sexo;
    }
    
    public String getRne() {
        return rne;
    }
    public void setRne(String rne) {
            this.rne = rne;
    }
    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
            this.nombre = nombre;
    }
    
    public ImportarAlumnos.Genero getSexo() {
        return sexo.SEXO;
    }
    public void setSexo(ImportarAlumnos.Genero sexo) {
            this.sexo = sexo;
    }
    
    public String mostrarDatos(){
        return "El alumno es " + getRne() + " " + getNombre();
    }
}
