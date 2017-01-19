package hexanome.agenda.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hexanome.agenda.R;
import hexanome.agenda.customui.OptionExpandableListAdapter;
import hexanome.agenda.model.Options;

public class OptionActivity extends AppCompatActivity {

    /**
     * Header of the options possibilities
     */
    List<String> optionsHeader;
    /**
     * Content of the options possibilities
     */
    HashMap<String, List<String>> optionsContent;

    boolean initialTclBus;
    boolean initialTclMetro;
    boolean initialTclTram;

    /**
     * Default constructor
     */
    public OptionActivity() {
        optionsHeader = new ArrayList<>();
        optionsContent = new HashMap<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        initialTclBus = Options.hasAlertTCLBus;
        initialTclMetro = Options.hasAlertTCLMetro;
        initialTclTram = Options.hasAlertTCLTramway;

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Options");

        // Create different options data
        optionsData();

        // Fill with different options
        OptionExpandableListAdapter listAdapter = new OptionExpandableListAdapter(this, optionsHeader, optionsContent);
        ExpandableListView expandable = (ExpandableListView) findViewById(R.id.expandable_view);
        expandable.setAdapter(listAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if ((initialTclBus != Options.hasAlertTCLBus && Options.hasAlertTCLBus) ||
                (initialTclMetro != Options.hasAlertTCLMetro && Options.hasAlertTCLMetro) ||
                (initialTclTram != Options.hasAlertTCLTramway && Options.hasAlertTCLTramway)) {
            Notification.Builder notifBuilder = new Notification.Builder(this)
                    .setContentTitle("Alerte TCL")
                    .setContentText("Grève TCL : Réseau perturbé")
                    .setSmallIcon(R.drawable.ic_notification);

            Intent notificationIntent = new Intent(this, MainActivity.class);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent intent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Notification notif;
            if (android.os.Build.VERSION.SDK_INT >= 16){
                notif = notifBuilder.build();
            } else{
                notif = notifBuilder.getNotification();
            }
            notif.flags |= Notification.FLAG_AUTO_CANCEL;

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1818864, notif);
        }

        finish();
        overridePendingTransition(R.anim.left_to_right_anim, R.anim.right_to_left_anim);
        return true;
    }

    /*
     * Preparing the list of options
     */
    private void optionsData() {
        // Adding options headers
        optionsHeader.add("Ajouter un agenda scolaire");
        optionsHeader.add("Ajouter un agenda associatif");
        optionsHeader.add("Ajouter des notifications");

        // Adding options contents
        List<String> listAgendasScolaire = new ArrayList<>();
        listAgendasScolaire.add("3 IF");
        listAgendasScolaire.add("4 IF");
        listAgendasScolaire.add("5 IF");
        listAgendasScolaire.add("3 GI");
        listAgendasScolaire.add("4 GI");
        listAgendasScolaire.add("5 GI");

        List<String> listAgendasAssociatif = new ArrayList<>();
        listAgendasAssociatif.add("Rotonde");
        listAgendasAssociatif.add("Ragda");
        listAgendasAssociatif.add("BDE");
        listAgendasAssociatif.add("K-Fête");
        listAgendasAssociatif.add("URL");

        List<String> listNotification = new ArrayList<>();
        listNotification.add("Alertes TCL Tramway");
        listNotification.add("Alertes TCL Métro");
        listNotification.add("Alertes TCL Bus");

        // Linking
        optionsContent.put(optionsHeader.get(0), listAgendasScolaire);
        optionsContent.put(optionsHeader.get(1), listAgendasAssociatif);
        optionsContent.put(optionsHeader.get(2), listNotification);
    }
}
