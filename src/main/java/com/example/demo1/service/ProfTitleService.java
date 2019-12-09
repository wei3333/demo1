package com.example.demo1.service;


import com.example.demo1.dao.ProfTitleDao;
import com.example.demo1.domin.ProfTitle;

import java.sql.SQLException;
import java.util.Collection;

public class ProfTitleService {
    private static ProfTitleDao profTitleDao = ProfTitleDao.getInstance();
    private static ProfTitleService profTitleService = new ProfTitleService();
    private ProfTitleService(){}

    public static ProfTitleService getInstance(){
        return profTitleService;
    }


    public Collection<ProfTitle> findAll() throws SQLException {
        return profTitleDao.findAll();
    }

    public ProfTitle find(Integer id) throws SQLException {
        return profTitleDao.find(id);
    }

    public boolean delete(Integer id) throws SQLException {
        return profTitleDao.delete(id);
    }


    public boolean add(ProfTitle profTitle) throws SQLException {
        return profTitleDao.add(profTitle);
    }

    public boolean update(ProfTitle profTitle) throws SQLException{
        return profTitleDao.update(profTitle);
    }
}
