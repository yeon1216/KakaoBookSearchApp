# How to build #

빌드 전, 'secret.properties' 파일을 생성해주세요. 그리고 다음과 같이 API KEY 값을 추가해야 합니다.
```
# secret.properties
kakao.api.token="{KAKAO_API_TOKEN}
```

# 기능, 사용 기술 등 #
- 기능 : 책 검색, 즐겨찾기
- 사용기술 : Compose, Coroutine, Hilt, Room, Paging3, Compose Stable, Flow, Jetpack
- API : https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-book
