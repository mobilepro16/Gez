package spelstudio.gez.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import spelstudio.gez.Helpers.UrlToVideoIdConverter;
import spelstudio.gez.Helpers.YoutubeVideoItem;
import spelstudio.gez.R;
import spelstudio.gez.interfaces.SearchAddGezClickListener;

public class YoutubeSearchVidAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    View mView;
    Activity mContext;
    AddGezRecyclerAdapter.AddGezRecItemClickListener mListener;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    SearchAddGezClickListener searchAddGezClickListener;
    public List<YoutubeVideoItem> arrayVideos = null;

    public YoutubeSearchVidAdapter(List<YoutubeVideoItem> arrayVideos, Activity mContext, SearchAddGezClickListener searchAddGezClickListener, AddGezRecyclerAdapter.AddGezRecItemClickListener mListener) {
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
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mViewHolder = (ViewHolder) holder;
//        Glide.with(mContext).load(R.drawable.video_demo).into(mViewHolder.mVideoDemo);
        UrlToVideoIdConverter mConv = new UrlToVideoIdConverter();
        String videoId = mConv.videoId(arrayVideos.get(position).getId());
        String url = "http://img.youtube.com/vi/" + videoId + "/3.jpg";
        Glide.with(mContext).load(url).into(mViewHolder.fl_player);
        Glide.with(mContext).load(R.drawable.btn_add_small).into(mViewHolder.mAddGezBtn);
        mViewHolder.mGezTimeTxt.setText(arrayVideos.get(position).getTitle());
        mViewHolder.mGezTitleTxt.setText(arrayVideos.get(position).getChannelTitle());
//        Glide.with(mContext).load(R.drawable.views).into(mViewHolder.mEyeImage);
        //todo add appropriate tags and their images
        mViewHolder.mAddGezBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchAddGezClickListener != null) {
                    mListener.onVideoClicked(arrayVideos.get(position).getTitle(), arrayVideos.get(position).getChannelTitle(), arrayVideos.get(position).getId());

                }
            }
        });
        mViewHolder.fl_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onVideoClicked(arrayVideos.get(position).getTitle(), arrayVideos.get(position).getChannelTitle(), arrayVideos.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayVideos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton mAddGezBtn;
        TextView mGezTitleTxt, mGezTimeTxt;
        ImageView fl_player, ivChanelLogo;


        public ViewHolder(View itemView) {
            super(itemView);
            fl_player = itemView.findViewById(R.id.item_sg_fl_player);
            mGezTimeTxt = itemView.findViewById(R.id.item_sg_title_txt);
            mGezTitleTxt = itemView.findViewById(R.id.item_sg_gezd_time_txt);
            mAddGezBtn = itemView.findViewById(R.id.item_sg_gez_add_btn);
            ivChanelLogo = itemView.findViewById(R.id.ivChanelLogo);
        }
    }


    public interface AddGezRecItemClickListener {
        public void onVideoClicked(String mVideoName, String mVideoOwner, String mVideoUrl);
    }
}
