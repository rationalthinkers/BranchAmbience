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
          </div><!--close sidebar_item--> 
        </div><!--close sidebar-->
       </div><!--close sidebar_container-->	
	 <form action="Upload" method="post" enctype="multipart/form-data">
	  <div id="content">
        <div class="content_item">
        
		  <h1>Welcome To Branch Ambience Demo</h1> 
		  <br/>
		   <h2>Customer Name Tag:</h2>  <input type="text" name="customerName" id="customerName"/><br/>
		  <div id="displayCam" style="display: block;">
		  <!-- 
		  <img id="scream" width="220" height="277" src="images/srk2.jpg" alt="The Scream">
		   -->
		  <video autoplay id="vid"  width="200" height="200"></video>
			<canvas id="myCanvas" width="200" height="200"  style="border:1px solid #d3d3d3;"></canvas><br>
			<div align="left">
			<input id="takePhoto" type="submit" name="Take_Photo" value="Take Photo" onclick="javascript:snapshot(); return false;"> 
			</div>
			<div align="right">
			<input id="sendPhoto" type="button" name="send_Photo" value="Send Photo" onclick="javascript:scanImage(); return false;"> 
			</div>


		  </div>
		  
           				  
		  <div id="hideCam" style='display: none;'>	  
		  <div class="content_container" >
		    <p>Your Device doesn't support Image Capture <BR/> Use below button to upload photo</p>
		  	
		      <input type="file" name="image" id="image"/>
		   <br/><br/>
		    
		      <input type="submit" name="submit" value="Upload" onclick="javascript:upload(); return false;"> 
		   
		  </div><!--close content_container-->
		  </div>
		  
		  
		</div><!--close content_item-->
		
		
      </div><!--close content-->   
	 <div id="content">
        <div class="content_item">
		<div class="content_container">
		    <h2>Progress log :</h2>
		  	<div id="logMessage" style="width: 650px;" ></div>
		  </div><!--close content_container-->	

	  </div><!--close content_container-->
		  
		  
	</div><!--close content_item-->
	</form>	
	</div><!--close site_content-->  	
  
    <footer>
	  <a href="#">Home</a> | <a href="#">Our Work</a> | <a href="#">Testimonials</a> | <a href="#">Projects</a> | <a href="#">Contact</a>
    </footer> 
  
  </div><!--close main-->
  
  <!-- javascript at the bottom for fast page loading -->
  <script type="text/javascript" src="js/jquery.min.js"></script>
  <script type="text/javascript" src="js/image_slide.js"></script>
  <script type="text/javascript" src="js/fetch.js" > </script>
	<script type="text/javascript">

		var socket = new WebSocket("ws://branchambience.mybluemix.net/websocket/log");
		socket.onopen = sendClientId;
		socket.onmessage = onMessage;
	
		function sendClientId(event) {
			var clientId = document.getElementById('clientId').value;
			console.log(document.getElementById('clientId').value);
			socket.send(clientId);
		}
		
		function onMessage(event) {
			
			var responseMessage = JSON.parse(event.data);
			document.getElementById('logMessage').innerHTML = document.getElementById('logMessage').innerHTML + '<BR />' + responseMessage.message;
		}

		function upload() {
			var image = document.getElementById('image');
			var data = new FormData();
			data.append('file', image.files[0]);
			data.append('customerName', document.getElementById('customerName').value);
			data.append('clientId', document.getElementById('clientId').value);
			fetch('/training', {
			  method: 'post',
			  body: data
			});
		}
	

    var video = document.querySelector("#vid");
    var canvas = document.querySelector('#myCanvas');
    var ctx = canvas.getContext('2d');
    var localMediaStream = null;

    var onCameraFail = function (e) {       
        document.getElementById("displayCam").style.display = "none";
        document.getElementById("hideCam").style.display = "block";
    };

    function snapshot() {
        if (localMediaStream) {
            ctx.drawImage(video, 0, 0, 200, 200);
        }
    }
    
    function scanImage(){
    	
    	 var canvasData = canvas.toDataURL("image/png");
    	 
    	
    	 var blobBin = atob(canvasData.split(',')[1]);
    	 var array = [];
    	 for(var i = 0; i < blobBin.length; i++) {
    	     array.push(blobBin.charCodeAt(i));
    	 }
    	 var file1=new Blob([new Uint8Array(array)], {type: 'image/png'});
    	 var data = new FormData();
    	
    	 data.append('file', file1);
    	 
    	 data.append('customerName', document.getElementById('customerName').value);
			data.append('clientId', document.getElementById('clientId').value);
		 
			fetch('/training', {
			  method: 'post',
			  body: data
			});
			
    	 
    }

    navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
    window.URL = window.URL || window.webkitURL;
    navigator.getUserMedia({video:true}, function (stream) {
        video.src = window.URL.createObjectURL(stream);
        localMediaStream = stream;
    }, onCameraFail);
    
  

</script>
</body>
</html>
