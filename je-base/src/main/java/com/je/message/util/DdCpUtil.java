package com.je.message.util;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.je.core.constants.doc.JEFileType;
import com.je.core.constants.message.WxMessageType;
import com.je.core.util.JEUUID;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.document.util.JeFileUtil;
import com.je.message.vo.DdMsgVo;
import me.chanjar.dingding.common.api.DdConsts;
import me.chanjar.dingding.common.bean.result.DdError;
import me.chanjar.dingding.common.bean.result.DdMediaUploadResult;
import me.chanjar.dingding.common.exception.DdErrorException;
import me.chanjar.dingding.common.util.http.DingDingTools;
import me.chanjar.dingding.cp.api.DdCpInMemoryConfigStorage;
import me.chanjar.dingding.cp.api.DdCpService;
import me.chanjar.dingding.cp.api.DdCpServiceImpl;
import me.chanjar.dingding.cp.bean.DdCpMessage;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 钉钉企业号工具类
 * @author zhangshuaipeng
 *
 */
public class DdCpUtil {
	/**
	 * 当前缓存的用户信息
	 */
	//public static Map<String, List<DynaBean>> wxCpUserInfos=new HashMap<String, List<DynaBean>>();
	private static DdCpUtil ddCpUtil = null;
	private DdCpUtil(){}
	public static DdCpUtil getInstance(){
		if(ddCpUtil==null){
			ddCpUtil=new DdCpUtil();
		}
		return ddCpUtil;
	}
	public void sendDdMsg(DdMsgVo msgVo){
		List<String> wxUserIds=new ArrayList<String>();
		Map<String,Object> sysVars=WebUtils.getAllSysVar(StringUtil.getDefaultValue(msgVo.getJtgsId(),msgVo.getCurrentUser().getJtgsId()));
		String userIds=msgVo.getUserIds();
		String deptIds="";
		JeFileUtil jeFileUtil=JeFileUtil.getInstance();
		DdCpService ddCpService=initWxCpService(sysVars);
		//构建消息信息
		try {
			DdCpMessage ddCpMsg=null;
			Boolean allUser=false;
			if("@all".equals(userIds)){
				allUser=true;
			}else{
				deptIds=msgVo.getDeptIds();
			}
//			Long agentId=265281416L;
			Long agentId=Long.parseLong(WebUtils.getBackVar("dingdingAgentId"));
			if(StringUtil.isNotEmpty(WebUtils.getSysVar("DINGTALK_AGENTID"))){
				agentId=Long.parseLong(WebUtils.getSysVar("DINGTALK_AGENTID"));
			}

			OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
			request.setUseridList(userIds);
			request.setDeptIdList(deptIds);
			request.setAgentId(agentId);
			request.setToAllUser(allUser);
			OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
			if(WxMessageType.WZ.equals(msgVo.getType())){
				msg.setMsgtype("text");
				msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
				msg.getText().setContent(msgVo.getContent());
			}else if(WxMessageType.TP.equals(msgVo.getType())){
				if(StringUtil.isNotEmpty(msgVo.getFilePath())){
					InputStream is=jeFileUtil.readFileIo(msgVo.getFilePath(), JEFileType.PROJECT);
					DdMediaUploadResult uploadMediaRes = ddCpService.mediaUpload(DdConsts.MEDIA_IMAGE, msgVo.getFileName(), is);
					jeFileUtil.closeInputStream(is);
					if(uploadMediaRes!=null){
						msg.setMsgtype(DdConsts.MEDIA_IMAGE);
						msg.setImage(new OapiMessageCorpconversationAsyncsendV2Request.Image());
						msg.getImage().setMediaId(uploadMediaRes.getMediaId());
					}else{
						throw new DdErrorException(buildError(90001, "图片文件上传失败!"));
					}
				}else{
					throw new DdErrorException(buildError(90001, "图片文件上传失败!"));
				}
			}else if(WxMessageType.YY.equals(msgVo.getType())){
				if(StringUtil.isNotEmpty(msgVo.getFilePath())){
					InputStream is=jeFileUtil.readFileIo(msgVo.getFilePath(), JEFileType.PROJECT);
					DdMediaUploadResult uploadMediaRes = ddCpService.mediaUpload(DdConsts.MEDIA_VOICE, msgVo.getFileName(), is);
					if(uploadMediaRes!=null){
						InputStream is2=jeFileUtil.readFileIo(msgVo.getFilePath(), JEFileType.PROJECT);
						String filePath=jeFileUtil.saveFile(is2,msgVo.getFilePath(),JEFileType.PLATFORM);
						File file=jeFileUtil.readFile(filePath,JEFileType.PLATFORM);
						DingDingTools dt=new DingDingTools();
						Long l=dt.getAmrDuration(file);
						msg.setMsgtype(DdConsts.MEDIA_VOICE);
						msg.setVoice(new OapiMessageCorpconversationAsyncsendV2Request.Voice());
						msg.getVoice().setMediaId(uploadMediaRes.getMediaId());
						Long time=l;
						msg.getVoice().setDuration(String.valueOf(time));
						jeFileUtil.closeInputStream(is);
						jeFileUtil.closeInputStream(is2);
						jeFileUtil.deleteFile(filePath,JEFileType.PLATFORM);
					}else{
						throw new DdErrorException(buildError(90003, "语音文件上传失败!"));
					}
				}else{
					throw new DdErrorException(buildError(90003, "语音文件上传失败!"));
				}
			}else if(WxMessageType.WJ.equals(msgVo.getType())){
				if(StringUtil.isNotEmpty(msgVo.getFilePath())){
					InputStream is=jeFileUtil.readFileIo(msgVo.getFilePath(), JEFileType.PROJECT);
					DdMediaUploadResult uploadMediaRes = ddCpService.mediaUpload(DdConsts.MEDIA_FILE, msgVo.getFileName(), is);
					jeFileUtil.closeInputStream(is);
					if(uploadMediaRes!=null){
						msg.setMsgtype("file");
						msg.setFile(new OapiMessageCorpconversationAsyncsendV2Request.File());
						msg.getFile().setMediaId(uploadMediaRes.getMediaId());
					}else{
						throw new DdErrorException(buildError(90005, "文件上传失败!"));
					}
				}else{
					throw new DdErrorException(buildError(90005, "文件上传失败!"));
				}
			}else if(WxMessageType.LJ.equals(msgVo.getType())){
				if(StringUtil.isNotEmpty(msgVo.getFilePath())){
					InputStream is=jeFileUtil.readFileIo(msgVo.getFilePath(), JEFileType.PROJECT);
					DdMediaUploadResult uploadMediaRes = ddCpService.mediaUpload(DdConsts.MEDIA_IMAGE, msgVo.getFileName(), is);
					jeFileUtil.closeInputStream(is);
					if(uploadMediaRes!=null){
						msg.setMsgtype(DdConsts.MEDIA_LINK);
						msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
						msg.getLink().setTitle(msgVo.getTitle());
						msg.getLink().setText(msgVo.getContent());
						msg.getLink().setMessageUrl(msgVo.getSourceUrl());
						msg.getLink().setPicUrl(uploadMediaRes.getMediaId());
					}else{
						throw new DdErrorException(buildError(90005, "文件上传失败!"));
					}
				}else{
					throw new DdErrorException(buildError(90005, "文件上传失败!"));
				}
			}else if(WxMessageType.OA.equals(msgVo.getType())){
				DdMediaUploadResult uploadMediaRes=null;
				if(StringUtil.isNotEmpty(msgVo.getFilePath())){
					InputStream is=jeFileUtil.readFileIo(msgVo.getFilePath(), JEFileType.PROJECT);
					uploadMediaRes = ddCpService.mediaUpload(DdConsts.MEDIA_IMAGE, msgVo.getFileName(), is);
					jeFileUtil.closeInputStream(is);
				}
				msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
				OapiMessageCorpconversationAsyncsendV2Request.Body body = new OapiMessageCorpconversationAsyncsendV2Request.Body();
				if(uploadMediaRes!=null){
					body.setImage(uploadMediaRes.getMediaId());
				}
				msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
				msg.getOa().setPcMessageUrl(msgVo.getPcUrl());
				msg.getOa().setMessageUrl(msgVo.getMobileUrl());
				msg.getOa().getHead().setText(msgVo.getTitle());
				if(!msgVo.getBgColor().equals("")){
					msg.getOa().getHead().setBgcolor("100"+msgVo.getBgColor().replace("#",""));
				}
				body.setForm(msgVo.getListFrom());
				OapiMessageCorpconversationAsyncsendV2Request.Rich rich = new OapiMessageCorpconversationAsyncsendV2Request.Rich();
				rich.setUnit(msgVo.getRichUnit());
				rich.setNum(msgVo.getRichNum());
				body.setRich(rich);
				body.setContent(msgVo.getContent());
				body.setTitle(msgVo.getBodyTitle());
				body.setFileCount(msgVo.getFileCount());
				body.setAuthor(msgVo.getAuthor());
				msg.setMsgtype(DdConsts.OA);
				msg.getOa().setBody(body);
			}else if(WxMessageType.MD.equals(msgVo.getType())){
				msg.setMsgtype(DdConsts.MARKDOWN);
				msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
				msg.getMarkdown().setText(msgVo.getContent());
				msg.getMarkdown().setTitle(msgVo.getTitle());
			}else if(WxMessageType.KP.equals(msgVo.getType())){
				msg.setMsgtype(DdConsts.ACTIONCARD);
				msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
				msg.getActionCard().setTitle(msgVo.getTitle());//通知标题
				msg.getActionCard().setMarkdown(msgVo.getContent());//内容md
				List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnlist = msgVo.getBtnJsonList();
				if(btnlist.size()==1){
					OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btn= new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
					btn=btnlist.get(0);
					msg.getActionCard().setSingleTitle(btn.getTitle());//单行按钮
					msg.getActionCard().setSingleUrl(btn.getActionUrl());//单行链接
				}else if(btnlist.size()>1){
					msg.getActionCard().setBtnOrientation(msgVo.getBtnOrientation());
					msg.getActionCard().setBtnJsonList(btnlist);
				}
			}
			if(msg!=null){
				request.setMsg(msg);
				ddCpService.messageSend(request);
			}
		} catch (DdErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化服务类
	 * @param sysVars
	 * @return
	 */
	public DdCpService initWxCpService(Map<String,Object> sysVars) {
		// TODO Auto-generated method stub
		DdCpService ddCpService=new DdCpServiceImpl();
		DdCpInMemoryConfigStorage config = new DdCpInMemoryConfigStorage();
		config.setCorpId(StringUtil.getDefaultValue(sysVars.get("DINGTALK_APPKEY"),""));
		config.setCorpSecret(StringUtil.getDefaultValue(sysVars.get("DINGTALK_APPID"),""));
		int agentId=0;
		if(StringUtil.isNotEmpty(WebUtils.getSysVar("DINGTALK_AGENTID"))){
			agentId=Integer.parseInt(WebUtils.getSysVar("DINGTALK_AGENTID"));
		}
		config.setAgentId(agentId);     // 设置钉钉应用ID
		ddCpService.setWxCpConfigStorage(config);
		return ddCpService;
	}
	public static String formatError(String errorInfoStr) {
		// TODO Auto-generated method stub
		Map<Integer,String> errorInfos=new HashMap<Integer,String>();
		errorInfos.put(-1, "系统繁忙");
		errorInfos.put(0, "请求成功");
		errorInfos.put(404, "请求的URI地址不存在");
		errorInfos.put(33001, "无效的企业ID!");
		errorInfos.put(33002, "无效的微应用的名称!");
		errorInfos.put(33003, "无效的微应用的描述!");
		errorInfos.put(33004, "无效的微应用的ICON!");
		errorInfos.put(33005, "无效的微应用的移动端主页!");
		errorInfos.put(33006, "无效的微应用的PC端主页!");

		errorInfos.put(34001, "无效的会话id!");
		errorInfos.put(34002, "无效的会话消息的发送者!");
		errorInfos.put(34003, "无效的会话消息的发送者的企业Id!");
		errorInfos.put(34004, "无效的会话消息的类型!");
		errorInfos.put(34005, "无效的会话音频消息的播放时间!");
		errorInfos.put(34006, "发送者不在企业中!");
		errorInfos.put(34007, "发送者不在会话中!");

		errorInfos.put(34008, "图片不能为空!");
		errorInfos.put(34009, "链接内容不能为空!");
		errorInfos.put(34010, "文件不能为空!");
		errorInfos.put(34011, "音频文件不能为空!");
		errorInfos.put(34012, "找不到发送者的企业!");
		errorInfos.put(34013, "找不到群会话对象!");
		errorInfos.put(34014, "会话消息的json结构无效或不完整!");
		errorInfos.put(34015, "发送群会话消息失败!");
		errorInfos.put(34016, "消息内容长度超过限制!");
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
		errorInfos.put(48002, "Api禁用！");
		errorInfos.put(48003, "不合法的suite_id");
		errorInfos.put(48004, "授权关系无效");
		errorInfos.put(48005, "API接口已废弃");
		errorInfos.put(50001, "redirect_uri未授权");
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
		errorInfos.put(90001, "您的服务器调用钉钉开放平台所有接口的请求都被暂时禁用了!");
		errorInfos.put(90002, "您的服务器调用钉钉开放平台当前接口的所有请求都被暂时禁用了！");
		errorInfos.put(90003, "您的企业调用钉钉开放平台所有接口的请求都被暂时禁用了，仅对企业自己的Accesstoken有效!");
		errorInfos.put(90004, "您当前使用的CorpId及CorpSecret被暂时禁用了，仅对企业自己的Accesstoken有效!");
		errorInfos.put(90005, "您的企业调用当前接口次数过多，请求被暂时禁用了，仅对企业自己的Accesstoken有效!");
		String error="执行出错，请联系管理员!";
		errorInfos.put(-1, "不合法的请求字符");
		if(StringUtil.isNotEmpty(errorInfoStr) && errorInfoStr.startsWith("{") && errorInfoStr.endsWith("}")){
			JSONObject errorInfo=  JSONObject.fromObject(errorInfoStr);
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
	 * 构建钉钉错误信息
	 * @param errcode
	 * @param errMsg
	 * @return
	 */
	public static DdError buildError(int errcode, String errMsg){
		DdError ddErr=new DdError();
		ddErr.setErrorCode(errcode);
		ddErr.setErrorMsg(errMsg);
		return ddErr;
	}
//	public static void main(String[] args) {
//		String text=FileOperate.readTxt("F:/gongweifawen_16111614-62.mx.xml", "UTF-8", "");
//		System.out.println(text);
//	}
}
