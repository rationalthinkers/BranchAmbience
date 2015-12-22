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
          <li><img width="940" height="300" src="images/home_2.jpg" alt="&quot;Branch Customer Image Training&quot;" /></li>
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
            <p>SQL DB <br/>Twitter Insight <BR/> Personality Insight </p>         
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
            <p>Email: <a href="mailto:rationalthinkers@gmail.com">tcs.rationalthinkers@gmail.com</a></p>
          </div><!--close sidebar_item--> 
        </div><!--close sidebar-->
       </div><!--close sidebar_container-->	
	 <form action="Upload" method="post" enctype="multipart/form-data">
	  <div id="content">
        <div class="content_item">
		  <h1>Welcome To Branch Ambience Demo</h1> 
          <p>If your device supports camera, use the below button to take your picture and continue</p>   				  
		  	  
		  <div class="content_container">
		    Customer Name Tag:  <input type="text" name="customerName" id="customerName"/>
		    <p>Use the below button to upload the photo</p>
		  	
		      <input type="file" name="image" id="image"/>
		   <br/><br/>
		    
		      <input type="submit" name="submit" value="Upload" onclick="javascript:upload(); return false;"> 
		   
		  </div><!--close content_container-->
		  
		  
		  
		</div><!--close content_item-->
		
		
      </div><!--close content-->   
	 <div id="content">
        <div class="content_item">
		<div class="content_container">
		    <h2>Progress log :</h2>
		  	<div id="logMessage"></div>
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
			
			document.getElementById('logMessage').innerHTML = document.getElementById('logMessage').innerHTML + '<BR />' + event.data;
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
	</script>
</body>
</html>
