# madcamp_w1
# **당신에게 완벽한 카페를 찾아드립니다!** 🙂

---

커픽은 다양한 목적에 맞춘 카페를 손쉽게 탐색할 수 있는 카페 추천 어플입니다. 데이트를 위한 분위기 좋은 카페부터, 조용히 공부할 수 있는 카페, 그리고 애견 동반이 가능한 카페까지! 

***1.***	**목적별 카페 추천** 🗂️

데이트 💑, 공부 📚, 애견 카페 🐾 등 다양한 테마로 구성된 카페 순위를 확인하고, 자신의 목적에 맞는 완벽한 공간을 쉽게 찾으세요.

***2.***	**리뷰 갤러리** 🖼️

다른 이용자들이 남긴 생생한 리뷰와 사진을 한눈에 확인하여 카페의 분위기와 특징을 더 직관적으로 알 수 있습니다.

***3.***	**즐겨찾기 기능** ⭐

마음에 드는 카페를 저장해 두고, 언제든지 빠르게 접근할 수 있는 개인 즐겨찾기 리스트를 만들어보세요.

☕ **커픽과 함께 카페 탐방의 새로운 즐거움을 느껴보세요!** 🥳

# 📲 앱 시작 화면 및 로그인 페이지

![시작화면.gif](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/c254234e-7e47-4a54-ab55-6a16a889a666/%E1%84%89%E1%85%B5%E1%84%8C%E1%85%A1%E1%86%A8%E1%84%92%E1%85%AA%E1%84%86%E1%85%A7%E1%86%AB.gif)

![로그인.gif](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/a7cb2da3-dc5c-4803-b130-506ab1c3448d/%E1%84%85%E1%85%A9%E1%84%80%E1%85%B3%E1%84%8B%E1%85%B5%E1%86%AB.gif)
---

![시작화면.gif](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/c254234e-7e47-4a54-ab55-6a16a889a666/%E1%84%89%E1%85%B5%E1%84%8C%E1%85%A1%E1%86%A8%E1%84%92%E1%85%AA%E1%84%86%E1%85%A7%E1%86%AB.gif)

![로그인.gif](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/a7cb2da3-dc5c-4803-b130-506ab1c3448d/%E1%84%85%E1%85%A9%E1%84%80%E1%85%B3%E1%84%8B%E1%85%B5%E1%86%AB.gif)

- 사용자가 이메일과 비밀번호를 입력하면 실시간으로 입력 필드를 감지하여 로그인 버튼 활성화 여부를 결정
- 로그인 버튼을 클릭하면 입력된 이메일과 비밀번호를 기반으로 `Firebase Authentication`을 사용해 인증을 시도
- 인증이 성공하면 로그인 성공 메시지를 표시하고, 메인 화면(`MainActivity`)으로 이동하며 현재 화면은 종료됩니다. 인증이 실패하면 오류 메시지를 표시하며, 실패 이유를 로그로 기록
- 회원가입 버튼을 클릭하면 회원가입 화면(`RegisterActivity`)으로 이동

# 🗂️ Tab1 (Coffic) : 상황에 맞는 카페 순위 목록

---

### 상황별 카페 순위를 보여주는 탭으로, 사용자가 각 카페의 정보를 확인하고 자신의 목적에 맞게 데이터를 정렬하거나 Pick을 선택하여 선호도를 표시할 수 있는 화면

![Screen_Recording_20250101_201953_Phone.gif](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/88ef6dcd-fc1e-4101-be08-437b28c025b7/Screen_Recording_20250101_201953_Phone.gif)

![Screen_Recording_20250101_202405_Coffic.gif](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/79f53be0-3b30-4104-9246-e2857e11d567/Screen_Recording_20250101_202405_Coffic.gif)

- 카페 데이터 수집 및 로드
    - `Tab1_DataManager`를 통해 로컬 JSON 파일에서 카페 데이터를 로드. JSON 파일은 파이썬의 `selenium` 라이브러리를 이용하여 크롤링. JSON 파일에서 각 카페의 이름, 주소, 운영 시간, 연락처, 이미지 URL 등의 정보를 `CafeData`객체로 구성
- 드롭다운 항목 선택에 따라 화면 정렬
    - `Spinner`를 통해 **Study Pick**, **Date Pick**, **Pet Pick** 등의 목적별로 카페 순위를 정렬. 이때, 선택된 정렬 기준에 따라 `PickManager`에서 데이터를 가져와 정렬 후 `RecyclerView`에 반영
- 카페 목록 구현
    - 각 카페 정보를 `Tab1Adapter`를 통해 `RecyclerView`에 표시. 추가로 `Glide`를 활용해 카페 이미지를 로드
    - 전화 아이콘 클릭 시 `Intent.ACTION_DIAL`을 통해 전화 앱을 실행
    - 하트 버튼 클릭 시 `PickManager`를 통해 상태를 변경하고 버튼 이미지를 업데이트
- Pick(좋아요) 기능 구현
    - `PickManager`를 통해 `SharedPreferences`에 각 카페의 Pick 데이터를 저장하거나 로드
    - 버튼 클릭 시 선호도(Pick) 상태를 토글하고, 변경된 데이터를 `RecyclerView`에 즉시 반영
    - `SharedPickManager`: `PickManager`의 상태 변화를 감지하는 리스너를 관리하며, Pick 상태 변경 시 리스너를 통해 `RecyclerView`를 업데이트 → 이후 tab2에서 이를 함께 사용할 수 있도록 구현
- 사진 클릭 → 카페 정보 페이지
    - `RecyclerView`의 이미지 버튼(카페 사진)을 클릭하면 `ViewCafeDialogFragment` 다이얼로그가 표시
    - 다이얼로그는 `SharedPreferences`에서 해당 카페의 이름, 주소, 운영 시간, 연락처, 이미지를 불러와 표시
    - **Glide**를 사용해 이미지를 정사각형(1:1 비율)으로 로드하여 깔끔하게 보여줍니다.
    - 다이얼로그 상단에는 뒤로가기 버튼이 있어 쉽게 종료 가능

# 👤 Tab2 (My-pick) : 마이 페이지

---

### 사용자가 프로필 사진 설정, 아이디  확인, 탭을 활용해 자신만의 카페 목록, 게시물 확인

- **프로필 사진 설정**
    - 사용자가 첫 실행 시 사진을 선택하여 프로필 이미지를 설정할 수 있도록 구현
    - 갤러리 호출에 `Intent.ACTION_PICK`을 활용
    - 선택한 이미지는 `Glide` 라이브러리를 사용하여 UI에 미리보기로 표시
- **아이디 표시**
    - 사용자가 로그인하면 입력한 이메일을 아이디로 표시하도록 설정
    - 로그인 시 전달된 데이터를 통해 프로필 화면에 실시간으로 아이디를 업데이트
- **탭 디자인**
    - `LinearLayout`을 활용하여 탭 아이콘을 화면 하단 중앙에 배치
- **탭 기능 연결**
    - 각 탭 클릭 시 `RecyclerView`와 연결하여 관련된 화면 및 데이터 표시
    - 탭 1은 좋아요 누른 카페 리스트 화면, 탭 3은 게시물 추가 화면으로 연결하여 직관적인 네비게이션 제공.
- **RecyclerView를 활용한 게시물 표시**
    - 각 탭은 `RecyclerView`를 통해 데이터를 표시
    - 탭 1과 탭 3에서 구현된 게시물 추가 및 표시 기능을 메인 화면에서도 활용할 수 있도록 연동
- **데이터 저장 및 유지**
    - 게시물 추가 시 `SharedPreferences`를 활용하여 데이터 저장 
    : tab1의 pick의 정보가 저장 및 연동
    - 앱 재실행 시에도 저장된 게시물을 유지하여 데이터 유실 방지

# 📝 Tab3 (U-pick) : 카페 리뷰 갤러리

---

### 사용자가 게시물을 추가, 확인, 저장할 수 있는 기능

![Screen_Recording_20250101_205135_Coffic.gif](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/7ab8cbbd-5768-4e6f-88b0-bf66ae0f2f14/Screen_Recording_20250101_205135_Coffic.gif)

- 게시물 추가 버튼
    - `ImageButton` 을 활용하여 하단에 추가 버튼 배치 → 클릭 시 `DialogFragment` 표시
- 갤러리에 사진 추가
    - `Intent.ACTION_PICK` 을 사용하여 기기 갤러리 호출 → 사용자가 선택한 이미지 `Glide` 로 미리보기 제공
- 텍스트와 이미지 저장
    - `SharedPreferences` 를 활용하여 텍스트와 이미지 URI 저장 → 앱 재실행 시 데이터 유지
    - 게시물 추가시 이를 `SharedPreferences`에 저장하기 전, `RecyclerView` 에 실시간으로 표시하도록 구현
- RecyclerView에 게시물 표시
    - `RecyclerView` 의 각 아이템 클릭시 `DialogFragment` 표시 → `SharedPreferences` 에서 해당 데이터 로드 및 표시
