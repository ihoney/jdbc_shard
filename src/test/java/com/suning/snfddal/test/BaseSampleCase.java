package com.suning.snfddal.test;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.After;

import com.suning.snfddal.jdbc.DispatcherDataSource;
import com.suning.snfddal.util.Utils;

public abstract class BaseSampleCase {

    protected DataSource dataSource;

    public BaseSampleCase() {
        try {
            String configLocation = "/config/ddal-config.xml";
            DispatcherDataSource dataSource = new DispatcherDataSource();
            dataSource.setConfigLocation(configLocation);
            dataSource.init();
            this.dataSource = dataSource;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @After
    public void disorty() {
        try {
            ((DispatcherDataSource) dataSource).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(Connection connection, Statement statement, ResultSet rs) {
        if (rs != null) {
            try {
                if (!rs.isClosed())
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                if (!statement.isClosed())
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                if (!connection.isClosed())
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public long getUUID() {
        UUID uuid = UUID.randomUUID();
        long murmurhash2_64 = Utils.murmurhash2_64(uuid.toString());
        murmurhash2_64 = Math.abs(murmurhash2_64);
        return murmurhash2_64;
    }

    public int nextOrderSeqVaule(Connection conn) throws SQLException {
        PreparedStatement ptmt = null;
        ResultSet rs = null;
        String sql = "SELECT nextval('order_seq')";
        try {
            conn = this.dataSource.getConnection();
            ptmt = conn.prepareStatement(sql);
            rs = ptmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            close(null, ptmt, rs);
        }
    }

    // public static void main(String[] args) throws ClassNotFoundException,
    // SQLException {
    // Class.forName("com.ibm.db2.jcc.DB2Driver");
    // Connection conn =
    // DriverManager.getConnection("jdbc:db2://10.19.250.15:60000/simpledb:currentSchema=DDAL;","db2inst1","FNU9r@bdq");
    // PreparedStatement ps =null;
    // ResultSet rSet = null;
    // try {
    // ps = conn.prepareStatement("select name from test where id=1");
    // rSet = ps.executeQuery();
    // while(rSet.next()){
    // System.out.println(rSet.getString(1)+"***********");
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally{
    // }
    // }
    //

}
