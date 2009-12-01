package deng.simplespringapp;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBootstrap {
	public static void main(String[] args) {
		final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/spring/spring.xml");
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run() {
				ctx.stop();
			}
		});
		
		ctx.start();
		
		MyBean myBean = ctx.getBean("myBean", MyBean.class);
		System.out.println("myBean.name: " + myBean.getName());
		

		MyCollectionBean myCollectionBean = ctx.getBean("myCollectionBean", MyCollectionBean.class);
		System.out.println("myCollectionBean.myList: " + myCollectionBean.getMyList());
		System.out.println("myCollectionBean.myMap: " + myCollectionBean.getMyMap());
	}
}
