package spelstudio.gez.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import spelstudio.gez.R;
import spelstudio.gez.activities.FriendsProfileAcitivity;
import spelstudio.gez.activities.PlayActivity;
import spelstudio.gez.interfaces.SearchAddGezClickListener;
import spelstudio.gez.webapi.Responses.rankingresponse.ArrayFriend;

public class RankRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    View mView;
    Activity mContext;
    List<ArrayFriend> items;

    public RankRecyclerAdapter(List<ArrayFriend> items, Activity mContext) {
        this.mContext = mContext;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.rank_list_itm, parent, false);
        this.mView = mView;
        ViewHolder mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder mViewHolder = (ViewHolder) holder;
        ArrayFriend objRank = items.get(position);
        Glide.with(mContext).load(objRank.getFriendPicUrl()).into(mViewHolder.profilePic);
        mViewHolder.frndNameTxt.setTag(objRank.getFriendID());
        mViewHolder.userWonTxt.setText(objRank.getUserFriendGood());
        mViewHolder.userPercentageTxt.setText(objRank.getUserFriendPercentage());
        mViewHolder.friendWonTxt.setText(objRank.getFriendUserGood());
        mViewHolder.userPlayTxt.setText(Html.fromHtml(objRank.getUserFriendGezOpen() + "<small>/" + objRank.getTotalGez()));
        //mViewHolder.friendsPlayTxt.setText(objRank.getFriendUserGezOpen());
        mViewHolder.friendsPlayTxt.setText(Html.fromHtml(objRank.getFriendUserGezOpen() + "<small>/" + objRank.getTotalGez()));
        mViewHolder.friendsPercentageTxt.setText(objRank.getFriendUserPercentage());
        mViewHolder.frndNameTxt.setText(objRank.getFriendName());
        if (Integer.parseInt(objRank.getUserFriendGood()) > Integer.parseInt(objRank.getFriendUserGood())) {
            mViewHolder.userWonTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextGreen));
            mViewHolder.friendWonTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextRed));
            mViewHolder.userWonTxt.setTypeface(mViewHolder.userWonTxt.getTypeface(), Typeface.BOLD);
            mViewHolder.friendWonTxt.setTypeface(mViewHolder.friendWonTxt.getTypeface(), Typeface.NORMAL);
            mViewHolder.rankImgUser.setVisibility(View.VISIBLE);
            mViewHolder.rankImgFrnd.setVisibility(View.GONE);

        } else {
            if (Integer.parseInt(objRank.getUserFriendGood()) == Integer.parseInt(objRank.getFriendUserGood())) {
                mViewHolder.friendWonTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextGreen));
                mViewHolder.userWonTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextGreen));
                mViewHolder.rankImgUser.setVisibility(View.GONE);
                mViewHolder.rankImgFrnd.setVisibility(View.GONE);

                mViewHolder.userWonTxt.setTypeface(mViewHolder.userWonTxt.getTypeface(), Typeface.NORMAL);
                mViewHolder.friendWonTxt.setTypeface(mViewHolder.friendWonTxt.getTypeface(), Typeface.NORMAL);

            } else {
                mViewHolder.friendWonTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextGreen));
                mViewHolder.userWonTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextRed));
                mViewHolder.rankImgUser.setVisibility(View.GONE);
                mViewHolder.rankImgFrnd.setVisibility(View.VISIBLE);

                mViewHolder.userWonTxt.setTypeface(mViewHolder.userWonTxt.getTypeface(), Typeface.NORMAL);
                mViewHolder.friendWonTxt.setTypeface(mViewHolder.friendWonTxt.getTypeface(), Typeface.BOLD);
            }
        }
        try {
            if (Float.parseFloat(objRank.getUserFriendPercentage().replace(",", ".")) > Float.parseFloat(objRank.getFriendUserPercentage().replace(",", "."))) {
                mViewHolder.userPercentageTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextGreen));
                mViewHolder.friendsPercentageTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextRed));

            } else {
                if (Float.parseFloat(objRank.getUserFriendPercentage()) > Float.parseFloat(objRank.getFriendUserPercentage())) {
                    mViewHolder.userPercentageTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextGreen));
                    mViewHolder.friendsPercentageTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextGreen));
                } else {
                    mViewHolder.friendsPercentageTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextGreen));
                    mViewHolder.userPercentageTxt.setTextColor(mContext.getResources().getColor(R.color.colorTextRed));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView rankImgUser, rankImgFrnd, profilePic;
        TextView userWonTxt, friendWonTxt, userPercentageTxt, friendsPercentageTxt, userPlayTxt, friendsPlayTxt, frndNameTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            frndNameTxt = (TextView) itemView.findViewById(R.id.rank_frnd_name);
            profilePic = (ImageView) itemView.findViewById(R.id.rank_list_frnd_img);
            rankImgUser = (ImageView) itemView.findViewById(R.id.rank_img_usr);
            rankImgFrnd = (ImageView) itemView.findViewById(R.id.rank_img_frnd);
            userWonTxt = (TextView) itemView.findViewById(R.id.rnk_usr_won_txt);
            userPercentageTxt = (TextView) itemView.findViewById(R.id.rnk_usr_percent_txt);
            userPlayTxt = (TextView) itemView.findViewById(R.id.rnk_usr_play_txt);
            friendWonTxt = (TextView) itemView.findViewById(R.id.rnk_frnd_won_txt);
            friendsPlayTxt = (TextView) itemView.findViewById(R.id.rnk_frnd_play_txt);
            friendsPercentageTxt = (TextView) itemView.findViewById(R.id.rnk_frnd_percent_txt);

            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, FriendsProfileAcitivity.class);
                    intent.putExtra("friendId", frndNameTxt.getTag().toString());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
