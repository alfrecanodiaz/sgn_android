package zentcode02.parks;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import zentcode02.parks.fragments.FormCategoriesFragment;
import zentcode02.parks.fragments.ModulosFragment;
import zentcode02.parks.network.Config;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.services.GPSTracker;
import zentcode02.parks.services.SincronizarDataServer;
import zentcode02.parks.utils.Global;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private GPSTracker gpsTracker;
    private AlertDialog.Builder alertDialogBuilder;
    private PreferenceManager preferenceManager;
    private int travesia_id;
    private View header;
    private LinearLayout headerContainer;
    private AlertDialog.Builder alert;
    private Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Iniciar el servicio
//        startAlarm();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("SGN");

        preferenceManager = new PreferenceManager(MainActivity.this);
        global = new Global(MainActivity.this);

        alert = new AlertDialog.Builder(this);

        gpsTracker = new GPSTracker(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txt_version = (TextView) navigationView.findViewById(R.id.label_version);
        txt_version.setText(global.getCurrentVersion());

        //Header
        header=navigationView.getHeaderView(0);
        //Set User Name
        String user_name = preferenceManager.getUserName();
        TextView txtUserName = (TextView) header.findViewById(R.id.txtUserNameApp);
        String label_usuario = "Usuario: " + user_name;
        txtUserName.setText(label_usuario);
        //Set header
        headerContainer = (LinearLayout) header.findViewById(R.id.headerContainer);
        headerContainer.setVisibility(View.GONE);
        setHeaderSideBar();

        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermissionsGPS()) {
                System.out.println("enable gps");
                enableGpsService();
            }
        } else {
            enableGpsService();
        }

        startView();
    }

    private void startView() {
        setTitle("Inicio");
        StartFragment fragment = new StartFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void setHeaderSideBar() {
        int check_data = 0;
        //Set unidad y destino
        String unidad = preferenceManager.getNombreUnidad();
        String destino = preferenceManager.getDestinoTravesia();
        TextView txtUnidad = (TextView) header.findViewById(R.id.txtUnidadSideBar);
        TextView txtDestino = (TextView) header.findViewById(R.id.txtDestinoSideBar);
        if (unidad != null && !Objects.equals(unidad, "")) {
            String label_unidad = "Unidad: " + unidad;
            txtUnidad.setText(label_unidad);
            check_data=1;
        }
        if (destino != null && !Objects.equals(destino, "")) {
            String label_destino = "Destino: " +destino;
            txtDestino.setText(label_destino);
            check_data=1;
        }
        if (check_data==1){
            headerContainer.setVisibility(View.VISIBLE);
        }
    }

    public void getSharedPreferences() {
        //Get travesia id shared preferences
        travesia_id = preferenceManager.getTravesiaId();
    }

//    public void startAlarm(View view) {
    public void startAlarm() {
//        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        int interval = 10000;
//
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//        Toast.makeText(this, "Servicio iniciado", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, SincronizarDataServer.class);
//        startService(intent);
        Intent ishintent = new Intent(this, SincronizarDataServer.class);
        final PendingIntent pintent = PendingIntent.getService(this, 0, ishintent, 0);
        final AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //alarm.cancel(pintent);
        Thread serviceThread = new Thread() {//create thread
            @Override
            public void run() {
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),60000, pintent);
            }
        };
        serviceThread.start();
//        Toast.makeText(this, "Servicio iniciado", Toast.LENGTH_SHORT).show();
    }

//    public void cancelAlarm(View view) {
    public void cancelAlarm() {
//        if (manager != null) {
//            manager.cancel(pendingIntent);
//            Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
//        }
        Intent intent = new Intent(this, SincronizarDataServer.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
        stopService(intent);
        Toast.makeText(this, "Servicio parado", Toast.LENGTH_SHORT).show();
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

        switch (id) {
            case R.id.action_logout:
                logoutApp();
                break;
            /*case R.id.action_config:
                DebugFragment fragmentDebug = new DebugFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                fragmentTransaction.replace(R.id.fragment_container, fragmentDebug);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;*/
        }

        return super.onOptionsItemSelected(item);
    }

    public void logoutApp() {
        if (isNetworkAvailable()) {
            clearSharedPreferences();
        } else {
            AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
            alertLogout.setTitle("Atención!");
            alertLogout.setIcon(android.R.drawable.ic_dialog_alert);
            alertLogout.setMessage("Error al cerrar la sesión actual, se requiere conexión a internet para proceder.");
            alertLogout.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertLogout.setCancelable(false);
            alertLogout.show();
        }
    }

    private void clearSharedPreferences() {
        preferenceManager.clearUserId();
        preferenceManager.clearUserName();
        preferenceManager.clearApiToken();
        redirectToLoginActivity();
    }

    private void redirectToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Get shared preferences, en el on create view y on create solo se trae la primera vez
        getSharedPreferences();
        alert.setTitle("Atención!");
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.setCancelable(false);
        switch (id) {
            case R.id.nav_home:
                StartFragment fragmentHome = new StartFragment();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                fragmentTransaction.replace(R.id.fragment_container, fragmentHome);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.nav_travesia:
                if (checkStatusOptions(0)) {
                    FormCategoriesFragment fragmentCategories = new FormCategoriesFragment();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    fragmentTransaction.replace(R.id.fragment_container, fragmentCategories);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    alert.show();
                }
                break;
            case R.id.nav_status_sync:
                DebugFragment debug = new DebugFragment();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                fragmentTransaction.replace(R.id.fragment_container, debug);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                /*if (checkStatusOptions(0)) {
                    ReportErrorsFragment reportErrorsFragment = new ReportErrorsFragment();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    fragmentTransaction.replace(R.id.fragment_container, reportErrorsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    alert.show();
                }*/
                break;
            /*case R.id.nav_historial:
                if (checkStatusOptions(0)) {
                    TravesiasHistorial fragmentHistorial = new TravesiasHistorial();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    fragmentTransaction.replace(R.id.fragment_container, fragmentHistorial);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    alert.show();
                }
                break;*/
            case R.id.nav_sincronizar:
                if (checkStatusOptions(1)) {
                    ModulosFragment fragmentSincronizar = new ModulosFragment();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    fragmentTransaction.replace(R.id.fragment_container, fragmentSincronizar);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    alert.show();
                }
                break;
            case R.id.nav_cerrarsesion:
                logoutApp();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkStatusOptions(int sincronizar) {
        if (travesia_id == 0) {
            if (sincronizar == 0) {
                alert.setMessage("No se sincronizo ninguna travesia.");
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean checkPermissionsGPS() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100);
            return true;
        }
        return false;
    }

    private boolean checkPermissionsCamera() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.CAMERA
            }, 200);
            return true;
        }
        return false;
    }

    private boolean checkPermissionsStorage() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 300);
            return true;
        }
        return false;
    }

    private void startPermissionsCheck(int flag_alert, final int check) {
        if (flag_alert == 1) {
            alertDialogBuilder.setTitle("Permiso requerido!");
            alertDialogBuilder.setMessage(Config.PERMISSION_DENIED);
            alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (check == 1) {
                                checkPermissionsGPS();
                            } else if (check == 2) {
                                checkPermissionsCamera();
                            } else  if (check == 3) {
                                checkPermissionsStorage();
                            }
                        }
                    }, 1000);
                }
            });
//            alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                }
//            });
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.show();
        } else if (flag_alert == 2) {
            gpsTracker.stopUsingGPS();
            stopService(new Intent(this, GPSTracker.class));
            alertDialogBuilder.setTitle("Permiso requerido!");
            alertDialogBuilder.setMessage(Config.PERMISSION_DENIED_PERMANENT);
            alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                                Uri.fromParts("package", getPackageName(), null));
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (check == 1) {
                                checkPermissionsGPS();
                            } else if (check == 2) {
                                checkPermissionsCamera();
                            } else  if (check == 3) {
                                checkPermissionsStorage();
                            }
                        }
                    }, 1000);
                }
            });
//            alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (check == 1) {
//                                checkPermissionsGPS();
//                            } else if (check == 2) {
//                                checkPermissionsCamera();
//                            } else  if (check == 3) {
//                                checkPermissionsStorage();
//                            }
//                        }
//                    }, 5000);
//                }
//            });
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.show();
        } else if (check != 0) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (check == 1) {
                        checkPermissionsGPS();
                    } else if (check == 2) {
                        checkPermissionsCamera();
                    } else  if (check == 3) {
                        checkPermissionsStorage();
                    }
                }
            }, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        alertDialogBuilder = new AlertDialog.Builder(this);
        int count;
        int flag_alert = 0;
        int flag_stop = 0;
        //check = 1 (GPS), check = 2 (CAMERA), check = 3 (STORAGE)
        int check;

        //GPS Y LOCATION PERMISSIONS
        switch (requestCode) {
            case 100:
                count = 0;
                check = 1;

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    flag_stop = 1;
                    enableGpsService();
                    check = 2;
                }

                if (flag_stop != 1) {
                    // for each permission check if the user granted/denied them
                    // you may want to group the rationale in a single dialog,
                    // this is just an example
                    for (int i = 0, len = permissions.length; i < len; i++) {
                        String permission = permissions[i];
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            // user rejected the permission
                            flag_alert = 1;
                            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                            if (! showRationale) {
                                // user also CHECKED "never ask again"
                                // you can either enable some fall back,
                                // disable features of your app
                                // or open another dialog explaining
                                // again the permission and directing to
                                // the app setting
                                flag_alert = 2;
                                check = 2;
                            }
                        } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            // user accept the permission
                            count = count + 1;
                        }
                    }
                    Log.d("count permission 1", String.valueOf(count));
                    if (count == 2) {
                        enableGpsService();
                        check = 2;
                    }
                }
                startPermissionsCheck(flag_alert, check);

                break;

            case 200:
                count = 0;
                check = 2;

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    flag_stop = 1;
                    check = 3;
                }

                if (flag_stop != 1) {

                    // for each permission check if the user granted/denied them
                    // you may want to group the rationale in a single dialog,
                    // this is just an example
                    for (int i = 0, len = permissions.length; i < len; i++) {
                        String permission = permissions[i];
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            // user rejected the permission
                            flag_alert = 1;
                            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                            if (! showRationale) {
                                // user also CHECKED "never ask again"
                                // you can either enable some fall back,
                                // disable features of your app
                                // or open another dialog explaining
                                // again the permission and directing to
                                // the app setting
                                flag_alert = 2;
                                check = 3;
                            }
                        } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            // user accept the permission
                            count = count + 1;
                        }
                    }
                    Log.d("count permission 2", String.valueOf(count));
                    if (count == 1) {
                        check = 3;
                    }
                }
                startPermissionsCheck(flag_alert, check);

                break;

            case 300:
                count = 0;
                check = 3;

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    flag_stop = 1;
                    check = 0;
                }

                if (flag_stop != 1) {
                    // for each permission check if the user granted/denied them
                    // you may want to group the rationale in a single dialog,
                    // this is just an example
                    for (int i = 0, len = permissions.length; i < len; i++) {
                        String permission = permissions[i];
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            // user rejected the permission
                            flag_alert = 1;
                            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                            if (! showRationale) {
                                // user also CHECKED "never ask again"
                                // you can either enable some fall back,
                                // disable features of your app
                                // or open another dialog explaining
                                // again the permission and directing to
                                // the app setting
                                flag_alert = 2;
                                check = 0;
                            }
                        } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            // user accept the permission
                            count = count + 1;
                        }
                    }
                    Log.d("count permission 3", String.valueOf(count));
                    if (count >= 2) {
                        check = 0;
                    }
                }
                startPermissionsCheck(flag_alert, check);

                break;
        }

    }

    private void enableGpsService() {
        Intent intent = new Intent(getApplicationContext(), GPSTracker.class);
        startService(intent);
        getFirstLocation();
    }

    private void getFirstLocation() {
        GPSTracker gps = new GPSTracker(this);
        if (gps.isGpsEnabled()) {
            gps.getLocation();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
