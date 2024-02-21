package com.example.practicamagic

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.practicamagic.entities.Pedido
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PedidosService : Service() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var childEventListener: ChildEventListener

    override fun onCreate() {
        super.onCreate()

        databaseReference = FirebaseDatabase.getInstance().reference.child("tienda").child("reservas_carta")

        childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val pedido = snapshot.getValue(Pedido::class.java)
                pedido?.let {
                    // Enviar notificación al administrador
                    enviarNotificacionAdmins(pedido)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }

        // Escuchar eventos de agregado de hijos en la base de datos de pedidos
        databaseReference.addChildEventListener(childEventListener)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        // Detener la escucha de eventos al destruir el servicio
        databaseReference.removeEventListener(childEventListener)
    }

    private fun enviarNotificacionAdmins(pedido: Pedido) {
        // Aquí envía la notificación a los administradores utilizando FCM
        // Puedes personalizar el contenido de la notificación con los detalles del pedido
        obtenerAdmins { admins ->
            admins.forEach { admin ->
                // Envía la notificación a cada administrador
                enviarNotificacion(admin, "Nuevo pedido realizado", "Un cliente ha realizado un nuevo pedido.")
            }
        }
    }

    private fun enviarNotificacion(adminId: String, title: String, body: String) {
        // Aquí envía la notificación a través de FCM utilizando el ID del administrador
        // Puedes personalizar esto de acuerdo a tu implementación específica de FCM
    }

    private fun obtenerAdmins(onAdminsObtained: (List<String>) -> Unit) {
        val adminsList: MutableList<String> = mutableListOf()
        val usuariosRef = FirebaseDatabase.getInstance().reference.child("usuarios")
        val query = usuariosRef.orderByChild("tipo").equalTo("admin")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (usuarioSnapshot in snapshot.children) {
                    val adminId = usuarioSnapshot.key
                    adminId?.let {
                        adminsList.add(it)
                    }
                }
                onAdminsObtained(adminsList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error
            }
        })
    }
}
