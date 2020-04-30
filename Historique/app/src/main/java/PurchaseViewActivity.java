import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lepl1225.R;

import java.util.ArrayList;

public class PurchaseViewActivity extends Fragment {
    private static final String TAG = "PurchaseViewActivity";
    private PurchaseListe adapter;
    private ListView purchaselistt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewhistorique,container,false);
        purchaselistt = (ListView) view.findViewById(R.id.HistoriqueItems);
        Log.d(TAG, "onCreateView: started");
        SetUpPurchaselist();

        return view;
    }

    private void SetUpPurchaselist(){
        final ArrayList<Purchase> purhcases = new ArrayList<>();
        purhcases.add(new Purchase("007","009","27 avril 2020",5,new Produit()));
        purhcases.add(new Purchase("007","009","27 avril 2020",5,new Produit()));
        purhcases.add(new Purchase("007","009","27 avril 2020",5,new Produit()));
        purhcases.add(new Purchase("007","009","27 avril 2020",5,new Produit()));

        adapter = new PurchaseListe(getActivity(),R.layout.historique_liste,purhcases,"");
        purchaselistt.setAdapter(adapter);

    }
}
