package com.roiding.rweibo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.roiding.rweibo.data.Note;
import com.roiding.rweibo.data.User;
import com.roiding.rweibo.util.NoteListAdapter;

public class UserActivity extends AbstractActivity {
    private ProgressBar pb_progress;
    private ListView lv_list;
    private List<Note> notes = new ArrayList<Note>();
    private User user;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user);

        findViewById(R.id.iv_navImage).setVisibility(View.GONE);
        pb_progress = (ProgressBar) findViewById(android.R.id.progress);
        lv_list = (ListView) findViewById(android.R.id.list);


        user = (User) getIntent().getSerializableExtra(Constants.KEY_USER);

        findViewById(R.id.tv_navTitle).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_navTitle)).setText(user.screenName);

        RelativeLayout userTab = (RelativeLayout) getLayoutInflater().inflate(R.layout.icon_tab_indicator, null);
        ((ImageView) userTab.findViewById(R.id.icon)).setImageResource(R.drawable.ic_tab_profile);
        RelativeLayout timelineTab = (RelativeLayout) getLayoutInflater().inflate(R.layout.icon_tab_indicator, null);
        ((ImageView) timelineTab.findViewById(R.id.icon)).setImageResource(R.drawable.ic_tab_timeline);
        RelativeLayout mentionsTab = (RelativeLayout) getLayoutInflater().inflate(R.layout.icon_tab_indicator, null);
        ((ImageView) mentionsTab.findViewById(R.id.icon)).setImageResource(R.drawable.ic_tab_mentions);
        RelativeLayout listsTab = (RelativeLayout) getLayoutInflater().inflate(R.layout.icon_tab_indicator, null);
        ((ImageView) listsTab.findViewById(R.id.icon)).setImageResource(R.drawable.ic_tab_lists);


        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabContentFactory factory = new TabHost.TabContentFactory() {
            public View createTabContent(String tabId) {
                Log.d(Constants.LOG_TAG, "createTabContent.." + tabId);
                return lv_list;
            }
        };


        tabHost.addTab(tabHost.newTabSpec(Constants.KEY_TAB_DMS).setIndicator(userTab).setContent(factory));
        tabHost.addTab(tabHost.newTabSpec(Constants.KEY_TAB_TIMELINE).setIndicator(timelineTab).setContent(factory));
        tabHost.addTab(tabHost.newTabSpec(Constants.KEY_TAB_METIONS).setIndicator(mentionsTab).setContent(factory));
        tabHost.addTab(tabHost.newTabSpec(Constants.KEY_TAB_LISTS).setIndicator(listsTab).setContent(factory));

        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(final String tabId) {
                Log.d(Constants.LOG_TAG, "onTabChanged.." + tabId);
                // display(userId, loadingView, lv, tabId);
                display(tabId);
            }
        });
    }

    private void display(final String tabKey) {
        new AsyncTask<Void, Void, List<Note>>() {
            @Override
            protected void onPreExecute() {
                notes.clear();
                pb_progress.setVisibility(View.VISIBLE);
                lv_list.setAdapter(new NoteListAdapter(UserActivity.this, Collections.<Note> emptyList()));
            }

            @Override
            protected List<Note> doInBackground(Void... v) {
                try {
                    List<Note> notes = new ArrayList<Note>();
                    notes = getNotes(tabKey);

                    return notes;
                } catch (Exception e) {
                    e.printStackTrace();
                    error = e;

                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Note> mNotes) {
                pb_progress.setVisibility(View.GONE);

                if (error != null)
                    showDialog(Constants.DIALOG_ERROR);

                if (mNotes == null)
                    return;

                notes.clear();
                notes.addAll(mNotes);
                lv_list.setAdapter(new NoteListAdapter(UserActivity.this, notes));
            }

        }.execute();
    }


    private List<Note> getNotes(String tabKey) throws OAuthMessageSignerException, OAuthExpectationFailedException,
            OAuthCommunicationException, ClientProtocolException, JSONException, IOException {
        if (Constants.KEY_TAB_TIMELINE.equals(tabKey))
            return provider.getUserTimeline(user.id);
        else if (Constants.KEY_TAB_METIONS.equals(tabKey))
            return provider.getMentions();

        return null;
    }

}
