package com.example.demo1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo1.domin.Department;
import com.example.demo1.helper.JSONUtil;
import com.example.demo1.service.DepartmentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class DepartmentController {
    @RequestMapping(value = "/department.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str) throws JSONException {
        JSONObject message = new JSONObject();
        try{
            if (id_str == null){
                return responseDepartments();
            }else {
                return responseDepartment(Integer.parseInt(id_str));
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
            return message.toString();
        }catch (Exception e){
            message.put("message","其他异常");
            e.printStackTrace();
            return message.toString();
        }
    }

    @RequestMapping(value = "/department.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String department_json = JSONUtil.getJSON(request);
        Department departmentToAdd = JSON.parseObject(department_json,Department.class);
        try{
            boolean added = DepartmentService.getInstance().add(departmentToAdd);
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

    @RequestMapping(value = "/department.ctl",method = RequestMethod.PUT)
    public JSONObject put(HttpServletRequest request) throws IOException{
        String department_json = JSONUtil.getJSON(request);
        Department departmentToUpdate = JSON.parseObject(department_json,Department.class);
        JSONObject message = new JSONObject();
        try{
            boolean updated = DepartmentService.getInstance().update(departmentToUpdate);
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

    @RequestMapping(value = "/department.ctl",method = RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false) String id_str){
        JSONObject message = new JSONObject();
        try{
            Integer id = Integer.parseInt(id_str);
            boolean deleted = DepartmentService.getInstance().delete(id);
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

    //响应一个专业对象
    private String responseDepartment(int id)
            throws SQLException {
        //根据id查找专业
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);
        return department_json;
    }
    //响应所有专业对象
    private String responseDepartments()
            throws SQLException {
        //获得所有专业
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        String department_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        return department_json;
    }
}
