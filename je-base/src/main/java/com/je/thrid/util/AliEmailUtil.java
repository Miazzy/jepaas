package com.je.thrid.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.message.vo.EmailMsgVo;

/**
 * 阿里云邮件服务工具类
 */
public class AliEmailUtil {
    private static String regionId="";
    private static String accessKey="";
    private static String accessSecret="";
    //控制台创建的发信地址
    private static String fromAddress="";
    //发信人昵称
    private static String fromNickName="";
    //控制台创建的标签
    private static String fromLabel="";
    public static void init(){
        if(StringUtil.isEmpty(regionId)){
            regionId = WebUtils.getBackVar("ALIYUN_EMAIL_REGIONID");
            accessKey = WebUtils.getBackVar("ALIYUN_IP_EMAIL_ACCESSKEY");
            accessSecret = WebUtils.getBackVar("ALIYUN_EMAIL_ACCESSSECRET");
            fromAddress = WebUtils.getBackVar("ALIYUN_EMAIL_FROMADDRESS");
            fromNickName = WebUtils.getBackVar("ALIYUN_EMAIL_FROMNICKNAME");
            fromLabel = WebUtils.getBackVar("ALIYUN_EMAIL_FROMLABEL");
        }
    }
    public static String sendEmail(EmailMsgVo msgVo){
        init();
        String errorCode="";
        try {
            IClientProfile profile=DefaultProfile.getProfile(regionId,accessKey,accessSecret);
            IAcsClient client = new DefaultAcsClient(profile);
            SingleSendMailRequest request = new SingleSendMailRequest();
            if(StringUtil.isNotEmpty(msgVo.getFb())) {
                request.setAccountName(msgVo.getFb());
            }else{
                request.setAccountName(fromAddress);
            }
            if(StringUtil.isNotEmpty(msgVo.getFaster())){
                request.setFromAlias(msgVo.getFaster());
            }else{
                request.setFromAlias(fromNickName);
            }
            request.setAddressType(1);
            if(StringUtil.isNotEmpty(msgVo.getCs())) {
                request.setTagName(msgVo.getCs());
            }else{
                request.setTagName(fromLabel);
            }
            request.setReplyToAddress(true);
            request.setToAddress(msgVo.getReceiveEmail());
            request.setSubject(msgVo.getSubject());
            request.setHtmlBody(msgVo.getContext());
            //开启需要备案，0关闭，1开启
            //request.setClickTrace("0");
            //如果调用成功，正常返回httpResponse；如果调用失败则抛出异常，需要在异常中捕获错误异常码；错误异常码请参考对应的API文档;
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
        }catch (ServerException e) {
            //捕获错误异常码
            errorCode=e.getErrCode();
            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        } catch (ClientException e) {
            errorCode=e.getErrCode();
            e.printStackTrace();
        }
        return errorCode;
    }
}
