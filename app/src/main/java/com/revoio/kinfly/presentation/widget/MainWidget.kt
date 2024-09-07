package com.revoio.kinfly.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.revoio.kinfly.R
import com.revoio.kinfly.presentation.MainActivity


class MainWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        // Perform this loop procedure for each widget that belongs to this provider.
        appWidgetIds?.forEach { appWidgetId ->
            updateMainWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }


    private fun updateMainWidget(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int
    ) {
        // Create an Intent to launch Activity.
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Get the layout for the widget and attach an onClick listener to the button.
        val views: RemoteViews = RemoteViews(context?.packageName, R.layout.widget_main).apply {
            setOnClickPendingIntent(R.id.mainWidget, pendingIntent)
        }

        // Update your widget's content
        views.setTextViewText(R.id.widget_title, "Updated Widget")


        // Tell the AppWidgetManager to perform an update on the current widget.
        appWidgetManager?.updateAppWidget(appWidgetId, views)

    }
}