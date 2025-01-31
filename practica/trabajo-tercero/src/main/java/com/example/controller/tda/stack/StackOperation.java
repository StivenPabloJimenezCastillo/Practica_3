package com.example.controller.tda.stack;

import com.example.controller.excepcion.ListEmptyException;
import com.example.controller.excepcion.OverFlowException;
import com.example.controller.tda.list.LinkedList;

public class StackOperation <E> extends LinkedList<E>{
    private Integer top;

    public StackOperation(Integer top) {
        this.top = top;
    }
    
    public Boolean verify() {
        return getSize().intValue() <= top.intValue();
    }
    
    public void push(E dato) throws Exception {
        if(verify()) {
            add(dato, 0);
        } else {
            throw new OverFlowException("Pila llena");
        }
    }
    
    public E pop() throws ListEmptyException {
        if(isEmpty()) {
            throw new ListEmptyException("Pila vacia");
        } else {
            return deleteFirst();
        }
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }
    
}
