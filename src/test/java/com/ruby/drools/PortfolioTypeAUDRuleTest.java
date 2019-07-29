package com.ruby.drools;

import com.ruby.drools.config.DroolConfig;
import com.ruby.drools.model.AUD;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DroolConfig.class)
public class PortfolioTypeAUDRuleTest {
    @Autowired
    private KieSession kieSession;

    @Test
    public void testBlankFields(){
        AUD aud = new AUD("", "");
        kieSession.insert(aud);
        kieSession.fireAllRules();
        assertEquals(aud.getErrorCode().intValue(), 4098);
        assertFalse(aud.isValid());
    }

    @Test
    public void testInValidPortfolioType(){
        AUD aud = new AUD("A", "");
        kieSession.insert(aud);
        kieSession.fireAllRules();
        assertEquals(aud.getErrorCode().intValue(), 0000);
        assertFalse(aud.isValid());
    }

    @Test
    public void testInValidSpecialCommentCode(){
        AUD aud = new AUD("O", "E");
        kieSession.insert(aud);
        kieSession.fireAllRules();
        assertEquals(aud.getErrorCode().intValue(), 0000);
        assertFalse(aud.isValid());
    }

    @Test
    public void testCSSpecialCommentCode(){
        AUD aud = new AUD("C", "CS");
        kieSession.insert(aud);
        kieSession.fireAllRules();
        assertEquals(aud.getErrorCode().intValue(), 4099);
        assertFalse(aud.isValid());
    }

    @Test
    public void testValidRequest(){
        AUD aud = new AUD("O", "CS");
        kieSession.insert(aud);
        kieSession.fireAllRules();
        assertTrue(aud.isValid());
    }
}
