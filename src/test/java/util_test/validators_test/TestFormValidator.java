package util_test.validators_test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import util.validators.FormValidator;

public class TestFormValidator {
  @Test
  public void testValidate() {
    FormValidator formValidator = mock(FormValidator.class);
    HttpServletRequest req = mock(HttpServletRequest.class);

    when(formValidator.validate(req)).thenReturn(true);
    assertTrue(formValidator.validate(req));
  }
}
