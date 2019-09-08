package rtx;

/**
 * 封装对RTX的操作
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Tencent C0. Ltd</p>
 * @author herolin
 * @version 1.0
 */
public class RTXSvrApi {

    //客户端与server传递的协议
    static int PRO_ADDUSER = 0x0001; //添加用户信息
    static int PRO_DELUSER = 0x0002; //删除用户信息
    static int PRO_GETUSERDETAILINFO = 0x0004; //获取用户详细信息
    static int PRO_SETUSERDETAILINFO = 0x0005; //更新用户详细信息
    static int PRO_GETUSERSMPLINFO = 0x0006; //获取用户简要信息
    static int PRO_SETUSERSMPLINFO = 0x0003; //更改用户简要信息
    static int PRO_SETUSERPRIVILEGE = 0x0007; //设置用户权限
    static int PRO_IFEXIST = 0x0008; //判断用户是否存在
    static int PRO_TRANUSER = 0X0009; //用户名与UIN的互转

    static int PRO_ADDDEPT = 0x0101; //增加部门
    static int PRO_DELDEPT = 0x0102; //删除部门
    static int PRO_SETDEPT = 0x0103; //更新部门信息
    static int PRO_GETCHILDDEPT = 0x0104; //获取子部门
    static int PRO_GETDEPTALLUSER = 0x0105; //获取部门用户
    static int PRO_SETDEPTPRIVILEGE = 0x0106; //设置部门权限
    static int PRO_GETDEPTSMPLINFO = 0x0107; //获取部门信息

    static int PRO_SMS_LOGON = 0x1000;
    static int PRO_SMS_SEND = 0x1001; //发送短信 多个手机号码(逗号分隔)可群发
    static int PRO_SMS_NICKLIST = 0x1002; //按用户读取短信
    static int PRO_SMS_FUNCLIST = 0x1003; //按功能号读取短信
    static int PRO_SMS_CHECK = 0x1004; //获取未读短信数量

    static int PRO_SYS_GETSESSIONKEY = 0x2000; //单点验证获取sessionKey
    static int PRO_SYS_GETUSERSTATUS = 0x2001; //查询用户在线状态
    static int PRO_SYS_SENDIM = 0x2002; //发送IM消息
    static int PRO_SYS_SESSIONKEYVERIFY = 0x2003; //验证sessionKey

    static int PRO_EXT_NOTIFY = 0x2100; //即时消息提醒

	static int PRO_IMPORTUSER = 0x0001; //导入xml用户数据
	static int PRO_EXMPORTUSER = 0x0002; //导出xml用户数据

    //对象名称
    static String OBJNAME_RTXEXT = "EXTTOOLS"; //扩展对象
    static String OBJNAME_RTXSYS = "SYSTOOLS"; //系统对象
    static String OBJNAME_DEPTMANAGER = "DEPTMANAGER"; //部门对象
    static String OBJNAME_USERMANAGER = "USERMANAGER"; //用户对象
    static String OBJNAME_SMSMANAGER = "SMSOBJECT"; //短信对象
    static String OBJNAME_USERSYNC = "USERSYNC"; //用户数据导入导出


    static String KEY_TYPE = "TYPE";

    static String KEY_USERID = "USERID";
    static String KEY_USERNAME = "USERNAME";
    static String KEY_UINTYPE = "UINTYPE";
    static String KEY_UIN = "UIN"; //RTX编号
    static String KEY_NICK = "NICK"; //登陆名
    static String KEY_MOBILE = "MOBILE"; //手机
    static String KEY_OUTERUIN = "OUTERUIN";
    static String KEY_LASTMODIFYTIME = "LASTMODIFYTIME";
    static String KEY_FACE = "FACE";	//头像
    static String KEY_PASSWORD = "PWD";	//密码

    static String KEY_AGE = "AGE";	//年龄
    static String KEY_GENDER = "GENDER";	//性别
    static String KEY_BIRTHDAY = "BIRTHDAY";	//生日
    static String KEY_BLOODTYPE = "BLOODTYPE";	//血型
    static String KEY_CONSTELLATION = "CONSTELLATION";	//星座
    static String KEY_COLLAGE = "COLLAGE";	//大学
    static String KEY_HOMEPAGE = "HOMEPAGE";	//主页
    static String KEY_EMAIL = "EMAIL";	//邮箱
    static String KEY_PHONE = "PHONE";	//电话
    static String KEY_FAX = "FAX";	//电话分机号码
    static String KEY_ADDRESS = "ADDRESS";	//地址
    static String KEY_POSTCODE = "POSTCODE";	//邮编号码
    static String KEY_COUNTRY = "COUNTRY";	//国家
    static String KEY_PROVINCE = "PROVINCE";	//省份
    static String KEY_CITY = "CITY";	//城市
    static String KEY_MEMO = "MEMO";	//个人说明
    static String KEY_STREET = "STREET";	//街道
    static String KEY_MOBILETYPE = "MOBILETYPE";
    static String KEY_AUTHTYPE = "AUTHTYPE";
    static String KEY_POSITION = "POSITION";
    static String KEY_OPENGSMINFO = "OPENGSMINFO";
    static String KEY_OPENCONTACTINFO = "OPENCONTACTINFO";
    static String KEY_PUBOUTUIN = "PUBOUTUIN";
    static String KEY_PUBOUTNICK = "PUBOUTNICK";
    static String KEY_PUBOUTNAME = "PUBOUTNAME";
    static String KEY_PUBOUTDEPT = "PUBOUTDEPT";
    static String KEY_PUBOUTPOSITION = "PUBOUTPOSITION";
    static String KEY_PUBOUTINFO = "PUBOUTINFO";
    static String KEY_OUTERPUBLISH = "OUTERPUBLISH";

    static String KEY_LDAPID = "LDAPID";
    static String KEY_DEPTID = "DEPTID";
    static String KEY_PDEPTID = "PDEPTID";
    static String KEY_SORTID = "SORTID";
    static String KEY_NAME = "NAME";
    static String KEY_INFO = "INFO";
    static String KEY_COMPLETEDELBS = "COMPLETEDELBS";


    //权限相关
    static String KEY_DENY = "DENY";
    static String KEY_ALLOW = "ALLOW";

    static String KEY_SESSIONKEY = "SESSIONKEY";


	//导入导出xml数据相关
	static String KEY_MODIFYMODE = "MODIFYMODE";
	static String KEY_DATA = "DATA";

    //短信相关
    static String KEY_SENDER = "SENDER";
    static String KEY_FUNNO = "FUNCNO";
    static String KEY_RECEIVER = "RECEIVER";
    static String KEY_RECEIVERUIN = "RECEIVERUIN";
    static String KEY_SMS = "SMS";
    static String KEY_CUT = "CUT";
    static String KEY_NOTITLE = "NOTITLE";
    static String KEY_DELFLAG = "DELFLAG";


    //RTXServer业务逻辑
    static String KEY_RECVUSERS = "RECVUSERS";
    static String KEY_IMMSG = "IMMSG";


    //消息提醒
    static String KEY_MSGID = "MSGID";
    static String KEY_MSGINFO = "MSGINFO";
    static String KEY_ASSISTANTTYPE = "ASSTYPE";
    static String KEY_TITLE = "TITLE";
	static String KEY_DELAYTIME = "DELAYTIME";

    //结果集合的描述
    static String KEY_RESULT_INCODE = "INNERCODE"; //内部错误
    static String KEY_RESULT_ERR_INFO = "ERR_INFO";
    static String KEY_RESULT_CODE = "CODE"; //返回错误
    static String KEY_RESULT_TYPE = "TYPE"; //返回类型
    static String KEY_RESULT_NAME = "NAME";
    static String KEY_RESULT_VALUE = "VALUE";
    static String KEY_RESULT_VARIANT = "VARIANT";


    //私有属性
    private int iObj;
    private int iProp;
    private int innerCode;
    private int iResult;

    static {
        System.loadLibrary("SDKAPIJava");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                         系统自带的函数                                                           //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//初始化类
    public native boolean Init();

	//析构类
    public native void UnInit();

	//通过int类型的错误代码获取错误信息
    public native String GetError(int innerCode);

	//获取版本号
    public native String GetVersion();

	//通过对象名称创建一个对象,对象名称如OBJNAME_RTXEXT，返回int类型的句柄，在本文称为iObjectHandle
    public native int GetNewObject(String szObjectName);

	//通过句柄获取对象名称(该方法一般用不上)
    public native String GetObjectName(int iObjectHandle);

	//设置句柄的对象名称(该方法一般用不上)
    public native int SetObjectName(int iObjectHandle, String szObjectName);

	//获取属性集合句柄
    public native int GetNewPropertys();

	//该int类型的数是否是句柄 (该方法一般用不上)
    public native int IsHandle(int iHandle);

	//引用计数(该方法一般用不上)
    public native int AddRefHandle(int iHandle);

	//释放句柄
    public native int ReleaseHandle(int iHandle);

	//向属性集合添加属性,iHandle表示属性集合的句柄
    public native int AddProperty(int iPropertyHandle, String szName, String szValue);

	//在属性集合中删除某个属性，通过索引删除
    public native int ClearProperty(int iPropertyHandle, int iIndex);

	//在属性集合中删除某个属性,通过属性名删除
    public native int RemoveProperty(int iPropertyHandle, String szName);

	//获取属性集合中某个属性的值
    public native String GetProperty(int iPropertyHandle, String szName);

	//设置服务器IP地址
    public native int SetServerIP(int iObjectHandle, String szServerIP);

	//获取服务器IP地址
    public native String GetServerIP(int iObjectHandle);

	//获取服务器IP地址
    public native int GetServerPort(int iObjectHandle);

	//设置服务器端口
    public native int SetServerPort(int iObjectHandle, int iPort);

	//获取属性集合中属性的总数
    public native int GetPropertysCount(int iHandle);

	//通过索引获取属性集合中的条目
    public native int GetPropertysItem(int iHandle, int iIndex);

	//调用SDK，传入对象句柄，属性句柄，命令ID
    public native int Call(int iObjectHandle, int iPropertyHandle, int iCmdID);

	//获取返回结果的集性集合，传入一个返回结果的句柄
    public native int GetResultPropertys(int iResultHandle);

	//通过传入一个返回结果的句柄，获取该句柄的结果，以int类型表示
    public native int GetResultInt(int iResultHandle);

	//通过传入一个返回结果的句柄，获取该句柄的结果，以String类型表示
    public native String GetResultString(int iResultHandle);

	//获取一个属性的名称
    public String GetPropertyItemName(int iHandle) {
        return GetProperty(iHandle, KEY_RESULT_NAME);
    }

	//获取一个属性的值
    public String GetPropertyItemValue(int iHandle) {
        return GetProperty(iHandle, KEY_RESULT_VALUE);
    }

	//获取内部错误代码
    public int GetResultInnerCode(int iHandle) {
        String sz = GetProperty(iHandle, KEY_RESULT_INCODE);
        return Integer.parseInt(sz);
    }
	//获取内部错误信息
    public String GetResultErrString(int iHandle) {
        String sz = GetProperty(iHandle, KEY_RESULT_ERR_INFO);
        return sz;
    }
	//获取SDK调用返回结果的Code，0表示成功，其他表示失败
    public int GetResultCode(int iHandle) {
        String sz = GetProperty(iHandle, KEY_RESULT_CODE);
        return Integer.parseInt(sz);
    }

	//获取某个属性的类型(一般用不上)
    public int GetResultType(int iHandle) {
        String sz = GetProperty(iHandle, KEY_RESULT_TYPE);
        return Integer.parseInt(sz);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                           扩展的函数                                                           //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 初始化对象
     * @param objName String
     */
    private void svrInit(String objName) {
        iObj = GetNewObject(objName);
        iProp = GetNewPropertys();
    }

    /**
     * 释放资源
     */
    private void release() {
        ReleaseHandle(iObj);
        ReleaseHandle(iProp);
        ReleaseHandle(iResult);
    }

   /**
    * 设置服务器IP
    * @param strIP String
    */
   public void setServerIP(String strIP){
       svrInit(OBJNAME_RTXSYS);
       SetServerIP(iObj,strIP);
       release();

   }

   /**
    * 设置服务器端口
    * @param iPort int 端口，默认是6000
    */
   public void setServerPort(int iPort){
       svrInit(OBJNAME_RTXSYS);
       SetServerPort(iObj,iPort);
       release();
   }

   /**
    * 取服务器IP
    *@param iPort int
    */
   public String getServerIP(){
       svrInit(OBJNAME_RTXSYS);
       String ip=GetServerIP(iObj);
       release();
       return ip;
   }

   /**
    * 取服务器端口
    * @return String 服务器地址
    */
   public int getServerPort(){
       svrInit(OBJNAME_RTXSYS);
       int port=GetServerPort(iObj);
       release();
       return port;
   }

    /**
     * 查询用户在线状态
     * @param UserName String 用户帐号
     * @return int 0:离线 1:在线 2:离开 11:不存在该用户 其它:未知状态
     */
    public int QueryUserState(String UserName) {

        svrInit(OBJNAME_RTXSYS);

        AddProperty(iProp, KEY_USERNAME, UserName);
         //AddProperty(iProp,KEY_UINTYPE,"Account"); 如果设置KEY_UINTYPE 为Account，无论传进去是纯数字还是字符串，都认为是帐号。

        int iResult = Call(iObj, iProp, PRO_SYS_GETUSERSTATUS);
        int innerCode = GetResultInnerCode(iResult);
        int iRTXState = GetResultInt(iResult);
        if (innerCode != 0) {
            System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
            release();
            return innerCode;
        }

    	release();
        return iRTXState;

    }


    /**
     * 删除用户信息
     * @param String UserName用户帐号
     * @return int  0 操作成功 非0为失败
     */
    public int deleteUser(String UserName) {
        if(UserName==null || "".equals(UserName)){
            return -1;
        }

        svrInit(OBJNAME_USERMANAGER);

        AddProperty(iProp,KEY_USERNAME,UserName);
       //AddProperty(iProp,KEY_UINTYPE,"Account"); 如果设置KEY_UINTYPE 为Account，无论传进去是纯数字还是字符串，都认为是帐号。

        iResult=Call(iObj,iProp,PRO_DELUSER);
        innerCode = GetResultInnerCode(iResult);
        if(innerCode!=0){
			System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
		}

        release();
        return innerCode;
    }


    /**
     * 新增用户
     * @param UserName string 用户帐号
     * @param DeptID String 部门ID
     * @paramint ChsName String 用户姓名
     * @paramint Pwd String 密码
     * @return int  0 操作成功 非0为失败
     */
    public int addUser(String UserName, String DeptID, String ChsName, String Pwd ) {

        svrInit(OBJNAME_USERMANAGER);

        AddProperty(iProp,KEY_NICK,UserName); //用户名
        AddProperty(iProp, KEY_DEPTID, DeptID); //组织ID
        AddProperty(iProp, KEY_NAME, ChsName); //真实名
        AddProperty(iProp, KEY_PASSWORD, Pwd);//密码

        iResult=Call(iObj,iProp,PRO_ADDUSER);
        innerCode = GetResultInnerCode(iResult);

        if (innerCode != 0)
        {
            System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
        }

        release();
        return innerCode;
    }

    /**
     * 查看用户简单资料
     * @param UserName String 用户帐号
     * @return String[][]  操作成功返回一个二维数组 失败为null
      */
	public String[][] GetUserSimpleInfo(String UserName)
	{

		String[][] info=null;

		svrInit(OBJNAME_USERMANAGER);

        AddProperty(iProp, KEY_USERNAME, UserName);
        //AddProperty(iProp,KEY_UINTYPE,"Account"); 如果设置KEY_UINTYPE 为Account，无论传进去是纯数字还是字符串，都认为是帐号。

        int iResult = Call(iObj, iProp, PRO_GETUSERSMPLINFO);
        int innerCode = GetResultInnerCode(iResult);
        if (innerCode != 0) {
            System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
        } else {
            int iProps = GetResultPropertys(iResult);
            int iCount = GetPropertysCount(iProps);
            info = new String[iCount][2];
            for (int i = 0; i < iCount; i++) {
                int iHandle = GetPropertysItem(iProps, i);
                info[i][0] = GetPropertyItemName(iHandle);
                info[i][1] = GetPropertyItemValue(iHandle);
                ReleaseHandle(iHandle);
            }
        }

        release();
        return info;
	}

	/**
     * 设置用户简单资料
     * @param UserName String 用户帐号
     * @param ChsName String 用户姓名
     * @param email String 邮箱地址
     * @param gender String 性别，男为0，女为1
     * @param mobile String 手机号码
     * @param phone String  电话
     * @param pwd String 密码
     * @return int  0 操作成功 非0为失败
      */
	public int SetUserSimpleInfo(String UserName,String ChsName,String email,String gender,String mobile,String phone,String pwd)
	{

		svrInit(OBJNAME_USERMANAGER);

		//把资料读出来再写回去
        AddProperty(iProp, KEY_USERNAME, UserName);
         //AddProperty(iProp,KEY_UINTYPE,"Account"); 如果设置KEY_UINTYPE 为Account，无论传进去是纯数字还是字符串，都认为是帐号。

        iResult = Call(iObj, iProp, PRO_GETUSERSMPLINFO);
        innerCode = GetResultInnerCode(iResult);
        if ( innerCode!= 0) {
            System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
            release();
        	return innerCode;
        } else {
            int iProps = GetResultPropertys(iResult);
            int iCount = GetPropertysCount(iProps);

           for (int i=0 ;i  < iCount ; i++)
           {
           		if (i==9)
           		{
           			if (pwd != null && !"".equals(pwd) && !"null".equals(pwd)) {
        				AddProperty(iProp, KEY_PASSWORD, pwd); //密码
        			}
           		}
           		else
           		{
           			int iHandle = GetPropertysItem(iProps, i);
            		AddProperty(iProp, GetPropertyItemName(iHandle), GetPropertyItemValue(iHandle));
            		ReleaseHandle(iHandle);
           		}

           }

        }

        //更新用户信息
        if (ChsName != null && !"".equals(ChsName) && !"null".equals(ChsName)) {
        	AddProperty(iProp, KEY_NAME, ChsName); //中文名
        }
        if (gender != null && !"".equals(gender) && !"null".equals(gender)) {
        	AddProperty(iProp, KEY_GENDER, gender); //性别
        }
        if (mobile != null && !"".equals(mobile) && !"null".equals(mobile)) {
        	AddProperty(iProp, KEY_MOBILE, mobile); //手机
        }
        if (phone != null && !"".equals(phone) && !"null".equals(phone)) {
        	AddProperty(iProp, KEY_PHONE, phone); //电话
        }
        if (email != null && !"".equals(email) && !"null".equals(email)) {
        	AddProperty(iProp, KEY_EMAIL, email); //邮箱
        }

       //AddProperty(iProp,KEY_UINTYPE,"Account"); 如果设置KEY_UINTYPE 为Account，无论传进去是纯数字还是字符串，都认为是帐号。

		//设置把用户简单资料写回去
        int iResult = Call(iObj, iProp, PRO_SETUSERSMPLINFO);
        int innerCode = GetResultInnerCode(iResult);
        if (innerCode != 0) {
        	System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
    	}

        release();
        return innerCode;
	}

	/**
     * 设置用户简单资料，支持设置部门ID
     * @param UserName String 用户帐号
     * @param DeptID String 部门ID
     * @param ChsName String 用户姓名
     * @param email String 邮箱地址
     * @param gender String 性别，男为0，女为1
     * @param mobile String 手机号码
     * @param phone String  电话
     * @param pwd String 密码
     * @return int  0 操作成功 非0为失败
      */
	public int SetUserSimpleInfoEx(String UserName,String DeptID, String ChsName,String email,String gender,String mobile,String phone,String pwd)
	{

		svrInit(OBJNAME_USERMANAGER);

		//把资料读出来再写回去
        AddProperty(iProp, KEY_USERNAME, UserName);

        iResult = Call(iObj, iProp, PRO_GETUSERSMPLINFO);
        innerCode = GetResultInnerCode(iResult);
        if ( innerCode!= 0) {
            System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
            release();
        	return innerCode;
        } else {
            int iProps = GetResultPropertys(iResult);
            int iCount = GetPropertysCount(iProps);

           for (int i=0 ;i  < iCount ; i++)
           {
           		if (i==9)
           		{
           			if (pwd != null && !"".equals(pwd) && !"null".equals(pwd)) {
        				AddProperty(iProp, KEY_PASSWORD, pwd); //密码
        			}
           		}
           		else
           		{
           			int iHandle = GetPropertysItem(iProps, i);
            		AddProperty(iProp, GetPropertyItemName(iHandle), GetPropertyItemValue(iHandle));
            		ReleaseHandle(iHandle);
           		}

           }

        }

        //更新用户信息
        if (DeptID != null && !"".equals(DeptID) && !"null".equals(DeptID)) {
        	AddProperty(iProp, KEY_DEPTID, DeptID); //部门ID
        }
        if (ChsName != null && !"".equals(ChsName) && !"null".equals(ChsName)) {
        	AddProperty(iProp, KEY_NAME, ChsName); //中文名
        }
        if (gender != null && !"".equals(gender) && !"null".equals(gender)) {
        	AddProperty(iProp, KEY_GENDER, gender); //性别
        }
        if (mobile != null && !"".equals(mobile) && !"null".equals(mobile)) {
        	AddProperty(iProp, KEY_MOBILE, mobile); //手机
        }
        if (phone != null && !"".equals(phone) && !"null".equals(phone)) {
        	AddProperty(iProp, KEY_PHONE, phone); //电话
        }
        if (email != null && !"".equals(email) && !"null".equals(email)) {
        	AddProperty(iProp, KEY_EMAIL, email); //邮箱
        }


		//设置把用户简单资料写回去
        int iResult = Call(iObj, iProp, PRO_SETUSERSMPLINFO);
        int innerCode = GetResultInnerCode(iResult);
        if (innerCode != 0) {
        	System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
    	}

        release();
        return innerCode;
	}

    /**
     * 查看用户详细资料
     * @param UserName String 用户帐号
     * @return String[][] 操作成功返回一个二维数组 失败返回null
     */
    public String[][] GetUserDetailInfo(String UserName) {

		String[][] info=null;
    	svrInit(OBJNAME_USERMANAGER);

        AddProperty(iProp, KEY_USERNAME, UserName);
         //AddProperty(iProp,KEY_UINTYPE,"Account"); 如果设置KEY_UINTYPE 为Account，无论传进去是纯数字还是字符串，都认为是帐号。

        int iResult = Call(iObj, iProp, PRO_GETUSERDETAILINFO);
        int innerCode = GetResultInnerCode(iResult);
        if (innerCode != 0) {
            System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
        } else {
            int iProps = GetResultPropertys(iResult);
            int iCount = GetPropertysCount(iProps);
            info = new String[iCount][2];
            for (int i = 0; i < iCount; i++) {
                int iHandle = GetPropertysItem(iProps, i);
                info[i][0] = GetPropertyItemName(iHandle);
                info[i][1] = GetPropertyItemValue(iHandle);
                ReleaseHandle(iHandle);
            }
        }

        release();
        return info;
    }

    /**
     * 更新用户信息
     * @param UserName String
     * @param ....... String
     * @param MOBILE String
     * @return int  0 操作成功 非0为失败
     */
    public int setUserDetailInfo(String UserName,String ADDRESS,String AGE,String BIRTHDAY,
    String BLOODTYPE,String CITY,String COLLAGE,String CONSTELLATION,String COUNTRY,String FAX,
    String HOMEPAGE,String MEMO,String POSITION,String POSTCODE,String PROVINCE,String STREET,
    String PHONE,String MOBILE) {

		//主要是得到DetpID
		svrInit(OBJNAME_USERMANAGER);

        AddProperty(iProp, KEY_USERNAME, UserName);
        //AddProperty(iProp,KEY_UINTYPE,"Account"); 如果设置KEY_UINTYPE 为Account，无论传进去是纯数字还是字符串，都认为是帐号。

        iResult = Call(iObj, iProp, PRO_GETUSERSMPLINFO);
        innerCode = GetResultInnerCode(iResult);
        if ( innerCode!= 0) {
            System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
            release();
        	return innerCode;
        } else {
            int iProps = GetResultPropertys(iResult);
            int iHandle = GetPropertysItem(iProps, 0);
            AddProperty(iProp, KEY_DEPTID, GetPropertyItemValue(iHandle));
            ReleaseHandle(iHandle);
        }


        if (ADDRESS != null && !"".equals(ADDRESS) && !"null".equals(ADDRESS)) {
            AddProperty(iProp, KEY_ADDRESS, ADDRESS); //住址
        }
        if (BLOODTYPE != null && !"".equals(BLOODTYPE) && !"null".equals(BLOODTYPE)) {
            AddProperty(iProp, KEY_BLOODTYPE, BLOODTYPE); //血型
        }
        if (COUNTRY != null && !"".equals(COUNTRY) && !"null".equals(COUNTRY)) {
            AddProperty(iProp, KEY_COUNTRY, COUNTRY); //国家
        }
        if (PROVINCE != null && !"".equals(PROVINCE) && !"null".equals(PROVINCE)) {
            AddProperty(iProp, KEY_PROVINCE, PROVINCE); //省
        }
        if (CITY != null && !"".equals(CITY) && !"null".equals(CITY)) {
            AddProperty(iProp, KEY_CITY, CITY); //城市
        }
        if (POSTCODE != null && !"".equals(POSTCODE) && !"null".equals(POSTCODE)) {
            AddProperty(iProp, KEY_POSTCODE, POSTCODE); //邮政编码
        }
        if (HOMEPAGE != null && !"".equals(HOMEPAGE) && !"null".equals(HOMEPAGE)) {
            AddProperty(iProp, KEY_HOMEPAGE, HOMEPAGE); //个人主页
        }
        if (PHONE != null && !"".equals(PHONE) && !"null".equals(PHONE)) {
            AddProperty(iProp, KEY_PHONE, PHONE); //电话
        }
        if (MOBILE != null && !"".equals(MOBILE) && !"null".equals(MOBILE)) {
            AddProperty(iProp, KEY_MOBILE, MOBILE); //移动电话
        }
        if (MEMO != null && !"".equals(MEMO) && !"null".equals(MEMO)) {
            AddProperty(iProp, KEY_MEMO, MEMO); //个人简介
        }
        if (POSITION != null && !"".equals(POSITION) && !"null".equals(POSITION)) {
            AddProperty(iProp, KEY_POSITION, POSITION); //职务
        }
        if (FAX != null && !"".equals(FAX) && !"null".equals(FAX)) {
            AddProperty(iProp, KEY_FAX, FAX); //传真
        }
        if (AGE != null && !"".equals(AGE) && !"null".equals(AGE)) {
            AddProperty(iProp, KEY_AGE, AGE); //年龄
        }
        if (BIRTHDAY != null && !"".equals(BIRTHDAY) && !"null".equals(BIRTHDAY)) {
            AddProperty(iProp, KEY_BIRTHDAY, BIRTHDAY); //生日
        }
        if (COLLAGE != null && !"".equals(COLLAGE) && !"null".equals(COLLAGE)) {
            AddProperty(iProp, KEY_COLLAGE, COLLAGE); //大学
        }
        if (STREET != null && !"".equals(STREET) && !"null".equals(STREET)) {
            AddProperty(iProp, KEY_STREET, STREET); //街道
        }

        //AddProperty(iProp,KEY_UINTYPE,"Account"); 如果设置KEY_UINTYPE 为Account，无论传进去是纯数字还是字符串，都认为是帐号。

        iResult = Call(iObj, iProp, PRO_SETUSERDETAILINFO);
        innerCode = GetResultInnerCode(iResult);
        if (innerCode != 0) {
        	System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
    	}
        release();
        return innerCode;
    }

    /**
     * 把RTX号码转换为呢称
     * @param  UIN String RTX号码
     * @return int  0 操作成功 非0为失败
     */
	public String UinToUserName ( String UIN)
	{
		String UserName = null;
		svrInit(OBJNAME_USERMANAGER);

        AddProperty(iProp,KEY_USERNAME,UIN); //RTX号码

        iResult=Call(iObj,iProp,PRO_GETUSERSMPLINFO);
        innerCode = GetResultInnerCode(iResult);
        if ( innerCode != 0){
        	System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
        	release();
        	return null;
        }

    	int iProps = GetResultPropertys(iResult);
        int iHandle = GetPropertysItem(iProps, 7);
        UserName = GetPropertyItemValue(iHandle);
        ReleaseHandle(iHandle);
        release();
        return UserName;

	}

    /**
     * 添加组织信息
     * @param deptId String		部门ID
     * @param DetpInfo String	部门信息
     * @param DeptName String	部门名称
     * @param ParentDeptId String 	父部门ID
     * @param type String 	0:只删除当前组织 1:删除下级组织及用户
     * @return int  0 操作成功 非0为失败
     */
    public int addDept(String deptId,String DetpInfo,String DeptName,String ParentDeptId ) {

        svrInit(OBJNAME_DEPTMANAGER);

        AddProperty(iProp,KEY_DEPTID,deptId);
        AddProperty(iProp,KEY_NAME,DeptName);
        AddProperty(iProp,KEY_INFO,DetpInfo);
        AddProperty(iProp,KEY_PDEPTID,ParentDeptId);

        iResult = Call(iObj, iProp, PRO_ADDDEPT);
        innerCode = GetResultInnerCode(iResult);
        if(innerCode!=0){
			System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
        }

        release();
        return innerCode;
    }

    /**
     * 修改组织信息
     * @param deptId String 	部门ID
     * @param DetpInfo string 	部门信息
     * @param DeptName string 	部门名称
     * @param ParentDeptId string	父部门ID
     * @return int  0 操作成功 非0为失败
     */
    public int setDept(String deptId,String DetpInfo,String DeptName,String ParentDeptId ) {

        svrInit(OBJNAME_DEPTMANAGER);

        AddProperty(iProp,KEY_DEPTID,deptId);
        AddProperty(iProp,KEY_NAME,DeptName);
        AddProperty(iProp,KEY_INFO,DetpInfo);
        AddProperty(iProp,KEY_PDEPTID,ParentDeptId);

        iResult = Call(iObj, iProp, PRO_SETDEPT);
        innerCode = GetResultInnerCode(iResult);
        if(innerCode!=0){
			System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
        }

        release();
        return innerCode;
    }

    /**
     * 删除组织信息
     * @param deptId String	部门
     * @param type String 0:只删除当前组织 1:删除下级组织及用户
     * @return int  0 操作成功 非0为失败
     */
    public int deleteDept(String deptId,String type) {

        svrInit(OBJNAME_DEPTMANAGER);

        AddProperty(iProp, KEY_DEPTID, deptId);
        AddProperty(iProp,KEY_COMPLETEDELBS,type);

        iResult = Call(iObj, iProp, PRO_DELDEPT);
        innerCode = GetResultInnerCode(iResult);
        if(innerCode!=0){
			System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
        }

        release();
        return innerCode;
    }

    /**
     * 判断某个组织是否存在
     * @param deptId String 部门ID
     * @return int 0:存在 非0:不存在
     */
    public int deptIsExist(String deptId) {
        if(deptId==null || "".equals(deptId)){
            return -100;
        }
        //注意,2006已经没有PRO_GETDEPTSMPLINFO接口，因此判断一个部门是否存在通过
        //添加一个部门，如果添加成功，把这个部门删掉，如果添加失败，则说明部门已存在

        String pdeptid = "0";
		String pdeptname = "tempdept";

        svrInit(OBJNAME_DEPTMANAGER);

        AddProperty(iProp,KEY_PDEPTID,pdeptid);
        AddProperty(iProp,KEY_DEPTID,deptId);
        AddProperty(iProp,KEY_NAME,pdeptname);

        iResult=Call(iObj,iProp,PRO_ADDDEPT);
        innerCode = GetResultInnerCode(iResult);

        if (innerCode == -5)
        {
        	release();
        	return innerCode;
        }
        else if (innerCode ==0)
        {
        	Call(iObj,iProp,PRO_DELDEPT);
        	release();
        	return innerCode;
        }
        else
        {
        	System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
        }
        release();
        return innerCode;
    }

    /**
     * 取当前组织下的用户
     * @param DeptID String 部门ID
     * @return String[] 成功返回部门下用户帐号数组，失败返回null
     */
    public String[] getDeptUsers(String DeptID){

        String[] user=null;

        svrInit(OBJNAME_DEPTMANAGER);

        AddProperty(iProp,KEY_DEPTID,DeptID);

        iResult =Call(iObj, iProp, PRO_GETDEPTALLUSER);
        innerCode = GetResultInnerCode(iResult);
        if (innerCode != 0)
        {
        	System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
        }
        int iProps=GetResultPropertys(iResult);
        int iCount=GetPropertysCount(iProps);
        if (iCount > 0) {
            user = new String[iCount];
            for (int i = 0; i < iCount; i++) {
                int iHandler = GetPropertysItem(iProps, i);
                user[i]=UinToUserName(GetPropertyItemValue(iHandler)) ; 	//iHandler获取出来的是RTX号码
                ReleaseHandle(iHandler);
            }
        }

        release();
        return user;
    }


    /**
     * 取当前组织下的子部门ID
     * @param DeptID String 部门ID
     * @return String[] 成功返回子部门数组，失败返回null
     */
    public String[] getChildDepts(String DeptID){

        String[] detps=null;

        svrInit(OBJNAME_DEPTMANAGER);

        AddProperty(iProp,KEY_PDEPTID,DeptID);

        iResult =Call(iObj, iProp, PRO_GETCHILDDEPT);
        innerCode = GetResultInnerCode(iResult);
        if(innerCode!=0){
          	System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
          	release();
          	return null;
        }

        int iProps=GetResultPropertys(iResult);
        int iCount=GetPropertysCount(iProps);
        if (iCount > 0) {
            detps = new String[iCount];
            for (int i = 0; i < iCount; i++) {
                int iHandler = GetPropertysItem(iProps, i);
                detps[i]=GetPropertyItemValue(iHandler);
                ReleaseHandle(iHandler);
            }
        }

        release();
        return detps;
    }

    /**
     * 判断某个用户是否存在
     * @param UserName String 用户帐号
     * @return int 0:存在 非0:不存在
     */
    public int userIsExist(String UserName) {

        svrInit(OBJNAME_USERMANAGER);

        AddProperty(iProp,KEY_USERNAME,UserName);
        AddProperty(iProp,KEY_UINTYPE,"Account"); //如果设置KEY_UINTYPE 为Account，无论传进去是纯数字还是字符串，都认为是帐号。

        iResult = Call(iObj, iProp, PRO_IFEXIST);
        innerCode = GetResultInnerCode(iResult);
        int iExist=GetResultInt(iResult);
        release();
        if(innerCode!=0){
          	System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
        }

         return iExist;
    }

   /**
    * 获取sessionKey
    * @param UserName String 用户帐号
    * @return String 成功返回SessionKey，失败返回null
    */
   public String getSessionKey(String UserName){

       svrInit(OBJNAME_RTXSYS);

       AddProperty(iProp,KEY_USERNAME,UserName);
       iResult=Call(iObj,iProp,PRO_SYS_GETSESSIONKEY);

       innerCode = GetResultInnerCode(iResult);
       String szKey = "";
       if(innerCode!=0){
           	System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
       }else{
           szKey=GetResultString(iResult);
       }

       release();
       return szKey;

   }


   /**
     * 发送消息提醒
     * @param receivers String 接收人(多个接收人以逗号分隔)
     * @param title String 消息标题
     * @param msg String 消息内容
     * @param type String 0:普通消息 1:紧急消息
     * @param delayTime String 显示停留时间(毫秒) 0:为永久停留(用户关闭时才关闭)
     * @return int 0:操作成功 非0:操作不成功
     */
    public int sendNotify(String receivers,String title,String msg, String type,String delayTime) {

        svrInit(OBJNAME_RTXEXT);

        AddProperty(iProp, KEY_USERNAME, receivers);
        AddProperty(iProp, KEY_TITLE, title);
        AddProperty(iProp, KEY_MSGINFO, msg);
        AddProperty(iProp, KEY_TYPE, type);
        AddProperty(iProp, KEY_MSGID, "0");
        AddProperty(iProp, KEY_ASSISTANTTYPE, "0");
//       AddProperty(iProp,KEY_UINTYPE,"Account"); //如果设置KEY_UINTYPE 为Account，无论传进去是纯数字还是字符串，都认为是帐号。

        if(!"0".equals(delayTime))
            AddProperty(iProp, KEY_DELAYTIME, delayTime);

        iResult = Call(iObj, iProp, PRO_EXT_NOTIFY);
        innerCode = GetResultInnerCode(iResult);

        release();
        return innerCode;
    }

    /**
    * 发送短信
    * @param sender String 发送人
    * @param receiver String 接收人(RTX用户或手机号码均可,最多128个)
    * @param smsInfo String 短信内容
    * @param autoCut int 是否自动分条发送 0:否 1:是
    * @param noTitle int 是否自动填写标题 0:自动 1:制定
    * @return int 成功返回0，失败返回其他
    */
   public int sendSms(String sender, String receiver, String smsInfo,int autoCut, int noTitle) {

       svrInit(OBJNAME_SMSMANAGER);

       AddProperty(iProp, KEY_SENDER, sender);
       AddProperty(iProp, KEY_RECEIVER, receiver);
       AddProperty(iProp, KEY_SMS, smsInfo);
       AddProperty(iProp, KEY_CUT, String.valueOf(autoCut));
       AddProperty(iProp, KEY_NOTITLE, String.valueOf(noTitle));
       //AddProperty(iProp,KEY_UINTYPE,"Account"); 如果设置KEY_UINTYPE 为Account，无论传进去是纯数字还是字符串，都认为是帐号。

       iResult = Call(iObj, iProp, PRO_SMS_SEND);
       innerCode = GetResultInnerCode(iResult);
       if ( innerCode != 0)
       {
       		System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
       }
       release();
       return innerCode;
   }

    /**
    * 导出rtx的用户数据为xml
    * @return string  成功返回RTX用户数据的xml,失败返回null
    */
	public String exportXmldata()
	{
		String strResult = null;
		svrInit(OBJNAME_USERSYNC);

		AddProperty(iProp,"MODIFYMODE","1");
		AddProperty(iProp,"XMLENCODE","<?xml version=\"1.0\" encoding=\"gb2312\" ?>");

		int iResult =Call(iObj, iProp, PRO_EXMPORTUSER);
		innerCode = GetResultInnerCode(iResult);
		if ( innerCode != 0)
		{
			System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
		}
	    strResult = GetResultString(iResult);

		release();
		return strResult;
	}

	/**
    * 导入xml的用户数据
    * @param xmldata String xml用户数据
    * @return int :成功返回部门名称，失败返回null
    */
	public int  importXmldata(String xmldata)
	{
		svrInit(OBJNAME_USERSYNC);

		AddProperty(iProp,KEY_MODIFYMODE,"1");
		AddProperty(iProp,KEY_DATA,xmldata);

		int iResult =Call(iObj, iProp, PRO_IMPORTUSER);
		innerCode = GetResultInnerCode(iResult);
		if ( innerCode != 0)
		{
			System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
		}

		release();
		return innerCode;
	}

	/**
    * 获取部门名称
    * @param deptID String 部门ID
    * @return String 0:操作成功 非0:操作不成功
    */
	public String  GetDeptName(String deptID)
	{
		svrInit(OBJNAME_DEPTMANAGER);
		String info=null;

		AddProperty(iProp,KEY_DEPTID,deptID);

		int iResult =Call(iObj, iProp, PRO_GETDEPTSMPLINFO);
		innerCode = GetResultInnerCode(iResult);
		if ( innerCode != 0)
		{
			System.out.println("错误代码:" + innerCode + "\t" + "错误信息："+ GetResultErrString(iResult) );
		}
		else
		{
        		int iProps=GetResultPropertys(iResult);
        		int iCount=GetPropertysCount(iProps);
        		System.out.println("GetResultPropertys:"+iProps+ "\t" + "GetPropertysCount:"+iCount);

            		int iHandle = GetPropertysItem(iProps, 2);
                	info = GetPropertyItemValue(iHandle);
                	ReleaseHandle(iHandle);
                	System.out.println(info);
		}

		release();
		return info;
	}


}
