var require = patchRequire(require);

exports.init = function(options){


  const INVOICE_PROPS = 'invoiceProps';
  const BILL_DATE = 'bill-date';
  const DUE_DATE = 'due-date';
  const AMOUNT_DUE = 'amount-due';
  const INVOICE_NUMBER = 'invoice-number';
  const ACCOUNT_NUMBER = 'account';
  const CUSTOMER_NAME = "customer-name";
  const INVOICE_NUMBER = "invoice-number";


  const ACCOUNT_PROPS = 'accountProps';
  const IDENTIFIER = 'identifier';
  const USERNAME = 'username';
  const PASSWORD = 'password';
  const MIMETYPE = 'mimeType';
  const FILE_EXTENSION = 'fileSuffix';

  const casper = options.casper

  const BILL_ID = 'bill-id';
  const IMAGE_URL = 'image-url';

  const data = {
    'accountProps':{},
    'invoiceProps':{}
  }

  for (var property in options.properties.props) {
    if (options.properties.props.hasOwnProperty(property)) {
        getProps()[property] = options.properties.props[property]
    }
  }

  this.setBillId = function(id){
    getData()[BILL_ID] = id;
  }

  this.getBillId = function(){
    return getData()[BILL_ID];
  }

  this.setImageUrl = function(url){
    getData()[IMAGE_URL] = url;
  }

  this.getImageUrl = function(){
    return getData()[IMAGE_URL];
  }

  this.setBillDate = function(date){
    this.setInvoiceProp(BILL_DATE, date);
  }

  this.getBillDate = function(){
    return this.getInvoiceProp(BILL_DATE);
  }

  this.setDueDate = function(date){
    this.setInvoiceProp(DUE_DATE, date);
  }

  this.getDueDate = function(){
    return this.getInvoiceProp(DUE_DATE);
  }

  this.setInvoiceNumber = function(invoiceNumber){
    this.setInvoiceProp(INVOICE_NUMBER, invoiceNumber);
  }

  this.getInvoiceNumber = function(){
    return this.getInvoiceProp(INVOICE_NUMBER);
  }

  this.setAmountDue = function(amountDue){
    this.setInvoiceProp(AMOUNT_DUE, amountDue);
  }

  this.getAmountDue = function(){
    return this.getInvoiceProp(AMOUNT_DUE);
  }

  this.setCustomerName= function(customerName){
    this.setInvoiceProp(CUSTOMER_NAME, customerName);
  }

  this.getCustomerName = function(){
    return this.getInvoiceProp(CUSTOMER_NAME);
  }

  this.setAccountNumber = function(accountNumber){
    this.setInvoiceProp(ACCOUNT_NUMBER, accountNumber);
  }

  this.getAccountNumber = function(){
    return this.getInvoiceProp(ACCOUNT_NUMBER);
  }


  this.setInvoiceProp = function(prop, value){
    this.getInvoiceProps()[prop] = sanitize(value);
  }

  this.getInvoiceProp = function(prop){
    return this.getInvoiceProps()[prop];
  }

  this.getInvoiceProps = function(){
    return getData()[INVOICE_PROPS];
  }

  function setAccountProp(prop, value){
    this.getAccountProps()[prop] = value
  }

  this.getAccountProp = function (prop){
    return this.getAccountProps()[prop]
  }

  this.getAccountProps = function(){
    return getProps();
  }

  function getProps(){
    return getData()[ACCOUNT_PROPS];
  }

  function getData(){
    return data;
  }

  this.getIdentifier = function(){
    return this.getAccountProps()[IDENTIFIER]
  }

  this.getUsername = function(){
    return this.getAccountProps()[USERNAME]
  }

  this.getPassword = function(){
    return this.getAccountProps()[PASSWORD]
  }

  this.getMimeType = function(){
    return this.getAccountProps()[MIMETYPE]
  }

  this.getFileExtension = function(){
    return this.getAccountProps()[FILE_EXTENSION]
  }

  this.getUploadOptions = function(){
    var options = {
      'url':this.getImageUrl(),
			'onlineFileId':this.getBillId(),
			'mimeType':this.getMimeType(),
			'fileExtension':this.getFileExtension(),
      'method':'GET',
      'data':{}
    }

    return options;
  }

  function sanitize(value){
    return casper.evaluate(function(val){
      if (typeof val === 'string'){
    		var str =  $('<div/>').text(val).html().replace(/&nbsp;/gi,'');
    		str = str.replace(/\s\s/gi, "");
    		str = str.trim();
    		return str;
    	}
    	return val;
    }, value)
  }

  this.validate = function(){
    var valid = true;

    for (var property in this.getInvoiceProps()) {
       //console.log("prop: " + property)
      if (this.getInvoiceProps().hasOwnProperty(property)) {
          //console.log(' value: ' + this.getInvoiceProp(property))
          //console.log(' property == null: ' + (property == null))
          //console.log(' this.getInvoiceProp(property) == undefined: ' + (this.getInvoiceProp(property) == 'undefined'))
          //console.log(' this.getInvoiceProp(property).length <= 1: ' + (this.getInvoiceProp(property).length < 1))
          if(property == null || typeof this.getInvoiceProp(property) == 'undefined' || this.getInvoiceProp(property).length < 1){
            valid = false;
          }
      }
    }
    return valid;
  }

  this.notValid = function(){
    for (var property in this.getInvoiceProps()) {
      if (this.getInvoiceProps().hasOwnProperty(property)) {
          if(property == null || typeof this.getInvoiceProp(property) == 'undefined' || this.getInvoiceProp(property).length < 1){
            return "Property [" + property + "] is missing.";
          }
      }
    }
  }

  this.hasBillId = function(){
    return isNull = this.getBillId() !== null && this.getBillId() > 0
  }

  return this;

}
