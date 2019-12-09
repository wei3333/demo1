package com.example.demo1.service;


import com.example.demo1.dao.UserDao;
import com.example.demo1.domin.User;

import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
    private static UserDao userDao = UserDao.getInstance();
    private static UserService userService = new UserService();

    public UserService() {
    }

    public static UserService getInstance(){
        return UserService.userService;
    }

    public Collection<User> findAll() throws SQLException {
        return userDao.findAll();
    }

    public User find(Integer id) throws SQLException{
        return userDao.find(id);
    }

    public User findByUsername(String username) throws SQLException{
        return userDao.findByUsername(username);
    }
/**
    public boolean add(User user)throws SQLException{
        return userDao.add(user);
    }
**/
    public boolean changePassword(User user)throws SQLException{
        return userDao.changePassword(user);
    }

    public boolean delete(Integer id)throws SQLException{
        return userDao.delete(id);
    }

    public User login(String username, String password)throws SQLException{
        return userDao.login(username,password);
    }
}