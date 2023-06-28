package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.charset.Charset
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.Adapters.SuperuserAdapter
import java.io.InputStreamReader

class LoginSQL(context: Context){
    private val databaseOpenHelper: DatabaseOpenHelper
    init {
        databaseOpenHelper = DatabaseOpenHelper(context.applicationContext)
    }
    private class DatabaseOpenHelper internal constructor(
        private val context: Context
    ) : SQLiteOpenHelper(
        context,
        context.getDatabasePath(DATABASE_NAME).toString(),
        null,
        DATABASE_VERSION
    ) {
        override fun onCreate(db: SQLiteDatabase) {

            val LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COL_USER_EMAIL TEXT, " +
                    "$COL_USER_NAME TEXT, " +
                    "$COL_USER_TYPE TEXT, " +
                    "$COL_USER_PASSWORD TEXT, " +
                    "$HR_EMAIL TEXT, " +
                    "$HR_NAME TEXT, " +
                    "$COMP_NAME TEXT, " +
                    "$ROLE TEXT, " +
                    "$REMARK TEXT)"

            // Execute the CREATE TABLE statement
            db.execSQL(LOGIN_TABLE)


        }

        override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
            onCreate(db)
        }
    }
    // This method is for adding data in our database
    fun addName(
        user_name : String,
        user_type : String,
        user_email : String,
        user_password : String,
        hr_email : String ="",
        hr_name : String="",
        company : String="",
        role : String="",
        remark : String=""  ){

        // below we are creating a content values variable
        val values = ContentValues()

        values.put(COL_USER_NAME, user_name)
        values.put(COL_USER_TYPE, user_type)
        values.put(COL_USER_EMAIL, user_email)
        values.put(COL_USER_PASSWORD, user_password)
        values.put(HR_EMAIL, hr_email)
        values.put(HR_NAME, hr_name)
        values.put(COMP_NAME, company)
        values.put(ROLE, role)
        values.put(REMARK, remark)

        // here we are creating a writable variable of our database as we want to
        // insert value in our database
        val db = databaseOpenHelper.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)

        // at last we are closing our database
        db.close()
    }

    fun getName(): Cursor? {

        // here we are creating a readable variable of our database
        // as we want to read value from it
        val db = databaseOpenHelper.writableDatabase
        // below code returns a cursor to read data from the database
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }

    fun checkEmail(userEmail: String): Boolean {
        val db = databaseOpenHelper.readableDatabase
        val selectionArg = arrayOf(userEmail)
        val selection: String = "${COL_USER_EMAIL}= ?"
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${TABLE_NAME} WHERE $selection", selectionArg)
        return cursor.count==0
    }
    fun checkLogin(email: String, password: String) : Boolean{
        val db = databaseOpenHelper.readableDatabase
        val selectionArg = arrayOf(email, password)
        val selection: String = "${COL_USER_EMAIL}= ?"
        val passSelection: String = "${COL_USER_PASSWORD}= ?"
        var cursor: Cursor = db.rawQuery("SELECT * FROM ${TABLE_NAME} WHERE $selection AND $passSelection",selectionArg)
        return cursor.count>0
    }

    fun getUserType(email: String): String? {
        val db = databaseOpenHelper.readableDatabase
        val selectionArgs = arrayOf(email)
        val selection = "$COL_USER_EMAIL = ?"
        val projection = arrayOf(COL_USER_TYPE)

        val cursor = db.query(
            TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var userType: String? = null
        if (cursor.moveToFirst()) {
            userType = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_TYPE))
        }

        cursor.close()
        return userType
    }

    fun deleteEntry(name: String, company: String, hr_email: String, hr_name: String, role: String) {
        val db = databaseOpenHelper.writableDatabase
        val selection = "$COL_USER_NAME = ? AND $COMP_NAME = ? AND $HR_EMAIL = ? AND $HR_NAME = ? AND $ROLE =?"
        val selectionArgs = arrayOf(name, company, hr_email, hr_name,role)

        db.delete(TABLE_NAME, selection, selectionArgs)
        db.close()
    }

    fun updateRemark(userEmail: String, remark: String): Boolean {
        val db = databaseOpenHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(REMARK, remark)

        val selection = "$COL_USER_EMAIL = ?"
        val selectionArgs = arrayOf(userEmail)

        val rowsAffected = db.update(TABLE_NAME, contentValues, selection, selectionArgs)
        db.close()

        return rowsAffected > 0
    }

    companion object{
        private const val DATABASE_NAME = "CDPC_LOGIN"
        private const val DATABASE_VERSION = 1

        val TABLE_NAME = "Login_Detail"
        const val COL_USER_NAME = "user_name"
        const val COL_USER_TYPE = "user_type"
        const val COL_USER_EMAIL = "user_email"
        const val COL_USER_PASSWORD = "user_password"
        const val HR_EMAIL = "hr_email"
        const val HR_NAME = "hr_name"
        const val COMP_NAME = "company"
        const val ROLE = "role"
        const val REMARK = "remark"
    }
}
