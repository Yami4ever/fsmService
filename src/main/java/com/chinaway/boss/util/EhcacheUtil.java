package com.chinaway.boss.util;

import java.io.InputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 缓存工具类
 * 
 * @author Yaming Zhuge
 * @date 2016年5月11日 上午11:45:36
 */
public class EhcacheUtil {

	private static CacheManager cacheManager = null;
	private static Cache cache = null;

	static {
		InputStream stream = EhcacheUtil.class.getClassLoader().getResourceAsStream("ehcache.xml");
		cacheManager = CacheManager.create(stream);
		cache = cacheManager.getCache("fsm-cache");
	}

	public static void put(Object key, Object value) {
		Element element = new Element(key, value);
		cache.put(element);
	}

	public static Object get(Object key) {
		Element element = cache.get(key);
		if (null == element) {
			return null;
		}
		return element.getObjectValue();
	}

	public static void remove(String key) {
		cache.remove(key);
	}

}
