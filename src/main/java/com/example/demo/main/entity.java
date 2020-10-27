package com.example.demo.main;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
@Component
@Data
public class entity {
    private String id;
    @FieldMeta(name = "名字")
    private  String name;
    @FieldMeta(name = "日期")
    private Date stime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        entity entity = (entity) o;
        return Objects.equals(getId(), entity.getId()) &&
                Objects.equals(getName(), entity.getName()) &&
                Objects.equals(getStime(), entity.getStime());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getStime());
    }
}
