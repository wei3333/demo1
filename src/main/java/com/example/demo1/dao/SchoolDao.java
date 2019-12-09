package com.example.demo1.dao;



import com.example.demo1.domin.School;
import com.example.demo1.helper.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SchoolDao {
    private static SchoolDao schoolDao = new SchoolDao();
    private static Collection<School> schools;

    private SchoolDao() {
    }

    public static SchoolDao getInstance() {
        return schoolDao;
    }

    public Collection<School> findAll() throws SQLException{
        Set<School> schools = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from school");
        while(resultSet.next()){
            //将数据库的数据加到集合中,显示在网页上
            School school = new School(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"));
            schools.add(school);
        }
        //关闭stmt对象
        stmt.close();
        //关闭connection对象。只要建立了连接,就必须关闭。
        connection.close();
        return schools;
    }

    public School find(Integer id) throws SQLException{
        School school = null;
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM school WHERE id=?");
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            school = new School(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"));
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return school;
    }


    public static boolean add(School school) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement pstmt = connection.prepareStatement("insert into school(no,description,remarks) values " +
                "(?,?,?)");
        pstmt.setString(1,school.getNo());
        pstmt.setString(2,school.getDescription());
        pstmt.setString(3,school.getRemarks());
        int affectedRowNum = pstmt.executeUpdate();
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM school WHERE id = ?"
        );
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        int affectedROWNUM = preparedStatement.executeUpdate();
        return affectedROWNUM > 0;
    }

    public boolean update(School school) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(" update school set description=?,no=?,remarks=? where id=?");
        //为预编译参数赋值
        preparedStatement.setString(1,school.getDescription());
        preparedStatement.setString(2,school.getNo());
        preparedStatement.setString(3,school.getRemarks());
        preparedStatement.setInt(4,school.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows > 0;
    }


    public boolean delete(School school){
        return schools.remove(school);
    }

}
