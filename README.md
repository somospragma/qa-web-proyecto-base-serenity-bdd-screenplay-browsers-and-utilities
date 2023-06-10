# proyecto-base-serenity-bdd-screenplay-browsers-and-utilities


## Name
Proyecto de estandares de Automatización y utilidades con serenity bdd y screenplay

## Description
This project aims to establish a standard base for test automation with different technologies such as Serenity Web under a Screenplay Pattern design, which can be used as a guide in different projects.

## Consideraciones
    - Para hacer uso de la funcionalidad de prueba que implementa la utilidad de Base de Datos es importante 
    que se instacie una Base de datos y se configura en el archivo de configuración ubicado en:
        ./src/main/resources/configs/congig.properties


    - La funcionalidad de prueba que implementa la utilidad de manejo de documentos de Google Sheet hace uso
    de un documento de Google Sheet ubicado en el drive de Pragma, las credenciasles del API de Google se encuentran
    en: 
        ./src/main/resources/credenciales.json
    las cuales fueron generadas por un API de google creado bajo una cuenta de Pragma. Si desea ejecutar un documento tambien 
    dentro del drive de Pragma solo cambie el ID del documento y cree un documento con 
        |correo | contraseña | secret de google authenticator |
    
    A su vez, el manejo de codigos de autenticacion de GoogleAuthenticator OTP tambien hace uso del documento de GoogleSheet,
    por lo cual es importante exista el documento con la información de acceso.
    De lo contrario solo use la implementacion dentro del feature Login.feature como un ejemplo


    Dadas las consideraciones anteriores, la implementacion de las utilidades solo se encuentran expuestas a modo 
    de ejemplo y unicamente funcionaran si se hacen las configuraciones pertinentes.


## ✅ Technologies
### This project required:
- [JDK java] version 11
- [Serenity] version 
- [Gradle] version
- [Appium] version

## Project status
<h4 align="center"> 🚧 Proyecto en construcción 🚧 </h4> 

## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

## 📁 Access to project

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://gitlab.com/calidad_de_software/framework-actions-automation.git
git branch -M develop
git push -uf origin develop
```

##  🛠️ Run tests Chrome gradle:
```
gradle clean test -Dcontext=chrome -Dwebdriver.driver=chrome
gradle clean test --info --stacktrace --tests "ruta.nameRunner" -Dcontext=chrome -Dwebdriver.driver=chrome
gradle clean test -Dcucumber.options="--tags @someTag" -Dcontext=chrome -Dwebdriver.driver=chrome
gradle clean test -Dcucumber.options="--tags '@someTag or @someTag'" -Dcontext=chrome -Dwebdriver.driver=chrome
```
### ejemplo
```
gradle clean test --info --stacktrace --tests "co.com.pragma.runners.LoginRunner" -Dcontext=chrome -Dwebdriver.driver=chrome
```

##  🛠️ Run tests Firefox gradle:
```
gradle clean test -Dcontext=firefox -Dwebdriver.driver=firefox
gradle test --tests "co.com.pragma.runners.LoginRunner" -Dcontext=firefox -Dwebdriver.driver=firefox
```
### ejemplo
```
gradle clean test --info --stacktrace --tests "co.com.pragma.runners.LoginRunner" -Dcontext=firefox -Dwebdriver.driver=firefox
```


cucumber.options might be cucumberOptions according to version

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


## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Automatically merge when pipeline succeeds](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing(SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***

## Support
Tell people where they can go to for help. It can be any combination of an issue tracker, a chat room, an email address, etc.

## Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README.

## Authors and acknowledgment

| [<img src="https://gitlab.com/uploads/-/system/user/avatar/13437423/avatar.png?width=400" width=115><br><sub>Mauro L. Ibarra P.</sub>](https://gitlab.com/mauro.ibarrap) <br/> | [<img src="https://secure.gravatar.com/avatar/6058d585f70156b4559f8e32b753252b?s=800&d=identicon" width=115><br><sub>V. Manuel Soto</sub>](https://gitlab.com/victor.soto1) |
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|

## License
Open source project.

