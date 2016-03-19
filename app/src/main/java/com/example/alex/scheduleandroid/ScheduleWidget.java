package com.example.alex.scheduleandroid;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.alex.scheduleandroid.database.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class ScheduleWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        SharedPreferences preferences = context.getSharedPreferences(Constants.GROUP_USER, Context.MODE_PRIVATE);
        String userGrp = preferences.getString(Constants.GROUP_USER, "");
        String fullWidgetText;

        if (!userGrp.equals("")){
            DatabaseManager databaseManager = new DatabaseManager(context);

            if (!databaseManager.isLessonEmpty()) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

                String dateStr = simpleFormat.format(calendar.getTime());

                int firstLesson = databaseManager.getNumberLesson(dateStr, userGrp);

                if (firstLesson != 0) {
                    String widgetText = context.getString(R.string.textWidget);
                    fullWidgetText = String.format(widgetText, String.valueOf(firstLesson));
                } else {
                    fullWidgetText = context.getString(R.string.noLessonsTextWidget);
                }
            } else {
                fullWidgetText = context.getString(R.string.noDateTextWidget);
            }

            databaseManager.closeDatabase();
        } else {
            fullWidgetText = context.getString(R.string.noGroupTextWidget);
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.schedule_widget);
        views.setTextViewText(R.id.appwidget_text, fullWidgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

