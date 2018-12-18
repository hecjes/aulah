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

import static com.aulah.modelo.Configurar.getNombreMetodo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *Clase para limpiar la tabla especificada
 * 
 * @author hector
 */
public class LimpiarTablas {
    //Hace una nueva conexion a la a la BD
    static Conexion conexion = new Conexion();
    
    public static int limpiarTabla(String p_nombre_tabla){
        int resultado = 0;
        String sentenciaSQL = "DELETE FROM " + p_nombre_tabla;
        Connection conn = conexion.establecerConexion();
        try {
            PreparedStatement instruccion = conn.prepareStatement(sentenciaSQL);
            resultado = instruccion.executeUpdate();
            conexion.cerrarConexion();
            return resultado;
            } catch (SQLException ex) {
            System.err.println(
                        "Error de Base de Datos tratando de limpiar la Tabla : " + p_nombre_tabla + ".\n"+
                        "Método: "+getNombreMetodo()+"\n"+
                        "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                        "Mensaje de error(SQL): " + ex.getMessage()+"\n"
            );
            return 0;
        }
    }
}
