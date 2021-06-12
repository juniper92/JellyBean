package com.example.timerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.timerapp.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    // 1. 바인딩 생성 후 binding 변수에 담은 후
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    // 3. 전체 시간을 저장하는 total과 시작됨을 체크할 수 있는 started를 선언
    var total = 0           // 4. 처음 시작값으로 '0' 초를,
    var started = false     // 5. started는 시작되지 않았으므로 'false'를 선언

    // 6. total과 started를 이용해 화면에 시간값을 출력하는 Handler를 구현하고, handler 변수에 저장, 이제 핸들러로 메시지 전달되면,
    val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val minute = String.format("%02d", total/60)    // 7. total에 입력되어 있는 시간(초)를 60으로 나눈 값은 분 단위로,
            val second = String.format("%02d", total%60)    // 8. 60으로 나눈 나머지값은 초 단위로 사용해서 textTimer에 입력.
            binding.textTimer.text = "$minute:$second"
        }
    }

    // 9. onCreate() 메서드 안에서,
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 2. setContentView에 binding.root를 전달
        setContentView(binding.root)

        // 10. buttonStart에 클릭리스너를 달고 시작 코드 구현
        binding.buttonStart.setOnClickListener {
            // 11. 버튼이 클릭되면 먼저 started를 true로 변경 후 새로운 스레드 실행
            started = true

            // 12. 스레드는 while문의 started가 true인 동안 while문을 반복하며 1초에 한 번씩 total의 값 증가시키고 핸들러에 메시지 전송
            thread(start=true) {
                while (started) {
                    Thread.sleep(1000)
                    if(started) {
                        total += 1
                        handler?.sendEmptyMessage(0)    // 13. 핸들러를 호출하는 곳이 하나밖에 없으므로, 메시지에 0을 담아 호출
                    }
                }
            }
        }

        // 14. buttonStop에 클릭리스너를 달고 종료 코드 구현, 종료 코드에서는 started에 false, total에 0, 시간을 표시하는 텍스트뷰에는 00:00를 입력해 초기화한다.
        binding.buttonStop.setOnClickListener {
            if(started) {
                started = false
                total = 0
                binding.textTimer.text = "00:00"
            }
        }
    }
} 
