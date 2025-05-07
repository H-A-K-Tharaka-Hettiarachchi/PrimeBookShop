package model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author KSHPRIME
 */
public class Validations {

    public static boolean isEmailValid(String email) {

        return email.matches("^[a-zA-Z0-9_\\!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    }

    public static boolean isPasswordValid(String password) {

        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$");

    }

    public static boolean isDouble(String price) {

        return price.matches("^\\d+(\\.\\d{2})?$");

    }

    public static boolean isInteger(String value) {

        return value.matches("^\\d+$");

    }

    public static boolean isDate(String date) {

        return date.matches("^\\d{4}-\\d{2}-\\d{2}$|^\\\\d{2}/\\d{2}/\\d{4}$");

    }

    public static Year getYear(String date) {

        //create the formatter using simple date format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            //format the date
            Date formatedDate = formatter.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatedDate);
            return Year.parse(String.valueOf(calendar.get(Calendar.YEAR)));
//            return formatedDate;

        } catch (Exception e) {

            // Handle the exception if the date string is invalid
            System.out.println("Invalid date format: " + e.getMessage());
            return null;
        }

    }

    public static boolean isMobileNumberValid(String mobile) {

        return mobile.matches("^07[01245678]{1}[0-9]{7}$");

    }

}
