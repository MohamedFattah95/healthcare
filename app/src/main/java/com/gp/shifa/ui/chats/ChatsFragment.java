package com.gp.shifa.ui.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gp.shifa.data.models.ChatRoomModel;
import com.gp.shifa.data.models.ChatsModel;
import com.gp.shifa.databinding.FragmentChatsBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.chat.ChatActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ChatsFragment extends BaseFragment<ChatsViewModel> implements ChatsNavigator, ChatsAdapter.Callback {

    @Inject
    LinearLayoutManager chatsLayoutManager;
    @Inject
    ChatsAdapter chatsAdapter;

    FragmentChatsBinding binding;

    private DatabaseReference rootRef;

    private List<ChatRoomModel> originalRoomsList;

    public static ChatsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        ChatsFragment fragment = new ChatsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {

        if (mViewModel != null && mViewModel.getDataManager().isUserLogged()) {
            binding.swipeRefreshView.setRefreshing(true);
            chatsAdapter.clearItems();
            mViewModel.getUserChats();
        }

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        chatsAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        rootRef = FirebaseDatabase.getInstance().getReference();

        originalRoomsList = new ArrayList<>();

        subscribeViewModel();
        setUpChatsAdapter();

        binding.swipeRefreshView.setOnRefreshListener(() -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                binding.swipeRefreshView.setRefreshing(true);
                chatsAdapter.clearItems();
                mViewModel.getUserChats();
            } else {
                binding.swipeRefreshView.setRefreshing(false);
            }
        });



    }

    private void setUpChatsAdapter() {
        binding.rvChats.setLayoutManager(chatsLayoutManager);
        binding.rvChats.setAdapter(chatsAdapter);
    }

    private void subscribeViewModel() {

        int currentUserId = mViewModel.getDataManager().getCurrentUserId();
        mViewModel.getUserChatsLiveData().observe(requireActivity(), response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);

            for (ChatsModel chat : response.getData()) {

                ChatRoomModel chatRoomModel = new ChatRoomModel();

                chatRoomModel.setRoomId(chat.getRoomId());

                if (chat.getUserId() == currentUserId) {
                    chatRoomModel.setUserId(chat.getMedicalId());
                } else {
                    chatRoomModel.setUserId(chat.getUserId());
                }

                chatsAdapter.addItem(chatRoomModel);

//                rootRef.child("Chat").child(chatRoomModel.getRoomId()).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        try {
//                            if (dataSnapshot.exists()) {
//
//                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    MessageModel message = snapshot.getValue(MessageModel.class);
//
//                                    chatRoomModel.setMessage(message.getMessage());
//                                    chatRoomModel.setMessageDate(message.getTime());
//                                    boolean isSeen = message.isSeen();
//                                    chatRoomModel.setIsSeen(isSeen);
//                                    chatRoomModel.setLastMsgByYou(message.getSenderId() == currentUserId);
//
//                                    chatsAdapter.addItem(chatRoomModel);
//
//                                    originalRoomsList.add(chatRoomModel); //for search only
//                                }
//
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            }
        });

    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        ErrorHandlingUtils.handleErrors(throwable);
        binding.swipeRefreshView.setRefreshing(false);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        showErrorMessage(message);
        binding.swipeRefreshView.setRefreshing(false);
    }

    @Override
    public void openChat(ChatRoomModel roomModel) {
        startActivity(ChatActivity.newIntent(requireActivity())
                .putExtra("receiver_id", roomModel.getUserId()));
    }

    @Override
    public void hideChatsLoading() {
        binding.swipeRefreshView.setRefreshing(false);
        hideLoading();
    }
}
