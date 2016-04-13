package com.daemon.mobile.mobilemanager;

import android.test.AndroidTestCase;

import com.daemon.mobile.engine.ReadContactsEngine;
import com.daemon.mobile.utils.ServiceUtils;

/**
 * Created by 10319 on 03/31/16.
 */
public class MyTest extends AndroidTestCase{
        public void testReadContacts(){
            ReadContactsEngine.readContacts(getContext());

        }

        public void testRunningService(){
            ServiceUtils.isServiceRunning(getContext(),"");
        }

}
