# CompoundIconTextView

An android library that is able to set a vector drawable at text view pre-Lollipop.

[![Platform](http://img.shields.io/badge/platform-android-brightgreen.svg?style=flat)](http://developer.android.com/index.html)
[![Language](http://img.shields.io/badge/language-java-orange.svg?style=flat)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![License](http://img.shields.io/badge/license-apache2.0-lightgrey.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Download CompoundIconTextView](https://api.bintray.com/packages/aakira/maven/compound-icon-textview/images/download.svg)](https://bintray.com/aakira/maven/compound-icon-textview/_latestVersion)

## Preview

![PREVIEW][preview]

## Features

* Set a vector drawable at text view pre-Lollipop

## Usage

### Gradle

You should set this line in your gradle file if you set colors to vector drawable pre-Lollipop.

```gradle
android {
    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }
}
```

### Code

```Java

CompoundIconTextView tv = (CompoundIconTextView) findViewById(R.id.compoundIconTextView);

// set icon drawable
tv.setVectorDrawableTop(R.drawable.ic_android_black_24dp);
tv.setVectorDrawableLeft(R.drawable.ic_android_black_24dp);

// set icon color
tv.setIconColorResource(R.color.colorPrimary);

// set icon size
tv.setIconSizeResource(R.dimen.icon_size, R.dimen.icon_size);
tv.setIconSize(32, 32);

// clear icon
tv.setVectorDrawableRight(CompoundIconTextView.UNDEFINED_RESOURCE);

```

### Xml

```xml

<?xml version="1.0" encoding="UTF-8"?>
 <com.github.aakira.compoundicontextview.CompoundIconTextView
     android:id="@+id/compoundIconTextView"
     android:layout_width="wrap_content"
     android:layout_height="wrap_content"
     android:drawablePadding="4dp"
     android:gravity="center"
     android:text="Hello World!"
     android:textColor="#3F51B5"
     android:textSize="16sp"
     app:cit_drawableLeft="@drawable/ic_android_black_24dp"
     app:cit_iconColor="#000"
     app:cit_iconHeight="16dp"
     app:cit_iconWidth="16dp" />
```

### Attributes

|attribute name|description|
|:-:|:-:|
|cit_drawableLeft|Sets a drawable or vector drawable to left of TextView|
|cit_drawableTop|Sets a drawable or vector drawable to top of TextView|
|cit_drawableBottom|Sets a drawable or vector drawable to bottom of TextView|
|cit_drawableRight|Sets a drawable or vector drawable to right of TextView|
|cit_drawableStart|Sets a drawable or vector drawable to start of TextView (for RTL)|
|cit_drawableEnd|Sets a drawable or vector drawable to end of TextView (for RTL)|
|cit_iconWidth|Sets a width of icon|
|cit_iconHeight|Sets a width of icon|
|cit_iconColor|Sets a icon color|

## Setup

### Gradle

Add the dependency in your `build.gradle`

```groovy
buildscript {
	repositories {
		jcenter()
	}
}

dependencies {
	compile 'com.github.aakira:compound-icon-textview:1.2.1@aar'
}
```
## Using libraries

* [Material icons](https://material.io/icons/#ic_cloud_download)

## Author

### Akira Aratani

* Twitter
 - [@_a_akira](https://twitter.com/_a_akira)
* Mail
 - developer.a.akira@gmail.com

## License

```
Copyright (C) 2017 A.Akira

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[preview]: /arts/preview.jpg
