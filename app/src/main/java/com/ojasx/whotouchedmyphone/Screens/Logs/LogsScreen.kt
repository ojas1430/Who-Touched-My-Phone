package com.ojasx.whotouchedmyphone.Screens.Logs

import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.ojasx.whotouchedmyphone.RoomDb.PIN.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen() {

    val context = LocalContext.current

    val dao = remember {
        AppDatabase.getDatabase(context).intruderLogDao()
    }

    val logs by dao.getAllLogs()
        .collectAsState(initial = emptyList())

    var selectedImage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = "Intrusion Logs",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0B0E17)
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF0B0E17),
                            Color(0xFF111528),
                            Color(0xFF0B0E17)
                        )
                    )
                )
                .padding(padding)
                .padding(bottom = 85.dp)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp)
            ) {

                items(logs) { log ->

                    val appName = log.appName

                    val date = java.text.SimpleDateFormat(
                        "dd MMM, hh:mm a",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date(log.timestamp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedImage = log.imageUri
                            },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1A1F2E)
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            AsyncImage(
                                model = log.imageUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {

                                Text(
                                    text = appName,
                                    color = Color(0xFFB388FF),
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = date,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Intruder tried to open $appName",
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }

            // FULL IMAGE VIEW
            if (selectedImage != null) {
                Dialog(onDismissRequest = { selectedImage = null }) {
                    Card(shape = RoundedCornerShape(24.dp)) {
                        AsyncImage(
                            model = selectedImage,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
