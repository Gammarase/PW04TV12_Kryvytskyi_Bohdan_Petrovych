package com.example.pw04tv12_kryvytskyi_bohdan_petrovych

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pw04tv12_kryvytskyi_bohdan_petrovych.calculators.CableCalculator
import com.example.pw04tv12_kryvytskyi_bohdan_petrovych.calculators.PowerNetworkCalculator
import com.example.pw04tv12_kryvytskyi_bohdan_petrovych.calculators.ShortCircuitCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PowerSystemCalculatorApp() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Power System Calculator") }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Calculate, "Cable Selection") },
                    label = { Text("Cable") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Calculate, "Short Circuit") },
                    label = { Text("SC Current") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Calculate, "Power Network") },
                    label = { Text("Network") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (selectedTab) {
                0 -> CableCalculator()
                1 -> ShortCircuitCalculator()
                2 -> PowerNetworkCalculator()
            }
        }
    }
}