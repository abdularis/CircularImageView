package com.github.abdularis.civ

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.IntDef

/**
 * Created by abdularis on 22/03/18.
 */
class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CircleImageView(context, attrs) {
    @IntDef(SHOW_INITIAL, SHOW_IMAGE)
    annotation class State

    private val mTextPaint: Paint
    private val mTextBounds: Rect
    private val mBackgroundPaint: Paint
    private val mBackgroundBounds: RectF
    private var initial: String
    private var mText: String
    private var mShowState: Int
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateCircleDrawBounds(mBackgroundBounds)
    }

    override fun onDraw(canvas: Canvas) {
        if (mShowState == SHOW_INITIAL) {
            val textBottom = mBackgroundBounds.centerY() - mTextBounds.exactCenterY()
            canvas.drawOval(mBackgroundBounds, mBackgroundPaint)
            canvas.drawText(initial, mBackgroundBounds.centerX(), textBottom, mTextPaint)
            drawStroke(canvas)
            drawHighlight(canvas)
        } else {
            super.onDraw(canvas)
        }
    }

    var text: String?
        get() = mText
        set(text) {
            mText = text ?: ""
            initial = extractInitial(text)
            updateTextBounds()
            invalidate()
        }

    @get:State
    var state: Int
        get() = mShowState
        set(state) {
            if (state != SHOW_INITIAL && state != SHOW_IMAGE) {
                val msg =
                    "Illegal avatar state value: $state, use either SHOW_INITIAL or SHOW_IMAGE constant"
                throw IllegalArgumentException(msg)
            }
            mShowState = state
            invalidate()
        }

    @get:Dimension
    var textSize: Float
        get() = mTextPaint.textSize
        set(size) {
            mTextPaint.textSize = size
            updateTextBounds()
            invalidate()
        }

    @get:ColorInt
    var textColor: Int
        get() = mTextPaint.color
        set(color) {
            mTextPaint.color = color
            invalidate()
        }

    @get:ColorInt
    var avatarBackgroundColor: Int
        get() = mBackgroundPaint.color
        set(color) {
            mBackgroundPaint.color = color
            invalidate()
        }

    private fun extractInitial(letter: String?): String {
        return if (letter == null || letter.trim { it <= ' ' }.isEmpty()) "?" else letter[0].toString()
    }

    private fun updateTextBounds() {
        mTextPaint.getTextBounds(initial, 0, initial.length, mTextBounds)
    }

    companion object {
        const val SHOW_INITIAL = 1
        const val SHOW_IMAGE = 2
        private const val DEF_INITIAL = "A"
        private const val DEF_TEXT_SIZE = 90
        private const val DEF_BACKGROUND_COLOR = 0xE53935

        @State
        private val DEF_STATE = SHOW_INITIAL
    }

    init {
        var text = DEF_INITIAL
        var textColor = Color.WHITE
        var textSize = DEF_TEXT_SIZE
        var backgroundColor = DEF_BACKGROUND_COLOR
        var showState = DEF_STATE
        attrs?.let {
            val a =
                context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView, 0, 0)
            text = a.getString(R.styleable.AvatarImageView_text) ?: ""
            textColor = a.getColor(R.styleable.AvatarImageView_textColor, textColor)
            textSize = a.getDimensionPixelSize(R.styleable.AvatarImageView_textSize, textSize)
            backgroundColor =
                a.getColor(R.styleable.AvatarImageView_avatarBackgroundColor, backgroundColor)
            showState = a.getInt(R.styleable.AvatarImageView_view_state, showState)
            a.recycle()
        }
        mShowState = showState
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.color = textColor
        mTextPaint.textSize = textSize.toFloat()
        mTextBounds = Rect()
        mText = text
        initial = extractInitial(text)
        updateTextBounds()
        mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBackgroundPaint.color = backgroundColor
        mBackgroundPaint.style = Paint.Style.FILL
        mBackgroundBounds = RectF()
    }
}