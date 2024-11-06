package com.proofit.task;

import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Configuration
public class AppConfig {

    @Value("${groovyScriptsDir}")
    private String scriptsDir;

    @Bean
    public GroovyScriptEngine getGroovyScriptEngine() {
        try {
            GroovyScriptEngine groovyScriptEngine = new GroovyScriptEngine(ResourceUtils.getFile(scriptsDir).getAbsolutePath(), this.getClass().getClassLoader());
            CompilerConfiguration compilerConfiguration = groovyScriptEngine.getConfig();
            compilerConfiguration.setScriptBaseClass("BaseScript");
            groovyScriptEngine.setConfig(compilerConfiguration);

            return groovyScriptEngine;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Autowired
    @Qualifier("baseScript")
    public GroovyObject getBaseScript(GroovyScriptEngine groovyScriptEngine){
        Class<GroovyObject> baseScriptClass = null;
        try {
            //noinspection unchecked
            baseScriptClass = groovyScriptEngine.loadScriptByName("BaseScript.groovy");

        } catch (ResourceException | ScriptException e) {
            throw new RuntimeException(e);
        }
        try {
            return baseScriptClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
