@echo off
setlocal enabledelayedexpansion
cd /d "%~dp0"
set "SELECTED_JDK="
set "SELECTED_MAJOR=0"
set "JAVAC_CMD="
set "JAVA_CMD="

REM 0. Verifica se JAVA_HOME_OVERRIDE foi definido manualmente pelo usuario
if defined JAVA_HOME_OVERRIDE (
    set "OVR=%JAVA_HOME_OVERRIDE%"
    if "!OVR:~-1!"=="\" set "OVR=!OVR:~0,-1!"
    if "!OVR:~-1!"==" " set "OVR=!OVR:~0,-1!"
    if exist "!OVR!\bin\javac.exe" if exist "!OVR!\bin\java.exe" (
        set "SELECTED_JDK=!OVR!"
        goto :finalize
    ) else (
        echo [AVISO] JAVA_HOME_OVERRIDE definido, mas javac.exe ou java.exe nao foram encontrados em: "!OVR!\bin"
    )
)

goto :start_search

:test_candidate
set "CANDIDATE=%~1"
if not defined CANDIDATE goto :eof
if "!CANDIDATE:~-1!"=="\" set "CANDIDATE=!CANDIDATE:~0,-1!"
if "!CANDIDATE:~-1!"==" " set "CANDIDATE=!CANDIDATE:~0,-1!"

REM Verifica pasta 'current' comum em gerenciadores como Scoop e SDKMAN
if not exist "!CANDIDATE!\bin\javac.exe" (
    if exist "!CANDIDATE!\current\bin\javac.exe" set "CANDIDATE=!CANDIDATE!\current"
)

if exist "!CANDIDATE!\bin\javac.exe" if exist "!CANDIDATE!\bin\java.exe" (
    if not "!CANDIDATE!"=="!SELECTED_JDK!" (
        set "CUR_MAJOR=0"
        for /f "tokens=2 delims= " %%V in ('"!CANDIDATE!\bin\javac.exe" -version 2^>^&1') do (
            set "FULL_VER=%%V"
            for /f "tokens=1,2 delims=." %%A in ("!FULL_VER!") do (
                if "%%A"=="1" (
                    set "CUR_MAJOR=%%B"
                ) else (
                    set "CUR_MAJOR=%%A"
                )
            )
        )

        REM Prioridade: Java 25 e o alvo do projeto. Se encontrado, seleciona na hora.
        REM Caso contrario, mantem a versao mais recente encontrada.
        if !CUR_MAJOR! equ 25 (
            set "SELECTED_MAJOR=!CUR_MAJOR!"
            set "SELECTED_JDK=!CANDIDATE!"
        ) else if !SELECTED_MAJOR! neq 25 (
            if !CUR_MAJOR! gtr !SELECTED_MAJOR! (
                set "SELECTED_MAJOR=!CUR_MAJOR!"
                set "SELECTED_JDK=!CANDIDATE!"
            )
        )
    )
)
goto :eof

:start_search

REM 1. Verifica variaveis de ambiente existentes (JAVA_HOME, JDK_HOME)
if defined JAVA_HOME call :test_candidate "%JAVA_HOME%"
if !SELECTED_MAJOR! equ 25 goto :finalize

if defined JDK_HOME call :test_candidate "%JDK_HOME%"
if !SELECTED_MAJOR! equ 25 goto :finalize

REM 2. Verifica se ha javac no PATH do sistema
for /f "delims=" %%I in ('where javac.exe 2^>nul') do (
    set "BIN_DIR=%%~dpI"
    if "!BIN_DIR:~-1!"=="\" set "BIN_DIR=!BIN_DIR:~0,-1!"
    for %%P in ("!BIN_DIR!\..") do call :test_candidate "%%~fP"
    if !SELECTED_MAJOR! equ 25 goto :finalize
)

REM 3. Busca rapida por Java 25 no diretorio de JDKs do usuario (.jdks do IntelliJ)
for /d %%D in ("%USERPROFILE%\.jdks\*25*") do (
    call :test_candidate "%%D"
    if !SELECTED_MAJOR! equ 25 goto :finalize
)

REM 4. Busca rapida por Java 25 nas pastas de instalacao do sistema
for %%R in (
    "%ProgramFiles%"
    "%ProgramW6432%"
    "%ProgramFiles(x86)%"
    "%LOCALAPPDATA%\Programs"
) do (
    if exist "%%~R" (
        for %%S in (
            "Java"
            "Eclipse Adoptium"
            "Microsoft"
            "Amazon Corretto"
            "BellSoft"
            "Zulu"
            "Azul"
            "Semeru"
            "RedHat"
            "SapMachine"
        ) do (
            if exist "%%~R\%%~S" (
                for /d %%D in ("%%~R\%%~S\*25*") do (
                    call :test_candidate "%%D"
                    if !SELECTED_MAJOR! equ 25 goto :finalize
                )
            )
        )
    )
)

REM 5. Busca rapida por Java 25 em gerenciadores de pacotes (Scoop, Chocolatey, SDKMAN, ASDF)
for %%P in (
    "%USERPROFILE%\scoop\apps"
    "%ProgramData%\chocolatey\lib"
    "%ALLUSERSPROFILE%\chocolatey\lib"
    "%USERPROFILE%\.sdkman\candidates\java"
    "%USERPROFILE%\.asdf\installs\java"
) do (
    if exist "%%~P" (
        for /d %%D in ("%%~P\*25*") do (
            call :test_candidate "%%D"
            if !SELECTED_MAJOR! equ 25 goto :finalize
        )
        for /d %%D in ("%%~P\*jdk*") do (
            for /d %%E in ("%%D\*25*") do (
                call :test_candidate "%%E"
                if !SELECTED_MAJOR! equ 25 goto :finalize
            )
        )
    )
)

REM 6. Busca rapida por Java 25 no Registro do Windows
for %%K in (
    "HKLM\SOFTWARE\JavaSoft\JDK"
    "HKLM\SOFTWARE\Eclipse Adoptium\JDK"
    "HKLM\SOFTWARE\Microsoft\JDK"
    "HKCU\SOFTWARE\JavaSoft\JDK"
    "HKCU\SOFTWARE\Eclipse Adoptium\JDK"
    "HKCU\SOFTWARE\Microsoft\JDK"
    "HKLM\SOFTWARE\WOW6432Node\JavaSoft\JDK"
) do (
    for /f "tokens=2* delims=	 " %%A in ('reg query %%K /s /v JavaHome 2^>nul ^| findstr /i "JavaHome"') do (
        echo %%B | findstr /i "25" >nul && (
            call :test_candidate "%%~B"
            if !SELECTED_MAJOR! equ 25 goto :finalize
        )
    )
)

REM 7. Se Java 25 nao foi encontrado, busca qualquer JDK instalado (selecionando a versao mais recente)
for /d %%D in ("%USERPROFILE%\.jdks\*") do call :test_candidate "%%D"

for %%R in (
    "%ProgramFiles%"
    "%ProgramW6432%"
    "%ProgramFiles(x86)%"
    "%LOCALAPPDATA%\Programs"
) do (
    if exist "%%~R" (
        for %%S in (
            "Java"
            "Eclipse Adoptium"
            "Microsoft"
            "Amazon Corretto"
            "BellSoft"
            "Zulu"
            "Azul"
            "Semeru"
            "RedHat"
            "SapMachine"
        ) do (
            if exist "%%~R\%%~S" (
                for /d %%D in ("%%~R\%%~S\*") do call :test_candidate "%%D"
            )
        )
    )
)

for %%P in (
    "%USERPROFILE%\scoop\apps"
    "%ProgramData%\chocolatey\lib"
    "%ALLUSERSPROFILE%\chocolatey\lib"
    "%USERPROFILE%\.sdkman\candidates\java"
    "%USERPROFILE%\.asdf\installs\java"
) do (
    if exist "%%~P" (
        for /d %%D in ("%%~P\*") do call :test_candidate "%%D"
    )
)

for %%K in (
    "HKLM\SOFTWARE\JavaSoft\JDK"
    "HKLM\SOFTWARE\JavaSoft\Java Development Kit"
    "HKLM\SOFTWARE\Eclipse Adoptium\JDK"
    "HKLM\SOFTWARE\Microsoft\JDK"
    "HKCU\SOFTWARE\JavaSoft\JDK"
    "HKCU\SOFTWARE\JavaSoft\Java Development Kit"
    "HKLM\SOFTWARE\WOW6432Node\JavaSoft\JDK"
) do (
    for /f "tokens=2* delims=	 " %%A in ('reg query %%K /s /v JavaHome 2^>nul ^| findstr /i "JavaHome"') do (
        call :test_candidate "%%~B"
    )
)

:finalize
if defined SELECTED_JDK (
    set "JAVA_HOME=%SELECTED_JDK%"
    set "JAVAC_CMD=%SELECTED_JDK%\bin\javac.exe"
    set "JAVA_CMD=%SELECTED_JDK%\bin\java.exe"
    set "PATH=%SELECTED_JDK%\bin;%PATH%"
) else (
    REM Fallback para executaveis no PATH (caso de shims ou instalacoes nao-padrao)
    for /f "delims=" %%I in ('where javac.exe 2^>nul') do (
        if not defined JAVAC_CMD set "JAVAC_CMD=%%I"
    )
    for /f "delims=" %%I in ('where java.exe 2^>nul') do (
        if not defined JAVA_CMD set "JAVA_CMD=%%I"
    )
)

if not defined JAVAC_CMD (
    echo [ERRO] Nenhum compilador Java ^(javac.exe^) foi encontrado no sistema.
    echo.
    echo O projeto requer um JDK ^(Java Development Kit^) instalado ^(recomendado: JDK 25 ou superior^).
    echo Para resolver, instale o JDK em:
    echo   - Eclipse Temurin: https://adoptium.net/
    echo   - Oracle JDK: https://www.oracle.com/java/technologies/downloads/
    echo.
    echo Se o JDK ja estiver instalado em um diretorio customizado, execute:
    echo   set JAVA_HOME_OVERRIDE=C:\caminho\para\seu\jdk
    echo   run.bat
    echo.
    exit /b 1
)

if not defined JAVA_CMD (
    echo [ERRO] O comando java.exe nao foi encontrado no sistema.
    exit /b 1
)
chcp 65001 >nul
if not exist out mkdir out
if exist out\sources.txt del out\sources.txt
for /r "%~dp0src" %%f in (*.java) do (
    set "caminho=%%f"
    set "caminho=!caminho:\=/!"
    echo "!caminho!">>out\sources.txt
)
"%JAVAC_CMD%" -encoding UTF-8 -d out @out\sources.txt || exit /b 1
"%JAVA_CMD%" -Dstdout.encoding=UTF-8 -cp out br.unifor.trocatroca.Main
