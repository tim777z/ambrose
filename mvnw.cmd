@echo off
:: Maven wrapper script for Ambrose
:: This script ensures reproducible builds by using a controlled Maven invocation

cd /d "%~dp0"

:: Use the system Maven 3.9+ if available, otherwise fall back
if "%~$PATH:mvn%"=="" (
  echo Error: Maven (mvn) is not installed or not in PATH. >&2
  exit /b 1
)

:: Pass all arguments through to mvn
goto :execute

:execute
mvn "%~1"