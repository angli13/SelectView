package com.fangli.selectviewexample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fangli.selectview.SelectViewListener
import kotlinx.android.synthetic.main.activity_main.*

class TestActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectView.changeOptions(listOf("One", "Two", "Three"))
        selectView2.changeOptions(listOf("A", "B", "C"))
        selectView3.changeOptions(listOf("WOW", "YES", "NO", "MAYBE"))
        selectView.listener = object: SelectViewListener {
            override fun OnOptionSelected(position: Int, text: String) {
                selectView2.setSelected(position)
                selectView3.setSelected(position)
            }

        }
        selectView2.listener = object: SelectViewListener {
            override fun OnOptionSelected(position: Int, text: String) {
                selectView.setSelected(position)
                selectView3.setSelected(position)
            }

        }
        selectView3.listener = object: SelectViewListener {
            override fun OnOptionSelected(position: Int, text: String) {
                selectView.setSelected(position)
                selectView2.setSelected(position)
            }

        }
    }

    fun setSelected(view: View){
        selectView.setSelected(1)
        selectView2.setSelected(1)
        selectView3.setSelected(1)
    }

    fun getSelected(view: View){
        selectView.getSelectedPosition()
        Toast.makeText(this,
                "1: ${selectView.getSelectedText()}, 2: ${selectView2.getSelectedText()}, 3: ${selectView3.getSelectedText()}",
                Toast.LENGTH_SHORT).show()
    }
}
