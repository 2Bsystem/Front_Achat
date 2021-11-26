package a2bsystem.com.achat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import a2bsystem.com.achat.Models.Achat;
import a2bsystem.com.achat.R;

public class AchatAdapter extends ArrayAdapter<Achat> {

    private ViewHolder viewHolder;

    private static class ViewHolder {
        private LinearLayout itemView;
    }

    public AchatAdapter(Context context, int textViewResourceId, ArrayList<Achat> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.achat_lines, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.achat_line);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Achat item = getItem(position);
        if (item != null) {


            TextView code = viewHolder.itemView.findViewById(R.id.achat_code);
            code.setText(item.getCode());

            TextView produit = viewHolder.itemView.findViewById(R.id.achat_produit);
            produit.setText(item.getName());

            TextView colis = viewHolder.itemView.findViewById(R.id.achat_colis);
            colis.setText(item.getColis()+"");

            TextView pieces = viewHolder.itemView.findViewById(R.id.achat_pieces);
            pieces.setText(item.getPieces()+"");

            TextView poids = viewHolder.itemView.findViewById(R.id.achat_poids);
            poids.setText(item.getPdsNet()+"");

            TextView pu = viewHolder.itemView.findViewById(R.id.achat_pu);
            pu.setText(item.getPu()+"");

            TextView montant = viewHolder.itemView.findViewById(R.id.achat_montant);
            montant.setText(item.getMontant() +" €");

            if(item.getStatut() == 2){
                code.setBackgroundResource(R.color.red);
                produit.setBackgroundResource(R.color.red);
                colis.setBackgroundResource(R.color.red);
                pieces.setBackgroundResource(R.color.red);
                poids.setBackgroundResource(R.color.red);
                pu.setBackgroundResource(R.color.red);
                montant.setBackgroundResource(R.color.red);
            }
            else if(item.getStatut() == 3){
                code.setBackgroundResource(R.color.border);
                produit.setBackgroundResource(R.color.border);
                colis.setBackgroundResource(R.color.border);
                pieces.setBackgroundResource(R.color.border);
                poids.setBackgroundResource(R.color.border);
                pu.setBackgroundResource(R.color.border);
                montant.setBackgroundResource(R.color.border);
            }
            else {
                code.setBackgroundResource(R.color.colorWhite);
                produit.setBackgroundResource(R.color.colorWhite);
                colis.setBackgroundResource(R.color.colorWhite);
                pieces.setBackgroundResource(R.color.colorWhite);
                poids.setBackgroundResource(R.color.colorWhite);
                pu.setBackgroundResource(R.color.colorWhite);
                montant.setBackgroundResource(R.color.colorWhite);
            }
        }

        return convertView;
    }
}
