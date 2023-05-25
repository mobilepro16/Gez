package spelstudio.gez.Helpers;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import spelstudio.gez.R;
import spelstudio.gez.constants.Constants;

public class YoutubeConnector {
    private YouTube youtube;
    private YouTube.Search.List query;


    public YoutubeConnector(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try {
            query = youtube.search().list("id,snippet");
            query.setKey(Constants.YOUTUBE_DEVELOPER_KEY);
            query.setType("video");
            query.setMaxResults(20L);
            query.setFields("items(id/videoId,snippet,snippet/title,snippet/description,snippet/thumbnails/default/url)");
        } catch (IOException e) {
            Log.d("YC", "Could not initialize: " + e);
        }
    }

    public List<YoutubeVideoItem> search(String keywords) {
        query.setQ(keywords);
        try {
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();
            List<YoutubeVideoItem> items = new ArrayList<YoutubeVideoItem>();
            for (SearchResult result : results) {
                YoutubeVideoItem item = new YoutubeVideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setChannelTitle(result.getSnippet().getChannelTitle());
                item.setId(result.getId().getVideoId());
                items.add(item);
            }
            return items;
        } catch (IOException e) {
            Log.d("YC", "Could not search: " + e);
            return null;
        }
    }
}
