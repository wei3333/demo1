package com.example.demo1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo1.domin.School;
import com.example.demo1.helper.JSONUtil;
import com.example.demo1.service.SchoolService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class SchoolController {
    @RequestMapping(value = "/school.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str) throws JSONException {
        JSONObject message = new JSONObject();
        try{
            if (id_str == null){
                return responseSchools();
            }else {
                return responseSchool(Integer.parseInt(id_str));
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            return message.toString();
        }catch (Exception e){
            message.put("message","其他异常");
            return message.toString();
        }
    }

    @RequestMapping(value = "/school.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String school_json = JSONUtil.getJSON(request);
        School schoolToAdd = JSON.parseObject(school_json,School.class);
        try{
            boolean added = SchoolService.getInstance().add(schoolToAdd);
            if (added){
                message.put("message","添加成功");
            }else {
                message.put("message","未能添加");
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","其他异常");
            e.printStackTrace();
        }
        return message;
    }

    @RequestMapping(value = "/school.ctl",method = RequestMethod.PUT)
    public JSONObject put(HttpServletRequest request) throws IOException{
        String school_json = JSONUtil.getJSON(request);
        School schoolToUpdate = JSON.parseObject(school_json,School.class);
        JSONObject message = new JSONObject();
        try{
            boolean updated = SchoolService.getInstance().update(schoolToUpdate);
            if(updated){
                message.put("message","修改成功");
            }else {
                message.put("message","未能修改");
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","其他异常");
            e.printStackTrace();
        }
        return message;
    }

    @RequestMapping(value = "/school.ctl",method = RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false) String id_str){
        JSONObject message = new JSONObject();
        try{
            Integer id = Integer.parseInt(id_str);
            boolean deleted = SchoolService.getInstance().delete(id);
            if (deleted){
                message.put("message","删除成功");
            }else {
                message.put("message","未能删除");
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","其他异常");
            e.printStackTrace();
        }
        return message;
    }

    //响应一个学院对象
    private String responseSchool(int id)
            throws SQLException {
        //根据id查找学院
        School school = SchoolService.getInstance().find(id);
        String school_json = JSON.toJSONString(school);
        return school_json;
    }
    //响应所有学院对象
    private String responseSchools()
            throws SQLException {
        //获得所有学院
        Collection<School> schools = SchoolService.getInstance().findAll();
        String schools_json = JSON.toJSONString(schools, SerializerFeature.DisableCircularReferenceDetect);
        return schools_json;
    }
}
