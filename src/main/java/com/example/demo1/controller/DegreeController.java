package com.example.demo1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo1.domin.Degree;
import com.example.demo1.helper.JSONUtil;
import com.example.demo1.service.DegreeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class DegreeController {
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str) throws JSONException {
        JSONObject message = new JSONObject();
        try{
            if (id_str == null){
                return responseDegrees();
            }else {
                return responseDegree(Integer.parseInt(id_str));
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            return message.toString();
        }catch (Exception e){
            message.put("message","其他异常");
            return message.toString();
        }
    }

    @RequestMapping(value = "/degree.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String degree_json = JSONUtil.getJSON(request);
        Degree degreeToAdd = JSON.parseObject(degree_json,Degree.class);
        try{
            boolean added = DegreeService.getInstance().add(degreeToAdd);
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

    @RequestMapping(value = "/degree.ctl",method = RequestMethod.PUT)
    public JSONObject put(HttpServletRequest request) throws IOException{
        String degree_json = JSONUtil.getJSON(request);
        Degree degreeToUpdate = JSON.parseObject(degree_json,Degree.class);
        JSONObject message = new JSONObject();
        try{
            boolean updated = DegreeService.getInstance().update(degreeToUpdate);
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

    @RequestMapping(value = "/degree.ctl",method = RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false) String id_str){
        JSONObject message = new JSONObject();
        try{
            Integer id = Integer.parseInt(id_str);
            boolean deleted = DegreeService.getInstance().delete(id);
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

    //响应一个学位对象
    private String responseDegree(int id)
            throws SQLException {
        //根据id查找学位
        Degree degree = DegreeService.getInstance().find(id);
        String degree_json = JSON.toJSONString(degree);
        return degree_json;
    }
    //响应所有学位对象
    private String responseDegrees()
            throws SQLException {
        //获得所有学位
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees, SerializerFeature.DisableCircularReferenceDetect);
        return degrees_json;
    }
}
