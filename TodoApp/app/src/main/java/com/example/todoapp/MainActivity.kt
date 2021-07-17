package com.example.todoapp

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    var list = arrayListOf<ToDoModel>()
    var adapter = TodoAdapter(list)

    val db by lazy {
        AppDataBase.getDatabase(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        binding.todoRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        binding.fabBtn2.setOnClickListener(){
            GlobalScope.launch(Dispatchers.IO){

                db.toDoDao().deleteTask()



            }
        }

        initSwipe()

        db.toDoDao().getTask().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                list.clear()
                list.addAll(it)
                Log.i("info", list.toString())
                adapter.notifyDataSetChanged()
            } else {
                list.clear()
                adapter.notifyDataSetChanged()
            }
        })





    }

    fun initSwipe() {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT) {
                    GlobalScope.launch(Dispatchers.IO) {
                        db.toDoDao().deleteGivenTask(adapter.getItemId(position))
                    }


                } else if (direction == ItemTouchHelper.RIGHT) {

                    GlobalScope.launch(Dispatchers.IO) {
                        db.toDoDao().deleteGivenTask(adapter.getItemId(position))
                    }

                    Toast.makeText(this@MainActivity,"Task is Done",Toast.LENGTH_LONG).show()

                }
            }

            override fun onChildDraw(
                    canvas: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView

                    val paint = Paint()
                    val icon: Bitmap

                    if (dX > 0) {

                        icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_check_white_png)

                        paint.color = Color.parseColor("#388E3C")

                        canvas.drawRect(
                                itemView.left.toFloat(), itemView.top.toFloat(),
                                itemView.left.toFloat() + dX, itemView.bottom.toFloat(), paint
                        )

                        canvas.drawBitmap(
                                icon,
                                itemView.left.toFloat(),
                                itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2,
                                paint
                        )


                    } else {
                        icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_delete_white_png)

                        paint.color = Color.parseColor("#D32F2F")

                        canvas.drawRect(
                                itemView.right.toFloat() + dX, itemView.top.toFloat(),
                                itemView.right.toFloat(), itemView.bottom.toFloat(), paint
                        )

                        canvas.drawBitmap(
                                icon,
                                itemView.right.toFloat() - icon.width,
                                itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2,
                                paint
                        )



                    }
                    viewHolder.itemView.translationX = dX


                } else {
                    super.onChildDraw(
                            canvas,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                    )
                }
            }


        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.todoRv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu?.findItem(R.id.Search)
        val searchView = item?.actionView as SearchView
        item.setOnActionExpandListener(object :MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                displayTodo()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                displayTodo()
                return true
            }

        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(!newText.isNullOrEmpty()){
                    displayTodo(newText)
                }
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun displayTodo(newText: String = "") {
        db.toDoDao().getTask().observe(this, Observer {
            if(it.isNotEmpty()){
                list.clear()
                list.addAll(
                        it.filter { todo ->
                            todo.Title.contains(newText,true)
                        }
                )
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun openNewTask(view: View) {
        startActivity(Intent(this, TaskActivity::class.java))
    }


}