package deng.simplespringapp;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class MyCollectionBean {
	private List<String> myList;
	private Map<String, String> myMap;
}
