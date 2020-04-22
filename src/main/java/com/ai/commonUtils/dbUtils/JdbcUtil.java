package com.ai.commonUtils.dbUtils;


import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lixuejun
 * @date: Create in 2019/10/17 下午11:10
 * @description:
 */
public class JdbcUtil {

    private Logger log = Logger.getLogger(JdbcUtil.class);

    public enum DBType{
        MYSQL,ORACLE
    }

    private static byte[] lock = new byte[0]; // 为了线程安全使用
    private JdbcUtil(){}
    private static JdbcUtil singletonInstance = null;
    public static JdbcUtil getInstance(){
        if (singletonInstance == null) {
            synchronized (lock){
                singletonInstance = new JdbcUtil();
            }
        }
        return singletonInstance;
    }

    private String mySqlDriver = "com.mysql.cj.jdbc.Driver";
    private String oracleDriver = "oracle.jdbc.driver.OracleDriver";

    private Connection conn = null;
    private Statement statement = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private ResultSetMetaData rsmd = null;
    private  Map<String,String> map = null;


    /**
     * 根据DBType 来判断连接哪个数据库
     * @param dbType
     * @param ip
     * @param port
     * @param dbName
     * @param uName
     * @param pwd
     */
    public Statement connectDB(DBType dbType,String ip,String port,String dbName,String uName,String pwd){
        switch (dbType){
            case ORACLE:
                String oracleURL = "jdbc:oracle:thin:@"+ip+":"+port+":"+dbName;
                try {
                    Class.forName(oracleDriver);
                    conn = DriverManager.getConnection(oracleURL, uName, pwd);
                    if (!conn.isClosed()) {
                        log.info("【成功】连接了Oracle数据库 :"+oracleURL);
                    }
                    statement = conn.createStatement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return statement;
            case MYSQL:
                String mySqlURL = "jdbc:mysql://"+ip+":"+port+"/"+ dbName+"?useUnicode=true&characterEncoding=utf8";
                try {
                    Class.forName(mySqlDriver).newInstance();
                    conn = DriverManager.getConnection(mySqlURL,uName,pwd);
                    if (!conn.isClosed()) {
                        log.info("【成功】连接MySQL数据库:"+mySqlURL);
                    }
                    statement = conn.createStatement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return statement;
        }
        return statement;
    }

    /*
     * 关闭数据库
     */
    public void closeDB() {
        if(rs != null ){
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
                log.info("【成功】数据库连接中断");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询数据库表，遍历返回的List<Map<String,Object>>，给出key,获取value,设置了boolean开关，true=为自动关闭，false为手动关闭
     * @param sql
     * @param mapKey
     * @return
     */
    public List <String> query(String sql,String mapKey,boolean isAutoCloseDB){
        List<String> mapVaule = new ArrayList<String>();
        try {
            List<Map<String,Object>> list = resultList(sql);
            System.out.println("刚取出来的结果size()="+list.size());
            for(Map<String,Object> map: list){
                for(Map.Entry<String,Object> m : map.entrySet()){
                    if(mapKey.equalsIgnoreCase(m.getKey())){
                        mapVaule.add(m.getValue().toString());
                    }
                }
            }
            log.info("数据库表中字段【"+mapKey+"】对应column值为："+mapVaule);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (isAutoCloseDB){
                closeDB();
            }else {
                log.info("【警告】数据库还没有关闭");
            }
        }
        return mapVaule;
    }

    /**
     * 私有方法 获取List<Map<String,Object>> sql结果结合
     * @param sql
     * @return
     */
    private  List<Map<String,Object>> resultList(String sql){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();//返回所有记录
        int count;
        try {
            rs = statement.executeQuery(sql);
            log.info("执行sql语句【"+sql+"】");
            ResultSetMetaData rsmd;
            rsmd = rs.getMetaData();
            count = rsmd.getColumnCount();
            while(rs.next()){
                Map  map = new HashMap();
                for(int i=1;i<=count;i++){
                    //获取指定列的表目录名称
                    String label=rsmd.getColumnLabel(i);
                    //以 Java 编程语言中 Object 的形式获取此 ResultSet 对象的当前行中指定列的值
                    Object object= rs.getObject(i);
                    //把数据库中的字段名和值对应为一个map对象中的一个键值对
                    map.put(label.toLowerCase(), object);
                }
                list.add(map);
            }
            log.info("SQL语句【"+sql+"】返回的数据："+list.toString());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
//        LOGGER.info("执行sql语句【"+sql+"】返回查询结果："+list.toString());
        return list;
    }

    /**
     * 查询数据表，返回List<Map<String,Object>>集合，如果isAutoCloseDB 为true则自动关闭数据库连接
     * @param sql
     * @param isAutoCloseDB
     * @return
     */
    public List query(String sql,boolean isAutoCloseDB){
        List list = new ArrayList();//返回所有记录
        int count;
        try {
            rs = statement.executeQuery(sql);
            log.info("执行sql语句【"+sql+"】");
            ResultSetMetaData rsmd;
            rsmd = rs.getMetaData();
            count = rsmd.getColumnCount();
            while(rs.next()){
                Map  map = new HashMap();
                for(int i=1;i<=count;i++){
                    //获取指定列的表目录名称
                    String label=rsmd.getColumnLabel(i);
                    //以 Java 编程语言中 Object 的形式获取此 ResultSet 对象的当前行中指定列的值
                    Object object= rs.getObject(i);
                    //把数据库中的字段名和值对应为一个map对象中的一个键值对
                    map.put(label.toLowerCase(), object);
                }
                list.add(map);
            }
            log.info("SQL语句【"+sql+"】返回的数据："+list.toString());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(isAutoCloseDB){
                closeDB();
            }else {
                log.info("【警告】数据库还没有关闭");
            }
        }
        return list;
    }

    /**
     * 删除数据库操作
     * @param sql
     * @param isAutoCloseDb
     * @return
     */
    public boolean deleteFrom(Statement statement,String sql, boolean isAutoCloseDb){
        boolean delete = false;
        try {
            delete = statement.execute(sql);
            log.info(String.format("执行sql语句[%s],且该语句返回结果为[%s]",sql,delete));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!delete) {
            log.info(String.format("由于语句[%s]执行为结果为[%s],所以返回false代表更新数",sql,delete));
            return false;
        }
        if (isAutoCloseDb){
            closeDB();
        }
        return true;
    }

    public int update(Statement statement,String sql,boolean isAutoCloseDB){
        int update = 0;
        try {
            update = statement.executeUpdate(sql);
            log.info(String.format("执行sql语句[%s],且该语句返回结果为[%s]",sql,update));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (update !=0) {
            log.info(String.format("由于语句[%s]执行为结果为[%s]",sql,update));
            return update;
        }
        if(isAutoCloseDB){
            closeDB();
        }
        return update;
    }

}
