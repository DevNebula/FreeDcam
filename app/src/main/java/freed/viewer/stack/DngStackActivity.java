package freed.viewer.stack;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ortiz.touch.TouchImageView;
import com.troop.freedcam.R;

import freed.ActivityAbstract;
import freed.jni.DngStack;
import freed.utils.LocationHandler;
import freed.viewer.dngconvert.DngConvertingFragment;
import freed.viewer.holder.FileHolder;

/**
 * Created by troop on 25.10.2016.
 */

public class DngStackActivity extends ActivityAbstract
{
    private String[] filesToStack = null;
    private TouchImageView imageView;
    private Button stackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dngstackactivity);
        stackButton = (Button)findViewById(R.id.button_dngstack);
        stackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DngStack stack = new DngStack(filesToStack);
                stack.StartStack();
                stackButton.setBackgroundResource(R.drawable.stack_done);
                stackButton.setClickable(false);
            }
        });
        imageView = (TouchImageView)findViewById(R.id.imageview_dngstack);
        filesToStack = getIntent().getStringArrayExtra(DngConvertingFragment.EXTRA_FILESTOCONVERT);
        ((TextView)findViewById(R.id.rawList)).setText(filesToStack.length+"");

    }

    @Override
    public LocationHandler getLocationHandler() {
        return null;
    }

    @Override
    public void WorkHasFinished(FileHolder fileHolder) {

    }

    @Override
    public void WorkHasFinished(FileHolder[] fileHolder) {

    }
}
