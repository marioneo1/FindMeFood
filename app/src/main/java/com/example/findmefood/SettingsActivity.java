package com.example.findmefood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SettingsActivity extends PreferenceActivity {
    private static final String TAG = SettingsActivity.class.getName();
    private static final String ARRAY_NAME_ENTRIES = "Entries";
    private static final String ARRAY_NAME_VALUES = "Values";

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof MultiSelectListPreference) {
                MultiSelectListPreference mSListPreference = (MultiSelectListPreference) preference;
                int index = mSListPreference.findIndexOfValue(stringValue);

                preference.setSummary(
                        index >= 0
                                ? mSListPreference.getEntries()[index]
                                : null);

            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
            loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || BlacklistPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    public static class BlacklistPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_blacklist);
            setHasOptionsMenu(true);
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            final SharedPreferences.Editor editor = prefs.edit();
            Preference button = findPreference("clear_blacklist");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    editor.clear().commit();
                    getActivity().finish();
                    return true;
                }
            });

            final MultiSelectListPreference mSListPreference = (MultiSelectListPreference) findPreference("multi_select_list_preference_1");
            final List<CharSequence> entries = new ArrayList<CharSequence>();
            final List<CharSequence> entryValues = new ArrayList<CharSequence>();
            try{
                JSONArray json_array_blacklist = new JSONArray(prefs.getString(ARRAY_NAME_ENTRIES,"[]"));
                JSONArray json_array_blacklist_values = new JSONArray(prefs.getString(ARRAY_NAME_VALUES,"[]"));

                for (int i = 0; i < json_array_blacklist.length(); i++){
                    entries.add((CharSequence)json_array_blacklist.get(i));
                    entryValues.add((CharSequence)json_array_blacklist_values.get(i));
                }

            }
            catch (JSONException e){
                Log.d(TAG, e.toString());
            }

            Log.d(TAG, "Entry:" + entries.toString());
            Log.d(TAG, "Value:" + entryValues.toString());

            mSListPreference.setEntries(entries.toArray(new CharSequence[]{}));
            mSListPreference.setEntryValues(entryValues.toArray(new CharSequence[]{}));

            mSListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) preference;

                    //Get index of selected entries and remove from Array.
                    for(String entry: (HashSet<String>) newValue){
                        Log.d(TAG, "NEW VALUE" + entry);
                        int index = multiSelectListPreference.findIndexOfValue(entry);
                        Log.d(TAG, "Index of value " + index);
                        entries.remove(index);
                        entryValues.remove(index);
                    }

                    //Update UI with removed entries
                    mSListPreference.setEntries(entries.toArray(new CharSequence[]{}));
                    mSListPreference.setEntryValues(entryValues.toArray(new CharSequence[]{}));

                    //Update shared preferences with removed entries
                    JSONArray json_array_blacklist = new JSONArray(entries);
                    JSONArray json_array_blacklist_values = new JSONArray(entryValues);
                    editor.putString(ARRAY_NAME_ENTRIES,json_array_blacklist.toString());
                    editor.putString(ARRAY_NAME_VALUES,json_array_blacklist_values.toString());
                    editor.commit();

                    return false;
                }
            });
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
