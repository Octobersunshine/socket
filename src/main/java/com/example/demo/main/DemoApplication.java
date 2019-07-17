package com.example.demo.main;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Iterator;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

    try {

            String str ="{{   'iRecCount': '10',   'totalCount': '10',   'RN': '1',   'I_ID': '55176',   'S_NO': '55157',   'I_OBJ_TYPE': '4',   'I_OBJ_ID': '0',   'I_REF_ID': '8002281',   'I_CYCLE': '1',   'S_TITLE': '【GGSN:ZZMME09BHW】网元 4G 网络性能 TAU成功率  TAU成功率:92.92% TAU成功率<98.00% 和TAU成功率>68.00%',   'S_MSG': '告警标题:【GGSN:ZZMME09BHW】网元 4G 网络性能 TAU成功率 告警周期:20190330指标描述:TAU成功次数次数：1190992,TAU请求次数：1281766,TAU成功率：92.92%,省份编码：371,统计小时：20190330,地市名称：ZZMME09BHW,省份名称：河南 TAU成功率:92.92% TAU成功率<98.00% 和TAU成功率>68.00%',   'S_DELIMITED_CYCLE': '20190330',   'CITY_CODE': '1010',   'CITY_NAME': '网管中心数据室',   'S_INST_ID': '1010',   'S_INST_NAME': '网管中心数据室',   'I_FLAG': '1',   'I_CREATE_TIME': '20190408151014',   'I_UPDATE_TIME': '20190408183232',   'I_DISPATCH_FLAG': '2',   'I_DISPATCH_TIME': '20190408180103',   'I_RESET_FLAG': '1',   'I_RESET_TIME': null,   'I_CANCEL_FLAG': '1',   'I_CANCEL_TIME': '20190408183232',   'I_OBJ_LEVEL': '2',   'I_NE_MODEL': '4',   'I_LARGE_CLASS': '1',   'I_SMALL_CLASS': '8',   'I_OBJ_SOURCE': '1',   'I_ZIP_CNT': '0',   'I_CLOSED_TIME': null,   'rowId': '1'  }, {   'iRecCount': '10',   'totalCount': '10',   'RN': '2',   'I_ID': '55174',   'S_NO': '55155',   'I_OBJ_TYPE': '4',   'I_OBJ_ID': '0',   'I_REF_ID': '8002281',   'I_CYCLE': '1',   'S_TITLE': '【GGSN:LUYMME09BER】网元 4G 网络性能 TAU成功率  TAU成功率:97.21% TAU成功率<98.00% 和TAU成功率>68.00%',   'S_MSG': '告警标题:【GGSN:LUYMME09BER】网元 4G 网络性能 TAU成功率 告警周期:20190330指标描述:TAU成功次数次数：34999773,TAU请求次数：36003886,TAU成功率：97.21%,省份编码：371,统计小时：20190330,地市名称：LUYMME09BER,省份名称：河南 TAU成功率:97.21% TAU成功率<98.00% 和TAU成功率>68.00%',   'S_DELIMITED_CYCLE': '20190330',   'CITY_CODE': '1010',   'CITY_NAME': '网管中心数据室',   'S_INST_ID': '1010',   'S_INST_NAME': '网管中心数据室',   'I_FLAG': '1',   'I_CREATE_TIME': '20190408151014',   'I_UPDATE_TIME': '20190408183232',   'I_DISPATCH_FLAG': '2',   'I_DISPATCH_TIME': '20190408180103',   'I_RESET_FLAG': '1',   'I_RESET_TIME': null,   'I_CANCEL_FLAG': '1',   'I_CANCEL_TIME': '20190408183232',   'I_OBJ_LEVEL': '2',   'I_NE_MODEL': '4',   'I_LARGE_CLASS': '1',   'I_SMALL_CLASS': '8',   'I_OBJ_SOURCE': '1',   'I_ZIP_CNT': '0',   'I_CLOSED_TIME': null,   'rowId': '2'  }}";

        JSONObject jsonObject = JSON.parseObject(str);
        for(Object res:jsonObject.keySet()){

            System.out.println("res：" +jsonObject.get(res));

        }

    }catch (Exception e){
e.printStackTrace();
    }


    }

     public  static void getstr(){

         System.out.println("连接失败执行");
    }

}
