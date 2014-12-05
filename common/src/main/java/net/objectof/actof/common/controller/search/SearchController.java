package net.objectof.actof.common.controller.search;


import net.objectof.actof.common.controller.IActofController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.repository.RepositoryController;
import net.objectof.model.query.Query;


public class SearchController extends IActofController {

    RepositoryController repository;

    private SearchModel model = new SearchModel();
    private boolean validForKind = false;

    public SearchController(RepositoryController repository, ChangeController changes) {
        super(changes);
        this.repository = repository;
    }

    public boolean isValid() {
        return isNonNull() && validForKind;
    }

    public Query getQuery() {
        return model.query;
    }

    public void setQuery(Query query) {
        model.query = query;
        validForKind = isValidForKind();
        getChangeBus().broadcast(new QueryChange());
    }

    public String getKind() {
        return model.kind;
    }

    public void setKind(String kind) {
        model.kind = kind;
        validForKind = isValidForKind();
        getChangeBus().broadcast(new QueryChange());
    }

    private boolean isNonNull() {
        return model.query != null && model.kind != null;
    }

    private boolean isValidForKind() {
        try {
            repository.getStagingTx().query(model.kind, model.query);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

}
