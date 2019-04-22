package sk.mmx.animalProject

import android.content.Intent
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


class GameScreen : AppCompatActivity(), View.OnClickListener {

    private var buttonFirst: Button? = null
    private var buttonSecond: Button? = null
    private var buttonThird: Button? = null

    private var gameArray: List<String> = emptyList()
    private var guessedArray = mutableListOf<String>()

    private var rndAnimal = 0

    private var textAnimal: TextView? = null

    private var mediaPlayer: MediaPlayer? = null
    private var mediaLength: Long? = null

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

        imageViewClose.setOnClickListener {
            val intent = Intent(this@GameScreen, MainActivity::class.java)
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

        newScreen()

    }

    override fun onClick(v: View?) {
        println(v?.tag.toString())

        val btnTag = v?.tag.toString()
        val firstAnimal = gameArray[0]
        val secondAnimal = gameArray[1]
        val thirdAnimal = gameArray[2]

        mediaLength = mediaPlayer?.duration?.toLong()
        mediaLength = mediaLength?.plus(300)

        if (btnTag == "0" && rndAnimal == 0) {

            buttonFirst?.setBackgroundResource(resources.getIdentifier("animal_$firstAnimal" + "_pass","drawable",packageName))

            mediaPlayer?.start()
            guessedArray.add(firstAnimal)

            Handler().postDelayed({
                newScreen()
            }, mediaLength!!)

        } else if (btnTag == "0" && rndAnimal != 0) {
            buttonFirst?.setBackgroundResource(
                resources.getIdentifier(
                    "animal_$firstAnimal" + "_fail",
                    "drawable",
                    packageName
                )
            )
            Handler().postDelayed({
                buttonFirst?.setBackgroundResource(
                    resources.getIdentifier(
                        "animal_$firstAnimal",
                        "drawable",
                        packageName
                    )
                )
            }, 1000)
        }

        if (btnTag == "1" && rndAnimal == 1) {
          buttonSecond?.setBackgroundResource(
                resources.getIdentifier(
                    "animal_$secondAnimal" + "_pass",
                    "drawable",
                    packageName
                )
            )

            mediaPlayer?.start()
            guessedArray.add(secondAnimal)
            Handler().postDelayed({
                newScreen()
            }, mediaLength!!)

        } else if (btnTag == "1" && rndAnimal != 1) {
            buttonSecond?.setBackgroundResource(
                resources.getIdentifier(
                    "animal_$secondAnimal" + "_fail",
                    "drawable",
                    packageName
                )
            )
            Handler().postDelayed({
                buttonSecond?.setBackgroundResource(
                    resources.getIdentifier(
                        "animal_$secondAnimal",
                        "drawable",
                        packageName
                    )
                )
            }, 1000)
        }

        if (btnTag == "2" && rndAnimal == 2) {
            buttonThird?.setBackgroundResource(
                resources.getIdentifier(
                    "animal_$thirdAnimal" + "_pass",
                    "drawable",
                    packageName
                )
            )

            mediaPlayer?.start()
            guessedArray.add(thirdAnimal)
            Handler().postDelayed({
                newScreen()
            }, mediaLength!!)
        } else if (btnTag == "2" && rndAnimal != 2) {
            buttonThird?.setBackgroundResource(
                resources.getIdentifier(
                    "animal_$thirdAnimal" + "_fail",
                    "drawable",
                    packageName
                )
            )
            Handler().postDelayed({
                buttonThird?.setBackgroundResource(
                    resources.getIdentifier(
                        "animal_$thirdAnimal",
                        "drawable",
                        packageName
                    )
                )
            }, 1000)
        }
    }

    private fun newScreen() {

        //mediaPlayer?.stop()

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

            val animalSound = gameArray[rndAnimal]
            mediaPlayer = MediaPlayer.create(this, resources.getIdentifier("animal_$animalSound", "raw", packageName))

            buttonFirst?.setBackgroundResource(resources.getIdentifier("animal_$firstAnimal", "drawable", packageName))
            buttonSecond?.setBackgroundResource(resources.getIdentifier("animal_$secondAnimal", "drawable", packageName))
            buttonThird?.setBackgroundResource(resources.getIdentifier("animal_$thirdAnimal", "drawable", packageName))
        }
    }

    private fun enterFullScreen() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}