To generate model and api stubs I used swagger-codegen-maven-plugin and after that switched off it. After that I moved controller and service from generated sources to src because of own implementation.

To excecute scripts I used GroovyScriptEngine which is declared as Bean to use it in CalculateService. By my convention scripts must be named as risk type + "_SumInsured.groovy" and risk type + "_Premium.groovy". To send script file there is /api/script_upload (fileUploading.mkv). Directory for them are set in application.properties as /GroovyScripts/.

When request comes a Bicycles instance is passed to getResponse(...) method of calculateService which is injected. There for each bicycle PremiumResponseObjects instance is created and filled by adding PremiumResponseRisks objects which are representation of each risk. I have to use switch/case/yield because in the model Bicycle and PremiumResponseObjects classes use different enums. After calculation of each risks in bicycles their premiums are sumed. I have two unit tests in CalculateServiceTest class. One for getPremiumResponseRisks method and one for getResponse method but of course I'm aware that more tests with more parameters should be there.

Calculation of policy premium is on /api/v1/calculate
