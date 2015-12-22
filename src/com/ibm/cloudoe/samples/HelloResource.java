package com.ibm.cloudoe.samples;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Path("/hello")
public class HelloResource {

	@GET
	public String getInformation() {

		// 'VCAP_APPLICATION' is in JSON format, it contains useful information
		// about a deployed application
		// String envApp = System.getenv("VCAP_APPLICATION");

		// 'VCAP_SERVICES' contains all the credentials of services bound to
		// this application.
		// String envServices = System.getenv("VCAP_SERVICES");
		// JSONObject sysEnv = new JSONObject(System.getenv());

		return "Hi World!";

	}

	public Content newSearch() throws Exception {

		String baseURL = "https://cdeservice.mybluemix.net";
		String username = "480214f6-45c9-4ea5-9629-ffa27e3dcb69";
		String password = "T9ui1n3bZ8";

		URI profileURI = new URI(baseURL + "/api/v1/messages/search?q=from:iamsrk&size=100").normalize();

		Request profileRequest = Request.Get(profileURI).addHeader("Accept", "application/json");

		Executor executor = Executor.newInstance().auth(username, password);
		org.apache.http.client.fluent.Response response = executor.execute(profileRequest);

		return response.returnContent();

	}

	@GET
	@Path("/searchTwitter")
	public Response searchTwitter() {

		String output = "";
		CredentialsProvider credsProvider = new BasicCredentialsProvider();

		// Replace the userid and password with your bluemix app credentials
		// Replace the URL with the base URL of the 'IBM Insights for twitter'
		// service. This could be different in your case
		credsProvider.setCredentials(new AuthScope("cdeservice.mybluemix.net", 443),
				new UsernamePasswordCredentials("480214f6-45c9-4ea5-9629-ffa27e3dcb69", "T9ui1n3bZ8"));
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

		try {
			// Replace the URL with the base URL of the 'IBM Insights for
			// twitter' service
			HttpHost target = new HttpHost("cdeservice.mybluemix.net", 443, "https");

			// Replace the value for parameter 'q' to any search term

			HttpGet getRequest = new HttpGet("/api/v1/messages/search?q=airport&size=5");

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
		return Response.status(200).entity(output).build();
	}

}