
package com.aulah.xutilidades;

import com.aulah.modelo.Conexion;
import static com.aulah.modelo.Configurar.getNombreMetodo;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GuardarCentro {
    
    /***
     * Guarda un Centro en la tabla tbl_centros
     */
    public int guardarCentro(String p_centro, String p_clave, String p_direccion) throws ClassNotFoundException {
        int estado;
        String sentenciaSQL = "INSERT INTO tbl_centros(codigo, centro, clave, direccion) VALUES(?,?,?,?)";
          try {
              Conexion conn = new Conexion();
              PreparedStatement ps = conn.establecerConexion().prepareStatement(sentenciaSQL);
              ps.setInt(1, 0);//Se envía 0, pero este valor es autoincrementado por la base de datos
              ps.setString(2, p_centro);
              ps.setString(3, p_clave);
              ps.setString(4, p_direccion);
              estado = ps.executeUpdate();
              conn.cerrarConexion();
              return estado;
          } catch (SQLException ex) {
              System.err.println("Error de Base de Datos tratando de guardar el Centro.\n"+
                "Método: "+getNombreMetodo()+"\n"+
                "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                "Mensaje de error(SQL): "+ex.getMessage()+"\n");
          }
         return 0;
    }
    
}
