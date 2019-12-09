package com.example.demo1.service;


import com.example.demo1.dao.DepartmentDao;
import com.example.demo1.domin.Department;

import java.sql.SQLException;
import java.util.Collection;

public final class DepartmentService {
    private static DepartmentDao departmentDao = DepartmentDao.getInstance();
    private static DepartmentService departmentService = new DepartmentService();
    private DepartmentService(){}

    public static DepartmentService getInstance(){
        return departmentService;
    }


    public Collection<Department> findAll() throws SQLException{
        return departmentDao.findAll();
    }

    public Collection<Department> findAllBySchool(Integer schoolId) throws SQLException{
        return departmentDao.findAllBySchool(schoolId);
    }

    public Department find(Integer id) throws SQLException{
        return departmentDao.find(id);
    }

    public boolean delete(Integer id) throws SQLException{
        return departmentDao.delete(id);
    }


    public boolean add(Department department)throws SQLException {
        return departmentDao.add(department);
    }

    public boolean update(Department department)throws SQLException {
        return departmentDao.update(department);
    }

}
