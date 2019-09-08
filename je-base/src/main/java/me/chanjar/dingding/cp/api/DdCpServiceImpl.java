package me.chanjar.dingding.cp.api;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.je.core.facade.extjs.JsonAssist;
import com.je.core.util.WebUtils;
import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import me.chanjar.dingding.common.bean.result.DdError;
import me.chanjar.dingding.common.bean.result.DdMediaUploadResult;
import me.chanjar.dingding.common.exception.DdErrorException;
import me.chanjar.dingding.common.util.http.*;
import me.chanjar.weixin.common.session.StandardSessionManager;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.common.util.http.ApacheHttpClientBuilder;
import me.chanjar.weixin.common.util.http.DefaultApacheHttpClientBuilder;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DdCpServiceImpl implements DdCpService {

  protected final Logger log = LoggerFactory.getLogger(DdCpServiceImpl.class);

  /**
   * 全局的是否正在刷新access token的锁
   */
  protected final Object globalAccessTokenRefreshLock = new Object();

  /**
   * 全局的是否正在刷新jsapi_ticket的锁
   */
  protected final Object globalJsapiTicketRefreshLock = new Object();

  protected DdCpConfigStorage configStorage;

  protected CloseableHttpClient httpClient;

  protected HttpHost httpProxy;
  protected WxSessionManager sessionManager = new StandardSessionManager();
  /**
   * 临时文件目录
   */
  private int retrySleepMillis = 1000;
  private int maxRetryTimes = 5;

  @Override
  public String getAccessToken(){
    return getAccessToken(false);
  }

  @Override
  public String getAccessToken(boolean forceRefresh){
    if (forceRefresh) {
      this.configStorage.expireAccessToken();
    }
    if (this.configStorage.isAccessTokenExpired()) {
      synchronized (this.globalAccessTokenRefreshLock) {
        if (this.configStorage.isAccessTokenExpired()) {
          try {
            DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(this.configStorage.getCorpId());
            request.setAppsecret(this.configStorage.getCorpSecret());
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            String accessToken=response.getAccessToken();
            this.configStorage.updateAccessToken(
                    accessToken, 7200);
          } catch (ApiException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return this.configStorage.getAccessToken();
  }

  @Override
  public void messageSend(OapiMessageCorpconversationAsyncsendV2Request request) throws DdErrorException {
    execute(request);
  }

  @Override
  public DdMediaUploadResult mediaUpload(String mediaType, String fileName, InputStream inputStream){
    FileItem fileItem = new FileItem(fileName,inputStream);
    return mediaUpload(mediaType,fileItem);
  }

  @Override
  public DdMediaUploadResult mediaUpload(String mediaType, File file){
    FileItem fileItem = new FileItem(file);
    return mediaUpload(mediaType,fileItem);
  }

  @Override
  public DdMediaUploadResult mediaUpload(String mediaType, FileItem fileItem){
    DdMediaUploadResult dur=new DdMediaUploadResult();
    try {
      String accessToken = getAccessToken();
      DingTalkClient  client = new DefaultDingTalkClient("https://oapi.dingtalk.com/media/upload");
      OapiMediaUploadRequest request = new OapiMediaUploadRequest();
      request.setType(mediaType);
      request.setMedia(fileItem);
      OapiMediaUploadResponse response = client.execute(request,accessToken);
      if(response.getErrcode()==42001 || response.getErrcode()==40001 || response.getErrcode()==40014){
        this.configStorage.expireAccessToken();
        response = client.execute(request,accessToken);
      }
      dur.setMediaId(response.getMediaId());
      dur.setType(response.getType());
      return dur;
    } catch (ApiException e) {
      e.printStackTrace();
    }
    return dur;
  }

  @Override
  public Integer departCreate(WxCpDepart depart){
    String token=getAccessToken();
    depart.setId(null);
    String url="https://oapi.dingtalk.com/department/create?access_token="+token;
    int id=0;
    try {
      String jsonstr=JsonAssist.getInstance().buildModelJson(depart).replace("id","ids");
      String result = DingDingTools.httpPost(url, jsonstr.replace("parentId", "parentid"));
      Map<String,String> map= DingDingTools.jsonStr2Map(result);
      String idstr=map.get("id");
      id=Integer.valueOf(idstr);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return id;
  }

  @Override
  public void departUpdate(WxCpDepart group){
    String ACCESS_TOKEN=getAccessToken();
    String url = "https://oapi.dingtalk.com/department/update?access_token="+ACCESS_TOKEN;
    try {
      DingDingTools.httpPost(url, JsonAssist.getInstance().buildModelJson(group));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void departDelete(Integer departId) {
    String token=getAccessToken();
    String url="https://oapi.dingtalk.com/department/delete?access_token="+token+"&id="+departId;
    try {
      String result = DingDingTools.httpGet(url,"UTF-8");
      Map<String,String> map= DingDingTools.jsonStr2Map(result);
      if(map.get("errcode").equals("60005")){
        List<WxCpUser> list = userList(String.valueOf(departId));
        String[] userids=new String[list.size()];
        int i=0;
        for(WxCpUser u:list){
          userids[i]=u.getUserId();
          Integer[] deptids=u.getDepartIds();
          if(deptids.length>1){
            List<Long> deptidsLong= new ArrayList<>();
            for(Integer deptid:deptids){
              if(!deptid.equals(departId)){
                deptidsLong.add(Long.valueOf(deptid));
              }
            }
            u.setDepartIds(deptids);
            userUpdate(u);
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/update");
            OapiUserUpdateRequest request = new OapiUserUpdateRequest();
            request.setUserid(u.getUserId());
            request.setDepartment(deptidsLong);
            try {
              client.execute(request, token);
            } catch (ApiException e) {
              e.printStackTrace();
            }
          }else{
             userDelete(userids);
          }
          i++;
        }
        DingDingTools.httpGet(url,"UTF-8");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<WxCpDepart> departGet(){
    String token=getAccessToken();
    String url="https://oapi.dingtalk.com/department/list?access_token="+token;
    String result;
    List<WxCpDepart> wxlist=new ArrayList<>();
    try {
      result = DingDingTools.httpGet(url, "UTF-8");
      HashMap<String, String> map= DingDingTools.jsonStr2Map(result);
      if(map.get("errcode").equals("0")){//成功
        String department=map.get("department");
        JSONArray json = JSONArray.fromObject(department);//userStr是json字符串
        int j=0;
        if(json.size()>0){
          for(int i=0;i<json.size();i++){
            WxCpDepart wx=new WxCpDepart();
            JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            String name=(String) job.get("name");
            Integer id=(Integer) job.get("id");
            if(job.get("parentid")!=null && !job.get("parentid").equals("")) {
              Integer parentid=(Integer) job.get("parentid");
              wx.setParentId(parentid);
            }else {
              wx.setParentId(0);
            }
            wx.setId(id);
            wx.setName(name);
            wx.setOrder(j);
            wxlist.add(wx);
          }
        }
        return wxlist;
      }else{
        return wxlist;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return wxlist;
    }
  }

  @Override
  public String userCreate(WxCpUser user){
    String token=getAccessToken();
    String url="https://oapi.dingtalk.com/user/create?access_token="+token;
    String userid="";
    try {
      String jsonstr=JsonAssist.getInstance().buildModelJson(user);
      jsonstr=jsonstr.replace("departIds","department");
      String result = DingDingTools.httpPost(url,jsonstr );
      Map<String,String> map= DingDingTools.jsonStr2Map(result);
      if(map.get("errcode").equals("60104")){//如果存在添加部门
        OapiUserUpdateRequest request = new OapiUserUpdateRequest();
        String wxId=map.get("userid");
        if(!wxId.equals("")){
          OapiUserGetResponse response=getUser(wxId,token);
          List<Long> deptlist=response.getDepartment();
          request.setUserid(wxId);
          request.setName(user.getName());
          request.setEmail(user.getEmail());
          request.setMobile(user.getMobile());
          request.setPosition(user.getPosition());
          request.setTel(user.getTel());
          Integer[] inrDepts=user.getDepartIds();
          if(inrDepts.length>0){
            deptlist.add(Long.valueOf(inrDepts[0]));
            request.setDepartment(deptlist);
            updateUser(request,token);
          }
        }
      }
      userid=map.get("userid");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return userid;
  }

  public String updateUser(OapiUserUpdateRequest request,String token){
    DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/update");
    String resultBody="";
    try {
      OapiUserUpdateResponse response = client.execute(request, token);
      resultBody=response.getBody();
    } catch (ApiException e) {
      e.printStackTrace();
    }
    return resultBody;
  }

  public OapiUserGetResponse getUser(String userid,String accessToken){
    DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
    OapiUserGetRequest request = new OapiUserGetRequest();
    request.setUserid(userid);
    request.setHttpMethod("GET");
    OapiUserGetResponse response=null;
    try {
      response = client.execute(request, accessToken);
    } catch (ApiException e) {
      e.printStackTrace();
    }
    return response;
  }

  @Override
  public String userUpdate(WxCpUser user) {
    String token=getAccessToken();
    String url = "https://oapi.dingtalk.com/user/update?access_token="+token;
    String userid="";
    try {
      String result = DingDingTools.httpPost(url, JsonAssist.getInstance().buildModelJson(user).replace("userId","userid").replace("departIds","department"));
      Map<String,String> map= DingDingTools.jsonStr2Map(result);
      if(map.get("errcode").equals("60121")){
        userid=userCreate(user);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return userid;
  }

  @Override
  public void userDelete(String[] userids){
    String token=getAccessToken();
    for(String str:userids) {
      String url="https://oapi.dingtalk.com/user/delete?access_token="+token+"&userid="+str;
      try {
        DingDingTools.httpGet(url, "UTF-8");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public WxCpUser userGet(String DDId) {
    String token=getAccessToken();
    WxCpUser user = new WxCpUser();
    DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
    OapiUserGetRequest request = new OapiUserGetRequest();
    request.setUserid(DDId);
    request.setHttpMethod("GET");
    try {
      OapiUserGetResponse response = client.execute(request, token);
      if(response.getErrcode()==60121){
        user=null;
      }else{
        String userid=response.getUserid();
        String mobile=response.getMobile();
        String tel=response.getTel();
//      String workPlace=response.getWorkPlace();
//      String remark=response.getRemark();
//      Boolean isAdmin=response.getIsAdmin();//是否是企业的管理员，true表示是，false表示不是
//      Boolean isBoss=response.getIsBoss();//是否为企业的老板，true表示是，false表示不是
//			Boolean isLeader=response.getisl;//是否是部门的主管，true表示是，false表示不是
        String name=response.getName();
        Boolean active=response.getActive();
        List<Long> department=response.getDepartment();//成员所属部门id列表
        String position=response.getPosition();//职位信息
        String email=response.getEmail();//员工的邮箱
//      String orgEmail=response.getOrgEmail();//员工的企业邮箱，如果员工的企业邮箱没有开通，返回信息中不包含
        String avatar=response.getAvatar();//头像url
//      String jobnumber=response.getJobnumber();//员工工号
//      Date hiredDate=response.getHiredDate();//入职时间
//			String stateCode=(String)job.get("stateCode");//国家地区码
        user.setUserId(userid);
        if(active){
          user.setStatus(1);
        }else {
          user.setStatus(0);
        }
        user.setPosition(position);
        user.setEmail(email);
        user.setTel(tel);
        user.setName(name);
        user.setAvatar(avatar);
        user.setMobile(mobile);
        Integer[] departIds = new Integer[department.size()];
        int k=0;
        for(Long deptl:department){
          departIds[k]=deptl.intValue();
          k++;
        }
        user.setDepartIds(departIds);
        user.setWeiXinId(userid);
      }
    } catch (ApiException e) {
      e.printStackTrace();
    }
    return user;
  }

  @Override
  public List<WxCpUser> userList(String departId){
    String token=getAccessToken();
    List<WxCpUser> userlist=new ArrayList<>();
    int offset=0;
    int size=100;
    String getUserCounturl="https://oapi.dingtalk.com/user/getDeptMember?access_token="+token+"&deptId="+departId;//用户列表
    try {
      String result = DingDingTools.httpGet(getUserCounturl, "UTF-8");
      HashMap<String, String> map= DingDingTools.jsonStr2Map(result);
      String[] userSize= {};
      if(map.get("userIds")!=null) {
        userSize=map.get("userIds").split(",");
      }
      int p=1;//循环次数
      if(userSize.length>0){
        p=userSize.length/100+1;
        for(int q=0;q<p;q++){
          offset=q;
            String getUserListurl="https://oap" +
                    "i.dingtalk.com/user/listbypage?access_token="+token+"&department_id="+departId+//用户详情列表
                    "&offset="+offset+"&size="+size;
            String resultUser = DingDingTools.httpGet(getUserListurl, "UTF-8");
            HashMap<String, String> usermap= DingDingTools.jsonStr2Map(resultUser);
            if(usermap.get("errcode").equals("0")) {
              String userList=usermap.get("userlist");
              JSONArray json = JSONArray.fromObject(userList);//userStr是json字符串
//              int j=0;
              if(json.size()>0){
                for(int i=0;i<json.size();i++){
                  WxCpUser user=new WxCpUser();
                  JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                  String userid=(String) job.get("userid");
//                  Long order=(Long) job.get("order");
                  String mobile=(String) job.get("mobile");//手机号
                  String tel=(String) job.get("tel");//分机号
//                  String workPlace=(String)job.get("workPlace");//办公地点
//                  String remark=(String)job.get("remark");//备注
//                  Boolean isAdmin=(Boolean)job.get("isAdmin");//是否是企业的管理员，true表示是，false表示不是
//                  Boolean isBoss=(Boolean)job.get("isBoss");//是否为企业的老板，true表示是，false表示不是
//                  Boolean isLeader=(Boolean)job.get("isLeader");//是否是部门的主管，true表示是，false表示不是
                  String name=(String)job.get("name");//成员名称
                  Boolean active=(Boolean)job.get("active");//表示该用户是否激活了钉钉
                  JSONArray department=(JSONArray)job.get("department");//成员所属部门id列表
                  String position=(String)job.get("position");//职位信息
                  String email=(String)job.get("email");//员工的邮箱
//                  String orgEmail=(String)job.get("orgEmail");//员工的企业邮箱，如果员工的企业邮箱没有开通，返回信息中不包含
                  String avatar=(String)job.get("avatar");//头像url
//                  String jobnumber=(String)job.get("jobnumber");//员工工号
//                  String hiredDate=(String)job.get("hiredDate");//入职时间
//                  String stateCode=(String)job.get("stateCode");//国家地区码
                  user.setUserId(userid);
                  if(active){
                    user.setStatus(1);
                  }else {
                    user.setStatus(0);
                  }
                  user.setPosition(position);
                  user.setEmail(email);
                  user.setTel(tel);
                  user.setName(name);
                  user.setAvatar(avatar);
                  user.setMobile(mobile);
                  Integer[] departIds = new Integer[department.size()];
                  for(int k=0;k<department.size();k++) {
                    departIds[k]=department.getInt(k);
                  }
                  user.setDepartIds(departIds);
                  user.setWeiXinId(userid);
                  userlist.add(user);
                }
              }
            }else {
              return userlist;
            }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return userlist;
  }

  @Override
  public List<WxCpUser> departGetUsers(Integer departId, Boolean fetchChild, Integer status) {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?department_id=" + departId;
    String params = "";
    if (fetchChild != null) {
      params += "&fetch_child=" + (fetchChild ? "1" : "0");
    }
    if (status != null) {
      params += "&status=" + status;
    } else {
      params += "&status=0";
    }

    String responseContent = get(url, params);
    JsonElement tmpJsonElement = new JsonParser().parse(responseContent);
    return WxCpGsonBuilder.INSTANCE.create()
      .fromJson(
        tmpJsonElement.getAsJsonObject().get("userlist"),
        new TypeToken<List<WxCpUser>>() {
        }.getType()
      );
  }

  @Override
  public String get(String url, String queryParam) {
    return null;
  }

  @Override
  public String post(String url, String postData)  {
    return null;
  }

  @Override
  public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws DdErrorException {
    return null;
  }

  /**
   * 向钉钉端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求
   */
  @Override
  public String execute(OapiMessageCorpconversationAsyncsendV2Request request)throws DdErrorException{
    int retryTimes = 0;
    do {
      try {
        String result = this.executeInternal(request);
        this.log.debug(result);
        return result;
      } catch (DdErrorException e) {
        if (retryTimes + 1 > this.maxRetryTimes) {
          this.log.warn("重试达到最大次数【{}】", this.maxRetryTimes);
          //最后一次重试失败后，直接抛出异常，不再等待
          throw new RuntimeException("钉钉服务端异常，超出重试次数");
        }
        DdError error = e.getError();
        /*
         * -1 系统繁忙, 1000ms后重试
         */
        if (error.getErrorCode() == -1) {
          int sleepMillis = this.retrySleepMillis * (1 << retryTimes);
          try {
            this.log.debug("钉钉系统繁忙，{} ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
            Thread.sleep(sleepMillis);
          } catch (InterruptedException e1) {
            throw new RuntimeException(e1);
          }
        } else {
          throw e;
        }
      }
    } while (retryTimes++ < this.maxRetryTimes);
    this.log.warn("重试达到最大次数【{}】", this.maxRetryTimes);
    throw new RuntimeException("钉钉服务端异常，超出重试次数");
  }

  @Override
  public String msgPost(OapiMessageCorpconversationAsyncsendV2Request request) {
    return null;
  }

  protected synchronized String executeInternal(OapiMessageCorpconversationAsyncsendV2Request request) throws DdErrorException {
    String accessToken = getAccessToken(false);
    DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
    try {
      OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request,accessToken);
      if(response.getErrcode()==42001 || response.getErrcode()==40001 || response.getErrcode()==40014){
        this.configStorage.expireAccessToken();
        return execute(request);
      }else{
        this.log.debug(response.getBody());
        return response.getBody();
      }
    }catch (ApiException e) {
      throw new RuntimeException(e);
    }
  }

  protected CloseableHttpClient getHttpclient() {
    return this.httpClient;
  }

  @Override
  public void setWxCpConfigStorage(DdCpConfigStorage ddConfigProvider) {
    this.configStorage = ddConfigProvider;
    ApacheHttpClientBuilder apacheHttpClientBuilder = this.configStorage
      .getApacheHttpClientBuilder();
    if (null == apacheHttpClientBuilder) {
      apacheHttpClientBuilder = DefaultApacheHttpClientBuilder.get();
    }

    apacheHttpClientBuilder.httpProxyHost(this.configStorage.getHttpProxyHost())
      .httpProxyPort(this.configStorage.getHttpProxyPort())
      .httpProxyUsername(this.configStorage.getHttpProxyUsername())
      .httpProxyPassword(this.configStorage.getHttpProxyPassword());

    if (this.configStorage.getHttpProxyHost() != null && this.configStorage.getHttpProxyPort() > 0) {
      this.httpProxy = new HttpHost(this.configStorage.getHttpProxyHost(), this.configStorage.getHttpProxyPort());
    }

    this.httpClient = apacheHttpClientBuilder.build();
  }

  @Override
  public void setRetrySleepMillis(int retrySleepMillis) {
    this.retrySleepMillis = retrySleepMillis;
  }


  @Override
  public void setMaxRetryTimes(int maxRetryTimes) {
    this.maxRetryTimes = maxRetryTimes;
  }

  @Override
  public WxSession getSession(String id) {
    if (this.sessionManager == null) {
      return null;
    }
    return this.sessionManager.getSession(id);
  }

  @Override
  public WxSession getSession(String id, boolean create) {
    if (this.sessionManager == null) {
      return null;
    }
    return this.sessionManager.getSession(id, create);
  }

  @Override
  public void setSessionManager(WxSessionManager sessionManager) {
    this.sessionManager = sessionManager;
  }

}
