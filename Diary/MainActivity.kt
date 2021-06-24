package com.myandroid.diary_2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit
import com.myandroid.diary_2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // 1. numberPicker 연결
    private val numberPicker1: NumberPicker by lazy {
        binding.numberPicker1

            // 2. 먼저 초기화되어야 할 것은 min&max value
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

    // 3. button 연결해주기
    private val openButton: AppCompatButton by lazy {
        binding.openButton
    }

    private val changePasswordButton: AppCompatButton by lazy {
        binding.changePasswordButton
    }

    // 9. 번호 변경시 예외처리를 위한 전역변수 선언
    private var changePasswordMode = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        numberPicker1
        numberPicker2
        numberPicker3

        // 4. openButton에 대해 동작 정의해주기
        openButton.setOnClickListener {

            // 9-1. 예외처리
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /* 5. openButton을 눌렀을 때, 저장되어있는 pw에 대한 값 가져와서 numberPicker에 설정되어 있는 값과 비교해줘야 한다.
            비교를 위해 기기에 저장되어있는 pw값 가져올 필요가 있는데, pw를 기기에 저장하는 방법은 로컬db를 사용하는 방식과 파일에 직접 적는 방식이 있다.
            파일에 직접 적는 방식 중에서도 편하게 preference 파일을 관리해줄 수 있도록 하는게 sharedpreference이고, 이를 통해 저장된 값을 가져와보자.*/
            val passwordPreferences = getSharedPreferences(
                "password",
                Context.MODE_PRIVATE
            ) // (인자값) name = 해당 파일의 이름 , mode = 원하는 mode(공개범위 등..)

            // 6. numberPicker에서 설정한 사용자가 입력한 패스워드값을 가져와야한다.
            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            /* 7. sharedpreference에 저장된 값과, 사용자가 입력한 값을 비교하자.
            sharedpreferences도 map처럼 key-value 형식으로 저장되게 되는데, 우리는 이 pw를 string형식으로 내보내고 내보낸 key를 pw라고 하도록 하자. */
            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                // 7-1. 값이 같으면 패스워드 성공 - 다음 페이지인 다이어리 페이지 오픈
                // 10. 마지막으로 startActivity 기능을 구현해보자 (DiaryActivity 클래스 완료된 시점임)
                 startActivity(Intent(this, DiaryActivity::class.java))    // intent = activity간에 데이터 넘겨준다.
            } else {
                // 7-2. 값이 다르면 패스워드 실패 - AlertDialog 띄워주기
                showErrorAlertDialog()
            }
        }

        // 8. 비밀번호 변경 기능 추가
        changePasswordButton.setOnClickListener {
            // changePassword라는 동작을 하는 동안에는, 다른 동작을 할 수 없게 예외처리를 해주어야 한다. pw변경하기를 누른 후 openButton을 누르면 내가 예상했던 결과와 다른 결과가 나타날 수 있기 때문에..
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            // 9-2. changePasswordMode일 경우 - 번호를 저장하는 기능
            if (changePasswordMode) {
                // 9-4. 변경모드일 때 한 번 더 눌러서 변경모드 끄는 기능

//                passwordPreferences.edit {
//                    val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"
//                    putString("password", passwordFromUser)
//
//                    /* sharedpreferences를 저장하는 기능에는 commit과 apply가 있다.
//                    commit은 파일이 다 저장될 때까지 UI를 멈추고 기다리는 기능 / apply는 비동기적으로 저장하는 방식 */
//                    // 9-4-1. 여기서 pw를 저장하는 건, 그렇게 긴 시간이 필요치 않기 때문에 제대로 저장이 될 때까지 블럭을 시키는 동작을 추가하자.
//                    // 9-4-2. 커밋에는 이렇게 바로 커밋함수를 호출하는 방법과,
//                    commit()
//                }

                // 9-4-3. 이렇게 바로 이 preferences endit은 true 라고 명시적으로 표시할 수 있다 + 람다함수.
                passwordPreferences.edit(true) {
                    putString("password", passwordFromUser)
                }

                // 9-4-4. 저장이 끝났기 때문에 changePasswordMode를 false로 바꿔주고, 백그라운드 컬러를 다시 검은색으로 바꿔준다.
                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)
            }


            // 9-3. changePasswordMode가 아닐 경우 - changePasswordMode가 활성화 :: 비밀번호가 맞는지 체크 - sharedpreferences에서 비밀번호를 가져와서 비교해봐야 한다.
            else {
                // 9-3-1. 패스워드 성공시 패스워드 변경모드로 들어가게
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()

                    // changePasswordMode 활성화 표시
                    changePasswordButton.setBackgroundColor(Color.RED)
                } else {
                    showErrorAlertDialog()
                }
            }

        }

    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 틀렸어요")

            /* 7-2-1. AlertDialog에는 positive버튼과 nagative버튼을 붙일 수 있다. 실패를 했지만 닫기버튼만 누를 것이므로 positive로 정의해주자.
            첫 번째 인자로는 button에 들어갈 text를 받는다.
            두번째로는 람다식을 받게 되는데, 그냥 버튼에 대해 람다를 작성할 때엔 view하나밖에 없어서 파라미터 인자가 생략이 가능했지만,
            positiveButton에 대해서는 onClickListener가 2개의 인자(1.Dialog  2.which)를 받는다 -> 인자를 생략하지 않고 명시적으로 사용한다.
            그런데 현재 dialog와 which가 사용되지 않기 때문에, 언더바(_)로 표시해 굳이 코드로 작성하지 않아도 되는 불필요한 코드를 제거한다.
            확인버튼을 눌렀을 때 Dialog가 바로 꺼지기 때문에, 딱히 동작은 정의하지 않고 넘어가도록 한다.
            */
            .setPositiveButton("확인") { _, _ -> }    // 비밀번호가 틀리면 alertdialog가 뜨고, 정의된 동작이 없기 때문에 이후에 아무 변화도 일어나지 않는다.
            .create()
            .show()
    }
}
