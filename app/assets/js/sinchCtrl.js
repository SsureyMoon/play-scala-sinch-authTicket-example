$(function () {
	console.log("welcome")

	/*** Set up sinchClient ***/

	sinchClient = new SinchClient({
		applicationKey: app_key, //Your sinch application key
		capabilities: {calling: true},
		startActiveConnection: true, /* NOTE: This is required if application is to receive calls / instant messages. */
		//Note: For additional loging, please uncomment the three rows below
		onLogMessage: function(message) {
			console.log(message);
		},
	});

	$('button#call-now').on('click', function(event) {
		event.preventDefault();

		var signUpObj = {};

		signUpObj.username = "anyusername@example.com";

    Q($.ajax({type:'POST', contentType:'application/json', dataType:'json', url:'http://localhost:9000/makecall',  data:JSON.stringify(signUpObj), success:function(json) {}}))
    		.then(sinchClient.start.bind(sinchClient))
    		.then(function() {
    			alert("sinch Auth ticket success")
    			console.log("sinch Auth ticket success")
    			$('div#message').html("<h3>Sinch Authentication ticket was generated. Check the console.</h3>")
    			})
    		.fail(handleError);
    	})

	/*	Q($.post('http://localhost:9000/makecall', JSON.stringify(signUpObj), {}, "json"))
		.then(sinchClient.start.bind(sinchClient))
		.then(function() {
			alert("sinch Auth ticket success")
			console.log("sinch Auth ticket success")
			$('div#message').html("<h3>Sinch Authentication ticket was generated. Check the console.</h3>")
			})
		.fail(handleError);
	})*/

	var handleError = function(error) {
    	try {
    		alert("sinch Auth ticket fail")
    		$('div#message').html("<h3>Generating Sinch Authentication ticket failed.</h3>")
    		error.responseJSON = error.responseJSON || {};
    		error.errorCode = error.errorCode || error.responseJSON.errorCode || '0';
    		error.message = error.message || (error.errorCode  + ' ' + (error.responseJSON.message || 'No backend'));
    	}
    	catch(e) {
    		console.error('FAIL', e);
    		error.message = "Server failure";
    	}

    }

})
