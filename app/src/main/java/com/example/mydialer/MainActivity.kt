package com.example.mydialer

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import timber.log.Timber
import timber.log.Timber.Forest.plant
import java.io.BufferedReader
import java.io.File


data class Contact(
    val name: String,
    val phone: String,
    val type: String
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())
        var contactList: Array<Contact> = arrayOf()
        val gson = Gson()
        val pathName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/phones.json"
        val bufferedReader: BufferedReader = File(pathName).bufferedReader()
        val inputString = bufferedReader.use { it.readText() }
        val recyclerView: RecyclerView = findViewById(R.id.rView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(this, contactList)
        contactList = gson.fromJson(inputString, Array<Contact>::class.java)

        val button: Button = findViewById(R.id.btn_search)
        button.setOnClickListener{
            val editText: EditText = findViewById(R.id.et_search)
            val srcText: String = editText.text.toString()
            if (srcText == "" || srcText == " "){
                recyclerView.adapter = Adapter(this, contactList)
            } else {
                var sortedList: Array<Contact> = arrayOf()
                for(i in 0 until contactList.size){
                    if(contactList[i].name.contains(srcText) || contactList[i].type.contains(srcText) || contactList[i].phone.contains(srcText)){
                        sortedList = sortedList + contactList[i]
                    }
                }
                recyclerView.adapter = Adapter(this, sortedList)
            }
        }
    }
}

class Adapter(private val context: Context, private val list: Array<Contact>) : RecyclerView.Adapter<Adapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.textName)
        val phone: TextView = view.findViewById(R.id.textPhone)
        val type: TextView = view.findViewById(R.id.textType)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.name.text = data.name
        holder.phone.text = data.phone
        holder.type.text = data.type
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
