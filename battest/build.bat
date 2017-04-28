set CASPER_PATH=%~dp0
set CASPER_BIN=%CASPER_PATH%bin\
set ARGV=%*
phantomjs %CASPER_BIN%bootstrap.js --casper-path=%CASPER_PATH% --cli docscraper.js
timeout 2 >nul
exit