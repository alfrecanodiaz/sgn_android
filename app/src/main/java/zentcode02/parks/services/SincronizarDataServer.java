package zentcode02.parks.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.Objects;

import zentcode02.parks.dbModels.ErrorsDB;
import zentcode02.parks.dbModels.Sincronizar;
import zentcode02.parks.utils.DBHelper;
import zentcode02.parks.utils.Global;
import zentcode02.parks.network.Config;
import zentcode02.parks.utils.Helper;

public class SincronizarDataServer extends Service {
    private final LocalBinder mBinder = new LocalBinder();
    protected Handler handler;
    private static String api_token = "lVzBtm8VsshnN1Ra8BqY4KWNHqaLYjLEeaBTsbXKgPQjCOMBBsjviGSSTBJ9";
    private static String params_url = "?api_token=" + api_token;
    static final String serverURL = Config.SERVER_URL +"sincronizacionFinal"+params_url;
    private static final String TAG = "SincronizarDataServer: ";
    private int count = 0;
    private Boolean running;
    private RequestQueue queue;
    private String background_response;
    private Sincronizar sincronizar;
    private Global global;

    public class LocalBinder extends Binder {
        public SincronizarDataServer getService(){
            return SincronizarDataServer .this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        global = new Global(getApplicationContext());
        queue = Volley.newRequestQueue(getApplicationContext());// start one time no more...
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Servicio destruido");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Aca hacer la llamada al servidor para sincronizar
                syncToServer();
            }
        });
        return android.app.Service.START_STICKY;
    }

    private void syncToServer() {
        if (!DBHelper.checkPendingSync())
        {
//            new Thread(new Runnable() {
//                public void run() {
//                    //Aquí ejecutamos nuestras tareas costosas
//                    jsonRequestServer();
//                }
//            }).start();
//            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    jsonRequestServer();
//                }
//            }, 1000);
            jsonRequestServer();
        }else{
            background_response = "Nada para sincronizar";
            Log.d("Log Service ", background_response);
            //restartRequest(background_response);
            onDestroy();
        }
    }

    private void jsonRequestServer() {
    if (!Objects.equals(DBHelper.GetDataFromDatabase(), "next")) {
        running = true;
        Thread MyThread = new Thread(){//create thread
            @Override
            public void run() {
                final String jsonDatosPostServer= DBHelper.GetDataFromDatabase();
                final int sync_id = DBHelper.getSincronizar_id();

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL,
                        new Response.Listener<String>() {//where request return ...
                            @Override
                            public void onResponse(String response) {
                                System.out.print("respuesta Server"+response);
                                responseRequest(sync_id);
                                /*if (sync_id != 0) {
                                    responseRequest(sync_id);
                                } else {
                                    background_response = "ID Sync null";
                                    restartRequest(background_response);
                                }*/
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("request Error", error.getMessage());
                        Log.i(TAG, "Hubo un error");
                        //Log.d("network response", String.valueOf(error.networkResponse));
                        if(error.networkResponse!=null) {
                            Log.d("log del error", String.valueOf(error.networkResponse.statusCode));
                            onRequestError(sync_id, String.valueOf(error.networkResponse.statusCode));
                            //restartRequest(background_response);
                        } else {
                            Log.d(TAG, "Error de Conexión a Internet");
                        }
                        onDestroy();
                    }
                }) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return jsonDatosPostServer.getBytes();
                    }
                };
                stringRequest.setShouldCache(false);// no caching url...
                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(
                                10000,//time to wait for it in this case 20s
                                5,//tryies in case of error
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )
                );
                queue.add(stringRequest);

                /*int i=0;
                while(running){

                    System.out.println("counter: "+i);
                    i++;

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        System.out.println("Sleep interrupted");
                    }

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
                System.out.println("onEnd Thread");*/

            }
        };
        MyThread.start(); // start thread

        //in some button declare
        running=false;
        } else {
            background_response = "Loop next";
            restartRequest(background_response);
        }
    }

//    private void jsonRequestServer() {
//        Log.i(TAG, "Ya se sincronizo, correr la funcion");
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//
//        if (!Objects.equals(DBHelper.GetDataFromDatabase(), "next")) {
//
//            final String jsonDatosPostServer= DBHelper.GetDataFromDatabase();
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_URL,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
////                        count = count + 1;
//                            // Display the first 500 characters of the response string.
//                            System.out.print("respuesta Server"+response);
//                            int sync_id = DBHelper.getSincronizar_id();
//                            if (sync_id != 0) {
//                                responseRequest(sync_id);
//                                syncToServer();
//                                onDestroy();
//                            } else {
//                                syncToServer();
//                                onDestroy();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            VolleyLog.d("request Error", error.getMessage());
//                            Log.i(TAG, "Hubo un error");
//                            onDestroy();
//                        }
//                    }) {
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    return jsonDatosPostServer.getBytes();
//                }
//            };
//            System.out.println("request"+stringRequest);
//            queue.add(stringRequest);
////        if (count > 1)
////        {
////            Log.i(TAG, "Se apago el wifi");
////            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
////            wifi.setWifiEnabled(false);
////        }
//
//        } else {
//            syncToServer();
//        }
//
//    }

    private void responseRequest(int sync_id) {
        sincronizar = Sincronizar.findById(Sincronizar.class, sync_id);
        Log.d("sincronizar DB", sincronizar.toString());
        sincronizar.setEnviado(true);
        sincronizar.save();
        background_response = "OK Volley";
        restartRequest(background_response);
    }

    private void onRequestError(int sync_id, String error_message) {
        ErrorsDB errorsDB = Select.from(ErrorsDB.class).where(Condition.prop(NamingHelper.toSQLNameDefault("sync_id")).eq(sync_id)).first();
        if (errorsDB == null) {
            System.out.println("Entro en el guardado del error");
            sincronizar = Sincronizar.findById(Sincronizar.class, sync_id);
            ErrorsDB requestError = new ErrorsDB(
                    error_message,
                    sincronizar.getId().intValue(),
                    sincronizar.toString(),
                    Helper.getMySqlDateTimeFormat()
            );
            requestError.save();
        } else {
            System.out.println("Registro de Error ya existe");
        }
    }

    private void restartRequest(String log_service) {
        Log.d("Log Service ", log_service);
        //onDestroy();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                syncToServer();
            }
        }, 1000);
    }

}
