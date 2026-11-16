@echo off
setlocal
chcp 65001 >nul
cd /d "%~dp0"
if not defined SERVER_PORT set "SERVER_PORT=8084"
if /I "%SPRING_PROFILES_ACTIVE%"=="h2" goto app
docker info >nul 2>&1
if errorlevel 1 (
    echo Откройте Docker Desktop и дождитесь запуска движка.
    pause
    exit /b 1
)
docker compose up -d --wait db
if errorlevel 1 (
    pause
    exit /b 1
)
:app
if not exist "build\libs\raspisa-lab4.jar" (
    call gradlew.bat bootJar --console=plain
    if errorlevel 1 (
        pause
        exit /b 1
    )
)
echo Откройте http://localhost:%SERVER_PORT%/login
echo Для остановки приложения нажмите Ctrl+C
java -jar "build\libs\raspisa-lab4.jar"
if errorlevel 1 pause
