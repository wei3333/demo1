package com.example.demo1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo1.domin.User;
import com.example.demo1.helper.JSONUtil;
import com.example.demo1.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

@RestController
public class UserController {
    @RequestMapping(value = "/user.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str,
                       @RequestParam(value = "username",required = false) String username,
                       @RequestParam(value = "password",required = false) String password)
            throws JSONException {
        JSONObject message = new JSONObject();
        String users_json = null;
        User user = null;
        try {
            if (id_str == null) {
                user = UserService.getInstance().login(username, password);
            }else {
                Integer id = Integer.parseInt(id_str);
                user = UserService.getInstance().find(id);
            }
            users_json = JSON.toJSONString(user, SerializerFeature.DisableCircularReferenceDetect);
            return users_json;
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            return message.toString();
        }catch(Exception e){
            message.put("message", "网络异常");
            return message.toString();
        }
    }

    @RequestMapping(value = "/user.ctl",method = RequestMethod.PUT)
    public JSONObject put(HttpServletRequest request) throws IOException {
        String user_json = JSONUtil.getJSON(request);
        //将JSON字串解析为User对象
        User userToUpdate = JSON.parseObject(user_json, User.class);
        JSONObject message = new JSONObject();
        //修改密码
        try {
            boolean updated = UserService.getInstance().changePassword(userToUpdate);
            if (updated){
                message.put("message","修改成功");
            }else {
                message.put("message","未能修改");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message","数据库操作异常");
        }
        return message;
    }
}
