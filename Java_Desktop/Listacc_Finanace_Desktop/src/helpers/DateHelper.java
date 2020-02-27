/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author E-book
 */
public class DateHelper {
    public static Date StringToDate(String date){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+1")); // UTC

        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter2.setTimeZone(TimeZone.getTimeZone("GMT+1")); // UTC

        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            //System.err.println("Failed to parse Date due to: " +e);
            try {
                return formatter2.parse(date);
            } catch (ParseException f) {
                //System.err.println("Failed to parse Date due to: " +e);
                return null;
            }
        }
    }
}
