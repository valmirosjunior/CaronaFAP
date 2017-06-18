package br.com.valmirosjunior.caronafap.controller.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.controller.activities.RegisterRideActivity;
import br.com.valmirosjunior.caronafap.controller.activities.ShowOneRideActivity;
import br.com.valmirosjunior.caronafap.controller.activities.ShowRidesActivity;
import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.model.Solicitation;
import br.com.valmirosjunior.caronafap.model.dao.RideDAO;
import br.com.valmirosjunior.caronafap.model.dao.SolicitationDAO;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.FaceBookManager;
import br.com.valmirosjunior.caronafap.util.MessageUtil;

/**
 * Created by junior on 15/06/17.
 */

public class RideHelper {
    private Activity activity;
    private RideDAO rideDAO;

    public RideHelper(Activity activity, RideDAO rideDAO) {
        this.activity = activity;
        this.rideDAO = rideDAO;
    }

    public DialogInterface.OnClickListener requestRide(final Context context, final Ride ride){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Solicitation solicitation = new Solicitation();
                solicitation.setRide(ride);
                solicitation.setMessageSended(Type.REQUEST);
                solicitation.setSender(FaceBookManager.getCurrentUser());
                SolicitationDAO.getInstance().sendSolicitation(solicitation);
                MessageUtil.showAlertDialogBuilder(context,
                        "Sucesso","Solicitação efetuada com Sucesso!");
            }
        };
    }

    public void findPatner(Ride ride){
        Type type = Type.OTHER_RIDES;
        Intent intent = new Intent(activity, ShowRidesActivity.class);
        intent.putExtra(Constants.TYPE_OBSERVER,type.name());
        rideDAO.setRide(ride);
        activity.startActivity(intent);
    }

    public void editRide(Ride ride){
        Intent intent;
        intent = new Intent(activity, RegisterRideActivity.class);
        intent.putExtra(Constants.ID_RIDE,ride.getId());
        activity.startActivity(intent);
    }

    public void deleteRide(final Ride ride){
        MessageUtil.showConfirm(activity, activity.getString(R.string.confirm_delete_ride),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rideDAO.removeRide(ride.getId());
                        if(activity instanceof ShowOneRideActivity){
                            activity.finish();
                        }
                    }
                });
    }

    public  void seeProfile(String idUser){
        Intent intent ;
        try {
            activity.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+idUser));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            try{
                activity.getPackageManager().getPackageInfo("com.facebook.lite", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+idUser));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }catch (Exception ex){
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://facebook.com/"+idUser));
                activity.startActivity(intent);
            }
        }

    }
    public  void seeRouteOnMap(Ride ride){
        String origin, dest;
        origin = ride.getOrigin().getAdress();
        dest = ride.getDestination().getAdress();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" +
                        origin+"&daddr=" +dest));
        activity.startActivity(intent);
    }


}
