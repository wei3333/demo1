package com.example.demo1.dao;


import com.example.demo1.domin.Teacher;
import com.example.demo1.domin.User;
import com.example.demo1.helper.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class UserDao {
    private static UserDao userDao = new UserDao();

    private UserDao() {
    }

    public static UserDao getInstance() {
        return userDao;
    }

    private static Collection<User> users;

    public Collection<User> findAll() throws SQLException {
        Set<User> users = new HashSet<User>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from user");
        while (resultSet.next()) {
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
            User user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    teacher);
            users.add(user);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        return users;
    }

    public User find(Integer id) throws SQLException {
        User desiredUser = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user where id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
            desiredUser = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    teacher);
        }
        return desiredUser;
    }

    public User findByUsername(String username) throws SQLException {
        User desiredUser = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user where username = ?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
            desiredUser = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    teacher);
        }
        return desiredUser;
    }

    public User login(String username, String password) throws SQLException{
        User user = null;
        Connection connection = JdbcHelper.getConn();
        PreparedStatement pstmt = connection.prepareStatement("select * from user where username = ? and password = ?");
        pstmt.setString(1,username);
        pstmt.setString(2,password);
        ResultSet resultSet = pstmt.executeQuery();
        if(resultSet.next()){
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    TeacherDao.getInstance().find(resultSet.getInt("teacher_id")));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return user;
    }

    public boolean changePassword(User user)throws SQLException{
        Connection connection = JdbcHelper.getConn();
        PreparedStatement pstmt = connection.prepareStatement("update user set password = ? where id = ?");
        pstmt.setString(1,user.getPassword());
        pstmt.setInt(2,user.getId());
        int affectedRowNum = pstmt.executeUpdate();
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement("delete from user where id=?");
        //为预编译的语句参数赋值
        pstmt.setInt(1, id);
        //执行预编译对象的executeUpdate()方法，获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        return affectedRowNum > 0;
    }

    private static void display(Collection<User> users) {
        for (User user : users) {
            System.out.println(user);
        }
    }


}