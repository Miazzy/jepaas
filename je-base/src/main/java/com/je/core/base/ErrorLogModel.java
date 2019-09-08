package com.je.core.base;

import cn.hutool.core.io.FastByteArrayOutputStream;
import com.je.core.exception.PlatformException;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.core.util.ExceptionUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ErrorLogModel {
    private static final  String LINE_SPEATATOR = System.lineSeparator();
    private String errorCode;
    private String errorMess;
    private String url;
    private Object errorParam;
    private Object requestParam;
    private String stacktrace;
    private String createTime;

    private ErrorLogModel() {
    }

    /**
     * TODO 暂不明确
     * @param exception
     * @param request
     * @return
     */
    public static ErrorLogModel builder(PlatformException exception, HttpServletRequest request) {
        String reqUrl=request.getRequestURI();
        ErrorLogModel model = new ErrorLogModel();
        model.errorCode = exception.getErrorCode();
        model.errorMess = exception.getErrorMsg();
        model.url = reqUrl;
        model.errorParam = formatErrorParam(exception.getErrorParam());
        model.requestParam=formatRequestParam(ExceptionUtil.buildRequestParam(request));
        model.stacktrace = stackToStr(exception);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss");
        model.createTime = df.format(LocalDateTime.now());
        return model;
    }
    private static String formatRequestParam(Map<String,Object> requestParam) {
        if (requestParam == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        if(requestParam!=null) {
            buf.append("请求参数：");
            for(String key:requestParam.keySet()){
                buf.append(LINE_SPEATATOR).append("     "+key+"：");
                buf.append(JsonBuilder.getInstance().toJson(requestParam.get(key)));
            }
        }
        return buf.toString();
    }
    private static String formatErrorParam(Object param) {
        if (param == null) {
            return "";
        }

        if (!param.getClass().isArray()) {
            return param.toString();
        }

        Object[] objs = (Object[]) param;
        if (objs.length == 0) {
            return "";
        }

        StringBuffer buf = new StringBuffer();
        buf.append("异常参数：");
        for (int i = 0; i < objs.length; i++) {
//            if (i  0) {
                buf.append(LINE_SPEATATOR).append("     ");
//            }
            buf.append("参数" + (i + 1) + ": ");
            buf.append(JsonBuilder.getInstance().toJson(objs[i]));
        }
        return buf.toString();
    }

    private static String stackToStr(Throwable throwable) {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(out));
        return out.toString();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMess() {
        return errorMess;
    }

    public String getUrl() {
        return url;
    }

    public Object getErrorParam() {
        return errorParam;
    }

    public String getStacktrace() {
        return stacktrace;
    }
    @Override
    public String toString() {
        String tmpl = LINE_SPEATATOR + "JEERROR-%s: %s"
                + LINE_SPEATATOR + "请求链接: %s"
                + LINE_SPEATATOR + "%s"
                + LINE_SPEATATOR + "%s"
                + LINE_SPEATATOR + "异常堆栈: %s"
                + LINE_SPEATATOR;

        return String.format(tmpl, this.errorCode, this.errorMess, this.url,this.requestParam, this.errorParam, this.stacktrace);
    }
}
