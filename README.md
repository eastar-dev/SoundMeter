# What is SoundMeter?

SoundMeter에서는 마이크에서 수집되는 소음을 감지 하여 화면에 표시 합니다.

이 코드는 예전에 만들어진 코드를 코틀린으로 변환 한것입니다.


이 앱은 다음과 같은 기술을 사용하였습니다.

1. coroutines
2. MediaRecorder
3. animator
4. LifecycleObserver
5. AudioPermission


이 앱은 다음과 같은 순서로 작동합니다.
1. 시작
2. 권한요청
3. 소리감지 및 화면표시
4. pause 상태에서 중지
5. resume 상태에서 재시작


이 앱의 주요특징
1. resume 상태에서만 작동하게 하기위해 LifecycleObserver와 coroutines 을 사용했습니다.

<br/><br/>



## Demo


![Screenshot](https://raw.githubusercontent.com/eastar-dev/SoundMeter/master/release/demo.webp)




## Thanks 





<br/><br/>


## License 
 ```code
Copyright 2016 eastar Jeong

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