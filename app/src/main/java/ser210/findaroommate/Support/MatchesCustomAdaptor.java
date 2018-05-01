package ser210.findaroommate.Support;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.R;

public class MatchesCustomAdaptor extends BaseAdapter {
    public static interface MatchesCustomAdaptorButtonFunctions{
        void onDeletePress(View view, int pos, User user);
        void onViewPress(View view, int pos, User user);
    }

    private ArrayList<User> list = new ArrayList<User>();
    private Context context;
    private LayoutInflater mInflater;
    private MatchesCustomAdaptorButtonFunctions ButtonHandler;

    public MatchesCustomAdaptor(ArrayList<User>List, Context context){
        Log.i("CustomADAPT","Called");
        this.list = List;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        Log.i("CustomADAPT","Size: "+list.size());
        return list.size();
    }

    @Override
    public User getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.getLong(this.getItem(i).getUid());
    }

    @Override
    public View getView(final int pos, View convert, ViewGroup viewGroup) {
        View view = convert;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.matchrow,viewGroup, false);
        }
        TextView text = (TextView)view.findViewById(R.id.list_item_string);
        text.setText(list.get(pos).toString());

        //Button Click Stuff
        Button vb = (Button)view.findViewById(R.id.view_btn);
        Button db = (Button)view.findViewById(R.id.delete_btn);

        vb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ButtonHandler==null)
                    return;
                ButtonHandler.onViewPress(view,pos,list.get(pos));
            }
        });
        db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ButtonHandler==null)
                    return;
                ButtonHandler.onDeletePress(view,pos,list.get(pos));
            }
        });
        return view;
    }

    public MatchesCustomAdaptorButtonFunctions getButtonHandler() {
        return ButtonHandler;
    }

    public void setButtonHandler(MatchesCustomAdaptorButtonFunctions buttonHandler) {
        ButtonHandler = buttonHandler;
    }
}
