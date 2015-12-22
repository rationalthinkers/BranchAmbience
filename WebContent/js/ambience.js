/**
 * http://usejsdoc.org/
 */
var socket = new WebSocket("ws://branchambience.mybluemix.net/websocket/log");
socket.onopen = sendClientId;
socket.onmessage = onMessage;

var video = document.querySelector("#vid");
var canvas = document.querySelector('#myCanvas');
var ctx = canvas.getContext('2d');
var localMediaStream = null;

function sendClientId(event) {
	var clientId = document.getElementById('clientId').value;
	console.log(document.getElementById('clientId').value);
	socket.send(clientId);
}

function onMessage(event) {
	var responseMessage = JSON.parse(event.data);
	console.log(responseMessage);
	
	if(responseMessage.code == -1) {
		
		document.getElementById('logMessage').innerHTML = document.getElementById('logMessage').innerHTML + '<BR />' + responseMessage.message;
	} else if (responseMessage.code == 0) {
		console.log(responseMessage.message);
		console.log(responseMessage.colour);
		if(responseMessage.colour != null) {
			console.log('colour Name' + responseMessage.colour);
			implementBgImage(responseMessage.colour)
		}
		
		if(responseMessage.musicFilePath !=null && responseMessage.musicFilePath !=''){
			console.log('music File Path :' + responseMessage.musicFilePath);
			implementBgMusic(responseMessage.musicFilePath)
		}
	} else {
		document.getElementById('logMessage').innerHTML = document.getElementById('logMessage').innerHTML + '<BR />' + responseMessage.message + '<BR />' ;		
		//if face not recognized or customer not found
		if(responseMessage.code == 100 || responseMessage.code == 200 ) {
			console.log('Restarting the face detection');
			App.faceDetected=false;
			//App.start('detectFace');
			
		}
		 
		
	
	}
}

function implementBgImage(colorName){
	var bgImage=document.getElementById("bgImage");
	bgImage.className='bg'+colorName;
	
}

function implementBgMusic(musicFilePath){
	var pAudio=document.getElementById("personalAudio");
	pAudio.src=musicFilePath;
	pAudio.load();
	pAudio.play();
}

function upload() {
	var image = document.getElementById('image');
	var uploadData = new FormData();
	var cid= document.getElementById('clientId').value;
	var toStub = document.getElementById('toStub').checked?'True':'False';
	
	uploadData.append('file', image.files[0]);
	uploadData.append('clientId', document.getElementById('clientId').value);
	uploadData.append('toStub', toStub);
	uploadData.append('twitterHandle', document.getElementById('twitterHandle').value);
	twitterHandle
	fetch('/Upload', {
	  method: 'post',
	  body: uploadData
	});
	
}

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
	data.append('clientId', document.getElementById('clientId').value);
	var toStub = document.getElementById('toStub').checked?'True':'False';
	uploadData.append('toStub', toStub);
	fetch('/Upload', {
		method: 'post',
		body: data
	});
}
