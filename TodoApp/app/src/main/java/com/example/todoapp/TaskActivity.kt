package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.databinding.ActivityTaskBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


const val DB_NAME="todo.db"
class TaskActivity : AppCompatActivity(), View.OnClickListener {

    val db by lazy{
        AppDataBase.getDatabase(this)
    }

    lateinit var myCalendar: Calendar
    lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
    private lateinit var binding: ActivityTaskBinding
    lateinit var timeSetListener:TimePickerDialog.OnTimeSetListener
    private val labels= arrayListOf<String>("Personal","Buissness","Insaurance","Shopping","Banking")
    var finalDate = 0L
    var finalTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.dateEdt.setOnClickListener(this)
        binding.timeEdt.setOnClickListener(this)
        binding.saveBtn.setOnClickListener(this)
        setUpSpinner()

    }

    private fun setUpSpinner() {
        val adapter=ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,labels)
        labels.sort()
        binding.spinnerCategory.adapter=adapter
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.dateEdt->{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    setListener()
                }
            }

            R.id.timeEdt->{
                setTimeListener()
            }

            R.id.saveBtn->{
                saveTodo()
            }



        }
    }

    private fun saveTodo() {

        val category = binding.spinnerCategory.selectedItem.toString()
        val title = binding.titleInplay.editText?.text.toString()
        val description = binding.taskinplay.editText?.text.toString()

       GlobalScope.launch(Dispatchers.IO) {
           db.toDoDao().insertTask(ToDoModel(title,description,category,finalDate,finalTime))
           finish()
       }

    }

    private fun setTimeListener() {

        myCalendar= Calendar.getInstance()
        timeSetListener=TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
            myCalendar.set(Calendar.MINUTE,minute)
            updateTime()
        }
        val timePickerDialog=TimePickerDialog(this,timeSetListener,myCalendar.get(Calendar.HOUR_OF_DAY)
        ,myCalendar.get(Calendar.MINUTE),false)


         timePickerDialog.show()


    }

    private fun updateTime() {
        val myformat="h:mm a"
        val stf=SimpleDateFormat(myformat)
        finalTime = myCalendar.time.time
        binding.timeEdt.setText(stf.format(myCalendar.time))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setListener() {
        myCalendar= Calendar.getInstance()
        dateSetListener=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updatedate()
        }
        val datePickerDialog=DatePickerDialog(this,dateSetListener,myCalendar.get(Calendar.YEAR)
        ,myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH))


        datePickerDialog.datePicker.minDate=System.currentTimeMillis()
        datePickerDialog.show()

    }

    private fun updatedate() {
        //Mon,5 jan,2020
        val myformat="EEE,d MMM,yyyy"
        val sdf=SimpleDateFormat(myformat)
        finalDate = myCalendar.time.time
        binding.dateEdt.setText(sdf.format(myCalendar.time))
        binding.timeInptLay.visibility=View.VISIBLE
    }


}