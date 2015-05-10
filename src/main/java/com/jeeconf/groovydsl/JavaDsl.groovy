package com.jeeconf.groovydsl

import com.jeeconf.groovydsl.Monitoring;

class JavaDsl {

    public static void main(String[] args) {
        Monitoring.sendStatusPeriodically("380934902436", 30000);
    }
}
