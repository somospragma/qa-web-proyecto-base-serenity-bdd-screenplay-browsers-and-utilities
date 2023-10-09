Feature: Compare Image

"""
  The possible outcomes of the comparison process are:
     MISMATCH : the images contains differences
     MATCH: Both images are the same ( no differences )
     SIZE_MISMATCH: the images have different resolution
  """

#  @Debug
  Scenario Outline: Successful MisMatch comparison
    Given the user has a captured image
    When he apply image comparison the <actual> image with <expected> image
    Then he should see that the result state is <result>
    Examples:
      | actual                               | expected                               | result     |
      | "src/test/resources/data/actual.png" | "src/test/resources/data/expected.png" | "MISMATCH" |
      | "src/test/resources/data/p1.png"     | "src/test/resources/data/p1_1.png"     | "SIZE_MISMATCH" |


  Scenario Outline: Do comparison by excluded areas and set minimal area to evaluate
    Given the user has a captured image
    When he apply image comparison the <actual> image with <expected> image setting customised options
    Then he should see that the result state is "MATCH"
    Examples:
      | actual                           | expected                           |
      | "src/test/resources/data/p2.png" | "src/test/resources/data/p2_1.png" |


  Scenario Outline: Should properly verify mismatch color in two image
    Given the user has a captured image
    When he apply image comparison the <actual> image with <expected> using pixel tolerance level
    Then he should see that the result state is "MISMATCH"
    Examples:
      | actual                           | expected                           |
      | "src/test/resources/data/p3.png" | "src/test/resources/data/p3_1.png" |

  @Debug
  Scenario Outline: Successful apply image comparison in acceptance test
    Given the user navigate to pragma store
    And he takes a screenshot of main page
    When he apply image custom comparison the <actual> image with <expected> image
    Then he should see that the result state is "MATCH"
    Examples:
      | actual                                           | expected                               |
      | "src/test/resources/data/screenshot/capture.png" | "src/test/resources/data/E2E_test.png" |
