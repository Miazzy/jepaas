package com.project.demo.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.bean.DynaBean;
import com.je.phone.vo.DsInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("demoReportGraphService")
public class DemoReportGraphServiceImpl implements DemoReportGraphService {

    @Autowired
    private PCDynaServiceTemplate serviceTemplate;

    /**
     *  页面操作
     *  进入图表报表引擎，点击数据源，进去创建页面
     *  业务bean：reportGraphService
     *  业务方法：loadTbBbDb
     *  字段名：数据源dynaBeanList中取出来数据，往里set的字段，返回到页面显示
     *  参数名：过滤传的一些参数
     *  进入图表或者报表，新建一个图表，在数据源区域，右击载入数据源
     *  拖拉字段放入对应的X,Y轴即可
     */
    /**
     *  图表报表的数据源
     *  params  请求的参数,类型是Map
     *  request 请求的报文
     * @param dsInfoVo
     * @return
     */
    public String loadTbBbDb(DsInfoVo dsInfoVo){
        //获取前台传过来的参数Map
        Map<String, Object> jsonObject = dsInfoVo.getParams();
        //定义过滤的SQL
        String order = "";
        String XMINFO_XMBM = (String)jsonObject.get("XMINFO_XMBM"); //取得参数编码
        String XMINFO_XMMC = (String)jsonObject.get("XMINFO_XMMC"); //取得参数名称
        //判断请求的参数是否为空，不为空则拼接过滤
        if(XMINFO_XMBM != null && !"".equals(XMINFO_XMBM)){
            order = order + " AND XMINFO_XMBM='"+ XMINFO_XMBM +"'";
        }
        if(XMINFO_XMMC != null && !"".equals(XMINFO_XMMC)){
            order = order + " AND XMINFO_XMMC like '%"+ XMINFO_XMMC +"%'";
        }
        //取得数据源信息
        List<DynaBean> dynaBeanList = serviceTemplate.selectList("JE_DEMO_XMINFO","AND 1 = 1"+ order +" ORDER BY SY_CREATETIME ASC");
        //定义一个JSON数组，存放返回的参数
        JSONArray arrays = new JSONArray();
        //循环数据源放入JSON数组中
        for(DynaBean dynaBean : dynaBeanList){
            JSONObject obj = new JSONObject();
            obj.put("项目编码", dynaBean.get("XMINFO_XMBM"));
            obj.put("项目名称", dynaBean.get("XMINFO_XMMC"));
            arrays.add(obj);
        }

        return arrays.toString();
    }

}
