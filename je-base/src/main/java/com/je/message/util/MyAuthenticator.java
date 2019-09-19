package com.je.message.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
/**
 * 邮箱密码验证器
 * @author zhangshuaipeng
 *
 */
public class MyAuthenticator extends Authenticator {
	String userName=null;//用户名
    String password=null;//密码
        
    public MyAuthenticator(){   
    }   
    public MyAuthenticator(String username, String password) {    
        this.userName = username;    
        this.password = password;    
    }    
    protected PasswordAuthentication getPasswordAuthentication(){   
        return new PasswordAuthentication(userName, password); 
    }
}
