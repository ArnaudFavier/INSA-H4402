package hexanome.agenda.customui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import hexanome.agenda.R;
import hexanome.agenda.model.Options;

public class OptionExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> optionsHeader;
    private HashMap<String, List<String>> optionsContent;

    public OptionExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.optionsHeader = listDataHeader;
        this.optionsContent = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.optionsContent.get(this.optionsHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String optionName = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.option_item, null);
        }

        final CheckBox checkboxOption = (CheckBox) convertView.findViewById(R.id.checkbox_option);
        switch (optionName) {
            case "3 IF":
                checkboxOption.setChecked(Options.has3IF);
                break;
            case "4 IF":
                checkboxOption.setChecked(Options.has4IF);
                break;
            case "5 IF":
                checkboxOption.setChecked(Options.has5IF);
                break;
            case "3 GI":
                checkboxOption.setChecked(Options.has3GI);
                break;
            case "4 GI":
                checkboxOption.setChecked(Options.has4GI);
                break;
            case "5 GI":
                checkboxOption.setChecked(Options.has5GI);
                break;
            case "Rotonde":
                checkboxOption.setChecked(Options.hasRotonde);
                break;
            case "Ragda":
                checkboxOption.setChecked(Options.hasRagda);
                break;
            case "BDE":
                checkboxOption.setChecked(Options.hasBDE);
                break;
            case "K-Fête":
                checkboxOption.setChecked(Options.hasKFete);
                break;
            case "URL":
                checkboxOption.setChecked(Options.hasURL);
                break;
            case "Alertes TCL Tramway":
                checkboxOption.setChecked(Options.hasAlertTCLTramway);
                break;
            case "Alertes TCL Métro":
                checkboxOption.setChecked(Options.hasAlertTCLMetro);
                break;
            case "Alertes TCL Bus":
                checkboxOption.setChecked(Options.hasAlertTCLBus);
                break;
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.textview_option);

        checkboxOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkboxOption = (CheckBox) view;
                boolean b = checkboxOption.isChecked();
                switch (optionName) {
                    case "3 IF":
                        Options.has3IF = b;
                        break;
                    case "4 IF":
                        Options.has4IF = b;
                        break;
                    case "5 IF":
                        Options.has5IF = b;
                        break;
                    case "3 GI":
                        Options.has3GI = b;
                        break;
                    case "4 GI":
                        Options.has4GI = b;
                        break;
                    case "5 GI":
                        Options.has5GI = b;
                        break;
                    case "Rotonde":
                        Options.hasRotonde = b;
                        break;
                    case "Ragda":
                        Options.hasRagda = b;
                        break;
                    case "BDE":
                        Options.hasBDE = b;
                        break;
                    case "K-Fête":
                        Options.hasKFete = b;
                        break;
                    case "URL":
                        Options.hasURL = b;
                        break;
                    case "Alertes TCL Tramway":
                        Options.hasAlertTCLTramway = b;
                        break;
                    case "Alertes TCL Métro":
                        Options.hasAlertTCLMetro = b;
                        break;
                    case "Alertes TCL Bus":
                        Options.hasAlertTCLBus = b;
                        break;
                }
            }
        });

        txtListChild.setText(optionName);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.optionsContent.get(this.optionsHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.optionsHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.optionsHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.option_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.textViewOptionHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}