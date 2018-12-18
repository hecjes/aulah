
package com.aulah.xutilidades;

import com.aulah.modelo.Conexion;
import static com.aulah.modelo.Configurar.getNombreMetodo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TraeDatos {
    private static String URL = "jdbc:mysql://localhost:3306/COLEGIO_BD";;
    private static String USUARIO = "root";
    private static String PASSWORD = "";
    
    //Constructor
    public TraeDatos() {
        Conexion con = new Conexion();
    }
    
    /***
     * Devuelve el contenido completo de una tabla
     * @param NOMBRE_TABLA
     */
    public ResultSet TraeDatosTabla(String nombre_tabla) throws ClassNotFoundException {
       Class.forName("org.apache.derby.jdbc.ClientDriver");
       String sentenciaSQL = "SELECT * FROM " + nombre_tabla;
          try {
              Connection conn = DriverManager.getConnection(URL,USUARIO,PASSWORD);
              conn.setSchema("HECTOR");
              Statement s = conn.createStatement();
              s.execute(sentenciaSQL);
              ResultSet rs = s.getResultSet();
              return rs;
          } catch (SQLException ex) {
              System.err.println("Error de Base de Datos tratando de obtener los datos de la tabla "+nombre_tabla+".\n"+
                "Método: "+getNombreMetodo()+"\n"+
                "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                "Mensaje de error(SQL): "+ex.getMessage()+"\n");
          }
         return null;
    }
    
    /***
     * Retorna el valor maximo del codigo de una tabla
     * @param NOMBRE_TABLA
     */
    public int TraeCodigoMaximoTabla(String nombre_tabla) throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String sentenciaSQL = "SELECT MAX(codigo) FROM " + nombre_tabla;
        int codigo=0;
          try {
              Connection conn = DriverManager.getConnection(URL,USUARIO,PASSWORD);
              conn.setSchema("HECTOR");
              Statement s = conn.createStatement();
              s.execute(sentenciaSQL);
              ResultSet rs = s.getResultSet();
              while(rs.next()){
                  codigo = rs.getInt(1);
              }
              return codigo;
          } catch (SQLException ex) {
              System.err.println("Error de Base de Datos tratando de obtener el codigo maximo de la tabla "+nombre_tabla+".\n"+
                "Método: "+getNombreMetodo()+"\n"+
                "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                "Mensaje de error(SQL): "+ex.getMessage()+"\n");
          }
         return 0;
    }
    
    /***
     * Devuelve el nombre de un Alumno
     * @param p_codigo_alumno
     */
    public static ResultSet TraeNombreAlumno(int p_codigo_alumno) throws ClassNotFoundException {
       Class.forName("org.apache.derby.jdbc.ClientDriver");
       String sentenciaSQL = "SELECT nombre FROM alumnos WHERE codigo = ?";
          try {
              Connection conn = DriverManager.getConnection(URL,USUARIO,PASSWORD);
              conn.setSchema("HECTOR");
              PreparedStatement ps = conn.prepareStatement(sentenciaSQL);
              ps.setInt(1,p_codigo_alumno);
              ResultSet rs = ps.executeQuery();
              return rs;
          } catch (SQLException ex) {
              System.err.println("Error de Base de Datos tratando de obtener el nombre del Alumno codigo "+p_codigo_alumno+".\n"+
                "Método: "+getNombreMetodo()+"\n"+
                "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                "Mensaje de error(SQL): "+ex.getMessage()+"\n");
          }
         return null;
    }
    
    /***
     * Devuelve el Periodo
     * @param p_codigo_periodo
     */
    public static ResultSet TraePeriodo(byte p_codigo_periodo) throws ClassNotFoundException {
       Class.forName("org.apache.derby.jdbc.ClientDriver");
       String sentenciaSQL = "SELECT periodo FROM periodos WHERE codigo = ?";
          try {
              Connection conn = DriverManager.getConnection(URL,USUARIO,PASSWORD);
              conn.setSchema("HECTOR");
              PreparedStatement ps = conn.prepareStatement(sentenciaSQL);
              ps.setByte(1,p_codigo_periodo);
              ResultSet rs = ps.executeQuery();
              return rs;
          } catch (SQLException ex) {
              System.err.println("Error de Base de Datos tratando de obtener el Periodo codigo "+p_codigo_periodo+".\n"+
                "Método: "+getNombreMetodo()+"\n"+
                "Sentencia SQL: \""+sentenciaSQL+"\"\n"+
                "Mensaje de error(SQL): "+ex.getMessage()+"\n");
          }
         return null;
    }
   
}