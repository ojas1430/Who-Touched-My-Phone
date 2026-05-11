package com.ojasx.whotouchedmyphone.Screens.Logs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.launch
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

    val scope = rememberCoroutineScope()

    var selectedImage by remember {
        mutableStateOf<String?>(null)
    }

    var showDeleteAllDialog by remember {
        mutableStateOf(false)
    }

    var selectedLogId by remember {
        mutableIntStateOf(-1)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

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

                actions = {

                    IconButton(

                        onClick = {

                            showDeleteAllDialog = true
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
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
                .padding(bottom = 80.dp)
        ) {

            if (logs.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "No Intrusion Logs",
                        color = Color.White
                    )
                }
            }

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),

                verticalArrangement = Arrangement.spacedBy(14.dp),

                contentPadding = PaddingValues(
                    top = 20.dp,
                    bottom = 100.dp
                )
            ) {

                items(logs) { log ->

                    val date = SimpleDateFormat(
                        "dd MMM, hh:mm a",
                        Locale.getDefault()
                    ).format(Date(log.timestamp))

                    Card(

                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(

                                onClick = {

                                },

                                onLongClick = {

                                    selectedLogId = log.id

                                    showDeleteDialog = true
                                }
                            ),

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
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {

                                        selectedImage = log.imageUri
                                    },

                                contentScale = ContentScale.Crop
                            )

                            Spacer(
                                modifier = Modifier.width(16.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = log.appName,
                                    color = Color(0xFFB388FF),
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(
                                    modifier = Modifier.height(6.dp)
                                )

                                Text(
                                    text = date,
                                    color = Color.White
                                )

                                Spacer(
                                    modifier = Modifier.height(6.dp)
                                )

                                Text(
                                    text = "Intruder tried to open ${log.appName}",
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }

            // Full Image Dialog
            if (selectedImage != null) {

                Dialog(

                    onDismissRequest = {

                        selectedImage = null
                    }
                ) {

                    Card(
                        shape = RoundedCornerShape(24.dp)
                    ) {

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

            // Delete All Dialog
            if (showDeleteAllDialog) {

                AlertDialog(

                    onDismissRequest = {

                        showDeleteAllDialog = false
                    },

                    confirmButton = {

                        TextButton(

                            onClick = {

                                scope.launch {

                                    dao.deleteAllLogs()
                                }

                                showDeleteAllDialog = false
                            }
                        ) {

                            Text(
                                text = "Delete",
                                color = Color.Red
                            )
                        }
                    },

                    dismissButton = {

                        TextButton(

                            onClick = {

                                showDeleteAllDialog = false
                            }
                        ) {

                            Text(
                                text = "Cancel"
                            )
                        }
                    },

                    title = {

                        Text(
                            text = "Delete All Logs"
                        )
                    },

                    text = {

                        Text(
                            text = "Are you sure you want to delete all intrusion logs?"
                        )
                    }
                )
            }

            // Single Delete Dialog
            if (showDeleteDialog) {

                AlertDialog(

                    onDismissRequest = {

                        showDeleteDialog = false
                    },

                    confirmButton = {

                        TextButton(

                            onClick = {

                                scope.launch {

                                    dao.deleteLogById(selectedLogId)
                                }

                                showDeleteDialog = false
                            }
                        ) {

                            Text(
                                text = "Delete",
                                color = Color.Red
                            )
                        }
                    },

                    dismissButton = {

                        TextButton(

                            onClick = {

                                showDeleteDialog = false
                            }
                        ) {

                            Text("Cancel")
                        }
                    },

                    title = {

                        Text("Delete Log")
                    },

                    text = {

                        Text(
                            "Do you want to delete this intrusion log?"
                        )
                    }
                )
            }
        }
    }
}