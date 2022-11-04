package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication.databinding.FragmentHomeBinding
import kotlin.random.Random

class Home_Fragment : Fragment(), Clic {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var list: ArrayList<Music>
    private lateinit var listShuffule: ArrayList<Music>
    private lateinit var myRvAdapter: MyRvAdapter
    private lateinit var myRvAdapter2: MyRvAdapter
    private lateinit var handler: Handler
    private var REQUEST_PERMISSSION: Int = 99
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        list = ArrayList()
        listShuffule = ArrayList()
        myRvAdapter = MyRvAdapter(list, this)


        myRvAdapter2 = MyRvAdapter(listShuffule, this)




        if (ActivityCompat.checkSelfPermission(
                binding.root.context,

                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                container!!.context as Activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSSION
            )
        } else {
            getSongs()
        }
        binding.rvPlaylist.adapter = myRvAdapter
        binding.rvShuffel.adapter = myRvAdapter2


        for (i in list) {
            if (Data.stop == 0) {
                binding.pause.setImageResource(R.drawable.ic_pause)
                Data.media?.start()
            } else {
                Data.media?.pause()
                binding.pause.setImageResource(R.drawable.ic_play)
            }

            binding.pause.setOnClickListener {
                if (Data.stop == 0) {
                    Data.stop = 1
                    binding.pause.setImageResource(R.drawable.ic_pause)
                    Data.media?.start()
                } else {
                    Data.stop = 0
                    binding.pause.setImageResource(R.drawable.ic_play)
                    Data.media?.pause()
                }

            }
        }


        if (Data.media != null) {
            binding.seekBar.max = Data.media!!.duration
        }


        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    Data.media!!.seekTo(p1)

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })



        binding.next.setOnClickListener {
            if (Data.position>=list.size-1){
                Data.position = 0
                Data.media?.stop()
                val mediaPlayer = MediaPlayer.create(binding.root.context, Uri.parse(list[Data.position].music))
                Data.media = mediaPlayer
                mediaPlayer.start()
            }else{
                Data.position = ++Data.position
                Data.media?.stop()
                val mediaPlayer = MediaPlayer.create(binding.root.context, Uri.parse(list[Data.position].music))
                Data.media = mediaPlayer
                mediaPlayer.start()
            }

        }

        binding.skip.setOnClickListener {
            if (Data.position > 0){
                Data.position = --Data.position
                Data.media?.stop()
                val mediaPlayer = MediaPlayer.create(binding.root.context, Uri.parse(list[Data.position].music))
                Data.media = mediaPlayer
                mediaPlayer.start()
            }
        }

        val music = Music()


        return binding.root
    }

    @SuppressLint("Recycle", "NotifyDataSetChanged", "InlinedApi")
    private fun getSongs(): List<Music> {
        val contentResolver: ContentResolver = requireActivity().contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val indexTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val indexData = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val imageId: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val indexArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)

            do {
                val music = Music(
                    cursor.getString(indexTitle),
                    cursor.getString(indexArtist),
                    cursor.getString(imageId),
                    cursor.getString(indexData),
                )
                list.add(
                    music
                )
                listShuffule.add(
                    music
                )

                val index = list.indexOf(music)
                myRvAdapter.notifyItemInserted(index)
            } while (cursor.moveToNext())
        }
        return list
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSongs()
            }
        }
        seekBarChanged()
    }


    fun seekBarChanged(){
        handler= Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, 1000)
    }

    val runnable=object : Runnable{
        override fun run() {
            binding.seekBar.progress=Data.media!!.currentPosition
            handler.postDelayed(this, 1000)
        }

    }

    override fun onDestroy() {
        Data.media?.stop()
        super.onDestroy()
    }

    override fun onPause() {
        Data.media?.stop()
        super.onPause()
    }

    override fun OnClick(music: Music) {
//        binding.artist.text = music.name

        Toast.makeText(binding.root.context, "${music.name}", Toast.LENGTH_SHORT).show()
    }
}
