package com.revoio.kinfly.core

import android.text.method.PasswordTransformationMethod
import android.view.View


class CustomPasswordTransformationMethod(private val replacementChar: Char) : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return CustomCharSequence(source, replacementChar)
    }

    private class CustomCharSequence(
        private val mSource: CharSequence,
        private val replacementChar: Char
    ) : CharSequence {
        override val length: Int
            get() = mSource.length

        override fun get(index: Int): Char {
            return replacementChar
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return mSource.subSequence(startIndex, endIndex) // No need to modify this
        }
    }
}
