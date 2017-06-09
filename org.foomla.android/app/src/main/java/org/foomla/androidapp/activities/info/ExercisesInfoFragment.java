package org.foomla.androidapp.activities.info;



public class ExercisesInfoFragment extends BaseInfoFragment {

	@Override
	protected String getUrl() {
		return getBaseInfoUrl() + "exercises" + getSuffix();
	}
}
