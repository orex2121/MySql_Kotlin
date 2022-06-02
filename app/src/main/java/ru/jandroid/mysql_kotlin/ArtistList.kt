package ru.jandroid.mysql_kotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ArtistList(val context: Activity, var artists: List<Artist>) : ArrayAdapter<Artist>(context, R.layout.layout_list_artist, artists) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_list_artist, null, true)

        val textViewId = listViewItem.findViewById(R.id.textViewId) as TextView
        val textViewName = listViewItem.findViewById(R.id.textViewName) as TextView
        val textViewGenre = listViewItem.findViewById(R.id.textViewGenre) as TextView

        val artist = artists[position]
        textViewId.text = artist.id
        textViewName.text = artist.name
        textViewGenre.text = artist.genre

        return listViewItem
    }
}