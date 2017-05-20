package com.enet.smartrestaurent;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CheckUpdatesService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.enet.smartrestaurent.action.FOO";
    private static final String ACTION_BAZ = "com.enet.smartrestaurent.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.enet.smartrestaurent.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.enet.smartrestaurent.extra.PARAM2";

    public CheckUpdatesService() {
        super("CheckUpdatesService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionUpdateCheck(Context context, String param1, String param2) {
        Intent intent = new Intent(context, CheckUpdatesService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionCheckUpdates(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {

            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCheckUpdates(String action, String param2) {
        while (true) {
            CommunicationService.sendToServer(this, "updates", Constants.CHECKUPDATES_ACTION);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
