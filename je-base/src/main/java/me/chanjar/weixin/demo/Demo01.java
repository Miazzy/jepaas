package me.chanjar.weixin.demo;

import java.util.List;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews.WxMpMaterialNewsArticle;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;

public class Demo01 {

	/**
	 * @param args
	 * @throws WxErrorException 
	 */
	public static void main(String[] args) throws WxErrorException {
		// TODO Auto-generated method stub
		WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
		config.setAppId("wx880cb61f22ef1d33"); // 设置微信公众号的appid
		config.setSecret("e6df2c7bfdee4708779ea07cd07eeff8"); // 设置微信公众号的app corpSecret
		config.setToken("jesaas201405"); // 设置微信公众号的token
		config.setAesKey("CXMyYK4UbN9B5su8Et8VQj1zAG2vQ8kbO36hwUDfEw3"); // 设置微信公众号的EncodingAESKey

		WxMpService wxService = new WxMpServiceImpl();
		wxService.setWxMpConfigStorage(config);
		// 用户的openid在下面地址获得 
		// https://mp.weixin.qq.com/debug/cgi-bin/apiinfo?t=index&type=用户管理&form=获取关注者列表接口%20/user/get 
		String openid = "oksCav0bXhrt1T2MIN70cpfe7GzQ";//云凤程的OPENID
		//WxMpKefuMessage message = WxMpKefuMessage.TEXT().toUser(openid).content("欢迎使用JEPLUS快速开发平台 , http://jepf3.com").build();
		//wxService.getKefuService().sendKefuMessage(message);	
		//wxService.getUserService().userUpdateRemark(openid, remark)
		//System.out.println(wxService.getUserService().userInfo(openid).getNickname());
		//wxService.getBlackListService().
//		for (WxUserTag wxUserTag : tag) {
//			System.out.println(wxUserTag.getName()+ wxUserTag.getId());
//		}
		WxMpMaterialService materialService = wxService.getMaterialService();
		materialService.materialFileBatchGet("image", 0, 20);
//		for (WxMaterialNewsBatchGetNewsItem ma : batchGetNewsItems) {
//			System.out.println(ma.getMediaId());
//		}
//		-R5sFE6jlvy0CPfKtp_CsTU0MypVlbe5C_3BoaRnN9I
//		-R5sFE6jlvy0CPfKtp_CsWhdHKTudV4IDWuaqq2P_rM
//		-R5sFE6jlvy0CPfKtp_CsZr_UstZoRkO0G0xFLVhH-w
//		-R5sFE6jlvy0CPfKtp_CsYs-mcJrRRZf5kPMpXPh9V8
//		-R5sFE6jlvy0CPfKtp_Csa6zd1gXI3aJpTaR-AIbqyg
//		-R5sFE6jlvy0CPfKtp_CsZeVG8VElNqTk96Yk7gIv-I
//		-R5sFE6jlvy0CPfKtp_CsQzsEplFPlEBkn-76c94Chs
		WxMpMaterialNews materialNews = materialService.materialNewsInfo("-R5sFE6jlvy0CPfKtp_CsTU0MypVlbe5C_3BoaRnN9I");
		List<WxMpMaterialNewsArticle> articles = materialNews.getArticles();
		System.out.println(articles.size());
		for (WxMpMaterialNewsArticle mp : articles) {
			System.out.println(mp.getContent());
		}
		
		
		WxMpMaterialFileBatchGetResult batchGetResult2 = materialService.materialFileBatchGet("image", 0, 20);
		//materialService.materialImageOrVoiceDownload(mediaId)
		List<WxMaterialFileBatchGetNewsItem> batchGetNewsItems2 = batchGetResult2.getItems();
		for (WxMaterialFileBatchGetNewsItem f : batchGetNewsItems2) {
			System.out.println(f.getMediaId()+"   "+f.getName());
		}
	}

}








