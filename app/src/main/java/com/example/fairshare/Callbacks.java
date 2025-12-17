package com.example.fairshare;

import java.util.List;

public interface Callbacks {

    interface LoginCallback {
        void onSuccess(UserModel user);
        void onFailure(String error);
    }

    interface GroupCallback {
        void onSuccess(List<UserModel> groups);
        void onFailure(String error);
    }
}

