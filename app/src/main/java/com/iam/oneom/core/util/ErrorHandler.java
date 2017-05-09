package com.iam.oneom.core.util;

import com.iam.oneom.BuildConfig;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class ErrorHandler {

    private static final String BUILD_TYPE = "_____build_type";
    private static final String TIME = "___________time";
    private static final String THREAD = "____thread_name";
    private static final String DEVICE_BRAND = "___device_brand";
    private static final String DEVICE_CPU = "_____device_cpu";
    private static final String DEVICE_TYPE = "____device_type";
    private static final String DEVICE_DISPLAY = "_device_display";
    private static final String DEVICE_MODEL = "___device_model";
    private static final String DEVICE_SDK = "_____device_sdk";
    private static final String DEVICE_RAM = "_____device_ram";
    private static final String DEVICE_OS = "______device_os";

    private static final String STACKTRACE_FORMAT = "stack_trace_%03d";
    private static final String CAUSED_BY = "Caused by: ";
    private static final String AT = "\t\tat ";
    private static final String MB_RAM = "MB RAM";
    private static final String EMPTY = "";

    private int i = 0;

    public void handleError(Thread thread, Throwable exception, boolean log) {
        Map<String, String> data = collectErrorData(thread, exception);
        if (log) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
        com.iam.oneom.core.network.Web.instance.sendError(data);
    }

    private Map<String, String> collectErrorData(Thread thread, Throwable exception) {
        Map<String, String> data = new TreeMap<>();

        data.putAll(getDeviceDataPairs());
        data.putAll(getMainDataMap(thread, exception));
        data.putAll(getCauseStackTraceMap(exception));

        return data;

    }

    private Map<String, String> getMainDataMap(Thread thread, Throwable exception) {
        Map<String, String> data = new TreeMap<>();

        data.put(BUILD_TYPE, BuildConfig.BUILD_TYPE);
        data.put(TIME, new Date(System.currentTimeMillis()).toString());
        data.put(THREAD, thread.getName());

        return data;
    }

    private Map<String, String> getDeviceDataPairs() {
        Map<String, String> data = new TreeMap<>();

        Device device = Device.instance();

        data.put(DEVICE_BRAND, device.BRAND);
        data.put(DEVICE_CPU, device.CPU);
        data.put(DEVICE_TYPE, device.DEVICE_TYPE);
        data.put(DEVICE_DISPLAY, device.DISPLAY_INFO);
        data.put(DEVICE_MODEL, device.MODEL);
        data.put(DEVICE_SDK, device.SDK);
        data.put(DEVICE_RAM, device.RAM_MEGABYTES + MB_RAM);
        data.put(DEVICE_OS, device.OS_VERSION);

        return data;
    }

    private Map<String, String> getCauseStackTraceMap(Throwable e) {
        Map<String, String> data = new TreeMap<>();

        if (e == null) {
            return data;
        }

        data.put(String.format(Locale.ENGLISH, STACKTRACE_FORMAT, i++),
                (i == 1 ? EMPTY : CAUSED_BY) + e.toString());
        data.putAll(getThrowableStackTraceMap(e));
        data.putAll(getCauseStackTraceMap(e.getCause()));

        return data;
    }

    private Map<String, String> getThrowableStackTraceMap(Throwable e) {
        Map<String, String> data = new TreeMap<>();

        if (e == null) {
            return data;
        }

        for (StackTraceElement s : e.getStackTrace()) {
            data.put(String.format(Locale.ENGLISH, STACKTRACE_FORMAT, i++), AT + s.toString());
        }

        return data;
    }
}
