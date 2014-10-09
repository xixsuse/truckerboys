package truckerboys.otto;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import truckerboys.otto.utils.LocationHandler;
import truckerboys.otto.utils.eventhandler.EventTruck;
import truckerboys.otto.utils.eventhandler.IEventListener;
import truckerboys.otto.utils.eventhandler.events.Event;
import truckerboys.otto.utils.eventhandler.events.NewDestination;
import truckerboys.otto.utils.tabs.SlidingTabLayout;
import truckerboys.otto.utils.tabs.TabPagerAdapter;

public class MainActivity extends FragmentActivity implements IEventListener {
    private ViewPager viewPager;
    private TabPagerAdapter pagerAdapter;
    private LocationHandler locationHandler;
    private OTTO otto;

    public static final String SETTINGS = "Settings_file";
    public static final String STATS = "Stats_file";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        otto = new OTTO();
        locationHandler = new LocationHandler(this);

        //Create standard view with a ViewPager and corresponding tabs.
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), otto.getViews());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(2);

        /*
         * Make sure we never have to reload any of the tabs after the app has been started.
         * This makes the users interaction experience with the app alot smoother.
         */
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);

        //Use Googles 'SlidingTabLayout' to display tabs for all views.
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab_slider);
        slidingTabLayout.setViewPager(viewPager);


        // Turns display properties (alive on/off) based on saved settings file
        if(getSharedPreferences(SETTINGS, 0).getBoolean("displayAlive", true)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        EventTruck.getInstance().subscribe(this);
    }

    public OTTO getOtto () {
        return otto;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void performEvent(Event event) {
        // Sets the current page to Map if a new destination is set
        if(event.isType(NewDestination.class)) {
            viewPager.setCurrentItem(0);
        }
    }
}
