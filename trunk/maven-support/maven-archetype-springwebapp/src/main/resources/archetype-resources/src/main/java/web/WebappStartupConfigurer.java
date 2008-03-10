package ${groupId}.web;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.context.ServletContextAware;

public class WebappStartupConfigurer implements ServletContextAware {

    protected Log logger = LogFactory.getLog(getClass());
    protected ServletContext servletContext;

    public void setServletContext(ServletContext ctx) {
        servletContext = ctx;
        loadAppInfo(ctx);
        loadServerInfo(ctx);
    }

    private void loadAppInfo(ServletContext ctx) {
        //version properties
        Properties appInfo = new Properties();
        try {
            URL res = ctx.getResource("/META-INF/MANIFEST.MF");
            if (res != null) {
                appInfo.load(res.openStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("Unble to read /META-INF/MANIFEST.MF for packageInfo");
        }

        SimpleDateFormat buildDateDf = new SimpleDateFormat("yyyy/MM/dd hh:mmaa");
        SimpleDateFormat buildNumberDf = new SimpleDateFormat("yyyyMMdd.HHmmss");
        Date now = new Date();

        appInfo.setProperty("buildVersion", appInfo.getProperty("buildVersion", "localhost-SNAPSHOT"));
        appInfo.setProperty("buildNumber", appInfo.getProperty("buildNumber", buildNumberDf.format(now)));
        appInfo.setProperty("buildTimestamp", appInfo.getProperty("buildTimestamp", "" + now.getTime()));

        Date buildDt = new Date(Long.parseLong(appInfo.getProperty("buildTimestamp")));
        appInfo.setProperty("fullBuildName",
                appInfo.getProperty("buildVersion") + " build#" +
                appInfo.getProperty("buildNumber") + ", date: " +
                buildDateDf.format(buildDt));

        ctx.setAttribute("appInfo", appInfo);
        logger.info("appInfo " + appInfo);

        //app startTime
        ctx.setAttribute("appStartTime", new Date());
    }

    private void loadServerInfo(ServletContext ctx) {
        String serverInfo = ctx.getServerInfo() + " With " +
                System.getProperty("java.vm.name") + " " +
                System.getProperty("java.vm.version") + " On " +
                System.getProperty("os.name") + " " +
                System.getProperty("os.arch") + " " +
                System.getProperty("os.version");
        ctx.setAttribute("serverInfo", serverInfo);
    }
}
