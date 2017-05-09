package br.com.valmirosjunior.caronafap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import br.com.valmirosjunior.caronafap.adapter.CustomAdapterRide;
import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.model.dao.RideDAO;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.MessageUtil;

public class ShowRider extends AppCompatActivity implements Observer {
    private RideDAO rideDAO;
    private Ride ride;
    private CustomAdapterRide customAdapterRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rider);


        rideDAO =RideDAO.getInstance();
        rideDAO.addObserver(this);

        customAdapterRide = new CustomAdapterRide(this,new ArrayList<Ride>());

        ListView listView = (ListView) findViewById(R.id.listViewShowRides);
        listView.setAdapter(customAdapterRide);

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu_my_liste_ride, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent;
        switch (item.getItemId()) {
            case R.id.edit_ride:
                intent = new Intent(this, RegisterRide.class);
                intent.putExtra(Constants.ID_RIDE,
                       customAdapterRide.getRides().get(info.position).getIdRide());
                startActivity(intent);
                return true;

            case R.id.viewOnmap :
                ride = customAdapterRide.getRides().get(info.position);
                intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" +
                                ride.getOrigin().getAdress()+
                                "&daddr=" +
                                ride.getDestination().getAdress()));
                startActivity(intent);
                return true;

            case R.id.viewProfile :
                String idUSer= customAdapterRide.getRides().get(info.position)
                        .getUser().getId();
                try {
                    this.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+idUSer));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    try{
                        this.getPackageManager().getPackageInfo("com.facebook.lite", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+idUSer));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }catch (Exception ex){
                        intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://facebook.com/"+idUSer));
                        startActivity(intent);
                    }
                }

            case R.id.delete:
                MessageUtil.showToast(this, "Excluir");
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public void update(Observable observable, Object o) {
        customAdapterRide.notifyDataSetInvalidated();
        customAdapterRide.setRides((List<Ride> )o);
        customAdapterRide.notifyDataSetChanged();
    }
}
