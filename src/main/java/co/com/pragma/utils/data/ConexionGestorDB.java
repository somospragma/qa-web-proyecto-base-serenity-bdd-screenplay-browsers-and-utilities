package co.com.pragma.utils.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionGestorDB {

    Logger logger=Logger.getLogger(ConexionGestorDB.class.getName());
    private Connection conexion;


    public static ConexionGestorDB util(){return new ConexionGestorDB();}

    public Connection getConnection() {
        return conexion;
    }

    public void setConnection(Connection con) {
        conexion = con;
    }


    public Connection crearConexionMySql(String strCon, String usr, String pwd) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(strCon, usr, pwd);
            logger.log(Level.FINE, "Connection successful");
            return conexion;
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException("Falló la creación de la conexión con la BD de MySQL, error: "+ ex.getMessage());
        }
    }

    public void closeConnection( Connection connection){
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,() ->"CONNECTION_NOT_CLOSED " + e.getMessage());
        }
    }
}
