package com.je.config.service;

import com.je.cache.service.MenuStaticizeCacheManager;
import com.je.cache.service.rbac.UserHomeCacheManager;
import com.je.core.constants.doc.JEFileSaveType;
import com.je.core.constants.doc.JEFileType;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.DateUtils;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.document.service.JeFileManager;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 系统模式
 *
 * @author zhangshuaipeng
 */
@Component("sysModeManager")
public class SysModeManagerImpl implements SysModeManager {
    private PCDynaServiceTemplate serviceTemplate;
    private PCServiceTemplate pcServiceTemplate;
    private JeFileManager jeFileManager;

    @Resource(name = "PCDynaServiceTemplate")
    public void setServiceTemplate(PCDynaServiceTemplate serviceTemplate) {
        this.serviceTemplate = serviceTemplate;
    }

    @Resource(name = "PCServiceTemplateImpl")
    public void setPcServiceTemplate(PCServiceTemplate pcServiceTemplate) {
        this.pcServiceTemplate = pcServiceTemplate;
    }

    @Resource(name = "jeFileManager")
    public void setJeFileManager(JeFileManager jeFileManager) {
        this.jeFileManager = jeFileManager;
    }

}
