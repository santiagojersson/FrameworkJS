package co.usa.edu.adf.FrameworkJS;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class DatabaseJS {

    //JDBC driver name and database URL
    private String JDBC_driver = "com.mysql.jdbc.Driver";
    private String DB_url = "";

    //Database credentials
    private String USER = "";
    private String PASS = "";
    private Connection conn = null;
    private Statement stmt = null;

    public void createConnection(String url, String user, String password) {
        DB_url = url;
        USER = user;
        PASS = password;
        try {

            System.out.println("[INFO] Connecting to database...");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + DB_url + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                    USER,
                    PASS);
            System.out.println("[INFO] Creating statement...");
            stmt = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Object> CreateSelectAll(Class<?> objeto) {

        try {
            String query = "SELECT * FROM " + objeto.getSimpleName();
            ResultSet rs = stmt.executeQuery(query);
            Field[] fields = objeto.getDeclaredFields();
            Method metodo = null;
            ArrayList<Object> objects = new ArrayList();
            while (rs.next()) {
                Object c = objeto.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    String campo = fields[i].getName();
                    //System.out.println("campo: "+campo);
                    Object parametro = rs.getObject(campo);
                    metodo = objeto.getMethod("set" + capitalize(campo), fields[i].getType());
                    metodo.invoke(c, casteoObjeto(fields[i].getType(), parametro.toString()));
                }
                objects.add(c);

            }
            return objects;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insert(Object objeto) {
        try {
            boolean b = false;
            Class cls = objeto.getClass();
            Field[] fields = cls.getDeclaredFields();
            String valor = "";
            for (int i = 0; i < fields.length; i++) {

                if (fields[i].getType().equals(boolean.class)) {
                    Method get = cls.getMethod("is" + capitalize(fields[i].getName()));
                    valor =valor+ get.invoke(objeto) + "";
                } else if (fields[i].getType().equals(java.util.Date.class)) {
                    Method get = cls.getMethod("get" + capitalize(fields[i].getName()));
                    valor = valor+"'"+get.invoke(objeto).toString()+"'";
                }else if(fields[i].getType().equals(String.class)){
                     Method get = cls.getMethod("get" + capitalize(fields[i].getName()));
                    valor = valor+"'"+get.invoke(objeto)+"'";
                }else {
                    Method get = cls.getMethod("get" + capitalize(fields[i].getName()));
                    valor = valor+get.invoke(objeto)+"";
                }
                if (i<fields.length-1) {
                    valor=valor+",";
                }
            }
            
            String consulta = "INSERT INTO " + cls.getSimpleName() + " VALUES"+"("+valor+")";
            //System.out.println(consulta);
            stmt.executeUpdate(consulta);
            b=true;
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    private String capitalize(String campo) {
        return Character.toUpperCase(campo.charAt(0)) + campo.substring(1);

    }

    private Object casteoObjeto(Class<?> type, String data) {

        try {
            Object temporal = null;
            ///System.out.println("data " + data);
            //System.out.println(data);
            if (type.getCanonicalName().equalsIgnoreCase("int")) {
                //System.out.println("data: "+data);
                temporal = Integer.parseInt(data.trim());
            } else if (type.getCanonicalName().equalsIgnoreCase("double")) {
                temporal = Double.parseDouble(data.trim());
            } else if (type.getCanonicalName().equalsIgnoreCase("boolean")) {
                temporal = Boolean.parseBoolean(data.trim());
            } else if (type.getCanonicalName().equalsIgnoreCase("java.lang.String")) {
                temporal = data;
            } else if (type.getCanonicalName().equalsIgnoreCase("java.util.Date")) {
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd");
                temporal = dt1.parse(data);
                //System.out.println(dt1.format(temporal));
            }
            return temporal;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
