package com.example.mylotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.mylotto.databinding.ActivityMainBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // 1. 버튼 3개 연결 
    private val clearButton: Button by lazy {
        binding.clearButton
    }

    private val addButton: Button by lazy {
        binding.addButton
    }

    private val runButton: Button by lazy {
        binding.runButton
    }
    

    // 2. 넘버피커 연결
    private val numberPicker: NumberPicker by lazy {
        binding.numberPicker
    }

    // 12. xml의 visible 속성 적용 -> 바인딩
    private val numberTextViewList: List<TextView> by lazy {
        listOf(
            binding.pickedNumber1,
            binding.pickedNumber2,
            binding.pickedNumber3,
            binding.pickedNumber4,
            binding.pickedNumber5,
            binding.pickedNumber6
        )
    }

    // 10. 예외처리 변수
    private var didRun = false

    // 11. 번호 중복 방지 변수
    private val pickNumberSet = hashSetOf<Int>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 3. 넘버피커 범위 정해주기
        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }

    // 5. initRunButton() = 랜덤번호 생성
    private fun initRunButton() {
        runButton.setOnClickListener {
            
            // 6. getRandomNumber() = 번호 랜덤 추출
            val list = getRandomNumber()

            didRun = true

            // 22. forEachIndexed 함수를 이용해 index와 number값 둘 다 return. 
            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true

                // 25. 백그라운드 설정 함수 
                setNumberBackground(number, textView)
            }

//            Log.d("MainActivity", list.toString())
        }
    }

    // 9. initAddButton() = 번호 선택추가
    private fun initAddButton() {
        addButton.setOnClickListener {

            /*################################## 예외처리 ##################################*/
            
            if (didRun) {   // 이미 run이 되서, 번호 추가를 할 수 없는 상태
                Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개 까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            /*################################## 예외처리 끝 ##################################*/
            
           
            // 13. numberTextViewList에서 텍스트를 직접 꺼내오자 
            val textView = numberTextViewList[pickNumberSet.size]
            
            // 14. 텍스트 보이게 설정
            textView.isVisible = true
            
            // 15. string값 넣어서 텍스트뷰에 설정해준다
            textView.text = numberPicker.value.toString()

            // 24. 백그라운드 설정 함수 호출
            setNumberBackground(numberPicker.value, textView)

            // 16. pickNumberSet에 numberPicker의 value 추가해주기
            pickNumberSet.add(numberPicker.value)
        }
    }

    // 23. 텍스트뷰의 백그라운드 설정하는 함수
    private fun setNumberBackground(number:Int, textView: TextView) {
        when(number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }

    // 17. initClearButton() = 번호 초기화
    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            
            // 19. list 데이터들을 하나하나 꺼내주는 forEach 함수를 이용해, 추가했던 텍스트들을 화면에서 숨김
            numberTextViewList.forEach {
                it.isVisible = false
            }

            // 18. 클릭되었을 경우, didRun값 해제
            didRun = false
        }
    }

    // List = 순서를 가지고 있는 자료구조. 중복값 허용. 추가,삭제,교체 용이.
    private fun getRandomNumber() : List<Int> {
        // mytableListOf() = 요소 변경 가능 
        // listOf() = 요소 변경 불가능, 읽기 전용.
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    
                    // 20. pickNumberSet에서 이미 번호가 들어있는 경우, continue
                    if (pickNumberSet.contains(i)) {
                        continue
                    }

                    this.add(i)
                }
            }

        // 7. 요소들 셔플
        numberList.shuffle()

        // 8. 6개의 랜덤한 숫자 추출
        // 21. pickNumberSet에 있는 사이즈만큼을 제외하고 subList해준다. 앞쪽은 pickNumberSet의 toList()를 통해 transformation후에 뒤쪽을 더해주게 되면 새로운 리스트 탄생
        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)

        return newList.sorted()     // sorted 함수로 정렬
    }
}
