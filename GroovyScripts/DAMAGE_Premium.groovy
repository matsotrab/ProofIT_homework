import com.proofit.task.model.Bicycle

Closure riskBasePremiumCriteria = { it.RISK_TYPE == 'DAMAGE'}

def riskBasePremium = riskBasePremiumData.find(riskBasePremiumCriteria).PREMIUM

BigDecimal premiumDefScale = riskBasePremium*sumInsuredFactor*ageFactor

premium = premiumDefScale.round(2)


private BigDecimal getSumInsuredFactor(){
    Closure sumInsuredFactorDataCriteria = {riskSumInsured >= it.VALUE_FROM && riskSumInsured <= it.VALUE_TO}

    BigDecimal factorMin = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).FACTOR_MIN
    BigDecimal factorMax = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).FACTOR_MAX
    BigDecimal valueFrom = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).VALUE_FROM
    BigDecimal valueTo = sumInsuredFactorData.find(sumInsuredFactorDataCriteria).VALUE_TO

    return factorMax - (factorMax - factorMin) * (valueTo - riskSumInsured) / (valueTo - valueFrom)
}

private BigDecimal getAgeFactor(){
    def model = bicycle.getModel()
    def make = bicycle.getMake()
    def manufactureYear = bicycle.getManufactureYear()
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    int age = currentYear - manufactureYear -1

    Closure ageFactorDataCriteria1 = {
        make == it.MAKE &&
                model == it.MODEL &&
                age >= it.VALUR_FROM && age <= it.VALUE_TO
    }
    Closure ageFactorDataCriteria2 = {
        make == it.MAKE &&
                age >= it.VALUR_FROM && age <= it.VALUE_TO
    }
    Closure ageFactorDataCriteria3 = {
        it.MAKE==null && age >= it.VALUR_FROM && age <= it.VALUE_TO
    }

    def filteredAgeFactorData = ageFactorData.find(ageFactorDataCriteria1)
    filteredAgeFactorData = filteredAgeFactorData? filteredAgeFactorData : ageFactorData.find(ageFactorDataCriteria2)
    filteredAgeFactorData = filteredAgeFactorData? filteredAgeFactorData : ageFactorData.find(ageFactorDataCriteria3)

    def factorMin = filteredAgeFactorData.FACTOR_MIN
    def factorMax = filteredAgeFactorData.FACTOR_MAX
    def valueFrom = filteredAgeFactorData.VALUE_FROM
    def valueTo = filteredAgeFactorData.VALUE_TO

    def factor = factorMax - (factorMax - factorMin) * (valueTo - age) / (valueTo - valueFrom)
    return  factor
}
