# ProgressRoundButton
A Download Button with Progress

## Demo

![demo](http://ww1.sinaimg.cn/large/0060lm7Tgw1ewzhnz6143g30600aoe82.gif)

## Usage

### step1

#### gradle

```groovy
 dependencies {
     compile fileTree(dir: 'libs', include: ['*.jar'])
     compile 'com.android.support:appcompat-v7:23.0.1'
     compile 'com.xiaochendev.progressroundbtn:library:0.9.1'
 }
 ```
#### step2 

you can define the button in xml like this:

```xml
<com.xiaochen.progressroundbutton.AnimDownloadProgressButton
        android:id="@+id/anim_btn"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        app:progressbtn_backgroud_color="@android:color/holo_orange_light"
        app:progressbtn_backgroud_second_color="@android:color/holo_green_light"/>
```

the Customized properties are in the follow table:
 
 | Property        | Format           | Default  |
 | ------------- |:-------------:| -----:|
 
 |progressbtn_radius  |float  |half of the button height  |
 |progressbtn_backgroud_color|color | #6699ff |
 |progressbtn_backgroud_second_color|color|Color.LTGRAY|
 |progressbtn_text_color|color|progressbtn_backgroud_color|
 |progressbtn_text_covercolor|color|Color.WHITE|
  
 The follow picture make a clear explanation:
 
 ![show](http://ww4.sinaimg.cn/large/0060lm7Tgw1ex1yr2b9xjj30eg0go75n.jpg)

## About me 

I am a developer in China，If you have any idea about this project，please [contact me](mailto:cctanfujun@163.com)
,Thank you!