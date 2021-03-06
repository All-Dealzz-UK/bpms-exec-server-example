package com.example.bpm;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.services.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

public class RestClient {
	
	private static final String BPMS_USER = "bpmsAdmin";
	private static final String BPMS_PASSWORD = "SECRET";
	private static final String BPMS_HOST = "as1.example.com:8080";
	private static final String DEPLOYMENT_ID = "com.example.bpm:kie-assets:1.0.2";
	private static final String PROCESS_NAME = "sample-process";
	
	public static void main(String[] args) throws MalformedURLException {
		URL baseUrl = new URL("http://" + BPMS_HOST + "/business-central");
		
		RemoteRestRuntimeEngineFactory remoteRestRuntimeEngineFactory = RemoteRestRuntimeEngineFactory.newBuilder()
				.addDeploymentId(DEPLOYMENT_ID)
				.addUserName(BPMS_USER)
				.addPassword(BPMS_PASSWORD)
				.addUrl(baseUrl)
				.build();
		
		RemoteRuntimeEngine runtimeEngine = remoteRestRuntimeEngineFactory.newRuntimeEngine();
		KieSession kSession = runtimeEngine.getKieSession();
		
		// Call process with a name
		Map<String, Object> processVars = new HashMap<>();
		processVars.put("name", "User");
		kSession.startProcess(PROCESS_NAME, processVars);
	}
	
	

}
