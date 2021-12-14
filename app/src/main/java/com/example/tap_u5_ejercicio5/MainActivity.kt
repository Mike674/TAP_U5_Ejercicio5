package com.example.tap_u5_ejercicio5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val baseRemota = FirebaseFirestore.getInstance()
    val lista = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cargarDatosDesdeNube()

        button.setOnClickListener {
            val documento = hashMapOf(
                "nombre" to nombre.text.toString(),
                "telefono" to telefono.text.toString(),
                "edad" to edad.text.toString().toInt()
            )

            baseRemota.collection("TAP")
                .add(documento as Any)
                .addOnSuccessListener {
                    //Si funcionó
                    AlertDialog.Builder(this)
                        .setMessage("SE INSERTÓ CON ÉXITO")
                        .setTitle("Atención")
                        .setPositiveButton("OK", {d, i ->})
                        .show()
                }
                .addOnFailureListener {
                    //No funcionó
                    AlertDialog.Builder(this)
                        .setMessage("ERROR: NO SE INSERTÓ")
                        .setTitle("Atención")
                        .setPositiveButton("OK", {d, i ->})
                        .show()
                }
            nombre.setText("")
            telefono.setText("")
            edad.setText("")
        }
    }

    fun cargarDatosDesdeNube(){
        baseRemota.collection("TAP")
            .addSnapshotListener { value, error ->
                if(error!=null){
                    AlertDialog.Builder(this)
                        .setMessage("ERROR: NO SE PUDO REALIZAR LA CONSULTA")
                        .setTitle("Atención")
                        .setPositiveButton("OK", {d, i ->})
                        .show()
                    return@addSnapshotListener
                }

                lista.clear()
                for(documento in value!!){
                    var cadena = "NOMBRE: "+documento.getString("nombre")+"\nTELÉFONO: "+
                            documento.getString("telefono")+"\nEDAD: "+documento.get("edad").toString()

                    lista.add(cadena)
                }

                listadocumentos.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista)
            }
    }
}