set JAPP_HOME=.\

set JAPP_MAIN=com.rbcloudtech.hycloud.client.InteractiveClient
set XX_JAR=.\
for %%i in ("%JAPP_HOME%\lib\*.jar") do set XX_JAR="%%i;%XX_JAR%"

@echo XX_JAR=%XX_JAR%
java -classpath %XX_JAR% %JAPP_MAIN%

pause