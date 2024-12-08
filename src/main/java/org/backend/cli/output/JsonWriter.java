package org.backend.cli.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonWriter {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void writeToJsonPretty(List<Object> dataList, String filePath) {
        try {
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(new File(filePath), dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writeToJsonPretty(Object data, String filePath) {
        try {
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(new File(filePath), data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}