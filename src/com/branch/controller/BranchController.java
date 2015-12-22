package com.branch.controller;

import static com.branch.constant.ResponseCodes.EMPTY_STRING;
import static com.branch.constant.ResponseCodes.ERR_BUSINESS_RULE_FAILED;
import static com.branch.constant.ResponseCodes.ERR_BUSINESS_RULE_FAILED_CODE;
import static com.branch.constant.ResponseCodes.ERR_CUST_NOT_FOUND;
import static com.branch.constant.ResponseCodes.ERR_CUST_NOT_FOUND_CODE;
import static com.branch.constant.ResponseCodes.ERR_FACE_NOT_RECOGNIZED;
import static com.branch.constant.ResponseCodes.ERR_FACE_NOT_RECOGNIZED_CODE;
import static com.branch.constant.ResponseCodes.ERR_PERSONALITY_INSIGHT_NOT_FOUND;
import static com.branch.constant.ResponseCodes.ERR_PERSONALITY_INSIGHT_NOT_FOUND_CODE;
import static com.branch.constant.ResponseCodes.ERR_TWITTER_DATA_NOT_FOUND;
import static com.branch.constant.ResponseCodes.ERR_TWITTER_DATA_NOT_FOUND_CODE;
import static com.branch.constant.ResponseCodes.MUSIC_DEFAULT_FP;
import static com.branch.constant.ResponseCodes.SUCCESS;
import static com.branch.constant.ResponseCodes.SUCCESS_MESSAGE;
import static com.branch.constant.ResponseCodes.DEFAULT_COLOUR;
import static com.branch.constant.ResponseCodes.MUSIC_DEFAULT;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import com.branch.dao.BusinessRuleDAO;
import com.branch.dao.CustomerDAO;
import com.branch.dao.CustomerInsightDAO;
import com.branch.dao.TwitterInsightDAO;
import com.branch.data.BigFive;
import com.branch.data.Customer;
import com.branch.util.BranchUtil;
import com.github.mhendred.face4j.DefaultFaceClient;
import com.github.mhendred.face4j.FaceClient;
import com.github.mhendred.face4j.model.Face;
import com.github.mhendred.face4j.model.Guess;
import com.github.mhendred.face4j.model.Photo;

public class BranchController {

	/**
	 * Your SkyBiometry API key
	 */
	protected static final String SKY_BIO_API_KEY = "bb4946d0ed514fd289f0bb19cb475815";

	/**
	 * Your SkyBiometry API secret
	 */
	protected static final String SKY_BIO_API_SEC = "0792115db25d481885583d8baaa4e9b6";

	/**
	 * Your SkyBiometry API namespace
	 */
	protected static final String NAMESPACE = "@RationalThinkers";

	private static BranchController branchController = null;

	private CustomerDAO customerDAO = new CustomerDAO();

	private TwitterInsightDAO twitterInsightDAO = new TwitterInsightDAO();

	private CustomerInsightDAO customerInsightDAO = new CustomerInsightDAO();

	private BusinessRuleDAO businessRuleDAO = new BusinessRuleDAO();

	public static BranchController createInstance() {

		return branchController;
	}

	public void triggerPersonalization(String clientId, FileItem multiparts, Boolean stub, String twitterHandle) {

		Customer customer = null;
		if(twitterHandle == null || "".equals(twitterHandle.trim())) {
			String customerPhotoTagName = recognizeFace(clientId, multiparts);
	
			if (null == customerPhotoTagName || "".equals(customerPhotoTagName)) {
	
				BranchUtil.sendTextToScreen(clientId,BranchUtil.buildResponse(ERR_FACE_NOT_RECOGNIZED_CODE, ERR_FACE_NOT_RECOGNIZED, EMPTY_STRING,
						EMPTY_STRING, MUSIC_DEFAULT_FP));
				return;
			}
	
			BranchUtil.sendTextToScreen(clientId, "Face Recognized as " + customerPhotoTagName);
	
			customer = getCustomerDetails(customerPhotoTagName);
			if (null == customer) {
				//BranchUtil.sendTextToScreen(clientId, "Customer details not found, uses Default Branch Ambience");
				/*
				 * Call default branch ambience
				 */
	
				BranchUtil.sendTextToScreen(clientId, BranchUtil.buildResponse(ERR_CUST_NOT_FOUND_CODE, ERR_CUST_NOT_FOUND, DEFAULT_COLOUR, MUSIC_DEFAULT,
						MUSIC_DEFAULT_FP));
				return;
			}
		} else {
			customer = new Customer();
			customer.setTwitterHandle(twitterHandle);
		}
		
		ArrayList<String> twitterMessages = BranchUtil.getTwitterMessages(twitterInsightDAO.searchTwitter(customer));

		if (twitterMessages.isEmpty()) {
			/*
			 * Call default branch ambience
			 */
			BranchUtil.sendTextToScreen(clientId, BranchUtil.buildResponse(SUCCESS, ERR_TWITTER_DATA_NOT_FOUND, EMPTY_STRING,
					EMPTY_STRING, MUSIC_DEFAULT_FP));
			return;
		}

		String personalityInsightJSON = null;
		if(stub) {
			BranchUtil.sendTextToScreen(clientId,
					"Got twitter Insight. <br /> Using stubbed personality insights.");
			System.out.println("Reading stubbed file: insight-stub/personality_" + customer.getTwitterHandle() + ".txt");
			personalityInsightJSON = readFile("insight-stub/personality_" + customer.getTwitterHandle() + ".txt");
		} else {
			
			BranchUtil.sendTextToScreen(clientId,
					"Got twitter Insight. <br /> Starting Personality Analysis using Watson Personality Insight Algorithm");

			personalityInsightJSON = customerInsightDAO.customer(BranchUtil.listToString(twitterMessages));
			System.out.println("Personality JSON Output: start----------------");
			System.out.println(personalityInsightJSON);
			System.out.println("Personality JSON Output: end----------------");
			//writeToFile(personalityInsightJSON, "insight-stub/personality_" + customer.getTwitterHandle() + ".txt");
		}
		/*
		 * Stubbing for personality insight output
		 */

		if (null == personalityInsightJSON || "".equals(personalityInsightJSON.trim())) {

			BranchUtil.sendTextToScreen(clientId, BranchUtil.buildResponse(SUCCESS, ERR_PERSONALITY_INSIGHT_NOT_FOUND,
					DEFAULT_COLOUR, MUSIC_DEFAULT, MUSIC_DEFAULT_FP));
			return;
		}

		BranchUtil.sendTextToScreen(clientId, "Personality Analysis completed sucessfully");

		BigFive bigFive = businessRuleDAO.getpersonalityColour(clientId,
				BranchUtil.buildBigFive(personalityInsightJSON));

		if ((bigFive.getColour() == null || "".equals(bigFive.getColour()))
				&& (bigFive.getMusicType() == null || "".equals(bigFive.getMusicType()))) {

			BranchUtil.sendTextToScreen(clientId, BranchUtil.buildResponse(SUCCESS, ERR_BUSINESS_RULE_FAILED,
					DEFAULT_COLOUR, MUSIC_DEFAULT, MUSIC_DEFAULT_FP));
			return;
		}

		System.out.println(bigFive.getColour());
		System.out.println(bigFive.getMusicType());
		String musicFilePath = BranchUtil.getMusicFilePath(bigFive.getMusicType());

		BranchUtil.sendTextToScreen(clientId, BranchUtil.buildResponse(SUCCESS, SUCCESS_MESSAGE,
				bigFive.getColour(), bigFive.getMusicType(), musicFilePath));
		return;
	}

	public String recognizeFace(String clientId, FileItem fi) {

		FaceClient faceClient = new DefaultFaceClient(SKY_BIO_API_KEY, SKY_BIO_API_SEC);
		Photo photo = null;
		File imageFile = null;
		boolean faceFound = false;
		String customerPhotoTagName = null;

		try {

			if (!fi.isFormField()) {
				imageFile = new File(fi.getName());

				fi.write(imageFile);
				System.out.println(clientId + ": Invoking Sky biometric...");
				BranchUtil.sendTextToScreen(clientId, "Face Recognition started by Invoking Sky biometric");

				photo = faceClient.recognize(imageFile, "all" + NAMESPACE);

				faceFound = false;
				for (Face face : photo.getFaces()) {
					for (Guess guess : face.getGuesses()) {
						System.out.println(guess);
						if (guess.second >= 60) {

							customerPhotoTagName = guess.first.substring(0, guess.first.indexOf("@"));
							faceFound = true;
							break;
						}

					}

					if (faceFound) {
						break;
					}

				}
			}

		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			imageFile.delete();
		}

		return customerPhotoTagName;
	}

	public Customer getCustomerDetails(String photoTag) {

		return customerDAO.identifyCustomerUsingTag(photoTag);
	}

	public Customer identifyCustomer(String clientId, FileItem fi) {

		FaceClient faceClient = new DefaultFaceClient(SKY_BIO_API_KEY, SKY_BIO_API_SEC);
		Photo photo = null;
		File imageFile = null;
		boolean faceFound = false;
		Customer customer = null;

		try {

			if (!fi.isFormField()) {
				imageFile = new File(fi.getName());

				fi.write(imageFile);
				System.out.println(clientId + ": Invoking Sky biometric...");
				BranchUtil.sendTextToScreen(clientId, "Face Recognition started by Invoking Sky biometric");

				photo = faceClient.recognize(imageFile, "all" + NAMESPACE);

				faceFound = false;
				for (Face face : photo.getFaces()) {
					for (Guess guess : face.getGuesses()) {
						System.out.println(guess);
						if (guess.second >= 35) {

							customer = customerDAO
									.identifyCustomerUsingTag(guess.first.substring(0, guess.first.indexOf("@")));
							faceFound = true;
							BranchUtil.sendTextToScreen(clientId,
									"Face Recognized as " + guess.first.subSequence(0, guess.first.indexOf("@")));
							break;
						}

					}

					if (faceFound) {
						break;
					}

				}
				if (!faceFound) {
					BranchUtil.sendTextToScreen(clientId, "Face Not Recognized");
				}

			}

		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			imageFile.delete();
		}

		return customer;
	}

	static {

		branchController = new BranchController();
	}

	public void trainCustomer(String clientId, String customerName, FileItem fi) {

		FaceClient faceClient = new DefaultFaceClient(SKY_BIO_API_KEY, SKY_BIO_API_SEC);
		Photo photo = null;
		File imageFile = null;

		try {

			if (!fi.isFormField()) {
				imageFile = new File(fi.getName());

				fi.write(imageFile);
				// System.out.println(clientId + ": Invoking Sky biometric...");
				// BranchUtil.sendTextToScreen(clientId, "Face Recognition
				// started by Invoking Sky biometric");

				photo = faceClient.detect(imageFile);// .recognize(imageFile,
														// "all@" + NAMESPACE);

				Face f = photo.getFace();
				faceClient.saveTags(f.getTID(), customerName + NAMESPACE, customerName);

				faceClient.train(customerName + NAMESPACE);

				System.out.println("Face trained to the user " + customerName);
				BranchUtil.sendTextToScreen(clientId, "Face trained to the user " + customerName);
			}

		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			BranchUtil.sendTextToScreen(clientId, "Unable to train the customer image, please try again later");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			BranchUtil.sendTextToScreen(clientId, "Unable to train the customer image, please try again later");
		} finally {
			imageFile.delete();
		}

	}

	private String readFile(String filePath) {

		BufferedReader br = null;
		String fileContent = new String();

		try {

			String sCurrentLine;

			InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
			br = new BufferedReader(new InputStreamReader(is));

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

		return fileContent;
	}

}
