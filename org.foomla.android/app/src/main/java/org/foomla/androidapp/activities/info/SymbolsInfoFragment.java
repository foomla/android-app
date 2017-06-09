package org.foomla.androidapp.activities.info;



public class SymbolsInfoFragment extends BaseInfoFragment {

	@Override
	protected String getUrl() {
		return getBaseInfoUrl() + "symbols" + getSuffix();
	}

}
