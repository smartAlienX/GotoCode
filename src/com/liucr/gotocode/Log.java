package com.liucr.gotocode;

import org.apache.commons.logging.impl.SimpleLog;

public class Log extends SimpleLog {

    private static Log log = new Log();

    private Log() {
        super("GoToCode");
    }

    public static void d(Object msg) {
        log.log(2, msg, null);
    }

}
