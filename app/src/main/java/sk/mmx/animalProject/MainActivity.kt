package sk.mmx.animalProject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity(), View.OnTouchListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hide the status bar.
        enterFullScreen()

        val btnFarm = findViewById<Button>(R.id.buttonFarm)
        val btnForest = findViewById<Button>(R.id.buttonForest)
        val btnPolar = findViewById<Button>(R.id.buttonPolar)
        val btnSavana = findViewById<Button>(R.id.buttonSavana)

        btnFarm.setOnTouchListener(this)
        btnForest.setOnTouchListener(this)
        btnPolar.setOnTouchListener(this)
        btnSavana.setOnTouchListener(this)


        val imageViewSettings = findViewById<ImageView>(R.id.imageViewSettings)
        imageViewSettings.setOnClickListener {
            val intent = Intent(this, SettingsScreen::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Hide the status bar.
        enterFullScreen()
    }

    override fun onRestart() {
        super.onRestart()
        enterFullScreen()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        val action = event?.actionMasked
        val btnTag = v?.tag.toString()

        when (action) {
            0 -> when (btnTag) {
                "1" -> {
                    v?.setBackgroundResource(R.drawable.btn_farm_tap3x)
                    environment = "farm"
                }
                "2" -> {
                    v?.setBackgroundResource(R.drawable.btn_polar_tap3x)
                    environment = "polar"
                }
                "3" -> {
                    v?.setBackgroundResource(R.drawable.btn_savana_tap3x)
                    environment = "savana"
                }
                "4" -> {
                    v?.setBackgroundResource(R.drawable.btn_forest_tap3x)
                    environment = "forest"
                }
            }
            1 -> when (btnTag) {
                "1" -> {
                    v?.setBackgroundResource(R.drawable.btn_farm3x)
                    toGameScreen()
                }
                "2" -> {
                    v?.setBackgroundResource(R.drawable.btn_polar3x)
                    toGameScreen()
                }
                "3" -> {
                    v?.setBackgroundResource(R.drawable.btn_savana3x)
                    toGameScreen()
                }
                "4" -> {
                    v?.setBackgroundResource(R.drawable.btn_forest3x)
                    toGameScreen()
                }
            }
        }
        return true
    }

    private fun toGameScreen() {
        val intent = Intent(this, GameScreen::class.java)
        startActivity(intent)
    }

    private fun enterFullScreen() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

}
