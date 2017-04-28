var require = patchRequire(require);

exports.setupCasper = function(options){

  var casperOptions = {
    verbose: options.verbose == null ? false : options.verbose,
    logLevel: options.logLevel == null ? "error" : options.logLevel,
     clientScripts:  [
       'c:/bin/casperjs/lib/jquery-1.7.1.min.js' ,
       'c:/bin/casperjs/lib/goget.js'],
      pageSettings: {
        webSecurityEnabled: false,
        userAgent: 'Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36',
        loadImages:  true
      },
      viewportSize: options.viewport == null ? {width: 1024, height: 768} : options.viewport
  }

  var casper = require('casper').create(casperOptions);

  var TEMP_DIR = options.tempDir == null ? "//192.168.111.214/company/Data Entry/goget/_temp/" : options.tempDir;
  this.SITE_ROOT = options.siteRoot;
  this.TEMP_DIR = TEMP_DIR;

  casper.on("remote.message", function(msg){
      this.echo("remote.msg: " + msg);
  });

  casper.on('step.error', function(err) {
  	this.echo("ERROR --- " + err);
      this.die("Step has failed: " + err);
  	this.evaluate( function(key1, key2, err){
  		log(key1, key2, 'error, js-api', 'error reported during casperjs execution: ' + err);
  	}, key1, key2, err);

  });

  casper.on('error', function(msg,backtrace) {
    this.echo("");
   this.echo("=========================");
   this.echo("ERROR:");
   this.echo("MSG: " + msg);
   this.echo("TRACE: " + JSON.stringify(backtrace));
   this.echo("=========================");
   this.echo("");
  });

  casper.on("page.error", function(msg, backtrace) {
    this.echo("");
   this.echo("=========================");
   this.echo("PAGE.ERROR:");
   this.echo("MSG: " + msg);
   this.echo("TRACE: " + JSON.stringify(backtrace));
   this.echo("=========================");
   this.echo("");
  });

  this.casper = casper;

  return this;

};
