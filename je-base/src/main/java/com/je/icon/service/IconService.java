package com.je.icon.service;

import java.io.FileNotFoundException;
import java.util.Map;

public interface IconService {
	/**
	 * 字体图标生成
	 * @param params
	 * @param fontIcon
	 * @return
	 */
	public String fontIconCSS(Map<String, String> params, Boolean fontIcon);
}
