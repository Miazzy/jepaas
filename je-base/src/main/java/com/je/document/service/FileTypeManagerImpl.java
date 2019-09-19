package com.je.document.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.je.cache.service.doc.FileTypeCacheManager;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.document.vo.FileType;

/**
 * 文件服务层
 * @author zhangshuaipeng
 *
 */
@Component("fileTypeManager")
public class FileTypeManagerImpl implements FileTypeManager {
	@Autowired
	private PCDynaServiceTemplate serviceTemplate;
	@Override
	public void doLoadFileType() {
		// TODO Auto-generated method stub
		List<DynaBean> lists=serviceTemplate.selectList("JE_SYS_FILETYPE","","JE_SYS_FILETYPE_ID,"+BeanUtils.getInstance().getProQueryFields("JE_SYS_FILETYPE"));
		FileTypeCacheManager.clearAllCache();
		for(DynaBean dynaBean:lists){
			if(StringUtil.isNotEmpty(dynaBean.getStr("FILETYPE_CODE"))){
				for(String type:dynaBean.getStr("FILETYPE_CODE").split(",")){
					FileType fileType=new FileType();
					fileType.setName(dynaBean.getStr("FILETYPE_NAME"));
					fileType.setCode(type);
					fileType.setIconCls(dynaBean.getStr("FILETYPE_ICONCLS"));
//					fileType.setBigIconCls(dynaBean.getStr("FILETYPE_BIGICONCLS"));
//					fileType.setThumbnailCls(dynaBean.getStr("FILETYPE_THUMBNAILCLS"));
//					fileType.setFormat(dynaBean.getStr("FILETYPE_FORMAT"));
					FileTypeCacheManager.putCache(type.toLowerCase(), fileType);
				}
			}
		}
	}
}
