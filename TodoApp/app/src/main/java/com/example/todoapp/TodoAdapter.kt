package com.example.todoapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ItemTodoBinding
import java.text.SimpleDateFormat
import java.util.*



 val colours=  arrayOf(Color.BLUE,Color.RED,Color.MAGENTA,Color.YELLOW, Color.CYAN)

class TodoAdapter(val list: List<ToDoModel>): RecyclerView.Adapter<TodoAdapter.TodoviewHolder>() {




    class TodoviewHolder(val binding: ItemTodoBinding):RecyclerView.ViewHolder(binding.root) {


        fun bind(toDoModel: ToDoModel) {

            with(binding){



                val randomColour=(0 until colours.size).random()
                viewColorTag.setBackgroundColor(colours[randomColour])
                txtShowTitle.text=toDoModel.Title
                txtShowTask.text=toDoModel.description
                txtShowCategory.text=toDoModel.category
                val myformatTime = "h:mm a"
                val stf = SimpleDateFormat(myformatTime)
                txtShowTime.text = stf.format(Date(toDoModel.time))
                val myformatDate = "EEE, d MMM yyyy"
                val sdf = SimpleDateFormat(myformatDate)
                txtShowDate.text = sdf.format(Date(toDoModel.date))

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoviewHolder {
        val binding=ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoviewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoAdapter.TodoviewHolder, position: Int) {

        holder.bind(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun getItemId(position: Int): Long {
        return list[position].id
    }
}