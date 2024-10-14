package it.fast4x.rimusic.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import database.MusicDatabaseDao
import database.MusicDatabaseDesktop
import database.entities.Song
import it.fast4x.innertube.Innertube
import it.fast4x.innertube.models.PlayerResponse
import it.fast4x.innertube.models.bodies.PlayerBody
import it.fast4x.innertube.requests.player
import it.fast4x.rimusic.enums.PageType
import it.fast4x.rimusic.models.SongEntity
import it.fast4x.rimusic.styling.Dimensions.layoutColumnBottomPadding
import it.fast4x.rimusic.styling.Dimensions.layoutColumnBottomSpacer
import it.fast4x.rimusic.styling.Dimensions.layoutColumnTopPadding
import it.fast4x.rimusic.styling.Dimensions.layoutColumnsHorizontalPadding
import it.fast4x.rimusic.ui.components.PlayerEssential
import it.fast4x.rimusic.ui.screens.AlbumScreen
import it.fast4x.rimusic.ui.screens.ArtistScreen
import it.fast4x.rimusic.ui.screens.MoodScreen
import it.fast4x.rimusic.ui.screens.PlaylistScreen
import it.fast4x.rimusic.ui.screens.QuickPicsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import player.frame.FrameContainer
import rimusic.composeapp.generated.resources.Res
import rimusic.composeapp.generated.resources.album
import rimusic.composeapp.generated.resources.app_icon
import rimusic.composeapp.generated.resources.app_logo_text
import rimusic.composeapp.generated.resources.artists
import rimusic.composeapp.generated.resources.library
import rimusic.composeapp.generated.resources.musical_notes
import vlcj.VlcjFrameController


@Composable
fun ThreeColumnsApp() {

    val db = remember { MusicDatabaseDesktop }

    val coroutineScope by remember { mutableStateOf(CoroutineScope(Dispatchers.IO)) }

    var videoId by remember { mutableStateOf("") }
    var nowPlayingSong by remember { mutableStateOf<Song?>(null) }
    var artistId by remember { mutableStateOf("") }
    var albumId by remember { mutableStateOf("") }
    var playlistId by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf<Innertube.Mood.Item?>(null) }

    val formatAudio =
        remember { mutableStateOf<PlayerResponse.StreamingData.AdaptiveFormat?>(null) }

    LaunchedEffect(videoId) {
        if (videoId.isEmpty()) return@LaunchedEffect

        Innertube.player(PlayerBody(videoId = videoId))
            ?.onSuccess {
                formatAudio.value =
                    it.streamingData?.adaptiveFormats?.filter { type -> type.isAudio }
                        ?.maxByOrNull {
                            it.bitrate?.times(
                                (if (it.mimeType.startsWith("audio/webm")) 100 else 1)
                            ) ?: -1
                        }
            }
        println("videoId  ${videoId} formatAudio url inside ${formatAudio.value?.url}")

        nowPlayingSong = db.getSong(videoId)
        println("nowPlayingSong ${nowPlayingSong}")
    }

    coroutineScope.launch {
        db.getAll().collect {
            println("songs in db ${it.size}")
        }
    }

    /*
    val formatAudio = body.value?.streamingData?.adaptiveFormats
        ?.filter { it.isAudio }
        ?.maxByOrNull {
            it.bitrate?.times( (if (it.mimeType.startsWith("audio/webm")) 100 else 1)
            ) ?: -1 }

     */


    /*
    val formatVideo = body.value?.streamingData?.adaptiveFormats
        ?.filter { it.isVideo }
        ?.maxByOrNull {
            it.bitrate?.times( (if (it.mimeType.startsWith("video/mp4")) 100 else 1)
            ) ?: -1 }
    */


    //val urlAudio by remember { mutableStateOf(formatAudio.value?.url ?: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4") }
    //var urlVideo by remember { mutableStateOf(formatVideo?.url ?: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4") }
    //var url by remember { mutableStateOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4") }
    //val url by remember { mutableStateOf(formatAudio.value?.url) }

    val url =
        formatAudio.value?.url //"https://rr4---sn-hpa7znzr.googlevideo.com/videoplayback?expire=1727471735&ei=Fsz2ZoyiPO7Si9oPpreTiAI&ip=178.19.172.167&id=o-ABmCff7qCeQd05V_WN5fpAFEfxHP3kxR6G55H_QdlBsh&itag=251&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&mh=43&mm=31%2C26&mn=sn-hpa7znzr%2Csn-4g5lznez&ms=au%2Conr&mv=m&mvi=4&pl=22&gcr=it&initcwndbps=2505000&vprv=1&svpuc=1&mime=audio%2Fwebm&rqh=1&gir=yes&clen=3291443&dur=194.901&lmt=1714829870710563&mt=1727449746&fvip=4&keepalive=yes&fexp=51299152&c=ANDROID_MUSIC&txp=2318224&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cgcr%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRQIhAP5IS0unA9IAhtAtkqY-63FGyG_eRi-FMMgNjWU1TWGzAiACd3c4niMxsPxXjp_55EylpIBysVBOpoD69oQ9xvF8bg%3D%3D&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=ABPmVW0wRAIgZBP07jXYZ5_4xSrp_hZ9jvIOMPsfOa-grREDshQvzSYCIC7ImmFVJCeLUMVASEkedlXa-R4je3RVC_fu2WH8XTvj"

    //var url by remember { mutableStateOf(urlVideo) }

    //val componentController = remember(url) { VlcjComponentController() }
    val frameController = remember(url) { VlcjFrameController() }

    var showPageSheet by remember { mutableStateOf(false) }
    var showPageType by remember { mutableStateOf(PageType.QUICKPICS) }

    //println("songs in db ${MusicDatabaseDesktop.getAllSongs()}")

    //val backStackEntry by navController.currentBackStackEntryAsState()
    //val currentScreen = backStackEntry?.destination?.route ?: "artists"

    Scaffold(
        containerColor = Color.Black,
        contentColor = Color.Gray,
        topBar = {
            /*
            DesktopTopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
             */
        },
        bottomBar = {
            PlayerEssential(
                frameController = frameController,
                url = url,
                song = nowPlayingSong,
                onExpandAction = { showPageSheet = true }
            )
        }
    ) { innerPadding ->

        ThreeColumnsLayout(
            /*
            onSongClick = {
                videoId.value = it
            },
            onArtistClick = {
                artistId.value = it
                showPageType = PageType.ARTIST
                showPageSheet = true
            },
            onAlbumClick = {
                albumId.value = it
                showPageType = PageType.ALBUM
                showPageSheet = true
            },
            onPlaylistClick = {},
             */
            onHomeClick = { showPageType = PageType.QUICKPICS },
            frameController = frameController,
            centerPanelContent = {
                when (showPageType) {
                    PageType.ALBUM -> {
                        AlbumScreen(
                            browseId = albumId,
                            onSongClick = {
                                videoId = it.id
                                coroutineScope.launch {
                                    db.insert(it)
                                }
                            },
                            onAlbumClick = {
                                albumId = it
                                showPageType = PageType.ALBUM
                                showPageSheet = true
                            },
                            onClosePage = { showPageSheet = false }
                        )
                    }

                    PageType.ARTIST -> {
                        ArtistScreen(
                            browseId = artistId,
                            onSongClick = {
                                videoId = it.id
                                coroutineScope.launch {
                                    db.insert(it)
                                }
                            },
                            onPlaylistClick = {
                                playlistId = it
                                showPageType = PageType.PLAYLIST
                                showPageSheet = true
                            },
                            onViewAllAlbumsClick = {},
                            onViewAllSinglesClick = {},
                            onAlbumClick = {
                                albumId = it
                                showPageType = PageType.ALBUM
                                showPageSheet = true
                            },
                            onClosePage = { showPageSheet = false }
                        )
                    }

                    PageType.PLAYLIST -> {
                        PlaylistScreen(
                            browseId = playlistId,
                            onSongClick = {
                                videoId = it.id
                                coroutineScope.launch {
                                    db.insert(it)
                                }
                            },
                            onAlbumClick = {
                                albumId = it
                                showPageType = PageType.ALBUM
                                showPageSheet = true
                            },
                            onClosePage = { showPageSheet = false }
                        )
                    }

                    PageType.MOOD -> {
                        mood?.let {
                            MoodScreen(
                                mood = it,
                                onAlbumClick = { id ->
                                    albumId = id
                                    showPageType = PageType.ALBUM
                                    showPageSheet = true
                                },
                                onArtistClick = { id ->
                                    artistId = id
                                    showPageType = PageType.ARTIST
                                    showPageSheet = true
                                },
                                onPlaylistClick = { id ->
                                    playlistId = id
                                    showPageType = PageType.PLAYLIST
                                    showPageSheet = true
                                }
                            )
                        }
                    }

                    PageType.QUICKPICS -> {
                        QuickPicsScreen(
                            onSongClick = {
                                videoId = it.id
                                coroutineScope.launch {
                                    db.insert(it)
                                }
                            },
                            onAlbumClick = {
                                albumId = it
                                showPageType = PageType.ALBUM
                                showPageSheet = true
                            },
                            onArtistClick = {
                                artistId = it
                                showPageType = PageType.ARTIST
                                showPageSheet = true
                            },
                            onPlaylistClick = {
                                playlistId = it
                                showPageType = PageType.PLAYLIST
                                showPageSheet = true
                            },
                            onMoodClick = {
                                mood = it
                                showPageType = PageType.MOOD
                                showPageSheet = true
                            }
                        )
                    }

                    else -> {}
                }
            }
        )

        /*
        AnimatedVisibility(
            visible = showPageSheet,
            enter = expandIn(animationSpec = tween(350, easing = LinearOutSlowInEasing), expandFrom = Alignment.TopStart),
            exit =  shrinkOut(animationSpec = tween(350, easing = FastOutSlowInEasing),shrinkTowards = Alignment.TopStart)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                when(showPageType){
                    PageType.ALBUM -> {
                        AlbumScreen(
                            browseId = albumId.value,
                            onSongClick = {
                                videoId.value = it
                            },
                            onAlbumClick = {
                                albumId.value = it
                                showPageType = PageType.ALBUM
                                showPageSheet = true
                            },
                            onClosePage = { showPageSheet = false }
                        )
                    }
                    PageType.ARTIST -> {
                        ArtistScreen(
                            browseId = artistId.value,
                            onSongClick = {
                                videoId.value = it
                            },
                            onPlaylistClick = {},
                            onViewAllAlbumsClick = {},
                            onViewAllSinglesClick = {},
                            onAlbumClick = {
                                albumId.value = it
                                showPageType = PageType.ALBUM
                                showPageSheet = true
                            },
                            onClosePage = { showPageSheet = false }
                        )
                    }
                    PageType.PLAYLIST -> {}
                    PageType.MOOD -> {}
                    else -> {}
                }
            }
        }
        */
        /*
        CustomModalBottomSheet(
            showSheet = showPageSheet,
            onDismissRequest = { showPageSheet = false },
            containerColor = Color.Black,
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth(),
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            dragHandle = {
                Surface(
                    modifier = Modifier.padding(vertical = 0.dp),
                    color =Color.Black,
                    shape = ThumbnailRoundness.Medium.shape()
                ) {}
            },
            shape = ThumbnailRoundness.Medium.shape()
        ) {
            when(showPageType){
                PageType.ALBUM -> {}
                PageType.ARTIST -> {
                    ArtistScreen(
                        browseId = artistId.value,
                        onSongClick = {
                            videoId.value = it
                        },
                        onPlaylistClick = {},
                        onViewAllAlbumsClick = {},
                        onViewAllSinglesClick = {},
                        onAlbumClick = {},
                        onClosePage = { showPageSheet = false }
                    )
                }
                PageType.PLAYLIST -> {}
                PageType.MOOD -> {}
                else -> {}
            }

        }
         */
    }
}

@Composable
fun ThreeColumnsLayout(
    /*
   onSongClick: (key: String) -> Unit = {},
   onArtistClick: (key: String) -> Unit = {},
   onAlbumClick: (key: String) -> Unit = {},
   onPlaylistClick: (key: String) -> Unit = {},
   onMoodClick: (mood: Innertube.Mood.Item) -> Unit = {},
     */
    onHomeClick: () -> Unit = {},
    frameController: VlcjFrameController = remember { VlcjFrameController() },
    centerPanelContent: @Composable () -> Unit = {}
) {
    Row(Modifier.fillMaxSize()) {
        LeftPanelContent()
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = Color.Gray.copy(alpha = 0.6f)
        )
        CenterPanelContent(
            onHomeClick = onHomeClick,
            content = {
                centerPanelContent()
            }
        )
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = Color.Gray.copy(alpha = 0.6f)
        )
        RightPanelContent(
            onShowPlayer = {}
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                FrameContainer(
                    Modifier.requiredHeight(200.dp),
                    //.border(BorderStroke(1.dp, Color.Red)),
                    frameController.size.collectAsState(null).value?.run {
                        IntSize(first, second)
                    } ?: IntSize.Zero,
                    frameController.bytes.collectAsState(null).value
                )
            }


        }

    }
}

@Composable
fun LeftPanelContent() {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.23f)
            .padding(horizontal = layoutColumnsHorizontalPadding)
            .padding(top = layoutColumnTopPadding)
    ) {
        val (currentTabIndex, setCurrentTabIndex) = remember { mutableStateOf(0) }
        TabRow(
            currentTabIndex,
            modifier = Modifier
                .height(36.dp)
                .fillMaxWidth(),
            backgroundColor = Color.Gray.copy(alpha = 0.2f),
            contentColor = Color.White.copy(alpha = 0.6f)
        ) {
            Tab(
                currentTabIndex == 0,
                onClick = { setCurrentTabIndex(0) },
                text = {
                    //Text("")
                },
                icon = {
                    Image(
                        painter = painterResource(Res.drawable.musical_notes),
                        colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.6f)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                    )
                }

            )
            Tab(
                currentTabIndex == 1,
                onClick = { setCurrentTabIndex(1) },
                text = {
                    //Text("")
                },
                icon = {
                    Image(
                        painter = painterResource(Res.drawable.artists),
                        colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.6f)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            )
            Tab(
                currentTabIndex == 2,
                onClick = { setCurrentTabIndex(2) },
                text = {
                    //Text("")
                },
                icon = {
                    Image(
                        painter = painterResource(Res.drawable.album),
                        colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.6f)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            )
            Tab(
                currentTabIndex == 3,
                onClick = { setCurrentTabIndex(3) },
                text = {
                    //Text("")
                },
                icon = {
                    Image(
                        painter = painterResource(Res.drawable.library),
                        colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.6f)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            )

        }
        /*
        Column(Modifier.fillMaxSize().border(1.dp, color = Color.Black)) {
            Text(text = "Left Panel  ", modifier = Modifier.padding(start = 8.dp, top = layoutColumnTopPadding))
        }
         */
        //Spacer(Modifier.size(100.dp))
        //Text(text = "Left Pane bottom Text Box")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CenterPanelContent(
    onHomeClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.7f)
            .padding(horizontal = layoutColumnsHorizontalPadding)
            .padding(top = layoutColumnTopPadding)
            .padding(bottom = layoutColumnBottomPadding)
            .verticalScroll(scrollState)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(Res.drawable.app_icon),
                colorFilter = ColorFilter.tint(Color.Green.copy(alpha = 0.6f)),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {}
                    )
            )
            Image(
                painter = painterResource(Res.drawable.app_logo_text),
                colorFilter = ColorFilter.tint(
                    Color.White
                    /*
                    when (colorPaletteMode) {
                        ColorPaletteMode.Light, ColorPaletteMode.System -> colorPalette.text
                        else -> Color.White
                    }

                     */
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .clickable { onHomeClick() }
            )
        }

        Column(Modifier.fillMaxSize().border(1.dp, color = Color.Black)) {

            content()

            Spacer(Modifier.height(layoutColumnBottomSpacer))


        }

    }
}


@Composable
fun RightPanelContent(
    onShowPlayer: (Boolean) -> Unit = {},
    content: @Composable () -> Unit
) {
    var showPlayer by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = layoutColumnsHorizontalPadding)
            .padding(top = layoutColumnTopPadding)
    ) {
        /*
        Text(text = "Right Panel", modifier = Modifier.clickable {
            showPlayer = !showPlayer
            onShowPlayer(showPlayer)
        })
        Spacer(Modifier.size(20.dp))

         */
        Spacer(Modifier.size(layoutColumnTopPadding))
        Row(
            //Modifier.border(1.dp, color = Color.Yellow)
        ) {
            content()
        }
    }
}
