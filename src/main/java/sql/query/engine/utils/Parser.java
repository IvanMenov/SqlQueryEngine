package sql.query.engine.utils;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Parser {
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static <T> T parseToObject(String json, Class<T> objClass) {
		T obj = null;
		try {
			obj = mapper.readValue(json, objClass);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return obj;
		
	}
	
	public static <T> T parseToObject(File file, Class<T> objClass) {
		T obj = null;
		try {
			obj = mapper.readValue(file, objClass);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return obj;
		
	}
}
