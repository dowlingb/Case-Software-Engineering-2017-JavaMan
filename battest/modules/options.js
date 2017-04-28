var require = patchRequire(require);

exports.init = function(){
	var ERROR;
	var CONTEXT;
	var MSG;

	this.setError = function(error){
  		this.ERROR = error
	}

	this.getError = function(){
  		return this.ERROR;
	}

	this.setContext = function(context){
		this.CONTEXT = context;
	}

	this.getContext = function(){
  		return this.CONTEXT;
	}

	this.setMsg = function(msg){
  		this.MSG = msg;
	}

	this.getMsg = function(){
  		return this.MSG;
	}

	return this;
}
