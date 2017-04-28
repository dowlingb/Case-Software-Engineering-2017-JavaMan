var require = patchRequire(require);

exports.setupCasper = function(options){

  const BOT_INIT = "botInitialized";
  const FOUND_PROPERTIES = "foundProps";
  const LOGGED_IN = "loggedIn";
  const ACCOUNT_FOUND = "accountFound";
  const BILL_PROPS_FOUND = "billPropsFound";
  const BILL_EXISTS = "billExists";
  const BILL_CREATED = "billCreated";
  const IMAGE_UPLOADED = "imageUploaded";
  const SUCCESS = "success";

  const TEMP_DIR = options.tempDir == null ? "//192.168.111.214/company/Data Entry/goget/_temp/" : options.tempDir;
  const SITE_ROOT = options.siteRoot;
  var key1, key2

  var casperOptions = {
    verbose: options.verbose == null ? false : options.verbose,
    logLevel: options.logLevel == null ? "error" : options.logLevel,
     clientScripts:  [
       'c:/bin/casperjs/lib/jquery-1.7.1.min.js' ,
       'c:/bin/casperjs/lib/goget2.js',
       'c:/bin/casperjs/lib/jspdf.min.js'],
      pageSettings: {
        webSecurityEnabled: false,
        userAgent: 'Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36',
        loadImages:  true
      },
      viewportSize: options.viewport == null ? {width: 1024, height: 768} : options.viewport
  }

  this.ERRORS = {
    loginNotFound: "Login not found",
    accountNotFound: "Account not found",
    searchNotFound: "Search not found",
    billPropsNotFound: "Bill properties not found",
    failedToUploadImage: "Failed to upload image",
    requiredPropsNotFound: "Required properties not found"
  }

  var casper = require('casper').create(casperOptions);

  casper.on("remote.message", function(msg){
      this.echo("remote.msg: " + msg);
  });

  casper.on('step.error', function(err) {
    console.log("****************step.error*********************")
    console.log(JSON.stringify(err))
    console.log("****************ERROR*********************")
    this.exit()
  });

  casper.on('error', function(msg,backtrace) {
    console.log("****************error*********************")
    console.log(msg)
    console.log(JSON.stringify(backtrace))
    console.log("****************ERROR*********************")
    this.exit()
  });

  casper.on("page.error", function(msg, backtrace) {
    console.log("****************page.error*********************")
    console.log(msg)
    console.log(JSON.stringify(backtrace))
    console.log("****************ERROR*********************")
  });

  this.start = function(){
    casper.start(SITE_ROOT)
    return casper
  }

  this.keyOne = function(){
    if(key1 == null){
        key1 = casper.cli.get(0)
    }
    return key1
  }

  this.keyTwo = function(){
    if(key2 == null){
      key2 = casper.cli.get(1)
    }
    return key2
  }

  this.xPath = function(){
    return require('casper').selectXPath;
  }

  this.getProperties = function(){
    var creds = casper.evaluate(function(key1, key2, status){
      logStatus(key1, key2, status);
      return creds(key1, key2);
    },this.keyOne(), this.keyTwo(), FOUND_PROPERTIES);

    return {'props':creds, 'TEMP_DIR':TEMP_DIR}
  }

  this.afterLoginSuccess = function(){
    casper.evaluate(function(key1, key2, status){
      logStatus(key1, key2, status);
      return creds(key1, key2);
    },this.keyOne(), this.keyTwo(), LOGGED_IN);
  }

  this.afterAccountFound = function(){
    casper.evaluate(function(key1, key2, status){
      logStatus(key1, key2, status);
    },this.keyOne(), this.keyTwo(), ACCOUNT_FOUND);
  }

  this.logError = function(tags, msg){
    casper.evaluate(function(key1, key2, tags, msg){
      log(key1, key2, tags, msg);
    },this.keyOne(), this.keyTwo(), tags, msg);
    casper.exit();
  }

  this.initComplete = function(){
    casper.evaluate(function(key1, key2, status){
      logStatus(key1, key2, status);
    },this.keyOne(), this.keyTwo(), BOT_INIT);
  }

  this.sanitizeProps = function(context){
    return casper.evaluate(function(key1, key2, context, status){
      var ctx = sanitizeMap(context);
      logStatus(key1, key2, status);
      return ctx;
    },this.keyOne(), this.keyTwo(), context, BILL_PROPS_FOUND);
  }


  this.createBill = function(map){
    var billId = casper.evaluate(function(key1, key2, map, existStatus, createStatus){
      if (!billExists(key1, key2, map)){
				var billId = createBill(key1, key2, map);
        if(billId != null){
          logStatus(key1, key2, createStatus)
          return billId;
        }else{
          log(key1, key2, "error", "Failed to create bill with props [" + JSON.stringify(map) + "]")
          return null;
        }
			}
      logStatus(key1, key2, existStatus)
      log(key1, key2, "billExists", "Bill exists for props: [" + JSON.stringify(map) + "]")
      return null

    },this.keyOne(), this.keyTwo(), map, BILL_EXISTS, BILL_CREATED);

    if(billId == null || billId == ""){
      casper.exit()
    }

    return billId;
  }


  this.uploadImage = function(billId, billFilePath){

    casper.evaluate( function(key1, key2, billId, billFilePath, status, error){
			var uploaded = uploadWithUrl(key1, key2, billId, 'file:///' + billFilePath);
      if(uploaded){
        logStatus(key1, key2, status);
      }else{
        log(key1, key2, "error", error + " with url [" + billFilePath + "]")
      }
		}, this.keyOne(), this.keyTwo(), billId, billFilePath, IMAGE_UPLOADED, this.ERRORS.failedToUploadImage);
  }

  this.exit = function(){
    casper.evaluate(function(key1, key2, status){
      logStatus(key1, key2, status);
      log(key1, key2, "success","Completed Successfuly")

    },this.keyOne(), this.keyTwo(), SUCCESS);
    casper.exit();
  }

  return this;

};
