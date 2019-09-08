package me.chanjar.weixin.demo;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;

public class Demo02 {

	/**
	 * @param args
	 * @throws WxErrorException 
	 */
	public static void main(String[] args) throws WxErrorException {
		// TODO Auto-generated method stub
		WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
		config.setCorpId("wx05972e80c16dd1bb");      // 设置微信企业号的appid
		config.setCorpSecret("IpA6zJFz-lKcJ6UcFoytqXpd8wZwy4lepOccjEbGH1w");  // 设置微信企业号的app corpSecret
		config.setAgentId(0);     // 设置微信企业号应用ID
		config.setToken("...");       // 设置微信企业号应用的token
		config.setAesKey("...");      // 设置微信企业号应用的EncodingAESKey

		WxCpServiceImpl wxCpService = new WxCpServiceImpl();
		wxCpService.setWxCpConfigStorage(config);
		//rZZdo5vb4Ae6gLCOLMDUJc5rE0kHAQi-9BNqlQP_PAJVaEY1kt3a4AZxKK2fQNHZMa6_It_Or7soh0-tRJz3KKWxKCbbKPCcVHbIuJpsIrbtOUUeEqCJxwRD4PVA9oFiIbOJvnaFz6q2XPtpbd90G495xR82VCbHZc1pFSBAlka1I9ZTgAjorZ3ApBiJcWhFP9ayDuDRMyJoyeO2EyIjax5_4MUUG-wzS1RKEF9fM_EESRk0gstXnNpOnci5FajNV4VGZCWefcXGQee14bxS3OWaRUwugsqajdJbFYeFB0w
		wxCpService.departGet();
		String userId = "zhangsp";
		WxCpMessage message = WxCpMessage.TEXT().agentId(0).toUser(userId).content("Hello World").build();
		wxCpService.messageSend(message);
	}

}
