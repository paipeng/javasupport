package demo.dengz1.myjbpm;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;

public class MyJBPMDemo {
	private static Logger logger = Logger.getLogger(MyJBPMDemo.class);
	
	public static void main(String[] args) {
		runMyProcess1c();
	}
	
	private static void runMyProcess1c() {
		JbpmContext jbpmContext = null;
		
		try {			
			logger.debug("Loading myprocess1 definition");
			InputStream inputStream = ClassLoader.getSystemResourceAsStream("demo/dengz1/myjbpm/myprocess1/processdefinition.xml");
			ProcessDefinition processDefinition = ProcessDefinition.parseXmlInputStream(inputStream);
			logger.info("Found processDefinition = " + processDefinition);
			
			logger.debug("Creating processInstance");
	        ProcessInstance processInstance = processDefinition.createProcessInstance();
			logger.info("ProcessInstance id: " + processInstance.getId() + " is ready.");
			
			Token token = processInstance.getRootToken();
			logger.debug("Start of process key: " + processInstance.getKey());
			logger.info("Current process token node: " + token.getNode());
			
			while(!token.hasEnded()) {
				logger.debug("Signal transition on processInstance.");
				token.signal();
				logger.info("Current process token node: " + token.getNode());
			}
			logger.debug("End of process key: " + processInstance.getKey());
		} finally {
			if (jbpmContext != null) {
				jbpmContext.close();
			}
		}
	}
	
	private static void runMyProcess1b() {
		JbpmContext jbpmContext = null;
		
		try {			
			logger.debug("Loading myprocess1 definition");
			InputStream inputStream = ClassLoader.getSystemResourceAsStream("demo/dengz1/myjbpm/myprocess1/processdefinition.xml");
			ProcessDefinition processDefinition = ProcessDefinition.parseXmlInputStream(inputStream);
			logger.info("Found processDefinition = " + processDefinition);
			
			logger.debug("Creating processInstance");
	        ProcessInstance processInstance = processDefinition.createProcessInstance();
			logger.info("ProcessInstance " + processInstance.getId() + " is ready.");
			
			Token token = processInstance.getRootToken();
			logger.debug("Start of process token signal.");
			logger.info("Current process token node: " + token.getNode());
	        
	        logger.debug("Signal transition on processInstance.");
	        token.signal();	        
			logger.info("Current process token node: " + token.getNode());
			
	        logger.debug("Signal transition on processInstance.");
	        token.signal();	        
			logger.info("Current process token node: " + token.getNode());
		} finally {
			if (jbpmContext != null) {
				jbpmContext.close();
			}
		}
	}
	
	private static void runMyProcess1a() {
		JbpmContext jbpmContext = null;
		
		try {
			logger.debug("Loading up jbpmConfiguration instance");		
			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
			jbpmContext = jbpmConfiguration.createJbpmContext();
			
			logger.debug("Find myprocess1 definition");
			GraphSession graphSession = jbpmContext.getGraphSession();
			ProcessDefinition processDefinition = graphSession.findLatestProcessDefinition("myprocess1");
			logger.info("Found processDefinition = " + processDefinition);
			
			String processId = "MyJBPMDemo" + System.currentTimeMillis();
			logger.debug("Setup processInstance with ID=" + processId);
	        ProcessInstance processInstance = processDefinition.createProcessInstance();
	        processInstance.setKey(processId);	        
	        //jbpmContext.addAutoSaveProcessInstance(processInstance); //???
			logger.info("ProcessInstance " + processId + " is ready.");

			logger.debug("Setup contextInstance.");
	        ContextInstance processContext = processInstance.getContextInstance();
			logger.debug("Adding myprocess1.var1");
	        processContext.setVariable("myprocess1.var1", "Foo");
	        logger.info("contextInstance is ready.");
	        
	        logger.info("Signal transition on processInstance.");
	        processInstance.signal();
		} finally {
			if (jbpmContext != null) {
				jbpmContext.close();
			}
		}
	}
}
