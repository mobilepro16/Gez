package spelstudio.gez.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import spelstudio.gez.Helpers.SharedPrefrenceHelper;
import spelstudio.gez.Helpers.UrlToVideoIdConverter;
import spelstudio.gez.R;
import spelstudio.gez.activities.PlayActivity;
import spelstudio.gez.constants.Constants;
import spelstudio.gez.interfaces.FriendListItemClickListener;
import spelstudio.gez.interfaces.PlayListItemClickListener;
import spelstudio.gez.webapi.Responses.Gez;

public class PlayListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Gez> arrayGez;
    Activity mContext;
    View mView;
    FrameLayout.LayoutParams params;
    YouTubePlayer.OnInitializedListener mInitializeListener;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    PlayListItemClickListener mListener;
    Boolean mArchvLst;

    public PlayListRecyclerAdapter(List<Gez> arrayGez, Activity mContext, PlayListItemClickListener mListener, Boolean mIsArchvLst) {
        this.arrayGez = arrayGez;
        params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        this.mContext = mContext;
        this.mListener = mListener;
        this.mArchvLst = mIsArchvLst;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.playlist_recycler_item, parent, false);
            ViewHolder mViewHolder = new ViewHolder(mView);
            this.mView = mView;
            return mViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.playlist_recycler_footer_item, parent, false);
            FooterViewHolder mViewHolder = new FooterViewHolder(mView);
            return mViewHolder;
        } else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder mViewHolder = (ViewHolder) holder;
            if (arrayGez.size() != 0) {
                mViewHolder.totalDoneTxt.setText(arrayGez.get(position).totalFriendsDoneRated + "/" + arrayGez.get(position).totalFriendsDone);
                mViewHolder.totalNotDoneTxt.setText(arrayGez.get(position).totalFriendsOpenRated + "/" + arrayGez.get(position).totalFriendsOpen);
                mViewHolder.rightWrongTxt.setText(arrayGez.get(position).totalGood + "/" + arrayGez.get(position).totalWrong);
                mViewHolder.titleTxt.setMaxLines(1);
                mViewHolder.descrptnTxt.setMaxLines(1);
                mViewHolder.titleTxt.setText(arrayGez.get(position).videoName.toString());
                mViewHolder.descrptnTxt.setText(arrayGez.get(position).gezAddedBy.toString());
                UrlToVideoIdConverter mConv = new UrlToVideoIdConverter();
                String videoId = mConv.videoId(arrayGez.get(position).videoUrl);
                String url = "http://img.youtube.com/vi/" + videoId + "/0.jpg";
                Glide.with(mContext).load(url).into(mViewHolder.fl_player);
                mViewHolder.fl_player.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.onItemClicked(arrayGez.get(position).gezID);
                        }
                    }
                });
                mViewHolder.totalNotDoneTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.onItemClicked(arrayGez.get(position).gezID);
                        }
                    }
                });
                mViewHolder.fl_player.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onItemClicked(arrayGez.get(position).gezID);
                    }
                });

            }

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder mFooterHolder = (FooterViewHolder) holder;
            mFooterHolder.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onFooterClicked();
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!mArchvLst) {
            if (position == arrayGez.size() || arrayGez.size() == 0) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (!mArchvLst) {
            if (arrayGez.size() > 0) {
                return arrayGez.size() + 1;
            } else {
                return 1;
            }
        } else {
            return arrayGez.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fl_player;
        TextView totalDoneTxt, totalNotDoneTxt, rightWrongTxt, titleTxt, descrptnTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            //todo remove this function call after demo testing
            fl_player = (ImageView) itemView.findViewById(R.id.item_pl_fl_player);
            totalDoneTxt = (TextView) itemView.findViewById(R.id.item_pl_done_txt);
            totalNotDoneTxt = (TextView) itemView.findViewById(R.id.item_pl_notdone_txt);
            rightWrongTxt = (TextView) itemView.findViewById(R.id.item_pl_rgt_wrng_txt);
            titleTxt = (TextView) itemView.findViewById(R.id.item_pl_name_txt);
            descrptnTxt = (TextView) itemView.findViewById(R.id.item_pl_time_txt);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        ImageButton addBtn;

        public FooterViewHolder(View itemView) {
            super(itemView);
            addBtn = (ImageButton) itemView.findViewById(R.id.palylist_footer_add_btn);
        }
    }
}