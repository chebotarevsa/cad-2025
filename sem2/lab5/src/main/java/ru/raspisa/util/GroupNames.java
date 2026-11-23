package ru.raspisa.util;

import java.util.Comparator;
import java.util.Locale;

public final class GroupNames {

    public static final String PATTERN = "[\\p{L}\\p{N}]+(?:-[\\p{L}\\p{N}]+)*";
    public static final Comparator<String> COMPARATOR = Comparator.nullsLast(GroupNames::compareNatural);

    private GroupNames() {
    }

    public static String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase(Locale.ROOT);
    }

    private static int compareNatural(String left, String right) {
        int leftIndex = 0;
        int rightIndex = 0;

        while (leftIndex < left.length() && rightIndex < right.length()) {
            char leftChar = left.charAt(leftIndex);
            char rightChar = right.charAt(rightIndex);

            if (Character.isDigit(leftChar) && Character.isDigit(rightChar)) {
                int leftEnd = digitEnd(left, leftIndex);
                int rightEnd = digitEnd(right, rightIndex);
                int numberComparison = compareNumberParts(
                        left.substring(leftIndex, leftEnd),
                        right.substring(rightIndex, rightEnd)
                );
                if (numberComparison != 0) return numberComparison;
                leftIndex = leftEnd;
                rightIndex = rightEnd;
                continue;
            }

            int charComparison = Character.compare(
                    Character.toUpperCase(leftChar),
                    Character.toUpperCase(rightChar)
            );
            if (charComparison != 0) return charComparison;
            leftIndex++;
            rightIndex++;
        }

        return Integer.compare(left.length(), right.length());
    }

    private static int digitEnd(String value, int start) {
        int end = start;
        while (end < value.length() && Character.isDigit(value.charAt(end))) end++;
        return end;
    }

    private static int compareNumberParts(String left, String right) {
        String normalizedLeft = left.replaceFirst("^0+(?!$)", "");
        String normalizedRight = right.replaceFirst("^0+(?!$)", "");
        int lengthComparison = Integer.compare(normalizedLeft.length(), normalizedRight.length());
        if (lengthComparison != 0) return lengthComparison;
        int valueComparison = normalizedLeft.compareTo(normalizedRight);
        return valueComparison != 0 ? valueComparison : Integer.compare(left.length(), right.length());
    }
}
