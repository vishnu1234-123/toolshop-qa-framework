package com.vishnu.toolshop.db;

import com.vishnu.toolshop.config.ConfigReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DbClient {

    private static String url(){
        return "jdbc:mariadb://"+ ConfigReader.get("db.host")+":"+ConfigReader.get("db.port")+"/"+ConfigReader.get("db.name");
    }

    private static Connection connect() throws SQLException{
        return DriverManager.getConnection(url(),ConfigReader.get("db.user"),ConfigReader.get("db.password"));
    }

    public static List<Map<String,Object>> query(String sql,Object... params) {
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                List<Map<String, Object>> rows = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int c = 1; c <= meta.getColumnCount(); c++) {
                        row.put(meta.getColumnLabel(c), rs.getObject(c));
                    }
                    rows.add(row);
                }
                return rows;
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed: " + sql + " -> " + e.getMessage(), e);
        }
    }


    public static Object scalar(String sql,Object... params){
        List<Map<String,Object>> rows=query(sql,params);
        return rows.isEmpty() ? null : rows.get(0).values().iterator().next();
    }

    public static  int execute(String sql,Object... params){
        String host=ConfigReader.get("db.host");
        if (!host.equals("localhost") && !host.equals("127.0.0.1")){
            throw new IllegalStateException("Refusing to modify a non-local database: "+host);
        }

        try(Connection conn=connect();PreparedStatement ps=conn.prepareStatement(sql)){
            for(int i=0;i<params.length;i++){
                ps.setObject(i+1,params[i]);
            }

            return ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("DB update failed: "+sql+" -> "+e.getMessage(),e);
        }


    }

}
