package com.example.demo1.dao;


import com.example.demo1.domin.ProfTitle;
import com.example.demo1.helper.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ProfTitleDao {
    private static ProfTitleDao profTitleDao=new ProfTitleDao();
    private ProfTitleDao(){}
    public static ProfTitleDao getInstance(){
        return profTitleDao;
    }
    private static Collection<ProfTitle> profTitles;

    public Set<ProfTitle> findAll() throws SQLException {
        Set<ProfTitle> profTitles = new HashSet<ProfTitle>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from proftitle");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            //以当前记录中的id,description,no,remarks值为参数，创建Degree对象
            ProfTitle profTitle = new ProfTitle(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"));
            //向degrees集合中添加Degree对象
            profTitles.add(profTitle);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        return profTitles;
    }

    public ProfTitle find(Integer id) throws SQLException{
        ProfTitle profTitle = null;
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM proftitle WHERE id=?");
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            profTitle = new ProfTitle(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"));
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return profTitle;
    }

    public boolean update(ProfTitle profTitle) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("update proftitle set description=?,no=?,remarks=? where id=?");
        //为预编译参数赋值
        preparedStatement.setString(1,profTitle.getDescription());
        preparedStatement.setString(2,profTitle.getNo());
        preparedStatement.setString(3,profTitle.getRemarks());
        preparedStatement.setInt(4,profTitle.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }

    public boolean add(ProfTitle profTitle) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO proftitle (description,no,remarks) VALUES (?,?,?)");
        //为预编译参数赋值
        preparedStatement.setString(1,profTitle.getDescription());
        preparedStatement.setString(2,profTitle.getNo());
        preparedStatement.setString(3,profTitle.getRemarks());
        //执行预编译语句，获取添加记录行数并赋值给affectedRowNum
        int affectedRowNum=preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM proftitle WHERE id=?");
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        //执行预编译语句，获取删除记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }

}
