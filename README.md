## Facebook 연동

팀 프로젝트 첫 스플린트로 Facebook과 연동을 맡게 되어서 구현해 보았다.
지금 작업하고 있는 팀프로젝트를 예제로 사용했다.

### 1. 연동 사전 과정

1. https://developers.facebook.com/quickstarts/268254320295870/?platform=android 에 접속 해서 로그인을 하면 아래와 같은 화면이 뜬다.

 ![페북연동과정](http://i.imgur.com/WVZRISy.png)

 * Import SDK -> Add SDK -> App Info -> Key Hashes -> App Events -> Finished 의 과정으로 진행

**1. Import SDK**

build.gradle에서 아래의 코드를 추가해준다.

```
repositories {
            mavenCentral()
        }
        ...


dependencies {
   compile 'com.facebook.android:facebook-android-sdk:[4,5)'
 }
```


 **1.1 AppId 등록**

  페이스북 developers에 가입하면 App Id가 발급 되는데 생성한 App ID도 추가해줘야한다. App ID를 생성한 페이지로 가면 App ID를 쉽게 확인할 수 있다. 아래와 같이 App ID를 values 폴더 - strings.xml 파일에서 string value로 만들어 준다.
![AppID등록](http://i.imgur.com/uNs7NKs.png)

**1.2 Manifest등록**

* 퍼미션 추가
```
<uses-permission android:name="android.permission.INTERNET"/>
...

<application
    <meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>

    <activity android:name="com.facebook.FacebookActivity"
           android:configChanges=
               "keyboard|keyboardHidden|screenLayout|screenSize|orientation"
           android:theme="@android:style/Theme.Translucent.NoTitleBar"
           android:label="@string/app_name" />
```
추가하면 아래와 같다.

![Facebook Menifest](http://i.imgur.com/kwvSw6S.png)


**2. Add SDK**

안드로이드 프로젝트의 패키지와 사용한 클래스를 등록해준다.

**3.Key Hashes**
```
keytool -exportcert -alias androiddebugkey -keystore %HOMEPATH%\.android\debug.keystore | openssl sha1 -binary | openssl base64
```
cmd창을 열어서 위의 명령어를 입력해야 하는데 그러기 위해선 Path설정이 되어있어야 한다.

keytool에 접근하기 위해선 java파일 bin폴더를 Path등록을 해줘야한다.
또한 openssl은 인증과정에 필요한 패스 설정이므로 openssl가 들어있는 폴더 또한 Path추가를 해준다.
존재하지 않는다면 구글링을 통해 다운 받아주자. 주의해야할 점은 같으 파일에 넣는다면 dll이 커지기 때문에 path설정을 2번 하더라도 꼭 다른 폴더에 넣어준다.
추가적으로 keystore, testkey도 안드로이드 스튜디오를 통해 받아 놔야 정확한 28자리 Path Key가 발급된다.

받은 28자리의 Path Key를 진행되고 있는 facebook developers 페이지에 넣어주자.

### 2. 안드로이드에서 구현

이제 설정이 모두 끝났고 코드를 구현한다.

1. .xml
```
<com.facebook.login.widget.LoginButton
       android:id="@+id/signinFacebook"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:layout_marginBottom="16dp"
       android:layout_marginTop="64dp"
       android:padding="0dp"/>
```
로그인을 할 수 있는 버튼을 참 쉽게 구현할 수 있다.

2. .java
* 선언
```
LoginButton signinFacebook; //페이스북에서 커스텀한 버튼인것 같다.
private CallbackManager callbackManager;
```

* onCreate()
```
  callbackManager = CallbackManager.Factory.create();

  //--------------Facebook Login-------------------
      signinFacebook = (LoginButton) findViewById(R.id.signinFacebook);
      signinFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));
      signinFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
          @Override
          public void onSuccess(LoginResult loginResult) {
              GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                  @Override
                  public void onCompleted(JSONObject object, GraphResponse response) {
                      Log.i("result", object.toString());
                      try {
                          Thread.sleep(1000);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                      Intent i  = new Intent(SignActivity.this, LobbyActivity.class);
                      startActivity(i);
                  }
              });
              Bundle parameters = new Bundle();
              parameters.putString("fields", "id,name,email,gender,birthday");
              graphRequest.setParameters(parameters);
              graphRequest.executeAsync();
          }

          @Override
          public void onCancel() {

          }

          @Override
          public void onError(FacebookException error) {
              Log.e("LoginErr",error.toString());
          }
      });
      //-----------------------------------------------
```
* Override함수 onActivityResult()
```
//---------Facebook Login------
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       callbackManager.onActivityResult(requestCode, resultCode, data);
   }
   //-----------------------------

```

정상적으로 로그인이 작동되는 것을 로그로 확인할 수 있다.
