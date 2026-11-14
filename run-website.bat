@echo off
echo ===================================================
echo   Starting InternSearch Web Application...
echo   Open your browser at: http://localhost:8080
echo ===================================================
cd /d "%~dp0mywebsite"
call mvnw.cmd spring-boot:run
pause
