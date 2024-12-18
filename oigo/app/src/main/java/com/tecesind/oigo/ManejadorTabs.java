package com.tecesind.oigo;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.tecesind.oigo.TraducirLSB.controlador.Traductor;
import com.tecesind.oigo.aprenderLSB.controlador.Tutor;
import com.tecesind.oigo.conversarLSB.controlador.Conversacion;
import com.tecesind.oigo.evaluarLSB.controlador.EvaluarLSB;

import dato.DaoMaster;

/**
 * Created by Rosember on 11/1/2015.
 */
public class ManejadorTabs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private MyViewPager viewPager;
    private ActionBar actionBar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private int[] tabIcons = {
            R.drawable.ic_action_social_people,
            R.drawable.ic_action_social_school,
            R.drawable.ic_action_communication_forum
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ManejadorTabs", "Inicia el manejador de tabs");
        setContentView(R.layout.activity_principal);

        if (DaoMaster.getSession()==null){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "dboigo", null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster= new DaoMaster(db);
            daoMaster.newSession();
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Traductor");
        navigationView = (NavigationView)findViewById(R.id.nav_view);


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (MyViewPager) findViewById(R.id.viewPagerTabs);
        setUpViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    //toggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    toggle.setDrawerIndicatorEnabled(false);
                    toggle.syncState();
                    actionBar.setHomeButtonEnabled(false);
                    actionBar.setTitle("Tratuctor");
                }else {

                    if (position == 1) {
                        //drawer.setEnabled(true);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        //toggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
                        toggle.setDrawerIndicatorEnabled(true);
                        toggle.syncState();
                        actionBar.setHomeButtonEnabled(true);
                        //Tutor.newInstance(1);
                        actionBar.setTitle("Aprendizaje");
                    } else {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        //toggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        toggle.setDrawerIndicatorEnabled(false);
                        toggle.syncState();
                        actionBar.setHomeButtonEnabled(false);
                        actionBar.setTitle("Conversación");
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.post(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //toggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();
        actionBar.setHomeButtonEnabled(false);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }


        });

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

    private void setUpViewPager(ViewPager viewPager) {
        AdaptadorTabs adapter = new AdaptadorTabs(getSupportFragmentManager());
        adapter.addFragment(Traductor.newInstance(), "ONE");
        adapter.addFragment(Tutor.newInstance(1,"Alfabeto"), "TWO");
        adapter.addFragment(Conversacion.newInstance(), "THREE");
        viewPager.setAdapter(adapter);
    }


    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.opAlfabeto) {
            // Handle the camera action
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(1);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opNumeros) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(2);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opNormas) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(3);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opFamilia) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(4);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opLugares) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(5);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opDeportes) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(6);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opComidas) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(7);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opAlimentos) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(8);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opTrabajo) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(9);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opColores) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(10);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opSemana) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(11);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opMeses) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(12);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opTiempos) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(13);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opPronombres) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(14);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opInterrogacion) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(15);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opAntonimos) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(16);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opSustantivos) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(17);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opDepartamentos) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(18);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opVerbos) {
            Tutor tutor = Tutor.getInstance();
            tutor.setModulo(19);
            tutor.setNombre(menuItem.getTitle().toString());
            tutor.cargarModulo();
        } else if (id == R.id.opEvaluacion) {
            Intent i= new Intent(this, EvaluarLSB.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}

