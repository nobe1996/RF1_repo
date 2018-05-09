class Controller{
	//host = 127.0.0.1
	constructor(host,port){
		this.control = host+":"+port;
		if(window.XMLHttpRequest){
			this.xmlhttp = new XMLHttpRequest();
		}
		else{
			this.xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}

	}
	
	login(reqType,uname,pword,resElement){
		var xmlhttp = this.xmlhttp;
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.responseText == "Login failed") {
				document.getElementById(resElement).style.color = "#ff0000";
			}
			else{
				document.getElementById(resElement).style.color = "#00ff00";
          		$("#loginForm").fadeOut(1000);
			}
			document.getElementById(resElement).innerHTML = xmlhttp.responseText;
		};

		xmlhttp.open(reqType,this.control+"?action=login&uname="
		+uname
		+"&pword="
		+pword,true);
		
		xmlhttp.send();
	}
	
	registerUser(reqType,fname,lname,uname,pword,email,resElement){
		var xmlhttp = this.xmlhttp;
		xmlhttp.onreadystatechange = function(){
			document.getElementById(resElement).innerHTML = xmlhttp.responseText;
		};
		
		xmlhttp.open(reqType,this.control+
		"?action=register&entity=user&fname="
		+fname
		+"&lname="
		+lname
		+"&uname="
		+uname
		+"&pword="
		+pword
		+"&email="
		+email,true);
		
		xmlhttp.send();
	}
	
	upload(fileselectid,uploadbtnid){	
		var xmlhttp = this.xmlhttp;
		var fileSelect = document.getElementById('file-select');
		var uploadButton = document.getElementById('upload-button');
		
		form.onsubmit = function(event){
			event.preventDefault();
			
			uploadButton.innerHTML = 'Uploading...';
			
			var files = fileSelect.files;
			var formData = new FormData();
			for (var i = 0; i < files.length; i++) {
			  var file = files[i];

			  // Check the file type.
			  if (!file.type.match('image.*')) {
				continue;
			  }

			  // Add the file to the request.
			  formData.append('photos[]', file, file.name);
			}
			
			xmlhttp.open('POST',this.control+"?action=upload&files="+photos,true);
			
			xmlhttp.onload = function () {
			  if (xmlhttp.status === 200) {
				// File(s) uploaded.
				uploadButton.innerHTML = 'Upload';
			  } else {
				alert('An error occurred!');
			  }
			};
			
			xmlhttp.send(formData);
		}
		
	}
}