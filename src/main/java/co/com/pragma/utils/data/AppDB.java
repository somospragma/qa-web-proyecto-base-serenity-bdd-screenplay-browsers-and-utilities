package co.com.pragma.utils.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppDB {
    static Logger logger=Logger.getLogger(AppDB.class.getName());
    private static ResultSet resultSet = null;
    private static Statement statement;
    private static boolean result = false;

    public AppDB(){
        // Write document why this constructor is empty
    }

    public static ResultSet executeSelect(String strQuery, Connection conexion) throws SQLException
    {
        try{
            statement = conexion.createStatement();
            resultSet = statement.executeQuery(strQuery);
        }catch(SQLException e){
            logger.log(Level.SEVERE,() -> "CONNECTION_FAILURE " + e.getMessage());
        }catch(Exception ex){
            logger.log(Level.SEVERE,() -> "DRIVER_NOT_FOUND " + ex.getMessage() );
        }
        return resultSet;
    }

    public static boolean executeUpdateOrDelete(String strQuery, Connection conexion)
    {
        try{
            statement = conexion.createStatement();
            statement.executeUpdate(strQuery);
            result = true;
        }catch(SQLException e){
            logger.log(Level.SEVERE,() -> "CONNECTION_FAILURE " + e.getMessage());
            result = false;
        }catch(Exception ex){
            logger.log(Level.SEVERE,() -> "DRIVER_NOT_FOUND " + ex.getMessage());
        }
        return result;
    }

    public static Map<String,String> fillHashWithResultSetRecord(ResultSet resultSet) throws SQLException {

        HashMap<String,String> hashMap = new HashMap<>();

        if(resultSet != null) {
            while(resultSet.next()) {
                for(int i=1;i<=resultSet.getMetaData().getColumnCount();i++){
                    String nombreCampo = resultSet.getMetaData().getColumnName(i);
                    String valorCampo = resultSet.getObject(i)== null?"":resultSet.getObject(i).toString();
                    hashMap.put(nombreCampo, valorCampo);
                }
            }
        }
        return hashMap;
    }

    public static List<Map<String,String>> fillHashWithResultSetList(ResultSet resultSet) throws SQLException {

        List<Map<String,String>> listRecords = new ArrayList<>();

        if(resultSet != null) {

            List<String> columnNames = new ArrayList<>();
            for (int i=1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                String columnName = resultSet.getMetaData().getColumnName(i);
                columnNames.add(columnName);
            }
            while(resultSet.next()) {
                //con LinkedHashMap mantenemos el orden en que se agregaron
                HashMap<String,String> hashMap = new LinkedHashMap<>();
                for(String columnName : columnNames){
                    String valorCampo = resultSet.getObject(columnName)== null?"":resultSet.getObject(columnName).toString();
                    hashMap.put(columnName, valorCampo);
                }
                listRecords.add(hashMap);
            }
        }
        return listRecords;
    }


}
