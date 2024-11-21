package com.example.prueba02_raquelmelo.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.prueba02_raquelmelo.domain.model.Post
import com.example.prueba02_raquelmelo.posts.viewModel.PostsViewModel
import com.example.prueba02_raquelmelo.posts.viewModel.UiState


@Composable
fun PostListScreen() {
    val postsViewModel = PostsViewModel()
    val uiState by postsViewModel.uiState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        postsViewModel.getPosts()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) { innerPadding ->

        Box(Modifier.padding(innerPadding)) {
            Column(Modifier.fillMaxSize()) {
                uiState?.let { state ->
                    when (state) {
                        UiState.Loading -> {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        is UiState.Success -> {
                            PostList(posts = state.posts)
                        }
                    }
                }
            }
            Button(
                onClick = { postsViewModel.getPosts() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text("Actualizar Posts")
            }
        }
    }
}

@Composable
fun PostList(posts: List<Post>) {
    LazyColumn {
        items(posts) { post ->
            PostItem(post = post)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black,
        ),
        border = BorderStroke(2.dp, color = Color.Black),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "User ID: ${post.userId}", style = MaterialTheme.typography.labelSmall)
            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = post.body, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
