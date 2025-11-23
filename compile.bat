@echo off

echo Irish Rail API | Compilation

java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo install Java 25 LTS or higher from:
    echo https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

echo Checking Java version...
java -version

if not exist "bin" (
    echo Creating bin directory...
    mkdir bin
)

echo Compiling Java files...
javac -d bin src\*.java

if %ERRORLEVEL% equ 0 (
    echo Compilation Successful
    echo run the application, use: run.bat
) else (
    echo Compilation Failed
    echo check the error messages above
    pause
    exit /b 1
)

pause
