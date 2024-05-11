package com.jasonhong.fx.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class BeanUtil {
    public static String toString(Object object) {
        Class<?> personClass = object.getClass();
        Field[] fields = personClass.getDeclaredFields();

        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append(personClass.getName()+"{");

        for (Field field : fields) {
            field.setAccessible(true); // 允许访问私有字段

            try {
                Object value = field.get(object);
                Class<?> fieldType = field.getType();

                infoBuilder//.append("Field Name: ")
                        .append(field.getName()) ;
                //infoBuilder.append("Field Type: ").append(fieldType.getName()).append("\n");

                if (fieldType.isArray() && value!=null) {
                    infoBuilder.append(": ").append(Array.getLength(value)).append(" ").append(fieldType.getName()).append(", ");
                } else {
                    infoBuilder.append(": ").append(value).append(", ");
                }

                //infoBuilder.append("\n"); // 添加换行符以便分隔不同字段的信息
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        infoBuilder.append("}");
        String infoString = infoBuilder.toString(); // 将StringBuilder转换为String
        return infoString;
    }
}
