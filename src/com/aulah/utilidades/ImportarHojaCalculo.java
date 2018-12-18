
package com.aulah.utilidades;

import com.aulah.modelo.ImportarAlumnos;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

/**
 *
 * Clase para leer datos desde una archivo de Hoja de Cálculo
 * 
 * @author Hector Armando Herrera
 */
public class ImportarHojaCalculo {
    
    private static ArrayList<AlumnoArrLst> lista_alumnos = new ArrayList<AlumnoArrLst>();
    
    public void leerArchivoExcel(File rutaArchivo){
        InputStream excelStream = null;
        String rne = "";
        String nombre = "";
        ImportarAlumnos.Genero sexo = ImportarAlumnos.Genero.SEXO;
        boolean es_fila_alumno = false;
        
        try {
            excelStream = new FileInputStream(rutaArchivo);
            // Representación del más alto nivel de la hoja excel.
            HSSFWorkbook libro_de_trabajo = new HSSFWorkbook(excelStream);
            // Elegimos la hoja que se pasa por parámetro.
            HSSFSheet hoja_de_trabajo = libro_de_trabajo.getSheetAt(0);
            // Objeto que nos permite leer una fila de la hoja excel, y de aquí extraer el contenido de las celdas.
            HSSFRow fila;
            // Obtiene el número de la ultima fila que contienen datos en la hoja
            int ultima_fila = hoja_de_trabajo.getLastRowNum();
            lista_alumnos.ensureCapacity(ultima_fila);
            // Cadena que usamos para almacenar la lectura de la celda
            String valor_celda = "";
            // Se recorren las filas obteniendo los datos       
            for (int r = 0; r < ultima_fila; r++) {
                fila = hoja_de_trabajo.getRow(r);
                if (fila == null){
                    break;
                }else{
                    System.out.print("Fila: " + r + " -> ");
                    for (int c = 0; c < (fila.getLastCellNum()); c++) {
                        if(fila.getCell(c) != null){
                            if(fila.getCell(c).getCellType() == Cell.CELL_TYPE_STRING){
                                valor_celda = fila.getCell(c).getStringCellValue();
                                if(es_identidad(valor_celda)){
                                    es_fila_alumno = true;
                                    rne = valor_celda;
                                    System.out.print(rne + " ");
                                    break;
                                }
                            }
                        }
                    }
                    if(es_fila_alumno){
                        for (int c = 0; c < (fila.getLastCellNum()); c++) {
                            if(fila.getCell(c) != null){
                                if(fila.getCell(c).getCellType() == Cell.CELL_TYPE_STRING){
                                    valor_celda = fila.getCell(c).getStringCellValue();
                                    if(es_nombre(valor_celda)){
                                        nombre = valor_celda;
                                        System.out.print(nombre);
                                        break;
                                    }
                                    
                                }
                            }
                        }
                    }
                    if(es_fila_alumno){
                        lista_alumnos.add(new AlumnoArrLst(rne, nombre, sexo));
                    }
                    es_fila_alumno = false;
                    System.out.println();
                }
            }
        } catch (FileNotFoundException fnex) {
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("No se pudo encontrar el archivo");
            mensaje.setContentText("");
            mensaje.setHeaderText("No se ha encontrado el archivo fuente de los datos");
            mensaje.show();
            System.err.println("El archivo no se ha encontrado: " + fnex.getMessage());
        } catch (IOException ex) {
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("El archivo no funciona");
            mensaje.setContentText("");
            mensaje.setHeaderText("Ocurrió un error al tratar de procesar el archivo fuente de los datos");
            mensaje.show();
            System.err.println("Error al procesar el archivo: " + ex.getMessage());
        } finally {
            try {
                excelStream.close();
            } catch (IOException ex) {
                System.err.println("Error al procesar el archivo cuando este se cerraba: " + ex.getMessage());
            }
        }
        //mostrarContenidoArrayList();
    }
    
    private boolean es_identidad(String p_identidad){
        if(p_identidad.length() == 13){
            byte cont = 0;
            char[] identidad = p_identidad.toCharArray();
            for(int i=0; i<identidad.length; i++){
                if(Character.isDigit(identidad[i])){
                    cont++;
                }
            }
            if(cont == 13){
                return true;
            }
        }
        return false;
    }
    
    private boolean es_nombre(String p_nombre){
        if((p_nombre.length() > 6) && (p_nombre.length() < 50)){
            byte cont = 0;
            char[] nombre = p_nombre.toCharArray();
            for(int i=0; i<nombre.length; i++){
                if(nombre[i] == ' '){
                    cont++;
                }
            }
            if((cont >= 2) && (cont <= 6)){
                return true;
            }
        }
        return false;
    }
    
    private void mostrarContenidoArrayList(){
        for(AlumnoArrLst a: lista_alumnos){
            System.out.println(a.mostrarDatos());            
        }
    }
    
    public static ArrayList<AlumnoArrLst> obtenerListaAlumnos(){
        return lista_alumnos;
    }
        
}
