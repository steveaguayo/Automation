package kimble.automation.domain.mobile.builders;

import kimble.automation.domain.mobile.TaxCodeMob;
import kimble.automation.domain.mobile.general.Deserializers;

public class TaxCodeBuilder extends EntityBuilderA<TaxCodeBuilder, TaxCodeMob> {

	public TaxCodeBuilder() throws Exception {
		e = new TaxCodeMob();
		e.setId(Deserializers.generateId());
		e.setName(DefaultValues.taxCodeName);
	}
	
	public TaxCodeBuilder withName(String aName) { 
		e.setName(aName);
		return this;
	}

	public TaxCodeMob build() throws Exception { return e; }
	
}
