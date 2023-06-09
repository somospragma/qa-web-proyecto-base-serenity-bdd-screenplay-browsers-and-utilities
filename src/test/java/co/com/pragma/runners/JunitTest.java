package co.com.pragma.runners;


import co.com.pragma.utils.data.AppDB;
import co.com.pragma.utils.data.ConexionGestorDB;
import net.serenitybdd.screenplay.targets.Target;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static co.com.pragma.utils.UtilConstants.*;

public class JunitTest {

    @Test
    public void connectionBDTest() throws SQLException {
        Connection connection = ConexionGestorDB.util().crearConexionMySql(MYSQL_URL,MYSQL_USER,MYSQL_PASSWORD);
        ResultSet resultSet = AppDB.executeSelect("SELECT * FROM search_values WHERE id='1'",connection);
        HashMap<String,String> hashMap = (HashMap<String, String>) AppDB.fillHashWithResultSetRecord(resultSet);
        System.out.println(hashMap);
        ConexionGestorDB.util().closeConnection(connection);
    }

    @Test
    public void urlDBEnviroments() {
        System.out.println(MYSQL_URL);
    }

}
