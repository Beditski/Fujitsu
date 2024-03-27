package com.fujitsu.deliveryapp.models;

import java.text.DateFormat;

public class FeeRules {
    private String region;
    private String transport;
    private double[] rbf;
    private double[] atef;
    private double[] wsef;
    private double[] wpef;
    private DateFormat datetime;

    public String getRegion() {
        return region;
    }

    public String getTransport() {
        return transport;
    }

    public double[] getRbf() {
        return rbf;
    }

    public double[] getAtef() {
        return atef;
    }

    public double[] getWsef() {
        return wsef;
    }

    public double[] getWpef() {
        return wpef;
    }

    public DateFormat getDatetime() {
        return datetime;
    }

    public FeeRules(String region, String transport, double[] rbf, double[] atef, double[] wsef, double[] wpef, DateFormat datetime) {
        this.region = region;
        this.transport = transport;
        this.rbf = rbf;
        this.atef = atef;
        this.wsef = wsef;
        this.wpef = wpef;
        this.datetime = datetime;

    }
}
