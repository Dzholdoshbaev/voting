package utils;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Common {
    private Common(){
    }

    public static Map<String ,String> parseUrlEncodedLogin(String rawLine, String delimiter){
        String[] pairs = rawLine.split(delimiter);
        Stream<Map.Entry<String,String>> stream = Arrays.stream(pairs)
                .map(Common::decodeLogin)
                .filter(Optional::isPresent)
                .map(Optional::get);
        return stream.collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }

    public static Map<String, String> parseUrlEncodedRegister(String rawLine, String delimiter) {
        String[] pairs = rawLine.split(delimiter);
        Map<String, String> resultMap = new HashMap<>();

        for (String pair : pairs) {
            Optional<Map.Entry<String, String>> entryOptional = decodeRegister(pair);
            if (entryOptional.isPresent()) {
                Map.Entry<String, String> entry = entryOptional.get();
                if (!resultMap.containsKey(entry.getKey())) {
                    resultMap.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return resultMap;
    }

    private static Optional<Map.Entry<String,String>> decodeLogin(String kv){
        if (!kv.contains("=")){
            return Optional.empty();
        }
        String[] pairs = kv.split("=");
        if (pairs.length != 2){
            return Optional.empty();
        }
        Charset utf8 = StandardCharsets.UTF_8;
        String key = URLDecoder.decode(pairs[0],utf8);
        String value = URLDecoder.decode(pairs[1],utf8);
        return Optional.of(Map.entry(key,value));
    }

    private static Optional<Map.Entry<String,String>> decodeRegister(String kv) {
        List<Map.Entry<String,String>> dataList = new ArrayList<>();

        if (!kv.contains("=")) {
            return Optional.empty();
        }

        String[] pairs = kv.split("=");
        if (pairs.length % 2 != 0) {
            return Optional.empty();
        }

        Charset utf8 = StandardCharsets.UTF_8;
        for (int i = 0; i < pairs.length; i += 2) {
            String key = URLDecoder.decode(pairs[i], utf8);
            String value = URLDecoder.decode(pairs[i + 1], utf8);
            Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(key, value);
            dataList.add(entry);
        }

        return Optional.ofNullable(dataList.get(0));
    }

    public static int calculatePercentage(double obtained, double total) {
        if (total == 0) {
            return (int) 0.0;
        }
        return (int) (obtained * 100.0 / total);
    }


}
