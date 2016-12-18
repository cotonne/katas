package com.example.customers.domain.exception;

import com.example.customers.controller.Step;

public class SaveException extends RuntimeException {
    private Step curStep;
    private String s;
    private Exception e;

    public SaveException(Step curStep, String s, Exception e) {

        this.curStep = curStep;
        this.s = s;
        this.e = e;
    }

    public Step getStep() {
        return Step.UNKNOWN;
    }
}
