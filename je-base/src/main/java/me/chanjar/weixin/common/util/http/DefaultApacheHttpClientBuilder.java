package me.chanjar.weixin.common.util.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * httpclient 连接管理器
 *
 * @author kakotor
 */
@NotThreadSafe
public class DefaultApacheHttpClientBuilder implements ApacheHttpClientBuilder {
  protected final Logger log = LoggerFactory.getLogger(DefaultApacheHttpClientBuilder.class);
  private final AtomicBoolean prepared = new AtomicBoolean(false);
  private int connectionRequestTimeout = 3000;
  private int connectionTimeout = 5000;
  private int soTimeout = 5000;
  private int idleConnTimeout = 60000;
  private int checkWaitTime = 60000;
  private int maxConnPerHost = 10;
  private int maxTotalConn = 50;
  private String userAgent;
  private HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
      return false;
    }
  };
  private SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
  private PlainConnectionSocketFactory plainConnectionSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
  private String httpProxyHost;
  private int httpProxyPort;
  private String httpProxyUsername;
  private String httpProxyPassword;
  /**
   * 闲置连接监控线程
   */
  private IdleConnectionMonitorThread idleConnectionMonitorThread;
  private HttpClientBuilder httpClientBuilder;

  private DefaultApacheHttpClientBuilder() {
  }

  public static DefaultApacheHttpClientBuilder get() {
    return new DefaultApacheHttpClientBuilder();
  }

  @Override
  public ApacheHttpClientBuilder httpProxyHost(String httpProxyHost) {
    this.httpProxyHost = httpProxyHost;
    return this;
  }

  @Override
  public ApacheHttpClientBuilder httpProxyPort(int httpProxyPort) {
    this.httpProxyPort = httpProxyPort;
    return this;
  }

  @Override
  public ApacheHttpClientBuilder httpProxyUsername(String httpProxyUsername) {
    this.httpProxyUsername = httpProxyUsername;
    return this;
  }

  @Override
  public ApacheHttpClientBuilder httpProxyPassword(String httpProxyPassword) {
    this.httpProxyPassword = httpProxyPassword;
    return this;
  }

  @Override
  public ApacheHttpClientBuilder sslConnectionSocketFactory(SSLConnectionSocketFactory sslConnectionSocketFactory) {
    this.sslConnectionSocketFactory = sslConnectionSocketFactory;
    return this;
  }

  /**
   * 获取链接的超时时间设置,默认3000ms
   * <p>
   * 设置为零时不超时,一直等待.
   * 设置为负数是使用系统默认设置(非上述的3000ms的默认值,而是httpclient的默认设置).
   * </p>
   *
   * @param connectionRequestTimeout 获取链接的超时时间设置(单位毫秒),默认3000ms
   */
  public void setConnectionRequestTimeout(int connectionRequestTimeout) {
    this.connectionRequestTimeout = connectionRequestTimeout;
  }

  /**
   * 建立链接的超时时间,默认为5000ms.由于是在链接池获取链接,此设置应该并不起什么作用
   * <p>
   * 设置为零时不超时,一直等待.
   * 设置为负数是使用系统默认设置(非上述的5000ms的默认值,而是httpclient的默认设置).
   * </p>
   *
   * @param connectionTimeout 建立链接的超时时间设置(单位毫秒),默认5000ms
   */
  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  /**
   * 默认NIO的socket超时设置,默认5000ms.
   *
   * @param soTimeout 默认NIO的socket超时设置,默认5000ms.
   * @see java.net.SocketOptions#SO_TIMEOUT
   */
  public void setSoTimeout(int soTimeout) {
    this.soTimeout = soTimeout;
  }

  /**
   * 空闲链接的超时时间,默认60000ms.
   * <p>
   * 超时的链接将在下一次空闲链接检查是被销毁
   * </p>
   *
   * @param idleConnTimeout 空闲链接的超时时间,默认60000ms.
   */
  public void setIdleConnTimeout(int idleConnTimeout) {
    this.idleConnTimeout = idleConnTimeout;
  }

  /**
   * 检查空间链接的间隔周期,默认60000ms.
   *
   * @param checkWaitTime 检查空间链接的间隔周期,默认60000ms.
   */
  public void setCheckWaitTime(int checkWaitTime) {
    this.checkWaitTime = checkWaitTime;
  }

  /**
   * 每路的最大链接数,默认10
   *
   * @param maxConnPerHost 每路的最大链接数,默认10
   */
  public void setMaxConnPerHost(int maxConnPerHost) {
    this.maxConnPerHost = maxConnPerHost;
  }

  /**
   * 最大总连接数,默认50
   *
   * @param maxTotalConn 最大总连接数,默认50
   */
  public void setMaxTotalConn(int maxTotalConn) {
    this.maxTotalConn = maxTotalConn;
  }

  /**
   * 自定义httpclient的User Agent
   *
   * @param userAgent User Agent
   */
  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public IdleConnectionMonitorThread getIdleConnectionMonitorThread() {
    return this.idleConnectionMonitorThread;
  }

  private synchronized void prepare() {
    if(prepared.get()){
      return;
    }
    Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
      .register("http", this.plainConnectionSocketFactory)
      .register("https", this.sslConnectionSocketFactory)
      .build();

    @SuppressWarnings("resource")
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
    connectionManager.setMaxTotal(this.maxTotalConn);
    connectionManager.setDefaultMaxPerRoute(this.maxConnPerHost);
    connectionManager.setDefaultSocketConfig(
      SocketConfig.copy(SocketConfig.DEFAULT)
        .setSoTimeout(this.soTimeout)
        .build()
    );

    this.idleConnectionMonitorThread = new IdleConnectionMonitorThread(
      connectionManager, this.idleConnTimeout, this.checkWaitTime);
    this.idleConnectionMonitorThread.setDaemon(true);
    this.idleConnectionMonitorThread.start();

    this.httpClientBuilder = HttpClients.custom()
      .setConnectionManager(connectionManager)
      .setConnectionManagerShared(true)
      .setDefaultRequestConfig(
        RequestConfig.custom()
          .setSocketTimeout(this.soTimeout)
          .setConnectTimeout(this.connectionTimeout)
          .setConnectionRequestTimeout(this.connectionRequestTimeout)
          .build()
      )
      .setRetryHandler(this.httpRequestRetryHandler);

    if (StringUtils.isNotBlank(this.httpProxyHost)
      && StringUtils.isNotBlank(this.httpProxyUsername)) {
      // 使用代理服务器 需要用户认证的代理服务器
      CredentialsProvider provider = new BasicCredentialsProvider();
      provider.setCredentials(
        new AuthScope(this.httpProxyHost, this.httpProxyPort),
        new UsernamePasswordCredentials(this.httpProxyUsername,
          this.httpProxyPassword));
      this.httpClientBuilder.setDefaultCredentialsProvider(provider);
    }

    if (StringUtils.isNotBlank(this.userAgent)) {
      this.httpClientBuilder.setUserAgent(this.userAgent);
    }
    prepared.set(true);
  }

  @Override
  public CloseableHttpClient build() {
    if(!prepared.get()){
      prepare();
    }
    return this.httpClientBuilder.build();
  }

  public static class IdleConnectionMonitorThread extends Thread {
    private final HttpClientConnectionManager connMgr;
    private final int idleConnTimeout;
    private final int checkWaitTime;
    private volatile boolean shutdown;

    public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr, int idleConnTimeout, int checkWaitTime) {
      super("IdleConnectionMonitorThread");
      this.connMgr = connMgr;
      this.idleConnTimeout = idleConnTimeout;
      this.checkWaitTime = checkWaitTime;
    }

    @Override
    public void run() {
      try {
        while (!this.shutdown) {
          synchronized (this) {
            wait(this.checkWaitTime);
            this.connMgr.closeExpiredConnections();
            this.connMgr.closeIdleConnections(this.idleConnTimeout,
              TimeUnit.MILLISECONDS);
          }
        }
      } catch (InterruptedException ignore) {
      }
    }

    public void trigger() {
      synchronized (this) {
        notifyAll();
      }
    }

    public void shutdown() {
      this.shutdown = true;
      synchronized (this) {
        notifyAll();
      }
    }
  }
}
