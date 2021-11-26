package a2bsystem.com.achat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import a2bsystem.com.achat.Models.Article;
import a2bsystem.com.achat.R;


public class ListViewArticleAdapter extends BaseAdapter {
    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<Article> articlesList = null;
    private ArrayList<Article> arraylist;

    public ListViewArticleAdapter(Context context, List<Article> articlesList) {
        mContext = context;
        this.articlesList = articlesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(articlesList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return articlesList.size();
    }

    @Override
    public Article getItem(int position) {
        return articlesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ListViewArticleAdapter.ViewHolder holder;
        if (view == null) {
            holder = new ListViewArticleAdapter.ViewHolder();
            view = inflater.inflate(R.layout.list_view_items_articles, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.nameArticle);
            view.setTag(holder);
        } else {
            holder = (ListViewArticleAdapter.ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText("[" + articlesList.get(position).getArtnr().replaceAll(" ","") + "] " + articlesList.get(position).getQ_gcar_lib1());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        articlesList.clear();
        if (charText.length() == 0) {
            articlesList.addAll(arraylist);
        } else {
            for (Article wp : arraylist) {
                if (wp.getQ_gcar_lib1().toLowerCase(Locale.getDefault()).contains(charText) || wp.getQ_gcar_lib1().toLowerCase(Locale.getDefault()).contains(charText)) {
                    articlesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}