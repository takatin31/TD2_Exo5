package com.example.exo5

import android.content.ContentResolver
import android.content.ContentUris
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSend.setOnClickListener{


            var smsObj = SmsManager.getDefault()

            val resolver: ContentResolver = contentResolver;
            val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                null)

            if (cursor!!.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                    if (phoneNumber > 0) {
                        val cursorPhone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                        if(cursorPhone!!.count > 0) {
                            while (cursorPhone.moveToNext()) {
                                val phoneNumValue = cursorPhone.getString(
                                    cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                smsObj.sendTextMessage(phoneNumValue,null,"buy this product", null, null)
                            }
                        }
                        cursorPhone.close()
                    }
                }
            } else {
                // toast("No contacts available!")
            }
            cursor.close()

        }
    }

}
