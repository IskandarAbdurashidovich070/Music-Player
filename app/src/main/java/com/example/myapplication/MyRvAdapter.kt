package com.example.myapplication

import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.RvItemBinding
import java.net.URI

class MyRvAdapter(var list: List<Music>, var clic: Clic) : RecyclerView.Adapter<MyRvAdapter.Vh>() {
    
    inner class Vh(var rvItemBinding: RvItemBinding):RecyclerView.ViewHolder(rvItemBinding.root){
        fun onBind(music: Music, position: Int ){



            rvItemBinding.artistName.text = music.artist
            rvItemBinding.musicName.text = music.name
            rvItemBinding.rvPlay.setOnClickListener {
                if (Data.media != null){
                    Data.media?.stop()
                    val mediaPlayer = MediaPlayer.create(rvItemBinding.root.context, Uri.parse(music.music) )
                    mediaPlayer.start()
                    Data.stop = 1
                    Data.media = mediaPlayer
                }else{
                    val mediaPlayer = MediaPlayer.create(rvItemBinding.root.context, Uri.parse(music.music) )
                    Data.media = mediaPlayer
                    mediaPlayer.start()
                }
                Data.path = music.music
                Data.name = music.name
                Data.position = position
                clic.OnClick(music)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemBinding.inflate(LayoutInflater.from(parent.context) , parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }
    override fun getItemCount(): Int = list.size

}

interface Clic{
    fun OnClick(music: Music)
}

