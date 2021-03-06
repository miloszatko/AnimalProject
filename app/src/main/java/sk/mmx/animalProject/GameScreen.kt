package sk.mmx.animalProject

import android.content.Intent
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.util.*


class GameScreen : AppCompatActivity(), View.OnClickListener, TextToSpeech.OnInitListener {

    private var buttonFirst: Button? = null
    private var buttonSecond: Button? = null
    private var buttonThird: Button? = null

    private var gameArray: List<String> = emptyList()
    private var guessedArray = mutableListOf<String>()

    private var rndAnimal = 0

    private var textAnimal: TextView? = null

    private var mediaPlayer: MediaPlayer? = null
    private var mediaLength: Long? = null

    private var mTTS:TextToSpeech? = null


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            mTTS!!.language = Locale.UK
            mTTS!!.setSpeechRate(0.8F)
            Log.i("TTS", "Succesfully initialised")
            newScreen()
         } else {
            Log.e("TTS", "Not initialised")
            newScreen()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_screen)

        enterFullScreen()

        val imageViewBackground = findViewById<ImageView>(R.id.imageViewBackground)
        val imageViewClose = findViewById<ImageView>(R.id.imageViewClose)

        textAnimal = findViewById(R.id.textAnimal)
        val typeface = Typeface.createFromAsset(assets, "Kalam-Bold.ttf")
        textAnimal?.typeface = typeface

        buttonFirst = findViewById(R.id.btnFirst)
        buttonSecond = findViewById(R.id.btnSecond)
        buttonThird = findViewById(R.id.btnThird)

        buttonFirst?.setOnClickListener(this)
        buttonSecond?.setOnClickListener(this)
        buttonThird?.setOnClickListener(this)

        mTTS = TextToSpeech(this, this)

        imageViewClose.setOnClickListener {
               val intent = Intent(this, MainActivity::class.java)
               startActivity(intent)
        }

        when (environment) {
            "farm" -> {
                imageViewBackground.setBackgroundResource(R.drawable.bkg_farm3x)
                gameArray = envFarm.shuffled()
            }
            "savana" -> {
                imageViewBackground.setBackgroundResource(R.drawable.bkg_savana3x)
                gameArray = envSavana.shuffled()
            }
            "polar" -> {
                imageViewBackground.setBackgroundResource(R.drawable.bkg_polar3x)
                gameArray = envPolar.shuffled()
            }
            "forest" -> {
                imageViewBackground.setBackgroundResource(R.drawable.bkg_forest3x)
                gameArray = envForest.shuffled()
            }
        }
    }

    override fun onClick(v: View?) {

        disableButtons()

        val btnTag = v?.tag.toString()
        val firstAnimal = gameArray[0]
        val secondAnimal = gameArray[1]
        val thirdAnimal = gameArray[2]

       if (btnTag == "0" && rndAnimal == 0) {

            buttonFirst?.setBackgroundResource(resources.getIdentifier("animal_$firstAnimal" + "_pass","drawable",packageName))
            playSound(firstAnimal)
            guessedArray.add(firstAnimal)

            Handler().postDelayed({
                newScreen()
            }, mediaLength!!)

        } else if (btnTag == "0" && rndAnimal != 0) {
            buttonFirst?.setBackgroundResource(
                resources.getIdentifier("animal_$firstAnimal" + "_fail","drawable",packageName))
                 playSound("fail")
            Handler().postDelayed({
                buttonFirst?.setBackgroundResource(
                    resources.getIdentifier("animal_$firstAnimal","drawable",packageName))
                enableButtons()
            }, 1000)
        }

        if (btnTag == "1" && rndAnimal == 1) {
          buttonSecond?.setBackgroundResource(
            resources.getIdentifier("animal_$secondAnimal" + "_pass","drawable",packageName))
            playSound(secondAnimal)

            guessedArray.add(secondAnimal)
            Handler().postDelayed({
                newScreen()
            }, mediaLength!!)

        } else if (btnTag == "1" && rndAnimal != 1) {
            buttonSecond?.setBackgroundResource(resources.getIdentifier("animal_$secondAnimal" + "_fail","drawable",packageName))
            playSound("fail")
            Handler().postDelayed({
                buttonSecond?.setBackgroundResource(resources.getIdentifier("animal_$secondAnimal","drawable",packageName))
                enableButtons()
            }, 1000)
        }

        if (btnTag == "2" && rndAnimal == 2) {
            buttonThird?.setBackgroundResource(
            resources.getIdentifier("animal_$thirdAnimal" + "_pass","drawable",packageName))
            playSound(thirdAnimal)

            guessedArray.add(thirdAnimal)
            Handler().postDelayed({
                newScreen()
            }, mediaLength!!)
        } else if (btnTag == "2" && rndAnimal != 2) {
            buttonThird?.setBackgroundResource(resources.getIdentifier("animal_$thirdAnimal" + "_fail","drawable",packageName))
            playSound("fail")

            Handler().postDelayed({
                buttonThird?.setBackgroundResource(resources.getIdentifier("animal_$thirdAnimal","drawable",packageName))
                enableButtons()
            }, 1000)
        }
    }

    private fun newScreen() {

        //mediaPlayer?.stop()

        enableButtons()

        if (guessedArray.size == gameArray.size) {
            val intent = Intent(this@GameScreen, MainActivity::class.java)
            startActivity(intent)
        } else {
            do {
                var y = true
                gameArray = gameArray.shuffled()
                rndAnimal = (0..2).random()

                guessedArray.forEach {
                    if (it == gameArray[rndAnimal]) {
                        y = false
                    }
                }
            } while (!y)

            val firstAnimal = gameArray[0]
            val secondAnimal = gameArray[1]
            val thirdAnimal = gameArray[2]

            textAnimal?.text = gameArray[rndAnimal].capitalize()

            buttonFirst?.setBackgroundResource(resources.getIdentifier("animal_$firstAnimal", "drawable", packageName))
            buttonSecond?.setBackgroundResource(resources.getIdentifier("animal_$secondAnimal", "drawable", packageName))
            buttonThird?.setBackgroundResource(resources.getIdentifier("animal_$thirdAnimal", "drawable", packageName))

            sayAnimal(gameArray[rndAnimal])
        }
    }


    private fun sayAnimal (toSpeak: String){

        println(toSpeak)

        mTTS!!.speak("Which animal is $toSpeak", TextToSpeech.QUEUE_FLUSH, null, "")

    }

    private fun playSound (input: String) {

        if (input == "fail") {
            mediaPlayer = MediaPlayer.create(this, resources.getIdentifier(input, "raw", packageName))
        } else {
            mediaPlayer = MediaPlayer.create(this, resources.getIdentifier("animal_$input", "raw", packageName))
            mediaLength = mediaPlayer?.duration?.toLong()
            mediaLength = mediaLength?.plus(300)
        }

        mediaPlayer?.start()

    }

    private fun disableButtons() {
        buttonFirst?.isClickable = false
        buttonSecond?.isClickable = false
        buttonThird?.isClickable = false
    }

    private fun enableButtons() {
        buttonFirst?.isClickable = true
        buttonSecond?.isClickable = true
        buttonThird?.isClickable = true
    }


    private fun enterFullScreen() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

}