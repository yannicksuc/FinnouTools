package fr.lordfinn.finnoutools.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
    private static final Pattern QUOTE_PATTERN = Pattern.compile("\"([^\"]*)\"");

    public static List<Component> wrapText(String text, int maxLength, NamedTextColor color) {
        List<Component> wrappedText = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() > maxLength) {
                wrappedText.add(Component.text(currentLine.toString(), color));
                currentLine = new StringBuilder();
            }
            if (currentLine.length() > 0) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }

        if (currentLine.length() > 0) {
            wrappedText.add(Component.text(currentLine.toString(), color));
        }

        return wrappedText;
    }

    public static String[] parseArgsArray(String[] inputArray) {
        List<String> parsedList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (String item : inputArray) {
            if (isQuoted(item)) {
                if (isSingleQuotedWord(item)) {
                    parsedList.add(extractContentFromQuotes(item));
                } else {
                    stringBuilder.append(item);
                }
            } else if (isClosingQuote(item)) {
                stringBuilder.append(" ").append(item);
                parsedList.add(extractContentFromQuotes(stringBuilder.toString()));
                stringBuilder.setLength(0);
            } else if (stringBuilder.length() > 0) {
                stringBuilder.append(" ").append(item);
            } else {
                parsedList.add(item);
            }
        }

        return parsedList.toArray(new String[0]);
    }

    private static boolean isQuoted(String item) {
        return item.startsWith("\"");
    }

    private static boolean isClosingQuote(String item) {
        return item.endsWith("\"");
    }

    private static boolean isSingleQuotedWord(String item) {
        return item.length() > 1 && isClosingQuote(item);
    }

    private static String extractContentFromQuotes(String item) {
        Matcher matcher = QUOTE_PATTERN.matcher(item);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static List<Component> createWrappedComponent(String text, int maxSize, NamedTextColor color) {
        return TextUtil.wrapText(text, maxSize, color);
    }

    public static String truncateStringIgnoringFormatting(String input, int maxLength, boolean doAddEllipsis) {
        String withoutFormatting = input.replaceAll("§[0-9a-fA-Fk-oK-OrR]", "");
        if (withoutFormatting.length() <= maxLength) {
            return input;
        }

        StringBuilder truncated = new StringBuilder();
        int visibleCharCount = 0;
        int i = 0;

        while (visibleCharCount < maxLength && i < input.length()) {
            char currentChar = input.charAt(i);
            if (currentChar == '§') {
                if (i + 1 < input.length()) {
                    char nextChar = input.charAt(i + 1);
                    if (isColorCode(nextChar)) {
                        truncated.append(currentChar).append(nextChar);
                        i += 2;
                        continue;
                    }
                }
            }

            truncated.append(currentChar);
            visibleCharCount++;
            i++;
        }

        return truncated + (doAddEllipsis ? "..." : "");
    }

    private static boolean isColorCode(char c) {
        return "0123456789aAbBcCdDeEfFkKlLmMnNoOrR".indexOf(c) != -1;
    }
}