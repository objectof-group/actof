package net.objectof.actof.porter.ui.operations;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import net.objectof.actof.common.controller.config.ActofEnv;
import net.objectof.actof.common.util.ActofUtil;
import net.objectof.actof.porter.ui.action.JsAction;
import net.objectof.actof.porter.ui.condition.Condition;
import net.objectof.actof.porter.ui.condition.Stage;
import net.objectof.actof.porter.ui.condition.Condition.Input;


/**
 * Class combining a Condition and an Action. This is useful for creating and
 * describing custom JavaScript rule matchers/transformers/listeners
 * 
 * @author NAS
 *
 */
public class JsOperation {

    public JsAction action;
    public Condition condition;

    public static List<JsOperation> load() {
        List<JsOperation> ops = new ArrayList<>();
        try {
            Scanner s = new Scanner(getOperationsFile());
            s = s.useDelimiter("\\Z");
            String contents = s.next();
            s.close();
            List<Map<Object, Object>> data = (List<Map<Object, Object>>) ActofUtil.deserialize(contents);

            for (Map<Object, Object> map : data) {
                ops.add(ActofUtil.deserialize(ActofUtil.serialize(map), JsOperation.class));
            }
        }
        catch (FileNotFoundException e) {}
        return ops;
    }

    public static void save(List<JsOperation> operations) throws IOException {
        Writer w = new FileWriter(getOperationsFile());
        w.write(ActofUtil.serialize(operations));
        w.close();
    }

    private static File getOperationsFile() {
        File appDir = ActofEnv.appDataDirectory("porter");
        appDir.mkdirs();
        File custom = new File(appDir.getPath() + "/custom-operations.json");
        return custom;
    }

    public static void main(String[] args) throws IOException {

        List<JsOperation> ops = load();

        JsOperation op = new JsOperation();
        op.condition = new Condition(Stage.MATCH, "KeyJS", Input.FIELD);
        op.action = new JsAction("function (context) { return input == context.getKey(); }");
        ops.add(op);
        save(ops);

    }
}
