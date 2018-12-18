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

public class Configurar {
    
    //Retorna el nombre de la clase en la cual se encuentra el metodo.
    public static String getNombreClase(){
        return new Exception().getStackTrace()[1].getClassName();
    }
    
    //Retorna el nombre del método desde el cual se hace el llamado.
    public static String getNombreMetodo(){
        return new Exception().getStackTrace()[1].getMethodName();
    }
}
