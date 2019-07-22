package com.cgi.droolssandbox;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolConfig {
	
	@Bean KieServices kieServices(){
		return KieServices.Factory.get();
	}
	
	@Bean
	public ReleaseId releaseId(){
		return new ReleaseIdImpl("com.cgi.droolssandbox", "SandBox", "1.0.0-1");
	}
	
	@Bean
	public Resource resource(@Value("${rule.xls.path:src/main/resources/xls/decision_table_example.xls}") String xlsPath){
		return ResourceFactory.newFileResource(xlsPath);
	}
	
	@Bean
	public InternalKieModule internalKieModule(ReleaseId releaseId,
			KieServices kieServices, Resource resource) {
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        String drlContent = compiler.compile(resource, InputType.XLS);
		System.out.println(drlContent);
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.generateAndWritePomXML(releaseId);
        kfs.write("src/main/resources/GenRulesContent.drl", drlContent);
        System.out.println(drlContent);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        return (InternalKieModule) kieBuilder.getKieModule();
	}
	
	@Bean
	public KieContainer kieContainer(KieServices kieServices, InternalKieModule internalKieModule, ReleaseId releaseId){
		kieServices.getRepository().addKieModule(internalKieModule);
        return kieServices.newKieContainer(releaseId);
	}

	@Bean
	public KieSession kieSession(KieContainer kieContainer) {
		return kieContainer.newKieSession();
	}
}
