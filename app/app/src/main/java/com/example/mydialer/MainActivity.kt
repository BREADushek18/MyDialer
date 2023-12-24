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

        plant(Timber.DebugTree())

        var ContactList: Array<Contact> = arrayOf()
        val gson = Gson()

        val pathName = Environment.getExternalStorageDirectory()
            .toString() + "/" + Environment.DIRECTORY_DOWNLOADS + "/phones.json"

        val bufferedReader: BufferedReader = File(pathName).bufferedReader()
        val inputString = bufferedReader.use { it.readText() }

        ContactList = gson.fromJson(inputString, ContactList::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(this, ContactList)

        val button: Button = findViewById(R.id.btn_search)
        button.setOnClickListener{
            val editText: EditText = findViewById(R.id.et_search)
            val srcText: String = editText.text.toString()

            if (srcText == "" || srcText == " "){
                recyclerView.adapter = Adapter(this, ContactList)
            } else {
                var SortedList: Array<Contact> = arrayOf()
                for(i in 0..ContactList.count() - 1){
                    if(ContactList[i].name.contains(srcText) || ContactList[i].type.contains(srcText) || ContactList[i].phone.contains(srcText)){
                        SortedList += ContactList[i]
                    }
                }
                recyclerView.adapter = Adapter(this, SortedList)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.name.text = data.name
        holder.phone.text = data.phone
        holder.type.text = data.type
    }
}