package com.je.core.filter;

import com.je.cache.service.rbac.TokenUserCacheManager;
import com.je.core.base.PlatformExceptionHandler;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.core.result.BaseRespResult;
import com.je.core.util.SecurityUserHolder;
import com.je.core.util.StringUtil;
import com.je.rbac.model.EndUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author QiaoYu
 */
public class SessionFilter extends OncePerRequestFilter {
    public static final String X_AUTH_TOKEN = "authorization";
    public static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);
    public static Set<String> noFilters = new HashSet<String>();
    public static Set<String> tokenFilters = new HashSet<String>();
    public static final String INTERNAL_REQUEST_KEY = "schedule_898901212";

    /**
     * TODO 暂不明确
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean doFilter = true;
        // 查看请求的request中是否由internalRequestKey，如有则放入白名单
        String internalRequestKey = request.getHeader("internalRequestKey");
        if (StringUtils.isNotEmpty(internalRequestKey) && internalRequestKey.equals(INTERNAL_REQUEST_KEY)) {
            doFilter = false;
        }
        String uri = request.getRequestURI();
        for (String s : noFilters) {
            if (s.indexOf("**") != -1) {
                s = s.substring(0, s.indexOf("**"));
                if (uri.indexOf(s) != -1) {
                    doFilter = false;
                    break;
                }
            } else if (s.equals(uri)) {
                doFilter = false;
                break;
            }
        }

        EndUser user = null;
        try {
            Enumeration<String> enumeration = request.getHeaders(X_AUTH_TOKEN);
            if (enumeration.hasMoreElements()) {
                String tokenId = enumeration.nextElement();
                user = TokenUserCacheManager.getCacheValue(tokenId);

                if (null != user) {
                    SecurityUserHolder.put(user);
                    SecurityUserHolder.putToken(tokenId);
                }
            } else {
                logger.info("请求头没拿到 [{}] 没有token的请求头，走拿cookies逻辑", uri);
                boolean tokenFilter = false;
                for (String s : tokenFilters) {
                    if (s.indexOf("**") != -1) {
                        s = s.substring(0, s.indexOf("**"));
                        if (StringUtil.isNotEmpty(uri) && uri.indexOf(s) != -1) {
                            tokenFilter = true;
                            break;
                        }
                    } else if (s.equals(uri)) {
                        tokenFilter = true;
                        break;
                    }
                }

                boolean cookieUser = false;
                Cookie[] cookies = request.getCookies();
                if (tokenFilter && cookies != null) {
                    for (Cookie cookie : cookies) {
                        String name = cookie.getName();
                        if (X_AUTH_TOKEN.equals(name)) {
                            String tokenId = cookie.getValue();
                            user = TokenUserCacheManager.getCacheValue(tokenId);
                            if (null != user) {
                                SecurityUserHolder.put(user);
                                SecurityUserHolder.putToken(tokenId);
                                cookieUser = true;
                            }
                        }
                    }
                }
                if (!cookieUser && StringUtil.isNotEmpty(request.getParameter(X_AUTH_TOKEN))) {
                    String tokenId = request.getParameter(X_AUTH_TOKEN);
                    user = TokenUserCacheManager.getCacheValue(tokenId);
                    if (null != user) {
                        SecurityUserHolder.put(user);
                        SecurityUserHolder.putToken(tokenId);
                    }
                }
            }
        } catch (Exception e) {
            PlatformException pEx = new PlatformException("用户没有登陆或者已经超时.", PlatformExceptionEnum.JE_RBAC_FILTER_ERROR, request, e);
            PlatformExceptionHandler.hanndleError(pEx, request, response);
            return;
        }

        try {
            if (doFilter && null == user) {
                logger.info("进入SessionFilter拦截器[Session是空的请求!] [{}]   参数: [{}]", uri, JsonBuilder.getInstance().toJson(request.getParameterMap()));
                PlatformExceptionHandler.writeErrorMsg(response, BaseRespResult.errorResult(PlatformExceptionEnum.UNKOWN_LOGINUSER + "", "用户未登录"));
            } else {
                filterChain.doFilter(request, response);
            }
        }catch (Exception e){
            logger.error("执行业务异常！",e);
            PlatformException pEx = new PlatformException("执行业务异常!", PlatformExceptionEnum.JE_PROJECT_ERROR, request, e);
            PlatformExceptionHandler.hanndleError(pEx, request, response);
        }finally {
            SecurityUserHolder.removeAll();
        }

    }
}
