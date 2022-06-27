package com.exceldemo.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.exceldemo.Contants;
import com.exceldemo.util.FileUtil;

@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model) {
        String currentPage = request.getParameter("currentPage");
        List<String> files = FileUtil.listDir(Contants.BASE_DIR);
        System.out.println(JSONObject.toJSONString(files));
        if (files != null && !files.isEmpty()) {
            model.addAttribute("details_file", files.get(files.size() - 1));
        } else {
            model.addAttribute("details_file", "details.txt");
        }
        if (currentPage == null || currentPage.equalsIgnoreCase("")) {
            currentPage = "1";
        }
        model.addAttribute("currentPage", Integer.valueOf(currentPage));
        return "index";
    }
}