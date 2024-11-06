    Closure riskBasePremiumCriteria = { it.RISK_TYPE == 'THEFT'}
    Closure sumInsuredFactorDataCriteria = {riskSumInsured >= it.VALUE_FROM && riskSumInsured <= it.VALUE_TO}

    BigDecimal factorMin = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).FACTOR_MIN
    BigDecimal factorMax = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).FACTOR_MAX
    BigDecimal valueFrom = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).VALUE_FROM
    BigDecimal valueTo = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).VALUE_TO

    def riskBasePremium = riskBasePremiumData.find(riskBasePremiumCriteria).PREMIUM
    def sumInsuredFactor = factorMax - (factorMax - factorMin) * (valueTo - riskSumInsured) / (valueTo - valueFrom)

    premium = riskBasePremium*sumInsuredFactor
