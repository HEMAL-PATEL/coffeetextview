package devmike.com.coffeesample

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.AnimRes
import android.support.annotation.IntegerRes
import android.view.View
import android.widget.Button
import android.widget.Toast
import devmike.com.coffeetextview.Animations
import devmike.com.coffeetextview.CoffeeTextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var text1 : CoffeeTextView
    lateinit var text2 : CoffeeTextView
    lateinit var text3 : CoffeeTextView
    lateinit var text4 : CoffeeTextView
    lateinit var text5 : CoffeeTextView

    override fun onClick(v: View?) {
        when (v){
            zoomin_btn ->
                doAnimation(text2, Animations.ZOOM_IN, zoomin_btn)
            zoomout_btn ->
                doAnimation(text1, Animations.ZOOM_OUT, zoomout_btn)
            blink_btn ->
                    doAnimation(text3, Animations.BLINK, blink_btn)
            bounce_btn ->
                    doAnimation(text4, Animations.BOUNCE, bounce_btn)
            fadein_btn ->
                    doAnimation(text5, Animations.FADE_IN, fadein_btn)

        }
    }

    private fun doAnimation(v :CoffeeTextView, anim :Int,  view :Button){
        v.animateCaption(anim).duration =2000
        v.animateTitle(anim)
        v.setCaptionText("Hello you click on: " + view.text.toString())
        v.setCaptionShadow(2f, 2f, 2f, Color.MAGENTA)
        v.setTitleColor(Color.RED)
        v.setTitleShadow(2f, 1f, 2f, Color.YELLOW)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text1 = findViewById(R.id.text_1)
        text2 =text_2
        text3 = text_3
        text4 =text_4
        text5 = text_5

        zoomout_btn.setOnClickListener(this)
        zoomin_btn.setOnClickListener(this)
        bounce_btn.setOnClickListener(this)
        blink_btn.setOnClickListener(this)
        fadein_btn.setOnClickListener(this)

    }


}
