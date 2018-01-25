package com.greenarobean.regul2.regulautomobile;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar = null;
    EditText pointage_start_time,pointage_time_expected,pointage_distance;
    private int mHour, mMinute;
    private int[] pointage = new int[3];
    TimePickerDialog ttd;
    EditText objEditText;
    TextView pointage_timeBeforeStart;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //this.setPage("pointage", "StartPageDebug");
        setPage("speciale_card", "StartPageDebug");

        FloatingActionButton btnNewSpeciale = (FloatingActionButton) findViewById(R.id.btnNewSpeciale);
        btnNewSpeciale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                setPage("speciale_card", "Nouvelle spéciale");

            }
        });

        FloatingActionButton btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView pointage_moyenne = findViewById(R.id.pointage_moyenne);
                pointage_timeBeforeStart = findViewById(R.id.pointage_timeBeforeStart);
                ToggleButton pointage_btn_today = findViewById(R.id.pointage_btn_today);

                //debug
                pointage[0] = 19 * 3600 + 05 * 60;
                pointage[1] = 2 * 3600;

                if(pointage[1] == 0 || pointage[0] == 0) {

                    pointage_moyenne.setText("Le temps imparti et la distance à parcourir sont nécessaires pour le calcul de la moyenne");
                }
                else {
                    float moyenne = ( (float) pointage[2] / 1000) / ((float) pointage[1] / 3600) ;

                    pointage_moyenne.setText("Moyenne : "+String.valueOf(moyenne)+" km/h");
                }

                Calendar now = new GregorianCalendar();
                Calendar depart = new GregorianCalendar();
                depart.set(Calendar.HOUR_OF_DAY, 0);
                depart.set(Calendar.MINUTE, 0);
                depart.set(Calendar.SECOND, 0);
                depart.set(Calendar.MILLISECOND, 0);

                depart.add(Calendar.SECOND, pointage[0] );

                Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
                date.set(Calendar.HOUR_OF_DAY, 0);
                date.set(Calendar.MINUTE, 0);
                date.set(Calendar.SECOND, 0);
                date.set(Calendar.MILLISECOND, 0);

                date.add(Calendar.SECOND, pointage[0] + pointage[1] );

                TextView pointage_txt_arrival = findViewById(R.id.pointage_txt_arrival);

                if(pointage_btn_today.isChecked()) {

                    date.add(Calendar.DAY_OF_MONTH,1);
                    depart.add(Calendar.DAY_OF_MONTH,1);
                }

                /*LocalDateTime now = LocalDateTime.now();

                String hour_arrival = now.format(DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH));
*/
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.FRANCE);
                //Log.d("REGUL1", date.toString());
                pointage_txt_arrival.setText(dateFormat.format(date.getTime()));

                Long diff = depart.getTimeInMillis() - now.getTimeInMillis();
                Log.d("REGUL1", String.valueOf(diff));

                if(timer != null) {
                    timer.cancel();
                }
                timer = new CountDownTimer(diff, 1000) {

                    public void onTick(long millisUntilFinished) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.FRANCE);
                        pointage_timeBeforeStart.setText(dateFormat.format(millisUntilFinished));

                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        pointage_timeBeforeStart.setText(R.string.countdownfinish);
                    }

                }.start();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pointage_start_time = findViewById(R.id.pointage_start_time);
        pointage_start_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 setHour(v.getContext(), pointage_start_time);

            }
        });
        pointage_time_expected = findViewById(R.id.pointage_time_expected);
        pointage_time_expected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setHour(v.getContext(), pointage_time_expected);

            }
        });

        pointage_distance = findViewById(R.id.pointage_distance);
        pointage_distance.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(pointage_distance.getText().toString()!="") pointage[2] = Integer.parseInt(pointage_distance.getText().toString());
                //Log.d("REGUL1", pointage_distance.getText().toString());

            }
        });
    }

    private void setHour(Context context, EditText lobjEditText) {

        objEditText = lobjEditText;

        ttd = new TimePickerDialog(context,new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {

                mHour = hourOfDay;
                mMinute = minute;

                objEditText.setText(String.format("%02d", hourOfDay) + ":"
                        + String.format("%02d", minute) );

                int i = 0;
                if(objEditText.getId() == R.id.pointage_start_time) i = 0;
                if(objEditText.getId() == R.id.pointage_time_expected) i = 1;

                pointage[i] = hourOfDay * 3600 + minute * 60;

            }
        }, mHour, mMinute, true);

        ttd.setTitle(getString(R.string.select_hour));
        ttd.show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_moyenne) {
            // Handle the camera action
        } else if (id == R.id.nav_pointage) {

           this.setPage("pointage", "Pointage");
        } else if (id == R.id.nav_cadenceur) {

        } else if (id == R.id.nav_etalon) {

        } else if (id == R.id.nav_speciale) {

            this.setPage("speciale", getString(R.string.speciales));

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setPage(String page, String title) {

        toolbar.setTitle(title);

        ConstraintLayout inc = null;

        inc = (ConstraintLayout) findViewById(R.id.inc_pointage);
        inc.setVisibility(View.GONE);
        inc = (ConstraintLayout) findViewById(R.id.inc_speciale);
        inc.setVisibility(View.GONE);
        inc = (ConstraintLayout) findViewById(R.id.inc_speciale_card);
        inc.setVisibility(View.GONE);

        int idLayout = getResources().getIdentifier("inc_"+page, "id", getPackageName());
//Log.e("REGUL1", String.valueOf(idLayout));
        if(idLayout>0) {
            inc = (ConstraintLayout) findViewById(idLayout);
            inc.setVisibility(View.VISIBLE);
        }

    }
}