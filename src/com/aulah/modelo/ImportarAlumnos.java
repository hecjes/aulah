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

package com.aulah.modelo;

import javafx.beans.property.SimpleStringProperty;

/**
 * Clase modelo de datos para llenar el ObservableList con los alumnos importados
 * 
 * @author hector
 */
public class ImportarAlumnos {
    
    private SimpleStringProperty rne;
    private SimpleStringProperty nombre;
    private SimpleStringProperty sexo;
    private Genero genero;
    
    public ImportarAlumnos(String rne, String nombre, String sexo) {
        this.rne = new SimpleStringProperty(rne);
        this.nombre = new SimpleStringProperty(nombre);
        this.sexo = new SimpleStringProperty(sexo);
    }
    
    public ImportarAlumnos(String rne, String nombre, Genero genero) {
        this.rne = new SimpleStringProperty(rne);
        this.nombre = new SimpleStringProperty(nombre);
        this.genero = genero;
    }
    
    public enum Genero {
        MASCULINO, FEMENINO, SEXO
    }
    
    public String getRne() {
        return rne.get();
    }
    public void setRne(String rne) {
            this.rne = new SimpleStringProperty(rne);
    }
    public String getNombre() {
        return nombre.get();
    }
    public void setNombre(String nombre) {
            this.nombre = new SimpleStringProperty(nombre);
    }
    public void setSexo(String sexo) {
            this.sexo = new SimpleStringProperty(sexo);
    }
    public Genero getGenero() {
        return genero;
    }
    public void setGenero(Genero genero) {
        this.genero = genero;
    }
    
    @Override
    public String toString() {
        return "Alumno{" +
                "nombre='" + nombre + '\'' +
                ", genero=" + genero +
                "}";
    }
}
