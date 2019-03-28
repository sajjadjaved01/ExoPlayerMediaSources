package com.yusufcakmak.exomediasources

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_extractor_media_source.*
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource




class ConcatenatingMediaSourceActivity : AppCompatActivity() {

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private val trackSelectionFactory = AdaptiveTrackSelection.Factory()
    private var trackSelector: DefaultTrackSelector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concatenating_media_source)
    }

    private fun initializePlayer() {
        trackSelector = DefaultTrackSelector(trackSelectionFactory)
        mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))

        val firstVideoDataSpec = DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.video))
        val firstVideoRawDataSource = RawResourceDataSource(this)
        firstVideoRawDataSource.open(firstVideoDataSpec)

        val secondDataSpec = DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.himym))
        val secondVideoRawDataSource = RawResourceDataSource(this)
        secondVideoRawDataSource.open(secondDataSpec)


        val firstVideoMediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(firstVideoDataSpec.uri)

        val secondVideoMediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(secondDataSpec.uri)

        val loopingMediaSource = LoopingMediaSource(firstVideoMediaSource, 2)

        val concatenatedSource = ConcatenatingMediaSource(loopingMediaSource, secondVideoMediaSource)

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        with(player) {
            prepare(concatenatedSource, false, false)
            playWhenReady = true
        }

        playerView.setShutterBackgroundColor(Color.TRANSPARENT)
        playerView.player = player
        playerView.requestFocus()
    }

    private fun releasePlayer() {
        player.release()
        trackSelector = null
    }

    public override fun onStart() {
        super.onStart()

        if (Util.SDK_INT > 23) initializePlayer()
    }

    public override fun onResume() {
        super.onResume()

        if (Util.SDK_INT <= 23) initializePlayer()
    }

    public override fun onPause() {
        super.onPause()

        if (Util.SDK_INT <= 23) releasePlayer()
    }

    public override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) releasePlayer()
    }

}