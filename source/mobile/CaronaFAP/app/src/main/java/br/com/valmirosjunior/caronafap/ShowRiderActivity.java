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

import br.com.valmirosjunior.caronafap.adapter.RideAdapter;
import br.com.valmirosjunior.caronafap.model.Notification;
import br.com.valmirosjunior.caronafap.model.Observable;
import br.com.valmirosjunior.caronafap.model.Observer;
import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.model.dao.NotificationDAO;
import br.com.valmirosjunior.caronafap.model.dao.RideDAO;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.network.FaceBookManager;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.MessageUtil;

public class ShowRiderActivity extends AppCompatActivity implements Observer {
    private RideDAO rideDAO;
    private Ride ride;
    private RideAdapter rideAdapter;
    private TextView textViewMessage;
    private Type typeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rider);


        rideAdapter = new RideAdapter(this,new ArrayList<Ride>());
        ListView listView = (ListView) findViewById(R.id.listViewShowRides);
        listView.setAdapter(rideAdapter);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);

        registerForContextMenu(listView);
        Intent intent = getIntent();

        if (intent.hasExtra(Constants.TYPE_OBSERVER)){
            typeObserver = (Type) intent.getSerializableExtra(Constants.TYPE_OBSERVER);

        }else {
            typeObserver = Type.MY_RIDE;
        }

        updateMessage();

        rideDAO =RideDAO.getInstance();
        rideDAO.addObserver(this);
        rideDAO.notifyObservers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rideDAO.addObserver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        rideDAO.deleteObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rideDAO.deleteObserver(this);
    }

    @Override
    public void onBackPressed(){
        if(typeObserver == Type.MY_RIDE){
            super.onBackPressed();
        }else{
            changeTypeObserver();
        }

    }

    private void changeTypeObserver() {
        updateMessage();
        MessageUtil.showProgressDialog(this);
        rideDAO.setRide(ride);
        typeObserver = typeObserver== Type.MY_RIDE ? Type.OTHER_RIDES: Type.MY_RIDE;
        rideDAO.notifyObservers();
        updateMessage();
        MessageUtil.hideProgressDialog();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater =getMenuInflater();
        if(typeObserver == Type.OTHER_RIDES){
            inflater.inflate(R.menu.menu_other_rides,menu);
        }else{
            inflater.inflate(R.menu.menu_my_liste_ride, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent;
        Ride ride = rideAdapter.getRides().get(info.position);
        switch (item.getItemId()) {
            case R.id.edit_ride:
                intent = new Intent(this, RegisterRideActivity.class);
                intent.putExtra(Constants.ID_RIDE,ride.getIdRide());
                startActivity(intent);
                return true;

            case R.id.delete:
                showConfirmDeleteRide(ride.getIdRide());
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

            case R.id.findRide:
                rideDAO.setRide(ride);
                typeObserver = Type.OTHER_RIDES;
                rideDAO.notifyObservers();
                updateMessage();
                return true;

            case R.id.requestRide:
                showConfirmRequestRide(ride);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
        private void updateMessage (){
            if(typeObserver ==  Type.MY_RIDE){
                setTitle(R.string.your_rides);
            }else{
                setTitle(R.string.result_search);
            }
        }

    private void showConfirmDeleteRide(final String idRide){
        AlertDialog.Builder builder = MessageUtil.createAlertDialogBuilder(this);
        builder.setTitle(R.string.alert);
        builder.setMessage(R.string.confirm_delete_ride);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rideDAO.removeRide(idRide);
            }
        });
        builder.setNegativeButton(R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmRequestRide(final Ride ride){
        AlertDialog.Builder builder = MessageUtil.createAlertDialogBuilder(this);
        builder.setTitle(R.string.alert);
        builder.setMessage(R.string.confirm_request_ride);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Notification notification = new Notification();
                notification.setIdRide(ride.getIdRide());
                notification.setReceiver(ride.getUser());
                notification.setType(Type.REQUEST);
                notification.setSender(FaceBookManager.getCurrentUser());
                NotificationDAO.getInstance().sendNotification(notification);
                startActivity(new Intent(ShowRiderActivity.this, ProfileUserActivity.class));
                ShowRiderActivity.this.finish();

            }
        });
        builder.setNegativeButton(R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void update(Observable observable, Object o) {
        //List<Ride> rides = rideDAO.getMyRides();
        List<Ride> rides = (List<Ride>) o;
        if (rides.size()==0){
            textViewMessage.setVisibility(View.VISIBLE);
            if(typeObserver == Type.MY_RIDE){
                textViewMessage.setText(R.string.no_register_rides);
            }else{
                textViewMessage.setText(R.string.no_found_rides);
            }
        }else {
            textViewMessage.setVisibility(View.INVISIBLE);
            textViewMessage.setText("");
        }
        rideAdapter.notifyDataSetInvalidated();
        rideAdapter.setRides(rides);
        rideAdapter.notifyDataSetChanged();

    }

    @Override
    public Type getType() {
        return typeObserver;
    }



}
