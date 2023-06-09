Feature: Login

  @LoginSuccessFul
  Scenario Outline: Iniciar sesion Mision Match
    Given que "Mauro" esta en el Login Page de Mision Match
    When ingresa las credenciales del registro <RowNumber> de Google Sheets con <mobileVerifyActive>
    Then el usuario visualiza el home de Mision Match

    Examples:
      | RowNumber |mobileVerifyActive|
      | 0         |false             |


