package spelstudio.gez.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;
import spelstudio.gez.R;
import spelstudio.gez.interfaces.FriendListItemClickListener;
import spelstudio.gez.interfaces.RecyclerFooterItemClickListener;
import spelstudio.gez.webapi.Responses.ArrayFriend;

public class FriendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ArrayFriend> friendsList;
    Activity mContext;
    View mView;
    TextView mFrndName;
    private static final int TYPE_ITEM=1;
    private static final int TYPE_FOOTER=2;
    FriendListItemClickListener mListener;
    RecyclerFooterItemClickListener mFooterListener;

    public FriendsListAdapter(List<ArrayFriend> friendsList, Activity mContext, FriendListItemClickListener mListener,RecyclerFooterItemClickListener mFooterListener) {
        this.friendsList = friendsList;
        this.mContext = mContext;
        this.mListener=mListener;
        this.mFooterListener=mFooterListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM){
        View mView= LayoutInflater.from(mContext).inflate(R.layout.friends_list_item,parent,false);
        ViewHolder mViewHolder=new ViewHolder(mView);
        this.mView=mView;
        return mViewHolder;
        }else if(viewType==TYPE_FOOTER){
            View mView= LayoutInflater.from(mContext).inflate(R.layout.friends_list_item_footer,parent,false);
            FooterViewHolder mViewHolder=new FooterViewHolder(mView);
            return mViewHolder;
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder) {
            ViewHolder mViewHolder = (ViewHolder) holder;
            mViewHolder.mFrndName.setText(friendsList.get(position).getFriendName());
            Glide.with(mContext).load(friendsList.get(position).getFriendPicUrl()).into(mViewHolder.mFrndImage);
            mViewHolder.mFrndImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null) {
                        mListener.onFriendItemClicked(friendsList.get(position).getFriendID());
                    }
                }
            });
            mViewHolder.mFrndName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null) {
                        mListener.onFriendItemClicked(friendsList.get(position).getFriendID());
                    }
                }
            });
            mViewHolder.mCardItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null) {
                        mListener.onFriendItemClicked(friendsList.get(position).getFriendID());
                    }
                }
            });

        }else if(holder instanceof FooterViewHolder){
            final FooterViewHolder mFooterHolder=(FooterViewHolder) holder;
            mFooterHolder.inviteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mFooterListener!=null) {
                        mFooterListener.onFooterClicked();
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==friendsList.size()){
              return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return friendsList.size()+1;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mFrndImage;
        TextView mFrndName;
        CardView mCardItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mFrndImage=itemView.findViewById(R.id.friends_list_fimage);
            mFrndName=itemView.findViewById(R.id.friends_list_fname);
            mCardItem=itemView.findViewById(R.id.friends_list_card);
        }
    }
    class FooterViewHolder extends RecyclerView.ViewHolder{
        ImageButton inviteBtn;
        public FooterViewHolder(View itemView) {
            super(itemView);
            inviteBtn=(ImageButton)itemView.findViewById(R.id.invite_frnd_btn);
        }
    }
}
