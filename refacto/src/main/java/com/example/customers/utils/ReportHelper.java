package com.example.customers.utils;

import com.example.customers.controller.Step;
import com.example.customers.domain.Report;
import com.example.customers.domain.exception.SaveException;

/**
 * Created by user on 10/12/2016.
 */
public class ReportHelper {
    public static void indicateSuccess(Report report, String identifier) {
        report.ok.add(String.format("Successfully save {}", identifier));
    }

    public static void indicateFailure(Report report, String identifier, Step step, SaveException e) {
        report.ko.add(String.format("Failed to save {} at step {}", identifier, step));
    }
}
