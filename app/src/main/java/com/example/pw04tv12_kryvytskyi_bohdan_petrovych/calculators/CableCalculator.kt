package com.example.pw04tv12_kryvytskyi_bohdan_petrovych.calculators

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CableCalculator() {
    var sm by remember { mutableStateOf("1300") }
    var ik by remember { mutableStateOf("2500") }
    var tf by remember { mutableStateOf("2.5") }

    var results by remember { mutableStateOf<CableResults?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = sm,
            onValueChange = { sm = it },
            label = { Text("Sm (MVA)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ik,
            onValueChange = { ik = it },
            label = { Text("Ik (A)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = tf,
            onValueChange = { tf = it },
            label = { Text("tf (s)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                results = calculateCableParameters(
                    sm.toDoubleOrNull() ?: 0.0,
                    ik.toDoubleOrNull() ?: 0.0,
                    tf.toDoubleOrNull() ?: 0.0
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        results?.let { res ->
            Text("Normal mode current: ${res.normalCurrent} A")
            Text("Post-emergency current: ${res.postEmergencyCurrent} A")
            Text("Economic cross-section: ${res.economicCrossSection} mm²")
            Text("Minimum cross-section: ${res.minimumCrossSection} mm²")
        }
    }
}

// Calculator functions
fun calculateCableParameters(sm: Double, ik: Double, tf: Double): CableResults {
    val im = (sm / 2) / (kotlin.math.sqrt(3.0) * 10)
    val imPa = 2 * im
    val sEk = im / 1.4
    val sVsS = (ik * kotlin.math.sqrt(tf)) / 92

    return CableResults(
        normalCurrent = String.format("%.1f", im),
        postEmergencyCurrent = String.format("%.0f", imPa),
        economicCrossSection = String.format("%.1f", sEk),
        minimumCrossSection = String.format("%.0f", sVsS)
    )
}

// Data classes for results
data class CableResults(
    val normalCurrent: String,
    val postEmergencyCurrent: String,
    val economicCrossSection: String,
    val minimumCrossSection: String
)