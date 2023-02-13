package com.figgo.cabs.figgodriver

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LiveRouting {
    fun firebaseInit(rideId:Int){
        val database = FirebaseDatabase.getInstance()
        val customerRef = database.getReference("$rideId customer")
        val driverRef = database.getReference("$rideId driver")
        var count:Int = 100
        while(count-- >=0) {
            val driverData = driverRef.child("loc $count")
            driverData.child("LAT ").setValue("23902839283")
            driverData.child("LON ").setValue("23902839283")
        }
        customerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })
    }
}