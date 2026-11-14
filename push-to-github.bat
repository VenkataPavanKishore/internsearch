@echo off
setlocal
echo ========================================================
echo   Pushing InternSearch Project to GitHub
echo   Repository: https://github.com/VenkataPavanKishore/internsearch.git
echo ========================================================
echo.

cd /d "%~dp0"

where git >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Git is not installed or not found in your system PATH.
    echo Please install Git for Windows from: https://git-scm.com/download/win
    echo After installing, reopen this script.
    echo.
    pause
    exit /b 1
)

echo [1/5] Initializing Git repository...
if not exist ".git" (
    git init
)

echo [2/5] Adding remote origin...
git remote remove origin >nul 2>&1
git remote add origin https://github.com/VenkataPavanKishore/internsearch.git

echo [3/5] Staging files...
git add .

echo [4/5] Committing changes...
git commit -m "Initial commit: InternSearch web application and discovery system"

echo [5/5] Setting main branch and pushing to GitHub...
git branch -M main
git push -u origin main

echo.
echo ========================================================
echo   Finished! Check your repository on GitHub:
echo   https://github.com/VenkataPavanKishore/internsearch
echo ========================================================
pause
