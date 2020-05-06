package hk.hkucs.ncov2019;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prof.rssparser.Article;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    public Context context;
    public List<Article> articleList;

    public NewsAdapter(Context context, List<Article> list){
        this.context = context;
        this.articleList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView link;
        TextView pubDate;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_title);
            link = itemView.findViewById(R.id.news_link);
            pubDate = itemView.findViewById(R.id.news_date);
            image = itemView.findViewById(R.id.news_img);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Article article = articleList.get(position);
        String pubDate;
        String srcPubDate = article.getPubDate();
        SimpleDateFormat srcFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            pubDate = format.format(srcFormat.parse(srcPubDate));
        } catch (ParseException e) {
            e.printStackTrace();
            pubDate = srcPubDate;
        }
        holder.pubDate.setText(pubDate);
        holder.title.setText(article.getTitle());
        Picasso.get().load(article.getImage()).placeholder(R.drawable.ic_broken_pic_holder).into(holder.image);
        String spanStr = "View in Browser";
        SpannableString str = new SpannableString(spanStr);
        str.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(article.getLink()));
                context.startActivity(intent);
            }
        }, 0, spanStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.link.setText(str);
        holder.link.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
        return articleList == null ? 0 : articleList.size();
    }
}
