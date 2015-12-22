package com.branch.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.branch.data.BigFive;
import com.branch.data.BigFiveInput;
import com.branch.util.BranchUtil;
import com.branch.util.services.VCAP_SERVICES;

public class BusinessRuleDAO {

	public BigFive getpersonalityColour(String clientId, BigFiveInput bigFiveInput) {

		JSONObject jsonObject = new JSONObject(bigFiveInput);
		BigFive responseObject = bigFiveInput.getBigFiveInput();

		CredentialsProvider credsProvider = new BasicCredentialsProvider();

		// Replace the userid and password with your bluemix app credentials
		// Replace the URL with the base URL of the 'IBM Insights for twitter'
		// service. This could be different in your case
		String username = VCAP_SERVICES.get("businessrules", "0", "credentials", "user");
		String url = VCAP_SERVICES.get("businessrules", "0", "credentials", "executionRestUrl");
		String password = VCAP_SERVICES.get("businessrules", "0", "credentials", "password");
		
//		String username = "resAdmin";
//		String url = "https://brsv2-b96f6b25.ng.bluemix.net/DecisionService/rest";
//		String password = "sl6q1r8hosbp";

		String domainName = BranchUtil.getDomainName(url);
		credsProvider.setCredentials(new AuthScope(domainName, 443),
				new UsernamePasswordCredentials(username, password));
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

		try {

			HttpHost target = new HttpHost(domainName, 443, "https");

			HttpPost postRequest = new HttpPost("/DecisionService/rest/ambianceBR/1.0/AmbienceRule/1.0/JSON");

			postRequest.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());

			postRequest.setEntity(new StringEntity(jsonObject.toString()));

			System.out.println("executing request to " + target);

			CloseableHttpResponse httpResponse = httpclient.execute(target, postRequest);

			HttpEntity responseEntity = httpResponse.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(httpResponse.getStatusLine());
			Header[] headers = httpResponse.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i]);
			}
			System.out.println("----------------------------------------");

			if (responseEntity != null) {
				// System.out.println(EntityUtils.toString(entity));
				String output = EntityUtils.toString(responseEntity);
				System.out.println(output);
				JSONObject jsonResult = new JSONObject(output);
				responseObject.setColour(jsonResult.getString("colourName"));
				responseObject.setMusicType(jsonResult.getString("musicType"));
				//BranchUtil.sendTextToScreen(clientId, "Business Rule Execution successful");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseObject;
	}
	
	public static void main(String[]arg) {
		
		String fileName = "C:/Users/224889/workspace/BranchAmbience/WebContent/WEB-INF/classes/insight-stub/personality_black.txt";
		BufferedReader br = null;
		String fileContent = new String();
		try {

			String sCurrentLine;

			FileInputStream fis = new FileInputStream(new File(fileName));
			br = new BufferedReader(new InputStreamReader(fis));

			while ((sCurrentLine = br.readLine()) != null) {
				fileContent += sCurrentLine;
				// System.out.println(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		BigFive bigFive = new BusinessRuleDAO().getpersonalityColour("", BranchUtil.buildBigFive(fileContent));
		
		System.out.println(bigFive.getColour());
		System.out.println(bigFive.getMusicType());
		
//		return fileContent;
		
		
	}

}
