/*
 * User: tom
 * Date: Jul 12, 2002
 * Time: 8:10:10 PM
 */
package test.net.sourceforge.pmd;

import junit.framework.TestCase;
import net.sourceforge.pmd.TypeSet;
import net.sourceforge.pmd.PMD;

import java.io.File;
import java.util.Set;
import java.util.HashSet;

public class TypeSetTest extends TestCase {
    public TypeSetTest(String name) {
        super(name);
    }

    public void testASTCompilationUnitPackage() {
        TypeSet t = new TypeSet();
        t.setASTCompilationUnitPackage("java.lang.");
        assertEquals("java.lang.", t.getASTCompilationUnitPackage());
    }

    public void testAddImport() {
        TypeSet t = new TypeSet();
        t.addImport("java.io.File");
        assertEquals(1, t.getImportsCount());
    }


    public void testFindClassImplicitImport() throws Throwable {
        TypeSet t = new TypeSet();
        Class clazz = t.findClass("String");
        assertEquals(String.class, clazz);
    }

    public void testFindClassSamePackage() throws Throwable {
        TypeSet t = new TypeSet();
        t.setASTCompilationUnitPackage("net.sourceforge.pmd.");
        Class clazz = t.findClass("PMD");
        assertEquals(PMD.class, clazz);
    }

    public void testFindClassExplicitImport() throws Throwable {
        TypeSet t = new TypeSet();
        t.addImport("java.io.File");
        Class clazz = t.findClass("File");
        assertEquals(File.class, clazz);
    }

    // inner class tests
    public void testExplicitImportResolver() throws Throwable {
        Set imports = new HashSet();
        imports.add("java.io.File");
        TypeSet.Resolver r = new TypeSet.ExplicitImportResolver(imports);
        assertEquals(File.class,  r.resolve("File"));
    }

    public void testImplicitImportResolverPass() throws Throwable {
        TypeSet.Resolver r = new TypeSet.ImplicitImportResolver();
        assertEquals(String.class, r.resolve("String"));
    }

    public void testImplicitImportResolverPassFail() throws Throwable {
        TypeSet.Resolver r = new TypeSet.ImplicitImportResolver();
        try {
            r.resolve("foo");
            throw new RuntimeException("Should have thrown an exception");
        } catch (ClassNotFoundException cnfe) {
            // cool
        }
    }

    public void testCurrentPackageResolverPass() throws Throwable {
        TypeSet.Resolver r = new TypeSet.CurrentPackageResolver("net.sourceforge.pmd.");
        assertEquals(PMD.class,  r.resolve("PMD"));
    }

    public void testImportOnDemandResolverPass() throws Throwable {
        Set imports = new HashSet();
        imports.add("java.io.*");
        imports.add("java.util.*");
        TypeSet.Resolver r = new TypeSet.ImportOnDemandResolver(imports);
        assertEquals(Set.class, r.resolve("Set"));
        assertEquals(File.class, r.resolve("File"));
    }

    public void testImportOnDemandResolverFail() throws Throwable {
        Set imports = new HashSet();
        imports.add("java.io.*");
        imports.add("java.util.*");
        TypeSet.Resolver r = new TypeSet.ImportOnDemandResolver(imports);
        try {
            r.resolve("foo");
            throw new RuntimeException("Should have thrown an exception");
        } catch (ClassNotFoundException cnfe) {
            // cool
        }
        try {
            r.resolve("String");
            throw new RuntimeException("Should have thrown an exception");
        } catch (ClassNotFoundException cnfe) {
            // cool
        }
    }

}



