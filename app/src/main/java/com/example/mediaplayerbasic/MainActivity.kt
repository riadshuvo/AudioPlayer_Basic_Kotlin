package com.example.mediaplayerbasic

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*
import java.lang.Exception
import java.text.FieldPosition

class MainActivity : AppCompatActivity() {
    var songList = ArrayList<SongInfo>()
    var adapter: MySongAdapter? = null
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ///this function will load song from internet
        //loadSong()

        //this function will load song from device storage
        CheckUserPermsions()

        adapter = MySongAdapter(songList)
        listView.adapter = adapter

        var mySongTrac = MySongTrac()
        mySongTrac.start()

    }

    fun loadSong() {
        songList.add(
            SongInfo(
                "Sura Fatiha",
                "a_ahmed",
                "http://server6.mp3quran.net/a_ahmed/001.mp3"
            )
        )
        songList.add(
            SongInfo(
                "Sura Fatiha",
                "a_ahmed",
                "http://server6.mp3quran.net/a_ahmed/002.mp3"
            )
        )
        songList.add(
            SongInfo(
                "Sura Fatiha",
                "a_ahmed",
                "http://server6.mp3quran.net/a_ahmed/003.mp3"
            )
        )
        songList.add(
            SongInfo(
                "Sura Fatiha",
                "a_ahmed",
                "http://server6.mp3quran.net/a_ahmed/004.mp3"
            )
        )
        songList.add(
            SongInfo(
                "Sura Fatiha",
                "a_ahmed",
                "http://server6.mp3quran.net/a_ahmed/005.mp3"
            )
        )
        songList.add(
            SongInfo(
                "Sura Fatiha",
                "a_ahmed",
                "http://server6.mp3quran.net/a_ahmed/006.mp3"
            )
        )
    }

    inner class MySongAdapter : BaseAdapter {

        var myListSong = ArrayList<SongInfo>()

        constructor(mySongList: ArrayList<SongInfo>) : super() {
            this.myListSong = mySongList
        }


        override fun getView(position: Int, view: View?, p2: ViewGroup?): View {
            val myView = layoutInflater.inflate(R.layout.song_ticket, null)
            val song = this.myListSong[position]
            myView.songName.text = song.title
            myView.authorName.text = song.authoreName
            myView.button.setOnClickListener() {

                if (myView.button.text == "Stop") {
                    mediaPlayer!!.stop()
                    myView.button.text = "play"

                } else {

                    mediaPlayer = MediaPlayer()

                    try {
                        mediaPlayer!!.setDataSource(song.songUrl)
                        mediaPlayer!!.prepare()
                        mediaPlayer!!.start()
                        myView.button.text = "Stop"
                        seekBar.max = mediaPlayer!!.duration
                    } catch (ex: Exception) {
                        print(ex.message)
                    }

                }

            }

            return myView
        }

        override fun getItem(p0: Int): Any {
            return this.myListSong[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return this.myListSong.size
        }

    }

    inner class MySongTrac() : Thread() {
        override fun run() {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (ex: Exception) {
                }

                runOnUiThread {
                    if (mediaPlayer != null) {
                        seekBar.progress = mediaPlayer!!.currentPosition
                    }
                }
            }
        }
    }

    fun CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_ASK_PERMISSIONS
                )
                return
            }
        }

        LoadSong()

    }

    //get acces to location permsion
    private val REQUEST_CODE_ASK_PERMISSIONS = 123


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoadSong()
            } else {
                // Permission Denied
                Toast.makeText(this, "denail", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun LoadSong() {
        val allSongsURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = contentResolver.query(allSongsURI, null, selection, null, null)
        if (cursor != null) {
            if (cursor!!.moveToFirst()) {

                do {

                    val songURL =
                        cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val SongAuthor =
                        cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val SongName =
                        cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    songList.add(SongInfo(SongName, SongAuthor, songURL))
                } while (cursor!!.moveToNext())


            }
            cursor!!.close()

            adapter = MySongAdapter(songList)
            listView.adapter = adapter
        }
    }


}


