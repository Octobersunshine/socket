package com.example.demo.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class MytestController {

    @Autowired
    CompareObjectUtils compareObjectUtils;

    @GetMapping("/get")
    public void getMessage()  throws IllegalAccessException, ClassNotFoundException
    {

        entity oldModel = new  entity();
        oldModel.setId("1");
        oldModel.setName("张三");

        // 模拟新数据
        entity model = new  entity();
        model.setId("2");
        model.setName("李四");


        List<Map<String, Object>> list = CompareObjectUtils.compareTwoClass(oldModel,model);


        String content = "";  // 定义变更字符串
        for(Map<String, Object> map : list){
            if (map.get("old") == null) map.put("old","无");
            content += map.get("name") + ":" + map.get("old") + " 变更为 " + map.get("new") + ";";
        }

    }
}
