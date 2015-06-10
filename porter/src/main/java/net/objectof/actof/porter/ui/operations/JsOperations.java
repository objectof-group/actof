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
import java.util.stream.Collectors;

import net.objectof.actof.common.controller.config.ActofEnv;
import net.objectof.actof.common.util.ActofUtil;
import net.objectof.actof.porter.ui.action.JsAction;
import net.objectof.actof.porter.ui.condition.Condition;
import net.objectof.actof.porter.ui.condition.Condition.Input;
import net.objectof.actof.porter.ui.condition.Condition.Stage;


/**
 * Class combining a Condition and an Action. This is useful for creating and
 * describing custom JavaScript rule matchers/transformers/listeners
 * 
 * @author NAS
 *
 */
public class JsOperations {

    public static List<Operation> load() {
        List<Operation> ops = new ArrayList<>();
        try {
            Scanner s = new Scanner(getOperationsFile());
            s = s.useDelimiter("\\Z");
            String contents = s.next();
            s.close();
            List<Map<Object, Object>> data = (List<Map<Object, Object>>) ActofUtil.deserialize(contents);

            for (Map<Object, Object> map : data) {
                Operation op = new Operation();
                JsAction action = ActofUtil.convert(map.get("action"), JsAction.class);
                Condition condition = ActofUtil.convert(map.get("condition"), Condition.class);
                op.setAction(action);
                op.setCondition(condition);
                ops.add(op);
            }
        }
        catch (FileNotFoundException e) {}
        return ops;
    }

    public static void save(List<Operation> operations) throws IOException {
        operations = operations.stream().filter(op -> op.getAction() instanceof JsAction).collect(Collectors.toList());
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

        List<Operation> ops = load();

        Operation op = new Operation();
        op.setCondition(new Condition(Stage.MATCH, "KeyJS", Input.FIELD));
        op.setAction(new JsAction("function (context) { return input == context.getKey(); }"));
        ops.add(op);
        save(ops);

    }
}
