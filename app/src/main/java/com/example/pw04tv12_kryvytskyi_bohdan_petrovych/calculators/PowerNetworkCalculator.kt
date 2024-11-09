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
import kotlin.math.pow

@Composable
fun PowerNetworkCalculator() {
    var rsn by remember { mutableStateOf("10.65") }
    var xsn by remember { mutableStateOf("24.02") }
    var rsnMin by remember { mutableStateOf("34.88") }
    var xsnMin by remember { mutableStateOf("65.68") }

    var results by remember { mutableStateOf<NetworkResults?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = rsn,
            onValueChange = { rsn = it },
            label = { Text("Rsn (Ω)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = xsn,
            onValueChange = { xsn = it },
            label = { Text("Xsn (Ω)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = rsnMin,
            onValueChange = { rsnMin = it },
            label = { Text("Rsn min (Ω)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = xsnMin,
            onValueChange = { xsnMin = it },
            label = { Text("Xsn min (Ω)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                results = calculateNetwork(
                    rsn.toDoubleOrNull() ?: 0.0,
                    xsn.toDoubleOrNull() ?: 0.0,
                    rsnMin.toDoubleOrNull() ?: 0.0,
                    xsnMin.toDoubleOrNull() ?: 0.0
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        results?.let { res ->
            Text("110kV bus SC currents (normal/minimum):")
            Text("Three-phase: ${res.iSh3}/${res.iSh3Min} A")
            Text("Two-phase: ${res.iSh2}/${res.iSh2Min} A")

            Text("\n10kV bus SC currents (normal/minimum):")
            Text("Three-phase: ${res.iShN3}/${res.iShN3Min} A")
            Text("Two-phase: ${res.iShN2}/${res.iShN2Min} A")

            Text("\nPoint 10 SC currents (normal/minimum):")
            Text("Three-phase: ${res.iLN3}/${res.iLN3Min} A")
            Text("Two-phase: ${res.iLN2}/${res.iLN2Min} A")
        }
    }
}



fun calculateNetwork(rsn: Double, xsn: Double, rsnMin: Double, xsnMin: Double): NetworkResults {
    // Calculate transformer reactance
    val xt = (11.1 * 115.0.pow(2)) / (100 * 6.3)

    // Normal mode calculations
    val rsh = rsn
    val xsh = xsn + xt
    val zsh = kotlin.math.sqrt(rsh.pow(2) + xsh.pow(2))

    // Minimum mode calculations
    val rshMin = rsnMin
    val xshMin = xsnMin + xt
    val zshMin = kotlin.math.sqrt(rshMin.pow(2) + xshMin.pow(2))

    // Calculate currents at 110kV
    val ish3 = (115.0 * 1000) / (kotlin.math.sqrt(3.0) * zsh)
    val ish2 = ish3 * (kotlin.math.sqrt(3.0) / 2)
    val ish3Min = (115.0 * 1000) / (kotlin.math.sqrt(3.0) * zshMin)
    val ish2Min = ish3Min * (kotlin.math.sqrt(3.0) / 2)

    // Calculate conversion coefficient and transformed resistances
    val kpr = (11.0.pow(2)) / (115.0.pow(2))
    val rshN = rsh * kpr
    val xshN = xsh * kpr
    val zshN = kotlin.math.sqrt(rshN.pow(2) + xshN.pow(2))
    val rshNMin = rshMin * kpr
    val xshNMin = xshMin * kpr
    val zshNMin = kotlin.math.sqrt(rshNMin.pow(2) + xshNMin.pow(2))

    // Calculate currents at 10kV bus
    val ishN3 = (11.0 * 1000) / (kotlin.math.sqrt(3.0) * zshN)
    val ishN2 = ishN3 * (kotlin.math.sqrt(3.0) / 2)
    val ishN3Min = (11.0 * 1000) / (kotlin.math.sqrt(3.0) * zshNMin)
    val ishN2Min = ishN3Min * (kotlin.math.sqrt(3.0) / 2)

    // Calculate line parameters
    val iL = 0.2 + 0.35 + 0.2 + 0.6 + 2.0 + 2.55 + 3.37 + 3.1
    val rL = iL * 0.64
    val xL = iL * 0.363

    // Calculate total impedances for point 10
    val rsumN = rL + rshN
    val xsumN = xL + xshN
    val zsumN = kotlin.math.sqrt(rsumN.pow(2) + xsumN.pow(2))
    val rsumNMin = rL + rshNMin
    val xsumNMin = xL + xshNMin
    val zsumNMin = kotlin.math.sqrt(rsumNMin.pow(2) + xsumNMin.pow(2))

    // Calculate currents at point 10
    val ilN3 = (11.0 * 1000) / (kotlin.math.sqrt(3.0) * zsumN)
    val ilN2 = ilN3 * (kotlin.math.sqrt(3.0) / 2)
    val ilN3Min = (11.0 * 1000) / (kotlin.math.sqrt(3.0) * zsumNMin)
    val ilN2Min = ilN3Min * (kotlin.math.sqrt(3.0) / 2)

    return NetworkResults(
        iSh3 = String.format("%.1f", ish3),
        iSh2 = String.format("%.0f", ish2),
        iSh3Min = String.format("%.0f", ish3Min),
        iSh2Min = String.format("%.0f", ish2Min),
        iShN3 = String.format("%.0f", ishN3),
        iShN2 = String.format("%.0f", ishN2),
        iShN3Min = String.format("%.0f", ishN3Min),
        iShN2Min = String.format("%.0f", ishN2Min),
        iLN3 = String.format("%.0f", ilN3),
        iLN2 = String.format("%.0f", ilN2),
        iLN3Min = String.format("%.0f", ilN3Min),
        iLN2Min = String.format("%.0f", ilN2Min)
    )
}

data class NetworkResults(
    val iSh3: String,
    val iSh2: String,
    val iSh3Min: String,
    val iSh2Min: String,
    val iShN3: String,
    val iShN2: String,
    val iShN3Min: String,
    val iShN2Min: String,
    val iLN3: String,
    val iLN2: String,
    val iLN3Min: String,
    val iLN2Min: String
)