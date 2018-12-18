
package com.aulah.xutilidades;

import com.aulah.modelo.Conexion;
import static com.aulah.modelo.Configurar.getNombreMetodo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GuardaAlumno {
    private static String URL;
    private static String USUARIO;
    private static String PASSWORD;

    public GuardaAlumno() {
        Conexion con = new Conexion();
    }
    
    public GuardaAlumno(int codigo, String nombre, String rne, String sexo, String estado, byte cod_periodo, byte cod_semestre) throws ClassNotFoundException{
        //GuardarDatosAlumno(codigo, nombre, rne, sexo, estado, cod_curso, cod_seccion, cod_semestre, cod_periodo);
    }
    
    /***
     * Guarda un Alumno en la tabla tbl_alumnos
     */
    public int GuardarDatosAlumno(String p_nombre, String p_rne, String p_sexo, String p_estado, byte p_cod_periodo, byte p_cod_semestre, byte p_cod_jornada, int p_cod_curso, byte p_cod_seccion) throws ClassNotFoundException {
        int estado;
        String sentenciaSQL = "INSERT INTO tbl_alumnos VALUES(?,?,?,?,?,?,?,?,?)";
          try {
              Conexion conn = new Conexion();
              PreparedStatement ps = conn.getConnection().prepareStatement(sentenciaSQL);
              ps.setInt(1, 0);//Se envía 0, pero este valor es autoincrementado por la base de datos
              ps.setString(2, p_nombre);
              ps.setString(3, p_rne);
              ps.setString(4, p_sexo);
              ps.setString(5, p_estado);
              ps.setByte(6, p_cod_periodo);
              ps.setByte(7, p_cod_semestre);
              ps.setByte(8, p_cod_jornada);
              ps.setInt(9, p_cod_curso);
              ps.setByte(10, p_cod_seccion);
              estado = ps.executeUpdate();
              return estado;
          } catch (SQLException ex) {
              System.err.println("Error de Base de Datos tratando de guardar el Alumno.\n"+
                "Método: "+getNombreMetodo()+"\n"+
                "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                "Mensaje de error(SQL): "+ex.getMessage()+"\n");
          }
         return 0;
    }
}
