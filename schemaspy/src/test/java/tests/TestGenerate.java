package tests;


import java.io.File;

import org.junit.Test;

import net.objectof.Context;
import net.objectof.actof.schemaspy.generator.IGenerator;
import net.objectof.impl.spring.ISpringContext;


public class TestGenerate {

    @Test
    public void testTemplating() throws Exception {
        Context<IGenerator> ctx = new ISpringContext<IGenerator>(IGenerator.class, "TestContext.xml");
        IGenerator g = ctx.forName("generator");
        g.setRoot(new File("."));
        g.process();
    }
}
