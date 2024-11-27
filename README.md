<h1 align="center">
  <br>
  <a href="http://www.amitmerchant.com/electron-markdownify"><img src="https://f.hubspotusercontent20.net/hubfs/2829524/Copia%20de%20LOGOTIPO_original-2.png"></a>
  <br>
  proyecto-base-serenity-bdd-screenplay-browsers-and-utilities
  <br>
</h1>

<h4 align="center">Proyecto base de <a href="https://github.com/karatelabs/karate" target="_blank">Pragma</a>.</h4>


<p align="center">
  <a href="https://www.oracle.com/java/technologies/javase-jdk16-downloads.html">
    <img src="https://img.shields.io/badge/JDK_Java-16-orange.svg" alt="JDK Java 16">
  </a>
  <a href="https://serenity-bdd.info/">
    <img src="https://img.shields.io/badge/Serenity-4-blueviolet.svg" alt="Serenity 4">
  </a>
  <a href="https://gradle.org/releases/">
    <img src="https://img.shields.io/badge/Gradle-latest_version-green.svg" alt="Gradle Last Version">
  </a>
  <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/">
    <img src="https://img.shields.io/badge/SQL-JDBC-blue.svg" alt="SQL JDBC">
  </a>
  <a href="https://developers.google.com/api-client-library">
    <img src="https://img.shields.io/badge/Google_APIs-Services-red.svg" alt="Google APIs">
  </a>
  <a href="https://github.com/romankh3/image-comparison">
    <img src="https://img.shields.io/badge/romankh3:image--comparison-4.4.0-yellow.svg" alt="romankh3:image-comparison:4.4.0">
  </a>
</p>


## Description
Este proyecto ofrece un paquete de utilidades y esta configurado para ser ejecutado desde comandos.

## Utilities
Encontrara utilidades para:
- Manejo de Bases de Datos
- Manejo de constantes y configuraciones
- Manejo de Apis de Google
- Comparación de imágenes
- Creación de reportes PDF

## Consideraciones
    - Para hacer uso de la la utilidad de Base de Datos es importante 
        que se instacie una Base de datos y se configura en el archivo de configuración ubicado en:

            ./src/main/resources/configs/congig.properties

        En las dependencias del proyecto esta agregada la dependencia del driver de MySQL, si no 
        desea realizar mayores ajustes respecto al motor de BD use MySQL. Si desea usar otro motor, 
        adiciones la dependencia del driver al build.gradle y configure este driver como observa 
        se realizo para MySQL en: 
    
            ./src/main/java/utils/data/ConnectionManagerDB.java
        
        Nota: Algunos motores de BD no requieren agregar la dependencia del driver como Oracle o MSserver

    - La funcionalidad de prueba que implementa la utilidad de manejo de documentos de Google Sheet hace uso
        de un documento de Google Sheet ubicado en el drive de Pragma (enlace al final), las credenciasles del API de Google se encuentran
        en: 
            ./src/main/resources/credenciales.json
    
        las cuales fueron generadas por un API de google creado bajo una cuenta de Pragma. Si desea ejecutar un documento tambien 
        dentro del drive de Pragma solo cambie el ID del documento en:
            ./src/main/resources/configs/congig.properties
        
        y cree un documento con las siguientes columnas
    
            |correo | contraseña | secret de google authenticator |
    
    A su vez, el manejo de codigos de autenticacion de GoogleAuthenticator OTP tambien hace uso del documento de GoogleSheet,
    por lo cual es importante exista el documento con la información de acceso. 
    De lo contrario solo use la implementacion dentro del feature Login.feature como un ejemplo
    
    - Para hacer uso de la funcionalidad de creación de reportes PDF es necesario realizar la importación de la dependencia itextpdf
    la cual pueden encontrar en el build.gradle con el comentario de PDF. Sumado a esto se debe crear una tarea la cual ejecute esta
    tarea al finalizar las ejecuciones de los casos de pruebas, la cual se llama ejecutarPDFCreator y también esta el build.gradle

    Dadas las consideraciones anteriores, la implementacion de las utilidades solo se encuentran expuestas a modo 
    de ejemplo y unicamente funcionaran si se hacen las configuraciones pertinentes.

-   [Documento de Google Sheet](https://docs.google.com/spreadsheets/d/1t2q5uJ1-rTwx0_AhS7mHKnaehTnLqGK8RR_I6ExZRHc/edit#gid=0)

## ✅ Technologies
### This project required:
- [JDK java] version 16
- [Serenity] version 4
- [Gradle] version

Nota: 
*   Se requiere Selenium posterior a la version 4.11 para la descarga automatica de algunos drivers de los navegadores
    La version de Serenity implementada (4.0.0) ya incluye Selenium 4.12 lo cual soporta los navegadores a Octubre del 2023
    si el proyecto presenta problemas relacionados a las version del driver descargado de forma automatica y la version de su 
    navegador vale la pena revisar que este trabajando con versiones recientes de Serenity y checkear las versiones de Selenium
    incluidas en dicha version de Serenity
*   Con Selenium Manager incluido en Serenity 4.0.0 ya no se requiere WebDriverManager de Boni Garcia, razon por la cual ya
    serenity no lo incluye dentro de sus dependencias


##  🛠️ Run tests Chrome gradle:
```
gradle clean test -Dcontext=chrome -Dwebdriver.driver=chrome
gradle clean test --info --stacktrace --tests "ruta.nameRunner" -Dcontext=chrome -Dwebdriver.driver=chrome
gradle clean test -Dcucumber.options="--tags @someTag" -Dcontext=chrome -Dwebdriver.driver=chrome
gradle clean test -Dcucumber.options="--tags '@someTag or @someTag'" -Dcontext=chrome -Dwebdriver.driver=chrome
```

Nota:

*   Si ejecuta en la consola de gradle no debe usar comillas simples '...' para encerrar '-Dwebdriver.driver=chrome'
*   Si ejecuta en la consola estándar de la máquina quizás si deba utilizar '...' en las porciones del comando que incluyan puntos
*   Con "./gradlew test ..." ejecuta el gradle compilado del proyecto
*   Con "gradle test ..." ejecuta el gradle de su maquina, el configurado en las variables de entorno de su sistema operativo


### ejemplo
```
./gradlew clean test --info --stacktrace --tests "co.com.pragma.runners.CompareImageRunner" -Dcontext=chrome '-Dwebdriver.driver=chrome'
./gradlew clean test --info --stacktrace --tests "co.com.pragma.runners.LoginRunner" -Dcontext=chrome '-Dwebdriver.driver=chrome'
```


##  🛠️ Run tests Firefox gradle:
```
./gradlew clean test -Dcontext=firefox '-Dwebdriver.driver=firefox'
./gradlew test --tests "co.com.pragma.runners.LoginRunner" '-Dcontext=firefox -Dwebdriver.driver=firefox'
```
### ejemplo
```
./gradlew clean test --info --stacktrace --tests "co.com.pragma.runners.LoginRunner" '-Dcontext=firefox -Dwebdriver.driver=firefox'
```

## **Run tests in different environments:**
```
gradle command... -Denvironment=defaul
gradle command... -Denvironment=dev
gradle command... -Denvironment=qa
gradle command... -Denvironment=prod
```
### Note: 
    - The default environment will be used if no other value is provided
    - Could modify the environment urls in .../test/resources/serenity.conf


## **Run tests in different browser:**
```
gradle command... -Dwebdriver.driver=chrome
gradle command... -Dwebdriver.driver=firefox
gradle command... -Dwebdriver.driver=edge
```
### Note:
    - The chrome browser will be used if no other value is provided
    - Could add browser in ./src/test/java/co/com/pragma/stepdefinitions/SerenityWebHooks.java

## Image Comparison
### About:
This is a library available to perform absolute comparison tests between images. Note that the concept of absolute comparison is based on the evaluation of pixel-by-pixel values between the images involved, which brings limitations to the testing.
### Source
https://github.com/romankh3/image-comparison
### Use of archetype and image comparison classes
To use this implementation in your projects, you need to create the following folders:
1. create this path : /resources/data/screenshot - in this route you will be able to save the screenshot took by utility class called *ScreenshotProvider*.
2. in  /resources/data/ : in this path you will be able to save your image to test the scenarios in *compare_image.feature*
3. You need to create this path : /resources/results - to save the results of image comparison Task 

## Authors and acknowledgment

| [<img src="https://gitlab.com/uploads/-/system/user/avatar/13437423/avatar.png?width=400" width=115><br><sub>Mauro L. Ibarra P.</sub>](https://gitlab.com/mauro.ibarrap) <br/> | [<img src="https://secure.gravatar.com/avatar/6058d585f70156b4559f8e32b753252b?s=800&d=identicon" width=115><br><sub>V. Manuel Soto</sub>](https://gitlab.com/victor.soto1) | [<img src="https://gitlab.com/uploads/-/system/user/avatar/15033064/avatar.png?width=400" width=115><br><sub>Cristian F. Roa C.</sub>](https://gitlab.com/cristian.roa) <br/> |
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|

## License
Open source project.

