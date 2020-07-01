package com.example.mediaplayerbasic

import android.icu.text.CaseMap

class SongInfo{
    var title: String? = null
    var authoreName: String? = null
    var songUrl: String? = null

    constructor(title: String?, authoreName: String?, songUrl: String?) {
        this.title = title
        this.authoreName = authoreName
        this.songUrl = songUrl
    }
}