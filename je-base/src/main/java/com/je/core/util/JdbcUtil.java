package com.je.core.util;

import com.google.common.io.Resources;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.service.PCServiceTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * JDBC工具类
 *
 * @author marico
 * 2011-9-19 下午04:39:42
 */
public class JdbcUtil {

    private static Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    /**
     * 数据库链接
     */
    private Connection connection;
    private String driverName;
    private String url;
    private String username;
    private String password;

    private JdbcUtil(String driverName, String url, String username, String password) {
        this.driverName = driverName;
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            Class.forName(driverName);
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new PlatformException("数据库工具类获取链接异常", PlatformExceptionEnum.JE_CORE_UTIL_JDBC_CONNECT_ERROR, new Object[]{driverName, url, username, password}, e);
        }
    }

    /**
     * 得到指定数据库工具操作
     *
     * @return
     */
    public static JdbcUtil getInstance(String driverName, String url, String username, String password) {
        return new JdbcUtil(driverName, url, username, password);
    }

    /**
     * 得到当前数据库工具操作
     *
     * @return
     */
    public static JdbcUtil getInstance() {
        Properties pro = new Properties();
        try {
            URL url = Resources.getResource("jdbc.properties");
            pro.load(url.openStream());
            return new JdbcUtil(pro.getProperty("jdbc.driverClassName"), pro.getProperty("jdbc.url"), pro.getProperty("jdbc.username"), pro.getProperty("jdbc.password"));
        } catch (Exception e) {
            throw new PlatformException("数据库工具类获取系统链接异常", PlatformExceptionEnum.JE_CORE_UTIL_JDBC_SYSTEMCONNECT_ERROR, e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * 重新连接
     *
     * @return
     */
    public Connection resetConnection() {
        close();
        try {
            Class.forName(driverName);
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new PlatformException("数据库工具类获取链接异常", PlatformExceptionEnum.JE_CORE_UTIL_JDBC_CONNECT_ERROR, new Object[]{driverName, url, username, password}, e);
        }
        return this.connection;
    }

    /**
     * 关闭数据库连接(不会关闭当前connection)
     *
     * @param ps
     * @param rs
     */
    public static void close(ResultSet rs, Statement ps) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("查询结果集关闭异常", e);
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                logger.error("执行SQL语句的Statement关闭异常", e);
            }
        }
    }

    /**
     * 关闭数据库连接
     *
     * @param rs
     * @param ps
     * @param conn
     */
    public static void close(ResultSet rs, Statement ps, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("查询结果集关闭异常", e);
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                logger.error("执行SQL语句的Statement关闭异常", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("数据库链接关闭异常", e);
            }
        }
    }

    /**
     * 关闭当前连接
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("数据库链接关闭异常", e);
            }
        }
    }

    /**
     * 查询数据
     *
     * @param sql
     * @return
     */
    public List<HashMap> query(String sql) {
        Statement stmt = null;
        ResultSet rs = null;
        List<HashMap> lists = new ArrayList<HashMap>();
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData data = rs.getMetaData();
            while (rs.next()) {
                HashMap jb = new HashMap();
                int count = data.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    // 获得指定列的列名
                    String columnName = data.getColumnName(i);
                    Object value = rs.getObject(i);
                    if (value instanceof Timestamp) {
                        jb.put(columnName, rs.getTimestamp(columnName));
                        //这个是特殊处理SQLserver的image对象
                    } else if (value != null && "class [B".equals(value.getClass() + "")) {
//						jb.put(columnName, rs.getBinaryStream(columnName));
                        InputStream inputStram = rs.getBinaryStream(columnName);
                        jb.put(columnName, getByte(inputStram));
//						jb.put(columnName, rs.getBinaryStream(columnLabel));
                        //MySql使用   开始
                    } else if (value instanceof BigDecimal) {
                        jb.put(columnName, value);
                    } else if (value == null) {
                        jb.put(columnName, null);
                        //MySql使用   结束
                    } else if (value instanceof String) {
                        jb.put(columnName, value);
                    } else {
                        // 获得指定列的列值
                        String columnValue = rs.getString(i);
                        columnValue = columnValue == null ? "" : columnValue;
                        jb.put(columnName, columnValue);
                    }
                }
                lists.add(jb);
            }
        } catch (SQLException e) {
            throw new PlatformException("数据库工具类查询SQL异常", PlatformExceptionEnum.JE_CORE_UTIL_JDBC_QUERY_ERROR, new Object[]{sql, driverName, url, username, password}, e);
        } finally {
            close(rs, stmt);
        }
        return lists;
    }

    /**
     * 执行SQL语句
     *
     * @param sql
     * @return
     */
    public boolean executeSql(String sql) {
        Statement stmt = null;
        boolean flag = false;
        try {
            stmt = connection.createStatement();
            stmt.execute(sql);
            flag = true;
        } catch (SQLException e) {
            throw new PlatformException("数据库工具类执行SQL异常", PlatformExceptionEnum.JE_CORE_UTIL_JDBC_EXECUTE_ERROR, new Object[]{sql, driverName, url, username, password}, e);
        } finally {
            close(null, stmt);
        }
        return flag;
    }

    public static byte[] getByte(InputStream value) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        try {
            while ((rc = value.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new PlatformException("数据库工具类获取字节异常", PlatformExceptionEnum.JE_CORE_UTIL_JDBC_GETBYTE_ERROR, e);
        } finally {
            if (value != null) {
                try {
                    value.close();
                } catch (IOException e) {
                    throw new PlatformException("数据库工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_JDBC_CLOSEIO_ERROR, e);
                }
            }
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }
}
