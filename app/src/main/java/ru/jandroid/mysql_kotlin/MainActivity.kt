package ru.jandroid.mysql_kotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.custom_alert_dialog.view.*
import org.json.JSONException
import org.json.JSONObject
import ru.jandroid.mysql_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ArtistAdapter.Listener {
    lateinit var binding: ActivityMainBinding
    private val adapter = ArtistAdapter(this)
    private var artistList = ArrayList<Artist>()
    private var index = 0
    private var artist = Artist("", "", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerInit()
        loadArtists()

        // Обработка кнопки +
        binding.buttonAdd.setOnClickListener { Dialog_Add_Update(this, "add") }
    }

    //Обработка КЛИКА по элементу
    override fun oClick(artist: Artist, index: Int) {
        this.artist = artist
        this.index = index
        Dialog_Add_Update(this, "upd", artist)
    }

    //Обработка кнопки УДАЛИТЬ
    override fun oClickDelete(artist: Artist) {
        AlertDialog.Builder(this)
            .setTitle("Удаление растения")
            .setMessage("Вы действительно хотите удалить растение?")
            .setPositiveButton("Да"){dialog, which ->
                adapter.deleteArtist(artist)
                add_del_upd_Artist(EndPoints.URL_DEL_ARTIST, artist)
            }.setNegativeButton("Нет"){dialog, which ->

            }
            .show()
    }

    // Включаем RECYCLER ADAPTER
    private  fun recyclerInit(){
        binding.apply {
            recyclerViewArtists.layoutManager = GridLayoutManager(this@MainActivity,1)
            recyclerViewArtists.adapter=adapter
            //rcView.setHasFixedSize(true) //фиксируем размер Item
        }
    }

    // Диалоговое окно ДОБАВИТЬ ОБНОВИТЬ
    private fun Dialog_Add_Update(context: Context, add_upd: String, artist:Artist = Artist("","","")){
        val view = View.inflate(context, R.layout.custom_alert_dialog, null)
        view.apply {
            val customDialog = AlertDialog.Builder(context).setView(view)
            val dialog = customDialog.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            //dialog.setCancelable(false)

            etName.setText(artist.name)
            etGenre.setText(artist.genre)

            if (add_upd == "add") btn_AddUpdate.setText("Добавить запись")
            if (add_upd == "upd") btn_AddUpdate.setText("Обновить запись")

            btnClose.setOnClickListener { dialog.dismiss() }

            btn_AddUpdate.setOnClickListener {
                artist.name = etName.text.toString().trim()
                artist.genre = etGenre.text.toString().trim()
                if (artist.name.isNotEmpty() && artist.genre.isNotEmpty()) {
                    if (add_upd == "add") {
                        add_del_upd_Artist(EndPoints.URL_ADD_ARTIST, artist)
                        Thread.sleep(500)
                        loadArtists()
                        adapter.addArtist(artist)

                    } else if (add_upd == "upd") {
                        add_del_upd_Artist(EndPoints.URL_UPD_ARTIST, artist)
                        adapter.updateArtist(artist, index)
                    }
                    dialog.dismiss()
                } else showToast("Пожалуйста, заполните все поля!")
            }
        }
    }

// ---------------------------- JSON загрузить из сети
    private fun loadArtists() {
    showToast("Загрузка")
        val stringRequest = StringRequest(Request.Method.GET, EndPoints.URL_GET_ARTIST,
            { response ->
                val obj = JSONObject(response)
                val objArray = obj.getJSONArray("artists")

                artistList.clear()
                for (i in 0 until objArray.length()) {
                    val objectArtist = objArray.getJSONObject(i)
                    val artist = Artist(
                        objectArtist.getString("id"),
                        objectArtist.getString("name"),
                        objectArtist.getString("genre")
                    )
                    artistList.add(artist)
                    adapter.fillArtist(artistList)
                }
            },
            {
                Log.d("MyLog", "Ошибка Volly: ${it.toString()}")
            })

        // Создаём очередь
        val queue = Volley.newRequestQueue(this)
        // Добавляем в очередь
        queue.add(stringRequest)
    }

// ----------------------------БД ----- добавить, удалить, обновить
    private fun add_del_upd_Artist(endPoint: String, artist: Artist) {

        val id = artist.id
        val name = artist.name
        val genre = artist.genre

        //Создаем запрос с помощью Volley
        val stringRequest = object : StringRequest(
            Request.Method.POST, endPoint,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
                }
            })
        {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("id", id)
                params.put("name", name)
                params.put("genre", genre)
                return params
            }
        }

        // Добавляем запрос в очередь
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}