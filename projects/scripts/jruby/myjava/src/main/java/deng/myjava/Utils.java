package deng.myjava;

import java.util.*;

public class Utils {
  public static Runnable runTaskService(Runnable taskService) {
  	System.out.println("Runnable taskService: " + taskService);
  	taskService.run();
  	return taskService;
  }
  public static Service runTaskService(Service taskService) {
  	System.out.println("Service taskService: " + taskService);
  	taskService.init();
  	taskService.run();
  	taskService.destroy();
  	return taskService;
  }
  
  public static TaskService runTaskService(TaskService taskService) {
  	System.out.println("TaskService taskService: " + taskService.getName());
  	taskService.init();
  	taskService.run();
  	taskService.destroy();
  	return taskService;
  }
  
  public static Service runService(Service service) {
  	System.out.println("Running service: " + service);
  	service.run();
  	return service;
  }
  public static Runnable runTask(Runnable task) {
  	System.out.println("Running task: " + task);
  	task.run();
  	return task;
  }
  public static Object printObject(Object object) {
  	System.out.println("Object: " + object);
  	System.out.println("IdentityHashCode: " + System.identityHashCode(object));
  	System.out.println("Class Ancestors: ");
  	Class cls = object.getClass();
  	int indent = 2;
  	while (cls != null) {
  		for (int i = 0; i < indent; i++)
  			System.out.print(" ");
  		System.out.println(cls);
  		indent += 2;
  		cls = cls.getSuperclass();
  	}
  	
  	Class<?>[] intfs = object.getClass().getInterfaces();
  	System.out.println("Implemented Interfaces: ");
  	for (Class intf : intfs) {
  		System.out.println("  " + intf);
  	}
  	return object;
  }
  public static List<?> takeList(List<?> list) {
  	int i = 0;
  	for (Object object : list) {
  		System.out.println("list[" + (i++) + "] " + object);
  	}
  	return list;
  }
  public static Map<String, Object> takeMap(Map<String, Object> map) {
  	for (Map.Entry<String, Object> e : map.entrySet()) {
  		System.out.println("map[" + (e.getKey()) + "] " + e.getValue());
  	}
  	return map;
  }
  public static Object[] takeArray(Object[] array) {
  	int i = 0;
  	for (Object object : array) {
  		System.out.println("array[" + (i++) + "] " + object);
  	}
  	return array;
  }
  public static String[] splitString(String text, String sep) {
  	return text.split(sep);
  }
  public static void sortNumbers(List<Integer> list) {
  	Collections.sort(list);
  }
}
