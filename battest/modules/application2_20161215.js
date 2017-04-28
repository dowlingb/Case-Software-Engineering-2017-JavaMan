var require = patchRequire(require);

exports.setupCasper = function(options){

  const BOT_START         = "botStart";
  const BOT_INIT          = "botInitialized";
  const FOUND_PROPERTIES  = "foundProps";
  const LOGGED_IN         = "loggedIn";
  const ACCOUNT_FOUND     = "accountFound";
  const NO_PAYMENT_DUE    = "noPaymentDue";
  const BILL_PROPS_FOUND  = "billPropsFound";
  const BILL_EXISTS       = "billExists";
  const BILL_CREATED      = "billCreated";
  const IMAGE_UPLOADED    = "imageUploaded";
  const SUCCESS           = "success";

  const TEMP_DIR = options.tempDir == null ? "//192.168.111.214/company/Data Entry/goget/_temp/" : options.tempDir;
  const SITE_ROOT = options.siteRoot;
  var key1, key2

  var casperOptions = {
    verbose: options.verbose == null ? false : options.verbose,
    logLevel: options.logLevel == null ? "error" : options.logLevel,
     clientScripts:  [
       'c:/bin/casperjs/lib/jquery-1.7.1.min.js' ,
       'c:/bin/casperjs/lib/goget2.js',
       'c:/bin/casperjs/lib/jspdf.min.js',
       'c:/bin/casperjs/lib/signalr.js'],
      pageSettings: {
        webSecurityEnabled: false,
        userAgent: 'Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36',
        loadImages:  true
      },
      viewportSize: options.viewport == null ? {width: 1024, height: 768} : options.viewport,
      exitOnError: options.exitOnError == null ? true : options.exitOnError
  }

  this.ERRORS = {
    loginNotFound: "Login not found",
    securityAnswerNotFound: "Security answer not found",
    accountNotFound: "Account not found",
    searchNotFound: "Search not found",
    billPropsNotFound: "Bill properties not found",
    failedToUploadImage: "Failed to upload image",
    requiredPropsNotFound: "Required properties not found",
    imageLinkNotFound: "Image link not found",
    accountPropertiesNotFound: "Account properties not found"
  }

  var casper = require('casper').create(casperOptions);

  casper.on("remote.message", function(msg){
      this.echo("remote.msg: " + msg);
  });

  casper.on('step.error', function(err) {
    console.log("****************step.error*********************")
    console.log(JSON.stringify(err))
    console.log("****************ERROR*********************")
    this.evaluate(function(key1, key2, tags, msg){
      log(key1, key2, tags, msg);
    },key1, key2, 'step-error', msg);
    this.exit()
    casper.bypass(999); //hack - exit() does not always exit. bypass(numberOfStepsToSkip)
  });

  casper.on('error', function(msg,backtrace) {
    console.log("****************error*********************")
    console.log(msg)
    console.log(JSON.stringify(backtrace))
    console.log("****************ERROR*********************")
    this.evaluate(function(key1, key2, tags, msg){
      log(key1, key2, tags, msg);
    },key1, key2, 'script-error', msg);
    this.exit()
    casper.bypass(999); //hack - exit() does not always exit. bypass(numberOfStepsToSkip)
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
      var props = creds(key1, key2);
      if(props != null && props != "" && typeof props != 'undefined'){
        logStatus(key1, key2, status);
        return props
      }else{
        return null;
      }
    },this.keyOne(), this.keyTwo(), FOUND_PROPERTIES);
    if(creds.length == 0){
      this.logError(this.ERRORS.accountPropertiesNotFound);
    }
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

  this.noPaymentDue = function(){
    casper.evaluate(function(key1, key2, status){
      logStatus(key1, key2, status);
    },this.keyOne(), this.keyTwo(), NO_PAYMENT_DUE);
    this.exit();
  }

  this.logError = function(msg){
    casper.evaluate(function(key1, key2, msg){
      log(key1, key2, "error", msg);
    },this.keyOne(), this.keyTwo(), msg);
    casper.exit();
    casper.bypass(999); //hack - exit() does not always exit. bypass(numberOfStepsToSkip)
  }

  this.initComplete = function(){
    casper.evaluate(function(key1, key2, status){
      logStatus(key1, key2, status);
      logInit(key1, key2);
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
      casper.bypass(999); //hack - exit() does not always exit. bypass(numberOfStepsToSkip)
    }

    return billId;
  }


  this.uploadWithDir = function(billId, billFilePath){
    this.uploadImage(billId, 'file:///' + billFilePath)
  }

  this.uploadWithUrl = function(billId, url){
    this.uploadImage(billId, url)
  }

  this.uploadImage = function(billId, imagePath){
    var uploaded = casper.evaluate( function(key1, key2, billId, imagePath){
			return uploadWithUrl(key1, key2, billId, imagePath);
		}, this.keyOne(), this.keyTwo(), billId, imagePath);

    if(uploaded){
      casper.evaluate(function(key1, key2, status){
        logStatus(key1, key2, status);
      },this.keyOne(), this.keyTwo(), IMAGE_UPLOADED);
    }else{
      this.logError(this.ERRORS.failedToUploadImage + " with url [" + imagePath + "]")
    }
  }

  this.exit = function(){
    casper.evaluate(function(key1, key2, status){
      logStatus(key1, key2, status);
      log(key1, key2, "success","Completed Successfully")

    },this.keyOne(), this.keyTwo(), SUCCESS);
    casper.exit();
  }

  this.removeTargets = function(){
    casper.evaluate(function () {
  		[].forEach.call(__utils__.findAll('a'), function(link) {
  			link.removeAttribute('target');
  		});
  	});
  }


  return this;

};
