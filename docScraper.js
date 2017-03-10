var casper = require('casper').create();
var x = require('casper').selectXPath;

function Method() {
	this.name = "";
	this.description = "";
	this.modAndType = "";
}

function Constructor(){
	this.name = "";
	this.description = "";
}

function Class() {
	this.name = "";
	this.constructors = [];
	this.methods = [];
	this.href = "";
}

var classes = [];
var rootUrl = "http://docs.oracle.com/javase/8/docs/api/";
var i = 0;


casper.start('http://docs.oracle.com/javase/8/docs/api/allclasses-frame.html');

//fills classes list with all class objects

casper.waitForText('All Classes', function(){
	var arrayindex = 0;
	var xIndex = 1;
	var jclass = this.fetchText(x('/html/body/div/ul/li[' + xIndex +']/a'));
	while(jclass != null){
		var classObj = new Class();
		classObj.name = jclass;
		classObj.href = this.fetchText(x('/html/body/div/ul/li[' + xIndex +']/a/@href'));
		classes.push(classObj);
		xIndex++;
		if(this.exists(x('/html/body/div/ul/li[' + xIndex +']/a'))){
			jclass = this.fetchText(x('/html/body/div/ul/li[' + xIndex +']/a'));
		}
		else{
			jclass = null;
		}
		if(classes.length%500 == 0){
			console.log("Found " + classes.length + " Java classes so far...");
		}
	}
	console.log("Found a total of " + classes.length + " Java classes.");
}, function(){
	console.log("Unable to open page");
}, 10000);

casper.then(function(){
	casper.repeat(classes.length, function(){
		this.thenOpen(rootUrl + classes[i].href, function(){
			var constIndex = 2;
			var constructor = this.fetchText(x('/html/body/div[4]/div[2]/ul/li/ul[2]/li/table/tbody/tr[' + constIndex + ']/td/code')).replace(/\u00a0/g, " ");
			var description = this.fetchText(x('/html/body/div[4]/div[2]/ul/li/ul[2]/li/table/tbody/tr[' + constIndex + ']/td/div')).replace(/\u00a0/g, " ");
			//console.log(classes[i].name + ": " + constructor + ": " + description);
			while(constructor != null){
				constObj = new Constructor();
				constObj.name = constructor;
				constObj.description = description;
				classes[i].constructors.push(constObj);
				constIndex++;
				if(this.exists(x('/html/body/div[4]/div[2]/ul/li/ul[2]/li/table/tbody/tr[' + constIndex + ']/td/code'))){
					constructor = this.fetchText(x('/html/body/div[4]/div[2]/ul/li/ul[2]/li/table/tbody/tr[' + constIndex + ']/td/code')).replace(/\u00a0/g, " ");
					description = this.fetchText(x('/html/body/div[4]/div[2]/ul/li/ul[2]/li/table/tbody/tr[' + constIndex + ']/td/div')).replace(/\u00a0/g, " ");
					//console.log(constructor + ": " + description);
				}
				else{
					constructor = null;
				}

			}
			var methIndex = 0;
			var method = this.fetchText(x('//*[@id="i'+ methIndex +'"]/td[2]/code')).replace(/\u00a0/g, " ");
			var mdescription = this.fetchText(x('//*[@id="i'+ methIndex +'"]/td[2]/div')).replace(/\u00a0/g, " ");
			var modAndType = this.fetchText(x('//*[@id="i'+ methIndex +'"]/td[1]')).replace(/\u00a0/g, " ");
			//console.log(modAndType + ": "+ method + ": " + mdescription);
			while(method != null){
				methObj = new Method();
				methObj.name = method;
				methObj.description = mdescription;
				methObj.modAndType = modAndType;
				classes[i].methods.push(methObj);
				methIndex++;
				if(this.exists(x('//*[@id="i'+ methIndex +'"]'))){
					method = this.fetchText(x('//*[@id="i'+ methIndex +'"]/td[2]/code')).replace(/\u00a0/g, " ");
					mdescription = this.fetchText(x('//*[@id="i'+ methIndex +'"]/td[2]/div')).replace(/\u00a0/g, " ");
					modAndType = this.fetchText(x('//*[@id="i'+ methIndex +'"]/td[1]')).replace(/\u00a0/g, " ");
					//console.log(modAndType +": "+ method + ": " + mdescription);
				}
				else{
					method = null;
				}
			}
			if(i%100 == 0){
				console.log("Populated methods for " + i + " classes so far...");
			}
		});
		i++;
	});
	console.log("All methods populated.");
});

casper.run(function(){
	this.echo("Complete.");
	this.exit();
});