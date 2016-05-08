package com.ramimartin.sample.multibluetooth;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Youngdo on 2016-04-25.
 */
public class RegistrationIntentService extends IntentService
{
    public RegistrationIntentService()
    {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        InstanceID instanceID = InstanceID.getInstance(this);
        try
        {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i("onHandleIntent", "|" + token + "|");
            PreferenceManager.instance(this).sentToken(true);

            Intent registrationCompleteIntent = new Intent(getString(R.string.action_registration_complete));
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationCompleteIntent);

            sendToServer(token);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void sendToServer(String token)
    {
        Log.i("sendToServer", "send to server");
    }
}