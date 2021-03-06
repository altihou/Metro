简票
========================
![img](https://github.com/backkomyoung/Metro/blob/master/app/src/main/res/mipmap-xxxhdpi/launcher.png)

一个Material Design风格的地铁购票APP

本猿大四学生，这是在暑假用了一个月时间完成的一个参赛作品，现开源，主要用于学习，欢迎Issues！<br>

主要功能是手机APP线上购地铁票，生成二维码供取票，同时还可以用NFC进站（需要手机支持NFC功能）。我只写了客户端的代码，模拟地铁口进站闸机的是我另一个同学写的，代码就不贴了。其中用了主流的MVP+Retrofit+RxJava等框架实现，模拟了西安的地铁路线，后台是我参赛团队中的一同学写的，接口地址我就不给了，有点乱（反正你们也没有接口文档 - -）<br>

下载apk [传送门](https://github.com/backkomyoung/Metro/blob/master/app/app-release.apk)

#### 主要界面截图：
<p>
<a href="https://github.com/backkomyoung/Metro/blob/master/screenshots/device-sony-station.png"><img src="https://github.com/backkomyoung/Metro/blob/master/screenshots/device-sony-station.png" width="32%"/></a>  
&nbsp;&nbsp;&nbsp;&nbsp;
<a href="https://github.com/backkomyoung/Metro/blob/master/screenshots/device-sony-ticket.png"><img src="https://github.com/backkomyoung/Metro/blob/master/screenshots/device-sony-ticket.png" width="32%"/></a>  
<br>
<a href="https://github.com/backkomyoung/Metro/blob/master/screenshots/device-sony-order.png"><img src="https://github.com/backkomyoung/Metro/blob/master/screenshots/device-sony-order.png" width="32%"/></a>  
&nbsp;&nbsp;&nbsp;&nbsp;
<a href="https://github.com/backkomyoung/Metro/blob/master/screenshots/device-sony-mine.png"><img src="https://github.com/backkomyoung/Metro/blob/master/screenshots/device-sony-mine.png" width="32%"/></a>  
</p>

#### 主要用到的第三方库：

* [RxJava](https://github.com/ReactiveX/RxJava)
* [Retrofit](https://github.com/square/retrofit)
* [Okhttp](https://github.com/square/okhttp)
* [Rxpermissions](https://github.com/tbruyelle/RxPermissions)
* [Zxing](https://github.com/zxing/zxing)
* [Glide](https://github.com/bumptech/glide)
* [Butterknife](https://github.com/JakeWharton/butterknife)
* [Gson](https://github.com/google/gson)
* [BottomBar](https://github.com/roughike/BottomBar)
* [CircleImageview](https://github.com/hdodenhof/CircleImageView)
* [DateTimePicker](https://github.com/flavienlaurent/datetimepicker)
* [Crop](https://github.com/Skykai521/android-crop-master)

### LICENSE
```
The MIT License (MIT)

Copyright (c) 2016 Backkom 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

