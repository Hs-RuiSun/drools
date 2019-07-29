package com.ruby.drools.config;

import org.apache.commons.io.FilenameUtils;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.File;

@Configuration
public class DroolConfig {
	private static final String RULES_PATH = "rules/";

	@Bean KieServices kieServices(){
		return KieServices.Factory.get();
	}

	@Bean
	public KieFileSystem kieFileSystem(KieServices kieServices, @Value("${rule.xls.path:src/main/resources/rules/}") String path) {
		File[] files = (new File(path)).listFiles();
		if(files.length == 0){
			throw new RuntimeException("no files found under " + path);
		}
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		SpreadsheetCompiler compiler = new SpreadsheetCompiler();
		for(File file : files){
			Resource resource = ResourceFactory.newFileResource(file);
			String type = FilenameUtils.getExtension(file.getPath());
			if(type.equals("xls") || type.equals("xlsx")){
				kieFileSystem.write(path + file.getName() + ".drl", compiler.compile(resource, InputType.XLS));
			}
			else if(type.equals("csv")){
				kieFileSystem.write(path + file.getName() + ".drl", compiler.compile(resource, InputType.CSV));
			}
			else {
				throw new RuntimeException("invalid spreadsheet file types found under " + path);
			}
		}
		return kieFileSystem;
	}

	@Bean
	public KieContainer kieContainer(KieServices kieServices, KieFileSystem kieFileSystem){
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
		KieRepository kieRepository = kieServices.getRepository();
		kieRepository.addKieModule(kieBuilder.getKieModule());
		return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
	}

	@Bean
	public KieSession kieSession(KieContainer kieContainer) {
		return kieContainer.newKieSession();
	}
}
