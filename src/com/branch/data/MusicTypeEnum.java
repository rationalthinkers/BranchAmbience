package com.branch.data;

public enum MusicTypeEnum {
	
	ROCK("Rock", "/audio/rock.mp3"), 
	COUNTRY("Country", "/audio/country.mp3"), 
	HIPHOP("Hip Hop", "/audio/hiphop.mp3"), 
	UPBEAT("Upbeat", "/audio/upbeat.mp3"),
	CLASSICAL("Classical", "/audio/classical.mp3");

	private String musicType;
	private String musicFilePath;

	private MusicTypeEnum(String music, String file) {
		musicType = music; 
		musicFilePath = file;
	}

	public String getMusicType() {
		return musicType;
	}

	public String getMusicFile() {
		return musicFilePath;
	}
	
}
