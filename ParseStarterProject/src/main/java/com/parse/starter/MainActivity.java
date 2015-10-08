/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends FragmentActivity implements ItemFragment.OnFragmentInteractionListener , ContentFragment.OnFragmentInteractionListener {


  private ContentFragment contentFragment;

  private FragmentTabHost mTabHost = null;;
  private View indicator = null;

  String TAG = "MainActivity";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    contentFragment = ContentFragment.newInstance();
    mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
    mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);


    mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Tab 1", getResources().getDrawable(
            R.drawable.cart)),
            ItemFragment.class, null);

      mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Tab 2", null),
              ItemFragment.class, null);
  }

    @Override
    public void onFragmentInteraction(Bundle bundle) {
       switch (bundle.getInt("what")){
         //Start content fragment
         case 0: {
           android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
           fm.beginTransaction().replace(android.R.id.tabcontent,contentFragment).commit();
         }break;

      }
    }




  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
