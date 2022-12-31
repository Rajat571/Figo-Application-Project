package com.pearl.pearllib

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pearl.figgodriver.R
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern


abstract class BaseClass : AppCompatActivity() {
    protected var versionNew: String? = null
    protected var versionName: String? = null
  // var mIsUpdateAppTask: IsAppUpdated? = null
    protected var baseApcContext: Context? = null
    protected var activityIn: AppCompatActivity? = null
    protected var LogTag: String? = null
    protected var CAId: String? = null
    protected var LogString: String? = null
    var STORAGE_PERMISSION_CODE = 1
    var session: Session? = null
    var classname = "Login"
    fun setBaseApcContextParent(
        cnt: Context?,
        ain: AppCompatActivity?,
        lt: String?,
        classname: String?
    ) {
        var classname = classname
        baseApcContext = cnt
        activityIn = ain
        LogTag = lt
        classname = classname
        printLogs(lt, "setBaseApcContextParent", "weAreIn")
    }

    protected fun internetChangeBroadCast() {
        printLogs("Logs", "initializeViews", "init")
        registerBroadcast()
    }

    @get:SuppressLint("ObsoleteSdkInt")
    val cTheme: Unit
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = getWindow()
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = getResources().getColor(R.color.app_color)
            }
        }
/*
    @SuppressLint("ObsoleteSdkInt")
    fun getgreenTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = getResources().getColor(R.color.green)
        }
    }*/

    @SuppressLint("ObsoleteSdkInt")
    fun getwhiteTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = getResources().getColor(R.color.white)
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getActiveNetworkInfo() != null
    }

    fun printLogs(tag: String?, funcs: String, msg: String) {
        Log.i("OSG-" + tag + "__" + funcs, msg)
        LogString =
            LogString + "TAG - " + tag + "<br/> FUNCTION - " + funcs + "<br/> DATA - " + msg + "<br/><br/><br/><br/>"
    }

    var IChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        override fun onReceive(pContext: Context, pIntent: Intent) {
            val cm: ConnectivityManager =
                pContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val no_connection: View? = findViewById<View>(R.id.no_connection)
            // TextView try_again = findViewById(R.id.try_again);
            if (cm.getActiveNetwork() != null) {
                no_connection?.visibility = View.GONE
                printLogs(LogTag, "BroadcastReceiver", "func1$this")
            } else {
                no_connection?.visibility = View.VISIBLE
            }
        }
    }

    fun registerBroadcast() {
        try {
            printLogs(LogTag, "registerBroadcast", "init")
            val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
            registerReceiver(IChangeReceiver, filter)
            isInternetReceiver = true
            printLogs(LogTag, "registerBroadcast", "exit")
        } catch (e: Exception) {
            printLogs(LogTag, "registerBroadcast", "Exception " + e.message)
        }
    }

    fun unregisterBroadcast() {
        printLogs(LogTag, "unregisterBroadcast", "init")
        try {
            if (isInternetReceiver) {
                printLogs(LogTag, "unregisterBroadcast", "isInternetReceiver")
                isInternetReceiver = false
                unregisterReceiver(IChangeReceiver)
            }
        } catch (e: Exception) {
            printLogs(LogTag, "unregisterBroadcast", "Exception " + e.message)
        }
    }

   /* protected fun showProgress(show: Boolean) {
        val ll_main: View = findViewById<View>(R.id.ll_main)
        val loader: View = findViewById<View>(R.id.loader)
        if (show) {
            ll_main.visibility = View.GONE
            loader.visibility = View.VISIBLE
        } else {
            ll_main.visibility = View.VISIBLE
            loader.visibility = View.GONE
        }
    }*/

    /*fun syncUpdates(baseApcContext: Context?, activityIn: AppCompatActivity?) {
        var versionCode = 1
        try {
            val packageInfo: PackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0)
            versionName = packageInfo.versionName
            versionCode = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        versionNew = versionCode.toString()
        //        versionNew = versionName;
        printLogs(
            LogTag,
            "syncUpdates",
            "versionName $versionName VersionCode $versionCode NewVersion $versionNew"
        )
        mIsUpdateAppTask = IsAppUpdated(versionNew, baseApcContext)
        mIsUpdateAppTask.execute(null as Void?)
    }*/

    fun verifyVersion() {
        /* syncUpdates(baseApcContext, activityIn);
        printLogs(LogTag, "verifyVersion", "init");
        session = new Session(baseApcContext);
        Boolean isUpdate = session.getIsUpdateRequired();
        printLogs(LogTag, "verifyVersion", "isUpdate " + isUpdate);
        if (isUpdate) {
            Intent intent = new Intent(baseApcContext, AppUpdateA.class);
            startActivity(intent);
            finish();
        }*/
    }

    fun openFileExplorer() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Picture"
            ), STORAGE_PERMISSION_CODE
        )
    }

    protected fun setCustomError(msg: String?, mEditView: EditText) {
        mEditView.setError(msg, null)
        mEditView.setBackgroundResource(R.drawable.input_error_profile)
        mEditView.requestFocus()
    }

    protected fun setCustomErrorDisabled(mEditView: EditText) {
        mEditView.setError(null)
        mEditView.setBackgroundResource(R.drawable.input_boder_profile)
    }

    fun validateName(inputUser: EditText): Boolean {
        val name = inputUser.text.toString()
        System.out.println("NAMEE==="+name)
        setCustomError(null, inputUser)
        return if (name.isEmpty()) {
            val sMessage = "Please enter name..!!"
            setCustomError(sMessage, inputUser)
            false
        } else if (!isValidName(name)) {
            val sMessage = "Name must be at least 3 character and at most 50 character..!!"
            setCustomError(sMessage, inputUser)
            false
        } else {
            setCustomErrorDisabled(inputUser)
            true
        }

    }

    fun validateAddress1(inputUser: EditText): Boolean {
        val address: String = inputUser.getText().toString().trim { it <= ' ' }
        setCustomError(null, inputUser)
        return if (address.isEmpty()) {
            val sMessage = "Please enter Address..!!"
            setCustomError(sMessage, inputUser)
            false
        } else if (!isValidAddress10(address)) {
            val sMessage =
                "Address must be at least 10 character and should have House no / Flat no / Road no."
            setCustomError(sMessage, inputUser)
            false
        } else {
            setCustomErrorDisabled(inputUser)
            true
        }
    }

    fun validateAddress(inputUser: EditText): Boolean {
        val address: String = inputUser.getText().toString().trim { it <= ' ' }
        setCustomError(null, inputUser)
        return if (address.isEmpty()) {
            val sMessage = "Please enter Address..!!"
            setCustomError(sMessage, inputUser)
            false
        } else if (!isValidAddress(address)) {
            val sMessage = "Address must be at least 3 character"
            setCustomError(sMessage, inputUser)
            false
        } else {
            setCustomErrorDisabled(inputUser)
            true
        }
    }

    fun validateEmail(email: EditText): Boolean {
        val email_id: String = email.getText().toString().trim { it <= ' ' }
        setCustomError(null, email)
        return if (email_id.isEmpty()) {
            val sMessage = "Please enter email..!!"
            setCustomError(sMessage, email)
            false
        } else {
            setCustomErrorDisabled(email)
            true
        }
    }

    fun validateDob(dob: EditText): Boolean {
        val dob_id: String = dob.getText().toString().trim { it <= ' ' }
        setCustomError(null, dob)
        return if (dob_id.isEmpty()) {
            val sMessage = "Please enter valid dob..!!"
            setCustomError(sMessage, dob)
            false
        } else {
            setCustomErrorDisabled(dob)
            true
        }
    }
    fun validateState(state: EditText): Boolean {
        val dob_id: String = state.getText().toString().trim { it <= ' ' }
        setCustomError(null, state)
        return if (dob_id.isEmpty()) {
            val sMessage = "state is must required ..!!"
            setCustomError(sMessage, state)
            false
        } else {
            setCustomErrorDisabled(state)
            true
        }
    }
    fun validateCity(city: EditText): Boolean {
        val dob_id: String = city.getText().toString().trim { it <= ' ' }
        setCustomError(null, city)
        return if (dob_id.isEmpty()) {
            val sMessage = "city is must required ..!!"
            setCustomError(sMessage, city)
            false
        } else {
            setCustomErrorDisabled(city)
            true
        }
    }
    fun validateWorkState(workState: EditText): Boolean {
        val dob_id: String = workState.getText().toString().trim { it <= ' ' }
        setCustomError(null, workState)
        return if (dob_id.isEmpty()) {
            val sMessage = "first work state is must required ..!!"
            setCustomError(sMessage, workState)
            false
        } else {
            setCustomErrorDisabled(workState)
            true
        }
    }

    fun validateNumber(number: EditText): Boolean {
        val num: String = number.getText().toString().trim { it <= ' ' }
        setCustomError(null, number)
        return if (num.isEmpty()) {
            val sMessage = "Please enter number..!!"
            setCustomError(sMessage, number)
            false
        } else if (!isValidMobile(num)) {
            val sMessage = "Number must be 10 or less than 13 character and not be alphabet..!!"
            setCustomError(sMessage, number)
            false
        } else {
            setCustomErrorDisabled(number)
            true
        }
    }
    fun validatedriverDLNo(driverDLNo: EditText): Boolean {
        val num: String = driverDLNo.getText().toString().trim { it <= ' ' }
        setCustomError(null, driverDLNo)
        return if (num.isEmpty()) {
            val sMessage = "Please enter driver license no..!!"
            setCustomError(sMessage, driverDLNo)
            false
        } /*else if (!isValidNumber(num)) {
            val sMessage = "Number must be 15 letter or digits..!!"
            setCustomError(sMessage, driverDLNo)
            false
        } */else {
            setCustomErrorDisabled(driverDLNo)
            true
        }
    }
    fun validatedriverRegistrationNo(registrationNo: EditText): Boolean {
        val num: String = registrationNo.getText().toString().trim { it <= ' ' }
        setCustomError(null,registrationNo)
        return if (num.isEmpty()) {
            val sMessage = "Please fill the field..!!"
            setCustomError(sMessage, registrationNo)
            false
        } else if (!isValidRegisterationNo(num)) {
            val sMessage = "Number must be 8 letter or digits..!!"
            setCustomError(sMessage, registrationNo)
            false
        } else {
            setCustomErrorDisabled(registrationNo)
            true
        }
    }
    fun validateDriverInsuranceDate(insurance: EditText): Boolean {
        val dob_id: String = insurance.getText().toString().trim { it <= ' ' }
        setCustomError(null, insurance)
        return if (dob_id.isEmpty()) {
            val sMessage = "please enter valid date ..!!"
            setCustomError(sMessage, insurance)
            false
        } else {
            setCustomErrorDisabled(insurance)
            true
        }
    }

    fun getExtension(uri: Uri,context: Context): String {
        val mimeType: String = uri.let { context?.getContentResolver()!!.getType(uri).toString()
        }

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)!!
    }

    fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openFileExplorer()
            return
        }
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
        val b = baos.toByteArray()
        Base64.encodeToString(b, Base64.DEFAULT)
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
    open fun StringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte =
                Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: java.lang.Exception) {
            e.message
            null
        }
    }
    fun hideKeybaord(v: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }

    protected override fun onPause() {
        super.onPause()
    }

    protected override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    protected abstract fun setLayoutXml()
    protected abstract fun initializeViews()
    protected abstract fun initializeClickListners()
    protected abstract fun initializeInputs()
    protected abstract fun initializeLabels()

    companion object {
        var isInternetReceiver = false
        fun isValidRegisterationNo(number: String): Boolean {
            var result: Boolean
            result = true
            if (number.length <8) {
                result = false
            }
            if (number.length >13) {
                result = false
            }

            return result
        }
     fun isValidMobile(phone: String): Boolean {
            return if (!Pattern.matches("[a-zA-Z]+", phone)) {
                phone.length >= 9 && phone.length <= 13
            }
            else false
        }

        fun isValidName(name: String): Boolean {
            var result: Boolean
            result = true
            if (name.length < 3) {
                result = false
            }
            if (name.length > 50) {
                result = false
            }
            return result
        }

        fun isValidAddress10(name: String): Boolean {
            var result: Boolean
            result = true
            if (name.length < 10) {
                result = false
            }
            return result
        }

        fun isValidAddress(name: String): Boolean {
            var result: Boolean
            result = true
            if (name.length < 3) {
                result = false
            }
            return result
        }
    }
}