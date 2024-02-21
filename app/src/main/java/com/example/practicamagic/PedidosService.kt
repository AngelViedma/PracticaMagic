package com.example.practicamagic

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.practicamagic.entities.Pedido
import org.json.JSONException
import org.json.JSONObject

class PedidoService : Service() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var childEventListener: ChildEventListener

    override fun onCreate() {
        super.onCreate()

        databaseReference = FirebaseDatabase.getInstance().reference.child("tienda").child("reservas_carta")

        childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val pedido = snapshot.getValue(Pedido::class.java)
                pedido?.let {
                    // Verificar si el pedido fue realizado por un cliente
                    if (pedido.usuarioId != null) {
                        // Enviar notificación al cliente cuando se obtiene el token de FCM
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val token = task.result
                                token?.let {
                                    val notificationTitle = "¡Tu pedido ha sido aprobado!"
                                    val notificationBody = "Tu pedido con ID ${pedido.id} ha sido preparado y está listo para su recolección."
                                    enviarNotificacionFCM(token, notificationTitle, notificationBody)
                                }
                            } else {
                                // Manejar el error al obtener el token
                                Log.e("PedidoService", "Error al obtener el token FCM: ${task.exception}")
                            }
                        }
                    }
                    // Verificar si el pedido está en estado "Preparado"
                    if (pedido.estado == "Preparado") {
                        // Enviar notificación a los administradores
                        enviarNotificacionAdmins(pedido)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val pedido = snapshot.getValue(Pedido::class.java)
                pedido?.let {
                    // Verificar si el estado del pedido cambió a "Preparado"
                    if (pedido.estado == "Preparado") {
                        // Enviar notificación al cliente cuando se obtiene el token de FCM
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val token = task.result
                                token?.let {
                                    val notificationTitle = "¡Tu pedido ha sido aprobado!"
                                    val notificationBody = "Tu pedido con ID ${pedido.id} ha sido preparado y está listo para su recolección."
                                    enviarNotificacionFCM(token, notificationTitle, notificationBody)
                                }
                            } else {
                                // Manejar el error al obtener el token
                                Log.e("PedidoService", "Error al obtener el token FCM: ${task.exception}")
                            }
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }

        // Escuchar eventos de cambios y agregado de hijos en la base de datos de pedidos
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
                enviarNotificacion(admin, "Nuevo pedido preparado", "Se ha preparado un nuevo pedido.")
            }
        }
    }

    private fun enviarNotificacionFCM(token: String, title: String, body: String) {
        val notification = JSONObject()
        val notificationBody = JSONObject()

        try {
            notificationBody.put("title", title)
            notificationBody.put("body", body)

            notification.put("to", token)
            notification.put("data", notificationBody)

            val request = object : JsonObjectRequest(
                Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                notification,
                Response.Listener {
                    // Notificación enviada exitosamente
                    Log.d("PedidoService", "Notificación enviada exitosamente")
                },
                Response.ErrorListener {
                    // Error al enviar la notificación
                    Log.e("PedidoService", "Error al enviar la notificación: $it")
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["Authorization"] = "key=BkwkR1MdJL379kMFFVyEyvRjPvBOZ8dem0tODKzj" // Reemplaza con tu clave del servidor FCM
                    return headers
                }
            }

            // Agregar la solicitud a la cola de solicitudes
            Volley.newRequestQueue(this).add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun enviarNotificacion(adminId: String, title: String, body: String) {
        // Aquí envía la notificación a través de FCM utilizando el ID del administrador
        // Puedes implementar una lógica similar a la función enviarNotificacionFCM()
        // para enviar notificaciones a los administradores.
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