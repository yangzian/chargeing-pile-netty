/**  
 * @Title: EhcacheUtil.java
 * @Package com.towerswatson.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author dingkunjie  
 * @date 2016年2月17日 下午5:11:18
 * @version V1.0  
 */
package com.chargepile.util;

import javax.annotation.Resource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.springframework.stereotype.Component;

/**
 * @Title: EhcacheUtil.java
 * @Package com.towerswatson.utils
 * @Description: ehcache缓存
 * @author dingkunjie
 * @date 2016年2月17日 下午5:11:18
 * @version V1.0
 */
@Component
public class TmpEhcacheUtil {
	//private static final CacheManager cacheManager = new CacheManager();
	
	private Cache cache;
	
    @Resource(name="tmpCache") 
    public void setEhCache(Cache tmpCache) { 
        this.cache = tmpCache; 
    }  
    
	/*
	 * 通过名称从缓存中获取数据
	 */
	@SuppressWarnings("deprecation")
	public Object getCacheElement(String cacheKey) throws Exception {
		Element e = cache.get(cacheKey);
		if (e == null) {
			return null;
		}
		return e.getValue();
	}

	/*
	 * 将对象添加到缓存中
	 */
	public void addToCache(String cacheKey, Object result) throws Exception {
		Element element = new Element(cacheKey, result);
		cache.put(element);
	}

}
