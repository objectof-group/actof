package net.objectof.actof.schemaspy.util;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import net.objectof.actof.common.controller.schema.SchemaController;
import net.objectof.actof.schemaspy.generator.IGenerator;
import net.objectof.actof.schemaspy.generator.templates.ICompositeTemplate;
import net.objectof.actof.schemaspy.generator.templates.IInterfaceTemplate;
import net.objectof.actof.schemaspy.generator.velocity.ITemplateContext;
import net.objectof.facet.Annotated;
import net.objectof.facet.Facet;
import net.objectof.facet.impl.IFacets;
import net.objectof.model.impl.IBaseMetamodel;

import org.eclipse.jdt.core.compiler.batch.BatchCompiler;


public class CodeGen {

    private static final String SOURCE = "src/main/resources/packages/";
    private static final String DEST = "src/main/java/";

    public static void generate(SchemaController schema, File jarfile) throws IOException,
            TransformerFactoryConfigurationError, TransformerException {

        File tempdir = File.createTempFile("SchemaSpyCompiler", "");
        tempdir.delete();
        tempdir.mkdir();
        File source = new File(tempdir, SOURCE);
        File dest = new File(tempdir, DEST);
        source.mkdirs();
        dest.mkdirs();

        codegen(schema, tempdir, source);
        compile(dest, jarfile);

    }

    @SuppressWarnings("unchecked")
    private static void codegen(SchemaController schema, File tempdir, File source) throws IOException,
            TransformerFactoryConfigurationError, TransformerException {

        ITemplateContext context = new ITemplateContext("");

        IInterfaceTemplate inter = new IInterfaceTemplate(context);
        ICompositeTemplate comp = new ICompositeTemplate(context);
        List<Facet<Annotated>> list = new ArrayList<>();
        list.add((Facet<Annotated>) (Object) inter);
        list.add((Facet<Annotated>) (Object) comp);

        IFacets<Annotated> facets = new IFacets<Annotated>("test.objectof.org:1401/mm/generate", list);


        IGenerator gen = new IGenerator(tempdir.getAbsolutePath(), SOURCE, DEST, IBaseMetamodel.INSTANCE, context,
                facets);


        String output = schema.toXML();
        Writer writer = new FileWriter(new File(source, "schema.xml"));
        writer.write(output);
        writer.close();


        gen.perform("root:", tempdir);
        gen.perform("process");




    }

    private static void compile(File dest, File jarfile) throws IOException {

        StringWriter log = new StringWriter();
        BatchCompiler.compile("-source 1.8 " + "\"" + dest.getCanonicalPath() + "\"", new PrintWriter(System.out),
                new PrintWriter(System.err), null);

        deleteJavaFiles(dest);

        buildJar(jarfile, dest);

        // deleteDir(dest);
    }


    private static void deleteJavaFiles(File dir) throws IOException {
        Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toFile().getName().endsWith(".java")) {
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                // Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

        });
    }

    private static void deleteDir(File dir) throws IOException {
        Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

        });
    }


    // Lifted/modified from
    // http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file
    public static void buildJar(File jarfile, File source) throws IOException {

        ProcessBuilder pb = new ProcessBuilder("jar", "cvf", jarfile.getAbsolutePath()
        /* source.getAbsolutePath() + "/*" */
        );

        pb.directory(source);
        int prefixlength = source.getAbsolutePath().length() + 1;
        Files.walk(source.toPath()).forEach(path -> {
            if (path.toFile().isDirectory()) { return; }
            pb.command().add(path.toFile().getAbsolutePath().substring(prefixlength));
        });

        System.out.println(pb.command());
        pb.redirectErrorStream(true);
        Process makejar = pb.start();
        try {
            makejar.waitFor();
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(makejar.getInputStream()));
        while ((line = reader.readLine()) != null) {
            System.out.println("Stdout: " + line);
        }




        /*
         * Manifest manifest = new Manifest();
         * manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION,
         * "1.0"); JarOutputStream target = new JarOutputStream(new
         * FileOutputStream(jarfile), manifest); add(source, source, target);
         * target.close();
         */

    }

    private static void add(File base, File source, JarOutputStream target) throws IOException {
        BufferedInputStream in = null;
        try {

            // create directory entries
            if (source.isDirectory()) {
                String basename = sanitize(base);
                String name = sanitize(source);
                if (!name.isEmpty()) {
                    if (!name.endsWith("/")) {
                        name += "/";
                    }
                    name = name.substring(basename.length());

                    JarEntry entry = new JarEntry(name);
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }
                for (File nestedFile : source.listFiles()) {
                    add(base, nestedFile, target);
                }

                return;
            }

            String name = sanitize(source);
            String basename = sanitize(base);
            name = name.substring(basename.length());

            JarEntry entry = new JarEntry(name);
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            in = new BufferedInputStream(new FileInputStream(source));

            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1) break;
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        }
        finally {
            if (in != null) in.close();
        }
    }

    private static String sanitize(File file) {
        return file.getPath().replace("\\", "/");
    }

}
