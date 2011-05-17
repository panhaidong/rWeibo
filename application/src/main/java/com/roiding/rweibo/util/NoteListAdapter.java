package com.roiding.rweibo.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roiding.rweibo.R;
import com.roiding.rweibo.data.Note;

public class NoteListAdapter extends BaseAdapter {
    private final List<Note> noteList;
    private Context context;
    private List<View> viewList;

    public NoteListAdapter(Context context, List<Note> list) {
        this.context = context;
        this.noteList = list;
        viewList = new ArrayList<View>(list.size());
        for (int i = 0; i < noteList.size(); i++)
            viewList.add(null);
    }

    private View buildView(Note n) {

        RelativeLayout v_note = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_note, null);

        TextView tv_nickname = (TextView) v_note.findViewById(R.id.tv_userName);
        TextView tv_text = (TextView) v_note.findViewById(R.id.tv_noteText);
        TextView tv_time = (TextView) v_note.findViewById(R.id.tv_noteCreateAt);
        ImageView iv_icon_user = (ImageView) v_note.findViewById(R.id.iv_userImage);

        tv_nickname.setText(n.user.screenName);
        tv_nickname.getPaint().setFakeBoldText(true);

        tv_time.setText(UserUtils.getTime(n.createdAt));

        AsyncImageManager.loadDrawable(n.user.profileImageUrl,
                new AsyncImageManager.DefaultImageCallback(iv_icon_user), context);
        tv_text.setText(Html.fromHtml(n.text));

        if (n.picThumbnail != null && n.picThumbnail.length() > 0) {
            v_note.findViewById(R.id.iv_noteMediaIcon).setVisibility(View.VISIBLE);
            ImageView iv_photo = (ImageView) v_note.findViewById(R.id.iv_noteImage);
            iv_photo.setVisibility(View.VISIBLE);
            AsyncImageManager.loadDrawable(n.picThumbnail, new AsyncImageManager.DefaultImageCallback(iv_photo),
                    context);
        }

        if (n.forwardNote != null) {
            Note nf = n.forwardNote;
            ViewGroup v_forward_note = (ViewGroup) v_note.findViewById(R.id.vg_noteForward);
            v_forward_note.setVisibility(View.VISIBLE);

            TextView tv_forward_text = (TextView) v_note.findViewById(R.id.tv_noteForwardText);

            tv_forward_text.setText(Html.fromHtml(nf.user.screenName) + "：");
            tv_forward_text.append(Html.fromHtml(nf.text));
            // tv_text.append(Html.fromHtml("<br><bold>" +
            // nf.getUser().getNickname() + "</bold>：" + nf.getText()));
            if (nf.picThumbnail != null && nf.picThumbnail.length() > 0) {
                v_note.findViewById(R.id.iv_noteMediaIcon).setVisibility(View.VISIBLE);
                ImageView iv_photo = (ImageView) v_note.findViewById(R.id.iv_noteForwardImage);
                iv_photo.setVisibility(View.VISIBLE);
                AsyncImageManager.loadDrawable(nf.picThumbnail, new AsyncImageManager.DefaultImageCallback(iv_photo),
                        context);
            }
        }

        return v_note;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = viewList.get(position);
        if (v == null) {
            v = buildView(noteList.get(position));
            viewList.set(position, v);

        }
        return v;
    }

    public int getCount() {
        return noteList.size();
    }

    public Object getItem(int position) {
        return noteList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
}
