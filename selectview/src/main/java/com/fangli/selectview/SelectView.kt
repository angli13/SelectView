package com.fangli.selectview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.*
import androidx.core.graphics.drawable.DrawableCompat


class SelectView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    //horizontal = 1 ; vertical = 0
    var mOrientation:Int = 1
    var mSelectedColor: Int = -1
    var mSelectedTextColor: Int = Color.BLACK
    var mNotSelectedColor: Int = Color.GRAY
    var mOptions: List<String> = listOf()
    var mFontSize = 14
    var mSelectedDrawable = R.drawable.select_view_border
    var mScaleType = -1
    var listener: SelectViewListener? = null
    var selectedIndex = -1
    var highlightView: ImageView
    var container: LinearLayout
    init {
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.SelectView,
                0, 0).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            try {
                mOrientation = getInteger(R.styleable.SelectView_orientation, mOrientation)
                mSelectedTextColor = getColor(R.styleable.SelectView_textSelectedColor, mSelectedTextColor)
                mSelectedColor = getColor(R.styleable.SelectView_selectedColor, mSelectedColor)
                mNotSelectedColor = getColor(R.styleable.SelectView_notSelectedColor, mNotSelectedColor)
                mFontSize = getDimensionPixelSize(R.styleable.SelectView_fontSize, mFontSize)
                mSelectedDrawable = getResourceId(R.styleable.SelectView_selectedDrawable, mSelectedDrawable)
                mScaleType = getInteger(R.styleable.SelectView_selectedScaleType, mScaleType)
            } finally {
                recycle()
            }
        }
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.select_view, this, true)
        container = findViewById(R.id.container)
        highlightView = findViewById(R.id.highlight)
        container.orientation = if (mOrientation == 1) LinearLayout.HORIZONTAL else  LinearLayout.VERTICAL
//        highlightView.visibility = View.INVISIBLE
        val bg = getDrawable(context, mSelectedDrawable)
        bg?.let {
            DrawableCompat.wrap(it)
            if (mSelectedColor != -1)
                DrawableCompat.setTint(bg, mSelectedColor)
        }
        highlightView.setImageDrawable(bg)
        highlightView.scaleType = getScaleType()
        val mainbg = getDrawable(context, R.drawable.select_view_border)
        mainbg?.let {
            DrawableCompat.wrap(it)
            DrawableCompat.setTint(mainbg, mNotSelectedColor)
        }
        background = mainbg
        changeOptions(mOptions)
    }


    private fun getScaleType(): ImageView.ScaleType? {
        when (mScaleType){
            -1 -> return ImageView.ScaleType.FIT_XY
            0 -> return ImageView.ScaleType.CENTER_CROP
            1 -> return ImageView.ScaleType.CENTER_INSIDE
            2 -> return ImageView.ScaleType.FIT_CENTER
            2 -> return ImageView.ScaleType.FIT_XY
        }
        return ImageView.ScaleType.FIT_XY
    }

    fun changeOptions(options: List<String>) {
        mOptions = options
        container.removeAllViews()
        mOptions.forEach {
            addTextView(it)
        }
        getChildAt(0)?.let {
            it.post {
                highlightView.visibility = View.GONE
                setHighLightViewDimensions()
                setSelected(selectedIndex)
            }
        }
        invalidate()
    }

    private fun setHighLightViewDimensions() {
        if (container.childCount>0){
            highlightView.layoutParams = FrameLayout.LayoutParams(container.getChildAt(0).measuredWidth, container.getChildAt(0).measuredHeight)
        }
    }

    private fun addTextView(text: String) {
        val textView = TextView(context)
        textView.text = text
        val width = if (mOrientation == 1) 0 else LinearLayout.LayoutParams.MATCH_PARENT
        val height = if (mOrientation == 1) LinearLayout.LayoutParams.MATCH_PARENT else 0
        textView.layoutParams = LinearLayout.LayoutParams(width, height, 1f)
        textView.gravity = Gravity.CENTER
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mFontSize.toFloat())
        textView.tag = container.childCount
        textView.setOnClickListener {
            val pos = it.tag as Int
            setSelectedText(pos)
            listener?.OnOptionSelected(pos, mOptions[pos])
        }
        container.addView(textView)
    }

    private fun setSelectedText(position: Int) {
        highlightView.visibility = View.VISIBLE
        if (selectedIndex>=0) {
            highlightView.animate().setDuration(100).translationY(container.getChildAt(position).y)
                    .translationX(container.getChildAt(position).x).start()
        } else {
            highlightView.x = container.getChildAt(position).x
            highlightView.y = container.getChildAt(position).y
        }
        selectedIndex = position
        print("selected ${position}")
        for (i in 0 until container.childCount){
            val tv = container.getChildAt(i) as TextView
            tv.setTextColor(if(i==selectedIndex) mSelectedTextColor else mNotSelectedColor)
        }
        setHighLightViewDimensions()
    }

    fun getSelectedPosition(): Int {
        return selectedIndex
    }

    fun getSelectedText(): String? {
        return if (mOptions.isNotEmpty() && selectedIndex >= 0) mOptions[selectedIndex] else null
    }

    fun setSelected(position: Int){
        if (position < container.childCount && position >= 0){
            setSelectedText(position)
        }
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}