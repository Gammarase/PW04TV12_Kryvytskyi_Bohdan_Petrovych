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
fun ShortCircuitCalculator() {
    var sk by remember { mutableStateOf("200") }
    var results by remember { mutableStateOf<ShortCircuitResults?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = sk,
            onValueChange = { sk = it },
            label = { Text("Sk (MVA)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                results = calculateShortCircuit(sk.toDoubleOrNull() ?: 0.0)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        results?.let { res ->
            Text("Xc: ${res.xc} Ω")
            Text("Xt: ${res.xt} Ω")
            Text("Total resistance: ${res.totalResistance} Ω")
            Text("Initial three-phase SC current: ${res.initialCurrent} kA")
        }
    }
}

fun calculateShortCircuit(sk: Double): ShortCircuitResults {
    val xc = 10.5.pow(2) / sk
    val xt = (10.5 / 100) * (10.5.pow(2) / 6.3)
    val xsum = xc + xt
    val ip0 = 10.5 / (kotlin.math.sqrt(3.0) * xsum)

    return ShortCircuitResults(
        xc = String.format("%.2f", xc),
        xt = String.format("%.2f", xt),
        totalResistance = String.format("%.2f", xsum),
        initialCurrent = String.format("%.1f", ip0)
    )
}

data class ShortCircuitResults(
    val xc: String,
    val xt: String,
    val totalResistance: String,
    val initialCurrent: String
)