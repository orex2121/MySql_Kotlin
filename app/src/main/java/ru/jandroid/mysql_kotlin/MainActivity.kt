package ru.jandroid.mysql_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject
import ru.jandroid.mysql_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var  binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Кнопка добавить артиста
        binding.buttonAddArtist.setOnClickListener { add_del_upd_Artist(EndPoints.URL_ADD_ARTIST) }

        // Кнопка удалить артиста - поиск записи по ID
        binding.buttonDelArtist.setOnClickListener { add_del_upd_Artist(EndPoints.URL_DEL_ARTIST) }

        // Кнопка "Все артисты" - отправляет на ViewArtistsActivity
        binding.buttonViewArtist.setOnClickListener {
            val intent = Intent(applicationContext, ViewArtistsActivity::class.java)
            startActivity(intent)
        }

        // Кнопка "Обновить запись" - поиск записи по ID
        binding.buttonUpdArtist.setOnClickListener { add_del_upd_Artist(EndPoints.URL_UPD_ARTIST) }
    }

    // Функция Добавить, Удалить, Обновить артиста в Базе данных
    private fun add_del_upd_Artist(endPoints: String) {

        val id = binding.editTextArtistId.text.toString()
        val name = binding.editTextArtistName.text.toString()
        val genre = binding.spinnerGenre.selectedItem.toString()

        //Создаем запрос с помощью Volley
        val stringRequest1 = object : StringRequest(
            Request.Method.POST, endPoints,
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
        VolleySingleton.instance?.addToRequestQueue(stringRequest1)
    }
}