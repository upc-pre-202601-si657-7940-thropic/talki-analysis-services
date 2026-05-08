package com.thropic.talki.filler.domain.model;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FillerDetector {

    private static final List<String> SPANISH_FILLERS = List.of(
            "este", "o sea", "eh", "básicamente", "o sea que",
            "bueno", "pues", "entonces", "verdad", "digamos",
            "como que", "osea", "um", "mmm", "este este"
    );

    public Map<String, Integer> detect(String text) {
        String normalized = text.toLowerCase();
        Map<String, Integer> counts = new HashMap<>();

        for (String filler : SPANISH_FILLERS) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(filler) + "\\b");
            Matcher matcher = pattern.matcher(normalized);
            int count = 0;
            while (matcher.find()) count++;
            if (count > 0) counts.put(filler, count);
        }

        return counts;
    }

    public int totalFillers(Map<String, Integer> fillersByType) {
        return fillersByType.values().stream().mapToInt(Integer::intValue).sum();
    }
}
