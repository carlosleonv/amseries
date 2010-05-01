package com.badbuxtour.util;

import org.apache.wicket.extensions.yui.calendar.DatePicker;

/**
 * @author eeugene
 *
 */
public class WicketUtil {

    public static DatePicker getDatePicker() {
        DatePicker datePicker = new DatePicker() {
            @Override
            protected String getDatePattern() {
                return "dd/MM/yyyy";
            }
        };
        return datePicker;
    }
}