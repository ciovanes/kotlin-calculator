package com.example.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    private fun appendToOperation(op: TextView, value: String) {
        op.text = op.text.toString() + value
    }

    @SuppressLint("SetTextI18n")
    private fun replaceCharacter(op: TextView, c : Char) {
        op.text = op.text.toString().dropLast(1) + c
    }

    private fun findSymbol(op: String): Char {
        return op.find { it in "+-x÷%" } as Char
    }

    private fun symbolCounter(op: String): Boolean {
        return op.find { it in "+-x÷%"  } != null
    }

    private fun formatNumber(number: Double): Number {
        return when {
            (number % 1.0 == 0.0) -> number.toInt()
            else -> number
        }
    }

    private fun calculateExpr(op: String): String {
        val symbol = findSymbol(op)
        val num = symbol.let { op.split(it).map { it.toDouble() } }

        val n1 = num[0]
        val n2 = num[1]

        when (symbol) {
            '+' -> return (formatNumber(n1.plus(n2))).toString()
            '-' -> return (formatNumber(n1.minus(n2))).toString()
            'x' -> return (formatNumber(n1 * n2)).toString()
            '÷' -> return (formatNumber(n1 / n2)).toString()
            '%' -> return (formatNumber(n1 % n2)).toString()
        }
        return ("Error")
    }

    private fun operationButtonClick(op: TextView, value: String) {
        val expr = op.text.toString()
        if (expr.isNotEmpty() && expr.last().isDigit()) {
            if (symbolCounter(expr)) {
                op.text = calculateExpr(expr)
                appendToOperation(op, value)
            }
            else {
                appendToOperation(op, value)
            }
        }
        else {
            replaceCharacter(op, value[0])
        }

    }

    private fun setupButtonListeners(op: TextView) {
        // Numpad
        val numpad = listOf(
            R.id.zero to "0",
            R.id.one to "1",
            R.id.two to "2",
            R.id.three to "3",
            R.id.four to "4",
            R.id.five to "5",
            R.id.six to "6",
            R.id.seven to "7",
            R.id.eight to "8",
            R.id.nine to "9"
        )

        numpad.forEach { (id, value) ->
            findViewById<MaterialButton>(id).setOnClickListener {
                appendToOperation(op, value)
            }
        }

        // Op characters
        val operators = listOf(
            R.id.plus to "+",
            R.id.minus to "-",
            R.id.multi to "x",
            R.id.div to "÷",
            R.id.mod to "%"
        )

        operators.forEach { (id, value) ->
            findViewById<MaterialButton>(id).setOnClickListener {
                operationButtonClick(op, value)
            }
        }

        // Special buttons
        findViewById<MaterialButton>(R.id.dot).setOnClickListener {
            appendToOperation(op, ".")
        }

        findViewById<MaterialButton>(R.id.equals).setOnClickListener {
            val expr = op.text.toString()
            if (expr.isNotEmpty() && expr.last().isDigit()) {
                op.text = calculateExpr(expr)
            }
        }

        findViewById<MaterialButton>(R.id.delete).setOnClickListener {
            op.text = op.text.dropLast(1)
        }

        findViewById<MaterialButton>(R.id.clear).setOnClickListener {
            op.text = ""
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val op = findViewById<TextView>(R.id.operations)
        setupButtonListeners(op)
    }
}