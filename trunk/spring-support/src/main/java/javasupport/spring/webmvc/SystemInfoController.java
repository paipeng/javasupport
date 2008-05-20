package javasupport.spring.webmvc;

import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class SystemInfoController extends AbstractController {
    
    private int MB = 1024 * 1024;

    private Properties applicationProperties;

    public void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @SuppressWarnings("unchecked")
    public ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView("systeminfo");
        mav.addObject("sysprops", new TreeMap<String, String>((Map)System.getProperties()));
        mav.addObject("sysenv", new TreeMap<String, String>((Map)System.getenv()));
        mav.addObject("systime", new Date());

        //Calculate app uptime duration
        Date appStartTime = (Date) getServletContext().getAttribute("appStartTime");
        long durationMillis = new Date().getTime() - appStartTime.getTime();
        String duration = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        mav.addObject("appUptime", duration);

        //Calculate system resources.
        Runtime runtime = Runtime.getRuntime();
        long totalMem = runtime.totalMemory() / MB;
        long freeMem = runtime.freeMemory() / MB;
        long avaCPU = runtime.availableProcessors();
        mav.addObject("sysres", "CPU: " + avaCPU + ", MemoryUsed: " + (totalMem - freeMem) + "M/" + totalMem + "M");
        
        if(applicationProperties== null){
            applicationProperties = new Properties();
        }
        mav.addObject("applicationProps", applicationProperties);
        
        return mav;
    }
}
