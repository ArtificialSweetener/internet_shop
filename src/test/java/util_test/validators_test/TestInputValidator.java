package util_test.validators_test;

import static org.junit.Assert.*;
import org.junit.Test;

import util.validators.InputValidator;

public class TestInputValidator { // done

	private InputValidator inputValidator = InputValidator.getInstance();

	@Test
	public void testIsNameValid() {
		assertTrue(inputValidator.isNameValid("John Doe"));
		assertTrue(inputValidator.isNameValid("Іван Петренко"));
		assertFalse(inputValidator.isNameValid("John123"));
		assertFalse(inputValidator.isNameValid("John_123"));
		assertFalse(inputValidator.isNameValid(" "));
		assertFalse(inputValidator.isNameValid(""));
	}

	@Test
	public void testIsSurnameValid() {
		assertTrue(inputValidator.isSurnameValid("Doe"));
		assertTrue(inputValidator.isSurnameValid("Петренко"));
		assertFalse(inputValidator.isSurnameValid("Doe123"));
		assertFalse(inputValidator.isSurnameValid(" "));
		assertFalse(inputValidator.isSurnameValid(""));
	}

	@Test
	public void testIsEmailValid() {
		assertTrue(inputValidator.isEmailValid("johndoe@example.com"));
		assertTrue(inputValidator.isEmailValid("johndoe+test@example.com"));
		assertFalse(inputValidator.isEmailValid("johndoe@"));
		assertFalse(inputValidator.isEmailValid("johndoeexample.com"));
		assertFalse(inputValidator.isEmailValid("@example.com"));
		assertFalse(inputValidator.isEmailValid(" "));
		assertFalse(inputValidator.isEmailValid(""));
	}

	@Test
	public void testIsPasswordValid() {
		assertTrue(inputValidator.isPasswordValid("Password123"));
		assertTrue(inputValidator.isPasswordValid("Pa$$word123"));
		assertTrue(inputValidator.isPasswordValid("Password12345"));

		assertFalse(inputValidator.isPasswordValid("Password"));
		assertFalse(inputValidator.isPasswordValid("Pass1"));
		assertFalse(inputValidator.isPasswordValid("Pass 1"));
		assertFalse(inputValidator.isPasswordValid("PASSWORD"));
		assertFalse(inputValidator.isPasswordValid("password"));
		assertFalse(inputValidator.isPasswordValid("password123"));
		assertFalse(inputValidator.isPasswordValid("PASSWORD123"));
		assertFalse(inputValidator.isPasswordValid("123456789"));
		assertFalse(inputValidator.isPasswordValid("pass"));
		assertFalse(inputValidator.isPasswordValid(" "));
		assertFalse(inputValidator.isPasswordValid(""));
	}

	@Test
	public void testIsNumberValid() {
		assertTrue(inputValidator.isNumberValid("123"));
		assertTrue(inputValidator.isNumberValid("0"));

		assertFalse(inputValidator.isNumberValid("123.45"));
		assertFalse(inputValidator.isNumberValid("-123"));
		assertFalse(inputValidator.isNumberValid(" "));
		assertFalse(inputValidator.isNumberValid(""));
	}

	@Test
	public void testCountryCodeValid() {
		assertTrue(inputValidator.isCountryCodeValid("1"));
		assertTrue(inputValidator.isCountryCodeValid("12"));
		assertTrue(inputValidator.isCountryCodeValid("123"));

		assertFalse(inputValidator.isCountryCodeValid("1234"));
		assertFalse(inputValidator.isCountryCodeValid("1a"));
		assertFalse(inputValidator.isCountryCodeValid("12 3"));
		assertFalse(inputValidator.isCountryCodeValid(" "));
		assertFalse(inputValidator.isCountryCodeValid(""));
	}

	@Test
	public void testMobilePhoneValid() {
		assertTrue(inputValidator.isMobilePhoneValid("1234567890"));
		assertTrue(inputValidator.isMobilePhoneValid("12345678901"));

		assertFalse(inputValidator.isMobilePhoneValid(""));
		assertFalse(inputValidator.isMobilePhoneValid("   "));
		assertFalse(inputValidator.isMobilePhoneValid("123456789"));
		assertFalse(inputValidator.isMobilePhoneValid("abcdefghij"));
		assertFalse(inputValidator.isMobilePhoneValid("123 4567890"));
	}

	@Test
	public void testIsAddressValid() {
		assertTrue(inputValidator.isAddressValid("123 Main, St."));
		assertFalse(inputValidator.isAddressValid(" "));
	}

	@Test
	public void testIsTitleValid() {
		assertTrue(inputValidator.isTitleValid("Product Title"));
		assertFalse(inputValidator.isTitleValid(" "));
	}

	@Test
	public void testIsDescriptionValid() {
		assertTrue(inputValidator.isDescriptionValid("Product, 123, Description."));
		assertFalse(inputValidator.isDescriptionValid(" "));
	}

	@Test
	public void testIsPriceValid() {
		assertTrue(inputValidator.isPriceValid("9.99"));
		assertTrue(inputValidator.isPriceValid("9"));
		assertTrue(inputValidator.isPriceValid("0.99"));

		assertFalse(inputValidator.isPriceValid("9,99"));
		assertFalse(inputValidator.isPriceValid(".99"));
		assertFalse(inputValidator.isPriceValid("9.99."));
		assertFalse(inputValidator.isPriceValid("9..99"));
		assertFalse(inputValidator.isPriceValid(".9.99"));

	}

}