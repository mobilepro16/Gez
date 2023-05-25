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

import spelstudio.gez.Helpers.UrlToVideoIdConverter;
import spelstudio.gez.R;
import spelstudio.gez.constants.Constants;
import spelstudio.gez.interfaces.SearchAddGezClickListener;
import spelstudio.gez.webapi.Responses.GetPromoVidzResponse;

public class AddGezRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    View mView;
    Activity mContext;
    AddGezRecItemClickListener mListener;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    SearchAddGezClickListener searchAddGezClickListener;
    public List<GetPromoVidzResponse.Video> arrayVideos = null;

    public AddGezRecyclerAdapter(List<GetPromoVidzResponse.Video> arrayVideos, Activity mContext, SearchAddGezClickListener searchAddGezClickListener, AddGezRecItemClickListener mListener) {
        this.arrayVideos = arrayVideos;
        this.searchAddGezClickListener = searchAddGezClickListener;
        this.mListener = mListener;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.search_gez_video_item, parent, false);
        this.mView = mView;
        ViewHolder mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mViewHolder = (ViewHolder) holder;
//        Glide.with(mContext).load(R.drawable.video_demo).into(mViewHolder.mVideoDemo);
        Glide.with(mContext).load(R.drawable.btn_add_small).into(mViewHolder.mAddGezBtn);
        mViewHolder.mGezTitleTxt.setText(arrayVideos.get(position).videoName);
        mViewHolder.mGezTimeTxt.setText(arrayVideos.get(position).videoOwner);
        UrlToVideoIdConverter mConv = new UrlToVideoIdConverter();
        String videoId = mConv.videoId(arrayVideos.get(position).videoUrl);
        String url = "http://img.youtube.com/vi/" + videoId + "/3.jpg";
        Glide.with(mContext).load(url).into(mViewHolder.fl_player);
        Glide.with(mContext).load(arrayVideos.get(position).videoLabelUrl).into(mViewHolder.ivChanelLogo);
        //todo add appropriate tags and their images
        mViewHolder.mAddGezBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchAddGezClickListener != null) {
                    mListener.onVideoClicked(arrayVideos.get(position).videoName, arrayVideos.get(position).videoOwner, arrayVideos.get(position).videoUrl);

                }
            }
        });

        mViewHolder.fl_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onVideoClicked(arrayVideos.get(position).videoName, arrayVideos.get(position).videoOwner, arrayVideos.get(position).videoUrl);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayVideos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mVideoDemo;
        public ImageButton mAddGezBtn;
        TextView mGezTitleTxt, mGezTimeTxt;
        ImageView fl_player, ivChanelLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            fl_player = (ImageView) itemView.findViewById(R.id.item_sg_fl_player);
            mGezTimeTxt = (TextView) itemView.findViewById(R.id.item_sg_title_txt);
            mGezTitleTxt = (TextView) itemView.findViewById(R.id.item_sg_gezd_time_txt);
            mAddGezBtn = (ImageButton) itemView.findViewById(R.id.item_sg_gez_add_btn);
            ivChanelLogo = itemView.findViewById(R.id.ivChanelLogo);
        }
    }


    public interface AddGezRecItemClickListener {
        public void onVideoClicked(String mVideoName, String mVideoOwner, String mVideoUrl);
    }
}
