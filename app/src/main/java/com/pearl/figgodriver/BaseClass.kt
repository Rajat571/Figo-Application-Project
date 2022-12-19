package com.pearl.figgodriver

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import android.webkit.MimeTypeMap
import java.io.ByteArrayOutputStream


class BaseClass (var context: Context) {
    fun getExtension(uri: Uri?): String? {

        val mimeType: String? = uri?.let { context.getContentResolver().getType(it).toString() }
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }
    fun BitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
        val b: ByteArray = baos.toByteArray()
        Base64.encodeToString(b, Base64.DEFAULT)
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}