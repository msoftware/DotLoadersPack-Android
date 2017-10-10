package com.agrawalsuneet.dotsloader.ui

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import com.agrawalsuneet.dotsloader.R
import com.agrawalsuneet.dotsloader.ui.basicviews.CircleView
import com.agrawalsuneet.dotsloader.ui.basicviews.LoaderContract
import com.agrawalsuneet.dotsloader.ui.basicviews.ModifiedLinearLayout

/**
 * Created by suneet on 10/10/17.
 */
class TashieLoader : ModifiedLinearLayout, LoaderContract {

    var noOfDots: Int = 8
    var animDelay : Int = 100

    private lateinit var dotsArray: Array<CircleView?>

    private var isDotsExpanded: Boolean = false

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(attrs)
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(attrs)
        initView()
    }

    override fun initAttributes(attrs: AttributeSet) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val calHeight = 2 * dotsRadius
        val calWidth = (2 * noOfDots * dotsRadius + (noOfDots - 1) * dotsDist)

        setMeasuredDimension(calWidth, calHeight)
    }

    override fun initView() {
        removeAllViews()
        removeAllViewsInLayout()
        setVerticalGravity(Gravity.BOTTOM)

        dotsArray = arrayOfNulls<CircleView?>(noOfDots)
        for (iCount in 0 until noOfDots) {
            val circle = CircleView(context, dotsRadius, dotsColor)

            var params = LinearLayout.LayoutParams(2 * dotsRadius, 2 * dotsRadius)

            if (iCount != noOfDots - 1) {
                params.rightMargin = dotsDist
            }

            addView(circle, params)
            dotsArray[iCount] = circle
        }

        val loaderView = this

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                startLoading()

                val vto = loaderView.viewTreeObserver
                vto.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun startLoading() {
        if (!isDotsExpanded) {
            for (iCount in 0 until noOfDots) {
                var anim = getExpandScaleAnimation(iCount)
                dotsArray[iCount]!!.startAnimation(anim)

                if (iCount == noOfDots - 1){
                    anim.setAnimationListener(object : Animation.AnimationListener{
                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                        override fun onAnimationEnd(p0: Animation?) {
                            startLoading()
                        }

                        override fun onAnimationStart(p0: Animation?) {
                        }

                    })
                } else {
                    anim.setAnimationListener(object : Animation.AnimationListener{
                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                        override fun onAnimationEnd(p0: Animation?) {
                            dotsArray[iCount]!!.visibility = View.VISIBLE
                        }

                        override fun onAnimationStart(p0: Animation?) {
                        }

                    })
                }

            }
            isDotsExpanded = true
        } else {
            for (iCount in 0 until noOfDots) {
                var anim = getCollapseAnimation(iCount)
                dotsArray[iCount]!!.startAnimation(anim)

                if (iCount == noOfDots - 1){
                    anim.setAnimationListener(object : Animation.AnimationListener{
                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                        override fun onAnimationEnd(p0: Animation?) {
                            startLoading()
                        }

                        override fun onAnimationStart(p0: Animation?) {
                        }

                    })
                } else {
                    anim.setAnimationListener(object : Animation.AnimationListener{
                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                        override fun onAnimationEnd(p0: Animation?) {
                            dotsArray[iCount]!!.visibility = View.INVISIBLE
                        }

                        override fun onAnimationStart(p0: Animation?) {
                        }

                    })
                }
            }
            isDotsExpanded = false
        }
    }

    private fun getExpandScaleAnimation(delay: Int): AnimationSet {
        var anim = AnimationSet(true);

        val scaleAnim = ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnim.duration = animDuration.toLong()
        scaleAnim.fillAfter = true
        scaleAnim.repeatCount = 0
        scaleAnim.startOffset = (animDelay * delay).toLong()
        anim.addAnimation(scaleAnim)

        anim.interpolator = interpolator
        return anim
    }

    private fun getCollapseAnimation(delay: Int): AnimationSet {
        var anim = AnimationSet(true);

        val scaleAnim = ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnim.duration = animDuration.toLong()
        scaleAnim.fillAfter = true
        scaleAnim.repeatCount = 0
        scaleAnim.startOffset = (animDelay * delay).toLong()
        anim.addAnimation(scaleAnim)

        anim.interpolator = interpolator
        return anim
    }

}