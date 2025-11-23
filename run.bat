@echo off
REM
REM

echo Irish Rail API - Starting

java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo install Java 25 LTS or higher from:
    echo https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

REM
if not exist "bin" (
    echo ERROR: Compiled classes not found!
    echo run compile.bat first to compile the project
    pause
    exit /b 1
)

REM
if not exist "bin\Main.class" (
    echo ERROR: Main.class not found!
    echo run compile.bat first to compile the project
    pause
    exit /b 1
)

REM
java -cp bin Main

echo Application Closed
pause
