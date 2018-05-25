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
    compile 'com.github.abdularis:CircularImageView:v1.2'
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
    app:avatarBackgroundColor="@color/colorAccent"
    app:text="A"
    app:textSize="22sp"/>

<com.github.abdularis.civ.AvatarImageView
    android:layout_width="60dp"
    android:layout_height="60dp"
    app:highlightEnable="false"
    app:strokeColor="#64B5F6"
    app:strokeWidth="1dp"
    app:avatarBackgroundColor="#8E24AA"
    app:text="B"
    app:textSize="22sp"/>

<com.github.abdularis.civ.AvatarImageView
    android:layout_width="60dp"
    android:layout_height="60dp"
    app:avatarBackgroundColor="#FDFD00"
    app:text="C"
    app:textSize="22sp"
    app:textColor="#3d3d01"/>

<com.github.abdularis.civ.AvatarImageView
    android:layout_width="60dp"
    android:layout_height="60dp"
    android:src="@drawable/figure2"
    app:strokeColor="#1976D2"
    app:strokeWidth="1dp"
    app:state="IMAGE"
    app:avatarBackgroundColor="#1976D2"
    app:text="B"
    app:textSize="22sp"/>
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
        <attr name="avatarBackgroundColor" format="color"/>
        <attr name="textSize" format="dimension"/>
        <attr name="textColor" format="color"/>
        <attr name="text" format="string"/>
        <attr name="state" format="enum">
            <enum name="INITIAL" value="1"/>
            <enum name="IMAGE" value="2"/>
        </attr>
    </declare-styleable>
</resources>
~~~
