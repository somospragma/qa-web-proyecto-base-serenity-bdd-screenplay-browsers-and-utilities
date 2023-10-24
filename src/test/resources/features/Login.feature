Feature: Login

   # --> descomente si configura una cuenta de google la cual a la cual tenga acceso y haya configurado segun Readme
#  @LoginSuccessFul-withOtp
#  Scenario Outline: Iniciar sesion Mision Match
#    Given que "Mauro" esta en el Login Page de Tienda Pragma
#    When ingresa las credenciales del registro <RowNumber> de Google Sheets con <mobileVerifyActive>
#    Then el usuario visualiza el home de Mision Match
#
#    #mobileVerifyActive false si la cuenta no tiene numeros de celulares asociados que hagan que sea la primera opcion de verificacion del doble factor
#    Examples:
#      | RowNumber |mobileVerifyActive|
#      | 1         |true             |

  @LoginUnSuccessFul
  Scenario Outline: Iniciar sesion Mision Match
    Given que "Mauro" esta en el Login Page de Tienda Pragma
    When ingresa las credenciales incorrectas del registro <RowNumber> de Google Sheets
    Then no logra avanzar

    #mobileVerifyActive false si la cuenta no tiene numeros de celulares asociados que hagan que sea la primera opcion de verificacion del doble factor
    Examples:
      | RowNumber |
      | 1         |


