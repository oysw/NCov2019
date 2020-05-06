package hk.hkucs.ncov2019.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<Channel> mChannel;
    private String rssUrl = "https://tools.cdc.gov/api/v2/resources/media/132608.rss";

    public LiveData<Channel> getChannel() {
        if (mChannel == null){
            mChannel = new MutableLiveData<>();
        }
        return mChannel;
    }

    public void setChannel(Channel channel){
        mChannel.postValue(channel);
    }

    public void fetchFeed() {
        Parser parser = new Parser();
        parser.onFinish(new OnTaskCompleted() {

            @Override
            public void onTaskCompleted(Channel channel) {
                setChannel(channel);
            }

            @Override
            public void onError(Exception e) {
                setChannel(new Channel(null, null, null, null, new ArrayList<Article>()));
                e.printStackTrace();
            }
        });
        parser.execute(rssUrl);
        parser.cancel();
    }
}