package com.example.demo.main;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CompareObjectUtils{


    /**
     * 获取两个对象同名属性内容不相同的列表
     * @param class1 对象1
     * @param class2 对象2
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    public static List<Map<String, Object>> compareTwoClass(Object class1, Object class2) throws ClassNotFoundException, IllegalAccessException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //获取对象的class
        Class<?> clazz1 = class1.getClass();
        Class<?> clazz2 = class2.getClass();
        //获取对象的属性列表
        Field[] field1 = clazz1.getDeclaredFields();
        Field[] field2 = clazz2.getDeclaredFields();
        //遍历属性列表field1
        for (int i = 0; i < field1.length; i++) {
            if(field1[i].isAnnotationPresent(FieldMeta.class))
                //遍历属性列表field2
                for (int j = 0; j < field2.length; j++) {
                    //如果field1[i]属性名与field2[j]属性名内容相同
                    if (field1[i].getName().equals(field2[j].getName())) {
                        field1[i].setAccessible(true);
                        field2[j].setAccessible(true);
                        //如果field1[i]属性值与field2[j]属性值内容不相同
                        if (!compareTwo(field1[i].get(class1), field2[j].get(class2)) && field1[i].isAnnotationPresent(FieldMeta.class) && field2[j].isAnnotationPresent(FieldMeta.class)) {
                            FieldMeta metaAnnotation = field1[i].getAnnotation(FieldMeta.class);
                            Map<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("name", metaAnnotation.name());
                            map2.put("old", field1[i].get(class1) == null ? "" : field1[i].get(class1) );
                            map2.put("new", field2[j].get(class2));
                            //解决时间格式化问题-bean上加了@DateTimeFormat(pattern="yyyy-MM-dd")
                            if(field1[i].isAnnotationPresent(DateTimeFormat.class) && field2[j].isAnnotationPresent(DateTimeFormat.class) ){
                                String old = DateUtils.formatDateToString((Date) field1[i].get(class1),field1[i].getAnnotation(DateTimeFormat.class).pattern());
                                map2.put("old",old == null ? "": old);
                                map2.put("new", DateUtils.formatDateToString((Date) field2[j].get(class2),field2[j].getAnnotation(DateTimeFormat.class).pattern()));
                            }
                            list.add(map2);
                        }
                        break;
                    }
                }
        }
        return list;
    }

    //对比两个数据是否内容相同
    public static boolean compareTwo(Object object1, Object object2) {

        if (object1 == null && object2 == null) {
            return true;
        }
        // 因源数据是没有进行赋值，是null值，改为""。
        //if (object1 == "" && object2 == null) {
        //    return true;
        //}
        //if (object1 == null && object2 == "") {
        //    return true;
        // }
        if (object1 == null && object2 != null) {
            return false;
        }
        if (object1.equals(object2)) {
            return true;
        }
        return false;
    }
}