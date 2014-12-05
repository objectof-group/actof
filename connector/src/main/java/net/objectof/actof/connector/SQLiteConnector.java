package net.objectof.actof.connector;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.objectof.actof.connector.parameter.Parameter.Type;
import net.objectof.model.Package;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.repo.impl.sql.ISqlDb;
import net.objectof.repo.impl.sqlite.ISQLite;

import org.w3c.dom.Document;


public class SQLiteConnector extends AbstractConnector {

    public SQLiteConnector() {

        super();

        addParameter(Type.FILE, "Filename");
        addParameter(Type.STRING, "Repository");
        addParameter(Type.INT, "Version");
    }

    @Override
    public Package getPackage() throws Exception {
        return getDb().forName(getPackageName());
    }

    @Override
    public Package createPackage(Document schema) throws Exception {
        IPackage schemaPackage = new ISourcePackage(IBaseMetamodel.INSTANCE, schema);
        return getDb().createPackage(getPackageName(), ISQLite.class.getName(), schemaPackage);
    }

    private ISqlDb getDb() throws IOException, SQLException {
        DataSource ds = ISQLite.createPool(new File(value("Filename")));
        ISqlDb db = new ISqlDb("net/objectof/repo/res/postgres/statements", ds);
        return db;
    }

    @Override
    public String getPackageName() {
        return value("Filename") + ":" + value("Version") + "/" + value("Repository");
    }

    @Override
    public String getType() {
        return "SQLite";
    }

}
