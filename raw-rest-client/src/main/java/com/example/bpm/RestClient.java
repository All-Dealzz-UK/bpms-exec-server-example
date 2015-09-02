package com.example.bpm;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class RestClient {

	private static final String BPMS_USER = "bpmsAdmin";
	private static final String BPMS_PASSWORD = "jbossadmin1!";
	private static final String BPMS_HOST = "as2.example.com:8080";
	private static final String DEPLOYMENT_ID = "com.example.bpm:kie-assets:1.0.2";
	private static final String PROCESS_NAME = "sample-process";

	public static void main(String[] args) {
		// Construct the url
		String uri = "http://" + BPMS_USER + ":" + BPMS_PASSWORD + "@"
				+ BPMS_HOST + "/business-central/rest/runtime/" + DEPLOYMENT_ID
				+ "/process/" + PROCESS_NAME + "/start";

		// Start the process with a name
		String name = "User";
		HttpPost startProcess = new HttpPost(uri + "?map_name=" + name);

		// Try-with-resources
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse httpResponse = httpClient
						.execute(startProcess);) {
			// Process response
			HttpEntity response = httpResponse.getEntity();
			String responseBody = EntityUtils.toString(response);
			System.out.println(responseBody);

			// clean up
			EntityUtils.consume(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
