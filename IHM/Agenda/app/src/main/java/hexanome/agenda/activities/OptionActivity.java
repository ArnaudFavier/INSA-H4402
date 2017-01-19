package hexanome.agenda.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hexanome.agenda.R;
import hexanome.agenda.customui.OptionExpandableListAdapter;

public class OptionActivity extends AppCompatActivity {

    /**
     * Header of the options possibilities
     */
    List<String> optionsHeader;
    /**
     * Content of the options possibilities
     */
    HashMap<String, List<String>> optionsContent;

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

        getSupportActionBar().setTitle("Options");

        // Create different options data
        optionsData();

        // Fill with different options
        OptionExpandableListAdapter listAdapter = new OptionExpandableListAdapter(this, optionsHeader, optionsContent);
        ExpandableListView expandable = (ExpandableListView) findViewById(R.id.expandable_view);
        expandable.setAdapter(listAdapter);
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
