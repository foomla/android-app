package org.foomla.androidapp.activities.info;



public class ImprintInfoFragment extends BaseInfoFragment {

    @Override
    protected String getUrl() {
        return getBaseInfoUrl() + "imprint" + getSuffix();
    }

}
