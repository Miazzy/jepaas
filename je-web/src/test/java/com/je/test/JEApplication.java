package com.je.test;

import com.je.core.util.StringUtil;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Scanner;
import org.eclipse.jetty.webapp.WebAppContext;
import javax.management.MBeanServer;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class JEApplication {

    private int port;
    private String context;
    private String webappPath;
    private int scanIntervalSeconds;
    private boolean jmxEnabled;
    private Server server;
    private WebAppContext webapp;


    public static void main(String[] args) {
        int argPort = 8080;
        if (args.length > 0 && StringUtil.isNotEmpty(args[0])) {
            argPort = Integer.parseInt(args[0]);
        }

        new JEApplication("src/main/webapp", argPort, "/").start();
    }

    public JEApplication(String webappPath, int port, String context) {
        this(webappPath, port, context, 0, false);
    }

    public JEApplication(String webappPath, int port, String context, int scanIntervalSeconds, boolean jmxEnabled) {
        this.webappPath = webappPath;
        this.port = port;
        this.context = context;
        this.scanIntervalSeconds = scanIntervalSeconds;
        this.jmxEnabled = jmxEnabled;
        validateConfig();
    }

    private void validateConfig() {
        if (port < 0 || port > 65536) {
            throw new IllegalArgumentException("Invalid port of web server: " + port);
        }
        if (context == null) {
            throw new IllegalStateException("Invalid 2context of web server: " + context);
        }
        if (webappPath == null) {
            throw new IllegalStateException("Invalid context of web server: " + webappPath);
        }
    }

    public void start() {
        if (server == null || server.isStopped()) {
            try {
                doStart();
            } catch (Throwable e) {
                e.printStackTrace();
                System.err.println("System.exit() ......");
                System.exit(1);
            }
        } else {
            throw new RuntimeException("Jetty Server already started.");
        }
    }

    private void doStart() throws Throwable {
        if (!portAvailable(port)) {
            throw new IllegalStateException("port: " + port + " already in use!");
        }

        System.setProperty("org.eclipse.jetty.util.URI.charset", "UTF-8");
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.Slf4jLog");
        System.setProperty("org.eclipse.jetty.server.Request.maxFormContentSize", "20000000");

        server = new Server(port);
        server.setHandler(getHandler());

        if (jmxEnabled) {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
            server.addBean(mBeanContainer);
        }

        if (scanIntervalSeconds > 0) {
            startFileWatchScanner();
        }

        long ts = System.currentTimeMillis();
        server.start();

        ts = System.currentTimeMillis() - ts;
        System.err.println("Jetty Server started: " + String.format("%.2f sec", ts / 1000d));

        server.join();
    }


    protected Handler getHandler() {
        webapp = new WebAppContext(webappPath, context);
        webapp.setDescriptor(webappPath + "/WEB-INF/web.xml");
        webapp.setDefaultsDescriptor("./webdefault.xml");
        webapp.setClassLoader(Thread.currentThread().getContextClassLoader());
        webapp.setConfigurationDiscovered(true);
        webapp.setParentLoaderPriority(true);
        return webapp;
    }

    private void startFileWatchScanner() throws Exception {
        List<File> scanList = new ArrayList<File>();
        scanList.add(new File(webappPath, "WEB-INF"));

        Scanner scanner = new Scanner();
        scanner.setReportExistingFilesOnStartup(false);
        scanner.setScanInterval(scanIntervalSeconds);
        scanner.setScanDirs(scanList);
        scanner.addListener(new Scanner.BulkListener() {

            @SuppressWarnings("rawtypes")
            public void filesChanged(List changes) {
                try {
                    System.err.println("Loading changes ......");
                    webapp.stop();
                    webapp.start();
                    System.err.println("Loading complete.\n");
                } catch (Exception e) {
                    System.err.println("Error reconfiguring/restarting webapp after change in watched files");
                    e.printStackTrace();
                }
            }
        });
        System.err.println("Starting scanner at interval of " + scanIntervalSeconds + " seconds.");
        scanner.start();
    }

    private static boolean portAvailable(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }
            if (ss != null) try {
                ss.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

}
