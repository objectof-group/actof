package net.objectof.actof.porter;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.objectof.actof.porter.rules.Rule;
import net.objectof.actof.porter.rules.RuleBuilder;
import net.objectof.aggr.Composite;
import net.objectof.aggr.Listing;
import net.objectof.connector.Connector;
import net.objectof.connector.Connector.Initialize;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.sql.ISQLiteConnector;
import net.objectof.model.Package;
import net.objectof.model.Stereotype;


public class PorterTesting {

    public static void main(String[] args) throws ConnectorException, FileNotFoundException {
        testRealm();
        // testRulePrinting();
        testRealmReversed();
    }

    private static void testSettings() throws ConnectorException, FileNotFoundException {
        new File("/home/nathaniel/Desktop/Porting/settings/quotes-migrate.db").delete();

        // old package
        Connector oldConnector = new ISQLiteConnector();
        oldConnector.setParameter(ISQLiteConnector.KEY_FILENAME, "/home/nathaniel/Desktop/Porting/settings/quotes.db");
        oldConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "example.com:1520/quotes");
        Package oldRepo = oldConnector.getPackage();

        // new package
        Connector newConnector = new ISQLiteConnector();
        newConnector.setParameter(ISQLiteConnector.KEY_FILENAME,
                "/home/nathaniel/Desktop/Porting/settings/quotes-migrate.db");
        newConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "example.com:1520/quotes");
        Package newRepo = newConnector.createPackage(new FileInputStream(
                "/home/nathaniel/Desktop/Porting/settings/settings-schema-migrate.xml"), Initialize.WHEN_EMPTY);

        // @formatter:off
        
        Rule settings = RuleBuilder.start()
                .forKey("Setting")
                .setKey("Preference")
                .build();
        
        Rule settingkey = RuleBuilder.start()
                .forKey("Setting.key")
                .setKey("Preference.name")
                .build();
        
        Rule append = RuleBuilder.start()
                .forStereotype(Stereotype.TEXT)
                .valueTransform((context) -> context.getValue().toString() + "...")
                .build();
        
        // @formatter:on

        Porter p = new Porter(settings, settingkey, append);
        p.port(oldRepo, newRepo);

    }

    private static void testRealm() throws ConnectorException, FileNotFoundException {
        new File("/home/nathaniel/Desktop/Porting/emptyapp/empty-port.db").delete();

        // old package
        Connector oldConnector = new ISQLiteConnector();
        oldConnector.setParameter(ISQLiteConnector.KEY_FILENAME, "/home/nathaniel/Desktop/Porting/emptyapp/empty.db");
        oldConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "realmproject.net:1502/realm");
        Package oldRepo = oldConnector.getPackage();

        // new package
        Connector newConnector = new ISQLiteConnector();
        newConnector.setParameter(ISQLiteConnector.KEY_FILENAME,
                "/home/nathaniel/Desktop/Porting/emptyapp/empty-port.db");
        newConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "realmproject.net:1521/realm");
        Package newRepo = newConnector.createPackage(new FileInputStream(
                "/home/nathaniel/Desktop/Porting/emptyapp/realm-port.xml"), Initialize.WHEN_EMPTY);

        Porter p = new Porter();

        // @formatter:off
        
        Rule roleToRoles = RuleBuilder.start()
            .forKey("Person.role")
            .setKey("Person.roles")
            .valueTransform((context) -> {
                Listing<Object> roles = context.getToTx().create("Person.roles");
                roles.add(context.getValue());
                return roles;
            })
            .build();
        
//        Rule portDescription = RuleBuilder.start()
//                .forKey("Session")
//                .valueTransform(context -> {
//                    Composite oldSession = (Composite) context.getValue();
//                    Composite oldAssignment = (Composite) oldSession.get("assignment");
//                    String description = oldAssignment.get("description").toString();
//                    
//                    Composite newSession = context.getToTx().create("Session");
//                    newSession.set("description", description);
//                    return newSession;
//                })
//                .build();
//        
        
        Rule portDescription = RuleBuilder.start()
                .forKey("Session")
                .onPort((before, after) -> {
                    Composite oldSession = (Composite) before.getValue();
                    Composite oldAssignment = (Composite) oldSession.get("assignment");
                    String description = oldAssignment.get("description").toString();
                    
                    Composite newSession = (Composite) after.getValue();
                    newSession.set("description", description);
                })
                .build();
        
        
        Rule dropAssnDesc = RuleBuilder.start()
                .forKey("Assignment.description")
                .drop()
                .build();

        Rule dropCourse = RuleBuilder.start()
                .forKey("Course", "Person.enrolledCourses", "Person.pendingCourses", "Assignment.course")
                .drop()
                .build();
        
        Rule dropPersonFields = RuleBuilder.start()
                .forKey("Person.email", "Person.pwdHashed", "Person.salt", "Person.roles")
                .drop()
                .build();
        
        Rule createAccount = RuleBuilder.start()
                .forKey("Person")
                .onPort((before, after) -> {
                    Composite oldPerson = (Composite) before.getValue();
                    Composite newPerson = (Composite) after.getValue();
                    
                    Composite account = after.getToTx().create("Account");
                    account.set("email", oldPerson.get("email"));
                    account.set("pwdHashed", oldPerson.get("pwdHashed"));
                    account.set("salt", oldPerson.get("salt"));
                    
                    Composite oldRole = (Composite) oldPerson.get("role");
                    Listing<Object> newRoles = after.getToTx().create("Account.roles");
                    account.set("roles", newRoles);
                    //Object newRole = p.updateReference(after, oldRole);
                    //newRoles.add(newRole);
                    newRoles.add(oldRole);
                    
                    newPerson.set("account", account);
                })
                .build();

        
//        Rule settings = RuleBuilder.start()
//                .forKey("Setting")
//                .setKey("Preference")
//                .build();
//        
//        Rule settingkey = RuleBuilder.start()
//                .forKey("Setting.key")
//                .setKey("Preference.name")
//                .build();
//        
//        Rule append = RuleBuilder.start()
//                .forStereotype(Stereotype.TEXT)
//                .valueTransform((k, v, kind) -> v.toString() + "...")
//                .build();
        
        // @formatter:on

        p.addRules(roleToRoles, portDescription, dropAssnDesc, dropPersonFields, createAccount, dropCourse);

        System.out.println("-----------------------------");

        p.port(oldRepo, newRepo);

    }

    private static void testRealmReversed() throws ConnectorException, FileNotFoundException {
        new File("/home/nathaniel/Desktop/Porting/rolereversal/empty-port.db").delete();

        // old package
        Connector oldConnector = new ISQLiteConnector();
        oldConnector.setParameter(ISQLiteConnector.KEY_FILENAME,
                "/home/nathaniel/Desktop/Porting/rolereversal/empty.db");
        oldConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "realmproject.net:1502/realm");
        Package oldRepo = oldConnector.getPackage();

        // new package
        Connector newConnector = new ISQLiteConnector();
        newConnector.setParameter(ISQLiteConnector.KEY_FILENAME,
                "/home/nathaniel/Desktop/Porting/rolereversal/empty-port.db");
        newConnector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "realmproject.net:1502/realm");
        Package newRepo = newConnector.createPackage(new FileInputStream(
                "/home/nathaniel/Desktop/Porting/rolereversal/realm-port.xml"), Initialize.WHEN_EMPTY);

        // @formatter:off
        
        Rule dropRoles = RuleBuilder.start().forKind("Person.roles.role").drop().build();
        
        Rule rolesToRole = RuleBuilder.start()
            .forKey("Person.roles")
            .setKey("Person.role")
            .valueTransform(context -> {
                Listing<Object> roles = (Listing<Object>) context.getValue();
                if (roles.size() == 0) { 
                    return null; 
                }
                return roles.get(0);
            })
            .build();
        
//        Rule roleToRoles = RuleBuilder.start()
//            .forKey("Person.role")
//            .setKey("Person.roles")
//            .valueTransform((context) -> {
//                Listing<Object> roles = context.getToTx().create("Person.roles");
//                roles.add(context.getValue());
//                return roles;
//            })
//            .build();
        
        
//        Rule settings = RuleBuilder.start()
//                .forKey("Setting")
//                .setKey("Preference")
//                .build();
//        
//        Rule settingkey = RuleBuilder.start()
//                .forKey("Setting.key")
//                .setKey("Preference.name")
//                .build();
//        
//        Rule append = RuleBuilder.start()
//                .forStereotype(Stereotype.TEXT)
//                .valueTransform((k, v, kind) -> v.toString() + "...")
//                .build();
        
        // @formatter:on

        Porter p = new Porter(rolesToRole, dropRoles);

        System.out.println("-----------------------------");

        p.port(oldRepo, newRepo);

    }

    private static void testRulePrinting() {
        // @formatter:off
        Rule testRule = RuleBuilder.start()
                .forKey("asdf")
                .forKey("qwerty")
                .setKey("asdf++")
                .match(context -> true)
                .build();
        System.out.println(testRule);
        // @formatter:on
    }

}
