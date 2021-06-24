package com.myandroid.diary_2

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import com.myandroid.diary_2.databinding.ActivityDiaryBinding

class DiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryBinding

    // 4-3. 핸들러 생성하기 (인자로 루퍼를 넣어줘야 하는데, getMainLooper을 넣어주면 이 핸들러는 메인쓰레드에 연결된 핸들러가 된다.)
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_diary)

        /* 2. EditText 코드로 연결시켜주자(onCreate 위쪽에서 lazy로 선언하는 방법도 있는데, 내부에서 선언해도 무방 )
        어차피 EditText는 onCreate 함수 밖으로 나갈 일이 없기 때문에 로컬변수로 선언.*/
//        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val diaryEditText = binding.diaryEditText

        // 1. 이번에는 detail이라는 이름으로 sharedPreferences 파일을 만들어보자! (만약 sharedPreferences를 같이 쓰고 싶다면 같은 이름으로 지정해도 무방)
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        // 3. setText라는 함수를 통해 detailPreference라는 값 중에 detail이라는 이름으로 저장한 값을 가져오도록 하고, default 밸류는 만약 저장된 값 없을 경우 빈 String을 넣어준다.
        diaryEditText.setText(detailPreferences.getString("detail", ""))



        // 4. diaryEditText가 글이 변경될 때마다, 글을 저장하는 기능을 만들어보자

        // 4-1. 이렇게 저장하면, 수정이 될 때마다 빈번하게 한글자씩 저장하게 되기 때문에, 다른 방법을 사용해보자.
//        diaryEditText.addTextChangedListener {
//            detailPreferences.edit {
//                putString("detail", diaryEditText.text.toString())    // it = 수정이 된 내용을 의미한다.
//            }
//        }

        // 5. 작성하다 잠깐 멈칫했을 때 저장할 수 있도록 만들어보기 - Thread 기능을 이용하는 방법 (thread에 넣게 되는 runnable 인터페이스를 구현해보자)
        val runnable = Runnable {
            // 쓰레드에서 일어나는 기능 구현
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                // 5-1. 이 기능은 수시로 백그라운드에서 변경내용을 저장하는 기능이기 때문에, commit을 통해서 thread를 블럭하지 않고, commit을 false로 주고 비동기로 넘기는 식으로 apply를 사용하자.
                putString("detail", diaryEditText.text.toString())
            }

            Log.d("DiaryActivity", "save!!!! ${diaryEditText.text.toString()}")
        }

         /* 4-2. 핸들러를 사용해보자 - 쓰레드를 열었을 때 UI에서 처리되는 작업을 UI쓰레드 혹은 메인쓰레드라고 하고, 새로운 쓰레드를 열었을 때 이건 UI쓰레드가 아님.
         새로운 쓰레드를 관리할 때 UI쓰레드와 생성한 쓰레드를 연결할 필요가 있는 경우가 있다. 메인 쓰레드가 아닌 곳에서는 ui change를 하는 동작을 수행할 수 없기 때문에.
         이렇게 메인 쓰레드와 연결해주는 기능을 핸들러에서 많이 사용할 수 있다. */
        diaryEditText.addTextChangedListener{

            Log.d("DiaryActivity", "TextChanged :: $it")
            // 6-1. 1초 이전에, 아직 실행되지 않고 펜딩되어있는 runnable이 있다면, 지워주기 위해 removeCallbacks라는 함수를 이용해 runnable을 넣어주자.
            handler.removeCallbacks(runnable)
            // 6. 핸들러의 기능 중에 많이 사용하는 함수는, post나 postdelay함수가 있는데, hostdelay함수를 이용해 일정 시간 이후에 쓰레드 실행시켜주는 기능을 만들어보자.
            handler.postDelayed(runnable, 1000) // 1초에 한 번씩 runnable 실행시키기
        }

    }
}