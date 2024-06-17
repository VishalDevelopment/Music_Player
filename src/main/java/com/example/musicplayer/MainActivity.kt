package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    companion object{
        var MusicData:ArrayList<Music> = ArrayList()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestRuntimePermission()
        setContentView(binding.root)

       MusicData = getAllAudio()
        binding.totalSong.text = "Total Song :  "+ MusicData.size
        binding.songRv.adapter = MusicAdapter(this, MusicData)
        binding.songRv.layoutManager = LinearLayoutManager(this)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fav.setOnClickListener {
            startActivity(Intent(this, FavouriteActivity::class.java))
        }
        binding.playlist.setOnClickListener {
            startActivity(Intent(this, PlaylistActivity::class.java))
        }

        binding.shuffle.setOnClickListener {
            Toast.makeText(this, "Shuffle Clicked", Toast.LENGTH_SHORT).show()
        }

        toggle =
            ActionBarDrawerToggle(this, binding.main, R.string.open_string, R.string.close_string)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.feedback -> Toast.makeText(this, "FeedBack", Toast.LENGTH_SHORT).show()
                R.id.exit -> Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show()
                R.id.rate_us -> Toast.makeText(this, "Rate US", Toast.LENGTH_SHORT).show()
            }
            true
        }

    }


    @SuppressLint("Range")
    private fun getAllAudio(): ArrayList<Music> {
        val tempList = ArrayList<Music>()

        // Filter for music files
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        // Projection to retrieve necessary data
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA
        )

        // Ensure correct sorting order
        val sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC"

        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,  // No selection arguments
            sortOrder,  // Correctly sorted by date added (newest first)
            null
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUriC = Uri.withAppendedPath(uri,albumC).toString()

                    // Create Music object only if file exists (prevents errors)
                    val file = File(pathC)
                    if (file.exists()) {
                        val music = Music(idC, title, albumC, artist, duration, pathC,artUriC)
                        tempList.add(music)

                    }
                } while (cursor.moveToNext())
                cursor.close()
            }
        }

        return tempList
    }

    private fun requestRuntimePermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Permission Granted !!", Toast.LENGTH_SHORT).show()
        }
    }
}