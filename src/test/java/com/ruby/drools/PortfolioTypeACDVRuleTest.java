package com.ruby.drools;

import com.ruby.drools.config.DroolConfig;
import com.ruby.drools.model.ACDV;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DroolConfig.class)
public class PortfolioTypeACDVRuleTest {
    @Autowired
    private KieSession kieSession;
    private final static String CS_O_COMPOSITION_TEST_DATA = "src/test/resources/cs_o_composition_test_data.xls";

    @Test
    public void testRequiredFields(){
        ACDV acdv = new ACDV("", "AB", "", "AC");
        kieSession.insert(acdv);
        kieSession.fireAllRules();
        assertEquals(acdv.getErrorCode().intValue(), 1111);
        assertFalse(acdv.isValid());
    }

    @Test
    public void testCSAndOComposition() throws IOException, InvalidFormatException {
        List<Row> rows = ExcelReaderUtil.read(CS_O_COMPOSITION_TEST_DATA);
        ACDV acdv;
        for(Row row : rows){
            acdv = new ACDV(row.getCell(0).getStringCellValue(), row.getCell(2).getStringCellValue(),
                    row.getCell(1).getStringCellValue(), row.getCell(3).getStringCellValue());
            kieSession.insert(acdv);
            kieSession.fireAllRules();
            assertEquals(row.getCell(4).getStringCellValue().equals("No"), acdv.isValid());
            if(!acdv.isValid()){
                assertEquals(row.getCell(5).getNumericCellValue(), acdv.getErrorCode().doubleValue(),0);
            }
        }
    }

    @Test
    public void testInValidRequestPortfolioType(){
        ACDV acdv = new ACDV("A", "AB", "", "AC");
        kieSession.insert(acdv);
        kieSession.fireAllRules();
        assertEquals(acdv.getErrorCode().intValue(), 0);
        assertFalse(acdv.isValid());
    }

    @Test
    public void testInValidRequestSpecialCommentCode(){
        ACDV acdv = new ACDV("C", "AA", "", "AC");
        kieSession.insert(acdv);
        kieSession.fireAllRules();
        assertEquals(acdv.getErrorCode().intValue(), 0);
        assertFalse(acdv.isValid());
    }

    @Test
    public void testInValidResponsePortfolioType(){
        ACDV acdv = new ACDV("C", "AB", "A", "AC");
        kieSession.insert(acdv);
        kieSession.fireAllRules();
        assertEquals(acdv.getErrorCode().intValue(), 0);
        assertFalse(acdv.isValid());
    }

    @Test
    public void testInValidResponseSpecialCommentCode(){
        ACDV acdv = new ACDV("C", "AB", "", "AA");
        kieSession.insert(acdv);
        kieSession.fireAllRules();
        assertEquals(acdv.getErrorCode().intValue(), 0);
        assertFalse(acdv.isValid());
    }
}
