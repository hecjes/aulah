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
package com.aulah.utilidades;

/**
 *
 * Clase modelo para los elementos (Objetos Alumno) del Arraylist de Evaluacion
 * Se usa para luego llenar un ObservableList
 * 
 * @author Hector Armando Herrera
 */
public class EvaluacionArrLst{
    private int codigo;
    private String nombre;
    private int valor;
    private String nombre_columna;
    
    public EvaluacionArrLst(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }
    
    public EvaluacionArrLst(int codigo, String nombre, String nombre_columna) {
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
    
    public int getValor(){
        return valor;
    }
    public void setValor(int valor){
        this.valor = valor;
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