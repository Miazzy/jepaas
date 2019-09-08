package com.je.icon.service;

import com.je.config.service.SysConfigManager;
import com.je.cache.service.config.ConfigCacheManager;
import com.je.core.constants.doc.JEFileSaveType;
import com.je.core.constants.doc.JEFileType;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.*;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.document.service.JeFileManager;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 图表服务层
 *
 * @author marico
 */
@Component("iconService")
public class IconServiceImpl implements IconService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PCDynaServiceTemplate pcServiceTemplate;
    @Autowired
    private PCServiceTemplate dbServiceTemplate;
    @Autowired
    private JeFileManager jeFileManager;
    /**
     * 字体图标生成
     * @param params
     * @param fontIcon
     * @return
     */
    public String fontIconCSS(Map<String, String> params, Boolean fontIcon) {
        // TODO Auto-generated method stub
        if (fontIcon) {
            this.importFont(params);
        }
        StringBuffer css = new StringBuffer();
        //字体文件目录
        String folder = "/JE/resource/fonts/css/";
        File fs = jeFileManager.readFile(folder, JEFileType.PLATFORM);
        if (fs.exists()) {
            for (File file : fs.listFiles()) {
                css.append(jeFileManager.readTxt(folder + file.getName(), "utf-8", "\n\r", JEFileType.PLATFORM));
                String name = file.getName();
                name = name.substring(0, name.lastIndexOf("."));
            }
            logger.info("字体图标生成成功");
        }
        return css.toString();
    }

    /**
     * 导入字体
     *
     * @param params
     */
    private void importFont(Map<String, String> params) {
        String file = params.get("file");//字体包
        String font = params.get("font");//字体类名
        String noFont = params.get("noFont");//不启用字体类名
        String fontB = params.get("fontB");//图标类名前缀
        //字体文件目录
        String folder = "/JE/resource/fonts/";
        String cssFolder = folder + "css/";
        String fontFolder = folder + font + "/";
        String cssFile = cssFolder + font + ".css";
        jeFileManager.createFolder(cssFolder, JEFileType.PLATFORM);
        //字体目录
        jeFileManager.deleteFolder(fontFolder, JEFileType.PLATFORM);
        jeFileManager.createFolder(fontFolder, JEFileType.PLATFORM);
        //将文件复制到字体目录
        String temp = file.substring(0, file.lastIndexOf("/") + 1) + "_temp/";
        jeFileManager.unZip(file,JEFileType.PLATFORM, JEFileSaveType.DEFAULT, temp, JEFileType.PLATFORM);
        File tFolder = jeFileManager.readFile(temp, JEFileType.PLATFORM);
        if (tFolder.list().length == 1) {
            temp += tFolder.list()[0] + "/";
        }

        File _cssFolder = jeFileManager.readFile(temp + "css/", JEFileType.PLATFORM);
        if (_cssFolder.exists()) {
            jeFileManager.copyFile(temp + "css/" + _cssFolder.list()[0],JEFileType.PLATFORM,JEFileSaveType.DEFAULT, cssFile, JEFileType.PLATFORM);
            System.out.println("css复制成功！");
        }
        String _fontFolderPath = temp + "fonts/";
        File _fontFolder = jeFileManager.readFile(_fontFolderPath, JEFileType.PLATFORM);
        if (_fontFolder.exists()) {
            for (String name : _fontFolder.list()) {
                jeFileManager.copyFile(_fontFolderPath + _fontFolder.list()[0],JEFileType.PLATFORM,JEFileSaveType.DEFAULT, fontFolder + name, JEFileType.PLATFORM);
            }
            logger.info("字体文件复制成功！");
        }
        //删除临时文件
        jeFileManager.deleteFolder(tFolder.getPath(),JEFileType.PLATFORM);
        //读取样式
        String css = jeFileManager.readTxt(cssFile, "utf-8", "\r\n", JEFileType.PLATFORM);
        String pattern = "[.]" + fontB + "-([^{])+?[:]before";
        String tableCode = "JE_CORE_FONTICON";
        //清空原有的字体
        pcServiceTemplate.deleteByWehreSql(tableCode, " and FONTICON_FONT='" + font + "'");
        //百度翻译
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        String appid = "20170410000044464";
        String appkey = "V2KNZPEbl9OCvA0YGS7O";
        String salt = "1435660288";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(css);
        int count = 0;
        while (m.find()) {
            String cls = m.group();
            cls = cls.substring(1, cls.length() - 7);
            String q = cls.substring(fontB.length() + 1);
            String sign = md5.encodePassword(appid + q + salt + appkey, null);
            String result = JavaAjaxUtil.sendGet("http://api.fanyi.baidu.com/api/trans/vip/translate", "q=" + q + "&from=en&to=zh&appid=" + appid + "&&salt=" + salt + "&sign=" + sign);
            JSONObject rt = JSONObject.fromObject(result);
            String text = "";
            try {
                text = rt.getJSONArray("trans_result").getJSONObject(0).getString("dst");
            } catch (Exception ex) {
                System.out.println(cls + "：json解析出错");
                System.out.println(rt.toString());
            }
            DynaBean bean = new DynaBean(tableCode, true);
            if (!"1".equals(noFont)) {
                cls = font + " " + cls;
            }
            bean.set("FONTICON_CLS", cls);
            bean.set("FONTICON_FONT", font);
            bean.set("FONTICON_NAME", text);
            pcServiceTemplate.buildModelCreateInfo(bean);
            pcServiceTemplate.insert(bean);
            count++;
        }
       logger.info("成功生成" + count + "条数据");
        //清空原有查询策略
        String funcCode = params.get("funcCode");
        String funcId = params.get("funcId");
        String qTableCode = "JE_CORE_QUERYSTRATEGY";

        pcServiceTemplate.deleteByWehreSql(qTableCode, " and QUERYSTRATEGY_FUNCINFO_ID='" + funcId + "'");
        List<Map> fonts = dbServiceTemplate.queryMapBySql("SELECT FONTICON_FONT FROM JE_CORE_FONTICON GROUP BY FONTICON_FONT");
        for (Map f : fonts) {
            DynaBean q = new DynaBean(qTableCode, true);
            q.set("QUERYSTRATEGY_FUNCCODE", funcCode);
            q.set("QUERYSTRATEGY_FUNCINFO_ID", funcId);
            q.set("QUERYSTRATEGY_NAME", f.get("FONTICON_FONT") + "");
            q.set("QUERYSTRATEGY_SQL", " and FONTICON_FONT='" + f.get("FONTICON_FONT") + "'");
            pcServiceTemplate.insert(q);
        }
    }
}