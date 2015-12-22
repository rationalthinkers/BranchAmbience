<%@ page language="java" session="true" %>
<!DOCTYPE html> 
<html>

<head>
  <title>Branch Ambience Demo</title>
  <meta name="description" content="website description" />
  <meta name="keywords" content="website keywords, website keywords" />
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" type="text/css" href="css/style.css" />
  <!-- modernizr enables HTML5 elements and feature detects -->
  <script type="text/javascript" src="js/modernizr-1.5.min.js"></script>
  <script type="text/javascript" src="js/ccv.js"></script>
<script type="text/javascript" src="js/face.js"></script>
<script type="text/javascript" src="js/faceDetect.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/stackblur.js"></script>
<script type="text/javascript" src="js/jquery.ajaxfileupload.js"></script>

</head>
<%
	String clientId = (((int)(Math.random() * 1000000))) + ""; 
	
%>
<input type="hidden" id="clientId" value=<%=clientId %>>
<body>
  <div id="main">
    
	<div id="site_content">		

      <div class="slideshow">
	    <ul class="slideshow">
          <li class="show"><img width="940" height="300" src="images/home_1.jpg" alt="&quot;Rational Thinkers Presents&quot;" /></li>
          <li><img width="940" height="300" src="images/home_2.jpg" alt="&quot;Branch Ambience Demo&quot;" /></li>
        </ul> 
	  </div><!--close slideshow-->	
	
	  <div class="sidebar_container">       
		<div class="sidebar">
          <div class="sidebar_item">
            <h2>Technologies Used</h2>
            <p>Java <br/> J2EE <br/></p>
          </div><!--close sidebar_item--> 
        </div><!--close sidebar-->     		
		<div class="sidebar">
          <div class="sidebar_item">
            <h2>IBM Bluemix </h2> <h3>Services Used</h3>           
            <p>SQL DB <br/>Twitter Insight <BR/> Personality Insight <Br/> Business Rules</p>         
		  </div><!--close sidebar_item--> 
        </div><!--close sidebar-->
		<div class="sidebar">
          <div class="sidebar_item">
            <h3>Team Members</h3>
            <p>Yuvaraj Nithyanandam <br/>KamalaKannan M <br/>Selvamanickam T M<br/>Arun Kumar S <br/>Ponsundar R<br/>Viswalingam D<br/></p>         
		  </div><!--close sidebar_item--> 
        </div><!--close sidebar-->  		
        <div class="sidebar">
          <div class="sidebar_item">
            <h2>Contact</h2>
            <p>Phone: +91 (0)44 67424648</p>
            <p>Email: <a href="mailto:tcs.rationalthinkers@gmail.com">tcs.rationalthinkers@gmail.com</a></p>
            <input type="checkbox" id="toStub" checked="checked" >
          </div><!--close sidebar_item--> 
        </div><!--close sidebar-->
       </div><!--close sidebar_container-->	
	 <form action="Upload" method="post" enctype="multipart/form-data">

	  <div id='bgImage'>
        
        
		  <h1>Welcome To Branch Ambience Demo</h1> 
		  <div id="displayCam" style="display: block;">
		  <!-- 
		  <img id="scream" width="220" height="277" src="images/srk2.jpg" alt="The Scream">
		   -->
		  <video autoplay id="vid"  width="150px" height="150px"></video>
		  <div style='display: none;'>
		  <canvas id="output"  width="150px" height="125px" ></canvas>
		  </div>
			
			<canvas id="myCanvas" width="150px" height="125px"></canvas><br>
			<!-- 
			<div align="left" >
			<input id="takePhoto" type="submit" name="Take_Photo" value="Take Photo" onclick="javascript:snapshot(); return false;"> 
			</div>
			<div align="right">
			<input id="sendPhoto" type="button" name="send_Photo" value="Send Photo" onclick="javascript:scanImage(); return false;"> 
			</div>
			 -->

		  </div>
		  
           				  
		  <div id="hideCam" style='display: none;'>	  
		  
		    <p>Your Device doesn't support Image Capture <BR/> Use below button to upload photo</p>
		  	
		      <input type="file" name="image" id="image"/>
		   <br/><br/>
		    
		      <input type="submit" name="submit" value="Upload" onclick="javascript:upload(); return false;"> 
		      
		   
		  
		  </div>
		  <br/><br/>
		  <div>
      <audio autoplay="autoplay" id="personalAudio" controls="controls">  </audio>
      </div>
		  
	      <input type="text" name="twitterHandle" id="twitterHandle"/>
		  
		  <h2>Progress log :</h2>
		  	<div id="logMessage" style="width: 650px;" ></div>
		  
		
		
		
      </div><!--close content-->   
      
	 
	</form>	
	</div><!--close site_content-->  	
  
    <footer>
	  <a href="#">Home</a> | <a href="#">Our Work</a> | <a href="#">Testimonials</a> | <a href="#">Projects</a> | <a href="#">Contact</a>
    </footer> 
  
  </div><!--close main-->
  
  <!-- javascript at the bottom for fast page loading -->
  
  <script type="text/javascript" src="js/image_slide.js"></script>
  <script type="text/javascript" src="js/fetch.js" > </script>
  <script type="text/javascript" src="js/ambience.js" > </script>
</body>
</html>
