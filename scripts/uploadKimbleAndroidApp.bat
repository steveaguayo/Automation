set seleniumProjectPath=C:\MXS\Kimble\Workspaces\Selenium\TestAutomation
set SAUCE_USERNAME=cb_kimble
set SAUCE_ACCESS_KEY=674b1300-f267-4c20-bed9-c97bafbe1d29
set zipName=KimbleExpensesAndroidApp.zip

c:
cd %seleniumProjectPath%\lib

curl -u %SAUCE_USERNAME%:%SAUCE_ACCESS_KEY% -X POST "http://saucelabs.com/rest/v1/storage/%SAUCE_USERNAME%/%zipName%?overwrite=true" -H "Content-Type: application/octet-stream" --data-binary @%zipName%

pause