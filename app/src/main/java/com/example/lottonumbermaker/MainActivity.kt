package com.example.lottonumbermaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import java.time.format.DateTimeFormatter
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    //레이아웃 파일 객체 연결
    private val resetBtn by lazy { findViewById<Button>(R.id.btn_reset) }
    private val addBtn by lazy { findViewById<Button>(R.id.btn_add) }
    private val randomBtn by lazy { findViewById<Button>(R.id.btn_random) }
    private val numpik by lazy { findViewById<NumberPicker>(R.id.np_num) }
    private val today by lazy { findViewById<TextView>(R.id.tv_today) }
    private val pikday by lazy { findViewById<TextView>(R.id.tv_picDay) }

    //하단 표시될 번호공 리스트
    private val ntvlist : List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1),
            findViewById(R.id.tv_num2),
            findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),
            findViewById(R.id.tv_num5),
            findViewById(R.id.tv_num6)
        )
    }

    private var didRandom = false //번호생성을 눌렀을때 true값이 될때까지 번호 생성
    private val pikNumber = hashSetOf<Int>() // 생성한 번호가 담기는 컬렉션

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //넘버피커 범위
        numpik.minValue = 1
        numpik.maxValue = 45

        today.text = LocalDate.now().toString()
        pikday.text = pikdayResult()


        initAdd()
        initReset()
        initRandom()


    }

    private fun pikdayResult(): String{
        val todayValue = LocalDate.now().dayOfWeek.value
        val plusDay = 6 - todayValue
        var pikday = ""
        if (plusDay < 0) {
            pikday = LocalDate.now().plusDays(6).toString()
        } else {
            pikday = LocalDate.now().plusDays(plusDay.toLong()).toString()
        }

        return pikday
    }

    // 번호 추가 버튼 클릭시
    private fun initAdd() {
        addBtn.setOnClickListener {
            when {
                didRandom -> showToast("초기화 버튼을 눌러주세요.")
                pikNumber.contains(numpik.value) -> showToast("이미 선택한 숫자에요.")
                pikNumber.size > 4 -> showToast("숫자는 최대 5개까지 선택할 수 있어요.")
                else -> {
                    val textView = ntvlist[pikNumber.size]
                    textView.isVisible = true
                    textView.text = numpik.value.toString()

                    setNumBack(numpik.value, textView)
                    pikNumber.add(numpik.value)
                }
            }
        }
    }

    // 초기화 버튼 클릭시
    private fun initReset() {
        resetBtn.setOnClickListener{
            pikNumber.clear()
            ntvlist.forEach{it.isVisible = false}
            didRandom = false
            numpik.value = 1
        }
    }

    //랜덤생성 버튼 클릭시
    private fun initRandom () {
        randomBtn.setOnClickListener{
            val list = getRandom()
            
            didRandom = true
            
            list.forEachIndexed { index, number ->
                val textView = ntvlist[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView)
            }
        }
    }


    private fun getRandom (): List<Int> {
        val numbers = (1..45).filter { it !in pikNumber }
        return (pikNumber + numbers.shuffled().take(6-pikNumber.size)).sorted()
    }


    // 생성한 번호 텍스트뷰 뒤쪽 배경 설정 함수
    private fun setNumBack(num: Int, text: TextView) {
        val background = when (num) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green
        }

        text.background = ContextCompat.getDrawable(this, background)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


}