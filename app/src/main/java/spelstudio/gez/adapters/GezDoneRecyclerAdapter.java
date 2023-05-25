package spelstudio.gez.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import spelstudio.gez.R;
import spelstudio.gez.interfaces.FriendListItemClickListener;
import spelstudio.gez.interfaces.RecyclerFooterItemClickListener;
import spelstudio.gez.interfaces.RecyclerViewPositionChangeListener;
import spelstudio.gez.webapi.Responses.ArrayFriendsDone;

public class GezDoneRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RadioGroup.OnCheckedChangeListener {
    View mView;
    Activity mContext;
    List<ArrayFriendsDone> items;
    private static final int TYPE_ITEM = 1;
    RecyclerFooterItemClickListener mFooterListener;
    private static final int TYPE_FOOTER = 2;
    GezDoneEmojiSelectedListener mEmojiSelectedListener;
    RecyclerViewPositionChangeListener mPositionlistener;
    FriendListItemClickListener mFrndItemClickListener;

    public GezDoneRecyclerAdapter(Activity mContext, List<ArrayFriendsDone> items, RecyclerViewPositionChangeListener mPositionlistener, RecyclerFooterItemClickListener mFooterListener) {
        this.mPositionlistener = mPositionlistener;
        this.mContext = mContext;
        this.items = items;
        this.mFooterListener = mFooterListener;
    }

    public int getItemSize() {
        return items.size() + 1;
    }

    public void setEmojiSelectedListener(GezDoneEmojiSelectedListener mEmojiSelectedListener) {
        this.mEmojiSelectedListener = mEmojiSelectedListener;
    }

    public void setFrndItemClickListener(FriendListItemClickListener mFrndItemClickListener) {
        this.mFrndItemClickListener = mFrndItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.gez_done_recycler_item, parent, false);
            this.mView = mView;
            ViewHolder mViewHolder = new ViewHolder(mView);
            return mViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.gez_opinion_footer_recycler_item, parent, false);
            FooterViewHolder mViewHolder = new FooterViewHolder(mView);
            return mViewHolder;
        } else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            final ViewHolder mViewHolder = (ViewHolder) holder;
            //todo match selected emoti with users and rate tell user right or wrong
            /*String SelectedEmoji = items.get(position).getFriendSelected();
            if (Integer.parseInt(items.get(position).getUserSelected()) > 0) {
                PlayActivity.GezDoneEmojiCounter++;
                setSelectedUser(mViewHolder.mEmojiGroup, Integer.parseInt(items.get(position).getUserSelected()));
            }*/
            final ArrayFriendsDone obj = items.get(position);

            if (obj.getUserSelected() == -1)
                mViewHolder.mEmojiGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        switch (i) {
                            case R.id.item_emoji_1: {
                                obj.setUserSelected("1");
                                setSelected(radioGroup, 0, 1, position, mViewHolder);
                                break;
                            }
                            case R.id.item_emoji_2: {
                                obj.setUserSelected("2");
                                setSelected(radioGroup, 1, 2, position, mViewHolder);
                                break;
                            }

                            case R.id.item_emoji_3: {
                                obj.setUserSelected("3");
                                setSelected(radioGroup, 2, 3, position, mViewHolder);
                                break;
                            }
                            case R.id.item_emoji_4: {
                                obj.setUserSelected("4");
                                setSelected(radioGroup, 3, 4, position, mViewHolder);
                                break;
                            }
                            case R.id.item_emoji_5: {
                                obj.setUserSelected("5");
                                setSelected(radioGroup, 4, 5, position, mViewHolder);
                                break;
                            }
                            case R.id.item_emoji_6: {
                                obj.setUserSelected("6");
                                setSelected(radioGroup, 5, 6, position, mViewHolder);
                                break;
                            }

                        }
                        if (position < items.size() + 2) {
                            mPositionlistener.onPositionFound(position);
                        }
                    }
                });
            Glide.with(mContext).load(obj.getFriendPicUrl()).into(mViewHolder.mProfileImg);
            mViewHolder.mProfileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mFrndItemClickListener != null) {
                        mFrndItemClickListener.onFriendItemClicked(obj.getFriendID());
                    }
                }
            });

            if (obj.getUserSelected() >= 0 && obj.getUserSelected() == obj.getFriendSelected()) {
                mViewHolder.frndStatusImg.setImageResource(R.drawable.won);
                setResult(mViewHolder.mEmojiGroup, obj, mViewHolder);
                mEmojiSelectedListener = null;

            } else if (obj.getUserSelected() >= 0 && obj.getUserSelected() != obj.getFriendSelected()) {
                setResult(mViewHolder.mEmojiGroup, obj, mViewHolder);
                mViewHolder.frndStatusImg.setImageResource(R.drawable.lost);
                mEmojiSelectedListener = null;
            } else {
                mViewHolder.frndStatusImg.setVisibility(View.GONE);
            }

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder mFooterHolder = (FooterViewHolder) holder;
            mFooterHolder.listGezBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mFooterListener != null) {
                        mFooterListener.onFooterClicked();
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == items.size() + 1 || position == items.size() || items.size() == 0) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    private void setSelected(RadioGroup radioGroup, int selectedChild, int emojiPosition, int itemPosition, ViewHolder mViewHolder) {

        for (int i = 0; i < 6; i++) {
            if (i != selectedChild) {
                radioGroup.getChildAt(i).setEnabled(false);
            }
        }
        if (selectedChild == items.get(itemPosition).getFriendSelected()) {
            if (mEmojiSelectedListener != null) {
                mEmojiSelectedListener.setGezDoneFriend(1, items.get(itemPosition).getFriendID(), emojiPosition, itemPosition);
                mViewHolder.frndStatusImg.setVisibility(View.VISIBLE);
                items.get(itemPosition).mUserWonStatus = 1;
                mViewHolder.frndStatusImg.setImageResource(R.drawable.won);
                radioGroup.getChildAt(emojiPosition).setEnabled(true);
                notifyDataSetChanged();
            }
        } else {
            mEmojiSelectedListener.setGezDoneFriend(0, items.get(itemPosition).getFriendID(), emojiPosition, itemPosition);
            mViewHolder.frndStatusImg.setVisibility(View.VISIBLE);
            mViewHolder.frndStatusImg.setImageResource(R.drawable.lost);
            radioGroup.getChildAt(selectedChild).setActivated(true);
            radioGroup.getChildAt(items.get(itemPosition).getFriendSelected()).setEnabled(true);
            items.get(itemPosition).mUserWonStatus = 0;
            radioGroup.getChildAt(items.get(itemPosition).getFriendSelected()).setEnabled(true);
            notifyDataSetChanged();
        }
    }

    private void setResult(RadioGroup radioGroup, ArrayFriendsDone obj, ViewHolder mViewHolder) {

        int friendSelected = obj.getFriendSelected();
        int userSelected = obj.getUserSelected();

        for (int i = 0; i < 6; i++) {
            if (i != (friendSelected)) {
                radioGroup.getChildAt(i).setEnabled(false);
            }
        }

        if (obj.getUserSelected() >= 0 && obj.getUserSelected() == obj.getFriendSelected()) {
            radioGroup.getChildAt(friendSelected).setEnabled(true);
            mViewHolder.frndStatusImg.setVisibility(View.VISIBLE);
            mViewHolder.frndStatusImg.setImageResource(R.drawable.won);
        } else if (obj.getUserSelected() >= 0 && obj.getUserSelected() != obj.getFriendSelected()) {
            radioGroup.getChildAt(friendSelected).setEnabled(true);
            radioGroup.getChildAt(userSelected).setActivated(true);
            mViewHolder.frndStatusImg.setVisibility(View.VISIBLE);
            mViewHolder.frndStatusImg.setImageResource(R.drawable.lost);
        }
    }

    private void setSelectedUser(RadioGroup radioGroup, int selectedChild) {
        for (int i = 0; i < 6; i++) {
            if (i != selectedChild) {
                radioGroup.getChildAt(i).setEnabled(false);
            }
        }
    }

    public interface GezDoneEmojiSelectedListener {
        public void setGezDoneFriend(int userpoint, String friendId, int emojiSelected, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mProfileImg, frndStatusImg;
        RadioGroup mEmojiGroup;
        RadioButton mBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            mBtn = (RadioButton) itemView.findViewById(R.id.item_emoji_1);
            mProfileImg = (ImageView) itemView.findViewById(R.id.gez_open_frnd_pic);
            frndStatusImg = (ImageView) itemView.findViewById(R.id.recycler_friend_status);
            mEmojiGroup = (RadioGroup) itemView.findViewById(R.id.gez_open_emoji_group);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        ImageButton listGezBtn;

        public FooterViewHolder(View itemView) {
            super(itemView);
            listGezBtn = (ImageButton) itemView.findViewById(R.id.list_gez_btn);
        }
    }
}
