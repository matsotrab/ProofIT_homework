import com.proofit.task.model.Bicycle

Closure riskBasePremiumCriteria = { it.RISK_TYPE == 'THIRD_PARTY_DAMAGE'}

def riskBasePremium = riskBasePremiumData.find(riskBasePremiumCriteria).PREMIUM

BigDecimal premiumDefScale = riskBasePremium * sumInsuredFactor * riskCountFactor

premium = premiumDefScale.round(2)


private BigDecimal getSumInsuredFactor(){
    Closure sumInsuredFactorDataCriteria = {riskSumInsured >= it.VALUE_FROM && riskSumInsured <= it.VALUE_TO}

    BigDecimal factorMin = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).FACTOR_MIN
    BigDecimal factorMax = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).FACTOR_MAX
    BigDecimal valueFrom = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).VALUE_FROM
    BigDecimal valueTo = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).VALUE_TO

    return factorMax - (factorMax - factorMin) * (valueTo - riskSumInsured) / (valueTo - valueFrom)
}

private BigDecimal getRiskCountFactor(){

    int riskCount = bicycle.getRisks().size()

    Closure riskCountFactorDataCriteria1 = {
        riskCount >= it.VALUR_FROM && riskCount <= it.VALUE_TO
    }

    def filteredRiskCountFactorData = riskCountFactorData.find(riskCountFactorDataCriteria1)

    def factorMin = filteredRiskCountFactorData.FACTOR_MIN
    def factorMax = filteredRiskCountFactorData.FACTOR_MAX
    def valueFrom = filteredRiskCountFactorData.VALUE_FROM
    def valueTo = filteredRiskCountFactorData.VALUE_TO

    def factor = factorMax - (factorMax - factorMin) * (valueTo - riskCount) / (valueTo - valueFrom)
    return  factor
}
