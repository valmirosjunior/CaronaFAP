package br.com.valmirosjunior.caronafap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.List;

import br.com.valmirosjunior.caronafap.adapter.NotificationAdapter;
import br.com.valmirosjunior.caronafap.model.Notification;
import br.com.valmirosjunior.caronafap.model.Observable;
import br.com.valmirosjunior.caronafap.model.Observer;
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.model.dao.NotificationDAO;
import br.com.valmirosjunior.caronafap.model.enums.Status;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.network.FaceBookManager;
import br.com.valmirosjunior.caronafap.util.MessageUtil;

public class ProfileUserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Observer {
    private User user;
    private FaceBookManager faceBookManager;
    private NotificationDAO notificationDAO;
    private NotificationAdapter notificationAdapter;
    private ProfilePictureView profilePictureNav,profilePictureMain;
    private TextView textViewUser, textViewWelcome,textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_profile_user);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileUserActivity.this, RegisterRideActivity.class));
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();


            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            notificationAdapter = new NotificationAdapter(this,new ArrayList<Notification>());
            ListView listView = (ListView) findViewById(R.id.listViewShowNotifications);
            listView.setAdapter(notificationAdapter);


            faceBookManager = new FaceBookManager(this);
            notificationDAO = NotificationDAO.getInstance();
            faceBookManager.addObserver(this);

            notificationDAO.addObserver(this);


            user = faceBookManager.getCurrentUser();

            textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);
            textViewUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_navHeader);

            profilePictureMain = (ProfilePictureView) findViewById(R.id.profilePictureUser);
            profilePictureNav = (ProfilePictureView)
                    navigationView.getHeaderView(0).findViewById(R.id.profilePictureUserNavBar);

            textViewWelcome.append(" " + user.getName());
            textViewUser.setText(user.getName());
            profilePictureMain.setProfileId(user.getId());
            profilePictureNav.setProfileId(user.getId());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationDAO.addObserver(this);
        update(notificationDAO,notificationDAO.getNotifications());
    }

    @Override
    protected void onPause() {
        super.onPause();
        notificationDAO.deleteObserver(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            showConfirmDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_register) {
            startActivity(new Intent(this, RegisterRideActivity.class));
        } else if (id == R.id.nav_seeRides) {
            startActivity(new Intent(this, ShowRiderActivity.class));
        } else if (id == R.id.nav_share) {
            MessageUtil.showToast(this, "Ainda não está pronto!");
        } else if (id == R.id.nav_send) {
            MessageUtil.showToast(this, "Ainda não está pronto!");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showConfirmDialog(){
        AlertDialog.Builder builder = MessageUtil.createAlertDialogBuilder(this);
        builder.setMessage(R.string.do_you_want_exit);
        builder.setTitle(R.string.confimation);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                faceBookManager.logout();
            }
        });
        builder.setNegativeButton(R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void update(Observable observable, Object o) {
        if ( observable instanceof FaceBookManager ){
            Status status;
            AlertDialog.Builder buider = MessageUtil.createAlertDialogBuilder(this);
            try {
                status = (Status) o;
                switch (status) {
                    case SUCCESS:
                        startActivity(new Intent(this,MainActivity.class));
                        faceBookManager.deleteObserver(this);
                        this.finish();
                        break;
                    case ERROR:
                        buider.setTitle(R.string.error);
                        buider.setMessage(R.string.internal_error);
                        buider.show();
                        break;
                }
            }catch (Exception e){
                buider.setTitle(R.string.error);
                buider.setMessage(R.string.internal_error);
                buider.show();
            }
        }else {
            List<Notification> notifications = (List<Notification>)o;
            notificationAdapter.notifyDataSetInvalidated();
            notificationAdapter.setNotifications(notifications);
            notificationAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public Type getType() {
        return null;
    }
}