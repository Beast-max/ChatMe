package com.example.chatme.Fragment

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatme.*
import com.example.chatme.AppUtiles.AppUtiles
import com.example.chatme.databinding.ChatItemLayoutBinding
import com.example.chatme.databinding.FragmentChatBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class fragment_chat : Fragment() {
    private lateinit var binding:FragmentChatBinding
    private lateinit var appUtiles: AppUtiles
    private lateinit var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<ChatListModel,ViewHolder>

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        appUtiles = AppUtiles()
        readChat()

        return binding.root
    }

    private fun readChat(){
        val query = FirebaseDatabase.getInstance().getReference("ChatList").child(appUtiles.getUID()!!)

        val firebaseRecyclerOption = FirebaseRecyclerOptions.Builder<ChatListModel>()
                .setLifecycleOwner(this)
                .setQuery(query, ChatListModel::class.java)
                .build()

        firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<ChatListModel,ViewHolder>(firebaseRecyclerOption){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val chatItemLayoutBinding :ChatItemLayoutBinding = DataBindingUtil
                        .inflate(LayoutInflater.from(parent.context),R.layout.chat_item_layout,parent,false)
                return ViewHolder(chatItemLayoutBinding)
            }

            override fun onBindViewHolder(
                    holder: ViewHolder,
                    p1: Int,
                    chatListModel: ChatListModel
            ) {
                val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                        .child(chatListModel.member)
                databaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val userModel = snapshot.getValue(UserModel::class.java)
                            val date = appUtiles.getTimeAgo(chatListModel.date.toLong())

                            val chatmodel = ChatModel(
                                    chatListModel.chatId,
                                    userModel?.name,
                                    chatListModel.lastMessage,
                                    userModel?.image,
                                    date,
                                    userModel?.online
                            )

                            holder.chatItemLayoutBinding.chatModel = chatmodel
                            holder.itemView.setOnClickListener {
                                val intent = Intent(context, MessageActivity::class.java)
                                intent.putExtra("hisid", userModel?.uid)
                                intent.putExtra("hisimage", userModel?.image)
                                intent.putExtra("chatid", chatListModel.chatId)
                                startActivity(intent)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewChat.setHasFixedSize(false)
        binding.recyclerViewChat.adapter = firebaseRecyclerAdapter


    }
    class ViewHolder(val chatItemLayoutBinding: ChatItemLayoutBinding):RecyclerView.ViewHolder(chatItemLayoutBinding.root)

    override fun onResume() {
        super.onResume()
        firebaseRecyclerAdapter.startListening()
    }
    override fun onPause() {
        super.onPause()
        firebaseRecyclerAdapter.stopListening()
    }
}


