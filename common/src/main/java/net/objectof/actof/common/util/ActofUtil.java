package net.objectof.actof.common.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.AbstractTransformer;


public class ActofUtil {

    public static String readFile(File file) throws FileNotFoundException {
        return readFile(new FileInputStream(file));
    }

    public static String readFile(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\Z");
        String contents = scanner.next();
        scanner.close();
        return contents;
    }

    public static String readFile(String filename) throws FileNotFoundException {
        return readFile(new File(filename));
    }

    public static Object deserialize(String json) {
        return new JSONDeserializer<Object>().deserialize(json);
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        return new JSONDeserializer<T>().deserialize(json, clazz);
    }

    public static String serialize(Object o) {
        return new JSONSerializer().transform(new ExcludeTransformer(), void.class).exclude("*.class")
                .prettyPrint(true).deepSerialize(o);
    }

}

class ExcludeTransformer extends AbstractTransformer {

    @Override
    public Boolean isInline() {
        return true;
    }

    @Override
    public void transform(Object object) {
        // Do nothing, null objects are not serialized.
        return;
    }
}