package com.example.pw04tv12_kryvytskyi_bohdan_petrovych

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {PowerSystemCalculatorTheme {
            PowerSystemCalculatorApp()
        }
        }
    }
}


//

@Composable
fun PowerSystemCalculatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = dynamicDarkColorScheme(LocalContext.current),
        content = content
    )
}

