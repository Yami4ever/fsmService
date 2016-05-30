package com.chinaway.boss.main;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.chinaway.boss.util.PropUtil;

/**
 * Rest服务启动入口
 * 
 * @author zhuge
 *
 */
public class RestfulMain {

	public static void main(String[] args) {
		try {
			URI baseUri = UriBuilder.fromUri("http://{host}/{appPath}")
					.resolveTemplate("host", PropUtil.getProperty("uri_host"))
					.resolveTemplate("appPath", PropUtil.getProperty("app_path"))
					.port(PropUtil.getIntProperty("uri_port")).build();
			ResourceConfig config = new ResourceConfig().packages(PropUtil.getProperty("scan_package"));
			HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
