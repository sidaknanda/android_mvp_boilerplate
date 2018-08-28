package co.sn.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.sn.di.ApplicationContext;
import co.sn.di.PreferenceInfo;

@Singleton
public class PreferencesHelperImpl implements PreferencesHelper {

    private final SharedPreferences mPrefs;
    public static final String PREF_NAME = "prefs";

    @Inject
    public PreferencesHelperImpl(@ApplicationContext Context context,
                                 @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }
}
