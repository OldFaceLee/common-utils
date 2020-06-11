package com.ai.commonUtils.dbUtils;


import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        ORACLE,MYSQL
    }

    // 为了线程安全使用
    private static byte[] lock = new byte[0];
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

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private ResultSetMetaData resultSetMetaData = null;


    /**
     * 根据DBType 来判断连接哪个数据库
     */
    public Statement connectDB(DBType dbType,String ip,String port,String dbName,String uName,String pwd){
        switch (dbType){
            case ORACLE:
                String oracleURL = "jdbc:oracle:thin:@"+ip+":"+port+":"+dbName;
                try {
                    Class.forName(oracleDriver);
                    connection = DriverManager.getConnection(oracleURL, uName, pwd);
                    if (!connection.isClosed()) {
                        log.info("【成功】连接了Oracle数据库 :"+oracleURL);
                    }
                    statement = connection.createStatement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MYSQL:
                String mySqlURL = "jdbc:mysql://"+ip+":"+port+"/"+ dbName+"?useUnicode=true&characterEncoding=utf8";
                try {
                    Class.forName(mySqlDriver).newInstance();
                    connection = DriverManager.getConnection(mySqlURL,uName,pwd);
                    if (!connection.isClosed()) {
                        log.info("【成功】连接MySQL数据库:"+mySqlURL);
                    }
                    statement = connection.createStatement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return statement;
    }

    /*
     * 关闭数据库
     */
    public void closeDB() {
        if(resultSet != null ){
            try {
                resultSet.close();
                log.info("释放resultSet资源");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement != null){
            try {
                statement.close();
                log.info("释放statement资源");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection != null){
            try {
                connection.close();
                log.info("【成功】数据库连接中断");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 这个适合select 指定的字段 from table,1V1
     * @param sql
     * @return
     */
    public Map<String,String> query(String sql,String field){
        log.info(String.format("执行数据库sql【%s】",sql));
        Map temMap = new HashMap();
        try {
            resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                String[] strs = field.split(",");
                for (int i = 0; i < strs.length; i++) {
                    temMap.put(strs[i],resultSet.getString(strs[i]));
                }
            }
            log.info("查询后的结果为："+temMap);
        } catch (SQLException throwables) {
            log.error("数据库查询失败",throwables);
            throwables.printStackTrace();
        }
        return temMap;
    }


    /**
     * 查询数据库表，遍历返回的List<Map<String,Object>>，给出key,获取value,设置了boolean开关，true=为自动关闭，false为手动关闭
     * @param sql
     * @param fieldName
     * @return
     */
    public List <String> query(String sql,String fieldName,boolean isAutoCloseDB){
        List<String> mapVaule = new ArrayList<String>();
        try {
            List<Map<String,Object>> list = resultList(sql);
            System.out.println("刚取出来的结果size()="+list.size());
            for(Map<String,Object> map: list){
                for(Map.Entry<String,Object> m : map.entrySet()){
                    if(fieldName.equalsIgnoreCase(m.getKey())){
                        if (m.getValue() == null) {
                            mapVaule.add(null);
                        }else {
                            mapVaule.add(m.getValue().toString());
                        }
                    }
                }
            }
            log.info("数据库表中字段【"+fieldName+"】对应column值为："+mapVaule);
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
        //返回所有记录
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        int count;
        try {
            resultSet = statement.executeQuery(sql);
            log.info("执行sql语句【"+sql+"】");
            ResultSetMetaData rsmd;
            rsmd = resultSet.getMetaData();
            count = rsmd.getColumnCount();
            while(resultSet.next()){
                Map  map = new HashMap();
                for(int i=1;i<=count;i++){
                    //获取指定列的表目录名称
                    String label=rsmd.getColumnLabel(i);
                    //以 Java 编程语言中 Object 的形式获取此 ResultSet 对象的当前行中指定列的值
                    Object object= resultSet.getObject(i);
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
            if(resultSet != null){
                try {
                    resultSet.close();
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
            resultSet = statement.executeQuery(sql);
            log.info("执行sql语句【"+sql+"】");
            ResultSetMetaData rsmd;
            rsmd = resultSet.getMetaData();
            count = rsmd.getColumnCount();
            while(resultSet.next()){
                Map  map = new HashMap();
                for(int i=1;i<=count;i++){
                    //获取指定列的表目录名称
                    String label=rsmd.getColumnLabel(i);
                    //以 Java 编程语言中 Object 的形式获取此 ResultSet 对象的当前行中指定列的值
                    Object object= resultSet.getObject(i);
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
            if(resultSet != null){
                try {
                    resultSet.close();
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

    public int insert(String sql){
        int insert = 0;
        try {
            insert = statement.executeUpdate(sql);
            log.info(String.format("执行sql语句[%s],且该语句返回结果为[%s]",sql,insert));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return insert;
    }

    public int insert(String tableName,Map<String,String> fieldValues){
        int insert = 0;
        String front = "insert into "+tableName+" (";
        String middle = ") values (";
        StringBuilder initFrontMiddle = new StringBuilder(front);
        for(Map.Entry<String,String> m : fieldValues.entrySet()){
            initFrontMiddle.append(m.getKey()).append(",");
        }
        String frontMiddle = initFrontMiddle.substring(0,initFrontMiddle.length()-1) + middle;

        StringBuilder end = new StringBuilder();
        for(Map.Entry<String,String> m : fieldValues.entrySet()){
            end.append("\"");
            end.append(m.getValue());
            end.append("\"").append(",");
        }
        String subEnd = end.substring(0,end.length()-1);
        String sqlFinal = frontMiddle + subEnd + ")";
        System.out.println(sqlFinal);
        try {
            insert = statement.executeUpdate(sqlFinal);
            log.info(String.format("执行sql语句[%s],且该语句返回结果为[%s]",sqlFinal,insert));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return insert;
    }

    /**
     * 转化为二维数组
     * @param tableName
     * @return
     */
    public Object[][] getTestData(String tableName,String field) throws ClassNotFoundException, SQLException {

        //创建一个结果集存放数据库执行完sql的数据
        resultSet = statement.executeQuery("select * from " + tableName);
        resultSetMetaData = resultSet.getMetaData();
        int colNum = resultSetMetaData.getColumnCount();
        //获取字段的名称
        List<String> colName = new ArrayList<>();
        for (int i = 1; i <= colNum; i++) {
            colName.add(resultSetMetaData.getColumnName(i));
        }
        //声明存放泛型string数组的list对象
        List<Object[]> list = new ArrayList<Object[]>();
        while (resultSet.next()) {
            String[] strings = new String[colNum];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = resultSet.getString(i+1);
            }
            list.add(strings);
        }
        System.out.println("=>"+list);
        resultSet.close();
        connection.close();
        //将list对象转换成二位数组
        Object[][] results = new Object[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            results[i] = list.get(i);
        }
        return results;
    }





    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JdbcUtil util = new JdbcUtil();
        util.connectDB(DBType.MYSQL,"localhost","3306","oldface","root","root");
        util.getTestData("gis_addr_compare","");
//        List<String> results= util.query("select * from gis_addr_compare","source_location",true);





    }


}
