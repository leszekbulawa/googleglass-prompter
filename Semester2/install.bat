@echo off

set SCRIPT="%USERPROFILE%\%RANDOM%-%RANDOM%-%RANDOM%-%RANDOM%.vbs"

echo Set oWS = WScript.CreateObject("WScript.Shell") >> %SCRIPT%
echo sLinkFile = "%USERPROFILE%\Desktop\Server.lnk" >> %SCRIPT%
echo quotes = """" >> %SCRIPT%
echo javaPath1 = oWS.RegRead("HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Java Runtime Environment\1.8\JavaHome") >> %SCRIPT%
echo javaPath2 = "\bin\javaw.exe" >> %SCRIPT%
echo Set oLink = oWS.CreateShortcut(sLinkFile) >> %SCRIPT%
echo oLink.TargetPath = quotes + javaPath1 + javaPath2 + quotes >> %SCRIPT%
echo currPath = "%CD%\" >> %SCRIPT%
echo appName = "ServerApp.jar" >> %SCRIPT%
echo argName = " -jar " >> %SCRIPT%
echo oLink.Arguments = argName + currPath + appName >> %SCRIPT%
echo oLink.Save >> %SCRIPT%

cscript /nologo %SCRIPT%
del %SCRIPT%