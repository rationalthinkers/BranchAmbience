package com.branch.dao;

import javax.ws.rs.core.Response;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.branch.data.Customer;
import com.branch.util.services.VCAP_SERVICES;

public class TwitterInsightDAO {

	public String searchTwitter(Customer customer) {

		String output = "";
		CredentialsProvider credsProvider = new BasicCredentialsProvider();

		// Replace the userid and password with your bluemix app credentials
		// Replace the URL with the base URL of the 'IBM Insights for twitter'
		// service. This could be different in your case
		String username = VCAP_SERVICES.get("twitterinsights", "0", "credentials", "username");
		String host = VCAP_SERVICES.get("twitterinsights", "0", "credentials", "host");
		String port = "443";// VCAP_SERVICES.get("twitterinsights", "0",
							// "credentials", "port");
		String password = VCAP_SERVICES.get("twitterinsights", "0", "credentials", "password");
		System.out.println("twitterinsights host " + host);
		System.out.println("twitterinsights username " + username);
		System.out.println("twitterinsights password " + password);

		credsProvider.setCredentials(new AuthScope(host, Integer.parseInt(port)),
				new UsernamePasswordCredentials(username, password));
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

		try {
			// Replace the URL with the base URL of the 'IBM Insights for
			// twitter' service
			HttpHost target = new HttpHost(host, Integer.parseInt(port), "https");

			// Replace the value for parameter 'q' to any search term
			String qString = null;

			if (!"".equals(customer.getTwitterHandle())) {
				qString = "from:" + customer.getTwitterHandle();

			} else {
				/*
				 * query to be prepared to search a customer
				 */
			}

			HttpGet getRequest = new HttpGet("/api/v1/messages/search?size=200&q=" + qString);

			System.out.println("executing request to " + target);

			CloseableHttpResponse httpResponse = httpclient.execute(target, getRequest);

			HttpEntity entity = httpResponse.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(httpResponse.getStatusLine());
			Header[] headers = httpResponse.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i]);
			}
			System.out.println("----------------------------------------");

			if (entity != null) {
				// System.out.println(EntityUtils.toString(entity));
				output = EntityUtils.toString(entity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

}
