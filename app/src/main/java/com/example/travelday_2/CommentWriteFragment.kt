package com.example.travelday_2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.travelday_2.databinding.FragmentCommentWriteBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CommentWriteFragment : Fragment() {
    lateinit var binding:FragmentCommentWriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommentWriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
    }

    fun initLayout() {
        binding.writeButton.setOnClickListener {
            val commentText = binding.commentArea.text.toString()
            val time = getTime()

            // 이전 Fragment에서 전달 받은 post의 key 값을 가져오기
            val postKey = arguments?.getString("key")
            if (postKey == null) {
                Toast.makeText(context, "Invalid post", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 현재 로그인한 사용자의 아이디를 가져오기
            val user = FirebaseAuth.getInstance().currentUser
            val email = user?.email ?: "Anonymous"

            // Comment 객체를 생성
            val comment = Comment(email, commentText, time)

            // 새로운 댓글의 key 값을 생성
            val commentKey = DBRef.contentRef.child(postKey).child("comments").push().key.toString()

            // 생성한 key 값의 위치에 댓글 데이터를 저장
            DBRef.contentRef
                .child(postKey)
                .child("comments")
                .child(commentKey)
                .setValue(comment)

            Toast.makeText(context, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    //현재 시간 함수
    fun getTime(): String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat =
            SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)
        return dateFormat
    }

}