package fr.lordfinn.finnoutools.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

public class TextUtil {

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
}