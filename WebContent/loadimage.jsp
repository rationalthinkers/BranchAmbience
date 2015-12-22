<%@ page language="java" session="true" %>
<!DOCTYPE html>
<html>
<head>
	<title>Branch</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="style.css" />
</head>
<body>

<%
	String clientId = (((int)(Math.random() * 1000000))) + ""; 
	session.setAttribute("clientId", clientId);
	System.out.println("JSP Session Id" + request.getRequestedSessionId());
%>

	<input type="hidden" id="clientId" value=<%=clientId %>>
	<table>
		<tr>
			<td style='width: 30%;'><img class = "newappIcon" src='images/newapp-icon.png'>
			</td>
			<td>
				<h1 id = "message"></h1>
				<p class='description'></p> Thanks for creating a <span class="blue">Liberty for Java Starter Application</span>. Get started by reading our <a
				href="https://www.ng.bluemix.net/docs/#starters/liberty/index.html#liberty">documentation</a>
				or use the Start Coding guide under your app in your dashboard.
			</td>
		</tr>
	</table>
	<script type="text/javascript" src="index.js"></script>
	<form action="Upload" method="post" enctype="multipart/form-data">
	<table  style='width: 70%;align: center;'>
		<tr>
			<td align="right">Upload File</td>
			<td><input type="file" name="image" id="image"/></td>
			<td><input type="button" name="submit" value="Upload" onclick="javascript:upload(); return false;"> </td>		
		</tr>
	</table>
	</form>
	<div id="logMessage">
	</div>
	<script type="text/javascript" src="js/fetch.js" > </script>
	<script type="text/javascript">

		var socket = new WebSocket("ws://branch.mybluemix.net/websocket/log");
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
			data.append('clientId', document.getElementById('clientId').value);
		
			fetch('/Upload', {
			  method: 'post',
			  body: data
			});
		}
	</script>
</body>
</html>