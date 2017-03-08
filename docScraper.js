var casper = require('casper').create();
var x = require('casper').selectXPath;

casper.start('http://docs.oracle.com/javase/8/docs/api/allclasses-frame.html');

casper.waitForText('All Classes', function(){
	var classes = [];
	var arrayindex = 0;
	var xIndex = 1;
	var jclass = this.fetchText(x('/html/body/div/ul/li[' + xIndex +']/a'));
	while(jclass != null){
		classes.push(jclass);
		xIndex++;
		if(this.exists(x('/html/body/div/ul/li[' + xIndex +']/a'))){
			jclass = this.fetchText(x('/html/body/div/ul/li[' + xIndex +']/a'));
		}
		else{
			jclass = null;
		}
	}
}, function(){
	console.log("Unable to open page");
}, 10000);

casper.run();