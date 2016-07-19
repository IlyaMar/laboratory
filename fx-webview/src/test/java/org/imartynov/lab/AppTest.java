package org.imartynov.lab;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest  extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testDefaultParams() throws Exception {
        LoginProcessor l = new LoginProcessor();
        l.prepare();
        LoginScenario.Parameters p = l.getParameters();
        p.testPassword = "adsfs";


        l.start();
        l.stop();
    }
}
