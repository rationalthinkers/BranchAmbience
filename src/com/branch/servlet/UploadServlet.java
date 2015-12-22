package com.branch.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import com.branch.controller.BranchController;
import com.branch.data.Customer;
import com.branch.response.UploadResponse;
import com.branch.util.services.VCAP_SERVICES;
import com.github.mhendred.face4j.DefaultFaceClient;
import com.github.mhendred.face4j.FaceClient;
import com.github.mhendred.face4j.model.Face;
import com.github.mhendred.face4j.model.Guess;
import com.github.mhendred.face4j.model.Photo;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/Upload")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Your SkyBiometry API key
	 */
	protected static final String API_KEY = "bb4946d0ed514fd289f0bb19cb475815";

	/**
	 * Your SkyBiometry API secret
	 */
	protected static final String API_SEC = "0792115db25d481885583d8baaa4e9b6";

	/**
	 * Your SkyBiometry API namespace
	 */
	protected static final String NAMESPACE = "RationalThinkers";

	/**
	 * user id to recognize
	 */
	protected static final String USER_ID = "SachinTendulkar@" + NAMESPACE;

	/**
	 * Default constructor.
	 */
	public UploadServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String username = VCAP_SERVICES.get("twitterinsights", "0", "credentials", "username");
		String host = VCAP_SERVICES.get("twitterinsights", "0", "credentials", "host");
		String port = "443";// VCAP_SERVICES.get("twitterinsights", "0",
							// "credentials", "port");
		String password = VCAP_SERVICES.get("twitterinsights", "0", "credentials", "password");
		System.out.println("twitterinsights host " + host);
		System.out.println("twitterinsights username " + username);
		System.out.println("twitterinsights password " + password);

		HttpSession session = request.getSession();
		session.setAttribute("Local", "Upload");
		System.out.println("Inside Upload get");
		// request.getRequestDispatcher("loadimage.jsp").forward(request,
		// response);
		request.getRequestDispatcher("branchAmbianceDemo.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String username = VCAP_SERVICES.get("personality_insights", "0", "credentials", "username");
		System.out.println("PersonalityInsights UN1" + username);

		BranchController branchController = new BranchController();
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				FileItem multiFile = multiparts.get(1);
				FileItem toStubFI = multiparts.get(2);
				Boolean stub = Boolean.valueOf(toStubFI.getString());
				FileItem twitterhandleFile = multiparts.get(3);

				branchController.triggerPersonalization(multiFile.getString(), multiparts.get(0), stub, twitterhandleFile.getString());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//session.setAttribute("CustomerInContext", customer);
		
//		response.getWriter().print(new JSONObject(uploadResponse).toString());

		// request.getRequestDispatcher("IdentifiedCustomer.jsp").forward(request,
		// response);

		// doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost1(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		FaceClient faceClient = new DefaultFaceClient(API_KEY, API_SEC);
		Photo photo = null;
		File imageFile = null;
		boolean faceFound = false;

		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

				for (FileItem fi : multiparts) {

					if (!fi.isFormField()) {
						imageFile = new File(fi.getName());

						fi.write(imageFile);

						photo = faceClient.recognize(imageFile, "all@" + NAMESPACE);

						// response.getWriter().append("Your guess:
						// ").append(photo.getFaces().get(0).getGuesses().get(0).first);
						faceFound = false;
						for (Face face : photo.getFaces()) {
							for (Guess guess : face.getGuesses()) {
								faceFound = true;
								response.getWriter().append("Your guess: ")
										.append(guess.first.subSequence(0, guess.first.indexOf("@")));
								System.out.println(guess);
							}
						}

						if (!faceFound) {

							response.getWriter().append("Your guess: No match to the user");

						}

						imageFile.delete();

					}
				}
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// doGet(request, response);
	}

}
