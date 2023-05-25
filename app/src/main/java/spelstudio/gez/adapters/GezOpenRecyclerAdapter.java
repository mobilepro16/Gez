package spelstudio.gez.adapters;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import spelstudio.gez.R;
import spelstudio.gez.activities.PlayActivity;
import spelstudio.gez.interfaces.FriendListItemClickListener;
import spelstudio.gez.interfaces.RecyclerFooterItemClickListener;
import spelstudio.gez.interfaces.RecyclerViewPositionChangeListener;
import spelstudio.gez.webapi.Responses.ArrayFriendsOpen;

public class GezOpenRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RadioGroup.OnCheckedChangeListener {
    View mView;
    Activity mContext;
    List<ArrayFriendsOpen> items;
    private static final int TYPE_ITEM = 1;
    RecyclerFooterItemClickListener mFooterListener;
    private static final int TYPE_FOOTER = 2;
    GezOpenEmojiSelectedListener mEmojiSelectedListener;
    RecyclerViewPositionChangeListener mPositionlistener;
    FriendListItemClickListener mFrndItemClickListener;

    public GezOpenRecyclerAdapter(Activity mContext, List<ArrayFriendsOpen> items, RecyclerViewPositionChangeListener mPositionlistener, RecyclerFooterItemClickListener mFooterListener, FriendListItemClickListener mFrndItemClickListener) {
        this.mPositionlistener = mPositionlistener;
        this.mContext = mContext;
        this.items = items;
        this.mFooterListener = mFooterListener;
        this.mFrndItemClickListener = mFrndItemClickListener;
    }

    public int getItemsSize() {
        return items.size() + 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.gez_open_recycler_item, parent, false);
            this.mView = mView;
            ViewHolder mViewHolder = new ViewHolder(mView);
            return mViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.gez_opinion_footer_recycler_item, parent, false);
            this.mView = mView;
            FooterViewHolder mFooterViewHolder = new FooterViewHolder(mView);

            return mFooterViewHolder;
        } else return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            ViewHolder mViewHolder = (ViewHolder) holder;
            //  todo match selected emoti with users and rate tell user right or wrong
            if (items.get(position).getUserSelected() > 0) {
                PlayActivity.GezOpenEmojiCounter++;
                setUserSelected(mViewHolder.mEmojiGroup, items.get(position).getUserSelected() - 1);
            }
            mViewHolder.mEmojiGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch (i) {
                        case R.id.item_emoji_opn_1: {
                            setSelected(radioGroup, 0, 1, position);
                            break;
                        }
                        case R.id.item_emoji_opn_2: {
                            setSelected(radioGroup, 1, 2, position);
                            break;
                        }

                        case R.id.item_emoji_opn_3: {
                            setSelected(radioGroup, 2, 3, position);
                            break;
                        }
                        case R.id.item_emoji_opn_4: {
                            setSelected(radioGroup, 3, 4, position);
                            break;
                        }
                        case R.id.item_emoji_opn_5: {
                            setSelected(radioGroup, 4, 5, position);
                            break;
                        }
                        case R.id.item_emoji_opn_6: {
                            setSelected(radioGroup, 5, 6, position);
                            break;
                        }

                    }
                    if (position < items.size() + 2) {
                        mPositionlistener.onPositionFound(position);
                    }
                }
            });
            Glide.with(mContext).load(items.get(position).getFriendPicUrl()).into(mViewHolder.mProfileImg);
            mViewHolder.mProfileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mFrndItemClickListener != null) {
                        mFrndItemClickListener.onFriendItemClicked(items.get(position).getFriendID().toString());
                    }
                }
            });

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder mFooterHolder = (FooterViewHolder) holder;
//            Glide.with(mContext).load(R.drawable.btn_list).into(mFooterHolder.listGezBtn);
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
        if (position == items.size()) {
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

    private void setUserSelected(RadioGroup radioGroup, int selectedChild) {

        for (int i = 0; i < 6; i++) {
            if (i != selectedChild) {
                radioGroup.getChildAt(i).setEnabled(false);
            }

            radioGroup.getChildAt(i).setActivated(false);
        }
    }

    private void setSelected(RadioGroup radioGroup, int selectedChild, int emojiPosition, int itemPosition) {
        for (int i = 0; i < 6; i++) {
            if (i != selectedChild) {
                radioGroup.getChildAt(i).setEnabled(false);
            }

            radioGroup.getChildAt(i).setActivated(false);
        }
        if (mEmojiSelectedListener != null) {
            mEmojiSelectedListener.setGezOpenFriend(items.get(itemPosition).getFriendID(), emojiPosition, itemPosition);
        }
    }

    public void setmEmojiSelectedListener(GezOpenEmojiSelectedListener mEmojiSelectedListener) {
        this.mEmojiSelectedListener = mEmojiSelectedListener;
    }

    public interface GezOpenEmojiSelectedListener {
        public void setGezOpenFriend(String friendId, int emojiSelected, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mProfileImg;
        RadioGroup mEmojiGroup;

        public ViewHolder(View itemView) {
            super(itemView);
            mProfileImg = (ImageView) itemView.findViewById(R.id.gez_open_frnd_pic);
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
