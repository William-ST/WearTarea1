package example.com.notificaciones;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_RESPUESTA_POR_VOZ = "extra_respuesta_por_voz";
    NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;
    final static String MI_GRUPO_DE_NOTIFIC = "mi_grupo_de_notific";
    public static final String EXTRA_MESSAGE="example.com.notificaciones.EXTRA_MESSAGE";
    public static final String ACTION_DEMAND="example.com.notificaciones.ACTION_DEMAND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Miramos si hemos recibido una respuesta por voz
        Bundle respuesta = RemoteInput.getResultsFromIntent(getIntent());
        if (respuesta != null) {
            CharSequence texto = respuesta.getCharSequence(EXTRA_RESPUESTA_POR_VOZ);
            ((TextView) findViewById(R.id.textViewRespuesta)).setText(texto);
        }


        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CANAL_ID, "Mis Notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 100, 300, 100});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Button wearButton = (Button) findViewById(R.id.boton1);
        wearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String s = "Texto largo con descripción detallada de la notificación. ";

                Intent intencionLlamar = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:555123456"));
                PendingIntent intencionPendienteLlamar =
                        PendingIntent.getActivity(MainActivity.this, 0, intencionLlamar, 0);

                // Creamos intención pendiente
                Intent intencionMapa = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=universidad+politecnica+valencia"));
                PendingIntent intencionPendienteMapa =
                        PendingIntent.getActivity(MainActivity.this, 0, intencionMapa, 0);

                // Creamos la acción
                NotificationCompat.Action accion =
                        new NotificationCompat.Action.Builder(R.mipmap.ic_action_call,
                                "llamar Wear", intencionPendienteLlamar).build();

                //Creamos una lista de acciones
                List<NotificationCompat.Action> acciones =
                        new ArrayList<NotificationCompat.Action>();
                acciones.add(accion);
                acciones.add(new NotificationCompat.Action(R.mipmap.ic_action_locate,
                        "Ver mapa", intencionPendienteMapa));

                // Creamos un BigTextStyle para la segunda página
                NotificationCompat.BigTextStyle segundaPg = new NotificationCompat.BigTextStyle();
                segundaPg.setBigContentTitle("Página 2").bigText("Más texto, pág 2.");

                Notification notificacionPg2 = new NotificationCompat.Builder(MainActivity.this)
                        .setStyle(segundaPg)
                        .build();

                // Creamos un BigTextStyle para la segunda página
                NotificationCompat.BigTextStyle terceraPg = new NotificationCompat.BigTextStyle();
                segundaPg.setBigContentTitle("Página 3").bigText("Aún más texto, pág 3.");

                Notification notificacionPg3 = new NotificationCompat.Builder(MainActivity.this)
                        .setStyle(terceraPg)
                        .build();

                // Creamos un BigTextStyle para la segunda página
                NotificationCompat.BigTextStyle cuartaPg = new NotificationCompat.BigTextStyle();
                segundaPg.setBigContentTitle("Página 4").bigText("El último texto, pág 4.");

                Notification notificacionPg4 = new NotificationCompat.Builder(MainActivity.this)
                        .setStyle(cuartaPg)
                        .build();

                List<Notification> notificationPages = new ArrayList<>();
                notificationPages.add(notificacionPg2);
                notificationPages.add(notificacionPg3);
                notificationPages.add(notificacionPg4);

                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true)
                        .setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.escudo_upv))
                        .addActions(acciones)
                        .addPages(notificationPages);

                NotificationCompat.Builder notificacion =
                        new NotificationCompat.Builder(MainActivity.this, CANAL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Título")
                                .setContentText(Html.fromHtml("<b>Notificación</b> <u>Android <i>Wear</i></u>"))
                                .setContentIntent(intencionPendienteMapa)
                                .addAction(R.mipmap.ic_action_call, "llamar", intencionPendienteLlamar)
                                //.extend(new NotificationCompat.WearableExtender().addActions(acciones))
                                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.escudo_upv))
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(s + s + s + s))
                                .extend(wearableExtender)
                                .setGroup(MI_GRUPO_DE_NOTIFIC);
                notificationManager.notify(NOTIFICACION_ID, notificacion.build());
                Toast.makeText(MainActivity.this, "Solo se ve hasta la 2da página wear2", Toast.LENGTH_SHORT).show();


                int idNotificacion2 = 002;
                NotificationCompat.Builder notificacion2 =
                        new NotificationCompat.Builder(MainActivity.this, CANAL_ID)
                                .setContentTitle("Nueva Conferencia")
                                .setContentText("Los neutrinos")
                                .setSmallIcon(R.mipmap.ic_action_mail_add)
                                .setGroup(MI_GRUPO_DE_NOTIFIC);
                notificationManager.notify(idNotificacion2, notificacion2.build());


                int idNotificacion3 = 003;
                NotificationCompat.Builder notificacion3 =
                        new NotificationCompat.Builder(MainActivity.this, CANAL_ID)
                                .setContentTitle("2 notificaciones UPV")
                                .setSmallIcon(R.mipmap.ic_action_attach)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.escudo_upv))
                                .setStyle(new NotificationCompat.InboxStyle()
                                        .addLine("Nueva Conferencia Los neutrinos")
                                        .addLine("Nuevo curso Wear OS")
                                        .setBigContentTitle("2 notificaciones UPV")
                                        .setSummaryText("info@upv.es"))
                                .setNumber(2)
                                .setGroup(MI_GRUPO_DE_NOTIFIC)
                                .setGroupSummary(true);
                notificationManager.notify(idNotificacion3, notificacion3.build());

            }
        });


        Button butonVoz = (Button) findViewById(R.id.boton_voz);
        butonVoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos una intención de respuesta
                Intent intencion = new Intent(MainActivity.this, MainActivity.class);
                PendingIntent intencionPendiente = PendingIntent.getActivity(MainActivity.this, 0, intencion, PendingIntent.FLAG_UPDATE_CURRENT);

                String[] opcRespuesta = getResources().getStringArray(R.array.opciones_respuesta);

                // Creamos la entrada remota para añadirla a la acción
                RemoteInput entradaRemota = new RemoteInput.Builder(EXTRA_RESPUESTA_POR_VOZ)
                        .setLabel("respuesta por voz")
                        .setChoices(opcRespuesta)
                        .build();
                // Creamos la acción
                NotificationCompat.Action accion = new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_set_as, "responder", intencionPendiente)
                        .addRemoteInput(entradaRemota).build();
                // Creamos la notificación
                int idNotificacion = 004;
                NotificationCompat.Builder notificacion4 =
                        new NotificationCompat.Builder(MainActivity.this, CANAL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Respuesta por Voz")
                                .setContentText("Indica una respuesta")
                                .extend(new NotificationCompat.WearableExtender()
                                        .addAction(accion));
                // Lanzamos la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(idNotificacion, notificacion4.build());
            }
        });

        Button buttonVozBroadcast = (Button) findViewById(R.id.boton_voz_broadcast);
        buttonVozBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos una intención de respuesta
                Intent intencion = new Intent(MainActivity.this, WearReceiver.class)
                        .putExtra(EXTRA_MESSAGE, "alguna información relevante")
                        .setAction(ACTION_DEMAND);

                PendingIntent intencionPendiente = PendingIntent.getBroadcast(MainActivity.this, 0, intencion, 0);

                String[] opcRespuesta = getResources().getStringArray(R.array.opciones_respuesta);

                // Creamos la entrada remota para añadirla a la acción
                RemoteInput entradaRemota = new RemoteInput.Builder(EXTRA_RESPUESTA_POR_VOZ)
                        .setLabel("respuesta por voz")
                        .setChoices(opcRespuesta)
                        .build();
                // Creamos la acción
                NotificationCompat.Action accion = new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_set_as, "responder", intencionPendiente)
                        .addRemoteInput(entradaRemota).build();

                // Creamos la notificación
                int idNotificacion = 005;
                NotificationCompat.Builder notificacion4 =
                        new NotificationCompat.Builder(MainActivity.this, CANAL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Respuesta por Voz")
                                .setContentText("Indica una respuesta")
                                .extend(new NotificationCompat.WearableExtender()
                                        .addAction(accion));
                // Lanzamos la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(idNotificacion, notificacion4.build());
            }
        });
    }
}
