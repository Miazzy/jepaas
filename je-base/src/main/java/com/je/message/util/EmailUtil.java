package com.je.message.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.SharedByteArrayInputStream;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.je.cache.service.doc.FileTypeCacheManager;
import com.je.core.constants.DocumentType;
import com.je.core.constants.doc.JEFileType;
import com.je.core.constants.message.SendContextType;
import com.je.core.util.DateUtils;
import com.je.core.util.JEUUID;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.DynaBean;
import com.je.document.util.JeFileUtil;
import com.je.document.vo.FileType;
import com.je.message.service.DwrManager;
import com.je.message.vo.EmailInfoVo;
import com.je.message.vo.EmailMsgVo;
import com.sun.mail.util.BASE64DecoderStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Email发送的工具类
 *
 * @author zhangshuaipeng
 */
public class EmailUtil {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static EmailUtil emailUtil = null;
    /**正在接受的邮箱地址   key 为地址   value 为 命令状态  task定时任务  send 接受  end 结束*/
//	public static Map<String,String> runEmails=new HashMap<String,String>();
    /**系统用户通讯录*/
//	public static Map<String,String> sysAddresss=new HashMap<String,String>();

    /**
     * 个人通讯录
     */
//	public static Map<String,HashMap<String,String>> userAddresses=new HashMap<String,HashMap<String,String>>();
    private EmailUtil() {
    }

    public static EmailUtil getInstance() {
        if (emailUtil == null) {
            emailUtil = new EmailUtil();
        }
        return emailUtil;
    }

    /**
     * 发送Email
     * @param msgVo 消息
     * @param emailInfoVo TODO未处理
     * @return
     */
    @SuppressWarnings("static-access")
    public void sendEmail(EmailMsgVo msgVo, EmailInfoVo emailInfoVo) throws Exception {
        // TODO Auto-generated method stub
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = buildEmailConfig(emailInfoVo, msgVo);
        if (emailInfoVo != null) {
            if (StringUtil.isNotEmpty(emailInfoVo.getSendAuth()) && "0".equals(emailInfoVo.getSendAuth())) {

            } else {
                authenticator = new MyAuthenticator(emailInfoVo.getAddress(), emailInfoVo.getPassword());
            }
        } else {
            if ("1".equals(WebUtils.getSysVar("JE_SYS_EMAIL_SERVERVALIDATE"))) {
                //如果需要身份认证，则创建一个密码验证器
                authenticator = new MyAuthenticator(WebUtils.getSysVar("JE_SYS_EMAIL_USERNAME"), WebUtils.getSysVar("JE_SYS_EMAIL_PASSWORD"));
                if (StringUtil.isEmpty(WebUtils.getSysVar("JE_SYS_EMAIL_USERNAME")) || StringUtil.isEmpty(WebUtils.getSysVar("JE_SYS_EMAIL_PASSWORD"))) {

                    throw new PlatformException("邮箱发送未提供帐号密码异常!",PlatformExceptionEnum.JE_MESSAGE_EMAIL_NOUSERNAME_ERROR,new Object[]{msgVo,emailInfoVo});
                }
            }
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getInstance(pro, authenticator);
        // 根据session创建一个邮件消息
        Message mailMessage = new MimeMessage(sendMailSession);
        String title = WebUtils.getSysVar("JE_SYS_TITLE");//账户名使用系统名称
        String sendAddress = WebUtils.getSysVar("JE_SYS_EMAIL_USERNAME");
        if (emailInfoVo != null) {
            sendAddress = emailInfoVo.getAddress();
            InternetAddress from = new InternetAddress(sendAddress);
            if (StringUtil.isNotEmpty(title)) {
                from.setPersonal(title);
            }
            mailMessage.setFrom(from);
        } else {
            // 创建邮件发送者地址
            InternetAddress from = new InternetAddress(sendAddress);
            if (StringUtil.isNotEmpty(title)) {
                from.setPersonal(title);
            }
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
        }
        // 创建邮件的接收者地址，并设置到邮件消息中
        String receiveEmail = msgVo.getReceiveEmail();
        //如果是外部邮件发送
        if (emailInfoVo != null) {
            InternetAddress[] to = buildAddress(msgVo.getReceiveEmail());
            mailMessage.setRecipients(RecipientType.TO, to);
        } else {
            if (receiveEmail.split(",").length > 1) {
                InternetAddress[] to = new InternetAddress().parse(receiveEmail);
                mailMessage.setRecipients(RecipientType.TO, to);
            } else {
                Address to = new InternetAddress(msgVo.getReceiveEmail());
                mailMessage.setRecipient(RecipientType.TO, to);
            }
        }
        //设置抄送
        if (StringUtil.isNotEmpty(msgVo.getCs())) {
            if (emailInfoVo != null) {
                InternetAddress[] to = buildAddress(msgVo.getCs());
                mailMessage.setRecipients(RecipientType.CC, to);
            } else {
                InternetAddress[] to = new InternetAddress().parse(msgVo.getCs());
                mailMessage.setRecipients(RecipientType.CC, to);
            }
        }
        //设置密送
        if (StringUtil.isNotEmpty(msgVo.getMs())) {
            if (emailInfoVo != null) {
                InternetAddress[] to = buildAddress(msgVo.getMs());
                mailMessage.setRecipients(RecipientType.BCC, to);
            } else {
                InternetAddress[] to = new InternetAddress().parse(msgVo.getMs());
                mailMessage.setRecipients(RecipientType.BCC, to);
            }
        }
        //设置紧急状态
        String faster = "3";//默认紧急状态为普通
        if ("1".equals(msgVo.getFaster())) {
            faster = "1";
        }
        mailMessage.setHeader("X-Priority", faster);
        if ("1".equals(msgVo.getReplySign())) {
            mailMessage.setHeader("Disposition-Notification-To", sendAddress);
        }
        // 设置邮件消息的主题
        String sj = msgVo.getSubject();
        mailMessage.setSubject(sj);
        // 设置邮件消息发送的时间
        mailMessage.setSentDate(new Date());
        String contextType = "text/html";//纯文本
        if (SendContextType.TEXT.equals(msgVo.getContextType())) {
            contextType = "text/plain"; //超文本
        }
        // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
        Multipart mainPart = new MimeMultipart();
        // 创建一个包含内容的MimeBodyPart
        MimeBodyPart mbp = new MimeBodyPart();
        // 设置HTML内容
        mbp.setContent(msgVo.getContext(), contextType + "; charset=utf-8");
        mainPart.addBodyPart(mbp);
        //发送附件
        List<String> fileNames = msgVo.getFileNames();
        List<String> fileAddress = msgVo.getAddresses();
        if (fileAddress.size() > 0 && fileNames.size() == fileAddress.size()) {
            for (Integer i = 0; i < fileNames.size(); i++) {
                MimeBodyPart filePart = new MimeBodyPart();
                FileDataSource fileData = new FileDataSource(JeFileUtil.webrootAbsPath + fileAddress.get(i));
                filePart.setDataHandler(new DataHandler(fileData));
                filePart.setFileName(MimeUtility.encodeText(fileNames.get(i)));
                mainPart.addBodyPart(filePart);
            }
        }
        // 将MiniMultipart对象设置为邮件内容
        mailMessage.setContent(mainPart);
        // 发送邮件
        Transport.send(mailMessage);
    }

    /**
     * 接受邮件连接
     * @param emailInfoVo TODO未处理
     * @param type 类型
     * @return
     */
    public Store connect(EmailInfoVo emailInfoVo, String type) {
        try {
            Properties props = new Properties();
            if ("pop3".equals(type)) {
                props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.pop3.socketFactory.port", emailInfoVo.getPort());//收件端口
                Session session = Session.getDefaultInstance(props, null);//声明连接对象
                URLName urln = new URLName("pop3", emailInfoVo.getPop3(), emailInfoVo.getPort(), null, emailInfoVo.getAddress(), emailInfoVo.getPassword());//收件协议    帐号  密码
                Store store = session.getStore(urln);//声明数据集
                store.connect();//连接
                return store;
            } else {
                Session session = Session.getDefaultInstance(props, null);//声明连接对象
                URLName urln = new URLName("imap", emailInfoVo.getImap(), emailInfoVo.getImapPort(), null, emailInfoVo.getAddress(), emailInfoVo.getPassword());//收件协议    帐号  密码
                Store store = session.getStore(urln);//声明数据集
                store.connect();//连接
                return store;
            }
        } catch (Exception e) {
            throw new PlatformException("邮箱连接服务器异常", PlatformExceptionEnum.JE_MESSAGE_EMAIL_CONNECT_ERROR,new Object[]{emailInfoVo,type},e);
//            e.printStackTrace();
        }
//        return null;
    }

    /**
     * 邮箱获取目录
     * @param store TODO未处理
     * @param type 类型
     * @return
     */
    public Folder getFolder(Store store, String type) {
        try {
            String folderName = "INBOX";
            if ("imap".equals(type)) {
                folderName = "JUNK";
            }
            Folder folder = store.getFolder(folderName);
            folder.open(Folder.READ_WRITE);
            return folder;
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            throw new PlatformException("邮箱接受获取目录异常", PlatformExceptionEnum.JE_MESSAGE_EMAIL_GETFOLDER_ERROR,new Object[]{type},e);
        }
    }

    /**
     * 扫描邮箱获取消息
     * @param folder TODO未处理
     * @param startDate TODO未处理
     * @param dwrFlag TODO未处理
     * @param userId 用户ID
     * @param dwrManager 发送的消息
     * @param dwrObj TODO未处理
     * @return
     */
    public Message[] getMessages(Folder folder, Date startDate, Boolean dwrFlag, String userId, DwrManager dwrManager, JSONObject dwrObj) {
        try {
            Message[] emails = folder.getMessages();
            Message[] results = null;
            JSONObject sendObj = new JSONObject();
            sendObj.put("id", dwrObj.getString("id"));
            sendObj.put("count", emails.length);
            sendObj.put("lastDate", DateUtils.formatDateTime(startDate));
            //读取 开始时间之后的所有邮件
//	        if(startDate!=null){
//	       	 SearchTerm st = new SentDateTerm(ComparisonTerm.GT, startDate);//过滤条件
//	       	 emails=folder.search(st);
//	        }else{
//	       	 emails=folder.getMessages();
//	        }
            dwrObj.put("msg", "开始扫描...");
            if (dwrFlag) {
                dwrManager.sendMsg(userId, "msg2", dwrObj.toString(), "JE.sys.email.util.Util.updateReceiveInfo", "", false);
            }
            int index = -1;
            for (int i = emails.length - 1; i > 0; i--) {
                Message message = emails[i];
                Date date = message.getSentDate();
                Date sentDate = DateUtils.getDate(DateUtils.formatDateTime(date), DateUtils.DAFAULT_DATETIME_FORMAT);
                if (dwrFlag) {
                    sendObj.put("index", emails.length - i);
                    sendObj.put("nowDate", DateUtils.formatDateTime(sentDate));
                    dwrManager.sendMsg(userId, "search", sendObj.toString(), "JE.sys.email.util.Util.updateReceiveInfo", "", false);
                }
                if (!sentDate.after(startDate)) {
                    break;
                }
                index = i;
            }
            if (index != -1 && index < emails.length) {
                results = subArray(emails, index);
            } else {
                results = new Message[0];
            }
            return results;
        } catch (Exception e) {
            throw new PlatformException("邮箱接受获取消息内容异常", PlatformExceptionEnum.JE_MESSAGE_EMAIL_GETMESSAGE_ERROR,new Object[]{startDate,userId,dwrObj},e);
        }
    }

    /**
     * 关闭邮箱的方法
     * @param store TODO未处理
     * @param folder TODO未处理
     */
    public void close(Store store, Folder folder) {
        try {
            folder.close(false);
            store.close();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            throw new PlatformException("邮箱连接关闭异常", PlatformExceptionEnum.JE_MESSAGE_EMAIL_STORECLOSE_ERROR,e);
        }
    }

    /**
     * 读取邮件信息
     * @param email 邮箱
     * @param emailInfo TODO未处理
     * @param sendObj TODO未处理
     * @param dwrManager 发送的消息
     * @return
     * @throws FolderClosedException
     * @throws MessagingException
     */
    public List<DynaBean> buildEmail(MimeMessage email, DynaBean emailInfo, JSONObject sendObj, DwrManager dwrManager) throws FolderClosedException, MessagingException {
        try {
            List<DynaBean> documents = new ArrayList<DynaBean>();
            emailInfo.set("EMAILDATA_SJ", DateUtils.formatDateTime(email.getSentDate()));//发送时间
            emailInfo.set("EMAILDATA_MS", getMailAddress(email, RecipientType.BCC));//获取密送
            emailInfo.set("EMAILDATA_CS", getMailAddress(email, RecipientType.CC));//获取抄送
            emailInfo.set("EMAILDATA_SJR", getMailAddress(email, RecipientType.TO));//收件人
            emailInfo.set("EMAILDATA_FJR", getFrom(email));//发件人
            emailInfo.set("EMAILDATA_SFHZ", getReplySign(email));//是否回执
            emailInfo.set("EMAILDATA_JJZT", getPriority(email));//获取紧急状态
            String bt = email.getSubject();
            if (StringUtil.isEmpty(bt)) {
                bt = "(无主题)";
            }
            emailInfo.set("EMAILDATA_BT", bt);//主题
            emailInfo.set("EMAILDATA_CONTEXTTYPE", "HTML");//内容类型
            StringBuffer bodytext = new StringBuffer();
            getMailContent(email, bodytext, emailInfo, documents, sendObj, dwrManager);
            String content = bodytext.toString();
//	    	emailInfo.set("EMAILDATA_ZW", bodytext.toString());//获取邮件内容，  暂时未解析内容，只获取的HTML类型的内容
            String contentType = null;
//	    	try{
            contentType = email.getContentType();
//	    	}catch(Exception e){
//	    		//文本类型获取出错
//	    	}
            if (StringUtil.isNotEmpty(contentType) && contentType.indexOf("text/plain") != -1) {
                emailInfo.set("EMAILDATA_CONTEXTTYPE", "TEXT");
            }
            if (documents.size() > 0) {
                JSONArray arrays = new JSONArray();
                String ywFj = "0";
                for (DynaBean document : documents) {
                    String fileName = document.getStr("DOCUMENT_DOCNAME");
                    if ("0".equals(document.getStr("FILES")) && StringUtil.isNotEmpty(content) && content.indexOf("cid:" + fileName) != -1) {
                        String address = document.getStr("DOCUMENT_ADDRESS");
                        if (StringUtil.isNotEmpty(content)) {
                            content = content.replace("cid:" + fileName, address);
                        }
                    } else {
                        JSONObject docs = new JSONObject();
                        docs.put("name", document.getStr("DOCUMENT_DOCNAME"));
                        docs.put("path", document.getStr("DOCUMENT_ADDRESS"));
                        docs.put("cls", document.getStr("DOCUMENT_ICONCLS"));
                        docs.put("extend", document.getStr("DOCUMENT_THUMBNAILCLS"));
                        arrays.add(docs);
                        ywFj = "1";
                    }
                }
                emailInfo.set("EMAILDATA_YWFJ", ywFj);//有无附件
                emailInfo.set("EMAILDATA_FJ", arrays.toString());//附件
            }
            emailInfo.set("EMAILDATA_ZW", content);
            return documents;
        } catch (FolderClosedException fce) {
            throw fce;
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            throw new PlatformException("邮箱接受构建消息内容异常", PlatformExceptionEnum.JE_MESSAGE_EMAIL_BUILDMESSAGE_ERROR,new Object[]{emailInfo,sendObj},e);
//            e.printStackTrace();
        }
    }

    /**
     * 获取文本内容
     * @param email 邮箱
     * @param context 内容
     * @param emailInfo TODO未处理
     * @param documents TODO未处理
     * @param sendObj TODO未处理
     * @param dwrManager TODO未处理
     * @throws Exception
     */
    public void getMailContent(MimeMessage email, StringBuffer context, DynaBean emailInfo, List<DynaBean> documents, JSONObject sendObj, DwrManager dwrManager) throws Exception {

        Object o = null;
        String contextType = null;
        try {
            o = email.getContent();
            contextType = email.getContentType();
        } catch (Exception e) {
            throw new PlatformException("邮箱接受连接服务器异常", PlatformExceptionEnum.JE_MESSAGE_EMAIL_CONNECT_ERROR,new Object[]{emailInfo,sendObj},e);
            //e.printStackTrace();
        }
        if (o == null) {
            logger.error("读取邮件内容失败，内容类型为：" + contextType);
        } else if (o instanceof Multipart) {
            Multipart multipart = (Multipart) o;
            reMultipart(multipart, context, emailInfo, documents, sendObj, dwrManager);
        } else if (o instanceof Part) {
            Part part = (Part) o;
            rePart(part, context, emailInfo, documents, sendObj, dwrManager);
            //文本格式
        } else if (StringUtil.isNotEmpty(contextType) && contextType.toLowerCase().indexOf("text/plain") != -1) {
            context.append(o.toString());
        } else if (StringUtil.isNotEmpty(contextType) && contextType.toLowerCase().indexOf("text/html") != -1) {
            context.append(o.toString());
        }
//        else if (StringUtil.isNotEmpty(email.getContentType()) && StringUtil.isNotEmpty(email.getFileName()) && email.getInputStream()!=null){
//        	IMAPInputStream imapIs=(IMAPInputStream)email.getContent();
//        	imapIs.
//        	Part part = (Part) o;
//            rePart(part,context,documents);
        //文本格式
//        }
        else {
            logger.error("读取邮件内容失败，内容类型为：" + contextType);
        }
    }

    /**
     * 邮箱获取消息内容
     * @param multipart TODO未处理
     * @param context 内容
     * @param emailInfo TODO未处理
     * @param documents TODO未处理
     * @param sendObj TODO未处理
     * @param dwrManager  TODO未处理
     * @throws Exception
     */
    private void reMultipart(Multipart multipart, StringBuffer context, DynaBean emailInfo, List<DynaBean> documents, JSONObject sendObj, DwrManager dwrManager) throws Exception {
        // 依次处理各个部分
        for (int j = 0, n = multipart.getCount(); j < n; j++) {
            Part part = multipart.getBodyPart(j);//解包, 取出 MultiPart的各个部分, 每部分可能是邮件内容,
            // 也可能是另一个小包裹(MultipPart)
            // 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative
            Object contextObj = null;
            try {
                contextObj = part.getContent();
            } catch (Exception e) {
                throw new PlatformException("邮箱接受获取消息内容异常", PlatformExceptionEnum.JE_MESSAGE_EMAIL_CONNECT_ERROR,new Object[]{emailInfo,sendObj},e);
//            	e.printStackTrace();
            }
            if (contextObj != null && contextObj instanceof Multipart) {
                Multipart p = (Multipart) part.getContent();// 转成小包裹
                //递归迭代
                reMultipart(p, context, emailInfo, documents, sendObj, dwrManager);
            } else {
                rePart(part, context, emailInfo, documents, sendObj, dwrManager);
            }
        }
    }

    /**
     *  邮箱接收消息
     * @param part 解析内容
     * @param context 内容
     * @param emailInfo TODO未处理
     * @param documents TODO未处理
     * @param sendObj TODO未处理
     * @param dwrManager TODO未处理
     * @throws Exception
     */
    private void rePart(Part part, StringBuffer context, DynaBean emailInfo, List<DynaBean> documents, JSONObject sendObj, DwrManager dwrManager) throws Exception {
        Object contextObj = null;
        String attachment = null;
        String bt = "";
        try {
            contextObj = part.getContent();
            attachment = part.getDisposition();
            bt = emailInfo.getStr("EMAILDATA_BT");
        } catch (Exception e) {
            throw new PlatformException("邮箱接受获取消息内容异常", PlatformExceptionEnum.JE_MESSAGE_EMAIL_CONNECT_ERROR,new Object[]{emailInfo,sendObj},e);
        }
        if (attachment != null) {
            String fileName = part.getFileName();
            if (fileName != null) {
                fileName = MimeUtility.decodeText(fileName); //MimeUtility.decodeText解决附件名乱码问题
                //推送
                sendObj.put("fileMsg", "正在下载文件：" + fileName);
                if (sendObj.getBoolean("dwrFlag")) {
                    dwrManager.sendMsg(emailInfo.getStr("EMAILDATA_GSRID"), "accept", sendObj.toString(), "JE.sys.email.util.Util.updateReceiveInfo", "", false);
                }
                InputStream in = part.getInputStream();// 打开附件的输入流
                DynaBean document = doDocument(fileName, in);
                document.set("FILES", "1");
                documents.add(document);
            }
        } else if (contextObj != null && contextObj instanceof BASE64DecoderStream) {
            String fileName = part.getFileName();
            if (fileName != null) {
                fileName = MimeUtility.decodeText(fileName); //MimeUtility.decodeText解决附件名乱码问题
                //推送
                sendObj.put("fileMsg", "正在下载文件：" + fileName);
                if (sendObj.getBoolean("dwrFlag")) {
                    dwrManager.sendMsg(emailInfo.getStr("EMAILDATA_GSRID"), "accept", sendObj.toString(), "JE.sys.email.util.Util.updateReceiveInfo", "", false);
                }
                InputStream in = part.getInputStream();// 打开附件的输入流
                DynaBean document = doDocument(fileName, in);
                document.set("FILES", "0");
                documents.add(document);
            }
            //回执信
        } else if (contextObj != null && contextObj instanceof SharedByteArrayInputStream && StringUtil.isNotEmpty(bt) && bt.startsWith("已读")) {
            context.append("这是邮件收条,在" + emailInfo.getStr("EMAILDATA_SJ") + "时间主题为：" + bt.substring(3) + " 的信件已被接收，此收条只表明收件人的计算机上曾显示过此邮件");
        } else {
            if (StringUtil.isNotEmpty(part.getContentType()) && part.getContentType().toLowerCase().startsWith("text/plain")) {
//               context.append(part.getContent());
            } else if (StringUtil.isNotEmpty(contextObj + "")) {
                context.append(contextObj);
            }
        }
    }

    /**
     * 持久文件和document数据 TODO 张帅鹏  修改接受的邮件附件信息，document信息补全
     *
     * @param fileName
     * @param in
     * @return
     */
    public DynaBean doDocument(String fileName, InputStream in) {
        JeFileUtil jeFileUtil = JeFileUtil.getInstance();
        DynaBean document = new DynaBean("JE_CORE_DOCUMENT", false);
//		String dirFormat = ConfigCacheManager.getCacheValue("struts.upload.dirFormat");
//		SimpleDateFormat dirFormatter = new SimpleDateFormat(dirFormat);
        String docPath = WebUtils.getConfigVar("struts.upload.path");
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        String dirPath = docPath + "/email/" + DateUtils.formatDate(new Date(), "yyyyMM");
        String filePath = dirPath + "/" + JEUUID.uuid() + "." + fileType;
//		File dir=new File(JeFileUtil.webrootAbsPath+dirPath);
//		if(!dir.exists()){
//			dir.mkdirs();
//		}
        document.set("JE_CORE_DOCUMENT", "JE_CORE_DOCUMENT_ID");
        document.set("DOCUMENT_DOCNAME", fileName);
        document.set("DOCUMENT_ADDRESS", filePath);
        document.set("DOCUMENT_TYPE", DocumentType.BATCH);
        document.set("DOCUMENT_FILETYPE", fileType);
        document.set("DOCUMENT_TABLECODE", "JE_SYS_EMAILDATA");
        //构建文件类型 信息       包括图标   大图标    文件格式名
        FileType typeVo = jeFileUtil.getFileType(fileType);
        document.set("DOCUMENT_FILETYPENAME", typeVo.getName());
        document.set("DOCUMENT_ICONCLS", typeVo.getIconCls());
//        document.set("DOCUMENT_BIGICONCLS", typeVo.getBigIconCls());
        jeFileUtil.saveFile(in, filePath, JEFileType.PROJECT);
        File file=jeFileUtil.readFile(filePath,JEFileType.PROJECT);
        String jeFileType="PROJECT";
        String jeSaveFileType=JeFileUtil.getFileSaveType();
        String beanTableCode="JE_SYS_EMAILDATA";
//        String thumbnailCls=typeVo.getThumbnailCls();
//        String privateUrl="";
//        if(StringUtil.isNotEmpty(typeVo.getThumbnailCls())){
//            thumbnailCls="<div class=\""+typeVo.getThumbnailCls()+"\" > </div>";
//        }else{
//            String imgUrl="/je/doc/document/doLoadFile?path="+filePath+"&jeFileType="+jeFileType+"&jeSaveFileType="+jeSaveFileType+"&beanTableCode="+beanTableCode;
//            if(StringUtil.isNotEmpty(privateUrl)){
//                imgUrl=privateUrl;
//            }
//            thumbnailCls="<img width=96 height=96 src=\""+imgUrl+"\" />";
//        }
        document.set("DOCUMENT_THUMBNAILCLS", jeFileUtil.getImageInfo(filePath,file));
        document.set("DOCUMENT_DOCSIZE", file.length());
        document.set("DOCUMENT_BUSTYPE", jeFileType);
        document.set("DOCUMENT_SAVETYPE", jeSaveFileType);
        return document;
    }

    /**
     * 得到发件人
     *
     * @param email
     * @return
     * @throws MessagingException
     */
    public String getFrom(MimeMessage email) throws MessagingException {
        InternetAddress[] addresses = (InternetAddress[]) email.getFrom();
        JSONArray arrays = new JSONArray();
        if (addresses != null) {
            for (InternetAddress address : addresses) {
                JSONObject info = new JSONObject();
                if (StringUtil.isNotEmpty(address.getAddress())) {
                    info.put("code", address.getAddress());
                    if (StringUtil.isEmpty(address.getPersonal())) {
                        info.put("text", address.getAddress());
                    } else {
                        info.put("text", address.getPersonal());
                    }
                    arrays.add(info);
                }
            }
        }
        return arrays.toString();
    }

    /**
     * * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
     */
    public String getReplySign(MimeMessage email) throws MessagingException {
        String replysign = "0";
        String needreply[] = email.getHeader("Disposition-Notification-To");
        if (needreply != null) {
            replysign = "1";
        }
        return replysign;
    }

    /**
     * 获取优先级    优先级1、紧急 3、普通 5、缓慢
     *
     * @return
     */
    public String getPriority(MimeMessage email) throws MessagingException {
        String replysign = "3";
        String needreply[] = email.getHeader("X-Priority");
        if (needreply != null && needreply.length > 0 && StringUtil.isNotEmpty(needreply[0])) {
            replysign = needreply[0].substring(0, 1);
        }
        if (!"1".equals(replysign)) {
            replysign = "0";
        }
        return replysign;
    }

    /**
     * 得到抄送 密送
     *
     * @param email 邮箱
     * @param type  收件人   Message.RecipientType.TO  抄送   Message.RecipientType.CC 密送   Message.RecipientType.BCC
     * @return
     * @throws MessagingException
     */
    public String getMailAddress(MimeMessage email, RecipientType type) throws MessagingException {
        InternetAddress[] addresses = (InternetAddress[]) email.getRecipients(type);
        JSONArray arrays = new JSONArray();
        if (addresses != null) {
            for (InternetAddress ia : addresses) {
                JSONObject info = new JSONObject();
                if (StringUtil.isNotEmpty(ia.getAddress())) {
                    info.put("code", ia.getAddress());
                    if (StringUtil.isEmpty(ia.getPersonal())) {
                        info.put("text", ia.getAddress());
                    } else {
                        info.put("text", ia.getPersonal());
                    }
                    arrays.add(info);
                }
            }
        }
        return arrays.toString();
    }

    /**
     * 截取数组
     *
     * @param arrays 消息组
     * @param startIndex 启动下标
     * @return
     */
    public Message[] subArray(Message[] arrays, int startIndex) {
        Message[] results = new Message[arrays.length - startIndex];
        for (int i = startIndex; i < arrays.length; i++) {
            results[i - startIndex] = arrays[i];
        }
        return results;
    }

    /**
     * 构建Email配置
     * @param emailInfoVo 邮箱信息
     * @param msgVo 消息
     * @return
     */
    public Properties buildEmailConfig(EmailInfoVo emailInfoVo, EmailMsgVo msgVo) {
        Properties p = new Properties();
        p.put("mail.smtp.ssl.enable", true);
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        if (emailInfoVo != null) {
            p.put("mail.smtp.host", emailInfoVo.getSmtp());
            //p.put("mail.debug","true");
            p.put("mail.smtp.port", emailInfoVo.getSendPort() + "");
            String auth = "true";
            if (StringUtil.isNotEmpty(emailInfoVo.getSendAuth()) && "0".equals(emailInfoVo.getSendAuth())) {
                auth = "false";
            }
            p.put("mail.smtp.auth", auth);
        } else {
            p.put("mail.smtp.host", WebUtils.getSysVar("JE_SYS_EMAIL_SERVERHOST") + "");
            p.put("mail.smtp.port", WebUtils.getSysVar("JE_SYS_EMAIL_SERVERPORT") + "");
            p.put("mail.smtp.auth", "1".equals(WebUtils.getSysVar("JE_SYS_EMAIL_SERVERVALIDATE")) ? "true" : "false");
        }
        return p;
    }

    /**
     * TODO未处理
     * @param sjrStr TODO未处理
     * @return
     * @throws Exception
     */
    public InternetAddress[] buildAddress(String sjrStr) throws Exception {
        if (StringUtil.isNotEmpty(sjrStr)) {
            JSONArray arrays = JSONArray.fromObject(sjrStr);
            InternetAddress[] ias = new InternetAddress[arrays.size()];
            for (int i = 0; i < arrays.size(); i++) {
                JSONObject infos = arrays.getJSONObject(i);
                InternetAddress to = new InternetAddress(infos.getString("code"));
                to.setPersonal(infos.getString("text"));
                ias[i] = to;
            }
            return ias;
        } else {
            return null;
        }
    }
}
