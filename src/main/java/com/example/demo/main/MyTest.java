package com.example.demo.main;


import java.io.File;
import java.math.BigDecimal;

public class MyTest {
    public static void main(String[] args) {

        short ss=-19071;
        Integer in=shortToInteger(ss);
        System.out.println(in);
    }
    public static Integer shortToInteger(Short s){
        if(s < 0){
            return 65535+1+s;
        }else{
            return new BigDecimal(s).intValue();
        }
    }

    public static int getLength(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255) {
                length++;
            } else {
                length += 2;
            }
        }
        return length;
    }

}
