package com.branch.data;

import java.util.HashMap;

public class PersonalityInsight {

	private InsightVO opennessInsight;
	private InsightVO conscientiousnessInsight;
	private InsightVO extraversionInsight;
	private InsightVO agreeablenessInsight;
	private InsightVO emotionalRangeInsight;

	private HashMap<String, InsightVO> allInsights = new HashMap<String, InsightVO>();

	public InsightVO getOpennessInsight() {
		return opennessInsight;
	}

	public void setOpennessInsight(InsightVO opennessInsight) {
		this.opennessInsight = opennessInsight;
	}

	public InsightVO getConscientiousnessInsight() {
		return conscientiousnessInsight;
	}

	public void setConscientiousnessInsight(InsightVO conscientiousnessInsight) {
		this.conscientiousnessInsight = conscientiousnessInsight;
	}

	public InsightVO getExtraversionInsight() {
		return extraversionInsight;
	}

	public void setExtraversionInsight(InsightVO extraversionInsight) {
		this.extraversionInsight = extraversionInsight;
	}

	public InsightVO getAgreeablenessInsight() {
		return agreeablenessInsight;
	}

	public void setAgreeablenessInsight(InsightVO agreeablenessInsight) {
		this.agreeablenessInsight = agreeablenessInsight;
	}

	public InsightVO getEmotionalRangeInsight() {
		return emotionalRangeInsight;
	}

	public void setEmotionalRangeInsight(InsightVO emotionalRangeInsight) {
		this.emotionalRangeInsight = emotionalRangeInsight;
	}

	public HashMap<String, InsightVO> getAllInsights() {
		return allInsights;
	}

	public void setAllInsights(HashMap<String, InsightVO> allInsights) {
		this.allInsights = allInsights;
	}

}
