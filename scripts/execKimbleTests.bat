REM create variable that stores the java bin path. This variable will used in the subsequent statements.
set javaBinPath=C:\Program Files\Java\jre7\bin

REM create variable that stores the project folder path. This variable will used in the subsequent statements.
REM needs to be up to and including the TestAutomation folder, e.g. C:\MXS\Kimble\Workspaces\Selenium\TestAutomation
REM this version automagically works out the path based upon the location of this script file but if this is moved this must be overriden
set javaTestProjectPath=%CD%\..\..\..

REM move to the project folder
c:
cd %javaTestProjectPath%

REM set path to dir that contains javac.exe and java.exe
set path=%javaBinPath%;%javaTestProjectPath%\lib

REM set the classpath, this tells java where to look for the library files, the project bin folder is adde as it will store the .class file after compile
set classpath=%javaTestProjectPath%\bin;%javaTestProjectPath%\lib\jxl.jar;%javaTestProjectPath%\lib\selenium-server-standalone-2.32.0.jar;%javaTestProjectPath%\lib\snakeyaml-1.11.jar;%javaTestProjectPath%\lib\gson-2.2.4.jar;%javaTestProjectPath%\lib\SauceREST.jar;%javaTestProjectPath%\lib\testng-6.8.jar

REM ## Don't need this bit as the build is done by eclipse ##
REM compile the dataProviderExample.java file, the -d parameter tells javac where to put the .class file that is created on compile
REM javac -verbose %javaTestProjectPath%\test\script\dataProviderExample.java -d %javaTestProjectPath%\bin

REM execute testng framework by giving the path of the testng.xml file as a parameter. The xml file tells testng what test to run
java org.testng.TestNG %javaTestProjectPath%\src\test\suiteconfig\%1 > %1.log

REM clean up the chromedrivers after execution is complete
C:\Windows\System32\taskkill /F /IM chromedriver.exe

pause