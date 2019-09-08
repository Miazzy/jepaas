package com.je.core.base;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.result.BaseRespResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class PlatformDispatcherServlet extends DispatcherServlet {

    /**
     *  TODO 暂不明确
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.error("request url is null url:" + request.getRequestURI());

        Writer writer = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            writer = response.getWriter();
            BaseRespResult baseRespResult = BaseRespResult.errorResult(HttpStatus.NOT_FOUND.toString(), request.getRequestURI());
            writer.write(baseRespResult.toString());
            writer.flush();
        } catch (IOException e) {
//            logger.error(e.getMessage());
            throw new PlatformException("后端返回前端数据出错，url链接："+request.getRequestURI(), PlatformExceptionEnum.JE_CORE_CONTROLLER_ERROR,e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
//                logger.error("", e);
                throw new PlatformException("后端返回前端数据出错，url链接："+request.getRequestURI(), PlatformExceptionEnum.JE_CORE_CONTROLLER_ERROR,e);
            }
        }
    }
}
