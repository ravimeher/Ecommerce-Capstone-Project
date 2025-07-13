package org.example.productcatalogservice;

import org.example.productcatalogservice.practiceinheritancetypes.Calculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    public void Test_AddTwoIntegers_RunsSuccessfully() {
        //Arrange
        Calculator calculator = new Calculator();
        //Act
        int result = calculator.add(10,20);
        //Assert
        assert(30 == result);
    }

    @Test
    public void Test_DivideByZero_ThrowsArithmeticException() {
        //Arrange
        Calculator calculator = new Calculator();
        //Act
        //int result = calculator.divide(10,20);
        //Assert
        assertThrows(ArithmeticException.class,
                ()->calculator.divide(10,0));
    }
}