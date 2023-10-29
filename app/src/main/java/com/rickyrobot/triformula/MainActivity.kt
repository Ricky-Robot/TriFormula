package com.rickyrobot.triformula

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.rickyrobot.triformula.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner = binding.spinner

        ArrayAdapter.createFromResource(
            this,
            R.array.formulas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFormula = spinner.selectedItem.toString()
                val opcionesFormulas = resources.getStringArray(R.array.formulas)

                binding.textoSpinner.text = selectedFormula

                if (selectedFormula == opcionesFormulas[0]){
                    binding.imagenFormula.setImageResource(R.drawable.regladetres)
                }
                else if(selectedFormula == opcionesFormulas[1]){
                    binding.imagenFormula.setImageResource(R.drawable.interescompuesto)
                }
                else if(selectedFormula == opcionesFormulas[2]){
                    binding.imagenFormula.setImageResource(R.drawable.densidad)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.textoSpinner.text = resources.getString(R.string.item_default)
            }
        }

    }
}