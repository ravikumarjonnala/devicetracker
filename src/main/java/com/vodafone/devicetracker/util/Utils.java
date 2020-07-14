package com.vodafone.devicetracker.util;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Class with utility methods not specific to any class' behaviour.
 */
public class Utils {
    /**
     * An empty string array.
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * Parses a given comma-separated string of values to an array of values.
     * <p>
     * Throws a <code>NullPointerException</code> if the input string is null.
     * 
     * @param csvString comma separated strings
     * @return array of separated values
     */
    public static String[] fromCSV(String csvString) {
        Objects.requireNonNull(csvString, "CSV string cannot be null.");
        csvString = csvString.replace(StringConstants.TAB, StringConstants.SPACE).trim();

        if (csvString.equals(StringConstants.EMPTY_STRING)) {
            return EMPTY_STRING_ARRAY;
        }

        String[] splits = csvString.split(StringConstants.COMMA, -1);
        return Stream.of(splits).map(field -> field.replace(StringConstants.TAB, StringConstants.SPACE).trim())
                .toArray(n -> new String[n]);
    }

    /**
     * Indicates if a given string is either null or empty.
     * 
     * @param str string to test
     * @return true, if null or empty
     */
    public static boolean isNullOrEmptyString(String str) {
        if (str == null) {
            return true;
        }

        return isEmptyString(str);
    }

    /**
     * Indicates if a given string is empty.
     * <p>
     * Throws null pointer exception if the input string is null.
     * 
     * @param str string to test
     * @return true, if empty
     */
    public static boolean isEmptyString(String str) {
        Objects.requireNonNull(str, "String cannot be null");
        return str.replace(StringConstants.TAB, StringConstants.SPACE).trim().length() == 0;
    }

    /**
     * Verifies that only one of the given two objects is null.
     * <p>
     * Neither can both objects be null nor both can be non-null.
     * 
     * @param o1 first object
     * @param o2 second object
     * @return true, if only one object is null
     */
    public static boolean isOnlyOneObjectNull(Object o1, Object o2) {
        return (o1 == null && o2 != null) || (o1 != null && o2 == null);
    }

    /**
     * Encloses a given string within single quotes.
     * <p>
     * Throws NullPointerException if the input string is null.
     * 
     * @param str string to enclose
     * @return string wrapped in single quotes
     */
    public static String encloseInSingleQuotes(String str) {
        return wrapString(str, StringConstants.SINGLE_QUOTE, StringConstants.SINGLE_QUOTE);
    }

    /**
     * Encloses a given string within parenthesis.
     * <p>
     * Throws NullPointerException if the input string is null.
     * 
     * @param str string to enclose
     * @return string wrapped in parenthesis
     */
    public static String encloseInParenthesis(String str) {
        return wrapString(str, StringConstants.OPEN_PARENTHESIS, StringConstants.CLOSED_PARENTHESIS);
    }

    private static String wrapString(String str, String front, String rear) {
        Objects.requireNonNull(str, "String cannot be null");
        Objects.requireNonNull(front, "Begin string cannot be null");
        Objects.requireNonNull(rear, "End string cannot be null");
        return front + str + rear;
    }
}
