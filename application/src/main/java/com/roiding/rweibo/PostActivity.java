package com.roiding.rweibo;

import android.os.Bundle;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;

import com.roiding.rweibo.data.Note;

public class PostActivity extends AbstractActivity {
    private MultiAutoCompleteTextView mactv_noteText;
    private String action;
    private Note note;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_post);
        action = getIntent().getAction();
        note = (Note) getIntent().getSerializableExtra(Constants.KEY_NOTE);
        mactv_noteText = (MultiAutoCompleteTextView) findViewById(R.id.mactv_noteText);

    }

    public void onClickHandler(View v) {

        if (v.getId() == R.id.btn_submit) {
            String text = mactv_noteText.getText().toString();
            try {
                if (Constants.ACTION_REPLY.equals(action)) {
                    provider.comment(text, note.id);
                } else if (Constants.ACTION_RETWEET.equals(action)) {
                    provider.repost(text, note.id);
                } else {
                    provider.update(text);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }

    }
}
