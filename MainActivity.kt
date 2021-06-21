package com.myandroid.secret_garden

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.myandroid.secret_garden.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    /* view를 lazy하게 초기화하는 이유 = MainActivity라는 클래스가 생성될 시점에는 뷰가 아직 그려지지 않았기 때문.
    * 뷰가 다 그려졌다고 알려주는 시점이 onCreate함수이기 때문이다. onCreate된 이후에 view에 대해 접근해야 한다. */
    private val numberPicker1: NumberPicker by lazy {
        binding.numberPicker1
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker2: NumberPicker by lazy {
        binding.numberPicker2
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker3: NumberPicker by lazy {
        binding.numberPicker3
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton: AppCompatButton by lazy {
        binding.openButton
    }

    private val changePasswordButton: AppCompatButton by lazy {
        binding.changePasswordButton
    }

    private var changePasswrodMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        numberPicker1
        numberPicker2
        numberPicker3


        /* 동작 정의시, openButton을 눌렀을 때 저장되어있는 pw의 값 가져와서, numberPicker 1,2,3에 설정되어 있는 value와 비교를 해야 한다.
        * 비교를 위해 기기에 저장되어있는 패스워드값을 가져올 필요가 있는데, 패스워드를 기기에 저장하는 방법은 2가지.
        * 1. 로컬db사용, 2. 파일에 직접 적는 방식, 2번 방법 중에서도 편하게 preference파일을 관리해줄 수 있도록 하는게 sharedpreference이고, 이것을 통해 저장되있는 값을 가져와보자.  */
        openButton.setOnClickListener {

            if (changePasswrodMode) {
                Toast.makeText(this, "비밀번호를 변경중이에요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // sharedpreference 사용방법, 인자로 name(-sharedpreference라는 파일의 이름) 과 mode를 받는다.
            // sharedpreference = 말 그대로 preference라는 파일을 다른 앱과 공유해서 사용할 수 있도록 하는 기능이다.
            val passwordPreferences = getSharedPreferences(
                "password",
                Context.MODE_PRIVATE
            )  // 우리는 password라는 파일을 다른 앱과 공유하지 않고 우리 앱에서만 사용하려 하기 때문에, MODE_PRIVATE으로 모드 정의함

            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}" // 넘버피커에서 설정한, 사용자가 입력한 패스워드 받아오기

            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                // 패스워드 성공 - 다음 페이지인 다이어리 페이지 오픈

                // startActivity()
                // TODO 다이어리 페이지 작성 후에 넘겨주어야 한다
            } else {
                // 실패 - 에러팝업 띄우기
                AlertDialog.Builder(this)
                    .setTitle("실패!!")
                    .setMessage("비밀번호가 잘못되었습니다.")
                    .setPositiveButton("확인") { _, _ -> }
                    .create()
                    .show()
            }
        }

        changePasswordButton.setOnClickListener {

            if (changePasswrodMode) {
                // 번호를 저장하는 기능
            } else {
                // changePasswordMode가 활성화 :: 비밀번호가 맞는지를 체크해야한다.

                val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

                val passwordFromUser =
                    "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}" // 넘버피커에서 설정한, 사용자가 입력한 패스워드 받아오기
            }

        }

    }
}