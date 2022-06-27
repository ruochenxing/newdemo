package com.exceldemo.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.exceldemo.entity.User;

@Controller
public class Demo2Controller {
	@RequestMapping("/index1")
	public String index1(Model model) {
		/**
		 * 1、获取单个值 2、能够在html页面进行遍历提示 3、如何在html页面转义html代码块
		 */
		model.addAttribute("msg", "传输单个字符串");
		List<User> userList = new ArrayList<User>();
		userList.add(new User(1, "zs", "123"));
		userList.add(new User(2, "ls", "456"));
		userList.add(new User(3, "ww", "789"));
		model.addAttribute("userList", userList);
		// 存储前端html代码
		model.addAttribute("html", "<h1>7788</h1>");
		return "index1";
	}
}