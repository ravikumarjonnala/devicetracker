package com.vodafone.devicetracker.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Test all business rules")
public class BusinessRulesTests {
    @Nested
    @DisplayName("Rule: isCycleTracker")
    class TestIsCycleTracker {
        @Test
        @DisplayName("Passing in a null for productId")
        public void nullProductId() {
            String productId = null;
            assertThrows(NullPointerException.class, () -> BusinessRules.isCycleTracker(productId),
                    "NullPointerException should have been thrown");
        }

        @Test
        @DisplayName("Passing in an empty string for productId")
        public void blankProductId() {
            String productId = StringConstants.EMPTY_STRING;
            assertFalse(() -> BusinessRules.isCycleTracker(productId), "False should have been returned");
        }

        @Test
        @DisplayName("Passing in a white spaced string for productId")
        public void whiteSpaceProductId() {
            String productId = StringConstants.SPACE;
            assertFalse(() -> BusinessRules.isCycleTracker(productId), "False should have been returned");
        }

        @Test
        @DisplayName("Passing in the string 'WG' for productId")
        public void wgProductId() {
            String productId = StringConstants.WG;
            assertTrue(() -> BusinessRules.isCycleTracker(productId), "True should have been returned");
        }
    }

}
