var HttpClient = function() {
    this.get = function(aUrl, aCallback) {
        var anHttpRequest = new XMLHttpRequest();
            anHttpRequest.onreadystatechange = function() { 
        if (anHttpRequest.readyState == 4 && anHttpRequest.status == 200)
                aCallback(anHttpRequest.responseText);
        }
        anHttpRequest.open( "GET", aUrl, true );            
		anHttpRequest.send( null );
		
		/*
		 GET usage: 
			var client = new HttpClient();
    		client.get('http://localhost:8082/hello', function(response) {
            	console.log(response);
        	});
		*/
	}
	
	this.post = function(URL, params){
		var xhr = new XMLHttpRequest();
		xhr.open("POST", URL, true);
		
		xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		
		xhr.send(params);

		/*
		 POST usage:
			var client = new HttpClient();
			client.post('http://localhost:8082/goodbye', "param=something");
			//use "param0=asd&param1=dsa" for multiple
		*/
	}
}