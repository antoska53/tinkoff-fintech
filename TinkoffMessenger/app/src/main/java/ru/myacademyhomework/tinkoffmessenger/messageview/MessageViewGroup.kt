package ru.myacademyhomework.tinkoffmessenger.messageview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import ru.myacademyhomework.tinkoffmessenger.R

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    init {
        inflate(context, R.layout.message_view_group_layout, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val imageView = getChildAt(0)
        val textViewNameMessage = getChildAt(1)
        val flexBox = getChildAt(2)

        var totalWidth = 0
        var totalHeight = 0

        measureChildWithMargins(
            imageView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )

        val marginLeft = (imageView.layoutParams as MarginLayoutParams).leftMargin
        val marginRight = (imageView.layoutParams as MarginLayoutParams).rightMargin
        totalWidth += imageView.measuredWidth + marginLeft + marginRight
        totalHeight = maxOf(totalHeight, imageView.measuredHeight)

        measureChildWithMargins(
            textViewNameMessage,
            widthMeasureSpec,
            imageView.measuredWidth,
            heightMeasureSpec,
            0
        )

        val textMarginLeft = (textViewNameMessage.layoutParams as MarginLayoutParams).leftMargin
        val textMarginRight = (textViewNameMessage.layoutParams as MarginLayoutParams).rightMargin
        totalWidth += textViewNameMessage.measuredWidth + textMarginLeft + textMarginRight
        totalHeight = maxOf(totalHeight, textViewNameMessage.measuredHeight)

        measureChildWithMargins(
            flexBox,
            widthMeasureSpec,
            imageView.measuredWidth,
            heightMeasureSpec,
            0
        )

        val flexBoxMarginLeft = (flexBox.layoutParams as MarginLayoutParams).leftMargin
        val flexBoxMarginRight = (flexBox.layoutParams as MarginLayoutParams).rightMargin
        if (flexBox.measuredWidth > textViewNameMessage.measuredWidth) {
            totalWidth += flexBox.measuredWidth - textViewNameMessage.measuredWidth
        }

        totalHeight += flexBox.measuredHeight

        val resultWidth = resolveSize(totalWidth + paddingRight + paddingLeft, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight + paddingTop + paddingBottom, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val imageView = getChildAt(0)
        val textViewNameMessage = getChildAt(1)
        val flexBox = getChildAt(2)

        imageView.layout(
            paddingLeft,
            paddingTop,
            imageView.measuredWidth + paddingLeft,
            imageView.measuredHeight + paddingTop
        )

        val marginRight = (imageView.layoutParams as MarginLayoutParams).rightMargin

        textViewNameMessage.layout(
            imageView.right + marginRight,
            paddingTop,
            imageView.right + marginRight + textViewNameMessage.measuredWidth,
            textViewNameMessage.measuredHeight + paddingTop
        )


        flexBox.layout(
            imageView.right + marginRight,
            textViewNameMessage.bottom,
            imageView.right + marginRight + flexBox.measuredWidth,
            textViewNameMessage.bottom + flexBox.measuredHeight + paddingTop
        )

    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

}