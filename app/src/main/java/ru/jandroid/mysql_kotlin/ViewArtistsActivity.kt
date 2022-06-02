package ru.jandroid.mysql_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ru.jandroid.mysql_kotlin.databinding.ActivityViewArtistsBinding

class ViewArtistsActivity : AppCompatActivity() {
    lateinit var binding : ActivityViewArtistsBinding
    private var artistList: MutableList<Artist>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewArtistsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        artistList = mutableListOf()
        loadArtists()
    }

    // Загрузить JSON из сети
    private fun loadArtists() {
        val stringRequest2 = StringRequest(Request.Method.GET, EndPoints.URL_GET_ARTIST,
            { response ->
                val obj = JSONObject(response)
                val objArray = obj.getJSONArray("artists")


                for (i in 0 until objArray.length()) {
                    val objectArtist = objArray.getJSONObject(i)
                    val artist = Artist(
                        objectArtist.getString("id"),
                        objectArtist.getString("name"),
                        objectArtist.getString("genre")
                    )
                    artistList!!.add(artist)
                    val adapter = ArtistList(this@ViewArtistsActivity, artistList!!)
                    binding.listViewArtists.adapter = adapter
                }
            },
            {
                Log.d("MyLog", "Ошибка Volly: ${it.toString()}")
            })

        // Создаём очередь
        val queue = Volley.newRequestQueue(this)
        // Добавляем в очередь
        queue.add(stringRequest2)
    }
}