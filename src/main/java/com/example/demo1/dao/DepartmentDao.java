package com.example.demo1.dao;



import com.example.demo1.domin.Department;
import com.example.demo1.domin.School;
import com.example.demo1.helper.JdbcHelper;
import com.example.demo1.service.SchoolService;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DepartmentDao {
    private static DepartmentDao departmentDao = new DepartmentDao();
    private DepartmentDao() {
    }

    public static DepartmentDao getInstance() {
        return departmentDao;
    }

    public Collection<Department> findAll() throws SQLException{
        Set<Department> departments = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from department");
        Department department = null;

        while(resultSet.next()) {
            School school = SchoolService.getInstance().find(resultSet.getInt("school_id"));
            department = new Department(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"),
                    school);
            departments.add(department);
        }
        //关闭资源
        JdbcHelper.close(resultSet,stmt,connection);
        return departments;
    }

    public Collection<Department>findAllBySchool(Integer schoolId) throws SQLException{
        Set<Department>departments = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        PreparedStatement pstmt = connection.prepareStatement("select * from department where school_id = ?");
        pstmt.setInt(1,schoolId);
        ResultSet resultSet = pstmt.executeQuery();
        Department department = null;
        while(resultSet.next()){
            School school = SchoolService.getInstance().find(resultSet.getInt("school_id"));
            department = new Department(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"),
                    school);
            departments.add(department);
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return departments;
    }

    public Department find(Integer id) throws SQLException{
        Department department = null;
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM department WHERE id=?");
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
            department = new Department(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"),
                    school);
        }
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return department;
    }

    public static boolean update(Department department) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        PreparedStatement pstmt = connection.prepareStatement("update department set description=?,no=?,remarks=?,school_id=? where id = ?");
        pstmt.setString(1, department.getDescription());
        pstmt.setString(2, department.getNo());
        pstmt.setString(3, department.getRemarks());
        pstmt.setInt(4,department.getSchool().getId());
        pstmt.setInt(5,department.getId());
        int affectedRows = pstmt.executeUpdate();
        JdbcHelper.close(pstmt,connection);
        return affectedRows > 0;
    }


    public static boolean add(Department department) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement pstmt = connection.prepareStatement("insert into department(description,no,remarks,school_id) values " +
                "(?,?,?,?)");
        pstmt.setString(1, department.getDescription());
        pstmt.setString(2, department.getNo());
        pstmt.setString(3, department.getRemarks());
        pstmt.setInt(4,department.getSchool().getId());
        int affectedRowNum = pstmt.executeUpdate();
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM department WHERE id = ?"
        );
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        int affectedROWNUM = preparedStatement.executeUpdate();
        return affectedROWNUM > 0;
    }

}
