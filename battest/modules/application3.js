const require = patchRequire(require);
const fs = require('fs');
const params = require('params');

exports.setupCasper = function(options){

  // const BOT_START         = "botStart";
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

  const LOCAL_DIR         = "./temp/";
  const LOCAL_IMAGE_DIR   = LOCAL_DIR + "images/";
  // const TEMP_ROOT_DIR = "//epc02/company/Data Entry/goget/_temp/"

  // const TEMP_DEFAULT_DIR = "default/"

  // var tempRootDir = options.tempRoot == null ? TEMP_ROOT_DIR : options.tempRoot;
  // var tempDir = options.tempDir == null ? TEMP_DEFAULT_DIR : options.tempDir;

  // const TEMP_DIR =  tempRootDir + tempDir;
  const SITE_ROOT = options.siteRoot;
  var key1, key2;
 

  var casperOptions = {
    verbose: options.verbose == null ? false : options.verbose,
    logLevel: options.logLevel == null ? "error" : options.logLevel,
     clientScripts:  [
       'c:/bin/casperjs/lib/jquery-1.7.1.min.js' ,
       'c:/bin/casperjs/lib/goget3.js',
      //  'c:/bin/casperjs/lib/jquery.blockUI.js',
       'c:/bin/casperjs/lib/jspdf.min.js',
      //  'c:/bin/casperjs/lib/jquery.idletimer.js',
       'c:/bin/casperjs/lib/signalr.js',
       'c:/bin/casperjs/lib/aws-sdk-2.7.13.min.js'],
      pageSettings: {
        webSecurityEnabled: false,
        userAgent: 'Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36',
        loadImages:  true
      },
      viewportSize: options.viewport == null ? {width: 1024, height: 768} : options.viewport,
      exitOnError: options.exitOnError == null ? true : options.exitOnError
  }

  this.imageDir = LOCAL_IMAGE_DIR;

  this.ERRORS = {
    loginNotFound: "Login not found. ",
    loginFailed: "Failed to log in. ",
    securityAnswerNotFound: "Security answer not found. ",
    accountNotFound: "Account not found. ",
    searchNotFound: "Search not found. ",
    billPropsNotFound: "Bill properties not found. ",
    failedToUploadImage: "Failed to upload image. ",
    requiredPropsNotFound: "Required properties not found. ",
    imageLinkNotFound: "Image link not found. ",
    accountPropertiesNotFound: "Account properties not found. ",
    noS3Creds:"S3 creds not found. ",
    billIdNotFound:"Bill id not found. "
  }

  var casper = require('casper').create(casperOptions);

  casper.selectOptionByValue = function(selector, valueToMatch){
    this.evaluate(function(selector, valueToMatch){
        var select = document.querySelector(selector),
            found = false;
        Array.prototype.forEach.call(select.children, function(opt, i){
            if (!found && opt.value.indexOf(valueToMatch) !== -1) {
                select.selectedIndex = i;
                found = true;
            }
        });
        // dispatch change event in case there is some kind of validation
        var evt = document.createEvent("UIEvents"); // or "HTMLEvents"
        evt.initUIEvent("change", true, true);
        select.dispatchEvent(evt);
    }, selector, valueToMatch);
  };

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
    return casper;
  }

  this.keyOne = function(){
    if(key1 == null){
        key1 = casper.cli.get(0)
    }
    return key1;
  }

  this.keyTwo = function(){
    if(key2 == null){
      key2 = casper.cli.get(1)
    }
    return key2;
  }

  this.xPath = function(){
    return require('casper').selectXPath;
  }

  this.getProperties = function(){
    var props = casper.evaluate(function(key1, key2, status){
      var response = properties(key1, key2);

      if(response.value != null && response.value != "" && typeof response.value != 'undefined'){
        logStatus(key1, key2, status);
        return response.value
      }else{
        logError(key1, key2, response.message)
        return null;
      }
    },this.keyOne(), this.keyTwo(), FOUND_PROPERTIES);

    if(props.length == 0){
      this.logError(this.ERRORS.accountPropertiesNotFound);
    }

    return {'props':props}

  }

  this.afterLoginSuccess = function(){
    casper.evaluate(function(key1, key2, status){
      logStatus(key1, key2, status);
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
    casper.exit();
    casper.bypass(999);
  }

  this.logError = function(params){
    var additionalMsg;
    //don't print out user and pass
    switch(params.getError()){
      case this.ERRORS.loginNotFound:
        additionalMsg = "Could not log in to account " + params.getContext().identifier + " using given login credentials. at Url: " + casper.getCurrentUrl();
        break;
      case this.ERRORS.loginFailed:
        additionalMsg = "Failed to log in to account " + params.getContext().identifier + " using given credentials.\nPlease ensure username and password are correct.";
        break;
      case this.ERRORS.securityAnswerNotFound:
        additionalMsg = ""; //print out what the security question was?
        break;
      case this.ERRORS.accountNotFound:
        additionalMsg = "Account: " + params.getContext().identifier + " was not found please ensure the account properties are correct."; 
        break;
      case this.ERRORS.searchNotFound:
        additionalMsg = "Search box was not found at Url: " + casper.getCurrentUrl();
        break;
      case this.ERRORS.billPropsNotFound:
        additionalMsg = "Please ensure the bill properties are available at Url: " + casper.getCurrentUrl();
        break;
      case this.ERRORS.failedToUploadImage:
        additionalMsg = ""; //This one is already pretty well covered.
        break;
      case this.ERRORS.requiredPropsNotFound:
        additionalMsg = params.getMsg();
        break;
      case this.ERRORS.imageLinkNotFound: 
        additionalMsg = "Image Link was not found at URL: " + casper.getCurrentUrl();
        break;
      case this.ERRORS.accountPropertiesNotFound:
        additionalMsg = "This is most likely a database error, please contact the database administrator.";
        break;
      case this.ERRORS.noS3Creds:
        additionalMsg = ""; // not sure yet 
        break;
      case this.ERRORS.billIdNotFound:
        additionalMsg = ""; //this should only happen if bill wasn't created for some reason. Which is handled in other additional messages. 
        break;
    }
    if(additionalMsg != null && additionalMsg.length > 0){
      casper.evaluate(function(key1, key2, msg){
        logError(key1, key2, msg);
      },this.keyOne(), this.keyTwo(), params.getError() + additionalMsg);
    }else{
      casper.evaluate(function(key1, key2, msg){
        logError(key1, key2, msg);
      },this.keyOne(), this.keyTwo(), params.getError());
    }
    casper.exit();
    casper.bypass(999); //hack - exit() does not always exit. bypass(numberOfStepsToSkip)
  }
/*
    this.logError = function(msg){
    casper.evaluate(function(key1, key2, msg){
      logError(key1, key2, msg);
    },this.keyOne(), this.keyTwo(), msg);
    casper.exit();
    casper.bypass(999); //hack - exit() does not always exit. bypass(numberOfStepsToSkip)
  }*/

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
      var existsResponse = billExists(key1, key2, map);
      if(existsResponse.status == RESPONSE_SUCCESS){
        if (existsResponse.value != true){
          var createResponse = createBill(key1, key2, map);
          if(createResponse.value != null){
            logStatus(key1, key2, createStatus)
            return createResponse.value;
          }else{
            logError(key1, key2, "Failed to create bill with props [" + JSON.stringify(map) + "]. Error [" + createResponse.message + "]")
            return null
          }
        }else{
          logStatus(key1, key2, existStatus)
          log(key1, key2, existStatus, "Bill exists for props: [" + JSON.stringify(map) + "]")
          return null
        }
      }else{
        logError(key1, key2, "Failed to create bill with props [" + JSON.stringify(map) + "]. Error [" + existsResponse.message + "]")
        return null;
      }

    },this.keyOne(), this.keyTwo(), map, BILL_EXISTS, BILL_CREATED);

    if(billId == null || billId == ""){
      casper.exit()
      casper.bypass(999); //hack - exit() does not always exit. bypass(numberOfStepsToSkip)
    }

    return billId;
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


  this.s3UploadFromFile = function(imageUrl, onlineFileId, mimeType, fileExtension){
    var input = fs.read(imageUrl, 'b')

    var imageBytes = casper.evaluate(function(input){
      return __utils__.encode(input);
    }, input);
    this.upload(imageBytes, onlineFileId, mimeType,fileExtension)
    fs.remove(imageUrl);
  }

  this.s3UploadFromUrl = function(options){

    if(!options.method || options.method == ""){
      options.method = "GET"
    }

    if(!options.data || options.data == ""){
      options.data = {}
    }

    var imageBytes = casper.evaluate(function(url, mimeType, method, data){
        if(mimeType == "application/pdf;base64"){
            return __utils__.getBase64(url, method, data);
        }
        else{
          return __utils__.getBinary(url, method, data);
        }
    },options.url,options.mimeType,options.method,options.data);

    this.upload(imageBytes, options.onlineFileId, options.mimeType, options.fileExtension);
  }

  this.upload = function(imageBytes, onlineFileId, mimeType, fileExtension){
    var creds = getCreds(this.keyOne(), this.keyTwo());
    var uploaded = false;
    if(creds){
      uploaded = casper.evaluate(function(key1, key2, creds, imageBytes, onlineFileId, successStatus, mimeType,fileExtension){
        if(imageBytes == null || imageBytes.length < 1){
          logError(key1, key2, "ImageBytes is empty");
          return false;
        }else{
          var response = s3Upload(creds, imageBytes, onlineFileId, mimeType, fileExtension);
          if(response.status == RESPONSE_SUCCESS){
            logStatus(key1, key2, successStatus + " [" + onlineFileId + "]")
            return true
          }else{
            logError(key1, key2, response.message)
            return false
          }
        }

      },this.keyOne(), this.keyTwo(),creds, imageBytes, onlineFileId, IMAGE_UPLOADED, mimeType, fileExtension);
    }else{
      this.logError(this.ERRORS.noS3Creds)
    }
    if(uploaded){
      casper.evaluate(function(key1, key2, onlineFileId){
        var response = logUploaded(key1, key2, onlineFileId);
        if(response.status != RESPONSE_SUCCESS){
          logError(key1, key2, response.message)
        }
      }, this.keyOne(), this.keyTwo(), onlineFileId);
    }else{
      this.logError(this.ERRORS.failedToUploadImage)
    }
  }

  var getCreds = function(key1, key2){
    return casper.evaluate(function(key1, key2){
      var response = s3Creds(key1,key2);
      if(response.status == RESPONSE_SUCCESS){
        return response.value;
      }else{
        logError(key1, key2, response.message)
      }
    },key1, key2);
  }

  return this;
};
