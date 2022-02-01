package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

public class ChatsModel {

    /**
     * sender_user_id : 310
     * receiver_user_id : 298
     * created_at : 2021-01-12T15:57:40.000000Z
     * chat_id : 298,310
     * sender_user : {"id":310,"name":"تاجر","image":"clients/310_mg_20210103_102317304.jpg__5ff17f0213164_.jpg"}
     * receiver_user : {"id":298,"name":"مشهور 1","image":"clients/298_3.jpg__5ff718322e8b9_.jpg"}
     */

    @SerializedName("sender_user_id")
    private int senderId;
    @SerializedName("receiver_user_id")
    private int receiverId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("chat_id")
    private String roomId;
    @SerializedName("sender_user")
    private SenderUserBean senderUser;
    @SerializedName("receiver_user")
    private ReceiverUserBean receiverUser;

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public SenderUserBean getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(SenderUserBean senderUser) {
        this.senderUser = senderUser;
    }

    public ReceiverUserBean getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(ReceiverUserBean receiverUser) {
        this.receiverUser = receiverUser;
    }

    public static class SenderUserBean {
        /**
         * id : 310
         * name : تاجر
         * image : clients/310_mg_20210103_102317304.jpg__5ff17f0213164_.jpg
         */

        private int id;
        private String name;
        private String image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class ReceiverUserBean {
        /**
         * id : 298
         * name : مشهور 1
         * image : clients/298_3.jpg__5ff718322e8b9_.jpg
         */

        private int id;
        private String name;
        private String image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
