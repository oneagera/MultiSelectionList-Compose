package com.oneagera.multiselectlazycolumn

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.unit.dp
import com.oneagera.multiselectlazycolumn.ui.theme.MultiSelectLazyColumnTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiSelectLazyColumnTheme {
                Surface(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    var isVisible by remember {
                        mutableStateOf(false)
                    }
                    val scope = rememberCoroutineScope()
                    //var color by remember { mutableStateOf(Green) }
                    var items by remember {
                        mutableStateOf(
                            (1..20).map {
                                ListItem(
                                    title = "item $it",
                                    isSelected = false
                                )
                            }
                        )
                    }

                    // Helper function to check if any item is selected
                    fun isAnyItemSelected(): Boolean {
                        return items.any { it.isSelected }
                    }

                    // Helper function to update the selection state of an item
                    fun updateItemSelection(j: Int) {
                        items = items.mapIndexed { i, item ->
                            if (i == j) {
                                item.copy(isSelected = !item.isSelected)
                            } else item
                        }
                    }

                    // Helper function to count the selected items
                    fun getSelectedItemCount(): Int {
                        return items.count { it.isSelected }
                    }

                    // Helper function to deselect all items
                    fun deselectAllItems() {
                        items = items.map { it.copy(isSelected = false) }
                    }

                    //deselect all items when the user presses the back button if any items are selected.
                    BackHandler(
                        enabled = getSelectedItemCount() > 0 // Enable only if there are selected items
                    ) {
                        deselectAllItems() // Deselect all items on back press
                        scope.launch {
                            isVisible = isAnyItemSelected()
                        }
                    }

                    Scaffold(
                        topBar = {
                            if (isVisible) {
                                AnimatedVisibility(visible = true) {
                                    TopAppBar(
                                        title = {
                                            Text(text = "${getSelectedItemCount()} Selected")
                                        },
                                        navigationIcon = {
                                            IconButton(
                                                onClick = {
                                                    deselectAllItems()
                                                    scope.launch {
                                                        isVisible = isAnyItemSelected()
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = ""
                                                )
                                            }
                                        },
                                        actions = {
                                            IconButton(
                                                onClick = { }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = null
                                                )
                                            }
                                            IconButton(
                                                onClick = { }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Info,
                                                    contentDescription = null
                                                )
                                            }
                                            IconButton(onClick = { }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.MoreVert,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    )
                                }
                            } else {
                                AnimatedVisibility(visible = true) {
                                    TopAppBar(title = { Text(text = "Multi Selection List") })
                                }

                            }
                        }
                    ) { contentPadding ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(contentPadding)
                        ) {
                            items(items.size) { i ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .then(
                                            if (items[i].isSelected) {
                                                Modifier.border(
                                                    2.dp,
                                                    MaterialTheme.colorScheme.surface,
                                                    RoundedCornerShape(10.dp)
                                                )
                                            } else Modifier
                                        )
                                        .background(
                                            if (items[i].isSelected) {
                                                MaterialTheme.colorScheme.primaryContainer
                                            } else Transparent
                                        )
                                        .combinedClickable(
                                            onLongClick = {
                                                updateItemSelection(i) // Toggle selection on long click
                                                scope.launch {
                                                    isVisible = isAnyItemSelected()
                                                }

                                            },
                                            onClick = {
                                                if (isAnyItemSelected()) {
                                                    updateItemSelection(i) // Toggle selection on long click
                                                    scope.launch {
                                                        isVisible = isAnyItemSelected()
                                                    }
                                                }
                                            }
                                        )
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = items[i].title,
                                        color = if (items[i].isSelected) {
                                            Black
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                    if (items[i].isSelected) {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = "Selected",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/*
items = items.mapIndexed { j, item ->
    if (i == j) {
        item.copy(isSelected = !item.isSelected)
    } else item
} //Map the list to a new list and so we can use the 'i' index
}
*/

