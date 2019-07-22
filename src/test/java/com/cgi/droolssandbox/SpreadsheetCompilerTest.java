package com.cgi.droolssandbox;

import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DroolConfig.class)
public class SpreadsheetCompilerTest {
	@Autowired
	private Resource resource;

	@Test
	public void testLoadFromClassPath() {
		SpreadsheetCompiler converter = new SpreadsheetCompiler();
		String drl = converter.compile(resource, InputType.XLS);
		assertNotNull(drl);
		assertTrue(drl.indexOf("import com.cgi.droolssandbox.Customer;") > -1);
		assertTrue(drl.indexOf("import com.cgi.droolssandbox.Offer;") > -1);
		assertTrue(drl.indexOf("package net.cloudburo.drools;") > -1);
		assertTrue(drl.indexOf("rule \"ProductPackageSelection\"") > -1);
	}
}
