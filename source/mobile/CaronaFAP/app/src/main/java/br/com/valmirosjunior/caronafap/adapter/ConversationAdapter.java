package br.com.valmirosjunior.caronafap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.model.Message;
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.network.FaceBookManager;

/**
 * Created by junior on 04/06/17.
 */

public class ConversationAdapter extends BaseAdapter{
    private List<Message> conversations;
    private User user;
    private TextView textView;
    private LayoutInflater layoutInflater;
    private Context context;

    public ConversationAdapter(Context context,List<Message> conversations) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conversations = conversations;
        user = FaceBookManager.getCurrentUser();
    }

    @Override
    public int getCount() {
        return conversations.size();
    }

    @Override
    public Object getItem(int position) {
        return conversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Message message = conversations.get(position);
        View rowView=null;
        if(user.equals(message.getSender())){
             rowView =layoutInflater.inflate(R.layout.item_message_right,null);
        }else{
            rowView =layoutInflater.inflate(R.layout.item_message_left,null);
        }
        textView = (TextView) rowView.findViewById(R.id.textView);
        return rowView;
    }
}
