package com.rickyrobot.triformula

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.rickyrobot.triformula.databinding.ActivityMainBinding
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var esReglaTres = true
    private var esInteresCompuesto = false
    private var esDensidad = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#454D38") // Cambia el color aquí
        }

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

        val ingresarVar1 = binding.ingresaVar1
        val ingresarVar2 = binding.ingresaVar2
        val ingresarVar3 = binding.ingresaVar3
        val maxDigitsBeforeDecimal = 11
        val maxDigitsAfterDecimal = 2
        val inputFilter = DecimalDigitsInputFilter(maxDigitsBeforeDecimal, maxDigitsAfterDecimal)

        ingresarVar1.filters = arrayOf(inputFilter)
        ingresarVar2.filters = arrayOf(inputFilter)
        ingresarVar3.filters = arrayOf(inputFilter)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFormula = spinner.selectedItem.toString()
                val opcionesFormulas = resources.getStringArray(R.array.formulas)

                binding.textoSpinner.text = selectedFormula
                binding.etVar3.visibility = View.VISIBLE
                ingresarVar3.visibility = View.VISIBLE

                ingresarVar1.text = null
                ingresarVar2.text = null
                ingresarVar3.text = null

                binding.textoResultado.text = null

                if (selectedFormula == opcionesFormulas[0]){
                    binding.imagenFormula.setImageResource(R.drawable.regladetres)
                    esReglaTres = true
                    esInteresCompuesto = false
                    esDensidad = false

                    binding.etVar1.text = resources.getString(R.string.var_a)
                    binding.etVar2.text = resources.getString(R.string.var_b)
                    binding.etVar3.text = resources.getString(R.string.var_c)
                }
                else if(selectedFormula == opcionesFormulas[1]){
                    binding.imagenFormula.setImageResource(R.drawable.interescompuesto)
                    esReglaTres = false
                    esInteresCompuesto = true
                    esDensidad = false

                    binding.etVar1.text = resources.getString(R.string.var_Ci)
                    binding.etVar2.text = resources.getString(R.string.var_i)
                    binding.etVar3.text = resources.getString(R.string.var_n)
                }
                else if(selectedFormula == opcionesFormulas[2]){
                    binding.imagenFormula.setImageResource(R.drawable.densidad)
                    esReglaTres = false
                    esInteresCompuesto = false
                    esDensidad = true

                    binding.etVar1.text = resources.getString(R.string.var_m)
                    binding.etVar2.text = resources.getString(R.string.var_V)

                    binding.etVar3.visibility = View.INVISIBLE
                    ingresarVar3.visibility = View.INVISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.textoSpinner.text = resources.getString(R.string.item_default)
            }
        }

        val botonCalcular = binding.botonCalcular

        botonCalcular.setOnClickListener{
            var textoResultado = resources.getString(R.string.Resultado) + "\n"

            if(esDensidad){
                ingresarVar3.text = ingresarVar2.text
            }

            if(ingresarVar1.text.toString() == ""
                || ingresarVar2.text.toString() == ""
                || ingresarVar3.text.toString() == "" ){

                val mensajeError = resources.getString(R.string.error_null)
                Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show()
            }
            else if(esReglaTres){
                if(ingresarVar1.text.toString().toDouble() == 0.0){
                    val mensajeError = resources.getString(R.string.error_divCero)
                    Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show()
                }
                else {
                    textoResultado += "%.2f".format(calcularReglaTres())
                    binding.textoResultado.text = textoResultado
                }
            }
            else if(esInteresCompuesto){
                if(ingresarVar1.text.toString().toDouble() < 0.0){
                    val mensajeError = resources.getString(R.string.error_capitalCeroNegativo)
                    Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show()
                }
                else if(ingresarVar2.text.toString().toDouble() <= 0.0){
                    val mensajeError = resources.getString(R.string.error_tasaCeroNegativa)
                    Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show()
                }
                else if(ingresarVar3.text.toString().toDouble() < 0.0){
                    val mensajeError = resources.getString(R.string.error_periodoNegativo)
                    Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show()
                }
                else{
                    textoResultado += "%.2f".format(calcularInteresCompuesto())
                    binding.textoResultado.text = textoResultado
                }
            }
            else if(esDensidad){
                if(ingresarVar1.text.toString().toDouble() <= 0.0){
                    val mensajeError = resources.getString(R.string.error_masaCeroNegativa)
                    Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show()
                }
                else if(ingresarVar2.text.toString().toDouble() <= 0.0){
                    val mensajeError = resources.getString(R.string.error_volumenCeroNegativo)
                    Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show()
                }
                else{
                    textoResultado += "%.2f".format(calcularDensidad())
                    binding.textoResultado.text = textoResultado
                }
            }
        }

        val botonLimpiar = binding.botonLimpiar

        botonLimpiar.setOnClickListener {
            ingresarVar1.text = null
            ingresarVar2.text = null
            ingresarVar3.text = null

            binding.textoResultado.text = ""
        }

    }

    private fun calcularReglaTres(): Double {
        val a = binding.ingresaVar1.text.toString().toDouble()
        val b = binding.ingresaVar2.text.toString().toDouble()
        val c = binding.ingresaVar3.text.toString().toDouble()

        return (b * c) / a
    }

    private fun calcularInteresCompuesto(): Double{
        val Ci = binding.ingresaVar1.text.toString().toDouble()
        val i = binding.ingresaVar2.text.toString().toDouble()
        val n = binding.ingresaVar3.text.toString().toDouble()

        return Ci * (1 + i).pow(n)
    }

    private fun calcularDensidad(): Double{
        val m = binding.ingresaVar1.text.toString().toDouble()
        val V = binding.ingresaVar2.text.toString().toDouble()

        return m/V
    }
}