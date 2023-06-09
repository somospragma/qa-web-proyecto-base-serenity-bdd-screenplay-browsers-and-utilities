Feature: Intereceptar Peticion Rest,
  Yo como certificacor deseo intereceptar una peticion rest realizada desde el Front para validarla
  
  Scenario: Intercepcion secilla de peticion
    Given que "Certificador" esta en el Reqbin Page
    When Envia una peticion Rest desde el Front y la intercepta
    Then validad el response de la peticcion
