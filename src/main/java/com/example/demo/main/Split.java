package com.example.demo.main;


import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Split {
    static final int byteSize = 2 * 1024 * 1024;

    public void run(String originFile, String targetDirectoryPath) {
        File sourceFile = new File(originFile);
        File targetFile = new File(targetDirectoryPath);
        if (!sourceFile.exists() || sourceFile.isDirectory()) {
            return;
        }
        if (targetFile.exists()) {
            if (!targetFile.isDirectory()) {
                return;
            }
        } else {
            targetFile.mkdirs();
        }

        RandomAccessFile rFile;
        OutputStream os;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); //得到前一天
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            rFile = new RandomAccessFile(originFile, "r");
            long fileLength = rFile.length();
            long startPos = 0;
            int filenum = (int) Math.ceil(fileLength /2.00/1024/1024);
            if (0 == filenum) {
                filenum = 1;
            }
            String ftpFileNmae = "";

            for (int i = 1; i <= filenum; i++) {
                rFile.seek(startPos + byteSize);
                int extra = eofOrNextCRLFInterval(rFile);
                int curbyteSize = byteSize + extra;
                rFile.seek(startPos);
                byte[] b = new byte[curbyteSize];
                int s = rFile.read(b);
                String newString = String.format("%02d", i);
                ftpFileNmae = "SNEP_DNS_DAY_SC_" + df.format(date) + "_P" + newString + ".csv";
                if (i == filenum) {
                    ftpFileNmae = "SNEP_DNS_DAY_SC_" + df.format(date) + "_P" + newString + "_END.csv";
                }
                os = new FileOutputStream(targetFile.getAbsolutePath() + "/" +  ftpFileNmae );
                os.write(b, 0, s);
                os.flush();
                os.close();
                startPos += curbyteSize;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int eofOrNextCRLFInterval(RandomAccessFile rFile) throws IOException{
        boolean isCRLF = false;
        int interval = 0;
        while(!isCRLF){
            try{
                interval ++;
                int readByte =  rFile.readByte();
                if(readByte == 0X0A){
                    isCRLF = true;
                }
            }catch(EOFException e){
                isCRLF = true;
                break;
            }
        }
        return interval;
    }


    public static void main(String[] args){
        String sourceFile = "F:\\ftpserver\\servertest\\tb_kr_iot_business_tmnl_dy.sql";
        String targetFilePath = "F:\\ftpserver\\servertest\\posinv";
        Split s = new Split();
        long start1 = System.currentTimeMillis();
        System.out.println(start1);
        s.run(sourceFile, targetFilePath);
        long start2 = System.currentTimeMillis();
        System.out.println(start2);
        System.out.println((start2 - start1)/1000.00 + "  second");
    }
}