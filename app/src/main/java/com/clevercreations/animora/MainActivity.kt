package com.clevercreations.animora
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF090A10)
private val Panel = Color(0xFF13151F)
private val Soft = Color(0xFF1C2030)
private val Pink = Color(0xFFFF4F9A)
private val Cyan = Color(0xFF53D9FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { AnimoraApp() } }
}
@Composable fun AnimoraApp() {
    var prompt by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("60 sec") }
    var style by remember { mutableStateOf("Toon 3D") }
    var status by remember { mutableStateOf("Ready to direct") }
    var scenes by remember { mutableStateOf(listOf<String>()) }
    MaterialTheme(colorScheme = darkColorScheme(background = Ink, surface = Panel, primary = Pink)) {
        Column(Modifier.fillMaxSize().background(Ink).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Text("ANIMORA", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                    Text("AI animation studio", color = Color(0xFF9DA2B5), fontSize = 13.sp)
                }
                Surface(shape = RoundedCornerShape(50), color = Soft) { Text("DIRECTOR AI", color = Cyan, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.height(18.dp))
            Text("What are we making?", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = prompt, onValueChange = { prompt = it }, modifier = Modifier.fillMaxWidth().height(150.dp), placeholder = { Text("A little fox finds a lost star and brings it home…", color = Color(0xFF6F7488)) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Pink, unfocusedBorderColor = Color(0xFF303545), focusedTextColor = Color.White, unfocusedTextColor = Color.White))
            Spacer(Modifier.height(18.dp))
            Text("Length", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("30 sec","60 sec","90 sec","2 min","2.5 min","3 min").forEach { d -> Choice(d, duration == d) { duration = d } } }
            Spacer(Modifier.height(16.dp))
            Text("Visual world", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Toon 3D","Storybook 2D","Soft clay","Anime toon").forEach { s -> Choice(s, style == s) { style = s } } }
            Spacer(Modifier.height(20.dp))
            Surface(color = Panel, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Continuity engine", color = Cyan, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Characters, world rules, emotions, camera direction, dialogue and music stay attached to the project instead of being regenerated from scratch for every shot.", color = Color(0xFFB8BDCC), fontSize = 13.sp, lineHeight = 19.sp)
                }
            }
            Spacer(Modifier.height(14.dp))
            Button(onClick = {
                if (prompt.isBlank()) { status = "Give the director a story first"; return@Button }
                scenes = Director.plan(prompt, duration)
                status = "Director planned " + scenes.size + " scenes • " + style
            }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(18.dp), colors = ButtonDefaults.buttonColors(containerColor = Pink)) {
                Text("DIRECT MY ANIMATION", color = Color.White, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.height(14.dp))
            Text(status, color = Color(0xFF9DA2B5), fontSize = 12.sp)

            if (scenes.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    color = Panel,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Animation render", color = Cyan, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Director plan created. Video rendering is not connected in this prototype yet.",
                            color = Color(0xFFB8BDCC),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(Modifier.height(12.dp))
                        scenes.take(5).forEachIndexed { i, scene ->
                            Text(
                                (i + 1).toString() + ". " + scene,
                                color = Color.White,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable fun Choice(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(onClick = onClick, color = if (selected) Pink else Soft, shape = RoundedCornerShape(50.dp)) { Text(text, color = Color.White, modifier = Modifier.padding(horizontal = 13.dp, vertical = 9.dp), fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) }
}
object Director {
    fun plan(prompt: String, duration: String): List<String> {
        val n = when(duration) { "30 sec" -> 5; "60 sec" -> 8; "90 sec" -> 11; "2 min" -> 14; "2.5 min" -> 17; else -> 20 }
        val beats = listOf("Establish world and character", "Character acts on goal", "Reaction and emotional beat", "Directed camera action", "Dialogue + expressive performance", "Story complication", "Character solves the problem", "Warm payoff")
        return (0 until n).map { i -> beats[i % beats.size] }
    }
}
