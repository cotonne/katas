package com.example.customers.domain;

import java.util.Vector;

public class Report {
    public ReportType entity;
    public Vector<String> ok = new Vector<String>();
    public Vector<String> ko = new Vector<String>();
}
