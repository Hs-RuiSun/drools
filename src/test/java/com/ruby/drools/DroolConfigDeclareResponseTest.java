package com.ruby.drools;

import com.ruby.drools.config.DroolConfig;
import com.ruby.drools.model.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DroolConfig.class)
public class DroolConfigDeclareResponseTest {
	@Autowired
	private KieContainer kieContainer;
	@Autowired
	private KieSession kieSession;
	
	@Test
	public void testValidResponse() {
		kieSession.insert(new Request("ABC", "123", 555));
		kieSession.fireAllRules();
		
		FactType respClass = getFactType();
		QueryResults results = getSingleQueryResults();
		QueryResultsRow row = results.iterator().next();
		Object qQuery = row.get("resultsList");
		assertEquals(555, respClass.get(qQuery, "rqstId") );
		assertEquals("VALID", respClass.get(qQuery, "respCode"));
		assertTrue((boolean)respClass.get(qQuery, "valid"));
	}
	
	@Test
	public void testInValidResponse() {
		kieSession.insert(new Request("DEF", "999", 111));
		kieSession.fireAllRules();
		
		FactType respClass = getFactType();
		QueryResults results = getSingleQueryResults();
		QueryResultsRow row = results.iterator().next();
		Object qQuery = row.get("resultsList");
		assertEquals(111, respClass.get(qQuery, "rqstId") );
		assertEquals("INVALID", respClass.get(qQuery, "respCode"));
		assertFalse((boolean)respClass.get(qQuery, "valid"));
	}
	
	@Test
	public void testResponseError() {
		kieSession.insert(new Request("ZZZ", "1000", 222));
		kieSession.fireAllRules();
		
		FactType respClass = getFactType();
		QueryResults results = getSingleQueryResults();
		QueryResultsRow row = results.iterator().next();
		Object qQuery = row.get("resultsList");
		assertEquals(222, respClass.get(qQuery, "rqstId") );
		assertEquals("ERROR", respClass.get(qQuery, "respCode"));
		assertFalse((boolean)respClass.get(qQuery, "valid"));
	}

	private QueryResults getSingleQueryResults() {
		return kieSession.getQueryResults("Response Results");
	}

	private FactType getFactType() {
		return kieContainer.getKieBase().getFactType("com.ruby.drools.model", "Response");
	}

}
