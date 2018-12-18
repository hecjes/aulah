
package com.aulah.xutilidades;

import com.aulah.xutilidades.GuardarCentro;
import java.util.Scanner;

public class Probar {
    public static void main(String[] args) {
        byte cod_centro;
        String centro;
        String clave;
        String direccion;
        int resultado;
        
        Scanner leer = new Scanner(System.in);
        
       /* System.out.print("Ingrese el codigo del centro ");
        cod_centro = leer.nextByte();
        System.out.print("Ingrese el nombre del centro ");
        centro = leer.nextLine();
        System.out.print("Ingrese la clave del centro ");
        clave = leer.nextLine();
        System.out.print("Ingrese la direccion del centro ");
        direccion = leer.nextLine();*/
        
        GuardarCentro gc = new GuardarCentro();
        try {
            resultado = gc.guardarCentro("IT5N", "07150085M5", "Teupasenti, el guanacaste");
            if(resultado == 1){
                System.out.println("Registro guardado");
            }else{
                System.out.println("No se pudo guardar");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error al Guardar Centro: "+ e.toString());
        }
        
        
    }
}
