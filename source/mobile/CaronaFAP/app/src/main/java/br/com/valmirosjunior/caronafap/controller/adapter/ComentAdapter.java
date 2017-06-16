package br.com.valmirosjunior.caronafap.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.model.Coment;

/**
 * Created by junior on 05/06/17.
 */

public class ComentAdapter extends BaseAdapter {
    private List<Coment> coments;
    private Context context;
    private LayoutInflater inflater;
    private TextView textView;
    private RatingBar ratingBar;

    public ComentAdapter(List<Coment> coments, Context context) {
        this.coments = coments;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return coments.size();
    }

    @Override
    public Object getItem(int position) {
        return coments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View rowview = inflater.inflate(R.layout.row_coment,null);
        textView = (TextView) rowview.findViewById(R.id.textView);
        ratingBar = (RatingBar) rowview.findViewById(R.id.ratingBar);

        return rowview;
    }
}
