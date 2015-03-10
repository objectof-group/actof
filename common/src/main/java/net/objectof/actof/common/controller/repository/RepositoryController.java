package net.objectof.actof.common.controller.repository;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import net.objectof.actof.common.controller.IActofController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.connector.Connector;
import net.objectof.model.Kind;
import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;


public class RepositoryController extends IActofController {

    private RepositoryModel model = new RepositoryModel();

    public RepositoryController(ChangeController changes) {
        super(changes);
    }

    public void connect(Connector conn) throws Exception {
        Package repo = conn.getPackage();
        setRepo(repo, conn.getPackageName());
        makeFresh();
    }

    public List<Kind<?>> getEntities() {
        List<Kind<?>> kinds = new ArrayList<>();
        for (Kind<?> kind : getRepo().getParts()) {
            if (kind.getPartOf() != null) {
                continue;
            }
            kinds.add(kind);
        }
        kinds.sort((a, b) -> a.getComponentName().compareTo(b.getComponentName()));
        return kinds;
    }

    /**
     * Creates a fresh transaction for staging changes to. This is the "dirty"
     * transaction which should be used to make all read-queries, as it contains
     * any modified data.
     */
    public void makeFresh() {
        model.stagingTx = model.repo.connect("repospy");
        model.cleanTx = model.repo.connect("repospy");
        clearTransientEntities();
        getChangeBus().broadcast(new RepositoryReplacedChange());
    }

    /**
     * Posts the current staging transaction, and creates new staging and fresh
     * transactions
     */
    public void post() {
        getStagingTx().post();
        makeFresh();
    }

    /**
     * Returns the current staging transaction. This is the "dirty" transaction
     * which should be used to make all read-queries, as it contains any
     * modified data.
     */
    public Transaction getStagingTx() {
        return model.stagingTx;
    }

    /**
     * Returns the current clean transaction. This is the "pure" transaction
     * which should be used to retrieve unmodified data as it currently exists
     * in-repo
     */
    public Transaction getCleanTx() {
        return model.cleanTx;
    }

    public Package getRepo() {
        return model.repo;
    }

    public String getRepoName() {
        return model.name;
    }

    private void setRepo(Package repo, String name) {
        model.repo = repo;
        model.name = name;
        getChangeBus().broadcast(new RepositoryReplacedChange());
    }

    public List<Resource<?>> getTransientsForKind(String kind) {
        return model.transients.stream().filter(res -> res.id().kind().getComponentName().equals(kind))
                .collect(Collectors.toList());

    }

    public void clearTransientEntities() {
        model.transients.clear();
    }

    public void addTransientEntity(Resource<?> res) {
        model.transients.add(res);
    }

    // TODO: Relies on each .send call writing json without linebreaks!
    public void load(Scanner scanner) {
        scanner = scanner.useDelimiter("\n");
        while (scanner.hasNext()) {
            String str = scanner.next();
            StringReader reader = new StringReader(str);
            model.stagingTx.receive("application/json", reader);
        }
    }

    // TODO: Relies on each .send call writing json without linebreaks!
    public String dump() throws IOException {

        Writer writer = new StringWriter();

        for (Kind<?> entity : getEntities()) {
            // non-transients
            Iterable<Resource<?>> iter = model.stagingTx.enumerate(entity.getComponentName());
            for (Resource<?> res : iter) {
                res.send("application/json", writer);
                writer.write("\n");
            }
            // transients
            for (Resource<?> res : getTransientsForKind(entity.getComponentName())) {
                res.send("application/json", writer);
                writer.write("\n");
            }
        }

        return writer.toString();
    }

}
