package com.roiding.rweibo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roiding.rweibo.data.Note;
import com.roiding.rweibo.util.AsyncImageManager;
import com.roiding.rweibo.util.UserUtils;

public class NoteActivity extends AbstractActivity {
    private Note note = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_note);

        note = (Note) getIntent().getSerializableExtra(Constants.KEY_NOTE);
        ((TextView) findViewById(R.id.tv_userScreenName)).setText("@" + note.user.screenName);
        ((TextView) findViewById(R.id.tv_userScreenName)).getPaint().setFakeBoldText(true);
        ((TextView) findViewById(R.id.tv_userDomain)).setText("http://www.weibo.com/" + note.user.domain);
        ((TextView) findViewById(R.id.tv_noteText)).setText(note.text);
        ((TextView) findViewById(R.id.tv_navTitle)).setText(note.user.screenName);
        ((TextView) findViewById(R.id.tv_noteSource)).setText(Html.fromHtml(UserUtils.getCompleteTime(note.createdAt)
                + " " + note.source));
        findViewById(R.id.tv_navTitle).setVisibility(View.VISIBLE);

        AsyncImageManager.loadDrawable(note.user.profileImageUrl, new AsyncImageManager.DefaultImageCallback(
                ((ImageView) findViewById(R.id.iv_userImage))), NoteActivity.this);

        if (note.picThumbnail != null && note.picThumbnail.length() > 0) {
            ImageView iv_photo = (ImageView) findViewById(R.id.iv_noteImage);
            iv_photo.setVisibility(View.VISIBLE);
            AsyncImageManager.loadDrawable(note.picBmiddle, new AsyncImageManager.DefaultImageCallback(iv_photo), this);
        }

        findViewById(R.id.spacer).setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.vg_userBadge).dispatchTouchEvent(event);
                return false;
            }
        });

        if (note.forwardNote != null) {
            Note nf = note.forwardNote;
            ViewGroup v_forward_note = (ViewGroup) findViewById(R.id.vg_noteForward);
            v_forward_note.setVisibility(View.VISIBLE);

            TextView tv_forward_text = (TextView) findViewById(R.id.tv_noteForwardText);

            tv_forward_text.setText(Html.fromHtml(nf.user.screenName) + "：");
            tv_forward_text.append(Html.fromHtml(nf.text));
            // tv_text.append(Html.fromHtml("<br><bold>" +
            // nf.getUser().getNickname() + "</bold>：" + nf.getText()));
            if (nf.picThumbnail != null && nf.picThumbnail.length() > 0) {
                ImageView iv_photo = (ImageView) findViewById(R.id.iv_noteForwardImage);
                iv_photo.setVisibility(View.VISIBLE);
                AsyncImageManager.loadDrawable(nf.picBmiddle, new AsyncImageManager.DefaultImageCallback(iv_photo),
                        this);
            }
        }

    }

    public void onClickHandler(View v) {

        if (v.getId() == R.id.vg_userBadge) {
            Intent intent = new Intent();
            intent.setClass(NoteActivity.this, UserActivity.class);
            intent.putExtra(Constants.KEY_USER, note.user);
            startActivity(intent);
        } else if (v.getId() == R.id.ibtn_reply) {
            Intent intent = new Intent(Constants.ACTION_REPLY);
            intent.putExtra(Constants.KEY_NOTE, note);
            intent.setClass(this, PostActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.ibtn_retweet) {
            Intent intent = new Intent(Constants.ACTION_RETWEET);
            intent.putExtra(Constants.KEY_NOTE, note);
            intent.setClass(this, PostActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.ibtn_favorite) {
        } else if (v.getId() == R.id.ibtn_share) {
        }
    }
}
