package com.aulah.xutilidades;

import com.aulah.modelo.Conexion;
import static com.aulah.modelo.Configurar.getNombreMetodo;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class GuardarPeriodo {
    /***
     * Guarda un Periodo en la tabla tbl_periodos
     */
    public int guardarPeriodo(String p_periodo, Date p_fecha_inicio, Date p_fecha_final, int p_cod_centro, int p_cod_tipo_periodo) throws ClassNotFoundException {
        int estado;
        String sentenciaSQL = "INSERT INTO tbl_periodos(codigo, periodo, fecha_inicio, fecha_final, cod_centro, cod_tipo_periodo) VALUES(?,?,?,?,?,?)";
          try {
              Conexion conn = new Conexion();
              PreparedStatement ps = conn.establecerConexion().prepareStatement(sentenciaSQL);
              //PRUEBA
              System.out.println("Periodo "+p_periodo + "    Fecha ini " + p_fecha_inicio + "    fecha fin " + p_fecha_final);
              ps.setInt(1, 0);//Se envía 0, pero este valor es autoincrementado por la base de datos
              ps.setString(2, p_periodo);
              ps.setDate(3, p_fecha_inicio);
              ps.setDate(4, p_fecha_final);
              ps.setInt(5, p_cod_centro);
              ps.setInt(6, p_cod_tipo_periodo);
              estado = ps.executeUpdate();
              conn.cerrarConexion();
              return estado;
          } catch (SQLException ex) {
              System.err.println("Error de Base de Datos tratando de guardar el Periodo.\n"+
                "Método: "+getNombreMetodo()+"\n"+
                "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                "Mensaje de error(SQL): "+ex.getMessage()+"\n");
          }
         return 0;
    }
}
