package com.je.core.base;

import com.alibaba.fastjson.JSON;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.core.result.BaseRespResult;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.*;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@ControllerAdvice
public class PlatformExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlatformExceptionHandler.class);

    public static final String ENCODING = "UTF-8";

    // 定义全局404处理
    @RequestMapping(value = "/**", produces = "application/json; charset=utf-8")
    @ResponseBody
    @ExceptionHandler(NoSuchRequestHandlingMethodException.class)
    public BaseRespResult handleNotFoundException(RuntimeException ex, HttpServletRequest request) {
        logger.warn("request url not found: [{}]", request.getRequestURI());
        return BaseRespResult.errorResult(PlatformExceptionEnum.UNKOWN_ERROR.toString(), "9999", "请求页面不存在，请检查");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public void handleException(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        hanndleError(ex, request, response);
    }

    public static void hanndleError(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (ex instanceof PlatformException) {
                String exceptionId="";
                PlatformException platformException = (PlatformException) ex;

                ErrorLogModel errorLog = null;
                try {
                    errorLog = ErrorLogModel.builder(platformException, request);
                } catch (Exception e) {
                    logger.error("request url: [{}] 出错了 ,参数：[{}]", request.getRequestURI(), ExceptionUtil.buildRequestParamToJson(request), ex);
                    writeErrorMsg(response, BaseRespResult.errorResult(PlatformExceptionEnum.UNKOWN_ERROR.toString(), "9999", ex.getMessage()));
                }

                if (errorLog != null) {
                    logger.error("[{}]", errorLog);
                    if ("1".equals(WebUtils.getSysVar("JE_SYS_EXCEPTION"))) {
                        PCDynaServiceTemplate serviceTemplate = SpringContextHolder.getBean("PCDynaServiceTemplate");
                        DynaBean exception = buildException(errorLog, request);
                        serviceTemplate.buildModelCreateInfo(exception);
                        exception=serviceTemplate.insert(exception);
                        exceptionId=exception.getStr("JE_CORE_EXCEPTION_ID");
                    }
                }

                writeErrorMsg(response, BaseRespResult.errorResult(platformException.getCode(), platformException.getErrorCode(), ex.getMessage(), ex.getMessage(),exceptionId));
            } else {
                logger.error("request url: [{}] 出错了 ,参数：[{}]", request.getRequestURI(), ExceptionUtil.buildRequestParamToJson(request), ex);
                writeErrorMsg(response, BaseRespResult.errorResult(PlatformExceptionEnum.UNKOWN_ERROR.toString(), "9999", ex.getMessage()));
            }
        } catch (Exception e) {
            logger.error("request url: [{}] 出错了 ,参数：[{}]", request.getRequestURI(), ExceptionUtil.buildRequestParamToJson(request), ex);
            writeErrorMsg(response, BaseRespResult.errorResult(PlatformExceptionEnum.UNKOWN_ERROR.toString(), "9999", ex.getMessage()));
        }
    }

    public static void writeErrorMsg(HttpServletResponse response, BaseRespResult result) {
        writeErrorMsg(response, JSON.toJSONString(result));
    }

    public static void writeErrorMsg(HttpServletResponse response, String result) {
        response.setContentType("text/html; charset=utf-8");
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.print(result);
            printWriter.flush();
        } catch (IOException e) {
            IOUtils.closeQuietly(printWriter);
        }
    }

    /**
     * 构建异常信息
     *
     * @param errorLog
     * @param request
     * @return
     */
    public static DynaBean buildException(ErrorLogModel errorLog, HttpServletRequest request) {
        EndUser currentUser = SecurityUserHolder.getCurrentUser();
        Map<String, Object> params = ExceptionUtil.buildRequestParam(request);
        DynaBean exception = new DynaBean("JE_CORE_EXCEPTION", false);
        exception.set(BeanUtils.KEY_PK_CODE, "JE_CORE_EXCEPTION_ID");
        exception.set("EXCEPTION_CODE", errorLog.getErrorCode());
        exception.set("EXCEPTION_MESSAGE", errorLog.getErrorMess());
        exception.set("EXCEPTION_EXCEPTIONPARAM", errorLog.getErrorParam());
        exception.set("EXCEPTION_TOKENID", SecurityUserHolder.getToken());
        exception.set("EXCEPTION_REQUESTPARAMS", JsonBuilder.getInstance().toJson(params));
        exception.set("EXCEPTION_REQUESTURL", errorLog.getUrl());
        exception.set("EXCEPTION_EXECUUSERCODE", currentUser.getUserCode());
        exception.set("EXCEPTION_EXECUUSER", currentUser.getUsername());
        exception.set("EXCEPTION_DSNAME", currentUser.getZhDs());
        exception.set("EXCEPTION_EXECUUSERID", currentUser.getUserId());
        exception.set("EXCEPTION_STACKTRACE", errorLog.getStacktrace());
        exception.set("EXCEPTION_EXECUTIME", DateUtils.formatDateTime(new Date()));
        return exception;
    }
}
