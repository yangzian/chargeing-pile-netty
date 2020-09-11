/**  
 * @Title: EhcacheUtil.java
 * @Package com.towerswatson.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author dingkunjie  
 * @date 2016年2月17日 下午5:11:18
 * @version V1.0  
 */
package com.chargeingpile.netty.chargeingpilenetty.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;

/**
 * @Title: EhcacheUtil.java
 * @Package com.towerswatson.utils
 * @Description: ehcache缓存
 * @author dingkunjie
 * @date 2016年2月17日 下午5:11:18
 * @version V1.0
 */
@Component
public class EhcacheUtil {
	//private static final CacheManager cacheManager = new CacheManager();
	private static final String path = "/ehcache.xml";

	private URL url;

	private CacheManager manager;

	private static EhcacheUtil ehCache;

	private EhcacheUtil(String path) {
		url = getClass().getResource(path);
		manager = CacheManager.create(url);
	}
	private EhcacheUtil() {
	}

	public static EhcacheUtil getInstance() {
		if (ehCache == null) {
			ehCache = new EhcacheUtil(path);
		}
		return ehCache;
	}

	public void put(String key, Object value) {
		Cache cache = manager.getCache("userCache");
		Element element = new Element(key, value);
		cache.put(element);
	}


	public Object get(String key) {
		Cache cache = manager.getCache("userCache");
		Element element = cache.get(key);
		return element == null ? null : element.getObjectValue();
	}


	public Cache get() {
		return manager.getCache("userCache");
	}

	public void remove(String key) {
		Cache cache = manager.getCache("userCache");
		cache.remove(key);
	}






	public void putList(String key, Object value) {
		Cache cache = manager.getCache("listGo");
		Element element = new Element(key, value);
		cache.put(element);
	}

	public Object getList(String key) {
		Cache cache = manager.getCache("listGo");
		Element element = cache.get(key);
		return element == null ? null : element.getObjectValue();
	}



}
