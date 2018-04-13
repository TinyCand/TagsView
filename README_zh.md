# TagsView

TagsView是一个可以自动换行的ViewGroup, 用于展示不定长的item, 用法类似于GridView, 可用来展示人物或物品的标签.

##效果图

![Alt screenshot](TagsViewDemo_zh.jpg)

## 用法
### 引入lib

在build.gradle添加依赖:

```groovy
dependencies {
    compile 'com.tinycand.mobile:TagsView:0.0.1-Beta'
}
```

### 布局

在布局文件中添加：

```xml
    <com.tinycand.tagsview.TagsView
        android:id="@+id/tagsView_all"
        app:horizontalSpacing="10dp"
        app:verticalSpacing="10dp"
        app:stretchMode="spacingWidth_auto"
        android:paddingLeft="16dp" android:paddingRight="16dp"
        android:paddingTop="5dp" android:paddingBottom="2dp"
        android:layout_width="match_parent" android:layout_height="wrap_content"
        />
```


###Java 代码

在Activity或Fragment中定义并使用TagsView:

    TagsView mMyTagsView = (TagsView) findViewById(R.id.tagsView_all);
    mMyTagsView.setStretchMode(TagsView.STRETCH_SPACING_AUTO);
    mMyTagsView.setAdapter(new BaseAdapter(){});


#License
Copyright 2018 TinyCand

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

See LICENSE file for details.
