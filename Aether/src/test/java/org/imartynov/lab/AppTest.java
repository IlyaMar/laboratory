package org.imartynov.lab;

import org.junit.Test;

public class AppTest 
{
	@Test
    public void testApp() throws Exception
    {
		App.resolveArtifact();
		App.resolveVersionRange();
    }
}
