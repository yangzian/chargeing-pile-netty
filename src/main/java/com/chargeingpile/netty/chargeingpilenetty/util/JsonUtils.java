package com.chargepile.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

	
/**
 * json和java bean 转换工具类
 *
 */
public class JsonUtils {
	private static ObjectMapper mapper = new ObjectMapper();  
	
	private static JsonUtils jUtils = new JsonUtils();
	
	
	
	private JsonUtils() {}
	
	public static JsonUtils getInstance(){
		return jUtils;
	}
	/**
	 * 将json串转成javaBean
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public <T> T jsonFromBean(String jsonStr,Class<T> clazz) {
		try {
			return mapper.readValue(jsonStr, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将bean 转成 json串
	 * @param bean
	 * @return
	 */
	public <T> String beanToJson(T bean){
		StringWriter strWrite = null ;    
		JsonGenerator jsonGen = null;
		try {
			strWrite = new StringWriter();    
			jsonGen = new JsonFactory().createJsonGenerator(strWrite);    
			mapper.writeValue(jsonGen, bean);    
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			
			try {
				jsonGen.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}   

		return strWrite.toString();
	}
	
	/**
	 * list 转成 json
	 * @param list
	 * @param clazz
	 * @return
	 */
	public <T> String jsonFromList(List<T> list,Class<T> clazz){
		StringWriter strWrite = null ;    
		JsonGenerator jsonGen = null;
		try {
			strWrite = new StringWriter();    
			jsonGen = new JsonFactory().createJsonGenerator(strWrite);    
			mapper.writeValue(jsonGen, list);    
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			
			try {
				jsonGen.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}      
		return strWrite.toString();
	}
	
	/**
	 * json to List
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> jsonToList(String json,Class<T> clazz){
		try {
			JavaType javaType = getCollectionType(ArrayList.class, clazz); 
			List<T> list  =(List<T>)mapper.readValue(json, javaType);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 public  JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {   
		 return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
	}

}
