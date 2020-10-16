package com.example.demo.main;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class entity {
    private String id;
    @FieldMeta(name = "名字")
    private  String name;
    @FieldMeta(name = "日期")

    private Date stime;
}
