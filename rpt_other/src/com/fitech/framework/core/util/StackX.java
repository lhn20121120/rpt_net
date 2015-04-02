package com.fitech.framework.core.util;
public class StackX {

    

    private int maxSize;

    

    private char[] stack;

    

    private int top;

    

    public StackX() {

       maxSize = 1000;

       stack = new char[maxSize];

       top = -1;

    }

    

    public void push(char c) {

       top++;

       if (top >= maxSize) 

           System.out.println("StackX is full...");

       else

           stack[top] = c;

    }

    

    public char pop() {

       char c;

       if (top < 0) {

           System.out.println("StackX is empty...");

           c = 0;

       } else

           c = stack[top--];

       return c;

    }

    

    public char peek() {

       char c;

       if (top < 0) {

           System.out.println("StackX is empty...");

           c = 0;

       } else

           c = stack[top];

       return c;

    }

    

    public boolean isEmpty() {

       return top == -1;

    }

    

    public boolean isFull() {

       return top == maxSize-1;

    }

 

}

