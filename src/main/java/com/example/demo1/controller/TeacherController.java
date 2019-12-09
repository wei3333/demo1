package com.example.demo1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo1.domin.Teacher;
import com.example.demo1.helper.JSONUtil;
import com.example.demo1.service.TeacherService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class TeacherController {
    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str) throws JSONException {
        JSONObject message = new JSONObject();
        try{
            if (id_str == null){
                return responseTeachers();
            }else {
                return responseTeahcer(Integer.parseInt(id_str));
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            return message.toString();
        }catch (Exception e){
            message.put("message","其他异常");
            return message.toString();
        }
    }

    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String teacher_json = JSONUtil.getJSON(request);
        Teacher teacherToAdd = JSON.parseObject(teacher_json,Teacher.class);
        try{
            boolean added = TeacherService.getInstance().add(teacherToAdd);
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

    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.PUT)
    public JSONObject put(HttpServletRequest request) throws IOException{
        String teacher_json = JSONUtil.getJSON(request);
        Teacher teacherToUpdate = JSON.parseObject(teacher_json,Teacher.class);
        JSONObject message = new JSONObject();
        try{
            boolean updated = TeacherService.getInstance().update(teacherToUpdate);
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

    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false) String id_str){
        JSONObject message = new JSONObject();
        try{
            Integer id = Integer.parseInt(id_str);
            boolean deleted = TeacherService.getInstance().delete(id);
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

    //响应一个教师对象
    private String responseTeahcer(int id)
            throws SQLException {
        //根据id查找教师
        Teacher teacher = TeacherService.getInstance().find(id);
        String teacher_json = JSON.toJSONString(teacher);
        return teacher_json;
    }
    //响应所有教师对象
    private String responseTeachers()
            throws SQLException {
        //获得所有教师
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        String teacher_json = JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);
        return teacher_json;
    }
}
