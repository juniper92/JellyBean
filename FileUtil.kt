package com.myandroid.fileio

import java.io.*

class FileUtil {

    /*======================================= 텍스트 파일 읽기 =======================================*/
    // 1. fullPath 파라미터로 파일의 경로를 전달받는 메서드 생성, result 변수로 파일을 읽은 결과값 리턴
    fun readTextFile(fullPath: String): String {

        // 2. 전달된 fullPath 경로를 File로 생성하고 실제 파일 있는지 검사
        val file = File(fullPath)
        // 3. 없으면 공백값 리턴
        if (!file.exists()) return ""


        // 4. FileReader로 file 읽고 BufferedReader에 담아 속도 향상
        val reader = FileReader(file)
        val buffer = BufferedReader(reader)

        // 5. buffer를 통해 한 줄씩 읽은 내용을 임시로 저장할 temp 변수 선언
        var temp = ""
        // 6. 모든 내용을 저장할 StringBuffer을 result 변수로 선언
        val result = StringBuffer()


        /* 7. while문 반복하며 buffer에서 한 줄을 꺼내 temp 변수에 담고,
        그 값이 null이라면 반복문 빠져나간다. null이 아니라면 result 변수에 append */
        while (true) {
            temp = buffer.readLine()
            if (temp == null) break;
            else result.append(buffer)
        }


        // 8. buffer을 close()로 닫고 결괏값 리턴
        buffer.close()
        return result.toString()
    }

    /* 내부 저장소에서 파일 읽으려면, 내부 저장소인 filesDir과 파일명 조합한다.
    * 그리고 readTextFile()의 파라미터로 넘기면 된다.
    * 디렉터리와 파일명 사이를 슬래시(/)로 구분하거나, File.pathSeparator로 구분할 수 있다. */
    // var content = readTextFile("${filesDir}/파일명.txt")


    /* openFileInput을 사용해 코드 축약 가능*/
    //var contents = ""
    //context.openFileInput("파일경로").bufferedReader().useLines { lines ->
    //    contents = lines.joinToString("\n")
    //}


    /*======================================= 텍스트 파일 쓰기 =======================================*/
    // 쓰기 파일은 총 3개의 파라미터(파일 생성할 디렉터리, 파일명, 작성할 내용) 사용

    //1. 3개의 파라미터를 가진 메서드 생성
    fun writeTextFile(directory: String, filename: String, content: String) {

        // 2. 디렉터리 존재여부 검사 후 없으면 생성. 디렉터리도 파일처럼 File 객체에 경로 전달하면 상태 체크 가능
        val dir = File(directory)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        // 3. 디렉터리가 생성되면 디렉터리에 파일명을 합해 FileWriter로 생성
        val writer = FileWriter(directory + "/" + filename)
        // 4. 생성된 FileWriter를 buffer에 담으면 쓰기 속도 향상됨
        val buffer = BufferedWriter(writer)

        // 5. buffer로 내용을 쓰고 close()로 닫는다다
        buffer.write(content)
        buffer.close()
    }

    /* 내부 저장소에 텍스트 파일을 쓸 때는 다음과 같이 사용한다 */
    // writeTextFile(filesDir, "filename.txt", "글의 내용")


    /* openFileOutPut을 사용해 코드 축약 가능*/
    //var contents = "Hello\nworld!"
    //context.openFileOutput("파일명", Context.MODE_PRIVATE).use { stream ->
    //    stream.write(contents.toByteArray())
    //}
}

