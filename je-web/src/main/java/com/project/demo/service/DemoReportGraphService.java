package com.project.demo.service;

import com.je.phone.vo.DsInfoVo;

public interface DemoReportGraphService {

    /**
     *  图表报表的数据源
     *  params  请求的参数,类型是Map
     *  request 请求的报文
     * @param dsInfoVo
     * @return
     */
    public String loadTbBbDb(DsInfoVo dsInfoVo);

}
