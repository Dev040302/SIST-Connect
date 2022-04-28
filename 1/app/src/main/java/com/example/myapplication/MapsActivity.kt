package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.myapplication.databinding.ActivityMapsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_maps.*
import com.google.android.gms.maps.model.CameraPosition




class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        search.setOnClickListener {

            if(bus_no.text.isEmpty()){
                bus_no.setError("Bus Number Required")
                return@setOnClickListener
            }
            else{
                var ref=Firebase.database.reference.child("Bus").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.child(bus_no.text.toString()).exists()){
                            var lat:Double = snapshot.child(bus_no.text.toString()).child("lat").getValue() as Double
                            var long:Double = snapshot.child(bus_no.text.toString()).child("long").getValue() as Double

                            AddMarker(lat,long,bus_no.text.toString())
                        }
                        else{

                            Toast.makeText(this@MapsActivity,"NO Bus Found",Toast.LENGTH_LONG).show()

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }
    }

    private fun AddMarker(lat: Double, long: Double, toString: String) {
        val marker = LatLng(lat,long)
        mMap.addMarker(MarkerOptions().position(marker).title("Marker in $toString"))
        val cameraPosition = CameraPosition.Builder()
            .target(marker)
            .zoom(17f)
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}