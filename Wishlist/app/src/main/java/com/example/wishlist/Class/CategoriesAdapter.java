//package com.example.wishlist.Class;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListAdapter;
//
//import com.example.wishlist.R;
//
//import java.util.ArrayList;
//
//public class CategoriesAdapter extends BaseAdapter implements ListAdapter {
//    private ArrayList<String> list = new ArrayList<String>();
//    private Context context;
//
//
//
//    public CategoriesAdapter(ArrayList<String> list, Context context) {
//        this.list = list;
//        this.context = context;
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int pos) {
//        return list.get(pos);
//    }
//
//    @Override
//    public long getItemId(int pos) {
//        return 0;
//        //just return 0 if your list items do not have an Id variable.
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        View view = convertView;
//        if (view == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(R.layout.category_item, null);
//        }
//
//        //Handle TextView and display string from your list
//        EditText listItemText = (EditText)view.findViewById(R.id.categoryText);
//        listItemText.setText(list.get(position));
//
//        //Handle buttons and add onClickListeners
//        Button deleteBtn = (Button)view.findViewById(R.id.deleteCategory);
//
//        deleteBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //do something
//                list.remove(position); //or some other task
//                notifyDataSetChanged();
//            }
//        });
//
//        return view;
//    }
//}