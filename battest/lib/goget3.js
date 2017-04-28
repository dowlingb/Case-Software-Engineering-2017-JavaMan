const ROOT_URL = "http://epc02:8080/goget";
const EAGRID_URL = "https://edge.energyadvan.com/eagrid"
// const EAGRID_URL = "http://localhost:8090/eagrid"
const GOGET_API = EAGRID_URL + "/api/goget/bot/"
const RESPONSE_SUCCESS = "success";
const RESPONSE_ERROR = "error";
const RESPONSE_API_ERROR = "API ERROR:  ";
// var ROOT_URL = "http://localhost:8085/goget";
var SAME_ORIGIN_POLICY = false;

const DEFAULT_FILE_EXTENSION = ".pdf";
const DEFAULT_MIME_TYPE = "application/pdf";

/*
Set to true if he server is using the same-orgin policy.  When needed a '?callback=?' will be used in ajax calls
*/
function useSameOriginPolicy(val){
	SAME_ORIGIN_POLICY = val;
}

function log(key1, key2, tags, msg){
	var mResponse = getDefaultResponse();
	if(key1 != null && key2 != null){
		$.ajax({
			url: GOGET_API + "log",
			async: false,
			data: { apiKey:key1, pri:key2, tags:tags, msg:msg },
			success: function( data ) {
				mResponse.value = true
			},
			error: function(xhr, status, error) {
				mResponse.status = RESPONSE_ERROR
				mResponse.message = RESPONSE_API_ERROR + error
			},
			dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
		});
	}else{
		mResponse.status = RESPONSE_ERROR
		mResponse.message = RESPONSE_API_ERROR + "keys cannot be null"
	}
	console.log("log: " + msg);
	if(mResponse.status == RESPONSE_SUCCESS){
		return mResponse.value
	}
	console.log(" " + mResponse.message)
	return mResponse;

}

function logError(key1, key2, msg){
	var mResponse = getDefaultResponse();
	$.ajax({
		url: GOGET_API + "logError",
		async: false,
		data: { apiKey:key1, pri:key2, msg:msg },
		success: function( data ) {
			mResponse.value = true
		},
		error: function(xhr, status, error) {
			mResponse.status = RESPONSE_ERROR
			mResponse.message = RESPONSE_API_ERROR + error
		},
		dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
	});
	console.log("logError: " + msg);
	if(mResponse.status == RESPONSE_SUCCESS){
		console.log("	" + RESPONSE_SUCCESS)
		return mResponse.value
	}
	console.log(" " + mResponse.message)
	return mResponse;
}

function logUploaded(key1, key2, onlineFileId){
	var mResponse = getDefaultResponse();
	$.ajax({
		url: GOGET_API + "logUploaded",
		async: false,
		data: { apiKey:key1, pri:key2, onlineFileId:onlineFileId},
		success: function( data ) {
			mResponse.value = true
		},
		error: function(xhr, status, error) {
			mResponse.status = RESPONSE_ERROR
			mResponse.message = RESPONSE_API_ERROR + error
		},
		dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
	});
	console.log("logUploaded");
	if(mResponse.status == RESPONSE_SUCCESS){
		console.log("	" + RESPONSE_SUCCESS)
		return mResponse.value
	}
	console.log(" " + mResponse.message)
	return mResponse;
}

function logStatus(key1, key2, status){
	var mResponse = getDefaultResponse();
	$.ajax({
		url: GOGET_API + "logStatus",
		async: false,
		data: { apiKey:key1, pri:key2, status:status },
		success: function( data ) {
			mResponse.value = true
		},
		error: function(xhr, status, error) {
			mResponse.status = RESPONSE_ERROR
			mResponse.message = RESPONSE_API_ERROR + error
			// console.log("******goget.js:log:ERROR: " + error + " resonseText= " + xhr.responseText);
		},
		dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
	});
	console.log("logStatus: " + status);
	if(mResponse.status == RESPONSE_SUCCESS){
		console.log("	" + RESPONSE_SUCCESS)
		return mResponse.value
	}
	console.log(" " + mResponse.message)
	return mResponse;
}

function logInit(key1, key2){
	var mResponse = getDefaultResponse()
	$.ajax({
		url: GOGET_API + "logInit",
		async: false,
		data: { apiKey:key1, pri:key2},
		success: function( data ) {
			mResponse.value = true
		},
		error: function(xhr, status, error) {
			mResponse.status = RESPONSE_ERROR
			mResponse.message = RESPONSE_API_ERROR + error
		},
		dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
	});
	console.log("logInit");
	if(mResponse.status == RESPONSE_SUCCESS){
		console.log("	" + RESPONSE_SUCCESS)
		return mResponse.value
	}
	console.log(" " + mResponse.message)
	return mResponse;
}

function properties(key1, key2){
	var mResponse = getDefaultResponse();

	$.ajax({
		url: GOGET_API + "properties",
		async: false,
		cache: false,
		data: { apiKey:key1, pri:key2 },
		success: function( data ) {
			if(data.status == "ok"){
				mResponse.value = data.response.properties;
			}else{
				mResponse.status = RESPONSE_ERROR
				mResponse.message = "API: Failed to find the correct credentials. Error[" + data.response + "]"
			}
		},
		error: function(xhr, status, error) {
			mResponse.status = RESPONSE_ERROR;
			mResponse.message = RESPONSE_API_ERROR + error
			//log(key1, key2, "error, js-api, billExists", "Failed @ billExists(key1, key2, props).  Caused by [error:" + error + ", resonseText: " + xhr.responseText + "].");
			//console.log("******goget.js:billExists:ERROR: " + error + " resonseText= " + xhr.responseText);
		},
		dataType:"json"
	});
	return mResponse;
};

function getDefaultResponse(){
	return {"value":null,"status":RESPONSE_SUCCESS,"message":""};
}

function billExists(key1, key2, props){
	var exists = false;
	var mResponse = getDefaultResponse()

	var strProps = JSON.stringify(props) ;
	$.ajax({
		url: GOGET_API + "billExists",
		async: false,
		cache: false,
		data: { apiKey:key1, pri:key2, props:strProps },
		success: function( data ) {
			mResponse.value = data.response.exists
		},
		error: function(xhr, status, error) {
			mResponse.status = RESPONSE_ERROR;
			mResponse.message = RESPONSE_API_ERROR + error
			//log(key1, key2, "error, js-api, billExists", "Failed @ billExists(key1, key2, props).  Caused by [error:" + error + ", resonseText: " + xhr.responseText + "].");
			//console.log("******goget.js:billExists:ERROR: " + error + " resonseText= " + xhr.responseText);
		},
		dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
	});

	return mResponse;

}

function createBill(key1, key2, props){

	var mResponse = getDefaultResponse()
	$.ajax({
		url: GOGET_API + "createBill",
		async: false,
		cache: false,
		data: { apiKey:key1, pri:key2, props:JSON.stringify(props) },
		success: function( data ) {
			if(data.status == "ok"){
				mResponse.value = data.response.onlineFileResourceId;
			}else{
				mResponse.status = RESPONSE_ERROR;
				mResponse.message = "API: " + data.response
			}
		},
		error: function(xhr, status, error) {
			mResponse.status = RESPONSE_ERROR;
			mResponse.message = RESPONSE_API_ERROR + error
		},
		dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
	});

	return mResponse;

}

function s3Creds(key1, key2){
	var mResponse = getDefaultResponse();
	$.ajax({
		url: GOGET_API + "awsCreds",
		cache: false,
		async: false,
		data: {'apiKey':key1, 'pri':key2},
		success: function( data ) {
			if(data.status == "ok"){
				mResponse.value = data.response
			}else{
				mResponse.status = RESPONSE_ERROR;
				mResponse.message = "API: " + data.response
			}
		},
		error: function(xhr, status, error) {
			mResponse.status = RESPONSE_ERROR;
			mResponse.message = RESPONSE_API_ERROR + error
		},
		dataType: (SAME_ORIGIN_POLICY ? "jsonp":"json")
	});

	return mResponse;
}

function s3Upload(creds, imageBytes, onlineFileId, mimeType, fileExtension){
	if(!fileExtension || fileExtension == ""){
		fileExtension = DEFAULT_FILE_EXTENSION;
	}

	if(!mimeType || mimeType == ""){
		mimeType = DEFAULT_MIME_TYPE;
	}

	var creds = new AWS.Credentials(creds.key1, creds.key2)
	var mResponse = getDefaultResponse();
	AWS.config.update({
		region:'us-east-1',
		credentials:creds
	});

	var ofiAsString = onlineFileId + fileExtension;
	var s3 = new AWS.S3({
		apiVersion: '2006-03-01',
		httpOptions:{xhrAsync: false},
		sslEnabled:true
	});

	var params = {
		Bucket:'epc-goget',
		apiVersion:'2006-03-01',
		Key:ofiAsString,
		ACL:'public-read',
		ContentType: mimeType,

		Body: imageBytes
	};

	s3.upload(params, function(err, data) {
	  if(err){
			mResponse.status = RESPONSE_ERROR;
			mResponse.message = err.message
		}else{
			mResponse.value = data
		}
	});
	return mResponse;

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
