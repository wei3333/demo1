package com.example.demo1.dao;



import com.example.demo1.domin.Degree;
import com.example.demo1.domin.Department;
import com.example.demo1.domin.ProfTitle;
import com.example.demo1.domin.Teacher;
import com.example.demo1.helper.JdbcHelper;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public final class TeacherDao {
    private static TeacherDao teacherDao = new TeacherDao();

    private TeacherDao() {
    }

    public static TeacherDao getInstance() {
        return teacherDao;
    }

    public Set<Teacher> findAll() throws SQLException {
        Set<Teacher> teachers = new HashSet<Teacher>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from teacher");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()) {
            //以当前记录中的id,name值为参数，获得degree_id,department_id,proftitle_id,创建Teacher对象
            ProfTitle profTitle = ProfTitleDao.getInstance().find(resultSet.getInt("proftitle_id"));
            Degree degree = DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
            Department department = DepartmentDao.getInstance().find(resultSet.getInt("department_id"));

            //创建Teacher对象，根据遍历结果中的id,name,degree_id,department_id,proftitle_id值
            Teacher teacher = new Teacher(resultSet.getInt("id"), resultSet.getString("no"), resultSet.getString("name"), profTitle, degree, department);
            teachers.add(teacher);
        }
        //关闭资源
        JdbcHelper.close(resultSet, statement, connection);
        return teachers;
    }

    public Teacher find(Integer id) throws SQLException {
        Teacher teacher = null;
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM teacher where id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()) {
            Degree degree = DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
            Department department = DepartmentDao.getInstance().find(resultSet.getInt("department_id"));
            ProfTitle profTitle = ProfTitleDao.getInstance().find(resultSet.getInt("proftitle_id"));
            //创建Degree对象，根据遍历结果中的id,name,degree_id,department_id,proftitle_id值
            teacher = new Teacher(resultSet.getInt("id"), resultSet.getString("no"), resultSet.getString("name"), profTitle, degree, department);
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return teacher;
    }

    public boolean update(Teacher teacher) throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String addSchool_sql = "update teacher set name=?,no=?,proftitle_id=?,degree_id=?,department_id=? where id=?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(addSchool_sql);
        //为预编译的语句参数赋值
        pstmt.setString(1, teacher.getName());
        pstmt.setString(2, teacher.getNo());
        pstmt.setInt(3, teacher.getTitle().getId());
        pstmt.setInt(4, teacher.getDegree().getId());
        pstmt.setInt(5, teacher.getDepartment().getId());
        pstmt.setInt(6, teacher.getId());
        //执行预编译对象的executeUpdate()方法，获取增加记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        //System.out.println("修改了 "+affectedRowNum+" 条");
        return affectedRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean affected = false;
        try {
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("SELECT * FROM user where teacher_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                UserDao.getInstance().delete(resultSet.getInt("id"));
            }
            //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM teacher WHERE ID =?");
            //为预编译的语句参数赋值
            pstmt.setInt(1, id);
            //执行预编译对象的executeUpdate()方法，获取增加记录的行数
            int affectedRowNum = pstmt.executeUpdate();
            connection.commit();
            affected = (affectedRowNum > 0);
        } catch (Exception e) {
            try {
                //回滚当前连接所做的操作
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                //回复自动提交
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return affected;
    }

    public static boolean add(Teacher teacher) {
        //获取数据库连接对象
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean affected = false;
        try {
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            //添加预编译语句
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO teacher (name, no,proftitle_id, degree_id, department_id) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, teacher.getName());
            preparedStatement.setString(2, teacher.getNo());
            preparedStatement.setInt(3, teacher.getTitle().getId());
            preparedStatement.setInt(4, teacher.getDegree().getId());
            preparedStatement.setInt(5, teacher.getDepartment().getId());
            //执行预编译对象的executeUpdate()方法，获得删除行数
            int affectedTeacherRowNum = preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("select id from teacher where no=?");
            preparedStatement.setString(1, teacher.getNo());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                teacher.setId(resultSet.getInt("id"));
            }
            preparedStatement = connection.prepareStatement("insert into user(username, password, teacher_id) values(?,?,?)");
            preparedStatement.setString(1,teacher.getNo());
            preparedStatement.setString(2,teacher.getNo());
            preparedStatement.setInt(3,teacher.getId());
            int affectedUserRowNum = preparedStatement.executeUpdate();
            if(affectedTeacherRowNum > 0 && affectedUserRowNum > 0){
                affected = true;
            }
            connection.commit();
        } catch (Exception e) {
            try {
                //回滚当前连接所做的操作
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                //回复自动提交
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            JdbcHelper.close(preparedStatement,connection);
        }
        return affected;
    }

}

