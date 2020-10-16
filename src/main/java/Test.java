import com.example.demo.main.entity;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Test<T> {
    public String contrastObj(Object oldBean, Object newBean) {
        String str="";
        T pojo1 = (T) oldBean;
        T pojo2 = (T) newBean;
        try {
            Class clazz = pojo1.getClass();
            Field[] fields = pojo1.getClass().getDeclaredFields();
            int i=1;
            for (Field field : fields) {
                if("serialVersionUID".equals(field.getName())){
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object o1 = getMethod.invoke(pojo1);
                Object o2 = getMethod.invoke(pojo2);
                if(o1==null || o2 == null){
                    continue;
                }
                if (!o1.toString().equals(o2.toString())) {
                    if(i!=1){
                        str+=";";
                    }
                    str+=i+"字段名称:"+field.getName()+",旧值:"+o1+",新值:"+o2;
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void main(String[] args) {
        // 模拟旧数据
        entity oldModel = new  entity();
        oldModel.setId("1");
        oldModel.setName("张三");
        // 模拟新数据
        entity model = new  entity();
        model.setId("2");
        model.setName("李四");
        Test<entity> t= new Test<>();
        String list = t.contrastObj(oldModel,model);
        System.out.println("oldModel:"+oldModel);
        System.out.println("model:"+model);
        System.out.println("list:"+list);
    }
}
