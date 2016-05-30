package com.chinaway.boss.ehcache;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.chinaway.boss.model.FsmState;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

public class EhcacheTest {
	
	private CacheManager cacheManager;
	
	@Before
	public void setManager() {
		// 加载配置文件
		InputStream stream = EhcacheTest.class.getClassLoader().getResourceAsStream("ehcache.xml");
		cacheManager = CacheManager.create(stream);
	}
	
	

//	@Test
	public void testCacheManager() {

		// 加载配置文件
//		InputStream stream = EhcacheTest.class.getClassLoader().getResourceAsStream("ehcache.xml");
//		CacheManager cacheManager = CacheManager.create(stream);

		String[] names = cacheManager.getCacheNames();

		Assert.assertEquals(1, names.length);
		Assert.assertEquals(names[0], "data-cache");

		Cache cache = cacheManager.getCache("fsm-cache");
		Assert.assertNotNull(cache);

		CacheConfiguration configuration = cache.getCacheConfiguration();
		configuration.setTimeToIdleSeconds(3600);

		System.out.println(cacheManager.getActiveConfigurationText());

		// 清除所有缓存的数据，但是缓存本身仍然存在
		// cm.clearAll();

		// 从内存中删除一个缓存以及所有的数据，Cache被销毁
		// cm.removeCache("data-cache");

	}

//	@Test
	public void testAddElementToCache() {
		Cache cache = cacheManager.getCache("fsm-cache");
		
		FsmState state  = new FsmState();
		state.setCode("stateCode1");
		state.setName("stateName1");
		
		cache.putIfAbsent(new Element(state.getCode(), state));
		
		System.out.println(cache.get(state.getCode()));
	}

}
