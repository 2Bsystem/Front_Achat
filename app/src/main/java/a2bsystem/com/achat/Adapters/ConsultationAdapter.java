package a2bsystem.com.achat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import a2bsystem.com.achat.Models.Consultation;
import a2bsystem.com.achat.R;

public class ConsultationAdapter extends ArrayAdapter<Consultation> {
    private ViewHolder viewHolder;

    private static class ViewHolder {
        private LinearLayout itemView;
    }

    public ConsultationAdapter(Context context, int textViewResourceId, ArrayList<Consultation> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.consultation_lines, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.consultation_line);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Consultation item = getItem(position);
        if (item != null) {

            TextView site = viewHolder.itemView.findViewById(R.id.consultation_site);
            site.setText(item.getSite());

            TextView fourni = viewHolder.itemView.findViewById(R.id.consultation_fourni);
            fourni.setText(item.getFourni());

            TextView date = viewHolder.itemView.findViewById(R.id.consultation_date);
            date.setText(item.getDateRecep());

            TextView tonnage = viewHolder.itemView.findViewById(R.id.consultation_tonnage);
            tonnage.setText(item.getTonnage());

            TextView montant = viewHolder.itemView.findViewById(R.id.consultation_montant);
            montant.setText(item.getMontant() +" €");

            if(!item.getStatut()){
                site.setBackgroundResource(R.color.red);
                fourni.setBackgroundResource(R.color.red);
                date.setBackgroundResource(R.color.red);
                tonnage.setBackgroundResource(R.color.red);
                montant.setBackgroundResource(R.color.red);
            }
            else {
                site.setBackgroundResource(R.color.colorWhite);
                fourni.setBackgroundResource(R.color.colorWhite);
                date.setBackgroundResource(R.color.colorWhite);
                tonnage.setBackgroundResource(R.color.colorWhite);
                montant.setBackgroundResource(R.color.colorWhite);
            }

        }

        return convertView;
    }
}