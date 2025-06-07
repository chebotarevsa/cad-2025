# Сборка приложения

## Создание приложения

mvn archetype:generate -DgroupId=ru.bsuedu.cad -DartifactId=demo -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false

## Сборка

mvn package

## Выполнение

mvn compile exec:java -Dexec.mainClass="ru.bsuedu.cad.App"
