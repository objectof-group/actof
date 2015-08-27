package net.objectof.actof.common.controller.config;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;
import java.util.Scanner;

import net.objectof.actof.common.util.ActofSerialize;


public class ConfigController {

    public static <T> void save(String program, String key, T data, Class<T> cls) {
        File datadir = ActofEnv.appDataDirectory(program);
        datadir.mkdirs();
        File datafile = new File(datadir, key + ".json");
        String contents = ActofSerialize.serialize(data);
        try {
            Writer datawriter = new FileWriter(datafile);
            datawriter.write(contents);
            datawriter.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static <T> Optional<T> load(String program, String key, Class<T> cls) {
        File datadir = ActofEnv.appDataDirectory(program);
        File datafile = new File(datadir, key + ".json");
        try {
            @SuppressWarnings("resource")
            Scanner scanner = new Scanner(datafile).useDelimiter("\\A");
            String contents = scanner.next();
            scanner.close();
            T data = ActofSerialize.deserialize(contents, cls);
            return Optional.ofNullable(data);
        }
        catch (FileNotFoundException e) {
            return Optional.empty();
        }

    }

}
