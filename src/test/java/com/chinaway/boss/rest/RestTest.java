package com.chinaway.boss.rest;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.ws.rs.client.WebTarget;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.Test;

public class RestTest {
	
	private HttpServer server;
	private WebTarget target;
	
	private static final URI BASE_URI = URI.create("http://localhost:9090/rest/");
	
//	@Test
//	public void test() throws IOException {
//		final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, createApp());
//		System.out.println(String.format("Application started.%nHit enter to stop it..."));
//        System.in.read();
//        server.stop();
//	}
	
//	public static ResourceConfig createApp() {
//        ResourceConfig rc = new ResourceConfig()
//        .packages("test")
//        .register(MyResource.class);
//        return rc;
//
//    }
	
	public static void main(String[] args) throws ParseException {
		// "Apr 6, 2016 9:37:41 AM"
		SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.US);
		java.util.Date date = format.parse("Apr 6, 2016 9:37:41 AM");
		System.out.println(date);
	}
	
	@Test
	public void testCommonConfiguration() throws ConfigurationException {
		Configuration config = new PropertiesConfiguration("fsm.properties");  
		String host = config.getString("uri_host");
		System.out.println(host);
	}

}
