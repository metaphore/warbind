del /F /Q "build\libs"
call gradlew prodJar
call launch4jc warbind_launch4j.xml
del "prod_out\warbind.jar"