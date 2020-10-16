package com.example.demo.main;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.math.BigDecimal;
@SpringBootApplication
public class MyTest {

    @Autowired
    public CompareObjectUtils CompareObjectUtils;



    public static void main(String[] args) {

        SpringApplication.run(MyTest.class, args);
// 模
      
    }



}
