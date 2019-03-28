package com.yusufcakmak.exomediasources

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_extractor_media_source.*
import com.google.android.exoplayer2.util.MimeTypes
import android.R.id
import android.net.Uri
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.SingleSampleMediaSource






class MergingMediaSourceActivity : AppCompatActivity() {

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private val trackSelectionFactory = AdaptiveTrackSelection.Factory()
    private var trackSelector: DefaultTrackSelector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merging_media_source)
    }


    private fun initializePlayer() {
        trackSelector = DefaultTrackSelector(trackSelectionFactory)
        mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))

        val dataSpec = DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.himym))
        val rawDataSource = RawResourceDataSource(this)
        rawDataSource.open(dataSpec)

        val mediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(rawDataSource.uri)


        val subtitleFormat = Format.createTextSampleFormat(
            null, // An identifier for the track. May be null.
            MimeTypes.APPLICATION_SUBRIP, // The mime type. Must be set correctly.
            C.SELECTION_FLAG_DEFAULT, // Selection flags for the track.
            "en"
        )

        val subtitleSourceMediaSource = SingleSampleMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(Uri.parse("https://firebasestorage.googleapis.com/v0/b/tyrapp-62319.appspot.com/o/himym.srt?alt=media&token=65509e43-c015-47a2-9dd3-7d47c5a250f6"), subtitleFormat, C.TIME_UNSET)


        val mergedSource = MergingMediaSource(mediaSource, subtitleSourceMediaSource)

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        with(player) {
            prepare(mergedSource, false, false)
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