package smartwatch.context.project;

import android.os.Bundle;
import android.widget.TextView;

import android.support.wearable.view.WearableListView;
import android.widget.Toast;
import java.util.ArrayList;

import smartwatch.context.common.superclasses.CommonActivity;

public class MainActivity extends CommonActivity {
    private TextView mHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sample icons for the list
        ArrayList<Integer> mIcons = new ArrayList<>();
        mIcons.add(R.drawable.ic_action_locate);
        mIcons.add(R.drawable.ic_action_star);
        mIcons.add(R.drawable.ic_action_user);


        // This is our list header
        mHeader = (TextView) findViewById(R.id.header);

        WearableListView wearableListView =
                (WearableListView) findViewById(R.id.wearable_List);
        wearableListView.setAdapter(new WearableAdapter(this, mIcons));
        wearableListView.setClickListener(mClickListener);
        wearableListView.addOnScrollListener(mOnScrollListener);
    }

    // Handle our Wearable List's click events
    private WearableListView.ClickListener mClickListener =
            new WearableListView.ClickListener() {
                @Override
                public void onClick(WearableListView.ViewHolder viewHolder) {
                    int clickedMenu = viewHolder.getLayoutPosition()+1;
                    switch (clickedMenu){
                        case 1:
                            placeIdString = "1";
                            scanWlan();
                            break;
                        default:
                            Toast.makeText(MainActivity.this,
                                    String.format("You selected item #%s",
                                            viewHolder.getLayoutPosition()+1),
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }

                }

                @Override
                public void onTopEmptyRegionClick() {
                    Toast.makeText(MainActivity.this,
                            "Top empty area tapped", Toast.LENGTH_SHORT).show();
                }
            };

    // The following code ensures that the title scrolls as the user scrolls up
    // or down the list
    private WearableListView.OnScrollListener mOnScrollListener =
            new WearableListView.OnScrollListener() {
                @Override
                public void onAbsoluteScrollChange(int i) {
                    // Only scroll the title up from its original base position
                    // and not down.
                    if (i > 0) {
                        mHeader.setY(-i);
                    }
                }

                @Override
                public void onScroll(int i) {
                    // Placeholder
                }

                @Override
                public void onScrollStateChanged(int i) {
                    // Placeholder
                }

                @Override
                public void onCentralPositionChanged(int i) {
                    // Placeholder
                }
            };
}
