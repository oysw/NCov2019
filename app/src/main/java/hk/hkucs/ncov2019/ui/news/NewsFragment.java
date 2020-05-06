package hk.hkucs.ncov2019.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.prof.rssparser.Channel;

import hk.hkucs.ncov2019.NewsAdapter;
import hk.hkucs.ncov2019.R;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private SwipeRefreshLayout newsSRView;
    private NewsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel =
                ViewModelProviders.of(this).get(NewsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_news, container, false);
        final RecyclerView recyclerView = root.findViewById(R.id.list_news);
        LinearLayoutManager manager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        newsViewModel.getChannel().observe(getViewLifecycleOwner(), new Observer<Channel>() {
            @Override
            public void onChanged(Channel channel) {
                if (channel != null){
                    adapter = new NewsAdapter(root.getContext(), channel.getArticles());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    newsSRView.setRefreshing(false);
                }
            }
        });

        newsSRView = root.findViewById(R.id.news_swipe);
        newsSRView.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        newsSRView.canChildScrollUp();
        newsSRView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.articleList.clear();
                adapter.notifyDataSetChanged();
                newsSRView.setRefreshing(true);
                newsViewModel.fetchFeed();
            }
        });
        newsViewModel.fetchFeed();
        return root;
    }
}
