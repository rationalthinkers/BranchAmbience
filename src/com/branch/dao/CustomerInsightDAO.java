package com.branch.dao;

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

import com.branch.util.BranchUtil;
import com.branch.util.services.VCAP_SERVICES;

public class CustomerInsightDAO {

	public String customer(String insightText) {

		String output = "";
		CredentialsProvider credsProvider = new BasicCredentialsProvider();

		// Replace the userid and password with your bluemix app credentials
		// Replace the URL with the base URL of the 'IBM Insights for twitter'
		// service. This could be different in your case
		String username = VCAP_SERVICES.get("personality_insights", "0", "credentials", "username");
		String url = VCAP_SERVICES.get("personality_insights", "0", "credentials", "url");
		String password = VCAP_SERVICES.get("personality_insights", "0", "credentials", "password");
		String domainName = BranchUtil.getDomainName(url);

		credsProvider.setCredentials(new AuthScope(domainName, 443),
				new UsernamePasswordCredentials(username, password));
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

		try {

			HttpHost target = new HttpHost(domainName, 443, "https");

			HttpPost postRequest = new HttpPost("/personality-insights/api/v2/profile");

			postRequest.setHeader("Content-Type", ContentType.TEXT_PLAIN.toString());

			postRequest.setEntity(new StringEntity(insightText));

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
				System.out.println("Personality Insight: " + responseEntity);
				output = EntityUtils.toString(responseEntity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

}
