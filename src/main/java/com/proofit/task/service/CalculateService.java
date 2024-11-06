package com.proofit.task.service;

import com.proofit.task.model.*;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CalculateService {

    @Autowired GroovyScriptEngine groovyScriptEngine;

    public PremiumResponse getResponse(Bicycles bicycles){
        PremiumResponse premiumResponse = new PremiumResponse();

        bicycles.getBicycles().forEach(bicycle -> {
            PremiumResponseObjects premiumResponseObjects = new PremiumResponseObjects();
            bicycle.getRisks().forEach(riskType -> {
                PremiumResponseRisks responseRisks = getPremiumResponseRisks(riskType, bicycle);
                premiumResponseObjects.addRisksItem(responseRisks);
                premiumResponseObjects.setCoverageType(switch(bicycle.getCoverage()){
                    case Bicycle.CoverageEnum.STANDARD -> {
                        yield PremiumResponseObjects.CoverageTypeEnum.STANDARD;
                    }
                    case Bicycle.CoverageEnum.EXTRA -> {
                        yield PremiumResponseObjects.CoverageTypeEnum.EXTRA;
                    }
                });
                premiumResponseObjects.setSumInsured(bicycle.getSumInsured());
                premiumResponseObjects.setPremium(premiumResponseObjects.getRisks().stream().map(PremiumResponseRisks::getPremium).mapToDouble(Double::doubleValue).sum());
                premiumResponseObjects.setAttributes(Map.of(
                        "MANUFACTURE_YEAR", bicycle.getManufactureYear().toString(),
                        "MODEL", bicycle.getModel(),
                        "MAKE", bicycle.getMake()));
            });
            premiumResponse.addObjectsItem(premiumResponseObjects);
            premiumResponse.setPremium(premiumResponse.getObjects().stream().map(PremiumResponseObjects::getPremium).mapToDouble(Double::doubleValue).sum());
        });
        return premiumResponse;
    }

    public PremiumResponseRisks getPremiumResponseRisks(String risk, Bicycle bicycle){
        PremiumResponseRisks premiumResponseRisks = new PremiumResponseRisks();
        BigDecimal riskSumInsured = null;
        BigDecimal premium = null;
        Binding sharedData = new Binding();
        sharedData.setProperty("bicycle", bicycle);
        sharedData.setProperty("riskSumInsured", riskSumInsured);
        sharedData.setProperty("premium", premium);
        try {
            groovyScriptEngine.run(risk + "_SumInsured.groovy", sharedData);
            groovyScriptEngine.run(risk + "_Premium.groovy", sharedData);
        } catch (ResourceException | ScriptException e) {
            throw new RuntimeException(e);
        }
        premiumResponseRisks.setPremium(((BigDecimal)sharedData.getProperty("premium")).doubleValue());
        premiumResponseRisks.setSumInsured(((BigDecimal)sharedData.getProperty("riskSumInsured")).doubleValue());
        premiumResponseRisks.setRiskType(risk);
        return premiumResponseRisks;
    }
}
