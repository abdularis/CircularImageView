# Android Circular Image View
[![](https://jitpack.io/v/abdularis/CircularImageView.svg)](https://jitpack.io/#abdularis/CircularImageView)

This library provides you circle and avatar imageview for android. it automatically scale and center a bitmap based on the size of the view but does not copy the bitmap itself.
> this project was inspired by [hdodenhof CircleImageView](https://github.com/hdodenhof/CircleImageView)

## Screenshot
![](demo.gif)

![](screenshots/screenshot.png)

## Setup
- **Step 1** Add repository into root build.gradle

~~~xml
allprojects {
    repositories {
    ...
    maven {
        url 'https://jitpack.io' }
    }
}
~~~

- **Step 2** Add library dependency into app build.gradle

~~~xml
dependencies {
    compile 'com.github.abdularis:CircularImageView:v1.1'
}
~~~

## Usage
Check also the sample app

- **Creating circle image view**

![](screenshots/fig2.gif)

~~~xml
<com.github.abdularis.civ.CircleImageView
    android:layout_width="100dp"
    android:layout_height="170dp"
    android:src="@drawable/figure"
    app:highlightColor="#80fb1743"
    app:strokeColor="@color/colorAccent"
    app:strokeWidth="2px"/>

<com.github.abdularis.civ.CircleImageView
    android:layout_width="160dp"
    android:layout_height="160dp"
    android:src="@drawable/figure2"
    android:onClick="onCircleImageClick"
    app:strokeColor="@color/colorAccent"/>
~~~

- **Creating avatar**

![](screenshots/fig1.gif)

You can choose either for avatar_state, IMAGE or INITIAL to show the first letter (default is INITIAL)

~~~xml
<com.github.abdularis.civ.AvatarImageView
    android:layout_width="60dp"
    android:layout_height="60dp"
    android:src="@drawable/figure"
    android:onClick="onAClick"
    app:strokeWidth="1dp"
    app:strokeColor="@android:color/white"
    app:avatar_backgroundColor="@color/colorAccent"
    app:avatar_text="A"
    app:avatar_textSize="22sp"/>

<com.github.abdularis.civ.AvatarImageView
    android:layout_width="60dp"
    android:layout_height="60dp"
    app:highlightEnable="false"
    app:strokeColor="#64B5F6"
    app:strokeWidth="1dp"
    app:avatar_backgroundColor="#8E24AA"
    app:avatar_text="B"
    app:avatar_textSize="22sp"/>

<com.github.abdularis.civ.AvatarImageView
    android:layout_width="60dp"
    android:layout_height="60dp"
    app:avatar_backgroundColor="#FDFD00"
    app:avatar_text="C"
    app:avatar_textSize="22sp"
    app:avatar_textColor="#3d3d01"/>

<com.github.abdularis.civ.AvatarImageView
    android:layout_width="60dp"
    android:layout_height="60dp"
    android:src="@drawable/figure2"
    app:strokeColor="#1976D2"
    app:strokeWidth="1dp"
    app:avatar_state="IMAGE"
    app:avatar_backgroundColor="#1976D2"
    app:avatar_text="B"
    app:avatar_textSize="22sp"/>
~~~

Java on view click listener

~~~java
// on click listener for avatar view 'A'
public void onAClick(View view) {
    AvatarImageView a = (AvatarImageView) view;
    if (a.getState() == AvatarImageView.SHOW_INITIAL) {
        a.setState(AvatarImageView.SHOW_IMAGE);
    } else {
        a.setState(AvatarImageView.SHOW_INITIAL);
    }
}
~~~


## XML Attributes

These are all attributes that you can use to customize the appearance of CircleImageView and AvatarImageView

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="CircleImageView">
        <attr name="strokeColor" format="color"/>
        <attr name="strokeWidth" format="dimension"/>
        <attr name="highlightEnable" format="boolean"/>
        <attr name="highlightColor" format="color"/>
    </declare-styleable>

    <declare-styleable name="AvatarImageView">
        <attr name="avatar_backgroundColor" format="color"/>
        <attr name="avatar_textSize" format="dimension"/>
        <attr name="avatar_textColor" format="color"/>
        <attr name="avatar_text" format="string"/>
        <attr name="avatar_state" format="enum">
            <enum name="INITIAL" value="1"/>
            <enum name="IMAGE" value="2"/>
        </attr>
    </declare-styleable>
</resources>
~~~
