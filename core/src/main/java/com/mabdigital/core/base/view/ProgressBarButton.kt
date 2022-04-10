package com.mabdigital.core.base.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.mabdigital.core.databinding.ProgressButtonBinding
import java.util.concurrent.atomic.AtomicBoolean

private const val START_LONG_PRESS_DELAY = 1500L
private const val LONG_PRESS_DELAY_REMAIN = 1500L

class ProgressBarButton @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0,
    detStyleRes: Int = 0
) : FrameLayout(context, attributes, defStyleAttr, detStyleRes) {

    init {
        setView()
    }

    private var anim: Animator? = null
    var listener: ButtonAction? = null

    private var _binding: ProgressButtonBinding? = null
    private val binding get() = _binding!!

    private var objectAnimator: ObjectAnimator? = null
    private var offerAccepted = AtomicBoolean(false)
    private var animeStart = AtomicBoolean(false)
    var onTap = Runnable {
        binding.progressBar.visibility = View.GONE
        setupCircleAnimate()
        animeStart.set(true)
        handlerView.postDelayed(onLongPress, LONG_PRESS_DELAY_REMAIN)
    }

    private var onLongPress = Runnable {
        offerAccepted.set(true)
        listener?.onLongClickDone()
        objectAnimator?.run {
            removeAllUpdateListeners()
            removeAllListeners()
        }
    }

    private var handlerView: Handler = Handler(Looper.getMainLooper())

    private fun resetUi() {
        handlerView.removeCallbacks(onLongPress)
        handlerView.removeCallbacks(onTap)
        anim?.cancel()
        binding.fillCircle.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        if (binding.progressBar.progress == 100) {
            listener?.acceptTimerDone()
        }
    }

    private fun setView() {
        if (_binding == null)
            _binding = ProgressButtonBinding.inflate(
                LayoutInflater.from(context),
                this, true
            )
        setupAnimate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(offerAccepted.get())
            return false
        event?.run {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!animeStart.get()) {
                        handlerView.postDelayed(
                            onTap,
                            ViewConfiguration.getTapTimeout().toLong()
                        )
                    } else { }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (!offerAccepted.get()) {
                        animeStart.set(false)
                        performClick()
                    } else { }
                }
                else -> resetUi()
            }
        }
        /*gestureDetector.onTouchEvent(event)
        gestureDetector.setIsLongpressEnabled(false)*/
        return true
    }

    override fun performClick(): Boolean {
        resetUi()
        super.performClick()
        return true
    }

    @SuppressLint("Recycle")
    private fun setupAnimate() {
        objectAnimator =
            ObjectAnimator.ofInt(binding.progressBar, "Progress", binding.progressBar.progress, 100)
                .setDuration(30000)
        objectAnimator?.addUpdateListener {
            val progress: Int = it.animatedValue as Int
            binding.progressBar.progress = progress
            if (binding.progressBar.progress == 100)
                listener?.run {
                    acceptTimerDone()
                }
        }
    }

    private fun setupCircleAnimate() {
        // get the center for the clipping circle
        with(binding.fillCircle) {
            val cx = binding.root.width / 2
            val cy = binding.root.height / 2

            // get the final radius for the clipping circle
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            // create the animator for this view (the start radius is zero)
            anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0f, finalRadius)
            // make the view visible and start the animation
            visibility = View.VISIBLE
            anim?.run {
                if(!isRunning)
                    start()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        objectAnimator?.start()
    }
}

interface ButtonAction {
    fun acceptTimerDone()
    fun onLongClickDone()
}