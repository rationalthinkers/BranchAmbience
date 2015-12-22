package com.branch.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.branch.data.BigFive;
import com.branch.data.BigFiveInput;
import com.branch.data.BigFiveOpenness;
import com.branch.data.BigFiveOpennessInput;
import com.branch.data.InsightVO;
import com.branch.data.MusicTypeEnum;
import com.branch.data.PersonalityInsight;
import com.branch.log.event.LogEventHandler;
import com.branch.response.UploadResponse;

public class BranchUtil {

	private static Map<String, String> musicTypes = new HashMap<String, String>();
	
	public static ArrayList<String> getTwitterMessages(String jsonInput) {

		ArrayList<String> twitterMessages = new ArrayList<String>();
		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(jsonInput);
			JSONArray tweets = jsonObject.getJSONArray("tweets");

			for (int tweetsArrayLength = 0; tweetsArrayLength < tweets.length(); tweetsArrayLength++) {

				JSONObject tweetObject = tweets.getJSONObject(tweetsArrayLength);
				JSONObject messageObject = tweetObject.getJSONObject("message");
				String textObject = messageObject.getString("body");

				twitterMessages.add(textObject);

			}

		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		return twitterMessages;
	}

	public static String listToString(ArrayList<String> messages) {

		String stringMessages = new String();

		for (String str : messages) {

			stringMessages += (" " + str);
		}

		return stringMessages;
	}

	public static void sendTextToScreen(String clientId, String message) {

		JSONObject messageObject = new JSONObject();

		try {
			messageObject.append("code", -1);
			messageObject.append("message", message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendTextToScreen(clientId, messageObject);

	}

	public static void sendTextToScreen(String clientId, JSONObject message) {
		System.out.println("Progress Log :"+message.toString());
		new LogEventHandler(clientId + ":" + message).start();
	}

	public static void sendTextToScreen(String clientId, UploadResponse message) {

		sendTextToScreen(clientId, new JSONObject(message));
	}

	public static BigFiveOpennessInput buildBigFiveOpenness(String jsonPersonalityInsight) {

		BigFiveOpenness bigFiveOpenness = new BigFiveOpenness();

		try {

			JSONObject jsonObject = new JSONObject(jsonPersonalityInsight);

			JSONArray bigFiveArray1 = ((JSONObject) (jsonObject.getJSONObject("tree").getJSONArray("children").get(0)))
					.getJSONArray("children");
			JSONArray bigFiveArray = ((JSONObject) bigFiveArray1.get(0)).getJSONArray("children");

			for (int arraySize = 0; arraySize < bigFiveArray.length(); arraySize++) {

				JSONObject childObject = (JSONObject) bigFiveArray.get(arraySize);

				String behaviourName = childObject.getString("name");

				if ("Openness".equals(behaviourName)) {

					JSONArray childArray = childObject.getJSONArray("children");

					for (int childArraySize = 0; childArraySize < childArray.length(); childArraySize++) {

						JSONObject grandChildObject = (JSONObject) childArray.get(childArraySize);

						InsightVO grandInsightBean = createInsight(grandChildObject);
						if ("Adventurousness".equals(grandInsightBean.getName())) {
							bigFiveOpenness.setAdventurousnessPercent(
									(int) (Double.valueOf(grandInsightBean.getPercentage()) * 100));
						}
						if ("Artistic interests".equals(grandInsightBean.getName())) {
							bigFiveOpenness.setArtisticInterestsPercent(
									(int) (Double.valueOf(grandInsightBean.getPercentage()) * 100));
						}
						if ("Authority-challenging".equals(grandInsightBean.getName())) {
							bigFiveOpenness.setAuthorityChallengingPercent(
									(int) (Double.valueOf(grandInsightBean.getPercentage()) * 100));
						}
						if ("Emotionality".equals(grandInsightBean.getName())) {
							bigFiveOpenness.setEmotionalityPercent(
									(int) (Double.valueOf(grandInsightBean.getPercentage()) * 100));
						}
						if ("Imagination".equals(grandInsightBean.getName())) {
							bigFiveOpenness.setImaginationPercent(
									(int) (Double.valueOf(grandInsightBean.getPercentage()) * 100));
						}
						if ("Intellect".equals(grandInsightBean.getName())) {
							bigFiveOpenness.setIntellectPercent(
									(int) (Double.valueOf(grandInsightBean.getPercentage()) * 100));
						}
						// bigFiveOpenness
						// personalityInsight.getAllInsights().put(grandChildObject.getString("name"),
						// grandInsightBean);
					}
				}

			}

			// System.out.println(((JSONObject)(jsonObject.getJSONObject("tree").getJSONArray("children").get(0))).getJSONArray("children"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BigFiveOpennessInput bigFiveOpennessInput = new BigFiveOpennessInput();
		bigFiveOpennessInput.setBigFiveOpennessInput(bigFiveOpenness);
		return bigFiveOpennessInput;
	}

	public static BigFiveInput buildBigFive(String jsonPersonalityInsight) {

		BigFiveInput bigFiveInput = new BigFiveInput();
		BigFive bigFive = new BigFive();

		try {

			JSONObject jsonObject = new JSONObject(jsonPersonalityInsight);

			JSONArray bigFiveArray1 = ((JSONObject) (jsonObject.getJSONObject("tree").getJSONArray("children").get(0)))
					.getJSONArray("children");
			JSONArray bigFiveArray = ((JSONObject) bigFiveArray1.get(0)).getJSONArray("children");

			for (int arraySize = 0; arraySize < bigFiveArray.length(); arraySize++) {

				JSONObject childObject = (JSONObject) bigFiveArray.get(arraySize);

				String behaviourName = childObject.getString("name");
				int percentage = (int) (childObject.getDouble("percentage") * 100);

				if ("Openness".equals(behaviourName)) {

					bigFive.setOpennessInsight(percentage);
				}

				if ("Conscientiousness".equals(behaviourName)) {

					bigFive.setConscientiousnessInsight(percentage);
				}

				if ("Extraversion".equals(behaviourName)) {

					bigFive.setExtraversionInsight(percentage);
				}

				if ("Agreeableness".equals(behaviourName)) {

					bigFive.setAgreeablenessInsight(percentage);
				}

				if ("Emotional range".equals(behaviourName)) {

					bigFive.setEmotionalRangeInsight(percentage);
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bigFiveInput.setBigFiveInput(bigFive);
		return bigFiveInput;
	}

	public static PersonalityInsight buildPersonalityInsight(String jsonPersonalityInsight) {

		PersonalityInsight personalityInsight = new PersonalityInsight();

		try {

			JSONObject jsonObject = new JSONObject(jsonPersonalityInsight);

			JSONArray bigFiveArray1 = ((JSONObject) (jsonObject.getJSONObject("tree").getJSONArray("children").get(0)))
					.getJSONArray("children");
			JSONArray bigFiveArray = ((JSONObject) bigFiveArray1.get(0)).getJSONArray("children");

			for (int arraySize = 0; arraySize < bigFiveArray.length(); arraySize++) {

				JSONObject childObject = (JSONObject) bigFiveArray.get(arraySize);

				String behaviourName = childObject.getString("name");
				InsightVO insightBean = createInsight(childObject);
				personalityInsight.getAllInsights().put(behaviourName, insightBean);

				if ("Openness".equals(behaviourName)) {

					personalityInsight.setOpennessInsight(insightBean);
				}

				if ("Conscientiousness".equals(behaviourName)) {

					personalityInsight.setConscientiousnessInsight(insightBean);
				}

				if ("Extraversion".equals(behaviourName)) {

					personalityInsight.setExtraversionInsight(insightBean);
				}

				if ("Agreeableness".equals(behaviourName)) {

					personalityInsight.setAgreeablenessInsight(insightBean);
				}

				if ("Emotional range".equals(behaviourName)) {

					personalityInsight.setEmotionalRangeInsight(insightBean);
				}

				JSONArray childArray = childObject.getJSONArray("children");

				for (int childArraySize = 0; childArraySize < childArray.length(); childArraySize++) {

					JSONObject grandChildObject = (JSONObject) childArray.get(childArraySize);

					InsightVO grandInsightBean = createInsight(grandChildObject);
					personalityInsight.getAllInsights().put(grandChildObject.getString("name"), grandInsightBean);
				}
			}

			// System.out.println(((JSONObject)(jsonObject.getJSONObject("tree").getJSONArray("children").get(0))).getJSONArray("children"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return personalityInsight;
	}

	private static InsightVO createInsight(JSONObject jsonObject) {

		InsightVO insightBean = new InsightVO();

		try {

			insightBean.setName(jsonObject.getString("name"));
			// System.out.println(insightBean.getName());
			insightBean.setPercentage(jsonObject.getDouble("percentage") + "");
			insightBean.setSampling_error(jsonObject.getDouble("sampling_error") + "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return insightBean;

	}

	public static String getDomainName(String sUrl) {

		URI uri;
		String domain = "";
		try {
			uri = new URI(sUrl);
			domain = uri.getHost();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return domain.startsWith("www.") ? domain.substring(4) : domain;

	}
	
	public static String getMusicFilePath(String musicType) {
		
		if(musicTypes.isEmpty()) {
			
			for (MusicTypeEnum musicTypeEnum : MusicTypeEnum.values()) {
				musicTypes.put(musicTypeEnum.getMusicType(), musicTypeEnum.getMusicFile());
			}
		}
		return musicTypes.get(musicType);
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// Properties prop = new Properties();
		// prop.load(new DataInputStream(new FileInputStream(new
		// File("c:\\test.txt"))));

		System.out.println(MusicTypeEnum.values()[0].getMusicFile());

		// buildPersonalityInsight((String) prop.get("jsonText"));
		// System.out.println(csDAO.customer(listToString(getTwitterMessages(str))));
	}

	public static UploadResponse buildResponse(int code, String message, String colour, String musicType,
			String musicFilePath) {

		UploadResponse uploadResponse = new UploadResponse();

		uploadResponse.setCode(code);
		uploadResponse.setColour(colour);
		uploadResponse.setMessage(message);
		uploadResponse.setMusicType(musicType);
		uploadResponse.setMusicFilePath(musicFilePath);

		return uploadResponse;
	}
	
	static {
		for (MusicTypeEnum musicTypeEnum : MusicTypeEnum.values()) {
			musicTypes.put(musicTypeEnum.getMusicType(), musicTypeEnum.getMusicFile());
		}
	}
}
