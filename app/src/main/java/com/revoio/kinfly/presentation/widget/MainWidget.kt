package com.revoio.kinfly.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.revoio.kinfly.R
import com.revoio.kinfly.presentation.activity.MainActivity


class MainWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        // Perform this loop procedure for each widget that belongs to this provider.
        appWidgetIds?.forEach { appWidgetId ->
            updateMainWidgetWithMessage(context, appWidgetManager, appWidgetId,null)
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE == intent?.action) {
            val newMessage = intent.getStringExtra("new_message")

            // Get the AppWidgetManager instance
            val appWidgetManager = AppWidgetManager.getInstance(context)

            // Get the current widget IDs
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context!!, MainWidget::class.java)
            )

            // Update the widget with the new message
            appWidgetIds?.forEach { appWidgetId ->
                updateMainWidgetWithMessage(context, appWidgetManager, appWidgetId, newMessage)
            }
        }
    }

    private fun updateMainWidgetWithMessage(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        message: String?
    ) {
        // Create an Intent to launch MainActivity
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Get the layout for the widget and attach an onClick listener to the button.
        val views: RemoteViews = RemoteViews(context?.packageName, R.layout.widget_main).apply {
            setOnClickPendingIntent(R.id.mainWidget, pendingIntent)
            setTextViewText(R.id.widget_title, message ?: "No new messages")
        }

        // Tell the AppWidgetManager to perform an update on the current widget.
        appWidgetManager?.updateAppWidget(appWidgetId, views)
    }


    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }
}