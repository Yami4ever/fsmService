package com.chinaway.boss.util;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 读取配置文件的工具类
 * 
 * @author zhuge
 *
 */
public class PropUtil {

	private PropUtil() {
	}

	private static PropUtil propUtil;
	public synchronized static PropUtil getInstance() {
		if (propUtil == null) {
			propUtil = new PropUtil();
		}
		return propUtil;
	}

	public static CompositeConfiguration config = new CompositeConfiguration();
	static {
		try {
			config.addConfiguration(new PropertiesConfiguration("fsm.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		return config.getString(key);
	}
	
	public static int getIntProperty(String key) {
		return config.getInt(key);
	}
	
	public static long getLongProperty(String key) {
		return config.getLong(key);
	}

}
