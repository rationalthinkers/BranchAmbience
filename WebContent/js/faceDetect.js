/** Start Google Chrome Canary with open -a Google\ Chrome\ Canary --args --enable-media-stream  OR enable the flag in about:flags **/

var App = {

	// Run if we do have camera support
	successCallback : function(stream) {
        console.log('yeah! camera support!');
        
        App.video.src = window.URL.createObjectURL(stream);
    },

	// run if we dont have camera support
	errorCallback : function(error) {
		alert('An error occurred while trying to get camera access (Your browser probably doesnt support getUserMedia() ): ' + error.code);
		document.getElementById("displayCam").style.display = "none";
        document.getElementById("hideCam").style.display = "block";
		return;
	},


	drawToCanvas : function(effect) {
		var video = App.video,
			ctx = App.ctx,
			canvas = App.canvas,
			canvasResult = App.canvasResult,
			ctxResult = App.ctxResult,
			i;
			ctx.drawImage(video, 0, 0, 150,125);

			App.pixels = ctx.getImageData(0,0,canvas.width,canvas.height);
			App.pixelResult = ctx.getImageData(0,0,canvasResult.width,canvasResult.height);

		 if(effect === 'detectFace') {
			var comp = ccv.detect_objects({ "canvas" : (App.canvas),
											"cascade" : cascade,
											"interval" : 10,
											"min_neighbors" : 1 });

			// Draw glasses on everyone!
			if(comp.length>0 && comp[0].confidence>3){
				ctxResult.putImageData(App.pixelResult,0,0);
				App.faceDetected=true;	
				scanImage();
				//clearInterval(App.playing);
			}
			/*
			comp.forEach(function(face) {
				ctx.beginPath();
				ctx.rect(face.x, face.y, face.width, face.height);
				ctx.lineWidth = 2;
				ctx.strokeStyle = 'green';
				ctx.stroke();
				//console.log('face confidence :'+face.confidence);
		       });
		       */
		    
		    /*
			for (i = 0; i < comp.length; i++) {
				ctx.drawImage(App.glasses, comp[i].x, comp[i].y,comp[i].width, comp[i].height);
			}*/
						
		}
	
					
	},

	start : function(effect) {		
		if(App.playing) { clearInterval(App.playing); }
		App.playing = setInterval(function() {
			if(!App.faceDetected){
				App.drawToCanvas(effect);
			}
		},50);
	}
};

App.init = function() {
	// Prep the document
	App.video = document.querySelector('video');
	
	App.glasses = new Image();
	App.glasses.src = "images/glasses.png";

	App.canvas = document.querySelector("#output");
	App.canvasResult = document.querySelector("#myCanvas");
	App.ctx = this.canvas.getContext("2d");
	App.ctxResult = this.canvasResult.getContext("2d");
	App.faceDetected=false;

	// Finally Check if we can run this puppy and go!
	/*
	if (navigator.webkitGetUserMedia) {
		navigator.webkitGetUserMedia('video', App.successCallback, App.errorCallback);
	}
	*/
	navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
    window.URL = window.URL || window.webkitURL;
    navigator.getUserMedia({video:true}, App.successCallback,  App.errorCallback);
	

	App.start('detectFace');

};


document.addEventListener("DOMContentLoaded", function() {
	console.log('ready!');
	App.init();
}, false);