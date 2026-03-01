@REM Maven Wrapper Script for Windows
@echo off
setlocal

set MAVEN_WRAPPER_PROPERTIES=.mvn\wrapper\maven-wrapper.properties
set MAVEN_USER_HOME=%USERPROFILE%\.m2
set MAVEN_WRAPPER_DIR=%MAVEN_USER_HOME%\wrapper

for /f "tokens=2 delims==" %%a in ('findstr distributionUrl %MAVEN_WRAPPER_PROPERTIES%') do set DISTRIBUTION_URL=%%a
for /f "tokens=4 delims=-" %%a in ("%DISTRIBUTION_URL%") do set MAVEN_VERSION=%%a
set MAVEN_HOME=%MAVEN_WRAPPER_DIR%\dists\apache-maven-%MAVEN_VERSION%

if not exist "%MAVEN_HOME%" (
    echo Downloading Maven %MAVEN_VERSION%...
    mkdir "%MAVEN_HOME%"
    powershell -Command "Invoke-WebRequest -Uri '%DISTRIBUTION_URL%' -OutFile '%TEMP%\maven.zip'"
    powershell -Command "Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%MAVEN_HOME%'"
    del "%TEMP%\maven.zip"
)

for /r "%MAVEN_HOME%" %%f in (mvn.cmd) do set MAVEN_BIN=%%f
call "%MAVEN_BIN%" %*
