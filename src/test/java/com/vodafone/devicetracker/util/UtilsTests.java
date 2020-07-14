package com.vodafone.devicetracker.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Test all utility methods")
public class UtilsTests {
    @Nested
    class Test_fromCSV {
        @Test
        void ensureNullPointerExceptionIsThrown() {
            assertThrows(NullPointerException.class, () -> Utils.fromCSV(null),
                    "NullPointerException should have been thrown");
        }

        @Test
        void testBlankStringParsing() {
            assertArrayEquals(Utils.EMPTY_STRING_ARRAY, Utils.fromCSV(StringConstants.EMPTY_STRING),
                    "Failed to parse a string of zero length");
            assertArrayEquals(Utils.EMPTY_STRING_ARRAY, Utils.fromCSV(StringConstants.SPACE),
                    "Failed to parse a string comprising of a single space character");
            assertArrayEquals(Utils.EMPTY_STRING_ARRAY, Utils.fromCSV(StringConstants.TAB),
                    "Failed to parse a string comprising of a single tab space");
        }

        @Test
        void testForSingleElementParsing() {
            String str = "test";
            assertArrayEquals(new String[] { str }, Utils.fromCSV(str),
                    "Failed to parse a CSV string with single element");
        }

        @Test
        void testBoundaries() {
            String field1 = "field1";
            String field2 = "field2";
            String field3 = "field3";

            String emptyFirstField = ",field2";
            String emptySecondField = "field1,";
            String emptyMiddleField = "field1,,field3";

            assertArrayEquals(new String[] { StringConstants.EMPTY_STRING, field2 }, Utils.fromCSV(emptyFirstField),
                    "Failed to parse a string of two elements with first element being empty");
            assertArrayEquals(new String[] { field1, StringConstants.EMPTY_STRING }, Utils.fromCSV(emptySecondField),
                    "Failed to parse a string of two elements with second element being empty");
            assertArrayEquals(new String[] { field1, StringConstants.EMPTY_STRING, field3 },
                    Utils.fromCSV(emptyMiddleField),
                    "Failed to parse a string of three elements with middle element being empty");
        }

        @Test
        void testFieldsAreStripped() {
            String field1 = StringConstants.SPACE;
            String field2 = StringConstants.TAB;
            String text = "text";
            String field3 = StringConstants.SPACE + text + StringConstants.TAB;

            String str = field1 + StringConstants.COMMA + field2 + StringConstants.COMMA + field3;
            assertArrayEquals(new String[] { StringConstants.EMPTY_STRING, StringConstants.EMPTY_STRING, text },
                    Utils.fromCSV(str), "Failed to strip out white spaces around fields");
        }
    }

    @Nested
    class Test_eclosingMethods {
        @Test
        void testEncloseInQuotes() {
            assertThrows(NullPointerException.class, () -> Utils.encloseInSingleQuotes(null),
                    "NullPointerException should have been thrown");
            String[] strings = new String[] { "", " ", "\t", "xyz" };
            for (String str : strings) {
                assertEquals(StringConstants.SINGLE_QUOTE + str + StringConstants.SINGLE_QUOTE,
                        Utils.encloseInSingleQuotes(str), "String " + str + " not enclosed in quotes as expected");
            }
        }

        @Test
        void testEncloseInParanthesis() {
            assertThrows(NullPointerException.class, () -> Utils.encloseInParenthesis(null),
                    "NullPointerException should have been thrown");
            String[] strings = new String[] { "", " ", "\t", "xyz" };
            for (String str : strings) {
                assertEquals(StringConstants.OPEN_PARENTHESIS + str + StringConstants.CLOSED_PARENTHESIS,
                        Utils.encloseInParenthesis(str), "String " + str + " not enclosed in paranthesis as expected");
            }
        }
    }
}
