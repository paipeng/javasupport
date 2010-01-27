package deng.myrunbean;

import java.io.*;
import java.util.*;
import org.slf4j.*;

/**
 * Simple HelloWorld RunnableBean(Service)
 */
public class HelloWorld implements RunnableBean {
	protected Logger logger = LoggerFactory.getLogger(getClass());
    protected String message;
    
    public void setMessage(String msg){
        message = msg;
    }

	public void run(){
		logger.info(message);
	}
	
	public void init() throws Exception{
		logger.info("Initialized.");
	}

	public void destroy() throws Exception{
		logger.info("Destroyed.");
	}
}