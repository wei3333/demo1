package com.example.demo1.service;


import com.example.demo1.dao.DegreeDao;
import com.example.demo1.domin.Degree;

import java.sql.SQLException;
import java.util.Collection;

public final class DegreeService {
    private static DegreeDao degreeDao = DegreeDao.getInstance();
    private static DegreeService degreeService = new DegreeService();
    private DegreeService(){}

    public static DegreeService getInstance(){
        return degreeService;
    }


    public Collection<Degree> findAll() throws SQLException{
        return degreeDao.findAll();
    }

    public Degree find(Integer id) throws SQLException {
        return degreeDao.find(id);
    }

    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        return degreeDao.delete(id);
    }


    public boolean add(Degree degree) throws SQLException, ClassNotFoundException {
        return degreeDao.add(degree);
    }


    public boolean update(Degree degree) throws SQLException{
        return degreeDao.update(degree);
    }
}
