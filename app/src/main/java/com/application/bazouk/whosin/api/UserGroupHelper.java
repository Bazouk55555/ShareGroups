package com.application.bazouk.whosin.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.application.bazouk.whosin.models.User;
import com.application.bazouk.whosin.models.UserGroup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.TAG;

public class UserGroupHelper {

    private static final String COLLECTION_NAME = "users_groups";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<DocumentReference> createUserGroup(UserGroup userGroupToCreate) {
        Map<String, Object> userGroupMap = new HashMap<>();
        userGroupMap.put("usernames", userGroupToCreate.getUsernames());
        userGroupMap.put("names", userGroupToCreate.getNames());
        userGroupMap.put("is_present", userGroupToCreate.getIsPresent());
        userGroupMap.put("name_of_the_group", userGroupToCreate.getNameOfTheGroup());
        return getUsersCollection().add(userGroupMap);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUserGroup(String idGroup){
        return UserGroupHelper.getUsersCollection().document(idGroup).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUserGroupPresence(String uid, List<Boolean> isPresent) {
        return UserGroupHelper.getUsersCollection().document(uid).update("is_present",isPresent);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserGroupHelper.getUsersCollection().document(uid).delete();
    }

}
