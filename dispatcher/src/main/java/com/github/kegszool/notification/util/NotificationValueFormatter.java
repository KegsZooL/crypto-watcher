package com.github.kegszool.notification.util;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NotificationValueFormatter {

    public static String formatPercentage(double percentage) {
        DecimalFormat df;
        if (percentage >= 1) {
            df = new DecimalFormat("0.0");
        } else if (percentage >= 0.01) {
            df = new DecimalFormat("0.00");
        } else {
            df = new DecimalFormat("0.####");
        }
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        return df.format(percentage);
    }

    public static String formatPrice(double price) {
        DecimalFormat df;
        if (price >= 100) {
            df = new DecimalFormat("0.00");
        } else if (price >= 1) {
            df = new DecimalFormat("0.000");
        } else if (price >= 0.001) {
            df = new DecimalFormat("0.0000");
        } else if (price >= 0.00001) {
            df = new DecimalFormat("0.000000");
        } else {
            df = new DecimalFormat("0.################");
        }
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        return df.format(price);
    }
}