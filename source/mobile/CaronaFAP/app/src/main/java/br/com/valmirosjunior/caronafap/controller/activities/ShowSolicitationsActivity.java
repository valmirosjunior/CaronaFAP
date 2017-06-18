package br.com.valmirosjunior.caronafap.controller.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.controller.adapter.SolicitationAdapter;
import br.com.valmirosjunior.caronafap.model.Solicitation;
import br.com.valmirosjunior.caronafap.model.dao.SolicitationDAO;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;

public class ShowSolicitationsActivity extends AppCompatActivity implements Observer{
    private SolicitationDAO sd;
    private SolicitationAdapter sa;
    private ListView lv;
    private  Type type;
    private Button btChange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_solicitations);
        init();
    }

    private void init(){
        btChange = (Button) findViewById(R.id.btChange);
        lv = (ListView) findViewById(R.id.lvSolicitations);
        sa = new SolicitationAdapter(this, new ArrayList<Solicitation>());
        lv.setAdapter(sa);
        lv.setOnItemClickListener(sa);
        sd = SolicitationDAO.getInstance();
        type = Type.RECEIVE;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sd.addObserver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sd.deleteObserver(this);
    }

    @Override
    public void update(Object object) {
        ArrayList <Solicitation>solicitations = (ArrayList<Solicitation>) object;
        sa.notifyDataSetInvalidated();
        sa.setSolicitations(solicitations);
        sa.notifyDataSetChanged();
    }

    @Override
    public void update(Observable observable, Object object) {
        update(object);
    }

    @Override
    public Type getType() {
        return type;
    }

    public void changeType(View view) {
        if(type== Type.SEND){
            type =Type.RECEIVE;
            btChange.setText(R.string.solicitations_receiveds);
        }else{
            type =Type.SEND;
            btChange.setText(R.string.solicitations_sendeds);
        }
        sd.notifyObservers();
    }
}
