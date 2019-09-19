package com.je.message.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpMassNews;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.WxMpMassVideo;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import net.sf.json.JSONObject;

import org.springframework.web.util.HtmlUtils;

import com.je.core.constants.doc.JEFileType;
import com.je.core.constants.message.WxMessageType;
import com.je.core.constants.message.WxTwType;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.document.util.JeFileUtil;
import com.je.message.vo.WxMsgVo;
/**
 * 微信企业号工具类
 * @author zhangshuaipeng
 *
 */
public class WxMpUtil {
	/**
	 * 当前缓存的用户信息
	 */
//	public static Map<String, List<DynaBean>> wxCpUserInfos=new HashMap<String, List<DynaBean>>();
	private static WxMpUtil wxCpUtil = null;
	private WxMpUtil(){}
	public static WxMpUtil getInstance(){
		if(wxCpUtil==null){
			wxCpUtil=new WxMpUtil();
		}
		return wxCpUtil;
	}

	/**
	 * 上传
	 * @param msgVo 消息
	 */
	public void sendWxMsg(WxMsgVo msgVo){
		Map<String,Object> sysVars=WebUtils.getAllSysVar(StringUtil.getDefaultValue(msgVo.getJtgsId(),msgVo.getCurrentUser().getJtgsId()));
		JeFileUtil jeFileUtil=JeFileUtil.getInstance();
		String userIds=msgVo.getUserIds();
//		if(!"@all".equals(userIds)){
//			for(String userId:userIds.split(",")){
//				if(StringUtil.isEmpty(userId))continue;
//				List<DynaBean> userBeans=wxCpUserInfos.get(userId);
//				if(userBeans!=null && userBeans.size()>0){
//					for(DynaBean userBean:userBeans){
//						if(StringUtil.isNotEmpty(userBean.getStr("USER_ID"))){
//							wxUserIds.add(userBean.getStr("USER_ID"));
//						}
//					}
//				}
//			}
//			if(wxUserIds.size()<=0)return;
//		}
		//构建消息信息
		try {
			WxMpService wxMpService=initWxMpService(sysVars);
			if(wxMpService==null){
				throw new WxErrorException(buildError(911111, "公众号配置信息错误!"));
			}
			WxMpMassOpenIdsMessage massMessage=null;
			String sendUserStr=userIds;
			if(StringUtil.isEmpty(sendUserStr)){
				throw new WxErrorException(buildError(911112, "接受用户为空!"));
			}
			if(WxMessageType.WZ.equals(msgVo.getType())){
				massMessage = new WxMpMassOpenIdsMessage();
				massMessage.setContent(msgVo.getContext());
				massMessage.setMsgType(WxConsts.MASS_MSG_TEXT);
			}else if(WxMessageType.TW.equals(msgVo.getType())){
				massMessage = new WxMpMassOpenIdsMessage();
				WxMpMassNews news = new WxMpMassNews();
				WxMpMassNews.WxMpMassNewsArticle article = new WxMpMassNews.WxMpMassNewsArticle();
				article.setTitle(msgVo.getTitle());
				if(WxTwType.HTML.equals(msgVo.getTwType())){
					String context=msgVo.getContext();
					article.setContent(context);
				}else if(WxTwType.UEditor.equals(msgVo.getTwType())){
					String context=msgVo.getContext();
					context=HtmlUtils.htmlUnescape(context.toString());
					article.setContent(context);
				}
				article.setContentSourceUrl(msgVo.getSourceUrl());
				article.setDigest(msgVo.getRemark());
				article.setAuthor(msgVo.getAuthor());
				if(StringUtil.isNotEmpty(msgVo.getFilePath())){
//					File file=new File(BaseAction.webrootAbsPath+msgVo.getFilePath());
					InputStream is=jeFileUtil.readFileIo(msgVo.getFilePath(), JEFileType.PROJECT);
					WxMediaUploadResult uploadMediaRes = wxMpService.getMaterialService()
							.mediaUpload(WxConsts.MEDIA_IMAGE, getExtention(msgVo.getFilePath()), is);
					jeFileUtil.closeInputStream(is);
					article.setThumbMediaId(uploadMediaRes.getMediaId());
				}
				news.addArticle(article);
				WxMpMassUploadResult massUploadResult = wxMpService.massNewsUpload(news);
				massMessage.setMsgType(WxConsts.MASS_MSG_NEWS);
				massMessage.setMediaId(massUploadResult.getMediaId());
			}else if(WxMessageType.TP.equals(msgVo.getType())){
				if(StringUtil.isNotEmpty(msgVo.getFilePath())){
//					File file=new File(BaseAction.webrootAbsPath+msgVo.getFilePath());
					InputStream is=jeFileUtil.readFileIo(msgVo.getFilePath(), JEFileType.PROJECT);
					WxMediaUploadResult uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MASS_MSG_IMAGE, getExtention(msgVo.getFilePath()), is);
					jeFileUtil.closeInputStream(is);
					if(uploadMediaRes!=null){
						massMessage = new WxMpMassOpenIdsMessage();
						massMessage.setMsgType(WxConsts.MASS_MSG_IMAGE);
						massMessage.setMediaId(uploadMediaRes.getMediaId());
//						wxMpMsg=WxMpKefuMessage.IMAGE().mediaId(uploadMediaRes.getMediaId()).toUser(sendUserStr).build();
					}else{
						throw new WxErrorException(buildError(90001, "图片文件上传失败!"));
					}
				}else{
					throw new WxErrorException(buildError(90001, "图片文件上传失败!"));
				}
			}else if(WxMessageType.YY.equals(msgVo.getType())){
				if(StringUtil.isNotEmpty(msgVo.getFilePath())){
//					File file=new File(BaseAction.webrootAbsPath+msgVo.getFilePath());
					InputStream is=jeFileUtil.readFileIo(msgVo.getFilePath(), JEFileType.PROJECT);
					WxMediaUploadResult uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MASS_MSG_VOICE, getExtention(msgVo.getFilePath()), is);
					jeFileUtil.closeInputStream(is);
					if(uploadMediaRes!=null){
						massMessage = new WxMpMassOpenIdsMessage();
						massMessage.setMsgType(WxConsts.MASS_MSG_VOICE);
						massMessage.setMediaId(uploadMediaRes.getMediaId());
					}else{
						throw new WxErrorException(buildError(90003, "语音文件上传失败!"));
					}
				}else{
					throw new WxErrorException(buildError(90003, "语音文件上传失败!"));
				}
			}else if(WxMessageType.SP.equals(msgVo.getType())){
				if(StringUtil.isNotEmpty(msgVo.getFilePath())){
//					File file=new File(BaseAction.webrootAbsPath+msgVo.getFilePath());
					InputStream is=jeFileUtil.readFileIo(msgVo.getFilePath(), JEFileType.PROJECT);
					WxMediaUploadResult uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MEDIA_VIDEO, getExtention(msgVo.getFilePath()), is);
					jeFileUtil.closeInputStream(is);
					if(uploadMediaRes!=null){
						massMessage = new WxMpMassOpenIdsMessage();
						WxMpMassVideo news=new WxMpMassVideo();
						news.setDescription(msgVo.getRemark());
						news.setTitle(msgVo.getTitle());
						news.setMediaId(uploadMediaRes.getMediaId());
						massMessage.setMsgType(WxConsts.MASS_MSG_VIDEO);
						WxMpMassUploadResult massUploadResult=wxMpService.massVideoUpload(news);
						massMessage.setMediaId(massUploadResult.getMediaId());
					}else{
						throw new WxErrorException(buildError(90004, "视频文件上传失败!"));
					}
				}else{
					throw new WxErrorException(buildError(90004, "视频文件上传失败!"));
				}
			}else if(WxMessageType.YINY.equals(msgVo.getType())){
//				String thumbId="";
//					if(StringUtil.isNotEmpty(msgVo.getFilePath())){
//						File file=new File(BaseAction.webrootAbsPath+msgVo.getFilePath());
//						InputStream is=new FileInputStream(file);
//						DdMediaUploadResult uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MEDIA_THUMB, getExtention(msgVo.getFilePath()), is);
//						if(uploadMediaRes!=null){
//							thumbId=uploadMediaRes.getMediaId();
//						}
//					}
				//wxMpMsg=WxMpKefuMessage.MUSIC().title(msgVo.getTitle()).toUser(sendUserStr).thumbMediaId(thumbId).musicUrl(msgVo.getUrl()).hqMusicUrl(msgVo.getSourceUrl()).build();
			}

			if(massMessage!=null){
				for(String openId:msgVo.getUserIds().split(",")){
					massMessage.getToUsers().add(openId);
				}
				wxMpService.massOpenIdsMessageSend(massMessage);
//				wxMpService.getKefuService().sendKefuMessage(wxMpMsg);
			}
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 初始化服务类
	 * @param sysVars
	 * @return
	 */
	public WxMpService initWxMpService(Map<String,Object> sysVars) {
		// TODO Auto-generated method stub
		WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
		WxMpService wxService = new WxMpServiceImpl();
		String appId= (String) sysVars.get("WX_GZH_APPID");
		String secret= (String) sysVars.get("WX_GZH_APPSECRET");
		String token= (String) sysVars.get("WX_GZH_TOKEN");
		String aesKey= (String) sysVars.get("WX_GZH_ENCODINGAESKEY");
		if(StringUtil.isEmpty(appId) || StringUtil.isEmpty(secret) || StringUtil.isEmpty(token) || StringUtil.isEmpty(aesKey)){
			return null;
		}
		config.setAppId(appId); // 设置微信公众号的appid
		config.setSecret(secret); // 设置微信公众号的app corpSecret
		config.setToken(token); // 设置微信公众号的token
		config.setAesKey(aesKey); // 设置微信公众号的EncodingAESKey
		wxService.setWxMpConfigStorage(config);
		return wxService;
	}

	/**
	 * TODO未处理
	 * @param errorInfoStr 消息
	 * @return
	 */
	public static String formatError(String errorInfoStr) {
		// TODO Auto-generated method stub
		Map<Integer,String> errorInfos=new HashMap<Integer,String>();
		errorInfos.put(-1, "系统繁忙");
		errorInfos.put(0, "请求成功");
		errorInfos.put(40001, "不合法的secret参数，secret在应用详情/通讯录管理助手可查看!");
		errorInfos.put(40003, "无效的UserID!");
		errorInfos.put(40004, "不合法的媒体文件类型,参考：上传的媒体文件限制!");
		errorInfos.put(40005, "不合法的type参数,参考：上传的媒体文件限制!");
		errorInfos.put(40006, "不合法的文件大小,参考：上传的媒体文件限制~");
		errorInfos.put(40007, "不合法的media_id参数");
		errorInfos.put(40008, "不合法的msgtype参数,参考：消息类型");
		errorInfos.put(40013, "不合法的CorpID,需确认CorpID是否填写正确，在 web管理端-设置 可查看");
		errorInfos.put(40014, "不合法的access_token");
		errorInfos.put(40016, "不合法的按钮个数,菜单按钮1-3个");
		errorInfos.put(40017, "不合法的按钮类型,参考：按钮类型");
		errorInfos.put(40018, "不合法的按钮名字长度,长度应不超过16个字节");
		errorInfos.put(40019, "不合法的按钮KEY长度,长度应不超过128字节");
		errorInfos.put(40020, "不合法的按钮URL长度,	长度应不超过1024字节");
		errorInfos.put(40022, "不合法的子菜单级数,只能包含一级菜单和二级菜单");
		errorInfos.put(40023, "不合法的子菜单按钮个数");
		errorInfos.put(40024, "不合法的子菜单按钮类型");
		errorInfos.put(40025, "不合法的子菜单按钮名字长度");
		errorInfos.put(40026, "不合法的子菜单按钮KEY长度");
		errorInfos.put(40027, "不合法的子菜单按钮URL长度");
		errorInfos.put(40029, "不合法的oauth_code");
		errorInfos.put(40031, "不合法的UserID列表");
		errorInfos.put(40032, "不合法的UserID列表长度");
		errorInfos.put(40033, "不合法的请求字符");
		errorInfos.put(40054, "不合法的子菜单url域名");
		errorInfos.put(40055, "不合法的菜单url域名");
		errorInfos.put(40056, "不合法的agentid");
		errorInfos.put(40057, "不合法的callbackurl或者callbackurl验证失败");
		errorInfos.put(40058, "不合法的参数");
		errorInfos.put(40059, "不合法的上报地理位置标志位");
		errorInfos.put(40063, "参数为空");
		errorInfos.put(40066, "不合法的部门列表");
		errorInfos.put(40068, "不合法的标签ID");
		errorInfos.put(40071, "不合法的标签名字");
		errorInfos.put(40072, "不合法的标签名字长度,不允许为空，最大长度限制为32个字（汉字或英文字母）");
		errorInfos.put(40073, "	不合法的openid");
		errorInfos.put(40074, "news消息不支持保密消息类型");
		errorInfos.put(40077, "不合法的pre_auth_code参数");
		errorInfos.put(40078, "不合法的auth_code参数");
		errorInfos.put(40080, "不合法的suite_secret");
		errorInfos.put(40082, "不合法的suite_token");
		errorInfos.put(40083, "不合法的suite_id");
		errorInfos.put(40084, "不合法的permanent_code参数");
		errorInfos.put(40085, "不合法的的suite_ticket参数");
		errorInfos.put(40086, "不合法的第三方应用appid");
		errorInfos.put(40092, "导入文件存在不合法的内容");
		errorInfos.put(40093, "不合法的jsapi_ticket参数");
		errorInfos.put(40094, "不合法的URL");
		errorInfos.put(40001, "缺少access_token参数");
		errorInfos.put(41002, "缺少corpid参数");
		errorInfos.put(41004, "缺少secret参数");
		errorInfos.put(41008, "缺少auth code参数");
		errorInfos.put(41009, "缺少userid参数");
		errorInfos.put(41010, "缺少url参数");
		errorInfos.put(41011, "缺少agentid参数");
		errorInfos.put(41016, "缺少title参数");
		errorInfos.put(41017, "缺少tagid参数");
		errorInfos.put(41021, "缺少suite_id参数");
		errorInfos.put(41025, "缺少permanent_code参数");
		errorInfos.put(42001, "access_token已过期");
		errorInfos.put(42007, "pre_auth_code已过期");
		errorInfos.put(42009, "suite_access_token已过期");
		errorInfos.put(43004, "指定的userid未绑定微信或未关注微信插件");
		errorInfos.put(44004, "文本消息content参数为空");
		errorInfos.put(45001, "多媒体文件大小超过限制");
		errorInfos.put(45002, "消息内容大小超过限制");
		errorInfos.put(45004, "应用description参数长度不符合系统限制");
		errorInfos.put(45007, "语音播放时间超过限制");
		errorInfos.put(45008, "图文消息的文章数量不符合系统限制");
		errorInfos.put(45009, "接口调用超过限制");
		errorInfos.put(45022, "应用name参数长度不符合系统限制");
		errorInfos.put(45024, "帐号数量超过上限");
		errorInfos.put(45026, "触发删除用户数的保护");
		errorInfos.put(45032, "图文消息author参数长度超过限制");
		errorInfos.put(46003, "菜单未设置");
		errorInfos.put(48002, "API接口无权限调用，请到微信管理工具下的通讯录同步模块中开启API操作！");
		errorInfos.put(48003, "不合法的suite_id");
		errorInfos.put(48004, "授权关系无效");
		errorInfos.put(48005, "API接口已废弃");
		errorInfos.put(50001, "redirect_url未登记可信域名");
		errorInfos.put(50002, "成员不在权限范围");
		errorInfos.put(50003, "应用已禁用");
		errorInfos.put(60001, "部门长度不符合限制");
		errorInfos.put(60003, "部门ID不存在");
		errorInfos.put(60004, "父部门不存在");
		errorInfos.put(60005, "部门下存在成员");
		errorInfos.put(60006, "部门下存在子部门");
		errorInfos.put(60007, "不允许删除根部门");
		errorInfos.put(60008, "部门已存在");
		errorInfos.put(60009, "部门名称含有非法字符");
		errorInfos.put(60010, "部门存在循环关系");
		errorInfos.put(60011, "指定的成员/部门/标签参数无权限");
		errorInfos.put(60012, "不允许删除默认应用");
		errorInfos.put(60020, "访问ip不在白名单之中");
		errorInfos.put(60102, "UserID已存在");
		errorInfos.put(60103, "手机号码不合法");
		errorInfos.put(60104, "手机号码已存在");
		errorInfos.put(60105, "邮箱不合法");
		errorInfos.put(60106, "邮箱已存在");
		errorInfos.put(60110, "用户所属部门数量超过限制");
		errorInfos.put(60111, "UserID不存在");
		errorInfos.put(60112, "成员name参数不合法");
		errorInfos.put(60123, "无效的部门id");
		errorInfos.put(60124, "无效的父部门id");
		errorInfos.put(60125, "非法部门名字");
		errorInfos.put(60127, "缺少department参数");
		errorInfos.put(60129, "成员手机和邮箱都为空");
		errorInfos.put(72023, "发票已被其他公众号锁定");
		errorInfos.put(72024, "发票状态错误");
		errorInfos.put(72037, "存在发票不属于该用户");
		errorInfos.put(80001, "可信域名不正确，或者无ICP备案");
		errorInfos.put(81001, "部门下的结点数超过限制（3W）");
		errorInfos.put(81002, "部门最多15层");
		errorInfos.put(81011, "无权限操作标签");
		errorInfos.put(82002, "不合法的PartyID列表长度");
		errorInfos.put(82003, "不合法的TagID列表长度");
		errorInfos.put(84019, "缺少templateid参数");
		errorInfos.put(84020, "templateid不存在");
		errorInfos.put(84021, "缺少register_code参数");
		errorInfos.put(84022, "无效的register_code参数	");
		errorInfos.put(84023, "不允许调用设置通讯录同步完成接口");
		errorInfos.put(84024, "无注册信息");
		errorInfos.put(85002, "包含不合法的词语");
		errorInfos.put(85004, "每企业每个月设置的可信域名不可超过20个");
		errorInfos.put(85005, "可信域名未通过所有权校验");
		errorInfos.put(301004, "删除人员包含管理员，不能删除管理员！");
		errorInfos.put(10098, "企业微信配置信息未配置，请配置后再试!");
		errorInfos.put(10099, "因为微信强制要求，组织机构只能有一个一级部门，请维护后进行同步!");
		errorInfos.put(90001, "图片上传失败!");
		errorInfos.put(90002, "系统未设置访问地址，无法发送图文消息！");
		errorInfos.put(90003, "语音文件上传失败!");
		errorInfos.put(90004, "视频文件上传失败!");
		errorInfos.put(90005, "视频文件上传失败!");
		String error="执行出错，请联系管理员!";
		errorInfos.put(-1, "不合法的请求字符");
		if(StringUtil.isNotEmpty(errorInfoStr) && errorInfoStr.startsWith("{") && errorInfoStr.endsWith("}")){
			JSONObject errorInfo= JSONObject.fromObject(errorInfoStr);
			if(errorInfo.containsKey("errcode")){
				int errcode=errorInfo.getInt("errcode");
				if(errorInfos.containsKey(errcode)){
					error=errorInfos.get(errcode);
				}
			}
		}
		return error;
	}
	/**
	 * 获取文件扩展名
	 * @param fileName
	 * @return
	 */
	public static String getExtention(String fileName)  {
		int pos = fileName.lastIndexOf(".")+1;
		return fileName.substring(pos);
	}
	/**
	 * 构建微信错误信息
	 * @param errcode
	 * @param errMsg
	 * @return
	 */
	public static WxError buildError(int errcode,String errMsg){
		WxError wxErr=new WxError();
		wxErr.setErrorCode(errcode);
		wxErr.setErrorMsg(errMsg);
		return wxErr;
	}
//	public static void main(String[] args) {
//		String text=FileOperate.readTxt("F:/gongweifawen_16111614-62.mx.xml", "UTF-8", "");
//		System.out.println(text);
//	}
}
