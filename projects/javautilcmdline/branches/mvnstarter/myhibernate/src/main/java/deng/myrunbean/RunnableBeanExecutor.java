package deng.myrunbean;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

/**
 * A wrapper bean that will execute a list of runnable beans.
 * 
 * @author zemian
 *
 */
public class RunnableBeanExecutor implements RunnableBean {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected TaskExecutor taskExecutor;
	protected List<RunnableBean> runnableBeans;
	
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	public void setRunnableBeans(List<RunnableBean> runnableBeans) {
		this.runnableBeans = runnableBeans;
	}
	
	public void run() {
		for(RunnableBean bean : runnableBeans){
			logger.info("Executing runnableBean " + bean);
			taskExecutor.execute(bean);
		}
	}

	public void destroy() throws Exception {
		//call in reverse order.
		for(int i = runnableBeans.size()-1; i>=0; i--){
			RunnableBean bean = runnableBeans.get(i);
			logger.info("Destroying runnableBean " + bean);
			bean.destroy();
		}
	}

	public void init() throws Exception {
		for(RunnableBean bean : runnableBeans){
			logger.info("Initializing runnableBean " + bean);
			bean.init();
		}
	}
}
