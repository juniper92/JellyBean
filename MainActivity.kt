package com.myandroid.lotto

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.myandroid.lotto.databinding.ActivityMainBinding

// 바인딩 생성
class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener {
            checkPermission()
        }
    }

    // 권한 확인 메서드
    fun checkPermission() {

        // 카메라 권한 승인 상태 확인 후, 결과값을 변수에 저장장
       val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        // import시 꼭 android로 선택하기!

        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            startProcess() // 1. 승인이면 프로그램 진행
        } else {
            requestPermission() // 2. 미승인이면 권한 요청청
       }
    }


    fun startProcess() {
        Toast.makeText(this, "카메라를 실행합니다.", Toast.LENGTH_LONG).show()
    }

    fun requestPermission() {
        // 2번째 파라미터는, 권한이 복수 개일 때를 대비해 배열로 입력
        // 3번째 파라미터는 리퀘스트 코드, 권한 요청 주체가 어떤 것인지 구분하기 위해 코드를 숫자로 입력해서 사용.
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 99)
    }



    /*권한 승인 묻는 팝업창에 사용자가 DENY또는 ALLOW를 수락을 클릭하면,
    * 액티비티의 onRequestPermissionsResult() 메서드 호출됨. 메서드 안에서 승인 후 처리하면 된다. */
    override fun onRequestPermissionsResult(
        requestCode: Int,   // 요청 주체 확인 코드, requestPermission() 메서드의 3번째 파라미터로 전달
        permissions: Array<out String>, // 요청 권한 목록. requestPermission() 메서드의 2번째 파라미터로 전달
        grantResults: IntArray  // 권한 목록에 대한 승인/미승인 값. 권한 목록의 개수와 같은 수의 결괏값 전달됨.
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            99-> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startProcess()
                } else {
                    finish()
                }
            }
        }
    }

}