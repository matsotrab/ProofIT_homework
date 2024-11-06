package com.proofit.task.service;

import com.proofit.task.model.Bicycle;
import com.proofit.task.model.Bicycles;
import com.proofit.task.model.PremiumResponse;
import com.proofit.task.model.PremiumResponseRisks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class CalculateServiceTest {

    @Autowired
    CalculateService calculateService;

    private static Stream<Arguments> getBicyclesSrc_PremiumResponseRisks() {
        Bicycle bicycle = new Bicycle();
        bicycle.setMake("Pearl");
        bicycle.setCoverage(Bicycle.CoverageEnum.EXTRA);
        bicycle.setSumInsured(1000d);
        bicycle.setModel("Gravel SL EVO");
        bicycle.setManufactureYear(2015);
        bicycle.setRisks(List.of("THEFT", "DAMAGE", "THIRD_PARTY_DAMAGE"));

        return Stream.of(
                Arguments.of("THEFT", bicycle, 1000d, 30d),
                Arguments.of("DAMAGE", bicycle, 500d, 6.95d),
                Arguments.of("THIRD_PARTY_DAMAGE", bicycle, 100d, 12d));
    }

    @ParameterizedTest
    @MethodSource("getBicyclesSrc_PremiumResponseRisks")
    public void testGetPremiumResponseRisks(String risk, Bicycle bicycle, double sumInsured, double premium){
        PremiumResponseRisks premiumResponseRisks = calculateService.getPremiumResponseRisks(risk, bicycle);
        Assertions.assertEquals(sumInsured, premiumResponseRisks.getSumInsured());
        Assertions.assertEquals(premium, premiumResponseRisks.getPremium());
        Assertions.assertEquals(risk, premiumResponseRisks.getRiskType());
    }

    @ParameterizedTest
    @MethodSource("getBicyclesSrc_PremiumResponse")
    public void testGetPremiumResponse(List<Bicycle> bicyclesList, double expectedPremium){
        Bicycles bicycles = new Bicycles();
        bicycles.setBicycles(bicyclesList);
        PremiumResponse premiumResponse = calculateService.getResponse(bicycles);
        Assertions.assertEquals(expectedPremium, premiumResponse.getPremium());
    }

    private static Stream<Arguments> getBicyclesSrc_PremiumResponse() {
        Bicycle bicycle_1 = new Bicycle();
        bicycle_1.setMake("Pearl");
        bicycle_1.setCoverage(Bicycle.CoverageEnum.EXTRA);
        bicycle_1.setSumInsured(1000d);
        bicycle_1.setModel("Gravel SL EVO");
        bicycle_1.setManufactureYear(2015);
        bicycle_1.setRisks(List.of("THEFT", "DAMAGE", "THIRD_PARTY_DAMAGE"));

        Bicycle bicycle_2 = new Bicycle();
        bicycle_2.setMake("Sensa");
        bicycle_2.setCoverage(Bicycle.CoverageEnum.STANDARD);
        bicycle_2.setSumInsured(225d);
        bicycle_2.setModel("V2");
        bicycle_2.setManufactureYear(2020);
        bicycle_2.setRisks(List.of("DAMAGE"));

        Bicycle bicycle_3 = new Bicycle();
        bicycle_3.setMake("OTHER");
        bicycle_3.setCoverage(Bicycle.CoverageEnum.STANDARD);
        bicycle_3.setSumInsured(200d);
        bicycle_3.setModel("OTHER");
        bicycle_3.setManufactureYear(2014);
        bicycle_3.setRisks(List.of("DAMAGE", "THIRD_PARTY_DAMAGE"));

        return Stream.of(
                Arguments.of(List.of(bicycle_1, bicycle_2, bicycle_3), 77.73d));
    }
}