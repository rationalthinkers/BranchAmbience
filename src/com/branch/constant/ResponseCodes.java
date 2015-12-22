package com.branch.constant;

public class ResponseCodes {

	public static String ERR_CUST_NOT_FOUND = "Customer details not found, uses Default Branch Ambience";
	public static String ERR_FACE_NOT_RECOGNIZED = "Face Not Recognized";
	public static String ERR_TWITTER_DATA_NOT_FOUND = "Unable to get Twitter Insight, Uses Default Branch Ambience";
	public static String ERR_PERSONALITY_INSIGHT_NOT_FOUND = "Personality Analysis failed";
	public static String ERR_BUSINESS_RULE_FAILED = "Business Rule Execution failed";

	public static int ERR_CUST_NOT_FOUND_CODE = 100;
	public static int ERR_FACE_NOT_RECOGNIZED_CODE = 200;
	public static int ERR_TWITTER_DATA_NOT_FOUND_CODE = 300;
	public static int ERR_PERSONALITY_INSIGHT_NOT_FOUND_CODE = 400;
	public static int ERR_BUSINESS_RULE_FAILED_CODE = 500;

	public static int SUCCESS = 0;
	public static String SUCCESS_MESSAGE = "Program successfully executed!!!";

	public static String DEFAULT_COLOUR = "Blue";
	public static String MUSIC_DEFAULT = "Classical";
	public static String MUSIC_DEFAULT_FP = "/music/classical.mp3";

	public static String EMPTY_STRING = "";

}
