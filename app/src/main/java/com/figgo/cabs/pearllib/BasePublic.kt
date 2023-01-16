package com.figgo.cabs.pearllib

import android.content.Context
import androidx.appcompat.app.AppCompatActivity


abstract class BasePublic : BaseClass() {
    fun CheckSession(baseApbcContext: Context?, activityIn: AppCompatActivity) {
        session = Session(baseApbcContext!!)
        val token: String = session!!.token.toString()
        var session_available = false
        if (session!!.hasSession!!) {
            if (!token.isEmpty()) {
                session_available = true
                printLogs(
                    "BasePublic ",
                    "CheckSession ",
                    "is_session_available - $session_available"
                )
            } else {
                session_available = false
                printLogs(
                    "BasePublic ",
                    "CheckSession ",
                    "is_session_available - $session_available"
                )
            }
        } else {
            session_available = false
            printLogs("BasePublic ", "CheckSession ", "is_session_available - $session_available")
        }
      /*  if (session_available) {
            val intent = Intent(baseApbcContext, UDashboard::class.java)
            startActivity(intent)
            activityIn.finish()
        }*/
    }
}