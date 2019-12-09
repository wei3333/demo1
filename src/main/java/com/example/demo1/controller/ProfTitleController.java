package com.example.demo1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo1.domin.ProfTitle;
import com.example.demo1.helper.JSONUtil;
import com.example.demo1.service.ProfTitleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class ProfTitleController {
    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str) throws JSONException {
        JSONObject message = new JSONObject();
        try{
            if (id_str == null){
                return responseProfTitles();
            }else {
                return responseProfTitle(Integer.parseInt(id_str));
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            return message.toString();
        }catch (Exception e){
            message.put("message","其他异常");
            return message.toString();
        }
    }

    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String profTitle_json = JSONUtil.getJSON(request);
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json,ProfTitle.class);
        try{
            boolean added = ProfTitleService.getInstance().add(profTitleToAdd);
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

    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.PUT)
    public JSONObject put(HttpServletRequest request) throws IOException{
        String profTitle_json = JSONUtil.getJSON(request);
        ProfTitle profTitleToUpdate = JSON.parseObject(profTitle_json,ProfTitle.class);
        JSONObject message = new JSONObject();
        try{
            boolean updated = ProfTitleService.getInstance().update(profTitleToUpdate);
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

    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false) String id_str){
        JSONObject message = new JSONObject();
        try{
            Integer id = Integer.parseInt(id_str);
            boolean deleted = ProfTitleService.getInstance().delete(id);
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

    //响应一个职称对象
    private String responseProfTitle(int id)
            throws SQLException {
        //根据id查找职称
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String profTitle_json = JSON.toJSONString(profTitle);
        return profTitle_json;
    }
    //响应所有职称对象
    private String responseProfTitles()
            throws SQLException {
        //获得所有职称
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().findAll();
        String profTitles_json = JSON.toJSONString(profTitles, SerializerFeature.DisableCircularReferenceDetect);
        return profTitles_json;
    }
}
