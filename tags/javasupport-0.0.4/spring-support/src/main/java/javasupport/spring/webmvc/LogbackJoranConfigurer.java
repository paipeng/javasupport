package javasupport.spring.webmvc;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;

public class LogbackJoranConfigurer implements InitializingBean, DisposableBean, Ordered {

    private Resource configFilename;

    public void setConfigFilename(Resource configFilename) {
        this.configFilename = configFilename;
    }

    public void afterPropertiesSet() {
        try {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            lc.shutdownAndReset();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            configurator.doConfigure(configFilename.getInputStream());
            StatusPrinter.print(configurator.getStatusManager());
        } catch (Exception e) {
            throw new BeanInitializationException("Failed to load logger config file.", e);
        }
    }

    public int getOrder() {
        return 0;
    }

    public void destroy() throws Exception {
    }
}
