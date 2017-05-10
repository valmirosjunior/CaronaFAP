package br.com.valmirosjunior.caronafap;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

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
    private TextView textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rider);

        customAdapterRide = new CustomAdapterRide(this,new ArrayList<Ride>());
        ListView listView = (ListView) findViewById(R.id.listViewShowRides);
        listView.setAdapter(customAdapterRide);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);

        registerForContextMenu(listView);

        rideDAO =RideDAO.getInstance();
        rideDAO.addObserver(this);
        rideDAO.notifyObservers();
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
        Ride ride = customAdapterRide.getRides().get(info.position);
        switch (item.getItemId()) {
            case R.id.edit_ride:
                intent = new Intent(this, RegisterRide.class);
                intent.putExtra(Constants.ID_RIDE,ride.getIdRide());
                startActivity(intent);
                return true;

            case R.id.viewOnmap :
                intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" +
                                ride.getOrigin().getAdress()+
                                "&daddr=" +
                                ride.getDestination().getAdress()));
                startActivity(intent);
                return true;

            case R.id.viewProfile :
                String idUSer= ride.getUser().getId();
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
                return true;
            case R.id.delete:
                showConfirmDialog(ride.getIdRide());
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    private void showConfirmDialog(final String idRide){
        AlertDialog.Builder builder = MessageUtil.createAlertDialogBuilder(this);
        builder.setMessage(getString(R.string.alert));
        builder.setTitle("Deseja realmente excluir carona?");

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rideDAO.removeRide(idRide);
            }
        });
        builder.setNegativeButton(getString(R.string.no), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void update(Observable observable, Object o) {
        List<Ride> rides = (List<Ride> )o;
        if (rides.size()==0){
            textViewMessage.setVisibility(View.VISIBLE);
            textViewMessage.setText(R.string.no_found_rides);
        }else {
            textViewMessage.setVisibility(View.INVISIBLE);
            textViewMessage.setText("");
        }
        customAdapterRide.notifyDataSetInvalidated();
        customAdapterRide.setRides(rides);
        customAdapterRide.notifyDataSetChanged();

    }
}
