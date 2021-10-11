package ru.myacademyhomework.tinkoffmessenger.MessageView

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
//        val textViewName = getChildAt(1)
        val textViewNameMessage = getChildAt(1)
//        val textViewMessage = getChildAt(2)
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

//        measureChildWithMargins(
//            textViewMessage,
//            widthMeasureSpec,
//            imageView.measuredWidth,
//            heightMeasureSpec,
//            0
//        )
//
//        val textMessageMarginLeft = (textViewMessage.layoutParams as MarginLayoutParams).leftMargin
//        val textMessageMarginRight =
//            (textViewMessage.layoutParams as MarginLayoutParams).rightMargin
//
//        if (textViewMessage.measuredWidth > textViewName.measuredWidth) {
//            totalWidth += textViewMessage.measuredWidth + textMessageMarginLeft + textMessageMarginRight - textViewName.measuredWidth
//        }
//        //   totalWidth += textViewMessage.measuredWidth + textMessageMarginLeft + textMessageMarginRight
//        totalHeight =
//            maxOf(totalHeight, textViewMessage.measuredHeight + textViewName.measuredHeight)


        measureChildWithMargins(
            flexBox,
            widthMeasureSpec,
            imageView.measuredWidth,
            heightMeasureSpec,
            0
        )

        val flexBoxMarginLeft = (flexBox.layoutParams as MarginLayoutParams).leftMargin
        val flexBoxMarginRight = (flexBox.layoutParams as MarginLayoutParams).rightMargin
//        if (flexBox.measuredWidth > textViewName.measuredWidth && flexBox.measuredWidth > textViewMessage.measuredWidth) {
        if (flexBox.measuredWidth > textViewNameMessage.measuredWidth) {
            totalWidth += flexBox.measuredWidth - textViewNameMessage.measuredWidth
           // totalWidth += flexBox.measuredWidth + flexBoxMarginLeft + flexBoxMarginRight - (textViewMessage.measuredWidth + textMessageMarginLeft + textMessageMarginRight)
        }
//        totalHeight = maxOf(
//            totalHeight,
//            flexBox.measuredHeight + textViewMessage.measuredHeight + textViewName.measuredHeight
//        )

        totalHeight += flexBox.measuredHeight

        val resultWidth = resolveSize(totalWidth + paddingRight + paddingLeft, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight + paddingTop + paddingBottom, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val imageView = getChildAt(0)
//        val textViewName = getChildAt(1)
        val textViewNameMessage = getChildAt(1)

//        val textViewMessage = getChildAt(2)
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

//        textViewMessage.layout(
//            imageView.right + marginRight,
//            textViewName.measuredHeight + paddingTop,
//            imageView.right + marginRight + textViewMessage.measuredWidth,
//            textViewMessage.measuredHeight + textViewName.measuredHeight + paddingTop
//        )

        flexBox.layout(
            imageView.right + marginRight,
            textViewNameMessage.bottom,
            imageView.right + marginRight + flexBox.measuredWidth,
            textViewNameMessage.bottom + flexBox.measuredHeight
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