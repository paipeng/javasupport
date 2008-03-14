package ${groupId}.web;

import javasupport.spring.webmvc.WebappStartupConfigurer;

public class WebappStartup extends WebappStartupConfigurer{
    @Override
    protected void load() {        
        loadAppInfo(servletContext);
        loadServerInfo(servletContext);
    }
}
