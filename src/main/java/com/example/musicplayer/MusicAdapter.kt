package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.databinding.MusicViewBinding

class MusicAdapter(val context :Context , val musicList:ArrayList<Music>):RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    class ViewHolder(binding: MusicViewBinding):RecyclerView.ViewHolder(binding.root) {
        var image = binding.songImg
        var name = binding.dongName
        var movie = binding.songMovie
        var duration = binding.songTime
        var root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MusicViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = musicList[position].title
        holder.duration.text = formateDuration(musicList[position].duration)
        holder.movie.text = musicList[position].album
        Glide.with(context).load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_icon))
            .into(holder.image)
        holder.root.setOnClickListener {

          var  intent = Intent(context,PlayerActivity::class.java)
            intent.putExtra("POSITION",position)
            intent.putExtra("CLASS","MUSICADAPTER")
            context.startActivity(intent)
        }

    }
}