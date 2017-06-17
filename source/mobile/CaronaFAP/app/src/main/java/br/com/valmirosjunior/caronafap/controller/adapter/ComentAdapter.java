package br.com.valmirosjunior.caronafap.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
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
    private ProfilePictureView ppvUser;

    public ComentAdapter(Context context,List<Coment> coments) {
        setComents(coments);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<Coment> getComents() {
        return coments;
    }

    public void setComents(List<Coment> coments) {
        this.coments = (coments == null)? new ArrayList<Coment>() : coments;
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
        Coment coment =coments.get(getCount()-position-1);
        View rowview = inflater.inflate(R.layout.row_coment,null);
        textView = (TextView) rowview.findViewById(R.id.tvComent);
        ratingBar = (RatingBar) rowview.findViewById(R.id.rbComent);
        ppvUser = (ProfilePictureView) rowview.findViewById(R.id.ppvUser);
        textView.setText(coment.getComent());
        ratingBar.setRating(coment.getNote());
        ppvUser.setProfileId(coment.getIdAuthor());
        return rowview;
    }
     public float getNote(){
         float note =0;
         for (Coment coment: coments) {
             note += coment.getNote();
         }
         return  note/coments.size();
     }
}
