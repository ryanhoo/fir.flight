# fir.flight

<img src="materials/firflight-demo.gif" align="right" width="220" hspace="60" vspace="20" alt="fir.flight demo gif">

[![Travis](https://travis-ci.org/ryanhoo/fir.flight.svg?branch=develop)](https://travis-ci.org/ryanhoo/fir.flight)
[![license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/ryanhoo/fir.flight#license)
[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

**fir.flight** is an android version [Test Flight](https://developer.apple.com/testflight/) by using the testing app distribution service provided by [fir.im](http://fir.im). With its help, you will be able to manage your apps' installation and updates more easily. Also **fir.flight** frees you from notifying your testers again and again.

**Apk is available on [Google Play Store](https://play.google.com/store/apps/details?id=io.github.ryanhoo.firFlight) and [fir.im](https://fir.im/firflight).**

<a href="https://play.google.com/store/apps/details?id=io.github.ryanhoo.firFlight&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" alt="Get it on Google Play" width="160" /> </a>

Also, fir.flight can be a wonderful project for beginners. It follows the software development procedure strictly. Here you will be able to learn

- how software is being created from scratch.
- how to use open APIs to build your own app.
- how to use popular techniques to boost app development(RxJava, Retrofit, MVP pattern, etc...).
- much more...

## Architecture

This project takes the MVP pattern from Google's [TODO-MVP](https://github.com/googlesamples/android-architecture/tree/todo-mvp/). But the implementation might be a little bit different from the original version.

<img src="materials/mvp-architecture.png" alt="mvp architecture" width="500" />

## Design

This project also provides an associated sketch design. It's available on Dropbox.

- [fir.flight.sketch](https://www.dropbox.com/s/8340stkzcxrvdss/fir.flight.sketch?dl=0)
- The missing [Roboto Fonts](https://www.dropbox.com/sh/5xl7m7scwoalnwa/AAAkNXH-Jb062jj9ZfaTVIsTa?dl=0)

<img src="materials/Artboard-Overview-v2.0.png" alt="Sketch Artboard Overview" width="500" />

## To do list

- **Features**
 - [ ] Auto update
 - [ ] Search
 - [ ] Add app by short link
 - [ ] Add app by qrcode
 - [ ] Try out without sign in
 - [ ] Permission on Android 6.0(M)
 - [ ] Accessibility: auto install

- **Code Improvements**
 - [ ] Dagger 2(Dependency Injection)
 - [ ] ButterKnife 8.0
 - [ ] Unit/UI Tests
 - [ ] Refactor(Model)

## Acknowledgements

Thanks to these projects and libraries:

**Libraries**

- [RxJava](https://github.com/ReactiveX/RxJava)
- [RxAndroid](https://github.com/ReactiveX/RxAndroid)
- [Gson](https://github.com/google/gson)
- [Retrofit](https://github.com/square/retrofit)
- [Glide](https://github.com/bumptech/glide)
- [Butter Knife](https://github.com/JakeWharton/butterknife)
- [SQLBrite](https://github.com/square/sqlbrite)
- [Stetho](http://github.com/facebook/stetho)
- [Calligraphy](https://github.com/chrisjenx/Calligraphy)

**Design**

- [Material icons](https://design.google.com/icons/)

## License

> The MIT License (MIT)
>
> Copyright (c) 2016 Ryan Hoo

> Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

> The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

> THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
