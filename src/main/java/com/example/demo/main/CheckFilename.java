package com.example.demo.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class CheckFilename {
    private static final String RESULT_FILE_PATH = "D:/result.txt";
    public static void main(String[] args) {

        String FIlePATH="D:\\dd";
        File file = new File(FIlePATH);
        StringBuilder strb=new StringBuilder();
        BufferedWriter rbw=null;
        try {
            rbw  = new BufferedWriter(new FileWriter(RESULT_FILE_PATH));
            func(file,strb,rbw);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ( rbw != null) {
                try {
                    rbw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     *
     * @param file
     */
    private static void func(File file,StringBuilder strb,BufferedWriter rbw) {
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isDirectory()){//若是目录，则递归打印该目录下的文件
                func(f,strb,rbw);
            }

            if (f.isFile())  {      //若是文件，直接打印
                System.out.println(f);

                checkFile(f,strb, rbw);
            }


        }
    }


    public static  void checkFile(File f,StringBuilder strb,BufferedWriter rbw){
        System.out.println("======Start Search!=======");
        File file = new File(f.getPath());
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String lineText = null;
            while ((lineText = br.readLine()) != null) {
                    String searchText = lineText.replace(" ","").toLowerCase();
                    if(searchText.contains("!=''")||searchText.contains("!='null'")||searchText.contains("isnotnull")||searchText.contains("!=''")||searchText.contains("<>''")){

                        rbw.write(f.getParent()+"\n");

                        rbw.write(f.getName()+"\n");
                        //rbw.write(searchText+"\n");
                        break;
                    }

                    }

            System.out.println("======Process Over!=======");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
