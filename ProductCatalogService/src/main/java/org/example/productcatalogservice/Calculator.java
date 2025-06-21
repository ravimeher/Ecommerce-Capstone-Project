package org.example.productcatalogservice;

public class Calculator {
    public int add(int a, int b){
        return a+b;
    }
    public int divide(int a,int b){
        try{
            return a/b;
        }
        catch(ArithmeticException ex){
            throw ex;
        }
    }
}
