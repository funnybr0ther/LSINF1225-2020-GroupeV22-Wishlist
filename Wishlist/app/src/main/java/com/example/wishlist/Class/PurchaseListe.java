import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.lepl1225.R;
import java.util.ArrayList;
import java.util.List;

public class PurchaseListe extends ArrayAdapter<Purchase>{

    private LayoutInflater inflater;
    private List<Purchase> purchases = null;
    private int LayoutRessource;
    private Context context;
    private String append;


    public PurchaseListe(@NonNull Context context, int resource, @NonNull List<Purchase> purchases, String ajouter) {
        super(context, resource, purchases);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutRessource = resource;
        this.context = context;
        append = ajouter;
        this.purchases = purchases;
    }

    private static class ViewHolder{
        TextView name;
        ProgressBar progressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // ViewHolder Build pattern start
        final ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(LayoutRessource,parent,false);
            holder = new ViewHolder();  // Crée un widget
            holder.name = (TextView) convertView.findViewById(R.id.HistoriqueItems); // HistoriqueItems > voir historique_liste.xml
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.ProgresseBarHistorique); // " "
            convertView.setTag(holder); // Affiche le widegt
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        String date_ = getItem(position).getDate();
        holder.name.setText(date_);

        return convertView;
    }
}
