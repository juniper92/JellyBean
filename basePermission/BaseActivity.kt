package com.myandroid.baseactivity

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

// 권한 처리와 같은 반복적인 코드들은 BaseActivity를 하나 만들어 두고, 각액티비티에서 상속받아 사용하면 효율적.


/* 베이스 액티비티는 다른 액티비티에서 상속받아 사용되므로,
직접 실행되는 것을 방지하고 상속받은 액티비티(구현체)에서만 사용할 수 있게 만들어야 한다.
그래서 일반적으로 추상 클래스로 설계한다.
액티비티지만 추상 클래스이기에 Activity 메뉴가 아닌 Class 메뉴를 통해 생성.*/


// 1. 권한 처리를 위해 액티비티의 기본 기능 필요 - AppCompactActivity 상속받아 기본기능 사용 가능하도록 작성
abstract class BaseActivity : AppCompatActivity() {

    // 2. 2개의 추상 메서드 생성
    abstract fun permissionGranted(requestCode: Int)
    abstract fun permissionDenied(requestCode: Int)

    // 3. 자식 액티비티에서 권한요청 시 직접 호출하는 메서드 작성(파라미터로 권한 배열 & requestCode 전달받음)
    fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        // 4. 안드로이드 버전 체크 코드 작성
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        }
        // 5. 권한 체크 필요 버전이면 else 블록 내에서 권한이 모두 승인된 것을 확인.
        else {
            val isAllPermissionsGranted = permissions.all {
                checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }

            // 6. 조건문을 통해 isAllPermissionsGranted가 true면, permissionGranted() 메서드 호출
            // false면 사용자에 승인 권한 요청
            if (isAllPermissionsGranted) {
                permissionGranted(requestCode)
            } else {
                ActivityCompat.requestPermissions(this, permissions, requestCode)
            }
        }

    }

    // 7. 사용자가 권한을 승인 혹은 거부 후 호출되는 onRequestPermissionsResult 메서드 오버라이드
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // 8. 메서드 안에서 먼저 grantResult에 all 메서드 이용해 결괏값이 모두 승인됐는지 확인
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionGranted(requestCode)  // true일때
        } else {
            permissionDenied(requestCode)   // false일때
        }
    }
}
