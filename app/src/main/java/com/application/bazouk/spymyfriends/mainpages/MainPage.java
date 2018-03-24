package com.application.bazouk.spymyfriends.mainpages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.application.bazouk.spymyfriends.R;
import com.application.bazouk.spymyfriends.connectionpages.ConnectionPage;
import com.application.bazouk.spymyfriends.groupes.PresenceGroup;
import com.application.bazouk.spymyfriends.groupes.ShareGroup;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrien on 10/01/2018.
 */

public class MainPage extends AppCompatActivity {

    private final String GET_FIRST_NAME = "first_name";
    private final String GET_LAST_NAME = "last name";
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private List<String> listOfFriends;
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        listOfFriends = new ArrayList<>();
        setToolbar();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        final String firstName;
        final String lastName;
        if(preferences.getString(GET_LAST_NAME,"").isEmpty() && preferences.getString(GET_FIRST_NAME,"").isEmpty())
        {
            firstName = getIntent().getStringArrayExtra("LastNameAndFirstName")[0];
            lastName = getIntent().getStringArrayExtra("LastNameAndFirstName")[1];
            editor.putString(GET_FIRST_NAME, firstName);
            editor.putString(GET_LAST_NAME, lastName);
            editor.apply();
        }
        else
        {
            lastName = preferences.getString(GET_LAST_NAME,"");
            firstName = preferences.getString(GET_FIRST_NAME,"");
        }
        TextView welcomeTextView = (TextView) findViewById(R.id.welcome);
        welcomeTextView.setText(firstName+" "+lastName);

        findViewById(R.id.presence_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent presenceGroupIntent = new Intent(MainPage.this,PresenceGroup.class);
                presenceGroupIntent.putExtra("first_name",firstName);
                presenceGroupIntent.putExtra("last_name",lastName);
                startActivity(presenceGroupIntent);
            }
        });

        findViewById(R.id.share_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this,ShareGroup.class));
            }
        });

        /*if(AccessToken.getCurrentAccessToken()!=null || getIntent().getBooleanExtra("isConnectedToFacebook", false))
        {
            getMainPage();
        }
        else {
            findViewById(R.id.facebook_login_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbackManager = CallbackManager.Factory.create();
                    LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
                    loginButton.setReadPermissions(Arrays.asList(
                            "public_profile", "email", "user_friends"));
                    // Callback registration
                    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            MainPage.this.accessToken = loginResult.getAccessToken();
                            getMainPage();
                        }

                        @Override
                        public void onCancel() {
                            System.out.println("HERE I AM IN CANCEL");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            System.out.println("JE SUIS DANS ERROR");
                        }
                    });
                }
            });
        }
    }

    private void getMainPage()
    {
        LinearLayout linearLayoutOfMainPage = ((LinearLayout)(findViewById(R.id.main_layout_of_main_page)));
        linearLayoutOfMainPage.removeView(findViewById(R.id.facebook_login_button));
        LinearLayout mainPageLayout = (LinearLayout) ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.main_page_layout, null);
        linearLayoutOfMainPage.addView(mainPageLayout);
        findViewById(R.id.fb_login_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
                AccessToken.setCurrentAccessToken(null);
                Intent intent = new Intent(MainPage.this,ConnectionPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOfFriends.clear();
                GraphRequest requestt =
                        new GraphRequest(
                                accessToken,
                                "/me/taggable_friends",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        JSONObject obj = response.getJSONObject();
                                        JSONArray arr;
                                        try {
                                            arr = obj.getJSONArray("data");
                                            for(int i =0; i<arr.length();i++) {
                                                JSONObject oneByOne = arr.getJSONObject(i);
                                                listOfFriends.add(oneByOne.opt("id").toString());
                                            }
                                            System.out.println("Need a break");
                                        } catch (JSONException e) {
                                            System.out.println("EXCEPTION!!!!!!!!!");
                                            e.printStackTrace();
                                        }

                                    }
                                }
                        );
                Bundle parameters = new Bundle();
                parameters.putInt("limit", 5000);
                parameters.putString("fields","id,name,first_name,email");
                requestt.setParameters(parameters);
                requestt.executeAsync();
            }
        });

        findViewById(R.id.start2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        try {
                            JSONArray jsonArrayFriends = jsonObject.getJSONObject("friendlist").getJSONArray("data");
                            JSONObject friendlistObject = jsonArrayFriends.getJSONObject(0);
                            String friendListID = friendlistObject.getString("id");
                            myNewGraphReq(friendListID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                /*GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                                try {
                                    myId = jsonObject.getString("id");
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });*/
                /*GraphRequest requestt =
                        new GraphRequest(
                                accessToken,
                                //"/"+listOfFriends.get(1).toString()+"/taggable_friends",
                                "/10214043234768544/taggable_friends",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        JSONObject obj = response.getJSONObject();
                                        JSONArray arr;
                                        try {
                                            arr = obj.getJSONArray("data");
                                            for(int i =0; i<arr.length();i++) {
                                                JSONObject oneByOne = arr.getJSONObject(i);
                                                listOfFriends.add(oneByOne.opt("name").toString());
                                            }
                                        } catch (JSONException e) {
                                            System.out.println("EXCEPTION!!!!!!!!!");
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );

                Bundle param = new Bundle();
                param.putString("fields", "friendlist,members");
                graphRequest.setParameters(param);
                graphRequest.executeAsync();
                /*Bundle bundle = new Bundle();
                bundle.putString("fields","id");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
                Bundle parameters = new Bundle();
                parameters.putInt("limit", 5000);
                parameters.putString("fields", "name");
                requestt.setParameters(parameters);
                requestt.executeAsync();
            }
        });*/
    }

    private void setToolbar()
    {
        findViewById(R.id.results).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this,AllTheGroupsPage.class));
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this,ProfilePage.class));
            }
        });

        findViewById(R.id.disconnection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();
                startActivity(new Intent(MainPage.this,ConnectionPage.class));
            }
        });
    }

    /*private void myNewGraphReq(String friendlistId) {
        final String graphPath = "/"+friendlistId+"/members/";
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = new GraphRequest(token, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                JSONObject object = graphResponse.getJSONObject();
                try {
                    JSONArray arrayOfUsersInFriendList= object.getJSONArray("data");
                    JSONObject user = arrayOfUsersInFriendList.getJSONObject(0);
                    String usersName = user.getString("name");
                    //String id = user.get
                    System.out.println("BREAK");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle param = new Bundle();
        param.putString("fields", "name");
        request.setParameters(param);
        request.executeAsync();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
