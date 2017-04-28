var ROOT_URL = "http://epc02:8080/goget";
var SAME_ORIGIN_POLICY = false;

/*
Set to true if he server is using the same-orgin policy.  When needed a '?callback=?' will be used in ajax calls
*/
function useSameOriginPolicy(val){
	SAME_ORIGIN_POLICY = val;
}

function log(key1, key2, tags, msg){

	$.ajax({
		url: ROOT_URL + "/api/account/log",
		async: false,
		data: { pub:key1, pri:key2, tags:tags, msg:msg },
		success: function( data ) {
			console.log("logged:" + msg);
		},
		error: function(xhr, status, error) {
			console.log("******goget.js:log:ERROR: " + error + " resonseText= " + xhr.responseText);
		},
		dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
	});
}


function creds(key1, key2){

	var creds;

	$.ajax({
		url: ROOT_URL + "/api/account/creds",
		async: false,
		cache: false,
		data: { pub:key1, pri:key2 },
		success: function( response ) {

			if(response.status == "okay"){
				creds = response.creds;
				log(key1, key2, "info, js-api, creds", "Successfully found credentials.");
			}else{
				log(key1, key2, "error,  js-api, creds", "Failed to find the correct credentials.");
			}

		},
		dataType:"json"
	});

	return creds;
};

function billExists(key1, key2, props){
	var exists = false;
	var strProps = JSON.stringify(props) ;
	$.ajax({
		url: ROOT_URL + "/api/account/billExists",
		async: false,
		cache: false,
		data: { pub:key1, pri:key2, props:strProps },
		success: function( response ) {
			exists = response.exists

			if(exists){
				log(key1, key2, "info, js-api, billExists", "Bill already exists for props: [" + strProps + "]");
			}else{
				log(key1, key2, "info, js-api, billExists", "Bill does not exists for props: [" + strProps + "]");
			}
		},
		error: function(xhr, status, error) {
			log(key1, key2, "error, js-api, billExists", "Failed @ billExists(key1, key2, props).  Caused by [error:" + error + ", resonseText: " + xhr.responseText + "].");
			console.log("******goget.js:billExists:ERROR: " + error + " resonseText= " + xhr.responseText);
		},
		dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
	});

	return exists;

}

function createBill(key1, key2, props){
	var billId = null;

	$.ajax({
		url: ROOT_URL + "/api/account/createBill",
		async: false,
		cache: false,
		data: { pub:key1, pri:key2, props:JSON.stringify(props) },
		success: function( response ) {

			if(response.status == "okay"){
				billId = response.billId;
				log(key1, key2, "info,  js-api, createBill", "Successfully created bill with [id:" + response.billId + "]");
			}else{
				billId	= null;
				log(key1, key2, "error, js-api, createBill", "Failed to create a bill in database.  Caused by:" + response.errors);
			}
		},
		error: function(xhr, status, error) {
			log(key1, key2, "error, js-api, createBill", "Failed @ createBill(key1, key2, props).  Caused by [error:" + error + ", resonseText: " + xhr.responseText + "].");
			console.log("******goget.js:createBill:ERROR: " + error + " resonseText= " + xhr.responseText);
		},
		dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
	});

	return billId;

}

function uploadWithUrl(key1, key2, billId, url){
	log(key1, key2, "info, js-api, uploadWithUrl", "billId:[" + billId + "], url:[" + url + "]");

	$.ajax({
		url: ROOT_URL + "/api/account/uploadWithUrl",
		cache: false,
		async: false,
		data: {'pub':key1, 'pri':key2, 'billId':billId, 'url':url},
		success: function( response ) {
			if(response.status == "okay"){
				log(key1, key2, "info, js-api, uploadWithUrl, available", "Successfully uploaded url data to billId [" + billId + "].");
				return true;
			}else{
				log(key1, key2, "error, js-api, uploadWithUrl", "Failed to create a bill in database.  Caused by:" + response.error);
				return false;
			}
		},
		error: function(xhr, status, error) {
			log(key1, key2, "error, js-api, uploadWithUrl", "Failed @ uploadWithUrl(key1, key2, billId, url).  Caused by [error:" + error + ", resonseText: " + xhr.responseText + "].");
			console.log("******goget.js:uploadWithUrl:ERROR: " + error + " resonseText= " + xhr.responseText);
		},
		dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
	});
}


/* attempts to clean up a 'string' object */
function sanitize(obj){
	if (typeof obj === 'string'){
		var str =  $('<div/>').text(obj).html().replace(/&nbsp;/gi,'');
		str = str.replace(/\s\s/gi, "");
		str = str.trim();
		return str
	}
	return obj;
}

/* attempts to clean up all 'string' objects in map */
function sanitizeMap(context){
	$.each( context, function(index,value){
		context[index] = sanitize(value);
	})
	return context;
}

/* performs a console.log on the provided map */
function printMap(context){
	var i = 0;
	$.each( context, function(index,value){
		console.log("******goget.js:printMap["+(i++)+"] -> key:["+index+"], value:["+value+"]");
	})
}



function finish(key1, key2){
	log(key1, key2, "info, success, js-api", "The bot has finished.");
}

function debug(status, msg){
	if(status){
		console.log("******goget.js DEBUG: " + msg);
	}
}

function println(msg){
	console.log("******goget.js PRINTLN: " + msg);
}

/*
*Note: This is not a secure random guid
*/
function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
    s4() + '-' + s4() + s4() + s4();
}

function submitFormWithName(name){
	var request = {};
	var formDom = document.forms[name];
	formDom.onsubmit = function() {
			var data = {};
			for(var i = 0; i < formDom.elements.length; i++) {
				 data[formDom.elements[i].name] = formDom.elements[i].value;
			}
			request.action = formDom.action;
			request.data = data;
			return false;
	}

	$('form[name="'+ name + '"]').submit();
	return request;
}

function submitFormWithId(id){
	var request = {};
	var formDom = document.forms.namedItem(id);
	formDom.onsubmit = function() {
			var data = {};
			for(var i = 0; i < formDom.elements.length; i++) {
				 data[formDom.elements[i].name] = formDom.elements[i].value;
			}
			request.action = formDom.action;
			request.data = data;
			return false;
	}

	$('form[name="'+ name + '"]').submit();
	return request;
}
