package com.suanban.demo;

import com.je.core.base.AbstractDynaController;
import com.je.core.base.MethodArgument;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 */
@Controller
@RequestMapping(value = "/je/product/demo")
public class DemoReturnInfoController  extends AbstractDynaController {

    @ResponseBody
    @RequestMapping(value = "/returnInfo",method = RequestMethod.POST)
    public void returnInfo(MethodArgument param){

       toWrite("\"你好，欢迎使用JEPLUTS。这是后台返回数据\"",param.getRequest(),param.getResponse());
    }

}
