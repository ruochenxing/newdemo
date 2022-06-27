package com.exceldemo.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.exceldemo.entity.User;

@Controller
public class Demo1Controller {
    @RequestMapping("/demo1")
    public String demo1(Model model) {
        model.addAttribute("msg", "传输单个字符串");
        List<User> userList = new ArrayList<User>();
        userList.add(new User(1, "zs", "123"));
        userList.add(new User(2, "ls", "456"));
        userList.add(new User(3, "ww", "789"));
        model.addAttribute("userList", userList);
        return "demo1";
    }

    @RequestMapping("/demo2")
    public String demo2(Model model) {
        model.addAttribute("msg", "传输单个字符串");
        List<User> userList = new ArrayList<User>();
        userList.add(new User(1, "zs", "123"));
        userList.add(new User(2, "ls", "456"));
        userList.add(new User(3, "ww", "789"));
        model.addAttribute("userList", userList);
        return "demo2";
    }

    @RequestMapping("/demo3")
    public String demo3(Model model) {
        model.addAttribute("msg", "传输单个字符串");
        List<User> userList = new ArrayList<User>();
        userList.add(new User(1, "zs", "123"));
        userList.add(new User(2, "ls", "456"));
        userList.add(new User(3, "ww", "789"));
        model.addAttribute("userList", userList);
        return "amap_demo1";
    }
}