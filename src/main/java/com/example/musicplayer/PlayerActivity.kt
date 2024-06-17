package com.example.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
lateinit var binding: ActivityPlayerBinding
var btnState = 0
    companion object{
     lateinit   var MusicList:ArrayList<Music>
        var currentIndex:Int= 0
        var mediaPlayer: MediaPlayer? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playerBtn.setImageResource(R.drawable.pause)
        binding.playerBtn.setOnClickListener {
            if (btnState==0){
                binding.playerBtn.setImageResource(R.drawable.pause)
                
                btnState = 1


            }
            else{
                binding.playerBtn.setImageResource(R.drawable.playbutton)
                btnState = 0
            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        currentIndex =  intent.getIntExtra("POSITION",0)
        var response = intent.getStringExtra("CLASS")
        when(response){
            "MUSICADAPTER"->{
                MusicList = ArrayList<Music>()
                MusicList.addAll(MainActivity.MusicData)
                if (MusicList.isNotEmpty()){
                    Toast.makeText(this, "Song Will play", Toast.LENGTH_SHORT).show()
                    mediaPlayer = MediaPlayer()
                    mediaPlayer!!.reset()
                    mediaPlayer!!.setDataSource(MusicList[currentIndex].path)
                    mediaPlayer!!.prepare()
                    mediaPlayer!!.start()
                }
                else{
                    Toast.makeText(this, "Music List is Empty", Toast.LENGTH_SHORT).show()
                }
            }

            else -> Toast.makeText(this, "repose don't match", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer?.isPlaying == true){
            mediaPlayer?.stop()
        }
    }
}