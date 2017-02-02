package freed.cam.ui.themesample.settings.childs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.troop.freedcam.R;

/**
 * Created by troop on 01.02.2017.
 */

public class GroupChild extends LinearLayout {

    private TextView headerTextview;
    LinearLayout childHolder;

    public GroupChild(Context context, String headername) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.settingsmenu_groupchild, this);
        headerTextview = (TextView)findViewById(R.id.groupchild_header);
        headerTextview.setText(headername);
        childHolder = (LinearLayout)findViewById(R.id.groupchild_childholder);
    }

    public void addView(View view)
    {
        childHolder.addView(view);
    }

    public void clearChilds()
    {
        childHolder.removeAllViews();
    }

    public int childSize()
    {
        return childHolder.getChildCount();
    }
}
