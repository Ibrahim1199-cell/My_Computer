package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {

    private var isAutoCanceled = false
    private val handler = Handler(Looper.getMainLooper())
    private var autoCloseRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Load the layout in all cases so the user can see it

        val editIp = findViewById<EditText>(R.id.editIp)
        val editMac = findViewById<EditText>(R.id.editMac)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Retrieve data from storage
        val sharedPref = getSharedPreferences("WolPrefs", MODE_PRIVATE)
        val savedIp = sharedPref.getString("savedIp", "") ?: ""
        val savedMac = sharedPref.getString("savedMac", "") ?: ""

        // Fill input fields with previously saved data
        editIp.setText(savedIp)
        editMac.setText(savedMac)

        // AUTOMATIC MODE: If info is already saved, start the countdown
        if (savedIp.isNotEmpty() && savedMac.isNotEmpty()) {
            btnSave.text = "CANCEL AUTO-CLOSE (3s)"

            // Timer to run after 3 seconds (Countdown task)
            autoCloseRunnable = Runnable {
                if (!isAutoCanceled) {
                    sendMagicPacket(savedIp, savedMac)
                    Toast.makeText(this@MainActivity, "🚀 Signal sent, closing...", Toast.LENGTH_SHORT).show()
                    finish() // Closes the application
                }
            }
            // Start the timer (3000 milliseconds = 3 seconds)
            autoCloseRunnable?.let { handler.postDelayed(it, 3000) }
        }

        // BUTTON CLICK LOGIC
        btnSave.setOnClickListener {
            if (btnSave.text.toString().contains("CANCEL")) {
                // If the user presses the button within 3 seconds, stop automatic mode
                isAutoCanceled = true
                autoCloseRunnable?.let { handler.removeCallbacks(it) } // Cancel the timer
                btnSave.text = "SAVE NEW INFO"
                Toast.makeText(this, "Automation stopped. You can edit now.", Toast.LENGTH_SHORT).show()
            } else {
                // Normal Save or Update operation
                val ip = editIp.text.toString().trim()
                val mac = editMac.text.toString().trim()

                if (ip.isNotEmpty() && mac.isNotEmpty()) {
                    // Securely save information to storage
                    sharedPref.edit().putString("savedIp", ip).putString("savedMac", mac).apply()

                    // Send the first signal and close
                    sendMagicPacket(ip, mac)
                    Toast.makeText(this, "✅ Successfully saved and first signal sent!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Please fill in the IP and MAC fields!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Magic Packet function that wakes up the computer
    private fun sendMagicPacket(ip: String, mac: String) {
        thread {
            try {
                val macBytes = mac.split(":").map { it.toInt(16).toByte() }.toByteArray()
                val bytes = ByteArray(6 + 16 * macBytes.size)
                for (i in 0 until 6) bytes[i] = 0xff.toByte()
                for (i in 6 until bytes.size step macBytes.size) {
                    System.arraycopy(macBytes, 0, bytes, i, macBytes.size)
                }
                val address = InetAddress.getByName(ip)
                val packet = DatagramPacket(bytes, bytes.size, address, 9)
                val socket = DatagramSocket()
                socket.send(packet)
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}