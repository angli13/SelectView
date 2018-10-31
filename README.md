# SelectView
A simple animated view for android, that lets a user select an option. Written in Kotlin.

![](https://media.giphy.com/media/RN7rXdjefg7ewoNpb6/giphy.gif)


# Usage
Add it to your xml file, all options are optional:
```
<com.fangli.selectview.SelectView  
  android:id="@+id/selectView"  
  android:layout_width="match_parent"  
  android:layout_height="70dp"   
  android:layout_margin="16dp"  
  android:minHeight="50dp" 
  app:fontSize="22sp"  
  app:orientation="horizontal"  
  app:notSelectedColor="@color/gray"   
  app:selectedDrawable="@drawable/star"  
  app:textSelectedColor="@color/blue"  
  app:selectedScaleType="fitCenter" 
  />
```

In you code:
````
//You set the option by using the changeOptions method
selectView.changeOptions(listOf("One", "Two", "Three"))

//Add a listener if you want to listen for selections
selectView.listener = object: SelectViewListener {  
    override fun OnOptionSelected(position: Int, text: String) {  
        //You get the position and the value of the selection
    }  
}

//You can get the selection at any time
selectView.getSelectedText()
selectView.getSelectedPosition()

//You can also set the selection programatically
selectView.setSelected(1)
````

# Install
````
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
````
````
dependencies {
	        implementation 'com.github.angli13:SelectView:0.1.0'
	}
  ````

