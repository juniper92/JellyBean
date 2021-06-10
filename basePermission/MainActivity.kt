
package com.myandroid.baseactivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.myandroid.baseactivity.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : BaseActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // btnCamera에 클릭리스너 달고, BaseActivity에 구현된 requirePermissions() 호출
        binding.btnCamera.setOnClickListener {
            requirePermissions(arrayOf(Manifest.permission.CAMERA), 10)
        }
    }

    // onActivityResult() 오버라이드 후 카메라 촬영 처리 코드 작성
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("카메라", "촬영 성공")
            } else {
                Log.d("카메라", "촬영 실패")
            }
        }
    }

    // 카메라 호출 코드 작성
    override fun permissionGranted(requestCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 99)
    }

    // 권한거부 토스트메시지 출력 코드 작성
    override fun permissionDenied(requestCode: Int) {
        Toast.makeText(baseContext, "권한 거부됨", Toast.LENGTH_LONG).show()
    }
}