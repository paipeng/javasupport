package demo.dengz1.myejbapp.ejb.simple;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

@MessageDriven(
	messageListenerInterface = MessageListener.class,
	activationConfig = { 
		@ActivationConfigProperty(					
			propertyName = "destination", propertyValue = "queue/my-ejb-app_Q1"),
		@ActivationConfigProperty(					
			propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(					
			propertyName = "messageSelector", propertyValue = "") 
	})
public class MyMDB implements MessageListener {
	static Logger logger = Logger.getLogger(MyMDB.class);
	
	@EJB(name="mySLSB", beanName="MySLSBImpl")
	MySLSB mySLSB;
	
    public void onMessage(Message message) {
    	logger.info("Received msg: " + message);
    	logger.debug("Calling mySLSB " + mySLSB);
    	
    	mySLSB.doSessionWork();
    }
}
