package br.com.valmirosjunior.caronafap.controller.adapter;

/**
 * Created by junior on 11/05/17.
 */

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.List;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.controller.activities.ShowOneSolicitationActivity;
import br.com.valmirosjunior.caronafap.model.Solicitation;
import br.com.valmirosjunior.caronafap.util.Constants;

public class SolicitationAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private List<Solicitation> solicitations;
    private Context context;
    private TextView textView;
    private ProfilePictureView profilePictureView;

    private static LayoutInflater inflater=null;


    public List<Solicitation> getSolicitations() {
        return solicitations;
    }

    public void setSolicitations(List<Solicitation> solicitations) {
        this.solicitations = solicitations == null ?
                new ArrayList<Solicitation>(): solicitations;
    }

    public SolicitationAdapter(Context context, List<Solicitation> solicitations) {
        setSolicitations(solicitations);
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return solicitations.size();
    }

    @Override
    public Object getItem(int position) {
        return solicitations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Solicitation solicitation = solicitations.get(position);
        View rowView;
        rowView = inflater.inflate(R.layout.row_solicitation_list, null);
        textView =(TextView) rowView.findViewById(R.id.tvDescriptoin);
        profilePictureView = (ProfilePictureView) rowView.findViewById(R.id.ppvUser);

        textView.setText(Html.fromHtml(solicitation.showDescription()));
        profilePictureView.setProfileId(solicitation.getSender().getId());
        return rowView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Solicitation solicitation = (Solicitation) getItem(position);
        Intent intent = new Intent(context, ShowOneSolicitationActivity.class);
        intent.putExtra(Constants.ID_SOLICITATION,solicitation.getId());
        context.startActivity(intent);
    }

}
