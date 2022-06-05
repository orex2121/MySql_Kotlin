package ru.jandroid.mysql_kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.jandroid.mysql_kotlin.databinding.ArtistItemBinding

class ArtistAdapter(val listener:Listener):RecyclerView.Adapter<ArtistAdapter.ArtistHolder>() {
    var artistList=ArrayList<Artist>()

    //ViewHolder Class
    class ArtistHolder(item: View):RecyclerView.ViewHolder(item) {
        val binding= ArtistItemBinding.bind(item)
        fun bind(artist:Artist, listener:Listener) = with(binding){
            textViewId.text=artist.id
            textViewName.text=artist.name
            textViewGenre.text=artist.genre
            itemView.setOnClickListener {
                listener.oClick(artist, position)
            }
            buttonDelete.setOnClickListener {
                listener.oClickDelete(artist)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.artist_item, parent, false)
        return ArtistHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistHolder, position: Int) {
        holder.bind(artistList[position], listener)
    }

    override fun getItemCount(): Int {
        return artistList.size
    }

// ----------------------------- ОБРАБОТКА НАЖАТИЙ
    //Добавляем элемент поштучно
    fun addArtist(artist:Artist){
        artistList.add(artist)
        notifyDataSetChanged()
    }

    //Удаляем элемент
    fun deleteArtist(artist:Artist){
        artistList.remove(artist)
        notifyDataSetChanged()
    }

    //Обновляет элемент
    fun updateArtist(artist:Artist, index : Int){
        artistList[index].name = artist.name
        artistList[index].genre = artist.genre
        notifyDataSetChanged()
    }

    //наполняем элементами при загрузке
    fun fillArtist(artistListFromMain:ArrayList<Artist>){
        artistList = artistListFromMain
        notifyDataSetChanged()
    }

//-------------- ИНТЕРФЕЙС ДЛЯ СЛУШАТЕЛЕЙ
    interface Listener{
        fun oClick(artist:Artist, index: Int)
        fun oClickDelete(artist:Artist)
    }
}