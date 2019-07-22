/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.droolssandbox;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kproject.ReleaseIdImpl;

import org.drools.core.event.DefaultAgendaEventListener;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.builder.ReleaseId;
import org.kie.api.definition.type.FactType;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.runtime.rule.Match;
import org.kie.internal.io.ResourceFactory;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

/**
 *
 * @author dawna.floyd
 */
public class DroolsPlayground {

    KieServices ks;
    KieSession ksession;
    static final String PACKAGE_NAME = "com.cgi.droolssandbox";
    static final String EXTERNAL_XLS_FILE = "src/main/resources/decision_table_example_in_memory_response.xls";

    KieContainer kc;
    File kPom;
    int ReleaseCount = 1;
    ReleaseId releaseId;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws DroolsParserException, IOException, InterruptedException {
        DroolsPlayground droolsTest = new DroolsPlayground();
        droolsTest.executeDrools();
    }

    public void executeDrools() throws DroolsParserException, IOException, InterruptedException {
        ks = KieServices.Factory.get();

        InternalKieModule kJar = createKJar();
        ks.getRepository().addKieModule(kJar);
        kc = ks.newKieContainer(releaseId);

        ksession = kc.newKieSession();
       // addAgendaListener(ksession);
       // addWMListener(ksession);
            addData(ksession);
            ksession.fireAllRules();
            retractClass(Request.class, ksession);
            checkResponses(ksession);
    }

    public void checkResponses(KieSession session) {
        FactType respClass = (FactType) kc.getKieBase()
            .getFactType("com.cgi.droolssandbox", "Response");
        QueryResults results = session.getQueryResults("Response Results");
        for (QueryResultsRow row : results) {
            Object qQuery = row.get("resultsList");
            System.out.println("Response created: rqstId=" 
                + respClass.get(qQuery, "rqstId") + " , respCode=" 
                + respClass.get(qQuery, "respCode") + " , valid=" 
                + respClass.get(qQuery, "valid") );
        }
    }

    @SuppressWarnings("unchecked")
    public void retractClass(Class myClass, KieSession session) {

        Collection<FactHandle> facts = session.
            getFactHandles(new ClassObjectFilter(myClass));

        for (FactHandle fh : facts) {
            session.delete(fh);
        }
    }

    void addData(KieSession session) {
        Request p1 = new Request();
        p1.setId(555);
        p1.setCode("ABC");
        p1.setSubCode("123");
        session.insert(p1);
        Request p2 = new Request();
        p2.setId(111);
        p2.setCode("DEF");
        p2.setSubCode("999");
        session.insert(p2);
        Request p3 = new Request();
        p3.setId(222);
        p3.setCode("ZZZ");
        p3.setSubCode("1000");
        session.insert(p3);
        Request p4 = new Request();
        p4.setId(333);
        p4.setCode("ERROR");
        p4.setSubCode("444");
        session.insert(p4);

    }

    public InternalKieModule createKJar() {
        releaseId = genReleaseId(++ReleaseCount);
        Resource drl = ResourceFactory.newFileResource(EXTERNAL_XLS_FILE);

        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        String drlContent = compiler.compile(drl, InputType.XLS);
        System.out.println("Start GeneratedDRLFile:");
        System.out.println(drlContent);
        System.out.println("End GeneratedDRLFile:");
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.generateAndWritePomXML(releaseId);
        kfs.write("src/main/resources/GenRulesContent.drl", drlContent);
        KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
        InternalKieModule kieModule = (InternalKieModule) kieBuilder.getKieModule();

        Results results = kieBuilder.buildAll().getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            for (Message result : results.getMessages()) {
                System.out.println(result.toString());
            }
            return null;
        }
        ks.getRepository().addKieModule(kieModule);
        return kieModule;
    }

    public ReleaseId genReleaseId(int iRefreshTimes) {
        releaseId
            = new ReleaseIdImpl(PACKAGE_NAME,
                "SandBox", "1.0.0-" + iRefreshTimes);
        return releaseId;

    }

    void addAgendaListener(KieSession session) {

        session.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void afterMatchFired(final AfterMatchFiredEvent event) {
                Match m = event.getMatch();
                System.out.println("Rule Executed: " + m.getRule().getName() + " from package:" + m.getRule().getPackageName());

            }

            @Override
            public void
                matchCreated(final MatchCreatedEvent event) {
                Match m = event.getMatch();
                String ruleName = m.getRule().getName();
                System.out.println("Rule Added to Agenda: " + ruleName);

            }

            @Override
            public void matchCancelled(MatchCancelledEvent event) {
                Match m = event.getMatch();
                String ruleName = m.getRule().getName();
                System.out.println("Rule Removed from Agenda: " + ruleName);
            }
        });

    }

    void addWMListener(KieSession session) {
        session.addEventListener(new DebugRuleRuntimeEventListener() {
            @Override
            public void objectInserted(ObjectInsertedEvent event) {
                System.out.println("(+)Added to WM: " + event.getObject().toString());

            }

            @Override
            public void objectUpdated(ObjectUpdatedEvent event) {
                System.out.println("(U)Updated in WM: " + event.getObject().toString());

            }

            @Override

            public void objectDeleted(ObjectDeletedEvent event) {
                System.out.println("(-)deleted in WM: " + event.getOldObject().toString());
            }
        });
    }

}
